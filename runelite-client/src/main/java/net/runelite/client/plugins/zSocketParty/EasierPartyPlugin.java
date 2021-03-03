/*
 * Copyright (c) 2020, Charles Xu <github.com/kthisiscvpv>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.runelite.client.plugins.zSocketParty;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.ModifierlessKeybind;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.*;
import net.runelite.client.plugins.socket.SocketConfig;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.zSocketParty.data.*;
import net.runelite.client.plugins.zSocketParty.messages.TilePing;
import net.runelite.client.plugins.zSocketParty.ui.prayer.PrayerSprites;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ws.PartyService;
import net.runelite.client.ws.WSClient;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

import static net.runelite.api.ChatMessageType.GAMEMESSAGE;

@PluginDescriptor(
		name = "[Maz] Socket-Party",
		description = "Create a party without having to use Discord party plugin.",
		tags = {"socket", "server", "discord", "connection", "broadcast", "party", "easy"}
)
@PluginDependency(SocketPlugin.class)
public class EasierPartyPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private EventBus eventBus;

	@Inject
	private PluginManager pluginManager;

	@Inject
	private SocketPlugin socketPlugin;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private PartyService partyService;

	@Inject
	private PartyPingOverlay partyPingOverlay;

	@Inject
	@Getter(AccessLevel.PUBLIC)
	private EasierPartyConfig config;
	@Provides
	EasierPartyConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EasierPartyConfig.class);
	}

	@Inject
	private ClientThread clientThread;

	private static final Set<ChatMessageType> COLLAPSIBLE_MESSAGETYPES = ImmutableSet.of(
			GAMEMESSAGE
	);

	private Color pingColor;
	private ModifierlessKeybind pingKey;
	private Boolean showPanel;

	@Getter(AccessLevel.PACKAGE)
	private final Map<UUID, PartyData> partyDataMap = Collections.synchronizedMap(new HashMap<>());

	@Getter(AccessLevel.PACKAGE)
	private final List<PartyTilePingData> pendingTilePings = Collections.synchronizedList(new ArrayList<>());
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	SpriteManager spriteManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	ItemManager itemManager;

	@Inject
	private WSClient wsClient;

	@Getter
	private final Map<String, PartyPlayer> partyMembers = new HashMap<>();

	private NavigationButton navButton;
	private boolean addedButton = false;
	private PartyPanel panel;
	@Getter
	private PartyPlayer myPlayer = null;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// This boolean states whether or not the room is currently active.
	@Getter(AccessLevel.PUBLIC)
	private boolean sotetsegActive;

	// This NPC represents the boss.
	private NPC sotetsegNPC;

	// This represents the bad tiles.
	private LinkedHashSet<Point> redTiles;

	@Getter(AccessLevel.PUBLIC)
	private Set<WorldPoint> mazePings;

	// This represents the amount of times to send data.
	private int dispatchCount;

	// This represents the state of the raid.
	private boolean wasInUnderworld;
	private int overworldRegionID;

	@Override
	protected void startUp()
	{
		overlayManager.add(partyPingOverlay);
		pingColor = config.pingTileColor();
		pingKey = config.PingKey();
		showPanel = config.showpartypanel();
		/////////////////////////////////////////////////////////////////////////////////////////////
		if(showPanel)
		{
			panel = new PartyPanel(this);
			//final BufferedImage icon = ImageUtil.getResourceStreamFromClass(ClientUI.class, "icon.png");

			final BufferedImage icon = ImageUtil.getResourceStreamFromClass(this.getClass(), "icon.png");

			navButton = NavigationButton.builder()
					.tooltip("Party Panel2")
					.icon(icon)
					.priority(7)
					.panel(panel)
					.build();

			clientToolbar.addNavigation(navButton);
			addedButton = true;
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("zSocketParty"))
		{
			pingColor = config.pingTileColor();
			pingKey = config.PingKey();
			showPanel = config.showpartypanel();
			if(showPanel)
			{
				panel = new PartyPanel(this);
				//final BufferedImage icon = ImageUtil.getResourceStreamFromClass(ClientUI.class, "icon.png");

				final BufferedImage icon = ImageUtil.getResourceStreamFromClass(this.getClass(), "icon.png");

				navButton = NavigationButton.builder()
						.tooltip("Party Panel2")
						.icon(icon)
						.priority(7)
						.panel(panel)
						.build();

				clientToolbar.addNavigation(navButton);
				addedButton = true;
			}else
			{
				clientToolbar.removeNavigation(navButton);
				myPlayer = null;
				addedButton = false;
				partyMembers.clear();
			}
		}
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(partyPingOverlay);
		//////////////////////////////////////////////////////////////////////////////////////////////
		clientToolbar.removeNavigation(navButton);
		myPlayer = null;
		addedButton = false;
		partyMembers.clear();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			myPlayer = new PartyPlayer(partyService.getLocalMember(), client, itemManager);
			panel.updatePartyPlayer(myPlayer);
		}else {
			partyMembers.clear();
			myPlayer = null;
			panel.showBannerView();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		try {
			if (client.getGameState() != GameState.LOGGED_IN)
			{
				return;
			}

			boolean changed = false;

			//myPlayer = new PartyPlayer(partyService.getLocalMember(), client, itemManager);
			if (myPlayer == null)
			{
				myPlayer = new PartyPlayer(partyService.getLocalMember(), client, itemManager);
				// member changed account, send new data to all members
				changed = true;
			}

			if (myPlayer.getStats() == null || !myPlayer.getStats().equals(new Stats(client)))
			{
				myPlayer.updatePlayerInfo(client, itemManager);
				changed = true;
				//changed = false;
			}
			else
			{
				final int energy = client.getEnergy();
				if (myPlayer.getStats().getRunEnergy() != energy)
				{
					myPlayer.getStats().setRunEnergy(energy);
					changed = true;
				}
			}

			if (!Objects.equals(client.getLocalPlayer().getName(), myPlayer.getUsername()))
			{
				myPlayer.setUsername(client.getLocalPlayer().getName());
				changed = true;
			}

			if (myPlayer.getPrayers() == null)
			{
				myPlayer.setPrayers(new Prayers(client));
				changed = true;
			}
			else
			{
				for (final PrayerSprites prayer : PrayerSprites.values())
				{
					changed = myPlayer.getPrayers().updatePrayerState(prayer, client) || changed;
				}
			}
			myPlayer = new PartyPlayer(partyService.getLocalMember(), client, itemManager);
			if (changed)
			{
				sendPlayerData(myPlayer);
			}
		}catch (Exception e){
			sendChatMessage(e.getMessage());
		}
	}

	private void sendPlayerData(PartyPlayer partyplayer)
	{
		try{
			JSONObject payload = new JSONObject();
			JSONObject pp = partyplayer.toJSON();
			pp.put("username", partyplayer.getUsername());
			payload.put("player-info", pp);

			eventBus.post(new SocketBroadcastPacket(payload));
		}catch (Exception e)
		{
			sendChatMessage(e.getMessage());
		}
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!client.isKeyPressed(pingKey.getKeyCode()) || client.isMenuOpen())
		{
			return;
		}
		Keybind CTRL = new Keybind(KeyEvent.VK_UNDEFINED, InputEvent.CTRL_DOWN_MASK);
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
				break;
			}
		}

		if (!isOnCanvas)
		{
			return;
		}

		event.consume();
		JSONArray data = new JSONArray();

		WorldPoint wp = selectedSceneTile.getWorldLocation();
		JSONObject jsonwp = new JSONObject();
		jsonwp.put("x", wp.getX());
		jsonwp.put("y", wp.getY());
		jsonwp.put("plane", wp.getPlane());
		jsonwp.put("player-name", client.getLocalPlayer().getName());
		jsonwp.put("color", pingColor.getRGB());
		data.put(jsonwp);

		JSONObject payload = new JSONObject();
		payload.put("tile-ping", data);
		//sendChatMessage("Attempting to send world point: " +  wp.getX() + "/"+wp.getY());
		eventBus.post(new SocketBroadcastPacket(payload));
		//sendPlayerData(myPlayer);
	}

	@Subscribe
	public void onSocketReceivePacket(SocketReceivePacket event)
	{
		try
		{
			JSONObject payload = event.getPayload();
			String localName = client.getLocalPlayer().getName();

			if (payload.has("tile-ping"))
			{
				if (this.client.getGameState() != GameState.LOGGED_IN) {
					return;
				}

				JSONArray data = payload.getJSONArray("tile-ping");
				JSONObject jsonwp = data.getJSONObject(0);

				String targetName = jsonwp.getString("player-name");

				//if (targetName.equals(localName)) { return; }

				int x = jsonwp.getInt("x");
				int y = jsonwp.getInt("y");
				int plane = jsonwp.getInt("plane");

				Color c = this.pingColor;
				if(jsonwp.has("color")) {
					c = new Color(jsonwp.getInt("color"));
				}
				//sendChatMessage("Attempting to draw world point: " +  x + "/"+y);
				WorldPoint wp = new WorldPoint(x, y, plane);
				final TilePing tilePing = new TilePing(wp);
				pendingTilePings.add(new PartyTilePingData(wp, c));

				if (wp.getPlane() != client.getPlane() || !wp.isInScene(client, wp.getX(), wp.getY()))
				{
					return;
				}

				clientThread.invoke(() -> client.playSoundEffect(SoundEffectID.SMITH_ANVIL_TINK));

			}
			else if (payload.has("player-info"))
			{
				if(config.showpartypanel())
				{
					JSONObject playerInfo = payload.getJSONObject("player-info");
					PartyPlayer receivedPlayer = new PartyPlayer(playerInfo);
					if(myPlayer.getUsername().equals(receivedPlayer.getUsername())){return;}
					partyMembers.put(receivedPlayer.getUsername(), receivedPlayer);

					SwingUtilities.invokeLater(() -> panel.updatePartyPlayer(receivedPlayer));
				}
			}
			else if (payload.has("leave-party"))
			{
				String userLeft = payload.getString("player-info");
				PartyPlayer leftUser = partyMembers.get(userLeft);
				panel.removePartyPlayer(leftUser);
			}
		}
		catch (Exception e)
		{
			sendChatMessage(e.getMessage());
		}
	}
/*
	@Subscribe // run after ChatMessageManager
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
				sendChatMessage("Leaving discord party");
				partyService.changeParty(null);
			}

		}
	}

	public void createPartyId(PartyService partyService){
		if (this.client.getGameState() != GameState.LOGGED_IN) {
			return;
		}
		if (this.partyUUID == null){
			this.partyUUID = UUID.randomUUID();
			sendChatMessage("generated a random ID.");
		}
		if (this.partyUUID != partyService.getPartyId()){
			partyService.changeParty(this.partyUUID);
			sendChatMessage("Created a new party ID: " + this.partyUUID.toString());
		}

		JSONArray data = new JSONArray();
		JSONObject jsonwp = new JSONObject();
		jsonwp.put("partyID", this.partyUUID);
		data.put(jsonwp);
		JSONObject payload = new JSONObject();
		payload.put("partyID", data);
		sendChatMessage("sending through socket");
		this.eventBus.post(SocketBroadcastPacket.class, new SocketBroadcastPacket(payload));
	}*/

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