package net.runelite.client.plugins.maze;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@PluginDescriptor(
        name = "[Maz] Maze",
        description = "shows maze",
        tags = {"maze"}
)
public class mazePlugin extends Plugin{
    @Inject
    private Client client;

    @Inject
    private mazeConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private SkillIconManager skillIconManager;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private mazeOverlay overlay;

    private NavigationButton uiNavigationButton;

    static final String CONFIG_GROUP = "mazeconfig";
    private String testMessage = "";
    @Getter
    private final Set<GroundObject> mazeList = new HashSet<>();

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        mazeList.clear();
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event)
    {
        if (config.debug()){
            sendDistinctChatMsg("" + this.client.getMapRegions()[0]); // 13123;
        }

        if (this.client.getMapRegions()[0] == config.regionID()){
            overlayManager.add(overlay);
            GroundObject obj = event.getGroundObject();
            if (obj.getId() == config.gID() || obj.getId() == 41750 || obj.getId() == 41751 || obj.getId() == 41752 || obj.getId() == 41753)
            {
                mazeList.add(obj);
            }
        }else {
            overlayManager.remove(overlay);
            mazeList.clear();
        }
    }

    @Provides
    mazeConfig provideConfig(ConfigManager configManager)
    {
        return (mazeConfig) configManager.getConfig(mazeConfig.class);
    }

    public void sendDistinctChatMsg(String msg){
        if(!msg.equals(testMessage)){
            sendChatMessage(msg);
            testMessage = msg;
        }
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
