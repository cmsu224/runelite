/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.Skill
 *  net.runelite.api.events.ClientTick
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.client.callback.ClientThread
 *  net.runelite.client.chat.ChatMessageBuilder
 *  net.runelite.client.chat.ChatMessageManager
 *  net.runelite.client.chat.QueuedMessage
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayManager
 *  net.runelite.client.util.ColorUtil
 *  net.runelite.client.util.Text
 *  org.pf4j.Extension
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.sockethealing;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.maz.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketPlayerLeave;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

@PluginDescriptor(name="Socket - Healing", description="Displays health overlays for socket party members. <br> Created by: A wild animal with a keyboard <br> Modified by: SpoonLite", enabledByDefault=false)
@PluginDependency(value=SocketPlugin.class)
public class SocketHealingPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SocketHealingOverlay socketHealingOverlay;
    @Inject
    private SocketHealingConfig config;
    @Inject
    private SocketPlugin socketPlugin;
    @Inject
    private ClientThread clientThread;
    @Inject
    private EventBus eventBus;
    @Inject
    private ChatMessageManager chatMessageManager;
    private Map<String, SocketHealingPlayer> partyMembers = new TreeMap<String, SocketHealingPlayer>();
    private int lastRefresh;
    public ArrayList<String> playerNames = new ArrayList();
    private boolean mirrorMode;

    @Provides
    SocketHealingConfig provideConfig(ConfigManager configManager) {
        return (SocketHealingConfig)configManager.getConfig(SocketHealingConfig.class);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void startUp() {
        this.overlayManager.add((Overlay)this.socketHealingOverlay);
        this.lastRefresh = 0;
        Map<String, SocketHealingPlayer> map = this.partyMembers;
        synchronized (map) {
            this.partyMembers.clear();
        }
    }

    protected void shutDown() {
        this.overlayManager.remove((Overlay)this.socketHealingOverlay);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e) {
        if (e.getGroup().equals("sockethealing")) {
            if (!this.config.hpPlayerNames().equals("")) {
                this.playerNames.clear();
                String[] arrayOfString = this.config.hpPlayerNames().split(",");
                int i = arrayOfString.length;
                for (int b = 0; b < i; b = (int)((byte)(b + 1))) {
                    String str = arrayOfString[b];
                    if ("".equals(str = str.trim())) continue;
                    this.playerNames.add(str.toLowerCase());
                }
            }
            if (e.getKey().equals("setHighestPriority")) {
                this.socketHealingOverlay.setLayer(this.config.setHighestPriority() ? OverlayLayer.ABOVE_WIDGETS : OverlayLayer.ABOVE_SCENE);
                ChatMessageBuilder message = new ChatMessageBuilder().append(Color.MAGENTA, "Re-load the plugin to change overlay layer!");
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.ITEM_EXAMINE).runeLiteFormattedMessage(message.build()).build());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING) {
            Map<String, SocketHealingPlayer> map = this.partyMembers;
            synchronized (map) {
                this.partyMembers.clear();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Subscribe
    private void onGameTick(GameTick event) {
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            SocketHealingPlayer playerHealth;
            int currentHealth = this.client.getBoostedSkillLevel(Skill.HITPOINTS);
            String name = this.client.getLocalPlayer().getName();
            Map<String, SocketHealingPlayer> map = this.partyMembers;
            synchronized (map) {
                playerHealth = this.partyMembers.get(name);
                if (playerHealth == null) {
                    playerHealth = new SocketHealingPlayer(name, currentHealth);
                    this.partyMembers.put(name, playerHealth);
                } else {
                    playerHealth.setHealth(currentHealth);
                }
            }
            ++this.lastRefresh;
            if (this.lastRefresh >= Math.max(1, this.config.refreshRate())) {
                JSONObject packet = new JSONObject();
                packet.put("name", name);
                packet.put("player-health", playerHealth.toJSON());
                this.eventBus.post((Object)new SocketBroadcastPacket(packet));
                this.lastRefresh = 0;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        block8: {
            try {
                JSONObject payload = event.getPayload();
                String localName = this.client.getLocalPlayer().getName();
                if (!payload.has("player-health")) break block8;
                String targetName = payload.getString("name");
                if (targetName.equals(localName)) {
                    return;
                }
                JSONObject statusJSON = payload.getJSONObject("player-health");
                Map<String, SocketHealingPlayer> map = this.partyMembers;
                synchronized (map) {
                    SocketHealingPlayer playerHealth = this.partyMembers.get(targetName);
                    if (playerHealth == null) {
                        playerHealth = SocketHealingPlayer.fromJSON(statusJSON);
                        this.partyMembers.put(targetName, playerHealth);
                    } else {
                        playerHealth.parseJSON(statusJSON);
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Subscribe
    public void onSocketPlayerLeave(SocketPlayerLeave event) {
        String target = event.getPlayerName();
        Map<String, SocketHealingPlayer> map = this.partyMembers;
        synchronized (map) {
            if (this.partyMembers.containsKey(target)) {
                this.partyMembers.remove(target);
            }
        }
    }

    public Map<String, SocketHealingPlayer> getPartyMembers() {
        return this.partyMembers;
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        if (this.config.hpMenu()) {
            Color color = Color.GREEN;
            String target = event.getTarget().replaceAll("[^A-Za-z0-9-()<>=]", " ");
            for (String playerName : this.getPartyMembers().keySet()) {
                if (!Text.removeTags((String)target).toLowerCase().contains(playerName.toLowerCase() + "  (level-")) continue;
                SocketHealingPlayer player = this.getPartyMembers().get(playerName);
                MenuEntry[] menuEntries = this.client.getMenuEntries();
                MenuEntry menuEntry = menuEntries[menuEntries.length - 1];
                int playerHealth = player.getHealth();
                if (playerHealth > this.config.greenZone()) {
                    color = this.config.greenZoneColor();
                }
                if (playerHealth <= this.config.greenZone() && playerHealth > this.config.orangeZone()) {
                    color = this.config.orangeZoneColor();
                }
                if (playerHealth <= this.config.orangeZone()) {
                    color = this.config.redZoneColor();
                }
                String hpAdded = ColorUtil.prependColorTag((String)(" - " + playerHealth), (Color)color);
                menuEntry.setTarget(event.getTarget() + hpAdded);
                this.client.setMenuEntries(menuEntries);
            }
        }
    }
    /*
    @Subscribe
    public void onClientTick(ClientTick event) {
        if (this.client.getSpegetSelectedSpellName() != null && Text.removeTags((String)this.client.getSelectedSpellName()).equalsIgnoreCase("heal other") && this.config.healOtherMES()) {
            String target;
            String option;
            MenuEntry[] menuEntries;
            String lowestHpName = "";
            int lowestHp = 130;
            block0: for (MenuEntry me : menuEntries = this.client.getMenuEntries()) {
                option = Text.removeTags((String)me.getOption()).toLowerCase();
                target = Text.removeTags((String)me.getTarget()).toLowerCase();
                if (!option.contains("cast") || !target.contains("heal other ->")) continue;
                target = target.substring(target.indexOf("-> ") + 3, target.indexOf("  (level-")).replaceAll("[^A-Za-z0-9]", " ");
                for (String playerName : this.getPartyMembers().keySet()) {
                    if (!target.contains(playerName.toLowerCase())) continue;
                    if (this.partyMembers.get(playerName).getHealth() >= lowestHp) continue block0;
                    lowestHpName = playerName.toLowerCase();
                    lowestHp = this.partyMembers.get(playerName).getHealth();
                    continue block0;
                }
            }
            for (MenuEntry me : menuEntries) {
                option = Text.removeTags((String)me.getOption()).toLowerCase();
                target = Text.removeTags((String)me.getTarget()).toLowerCase();
                if (!option.contains("cast") || !target.contains("heal other ->") || (target = target.substring(target.indexOf("-> ") + 3, target.indexOf("  (level-")).replaceAll("[^A-Za-z0-9]", " ")).contains(lowestHpName)) continue;
                me.setDeprioritized(true);
            }
        }
    }*/
}

