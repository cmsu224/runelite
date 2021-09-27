package net.runelite.client.plugins.mtimer;

import com.google.inject.Provides;
import java.time.Duration;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
        name = "[Maz] Custom Timer"
)

public class mTimerPlugin extends Plugin{
    @Inject
    private Client client;

    @Inject
    private mTimerConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private mTimerOverlay overlay;

    private static final Pattern DIGIT_PATTERN = Pattern.compile("(\\d+)");

    private int tobVarbit = 0;

    @Getter
    private mTimer timer = new mTimer();
    @Getter
    private boolean showOverlay = false;
    @Getter
    private boolean raidSucceeded = false;
    @Getter
    private String timerStatus = "off";
    @Getter
    private Duration timeToBeat = Duration.ZERO;

    @Provides
    mTimerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(mTimerConfig.class);
    }

    public mTimerPlugin() {
    }

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        reset();
        showOverlay = false;
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        if (event.getType() != ChatMessageType.GAMEMESSAGE)
        {
            return;
        }

        String message = Text.removeTags(event.getMessage());

        String test = config.getStartMsgs();
        //start timer
        if(config.getStartMsgs().toLowerCase().contains(message.toLowerCase()))
        //if (message.startsWith(MESSAGE_RAID_STARTED))
        {
            showOverlay = true;
            reset();
            timer.start();

            return;
        }

        if (!timer.isActive())
        {
            return;
        }

        //stop timer
        if(config.getStopMsgs().toLowerCase().contains(message.toLowerCase()))
        //if (message.startsWith(MESSAGE_RAID_COMPLETED))
        {
            timer.stop();
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        timer.tick();
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggleKeybind())
    {
        @Override
        public void hotkeyPressed()
        {
            if(!timer.isActive() && timerStatus.startsWith("pause")){
                showOverlay = false;
                timerStatus = "off";
                reset();
            }
            else if(!timer.isActive()){
                showOverlay = true;
                timer.start();
                timerStatus = "on";
            }else{
                showOverlay = true;
                timer.stop();
                timerStatus = "pause";
            }
        }
    };

    private void reset()
    {
        timer.reset();
        timeToBeat = Duration.ZERO;
        raidSucceeded = false;
    }
}