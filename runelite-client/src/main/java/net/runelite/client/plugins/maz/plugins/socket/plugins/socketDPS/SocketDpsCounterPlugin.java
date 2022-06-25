/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.Hitsplat
 *  net.runelite.api.NPC
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.HitsplatApplied
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.client.callback.ClientThread
 *  net.runelite.client.chat.ChatMessageManager
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.events.OverlayMenuClicked
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.plugins.PluginManager
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  org.pf4j.Extension
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketDPS;

import com.google.inject.Provides;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Hitsplat;
import net.runelite.api.NPC;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.maz.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Socket - Damage Counter", description="Counts damage by a party", enabledByDefault=false)
@PluginDependency(value=SocketPlugin.class)
public class SocketDpsCounterPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SocketDpsCounterPlugin.class);
    private static final ArrayList<Integer> BOSSES = new ArrayList();
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SocketDpsOverlay socketDpsOverlay;
    @Inject
    private SocketDpsDifferenceOverlay differenceOverlay;
    @Inject
    private SocketDpsConfig config;
    @Inject
    private EventBus eventBus;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private SocketPlugin socketPlugin;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ChatMessageManager chatMessageManager;
    public Map<String, Integer> members = new ConcurrentHashMap<String, Integer>();
    private List<String> highlights = new ArrayList<String>();
    private List<String> danger = new ArrayList<String>();
    private static final DecimalFormat DMG_FORMAT = new DecimalFormat("#,##0");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.0");
    private boolean mirrorMode;

    @Provides
    SocketDpsConfig provideConfig(ConfigManager configManager) {
        return (SocketDpsConfig)configManager.getConfig(SocketDpsConfig.class);
    }

    protected void startUp() {
        this.members.clear();
        this.overlayManager.add((Overlay)this.socketDpsOverlay);
        this.overlayManager.add((Overlay)this.differenceOverlay);
        this.clientThread.invoke(this::rebuildAllPlayers);
        BOSSES.addAll(Arrays.asList(5886, 5887, 5888, 5889, 5890, 5891, 5908, 6503, 6609, 5862, 5863, 5866, 2054, 6505, 319, 2215, 6494, 5779, 6499, 128, 963, 965, 4303, 4304, 6500, 6501, 239, 2642, 650, 3129, 6495, 8713, 6504, 6610, 6611, 6612, 6615, 3106, 3108, 3162, 2205, 2265, 2266, 2267, 11204, 7540, 7541, 7542, 7543, 7544, 7545, 7530, 7531, 7532, 7533, 7525, 7526, 7527, 7528, 7529, 7551, 7552, 7553, 7554, 7555, 7559, 7560, 7561, 7562, 7563, 7566, 7567, 7569, 7570, 7571, 7572, 7573, 7574, 7584, 7585, 7604, 7605, 7606, 9425, 9426, 9427, 9428, 9429, 9430, 9431, 9432, 9433, 9416, 9417, 9418, 9419, 9420, 9421, 9422, 9423, 9424, 11153, 11154, 11155, 8360, 8361, 8362, 8363, 8364, 8365, 8359, 8354, 8355, 8356, 8357, 8387, 8388, 8338, 8339, 8340, 8341, 8369, 8370, 8371, 8372, 8373, 8374, 8375, 10814, 10815, 10816, 10817, 10818, 10819, 10812, 10786, 10787, 10788, 10789, 10864, 10865, 10766, 10767, 10768, 10769, 10830, 10831, 10832, 10833, 10834, 10835, 10836, 10822, 10823, 10824, 10825, 10826, 10827, 10813, 10807, 10808, 10809, 10810, 10867, 10868, 10770, 10771, 10772, 10773, 10847, 10848, 10849, 10850, 10851, 10852, 10853, 11278, 11279, 11280, 11281, 11282));
    }

    protected void shutDown() {
        this.overlayManager.remove((Overlay)this.socketDpsOverlay);
        this.overlayManager.remove((Overlay)this.differenceOverlay);
        this.members.clear();
    }

    @Subscribe
    void onConfigChanged(ConfigChanged configChanged) {
        if (configChanged.getGroup().equals("socketdpscounter")) {
            this.clientThread.invoke(this::rebuildAllPlayers);
        }
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING) {
            this.members.clear();
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        Actor target = hitsplatApplied.getActor();
        Hitsplat hitsplat = hitsplatApplied.getHitsplat();
        if (this.client.getLocalPlayer() != null && hitsplat.isMine() && target != this.client.getLocalPlayer() && target instanceof NPC && hitsplat.getAmount() > 0) {
            NPC npc = (NPC)target;
            int interactingId = npc.getId();
            if (!this.config.onlyBossDps() || BOSSES.contains(interactingId)) {
                int hit = hitsplat.getAmount();
                String pName = this.client.getLocalPlayer().getName();
                this.members.put(pName, this.members.getOrDefault(pName, 0) + hit);
                this.members.put("Total", this.members.getOrDefault("Total", 0) + hit);
                JSONObject data = new JSONObject();
                data.put("player", pName);
                data.put("target", interactingId);
                data.put("hit", hit);
                data.put("world", this.client.getWorld());
                JSONObject payload = new JSONObject();
                payload.put("dps-counter", data);
                this.eventBus.post((Object)new SocketBroadcastPacket(payload));
                this.members = this.sortByValue(this.members);
            }
        }
    }

    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked event) {
        if (event.getEntry() == SocketDpsOverlay.RESET_ENTRY) {
            this.members.clear();
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        if (npc.isDead() && BOSSES.contains(npc.getId())) {
            log.debug("Boss has died!");
            if (this.config.clearDamage() == SocketDpsConfig.clearMode.ALWAYS) {
                this.clearMembers();
            }
            if (this.config.clearDamage() == SocketDpsConfig.clearMode.ANY_WORLD || this.config.clearDamage() == SocketDpsConfig.clearMode.YOUR_WORLD) {
                JSONObject data = new JSONObject();
                data.put("boss", npc.getId());
                data.put("world", this.client.getWorld());
                JSONObject payload = new JSONObject();
                payload.put("dps-clear", data);
                this.eventBus.post((Object)new SocketBroadcastPacket(payload));
            }
        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            if (this.client.getGameState() == GameState.LOGGED_IN && this.client.getLocalPlayer() != null) {
                JSONObject payload = event.getPayload();
                if (payload.has("dps-clear")) {
                    JSONObject data = payload.getJSONObject("dps-clear");
                    int world = data.getInt("world");
                    if (this.config.clearDamage() == SocketDpsConfig.clearMode.ANY_WORLD || this.config.clearDamage() == SocketDpsConfig.clearMode.YOUR_WORLD && world == this.client.getWorld()) {
                        this.clearMembers();
                    }
                } else if (payload.has("dps-counter")) {
                    JSONObject data = payload.getJSONObject("dps-counter");
                    int world = data.getInt("world");
                    if ((this.config.clearDamage() == SocketDpsConfig.clearMode.ANY_WORLD || this.config.clearDamage() == SocketDpsConfig.clearMode.YOUR_WORLD && world == this.client.getWorld()) && !data.getString("player").equals(this.client.getLocalPlayer().getName())) {
                        this.clientThread.invoke(() -> {
                            String attacker = data.getString("player");
                            int targetId = data.getInt("target");
                            this.updateDpsMember(attacker, targetId, data.getInt("hit"));
                        });
                    }
                }
            }
        }
        catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    private void updateDpsMember(String attacker, int targetId, int hit) {
        if (BOSSES.contains(targetId) || !this.config.onlyBossDps()) {
            this.members.put(attacker, this.members.getOrDefault(attacker, 0) + hit);
            this.members.put("Total", this.members.getOrDefault("Total", 0) + hit);
            this.members = this.sortByValue(this.members);
            this.updateDanger();
        }
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public List<String> getHighlights() {
        String configplayers = this.config.getPlayerToHighlight().toLowerCase();
        return configplayers.isEmpty() ? Collections.emptyList() : SocketText.fromCSV(configplayers);
    }

    void rebuildAllPlayers() {
        this.highlights = this.getHighlights();
    }

    void updateDanger() {
        this.danger.clear();
        for (String mem1 : this.members.keySet()) {
            if (!this.highlights.contains(mem1)) continue;
            for (String mem2 : this.members.keySet()) {
                if (mem2.equalsIgnoreCase(mem1) || this.members.get(mem2) - this.members.get(mem1) > 50) continue;
                this.danger.add(mem2);
            }
        }
    }

    private void clearMembers() {
        if (this.members.size() > 0) {
            if (this.config.dmgMessage() && this.client.getLocalPlayer() != null) {
                double totalDamage = this.members.get("Total") != null ? (double)this.members.get("Total").intValue() : 0.0;
                double personalDamage = this.members.get(this.client.getLocalPlayer().getName()) != null ? (double)this.members.get(this.client.getLocalPlayer().getName()).intValue() : 0.0;
                double percent = 0.0;
                if (totalDamage > 0.0 && personalDamage > 0.0) {
                    percent = personalDamage / totalDamage * 100.0;
                }
                this.client.addChatMessage(ChatMessageType.FRIENDSCHATNOTIFICATION, "", "Personal Damage: <col=ff0000>" + DMG_FORMAT.format(personalDamage) + "</col> (<col=ff0000>" + DECIMAL_FORMAT.format(percent) + "%</col>) | Total Damage: <col=ff0000>" + DMG_FORMAT.format(totalDamage) + "</col>", null);
            }
            this.members.clear();
        }
    }

    public Map<String, Integer> getMembers() {
        return this.members;
    }

    public List<String> getDanger() {
        return this.danger;
    }
}

