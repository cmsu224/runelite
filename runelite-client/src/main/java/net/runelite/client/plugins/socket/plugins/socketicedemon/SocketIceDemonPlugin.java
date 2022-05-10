/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.NPC
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.GameObjectDespawned
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.GraphicsObjectCreated
 *  net.runelite.api.events.ItemContainerChanged
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.NpcSpawned
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  org.pf4j.Extension
 */
package net.runelite.client.plugins.socket.plugins.socketicedemon;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.socket.plugins.socketicedemon.SocketIceDemonConfig;
import net.runelite.client.plugins.socket.plugins.socketicedemon.SocketIceDemonOverlay;
import net.runelite.client.plugins.socket.plugins.socketicedemon.SocketIceDemonPanelOverlay;
import net.runelite.client.plugins.socket.plugins.socketicedemon.util.Raids1Util;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(name="Socket - Ice Demon", description="Socket ice demon", tags={"xeric", "iceDemon", "chambers", "cox", "socket"})
@PluginDependency(value=SocketPlugin.class)
public class SocketIceDemonPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SocketIceDemonOverlay overlay;
    @Inject
    private SocketIceDemonPanelOverlay panelOverlay;
    @Inject
    private SocketIceDemonConfig config;
    @Inject
    private EventBus eventBus;
    public int roomtype = -1;
    private int plane;
    private int base_x;
    private int base_y;
    int room_base_x;
    int room_base_y;
    int rot;
    int wind;
    int lastKindling = 0;
    int teamTotalKindlingCut = 0;
    int teamTotalKindlingLit = 0;
    int teamKindlingNeeded = 0;
    boolean dumpKindling = false;
    boolean allKindlingLit = false;
    int litBraziers = 0;
    boolean cuttingTree = false;
    boolean lightingBrazier = false;
    ArrayList<GameObject> unlitBrazierList = new ArrayList();
    NPC iceDemon = null;
    int iceDemonActivateTicks = 10;
    boolean iceDemonActive = false;
    int deadTree = 29764;
    boolean treeKilled = false;
    ArrayList<Integer> chopAnimationList = new ArrayList<Integer>(Arrays.asList(879, 877, 875, 873, 871, 869, 867, 8303, 2846, 2117, 7264, 8324, 8778, 24));
    ArrayList<String> playerNameList = new ArrayList();
    ArrayList<Integer> playerKindlingList = new ArrayList();
    boolean dumpedIntoLit = false;
    boolean litMessage = false;
    private boolean mirrorMode;

    @Provides
    SocketIceDemonConfig getConfig(ConfigManager configManager) {
        return (SocketIceDemonConfig)configManager.getConfig(SocketIceDemonConfig.class);
    }

    protected void startUp() throws Exception {
        this.reset();
    }

    protected void shutDown() throws Exception {
        this.reset();
        this.overlayManager.remove((Overlay)this.overlay);
        this.overlayManager.remove((Overlay)this.panelOverlay);
    }

    protected void reset() {
        this.roomtype = -1;
        this.lastKindling = 0;
        this.teamTotalKindlingCut = 0;
        this.teamTotalKindlingLit = 0;
        this.teamKindlingNeeded = 0;
        this.dumpKindling = false;
        this.allKindlingLit = false;
        this.litBraziers = 0;
        this.cuttingTree = false;
        this.lightingBrazier = false;
        this.unlitBrazierList.clear();
        this.iceDemon = null;
        this.iceDemonActivateTicks = 10;
        this.iceDemonActive = false;
        this.treeKilled = false;
        this.playerNameList.clear();
        this.playerKindlingList.clear();
        this.dumpedIntoLit = false;
        this.litMessage = false;
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
            this.searchForIceDemon();
        }
        WorldPoint wp = this.client.getLocalPlayer().getWorldLocation();
        int x = wp.getX() - this.client.getBaseX();
        int y = wp.getY() - this.client.getBaseY();
        int type = Raids1Util.getroom_type(this.client.getInstanceTemplateChunks()[plane][x / 8][y / 8]);
        if (type != this.roomtype) {
            if (type == 12) {
                this.overlayManager.add((Overlay)this.overlay);
                this.overlayManager.add((Overlay)this.panelOverlay);
            } else if (this.roomtype == 12) {
                if (type == 2 && this.config.display4Scav()) {
                    this.overlayManager.remove((Overlay)this.overlay);
                } else {
                    this.overlayManager.remove((Overlay)this.panelOverlay);
                    this.overlayManager.remove((Overlay)this.overlay);
                }
            } else if (type == 2 && this.config.display4Scav()) {
                this.overlayManager.add((Overlay)this.panelOverlay);
            } else {
                this.overlayManager.remove((Overlay)this.panelOverlay);
                this.overlayManager.remove((Overlay)this.overlay);
            }
            this.roomtype = type;
        }
        if (this.iceDemonActive) {
            --this.iceDemonActivateTicks;
            if (this.iceDemonActivateTicks <= 0) {
                this.iceDemonActive = false;
                this.iceDemonActivateTicks = 10;
                this.iceDemon = null;
            }
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned e) {
        if (this.client.getVarbitValue(5432) == 1) {
            if (e.getGameObject().getId() == 29748) {
                ++this.litBraziers;
                if (this.litBraziers > 4) {
                    this.litBraziers = 4;
                }
            } else if (e.getGameObject().getId() == 29747) {
                if (this.unlitBrazierList.size() < 4 && e.getGameObject().getPlane() == this.client.getPlane()) {
                    this.unlitBrazierList.add(e.getGameObject());
                }
            } else if (e.getGameObject().getId() == this.deadTree) {
                LocalPoint treeLP = LocalPoint.fromWorld((Client)this.client, (WorldPoint)e.getGameObject().getWorldLocation());
                LocalPoint playerLP = LocalPoint.fromWorld((Client)this.client, (WorldPoint)this.client.getLocalPlayer().getWorldLocation());
                if (treeLP != null && playerLP != null && e.getGameObject().getLocalLocation().distanceTo(this.client.getLocalPlayer().getLocalLocation()) == 128) {
                    this.cuttingTree = true;
                    this.treeKilled = true;
                }
            }
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned e) {
        if (this.client.getVarbitValue(5432) == 1) {
            if (e.getGameObject().getId() == 29748) {
                --this.litBraziers;
                if (this.litBraziers < 0) {
                    this.litBraziers = 0;
                }
            } else if (e.getGameObject().getId() == 29747 && e.getGameObject().getPlane() == this.client.getPlane()) {
                this.unlitBrazierList.remove((Object)e.getGameObject());
            }
        }
    }

    @Subscribe
    public void onGraphicsObjectCreated(GraphicsObjectCreated event) {
        if (this.client.getVarbitValue(5432) == 1 && event.getGraphicsObject().getId() == 188) {
            this.iceDemonActive = true;
            this.iceDemonActivateTicks = 10;
        }
    }

    @Subscribe
    private void onNpcSpawned(NpcSpawned event) {
        if (this.client.getVarbitValue(5432) == 1 && event.getNpc().getId() == 7584) {
            this.iceDemon = event.getNpc();
        }
    }

    @Subscribe
    private void onNpcDespawned(NpcDespawned event) {
        if (this.client.getVarbitValue(5432) == 1 && event.getNpc().getId() == 7584) {
            this.iceDemon = null;
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged e) {
        if (this.client.getVarbitValue(5432) == 1 && e.getActor().getName() != null && this.client.getLocalPlayer() != null && e.getActor().getName().equals(this.client.getLocalPlayer().getName())) {
            if (this.chopAnimationList.contains(e.getActor().getAnimation())) {
                this.cuttingTree = true;
            } else if (e.getActor().getAnimation() == 3687 || e.getActor().getAnimation() == 832 && this.roomtype == 12) {
                if (e.getActor().getAnimation() == 832) {
                    this.dumpedIntoLit = true;
                }
                this.lightingBrazier = true;
            } else {
                this.cuttingTree = false;
            }
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged e) {
        if (this.client.getVarbitValue(5432) == 1 && e.getContainerId() == 93) {
            int currentKindling = e.getItemContainer().count(20799);
            if (this.cuttingTree) {
                if (currentKindling > this.lastKindling) {
                    int diff = currentKindling - this.lastKindling;
                    JSONObject data = new JSONObject();
                    data.put("player", this.client.getLocalPlayer().getName());
                    data.put("kindling", diff);
                    JSONObject payload = new JSONObject();
                    payload.put("socketicecut", data);
                    this.eventBus.post((Object)new SocketBroadcastPacket(payload));
                    this.teamTotalKindlingCut += diff;
                    if (this.teamTotalKindlingCut >= this.teamKindlingNeeded && this.config.dumpMsg()) {
                        if (!this.dumpKindling) {
                            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ff0000>DUMP YOUR KINDLING!", "");
                        }
                        this.dumpKindling = true;
                    }
                    if (!this.playerNameList.contains(this.client.getLocalPlayer().getName())) {
                        this.playerNameList.add(this.client.getLocalPlayer().getName());
                        this.playerKindlingList.add(0);
                    }
                    int index = this.playerNameList.indexOf(this.client.getLocalPlayer().getName());
                    this.playerKindlingList.set(index, this.playerKindlingList.get(index) + diff);
                }
                if (this.treeKilled) {
                    this.treeKilled = false;
                    this.cuttingTree = false;
                }
            } else if (this.lightingBrazier) {
                if (currentKindling == 0) {
                    JSONObject data = new JSONObject();
                    data.put("player", this.client.getLocalPlayer().getName());
                    data.put("kindling", this.lastKindling);
                    JSONObject payload = new JSONObject();
                    payload.put("socketicelight", data);
                    this.eventBus.post((Object)new SocketBroadcastPacket(payload));
                    if (this.client.getVarbitValue(5424) == 1) {
                        if (this.lastKindling <= 17) {
                            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ff0000> Are you daft, cunt?!", "");
                        }
                    } else {
                        if (this.dumpedIntoLit && this.litBraziers < 4) {
                            this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " dumped " + this.lastKindling + " kindling into a lit brazier");
                            this.litMessage = true;
                        }
                        if (!this.litMessage && this.lastKindling <= 17) {
                            this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " only dumped " + this.lastKindling + " kindling");
                        }
                        this.litMessage = false;
                        this.dumpedIntoLit = false;
                    }
                    this.teamTotalKindlingLit += this.lastKindling;
                    if (this.config.dumpMsg() && this.teamTotalKindlingLit >= this.teamKindlingNeeded) {
                        if (!this.allKindlingLit) {
                            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ff0000>KINDLING DUMPED.... Ice Demon starting!", "");
                        }
                        this.allKindlingLit = true;
                    }
                }
                this.lightingBrazier = false;
            }
            this.lastKindling = currentKindling;
        }
    }

    private void searchForIceDemon() {
        int[][] templates = this.client.getInstanceTemplateChunks()[this.plane];
        for (int cx = 0; cx < 12; cx += 4) {
            for (int cy = 0; cy < 12; cy += 4) {
                int template = templates[cx][cy];
                int tx = template >> 14 & 0x3FF;
                int ty = template >> 3 & 0x7FF;
                if (Raids1Util.getroom_type(template) != 12) continue;
                this.rot = Raids1Util.getroom_rot(template);
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
                this.wind = Raids1Util.getroom_winding(template);
            }
        }
    }

    private void sendFlag(String msg) {
        JSONArray data = new JSONArray();
        JSONObject jsonmsg = new JSONObject();
        jsonmsg.put("msg", msg);
        data.put(jsonmsg);
        JSONObject send = new JSONObject();
        send.put("socketicealt", data);
        this.eventBus.post((Object)new SocketBroadcastPacket(send));
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        if (this.client.getVarbitValue(5432) == 1) {
            try {
                JSONObject payload = event.getPayload();
                if (payload.has("socketicecut")) {
                    JSONObject data = payload.getJSONObject("socketicecut");
                    String name = data.getString("player");
                    if (!name.equals(this.client.getLocalPlayer().getName())) {
                        if (!this.playerNameList.contains(name)) {
                            this.playerNameList.add(name);
                            this.playerKindlingList.add(0);
                        }
                        int kindlingCount = data.getInt("kindling");
                        this.teamTotalKindlingCut += kindlingCount;
                        int index = this.playerNameList.indexOf(name);
                        this.playerKindlingList.set(index, this.playerKindlingList.get(index) + kindlingCount);
                        if (this.teamTotalKindlingCut >= this.teamKindlingNeeded && this.config.dumpMsg()) {
                            if (!this.dumpKindling) {
                                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ff0000>DUMP YOUR KINDLING!", "");
                            }
                            this.dumpKindling = true;
                        }
                    }
                } else if (payload.has("socketicelight")) {
                    JSONObject data = payload.getJSONObject("socketicelight");
                    if (!data.getString("player").equals(this.client.getLocalPlayer().getName())) {
                        int kindlingCount = data.getInt("kindling");
                        this.teamTotalKindlingLit += kindlingCount;
                        if (this.config.dumpMsg() && this.teamTotalKindlingLit >= this.teamKindlingNeeded) {
                            if (!this.allKindlingLit) {
                                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ff0000>KINDLING DUMPED.... Ice Demon starting!", "");
                            }
                            this.allKindlingLit = true;
                        }
                    }
                } else if (payload.has("socketicealt")) {
                    JSONArray data = payload.getJSONArray("socketicealt");
                    JSONObject jsonmsg = data.getJSONObject(0);
                    String msg = jsonmsg.getString("msg");
                    this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        boolean tempInRaid;
        boolean bl = tempInRaid = this.client.getVarbitValue(5432) == 1;
        if (!tempInRaid) {
            this.reset();
            this.overlayManager.remove((Overlay)this.panelOverlay);
            this.overlayManager.remove((Overlay)this.overlay);
        } else {
            this.teamKindlingNeeded = this.client.getVarbitValue(5424) == 1 ? 48 : (this.client.getVarbitValue(5424) >= 5 ? (this.client.getVarbitValue(5424) - 1) * 18 : 36 + this.client.getVarbitValue(5424) * 12);
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e) {
        if (this.client.getLocalPlayer() != null) {
            WorldPoint wp = this.client.getLocalPlayer().getWorldLocation();
            int x = wp.getX() - this.client.getBaseX();
            int y = wp.getY() - this.client.getBaseY();
            int type = Raids1Util.getroom_type(this.client.getInstanceTemplateChunks()[this.plane][x / 8][y / 8]);
            if (!this.config.display4Scav() && type != 12) {
                this.overlayManager.remove((Overlay)this.panelOverlay);
                this.overlayManager.remove((Overlay)this.overlay);
            } else if (this.config.display4Scav() && type == 2) {
                this.overlayManager.add((Overlay)this.panelOverlay);
                this.overlayManager.add((Overlay)this.overlay);
            }
        }
    }
}

