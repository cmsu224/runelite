package net.runelite.client.plugins.maz.plugins.soundReplace;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@PluginDescriptor(
        name = "Sound Replacer",
        description = "Replace ingame sound",
        tags = {"maz","sound", "ben", "mitch", "ree"},
        enabledByDefault = false
)

public class soundReplacePlugin extends Plugin
{
    @Inject
    private soundReplaceConfig config;

    @Inject
    private soundReplaceEffectOverlay effectOverlay;

    @Inject
    private soundReplaceAreaOverlay areaOverlay;

    @Inject
    private ConfigManager configManager;

    @Inject
    private OverlayManager overlayManager;

    @Provides
    soundReplaceConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(soundReplaceConfig.class);
    }

    Set<Integer> replaceEffectSound = new HashSet<>();
    Set<Integer> replaceAreaSound = new HashSet<>();

    LinkedList<Integer> effectSoundsList = new LinkedList<>();
    LinkedList<Integer> areaSoundsList = new LinkedList<>();

    Clip mitch;
    Clip ben;

    @Override
    protected void startUp()
    {
        enableOverlay(config.soundDebug());
        updateEffectFilter();
        updateAreaFilter();
        loadVoiceOvers();
    }

    @Override
    protected void shutDown()
    {
        enableOverlay(false);
        unloadVoiceOvers();
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {
        if(event.getGroup().equals("soundreplace"))
        {
            switch (event.getKey())
            {
                case "soundDebug":
                    enableOverlay(config.soundDebug());
                    break;
                case "replacelistEffect":
                    updateEffectFilter();
                    break;
                case "replacelistArea":
                    updateAreaFilter();
            }
        }
    }


    @Subscribe
    public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed)
    {
        switch (config.soundMode())
        {
            case DISABLED:
                break;
            case MITCH:
                if (replaceEffectSound.isEmpty())
                {
                    break;
                }
                if (replaceEffectSound.contains(soundEffectPlayed.getSoundId())){
                    soundEffectPlayed.consume();
                    //Play MITCH
                    playSound(mitch);
                    break;
                }
                break;
            case BEN:
                if (replaceEffectSound.isEmpty())
                {
                    break;
                }
                if (replaceEffectSound.contains(soundEffectPlayed.getSoundId())){
                    soundEffectPlayed.consume();
                    //Play BEN
                    playSound(ben);
                    break;
                }
                break;
        }
        if (!soundEffectPlayed.isConsumed())
        {
            addEffectSound(soundEffectPlayed.getSoundId());
        }
    }

    @Subscribe
    public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed areaSoundEffectPlayed)
    {
        switch (config.soundMode())
        {
            case DISABLED:
                break;
            case MITCH:
                if (replaceAreaSound.isEmpty())
                {
                    break;
                }
                if (replaceAreaSound.contains(areaSoundEffectPlayed.getSoundId())){
                    areaSoundEffectPlayed.consume();
                    //Play MITCH
                    playSound(mitch);
                    break;
                }
                break;
            case BEN:
                if (replaceAreaSound.isEmpty())
                {
                    break;
                }
                if (replaceAreaSound.contains(areaSoundEffectPlayed.getSoundId())){
                    areaSoundEffectPlayed.consume();
                    //Play BEN
                    playSound(ben);
                    break;
                }
                break;
        }
        if(!areaSoundEffectPlayed.isConsumed())
        {
            addAreaSound(areaSoundEffectPlayed.getSoundId());
        }
    }

    private void updateEffectFilter()
    {
        replaceEffectSound = getNumbersFromConfig(config.replacelistEffect());
    }

    private void updateAreaFilter()
    {
        replaceAreaSound = getNumbersFromConfig(config.replacelistArea());
    }

    private Set<Integer> getNumbersFromConfig(String source)
    {
        return Arrays.stream(source.split(","))
                .map(String::trim)
                .filter(NumberUtils::isParsable)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    public void enableOverlay(boolean bool)
    {
        if (bool)
        {
            overlayManager.add(effectOverlay);
            overlayManager.add(areaOverlay);
        }
        else
        {
            overlayManager.remove(effectOverlay);
            overlayManager.remove(areaOverlay);
            effectSoundsList.clear();
            areaSoundsList.clear();
        }

    }

    private void addEffectSound(Integer sound)
    {
        effectSoundsList.addFirst(sound);
        if (effectSoundsList.size()>7)
            effectSoundsList.removeLast();
    }

    private void addAreaSound(Integer sound)
    {
        areaSoundsList.addFirst(sound);
        if (areaSoundsList.size()>7)
            areaSoundsList.removeLast();
    }

    private void playSound(Clip audioClip) {
        audioClip.setFramePosition(0);
        audioClip.loop(0);
    }

    private void loadVoiceOvers() {
        try {
            mitch = AudioSystem.getClip();
            loadSound(mitch, "mitch.wav");
        } catch (LineUnavailableException e) {
            log.warn("Failed to play audio clip", e);
        }
        try {
            ben = AudioSystem.getClip();
            loadSound(ben, "ben.wav");
        } catch (LineUnavailableException e) {
            log.warn("Failed to play audio clip", e);
        }
    }

    private void loadSound(Clip audioClip, String name) {
        InputStream in = getClass().getResourceAsStream(name);

        if (in == null) {
            log.warn("Missing audio file {}", name);
            return;
        }

        try (InputStream fileStream = new BufferedInputStream(in);
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileStream)) {
            audioClip.open(audioStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            log.warn("Failed to load audio file", e);
        }
    }

    private void unloadVoiceOvers() {
        mitch.stop();
        mitch.close();
        ben.stop();
        ben.close();
    }
}