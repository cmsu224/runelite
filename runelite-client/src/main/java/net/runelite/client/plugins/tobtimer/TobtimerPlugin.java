package net.runelite.client.plugins.tobtimer;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import net.runelite.client.input.KeyManager;
import java.time.Duration;

@Slf4j
@PluginDescriptor(
        name = "[Maz] Custom Timer",
        description = "Timer",
        tags = {"Tob, timer, box, maz"}
)
public class TobtimerPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private TobtimerConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private TobtimerOverlay overlay;

    @Inject
    private SkillIconManager skillIconManager;

    @Inject
    private ClientToolbar clientToolbar;
    private NavigationButton uiNavigationButton;

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

    static final String CONFIG_GROUP = "tobtimer";
    private int theatreTicks = 0;
    private String theatreTime;
    private boolean startedByChat = false;
    public boolean active = false;
    private int totalTimeTicks = 0;
    private String theatreTimeTotal;

    @Inject
    private KeyManager keyManager;

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
        keyManager.registerKeyListener(hotkeyListener);
    }

    @Override
    protected void shutDown() throws Exception
    {
        timer.reset();
        showOverlay = false;
        overlayManager.remove(overlay);
        keyManager.unregisterKeyListener(hotkeyListener);
    }

    @Provides
    TobtimerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TobtimerConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        timer.tick();
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {

        if (event.getType() != ChatMessageType.GAMEMESSAGE)
        {
            return;
        }

        String message = Text.removeTags(event.getMessage()).toLowerCase();
        String[] startMsgs = config.getStartMsgs().toLowerCase().split(";");
        String[] stopMsgs = config.getStopMsgs().toLowerCase().split(";");

        //start timer
        if(checkMsgs(startMsgs, message))
        //if (message.startsWith(MESSAGE_RAID_STARTED))
        {
            showOverlay = true;
            timer.reset();
            timer.start();
            return;
        }

        if (!timer.isActive())
        {
            return;
        }

        //stop timer
        if(checkMsgs(stopMsgs, message))
        //if (message.startsWith(MESSAGE_RAID_COMPLETED))
        {
            timer.stop();
        }
    }

    private boolean checkMsgs(String[] msgList, String msg){
        for(String x : msgList){
            if(msg.contains(x)){
                return true;
            }
        }
        return false;
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggleKeybind())
    {
        @Override
        public void hotkeyPressed()
        {
            if(!timer.isActive() && showOverlay){
                showOverlay = false;
                timer.reset();
            }
            else if(!timer.isActive()){
                showOverlay = true;
                timer.start();
            }else{
                showOverlay = true;
                timer.stop();
            }
        }
    };

}
