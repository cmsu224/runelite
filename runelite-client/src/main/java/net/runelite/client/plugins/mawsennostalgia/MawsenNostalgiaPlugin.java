package net.runelite.client.plugins.mawsennostalgia;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@PluginDescriptor(
        name = "<html><font color=#FF0000>Mawsen Nostalgia",
        tags = {"mawsen", "maz", "nex"}
)
public class MawsenNostalgiaPlugin extends Plugin {

    private final Map<VoiceOver, Clip> voiceOverClips = new HashMap<>();

    @Inject
    private Client client;

    @Inject
    private MawsenNostalgiaConfig config;

    @Override
    protected void startUp() {
        loadVoiceOvers();
        log.debug("Loaded {} voice over sounds", voiceOverClips.size());
        updateVolumeGain(config.volumeGain());
    }

    @Override
    protected void shutDown() {
        unloadVoiceOvers();
    }

    @Provides
    MawsenNostalgiaConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(MawsenNostalgiaConfig.class);
    }

    @Subscribe
    public void onCommandExecuted(CommandExecuted command) {
        String[] arguments = command.getArguments();

        if (command.getCommand().equals("vo")) {

            if (arguments.length < 1) {
                return;
            }
            String voiceOverName = arguments[0].toUpperCase();

            try {
                VoiceOver voiceOver = VoiceOver.valueOf(voiceOverName);
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Playing voiceover " + voiceOver, null);

                playVoiceOver(voiceOver);
            } catch (IllegalArgumentException e) {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Unknown voiceover: " + voiceOverName, null);
            }
        } else if (command.getCommand().equals("vovol")) {

            if (arguments.length < 1) {
                return;
            }
            String volume = arguments[0].toUpperCase();

            try {
                float decibels = Float.parseFloat(volume);
                updateVolumeGain(decibels);
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Updated volume gain to: " + decibels + " Db", null);
            } catch (NumberFormatException e) {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Invalid number: " + volume, null);
            }
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }
        String text = Text.removeTags(event.getMessage());

        if (!text.startsWith("Nex: ")) {
            return;
        }

        VoiceOver voiceOver = VoiceOver.forTriggerLine(text.substring(5));

        if (voiceOver != null) {
            playVoiceOver(voiceOver);
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {

        if (event.getGroup().equals(MawsenNostalgiaConfig.GROUP)) {
            log.debug("Updating volume gain to {} Db", config.volumeGain());
            updateVolumeGain(config.volumeGain());
        }
    }

    private void loadVoiceOvers() {
        for (VoiceOver voiceOver : VoiceOver.values()) {
            try {
                Clip audioClip = AudioSystem.getClip();
                loadSound(audioClip, voiceOver.file());
                voiceOverClips.put(voiceOver, audioClip);
            } catch (LineUnavailableException e) {
                log.warn("Failed to play audio clip", e);
            }
        }
    }

    private void unloadVoiceOvers() {
        for (Clip audioClip : voiceOverClips.values()) {
            audioClip.stop();
            audioClip.close();
        }
    }

    private void updateVolumeGain(float decibels) {
        for (Clip clip : voiceOverClips.values()) {
            FloatControl control = (FloatControl) clip.getControl(Type.MASTER_GAIN);
            control.setValue(decibels);
        }
    }

    private void playVoiceOver(VoiceOver voiceOver) {
        Clip clip = voiceOverClips.get(voiceOver);

        if (clip == null) {
            log.warn("Voiceover '{}' is not loaded.", voiceOver);
        } else {
            playSound(clip);
        }
    }

    private void playSound(Clip audioClip) {
        audioClip.setFramePosition(0);
        audioClip.loop(0);
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
}