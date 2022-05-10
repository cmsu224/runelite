/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Prayer
 *  net.runelite.api.Skill
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.kit.KitType
 *  net.runelite.client.callback.ClientThread
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.ui.overlay.infobox.InfoBox
 *  net.runelite.client.ui.overlay.infobox.InfoBoxManager
 *  net.runelite.client.util.ImageUtil
 *  org.pf4j.Extension
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.socket;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.SocketConfig;
import net.runelite.client.plugins.socket.SocketConnection;
import net.runelite.client.plugins.socket.SocketInfobox;
import net.runelite.client.plugins.socket.SocketState;
import net.runelite.client.plugins.socket.hash.AES256;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketPlayerJoin;
import net.runelite.client.plugins.socket.packet.SocketPlayerLeave;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.socket.packet.SocketShutdown;
import net.runelite.client.plugins.socket.packet.SocketStartup;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Socket", description="Socket connection for broadcasting messages across clients.", tags={"socket", "server", "discord", "connection", "broadcast"}, enabledByDefault=false)
public class SocketPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SocketPlugin.class);
    public static final String CONFIG_VERSION = "Socket Plugin v2.1.0";
    public static final String PASSWORD_SALT = "$P@_/gKR`y:mv)6K";
    @Inject
    private Client client;
    @Inject
    private EventBus eventBus;
    @Inject
    private ClientThread clientThread;
    @Inject
    private SocketConfig config;
    @Inject
    private InfoBoxManager infoBoxManager;
    private long nextConnection;
    public SocketConnection connection = null;
    private SocketInfobox connectionIB = null;
    final BufferedImage icon_Connected = ImageUtil.loadImageResource(((Object)((Object)this)).getClass(), (String)"icon_Connected.png");
    final BufferedImage icon_Disconnected = ImageUtil.loadImageResource(((Object)((Object)this)).getClass(), (String)"icon_Disconnected.png");
    final BufferedImage icon_Ready = ImageUtil.loadImageResource(((Object)((Object)this)).getClass(), (String)"icon_Ready.png");
    public String connectionState = "";
    public static SocketPlugin instance = null;
    private DeferredCheck deferredCheck;
    protected static final Set<Integer> VERZIK_P2_IDS = ImmutableSet.of(8372, 10833, 10850);

    @Provides
    SocketConfig getConfig(ConfigManager configManager) {
        return (SocketConfig)configManager.getConfig(SocketConfig.class);
    }

    protected void startUp() {
        instance = this;
        this.infoBoxManager.removeInfoBox((InfoBox)this.connectionIB);
        this.nextConnection = 0L;
        this.eventBus.register(SocketReceivePacket.class);
        this.eventBus.register(SocketBroadcastPacket.class);
        this.eventBus.register(SocketPlayerJoin.class);
        this.eventBus.register(SocketPlayerLeave.class);
        this.eventBus.register(SocketStartup.class);
        this.eventBus.register(SocketShutdown.class);
        this.eventBus.post((Object)new SocketStartup());
        this.connectionState = "";
    }

    protected void shutDown() {
        instance = null;
        this.infoBoxManager.removeInfoBox((InfoBox)this.connectionIB);
        this.eventBus.post((Object)new SocketShutdown());
        this.eventBus.unregister(SocketReceivePacket.class);
        this.eventBus.unregister(SocketBroadcastPacket.class);
        this.eventBus.unregister(SocketPlayerJoin.class);
        this.eventBus.unregister(SocketPlayerLeave.class);
        this.eventBus.unregister(SocketStartup.class);
        this.eventBus.unregister(SocketShutdown.class);
        if (this.connection != null) {
            this.connection.terminate(true);
        }
        this.connectionState = "";
    }

    private SocketInfobox createInfoBox(BufferedImage image, String status) {
        return new SocketInfobox(image, this.config, this, status);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.statCheckOnGameTick();
            if (this.connection != null) {
                SocketState state;
                if (this.config.infobox() && !this.connection.getState().toString().equals(this.connectionState)) {
                    this.connectionState = this.connection.getState().toString();
                    this.infoBoxManager.removeInfoBox((InfoBox)this.connectionIB);
                    switch (this.connection.getState()) {
                        case DISCONNECTED: 
                        case TERMINATED: {
                            this.connectionIB = this.createInfoBox(this.icon_Disconnected, "Disconnected");
                            break;
                        }
                        case CONNECTING: {
                            this.connectionIB = this.createInfoBox(this.icon_Ready, "Connecting...");
                            break;
                        }
                        case CONNECTED: {
                            this.connectionIB = this.createInfoBox(this.icon_Connected, "Connected");
                        }
                    }
                    this.infoBoxManager.addInfoBox((InfoBox)this.connectionIB);
                }
                if ((state = this.connection.getState()) == SocketState.CONNECTING || state == SocketState.CONNECTED) {
                    return;
                }
            }
            if (System.currentTimeMillis() >= this.nextConnection) {
                this.nextConnection = System.currentTimeMillis() + 30000L;
                this.connection = new SocketConnection(this, this.client.getLocalPlayer().getName());
                new Thread(this.connection).start();
            }
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getKey().equals("infobox")) {
            if (this.config.infobox()) {
                this.infoBoxManager.addInfoBox((InfoBox)this.connectionIB);
            } else {
                this.infoBoxManager.removeInfoBox((InfoBox)this.connectionIB);
            }
        }
        if (this.config.disableChatMessages()) {
            return;
        }
        if (event.getGroup().equals(CONFIG_VERSION) && !event.getKey().equals("infobox")) {
            this.clientThread.invoke(() -> this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=b4281e>Configuration changed. Please restart the plugin to see updates.", null));
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGIN_SCREEN && this.connection != null) {
            this.connection.terminate(false);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Subscribe
    public void onSocketBroadcastPacket(SocketBroadcastPacket packet) {
        try {
            PrintWriter outputStream;
            if (this.connection == null || this.connection.getState() != SocketState.CONNECTED) {
                return;
            }
            String data = packet.getPayload().toString();
            log.debug("Deploying packet from client: {}", (Object)data);
            String secret = this.config.getPassword() + PASSWORD_SALT;
            JSONObject payload = new JSONObject();
            payload.put("header", "BROADCAST");
            payload.put("payload", AES256.encrypt(secret, data));
            PrintWriter printWriter = outputStream = this.connection.getOutputStream();
            synchronized (printWriter) {
                outputStream.println(payload.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("An error has occurred while trying to broadcast a packet.", (Throwable)e);
        }
    }

    private void statCheckOnGameTick() {
        if (this.client == null || this.client.getLocalPlayer() == null) {
            return;
        }
        if (this.deferredCheck != null && this.client.getTickCount() == this.deferredCheck.getTick()) {
            this.checkStats();
            this.deferredCheck = null;
        }
    }

    private boolean isInRegion(int regionID) {
        List<Integer> regions = Arrays.asList(12631, 13125, 13122, 13123, 13379, 12612, 12611, 12867);
        return regions.contains(regionID);
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        this.onCheckAnimationChanged(event);
    }

    private boolean ignoredNPCs(NPC target) {
        if (target != null && target.getName() != null) {
            return !VERZIK_P2_IDS.contains(target.getId());
        }
        return false;
    }

    private boolean otherShitBow(int i) {
        int[] e;
        for (int i2 : e = new int[]{861, 12788, 22550, 22547}) {
            if (i2 != i) continue;
            return true;
        }
        return false;
    }

    private void checkStats() {
        int[] hits;
        int anim = this.deferredCheck.getAnim();
        int hammerBop = 401;
        int godBop = 7045;
        int bow = 426;
        int clawSpec = 7514;
        int clawBop = 393;
        int whip = 1658;
        int chalyBop = 440;
        int chalySpec = 1203;
        int scy = 8056;
        int bggsSpec = 7643;
        int bggsSpec2 = 7642;
        int hammerSpec = 1378;
        int del = 1100;
        int lanceSmack = 8290;
        int lancePoke = 8288;
        for (int i : hits = new int[]{lancePoke, lanceSmack, clawSpec, clawBop, whip, chalySpec, scy, bggsSpec, bggsSpec2, hammerSpec}) {
            boolean is118;
            if (anim != i) continue;
            int lvl = this.client.getBoostedSkillLevel(Skill.STRENGTH);
            boolean piety = this.deferredCheck.isPiety();
            boolean bl = is118 = lvl >= 118;
            if (piety && is118) break;
            String s = "attacked";
            if (i == clawSpec) {
                s = "claw speced";
            } else if (i == chalySpec) {
                s = "chally speced";
            } else if (i == bggsSpec || i == bggsSpec2) {
                s = "bgs speced";
            } else if (i == hammerSpec) {
                s = "hammer speced";
            }
            Object s2 = "";
            s2 = !piety ? (!is118 ? " with " + lvl + " strength and without piety." : " without piety.") : " with " + lvl + " strength.";
            this.flagMesOut("You " + s + (String)s2);
            break;
        }
    }

    public static int getCurrentRegionID(Client client) {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return -1;
        }
        WorldPoint wp = WorldPoint.fromLocalInstance((Client)client, (LocalPoint)localPlayer.getLocalLocation());
        return wp == null ? -1 : wp.getRegionID();
    }

    private void onCheckAnimationChanged(AnimationChanged event) {
        if (event != null && event.getActor() instanceof Player) {
            Player p = (Player)event.getActor();
            if (p == null) {
                return;
            }
            int anim = p.getAnimation();
            if (p.getPlayerComposition() == null) {
                return;
            }
            int wep = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
            int hammerBop = 401;
            int godBop = 7045;
            int bow = 426;
            int lanceSmack = 8290;
            int lancePoke = 8288;
            int clawSpec = 7514;
            int clawBop = 393;
            int whip = 1658;
            int chalyBop = 440;
            int chalySpec = 1203;
            int scy = 8056;
            int bggsSspec = 7643;
            int hammerSpec = 1378;
            int trident = 1167;
            int surge = 7855;
            Actor interacting = p.getInteracting();
            NPC target = null;
            if (p.getInteracting() != null && p.getInteracting() instanceof NPC) {
                target = (NPC)interacting;
            }
            if (p.equals((Object)this.client.getLocalPlayer()) && anim != 0 && anim != -1) {
                if (!this.ignoredNPCs(target)) {
                    int style = this.client.getVar(VarPlayer.ATTACK_STYLE);
                    if (anim == scy) {
                        String b = "";
                        if (style == 0) {
                            b = "accurate";
                        } else if (style == 2) {
                            b = "crush";
                        } else if (style == 3) {
                            b = "defensive";
                        }
                        if (!b.equals("")) {
                            if (this.isInRegion(SocketPlugin.getCurrentRegionID(this.client))) {
                                this.flagMesOut("You scythed on " + b + ".");
                            } else if (!b.equals("crush")) {
                                this.flagMesOut("You scythed on " + b + ".");
                            }
                        }
                    } else if (anim == bow && !this.otherShitBow(wep) && !this.client.isPrayerActive(Prayer.RIGOUR)) {
                        this.flagMesOut("You bowed without rigour active.");
                    } else if (anim == hammerBop && wep == 13576) {
                        this.flagMesOut("You hammer bopped.");
                    } else if (anim == godBop) {
                        this.flagMesOut("You godsword bopped.");
                    } else if (anim == chalyBop) {
                        this.flagMesOut("You chaly poked.");
                    }
                }
                this.deferredCheck = new DeferredCheck(this.client.getTickCount(), anim, wep, this.client.isPrayerActive(Prayer.PIETY));
            }
        }
    }

    private void flagMesOut(String mes) {
        if (this.client != null && this.client.getLocalPlayer() != null && this.client.getLocalPlayer().getName() != null) {
            if (config.dontsend() && config.printMyLeach()){

                String mes2 = ColorUtil.prependColorTag((String)mes, Color.WHITE);
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", mes2, "");
            }
            if (config.dontsend()){
                return;
            }
            String finalS = mes.toLowerCase().replaceAll("you ", this.client.getLocalPlayer().getName() + " ");
            JSONArray data = new JSONArray();
            JSONObject json$ = new JSONObject();
            json$.put("print", finalS);
            json$.put("sender", this.client.getLocalPlayer().getName());
            int[] mapRegions = this.client.getMapRegions() == null ? new int[0] : this.client.getMapRegions();
            json$.put("mapregion", Arrays.toString(mapRegions));
            json$.put("raidbit", this.client.getVarbitValue(5432));
            data.put(json$);
            JSONObject send = new JSONObject();
            send.put("sLeech", data);
            this.eventBus.post((Object)new SocketBroadcastPacket(send));
        }
    }

    public Client getClient() {
        return this.client;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public ClientThread getClientThread() {
        return this.clientThread;
    }

    public SocketConfig getConfig() {
        return this.config;
    }

    public InfoBoxManager getInfoBoxManager() {
        return this.infoBoxManager;
    }

    public long getNextConnection() {
        return this.nextConnection;
    }

    public void setNextConnection(long nextConnection) {
        this.nextConnection = nextConnection;
    }

    public static class DeferredCheck {
        private int tick;
        private int anim;
        private int wep;
        private boolean piety;

        public DeferredCheck(int tick, int anim, int wep, boolean piety) {
            this.tick = tick;
            this.anim = anim;
            this.wep = wep;
            this.piety = piety;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof DeferredCheck)) {
                return false;
            }
            DeferredCheck other = (DeferredCheck)o;
            return other.canEqual(this) && this.getTick() == other.getTick() && this.getAnim() == other.getAnim() && this.getWep() == other.getWep() && this.isPiety() == other.isPiety();
        }

        protected boolean canEqual(Object other) {
            return other instanceof DeferredCheck;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getTick();
            result = result * 59 + this.getAnim();
            result = result * 59 + this.getWep();
            return result * 59 + (this.isPiety() ? 79 : 97);
        }

        public String toString() {
            return "SocketPlugin.DeferredCheck(tick=" + this.getTick() + ", anim=" + this.getAnim() + ", wep=" + this.getWep() + ", piety=" + this.isPiety() + ")";
        }

        public int getTick() {
            return this.tick;
        }

        public void setTick(int tick) {
            this.tick = tick;
        }

        public int getAnim() {
            return this.anim;
        }

        public void setAnim(int anim) {
            this.anim = anim;
        }

        public int getWep() {
            return this.wep;
        }

        public void setWep(int wep) {
            this.wep = wep;
        }

        public boolean isPiety() {
            return this.piety;
        }

        public void setPiety(boolean piety) {
            this.piety = piety;
        }
    }
}

