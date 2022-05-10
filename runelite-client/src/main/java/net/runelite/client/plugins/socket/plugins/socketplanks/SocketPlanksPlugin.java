/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.ItemContainerChanged
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.NpcLootReceived
 *  net.runelite.client.game.ItemStack
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  org.apache.commons.lang3.StringUtils
 *  org.pf4j.Extension
 */
package net.runelite.client.plugins.socket.plugins.socketplanks;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.socket.plugins.socketplanks.SocketPlanksConfig;
import net.runelite.client.plugins.socket.plugins.socketplanks.SocketPlanksOverlay;
import net.runelite.client.plugins.socket.plugins.socketplanks.SocketPlanksOverlayPanel;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.StringUtils;

@PluginDescriptor(name="Socket - Planks", description="Aint letting these bastards get away with shit", tags={"cox"})
@PluginDependency(value=SocketPlugin.class)
public class SocketPlanksPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private EventBus eventBus;
    @Inject
    private SocketPlanksOverlay overlay;
    @Inject
    private SocketPlanksOverlayPanel overlayPanel;
    public boolean planksDropped = false;
    public boolean planksPickedUp = false;
    public int mostPlanks = 0;
    public int plankCount = 0;
    public boolean chestBuilt = false;
    public int planksDroppedTime = -1;
    public int planksPickedUpTime = -1;
    public int chestBuiltTime = -1;
    public String planksPickedUpTimeStr = "";
    public String chestBuiltTimeStr = "";
    public int splitTimerDelay = 8;
    public String nameGotPlanks = "";
    public WorldPoint planksDroppedTile = null;
    private boolean mirrorMode;

    @Provides
    SocketPlanksConfig provideConfig(ConfigManager configManager) {
        return (SocketPlanksConfig)configManager.getConfig(SocketPlanksConfig.class);
    }

    protected void startUp() throws Exception {
        this.reset();
        this.overlayManager.add((Overlay)this.overlay);
        this.overlayManager.add((Overlay)this.overlayPanel);
    }

    protected void shutDown() throws Exception {
        this.reset();
        this.overlayManager.remove((Overlay)this.overlay);
        this.overlayManager.remove((Overlay)this.overlayPanel);
    }

    protected void reset() {
        this.planksDropped = false;
        this.planksPickedUp = false;
        this.plankCount = 0;
        this.mostPlanks = 0;
        this.chestBuilt = false;
        this.planksDroppedTime = -1;
        this.planksPickedUpTime = -1;
        this.chestBuiltTime = -1;
        this.planksPickedUpTimeStr = "";
        this.chestBuiltTimeStr = "";
        this.splitTimerDelay = 8;
        this.nameGotPlanks = "";
        this.planksDroppedTile = null;
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.splitTimerDelay > 0 && this.chestBuilt) {
            --this.splitTimerDelay;
        }
    }

    @Subscribe
    public void onNpcLootReceived(NpcLootReceived event) {
        if (event.getNpc().getName() != null && event.getNpc().getName().equals("Scavenger beast") && this.client.getVarbitValue(5432) == 1) {
            for (ItemStack item : event.getItems()) {
                if (item.getId() != 21036 || this.planksDropped) continue;
                this.planksDropped = true;
                this.planksDroppedTime = this.timeToSeconds(this.getTime());
                this.planksDroppedTile = WorldPoint.fromLocal((Client)this.client, (LocalPoint)item.getLocation());
                this.nameGotPlanks = this.client.getLocalPlayer().getName();
                this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " got planks");
                JSONObject data = new JSONObject();
                data.put("player", this.client.getLocalPlayer().getName());
                data.put("time", this.planksDroppedTime);
                data.put("x", this.planksDroppedTile.getX());
                data.put("y", this.planksDroppedTile.getY());
                data.put("plane", this.planksDroppedTile.getPlane());
                JSONObject payload = new JSONObject();
                payload.put("socketplanksdropped", data);
                this.eventBus.post((Object)new SocketBroadcastPacket(payload));
            }
        }
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event) {
        if (this.client.getVarbitValue(5432) == 1 && event.getContainerId() == 93) {
            this.plankCount = event.getItemContainer().count(21036);
            if (this.plankCount > this.mostPlanks && this.plankCount >= 2 && !this.planksPickedUp) {
                int time = this.timeToSeconds(this.getTime()) - this.planksDroppedTime;
                if (time > 15) {
                    this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " took " + time + " seconds to pick up the fucking planks");
                } else {
                    this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " picked up planks");
                }
                this.planksPickedUp = true;
                this.planksPickedUpTime = this.timeToSeconds(this.getTime());
                this.planksPickedUpTimeStr = this.secondsToTime(this.planksPickedUpTime - this.planksDroppedTime);
                this.nameGotPlanks = "";
                JSONObject data = new JSONObject();
                data.put("player", this.client.getLocalPlayer().getName());
                data.put("time", this.planksPickedUpTime);
                data.put("timeStr", this.planksPickedUpTimeStr);
                JSONObject payload = new JSONObject();
                payload.put("socketplankspickedup", data);
                this.eventBus.post((Object)new SocketBroadcastPacket(payload));
            }
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        if (this.client.getVarbitValue(5432) == 1 && event.getActor().getName() != null && event.getActor().getName().equals(this.client.getLocalPlayer().getName()) && (event.getActor().getAnimation() == 3676 || event.getActor().getAnimation() == 7049) && !this.chestBuilt) {
            int time = this.timeToSeconds(this.getTime()) - this.planksPickedUpTime;
            if (time > 15) {
                this.sendFlag("<col=ff0000>Holy shit... " + this.client.getLocalPlayer().getName() + " took " + time + " seconds to build the fucking chest");
            } else {
                this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " built the chest");
            }
            this.chestBuilt = true;
            this.chestBuiltTime = this.timeToSeconds(this.getTime());
            this.chestBuiltTimeStr = this.secondsToTime(this.chestBuiltTime - this.planksPickedUpTime);
            String totalTime = this.secondsToTime(this.chestBuiltTime - this.planksDroppedTime);
            String msg = "Total Time: <col=ff0000>" + totalTime + "</col> Picked Up: <col=ff0000>" + this.planksPickedUpTimeStr + "</col> Chest Built: <col=ff0000>" + this.chestBuiltTimeStr;
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null);
            JSONObject data = new JSONObject();
            data.put("player", this.client.getLocalPlayer().getName());
            data.put("time", this.chestBuiltTime);
            data.put("timeStr", this.chestBuiltTimeStr);
            JSONObject payload = new JSONObject();
            payload.put("socketplanksbuilt", data);
            this.eventBus.post((Object)new SocketBroadcastPacket(payload));
        }
    }

    private void sendFlag(String msg) {
        JSONArray data = new JSONArray();
        JSONObject jsonmsg = new JSONObject();
        jsonmsg.put("msg", msg);
        data.put(jsonmsg);
        JSONObject send = new JSONObject();
        send.put("socketplanksmsg", data);
        this.eventBus.post((Object)new SocketBroadcastPacket(send));
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            JSONObject payload = event.getPayload();
            if (payload.has("socketplanksdropped")) {
                this.planksDropped = true;
                JSONObject data = payload.getJSONObject("socketplanksdropped");
                if (!data.getString("player").equals(this.client.getLocalPlayer().getName())) {
                    this.nameGotPlanks = data.getString("player");
                    this.planksDroppedTime = data.getInt("time");
                    int x = data.getInt("x");
                    int y = data.getInt("y");
                    int plane = data.getInt("plane");
                    this.planksDroppedTile = new WorldPoint(x, y, plane);
                }
            } else if (payload.has("socketplankspickedup")) {
                this.planksPickedUp = true;
                JSONObject data = payload.getJSONObject("socketplankspickedup");
                if (!data.getString("player").equals(this.client.getLocalPlayer().getName())) {
                    this.planksPickedUpTime = data.getInt("time");
                    this.planksPickedUpTimeStr = data.getString("timeStr");
                    this.nameGotPlanks = "";
                }
            } else if (payload.has("socketplanksbuilt")) {
                JSONObject data;
                if (!this.chestBuilt && !(data = payload.getJSONObject("socketplanksbuilt")).getString("player").equals(this.client.getLocalPlayer().getName())) {
                    this.chestBuiltTime = data.getInt("time");
                    this.chestBuiltTimeStr = data.getString("timeStr");
                    String totalTime = this.secondsToTime(this.chestBuiltTime - this.planksDroppedTime);
                    String msg = "Total Time: <col=ff0000>" + totalTime + "</col> Picked Up: <col=ff0000>" + this.planksPickedUpTimeStr + "</col> Chest Built: <col=ff0000>" + this.chestBuiltTimeStr;
                    this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null);
                }
                this.chestBuilt = true;
            } else if (payload.has("socketplanksmsg")) {
                JSONArray data = payload.getJSONArray("socketplanksmsg");
                JSONObject jsonmsg = data.getJSONObject(0);
                String msg = jsonmsg.getString("msg");
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    private void onVarbitChanged(VarbitChanged event) {
        if (this.client.getVarbitValue(5432) != 1) {
            this.reset();
        }
    }

    public String getTime() {
        int seconds = (int)Math.floor((double)this.client.getVarbitValue(6386) * 0.6);
        return this.secondsToTime(seconds);
    }

    public String secondsToTime(int seconds) {
        StringBuilder builder = new StringBuilder();
        if (seconds >= 3600) {
            builder.append((int)Math.floor(seconds / 3600) + ":");
        }
        seconds %= 3600;
        if (builder.toString().equals("")) {
            builder.append((int)Math.floor(seconds / 60));
        } else {
            builder.append(StringUtils.leftPad((String)String.valueOf((int)Math.floor(seconds / 60)), (int)2, (char)'0'));
        }
        builder.append(":");
        builder.append(StringUtils.leftPad((String)String.valueOf(seconds %= 60), (int)2, (char)'0'));
        return builder.toString();
    }

    private int timeToSeconds(String s) {
        int seconds = -1;
        String[] split = s.split(":");
        if (split.length == 2) {
            seconds = Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1]);
        }
        if (split.length == 3) {
            seconds = Integer.parseInt(split[0]) * 3600 + Integer.parseInt(split[1]) * 60 + Integer.parseInt(split[2]);
        }
        return seconds;
    }
}

