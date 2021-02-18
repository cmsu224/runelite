//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.sotetseg;

import com.google.inject.Provides;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "Socket - Sotetseg",
        description = "Extended plugin handler for Sotetseg in the Theatre of Blood.",
        tags = {"socket", "server", "discord", "connection", "broadcast", "sotetseg", "theatre", "tob"},
        enabledByDefault = false
)
public class SotetsegPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private EventBus eventBus;
    @Inject
    private SotetsegConfig config;
    @Inject
    private SotetsegOverlay overlay;
    private boolean sotetsegActive;
    private NPC sotetsegNPC;
    private LinkedHashSet<Point> redTiles;
    private Set<WorldPoint> mazePings;
    private int dispatchCount;
    private boolean wasInUnderworld;
    private int overworldRegionID;

    public SotetsegPlugin() {
    }

    @Provides
    SotetsegConfig getConfig(ConfigManager configManager) {
        return (SotetsegConfig)configManager.getConfig(SotetsegConfig.class);
    }

    protected void startUp() {
        this.sotetsegActive = false;
        this.sotetsegNPC = null;
        this.redTiles = new LinkedHashSet();
        this.mazePings = Collections.synchronizedSet(new HashSet());
        this.dispatchCount = 5;
        this.wasInUnderworld = false;
        this.overworldRegionID = -1;
        this.overlayManager.add(this.overlay);
    }

    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        NPC npc = event.getNpc();
        switch(npc.getId()) {
            case 8387:
            case 8388:
                this.sotetsegActive = true;
                this.sotetsegNPC = npc;
            default:
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        NPC npc = event.getNpc();
        switch(npc.getId()) {
            case 8387:
            case 8388:
                if (this.client.getPlane() != 3) {
                    this.sotetsegActive = false;
                    this.sotetsegNPC = null;
                }
            default:
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.sotetsegActive) {
            Player player = this.client.getLocalPlayer();
            if (this.sotetsegNPC != null && this.sotetsegNPC.getId() == 8388) {
                this.redTiles.clear();
                this.mazePings.clear();
                this.dispatchCount = 5;
                if (this.isInOverWorld()) {
                    this.wasInUnderworld = false;
                    if (player != null && player.getWorldLocation() != null) {
                        WorldPoint wp = player.getWorldLocation();
                        this.overworldRegionID = wp.getRegionID();
                    }
                }
            }

            if (!this.redTiles.isEmpty() && this.wasInUnderworld && this.dispatchCount > 0) {
                --this.dispatchCount;
                JSONArray data = new JSONArray();
                Iterator var4 = this.redTiles.iterator();

                while(var4.hasNext()) {
                    Point p = (Point)var4.next();
                    WorldPoint wp = this.translateMazePoint(p);
                    JSONObject jsonwp = new JSONObject();
                    jsonwp.put("x", wp.getX());
                    jsonwp.put("y", wp.getY());
                    jsonwp.put("plane", wp.getPlane());
                    data.put(jsonwp);
                }

                JSONObject payload = new JSONObject();
                payload.put("sotetseg-extended", data);
                this.eventBus.post(new SocketBroadcastPacket(payload));
            }
        }

    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            JSONObject payload = event.getPayload();
            if (!payload.has("sotetseg-extended")) {
                return;
            }

            this.mazePings.clear();
            JSONArray data = payload.getJSONArray("sotetseg-extended");

            for(int i = 0; i < data.length(); ++i) {
                JSONObject jsonwp = data.getJSONObject(i);
                int x = jsonwp.getInt("x");
                int y = jsonwp.getInt("y");
                int plane = jsonwp.getInt("plane");
                WorldPoint wp = new WorldPoint(x, y, plane);
                this.mazePings.add(wp);
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        if (this.sotetsegActive) {
            GroundObject o = event.getGroundObject();
            if (o.getId() == 33035) {
                Tile t = event.getTile();
                WorldPoint p = WorldPoint.fromLocal(this.client, t.getLocalLocation());
                Point point = new Point(p.getRegionX(), p.getRegionY());
                if (this.isInOverWorld()) {
                    this.redTiles.add(new Point(point.getX() - 9, point.getY() - 22));
                }

                if (this.isInUnderWorld()) {
                    this.redTiles.add(new Point(point.getX() - 42, point.getY() - 31));
                    this.wasInUnderworld = true;
                }
            }
        }

    }

    private boolean isInOverWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13123;
    }

    private boolean isInUnderWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13379;
    }

    private WorldPoint translateMazePoint(Point mazePoint) {
        Player p = this.client.getLocalPlayer();
        if (this.overworldRegionID == -1 && p != null) {
            WorldPoint wp = p.getWorldLocation();
            return WorldPoint.fromRegion(wp.getRegionID(), mazePoint.getX() + 9, mazePoint.getY() + 22, 0);
        } else {
            return WorldPoint.fromRegion(this.overworldRegionID, mazePoint.getX() + 9, mazePoint.getY() + 22, 0);
        }
    }

    public boolean isSotetsegActive() {
        return this.sotetsegActive;
    }

    public Set<WorldPoint> getMazePings() {
        return this.mazePings;
    }
}
