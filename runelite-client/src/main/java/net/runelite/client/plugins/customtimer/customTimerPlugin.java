package net.runelite.client.plugins.customtimer;

import com.google.inject.Provides;
import java.time.Duration;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
        name = "<html><font color=#FF0000>[Maz] Reminder"
)

public class customTimerPlugin extends Plugin{
    @Inject
    private Client client;

    @Inject
    private customTimerConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private customTimerOverlay overlay;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private Notifier notifier;

    private static final Pattern DIGIT_PATTERN = Pattern.compile("(\\d+)");

    private int tobVarbit = 0;

    @Getter
    private customTimer timer = new customTimer();
    @Getter
    private boolean showOverlay = false;
    @Getter
    private boolean raidSucceeded = false;
    @Getter
    private String timerStatus = "off";
    @Getter
    private Duration timeToBeat = Duration.ZERO;

    @Provides
    customTimerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(customTimerConfig.class);
    }

    public customTimerPlugin() {
    }

    @Override
    protected void startUp() throws Exception
    {
        if(config.overlay()){
            showOverlay = true;
        }
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
    public void onConfigChanged(ConfigChanged event)
    {
        if (event.getGroup().equals("customtimer"))
        {
            if(config.overlay()){
                showOverlay = true;
            }
            if(!config.overlay()){
                showOverlay = false;
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        if (client.getGameState() == GameState.LOGGED_IN)
        {
            if(!timerStatus.equals("on")){
                reset();
                timer.start();
                timerStatus = "on";
            }
            timer.tick();
        }

        Duration d = Duration.ofMinutes(config.reminderTime());
        if(d.toString().equals(timer.getGameTime().toString())){
            sendChatMessage(config.reminder());

            final StringBuilder notificationStringBuilder = new StringBuilder()
                    .append(config.reminder());

            notifier.notify(notificationStringBuilder.toString());
            reset();
            timer.start();
            timerStatus = "on";
        }

    }

    private void reset()
    {
        timer.reset();
        timeToBeat = Duration.ZERO;
        raidSucceeded = false;
    }

    public void sendChatMessage(String chatMessage)
    {
        final String message = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(chatMessage)
                .build();

        chatMessageManager.queue(
                QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(message)
                        .build());
    }
}