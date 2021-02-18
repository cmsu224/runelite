//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zeasyparty;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.chatfilter.ChatFilterPlugin;
import net.runelite.client.plugins.party.messages.TilePing;
import net.runelite.client.plugins.socket.SocketLog;
import net.runelite.client.plugins.socket.SocketState;
import net.runelite.client.plugins.socket.hash.AES256;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ws.PartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.runelite.api.Client;
import net.runelite.client.plugins.socket.plugins.sotetseg.SotetsegPlugin;

import static net.runelite.api.ChatMessageType.*;
import static net.runelite.api.ChatMessageType.MODCHAT;

@PluginDescriptor(
        name = "[Maz]EasierPartyTest",
        description = "Create a party without having to add people as a friend on Discord."
)
public class EasyPartyPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(EasyPartyPlugin.class);
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private PartyService partyService;
    private NavigationButton navigationButton;
    @Inject
    private Client client;
    @Inject
    private EventBus eventBus;
    @Inject
    private ChatMessageManager chatMessageManager;
    private boolean sotetsegActive;
    private UUID partyUUID;
    private static final Set<ChatMessageType> COLLAPSIBLE_MESSAGETYPES = ImmutableSet.of(
            //ENGINE,
            GAMEMESSAGE
            //ITEM_EXAMINE,
            //NPC_EXAMINE,
            //OBJECT_EXAMINE,
            //SPAM,
            //PUBLICCHAT,
            //MODCHAT
    );

    public EasyPartyPlugin() {
    }

    protected void startUp() throws Exception {
        //BufferedImage icon = ImageUtil.getResourceStreamFromClass(this.getClass(), "icon.png");
        //EasyPartyPanel easyPartyPanel = new EasyPartyPanel(this.partyService);
        //this.navigationButton = NavigationButton.builder().priority(9).tooltip("EasyParty").icon(icon).panel(easyPartyPanel).build();
        //this.clientToolbar.addNavigation(this.navigationButton);
        //createPartyId(this.partyService);
    }

    protected void shutDown() throws Exception {
        //this.clientToolbar.removeNavigation(this.navigationButton);
    }
/*
    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event)
    {
        if (!client.isKeyPressed(KeyCode.KC_SHIFT) || client.isMenuOpen())
        //if (!client.isKeyPressed(KeyEvent.VK_F10) || client.isMenuOpen())
        {
            return;
        }

        Tile selectedSceneTile = client.getSelectedSceneTile();
        if (selectedSceneTile == null)
        {
            return;
        }

        boolean isOnCanvas = false;

        for (MenuEntry menuEntry : client.getMenuEntries())
        {
            if (menuEntry == null)
            {
                continue;
            }

            if ("walk here".equalsIgnoreCase(menuEntry.getOption()))
            {
                isOnCanvas = true;
            }
        }

        if (!isOnCanvas)
        {
            return;
        }

        event.consume();

        //final TilePing tilePing = new TilePing(selectedSceneTile.getWorldLocation());
        //tilePing.setMemberId(party.getLocalMember().getMemberId());
        //wsClient.send(tilePing);

        WorldPoint wp = selectedSceneTile.getWorldLocation();

        JSONArray data = new JSONArray();
        JSONObject jsonwp = new JSONObject();
        jsonwp.put("x", wp.getX());
        jsonwp.put("y", wp.getY());
        jsonwp.put("plane", wp.getPlane());
        data.put(jsonwp);

        JSONObject payload = new JSONObject();
        payload.put("sotetseg-extended", data);
        sendChatMessage("sending loc: " + wp.getX() + "; " + wp.getY());
        this.eventBus.post(new SocketBroadcastPacket(payload));
    }*/
    @Subscribe(priority = -2) // run after ChatMessageManager
    public void onChatMessage(ChatMessage chatMessage)
    {
        if (COLLAPSIBLE_MESSAGETYPES.contains(chatMessage.getType()))
        {
            final MessageNode messageNode = chatMessage.getMessageNode();
            // remove and re-insert into map to move to end of list
            //final String key = messageNode.getName() + ":" + messageNode.getValue();
            //sendChatMessage("NEW CHAT MESG TYPE: "+chatMessage.getType());
            //sendChatMessage("NEW CHAT MESG DETECTED: "+messageNode.getValue());
            if(!messageNode.getValue().contains("Member (1):"))
            {
                if (messageNode.getValue().contains("Members (") || messageNode.getValue().contains("!Dcjoin")){
                    createPartyId(partyService);
                }
            }
            if (messageNode.getValue().contains("Socket terminated") || messageNode.getValue().contains("Configuration changed. Please restart the plugin to see updates") || messageNode.getValue().contains("Any active socket server connections were closed") )
            {
                partyService.changeParty(null);
            }

        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
    try {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        JSONObject payload = event.getPayload();
        if (!payload.has("partyID")) {
            return;
        }
        JSONArray data = payload.getJSONArray("partyID");
        JSONObject jsonmsg = data.getJSONObject(0);
        String partyID = jsonmsg.getString("partyID");

        if (!this.partyUUID.toString().equals(partyID)){
            this.partyUUID = UUID.fromString(partyID);
            partyService.changeParty(this.partyUUID);
            sendChatMessage("Joined Socket-Discord Party: "+partyID);
        }
    } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public void createPartyId(PartyService partyService){
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        if (this.partyUUID == null){
            this.partyUUID = UUID.randomUUID();
            //sendChatMessage("generated a random ID.");
        }
        if (this.partyUUID != partyService.getPartyId()){
            partyService.changeParty(this.partyUUID);
            //sendChatMessage("Created a new party ID: " + this.partyUUID.toString());
        }

        JSONArray data = new JSONArray();
        JSONObject jsonwp = new JSONObject();
        jsonwp.put("partyID", this.partyUUID);
        data.put(jsonwp);
        JSONObject payload = new JSONObject();
        payload.put("partyID", data);
        //sendChatMessage("sending through socket");
        this.eventBus.post(new SocketBroadcastPacket(payload));
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
