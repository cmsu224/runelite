/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.GameTick
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
 *  org.pf4j.Extension
 */
package net.runelite.client.plugins.socket.plugins.socketvangpots;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
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
import net.runelite.client.plugins.socket.plugins.socketvangpots.CoxUtil;
import net.runelite.client.plugins.socket.plugins.socketvangpots.SocketVangPotsConfig;
import net.runelite.client.plugins.socket.plugins.socketvangpots.SocketVangsOverlayPanel;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;


@PluginDescriptor(name="Socket - Vanguard Pots", description="Lets the prepper know how many Overloads were dropped at Vanguards", tags={"cox", "chambers", "xeric", "spoon", "spoonlite", "overload", "raid"})
@PluginDependency(value=SocketPlugin.class)
public class SocketVangPotsPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private EventBus eventBus;
    @Inject
    private SocketVangPotsConfig config;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SocketVangsOverlayPanel overlay;
    public int overloadsDropped = 0;
    public boolean inRoom = false;
    private int roomtype = -1;
    private int plane;
    private int base_x;
    private int base_y;
    int room_base_x;
    int room_base_y;
    int rot;
    int wind;

    @Provides
    SocketVangPotsConfig provideConfig(ConfigManager configManager) {
        return (SocketVangPotsConfig)configManager.getConfig(SocketVangPotsConfig.class);
    }

    protected void startUp() throws Exception {
        this.reset();
    }

    protected void shutDown() throws Exception {
        this.reset();
    }

    protected void reset() {
        this.overloadsDropped = 0;
    }

    @Subscribe
    public void onNpcLootReceived(NpcLootReceived event) {
        if (event.getNpc().getName() != null && event.getNpc().getName().equalsIgnoreCase("vanguard") && this.client.getVarbitValue(5432) == 1 && this.client.getLocalPlayer() != null) {
            for (ItemStack item : event.getItems()) {
                if (item.getId() != 20996) continue;
                ++this.overloadsDropped;
                this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " got an Overload");
                JSONObject data = new JSONObject();
                data.put("player", this.client.getLocalPlayer().getName());
                JSONObject payload = new JSONObject();
                payload.put("socketvangpots", data);
                this.eventBus.post((Object)new SocketBroadcastPacket(payload));
            }
        }
    }

    private void sendFlag(String msg) {
        JSONArray data = new JSONArray();
        JSONObject jsonmsg = new JSONObject();
        jsonmsg.put("msg", msg);
        data.put(jsonmsg);
        JSONObject send = new JSONObject();
        send.put("socketvangpotsmsg", data);
        this.eventBus.post((Object)new SocketBroadcastPacket(send));
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            JSONObject payload = event.getPayload();
            if (payload.has("socketvangpots")) {
                JSONObject data = payload.getJSONObject("socketvangpots");
                if (!data.getString("player").equals(this.client.getLocalPlayer().getName())) {
                    ++this.overloadsDropped;
                }
            } else if (payload.has("socketvangpotsmsg") && this.config.showChatMessage()) {
                JSONArray data = payload.getJSONArray("socketvangpotsmsg");
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

    @Subscribe
    public void onGameTick(GameTick e) {
        if (this.client.getVarbitValue(5432) == 0) {
            if (this.roomtype != -1) {
                try {
                    this.shutDown();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return;
        }
        int plane = this.client.getPlane();
        int base_x = this.client.getBaseX();
        int base_y = this.client.getBaseY();
        if (this.base_x != base_x || this.base_y != base_y || this.plane != plane) {
            this.base_x = base_x;
            this.base_y = base_y;
            this.plane = plane;
            this.searchForVanguards();
        }
        WorldPoint wp = this.client.getLocalPlayer().getWorldLocation();
        int x = wp.getX() - this.client.getBaseX();
        int y = wp.getY() - this.client.getBaseY();
        int type = CoxUtil.getroom_type(this.client.getInstanceTemplateChunks()[plane][x / 8][y / 8]);
        if (type != this.roomtype) {
            if (type == 6 || type == 3) {
                this.overlayManager.add((Overlay)this.overlay);
            } else if (this.roomtype == 6 || this.roomtype == 3) {
                this.overlayManager.remove((Overlay)this.overlay);
            }
            this.roomtype = type;
        }
    }

    private void searchForVanguards() {
        int[][] templates = this.client.getInstanceTemplateChunks()[this.plane];
        for (int cx = 0; cx < 13; cx += 4) {
            for (int cy = 0; cy < 13; cy += 4) {
                int template = templates[cx][cy];
                int tx = template >> 14 & 0x3FF;
                int ty = template >> 3 & 0x7FF;
                if (CoxUtil.getroom_type(template) != 6) continue;
                this.rot = CoxUtil.room_rot(template);
                if (this.rot == 0) {
                    this.room_base_x = cx - (tx & 3) << 3;
                    this.room_base_y = cy - (ty & 3) << 3;
                } else if (this.rot == 1) {
                    this.room_base_x = cx - (ty & 3) << 3;
                    this.room_base_y = cy + (tx & 3) << 3 | 7;
                } else if (this.rot == 2) {
                    this.room_base_x = cx + (tx & 3) << 3 | 7;
                    this.room_base_y = cy + (ty & 3) << 3 | 7;
                } else if (this.rot == 3) {
                    this.room_base_x = cx + (ty & 3) << 3 | 7;
                    this.room_base_y = cy - (tx & 3) << 3;
                }
                this.wind = CoxUtil.room_winding(template);
            }
        }
    }
}

