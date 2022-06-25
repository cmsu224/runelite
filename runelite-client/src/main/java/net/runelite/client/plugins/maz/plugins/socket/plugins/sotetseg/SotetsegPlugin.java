/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GroundObject
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.Tile
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.GroundObjectSpawned
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.NpcSpawned
 *  net.runelite.api.events.ProjectileMoved
 *  net.runelite.api.widgets.Widget
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.plugins.PluginManager
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  net.runelite.client.util.ColorUtil
 *  org.pf4j.Extension
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.sotetseg;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.maz.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;

@PluginDescriptor(name="Socket - Sotetseg", description="Extended plugin handler for Sotetseg in the Theatre of Blood.", tags={"socket", "server", "discord", "connection", "broadcast", "sotetseg", "theatre", "tob"})
@PluginDependency(value=SocketPlugin.class)
public class SotetsegPlugin
extends Plugin {
    static final String CONFIG_GROUP = "Socket Sotetseg";
    @Inject
    private ConfigManager configManager;
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
    private SotetsegConfig config;
    private boolean flashFlag;
    @Inject
    private SotetsegOverlay overlay;
    @Inject
    private MazeTrueTileOverlay mazeOverlay;
    @Inject
    private InvisibleBallOverlay ballOverlay;
    @Inject
    private InvisibleTargetsOverlay targetsOverlay;
    private boolean sotetsegActive;
    private NPC sotetsegNPC;
    public LinkedHashSet<Point> redTiles;
    private ArrayList<Point> cRedTiles;
    private ArrayList<Point> solvedRedTiles;
    private int firstTick = -1;
    public Set<WorldPoint> mazePings;
    private int dispatchCount;
    private boolean wasInUnderworld;
    private int overworldRegionID;
    private int underworldRegionID;
    private ArrayList<Point> mazeTiles;
    public ArrayList<Point> mazeSolved;
    private ArrayList<Point> mazePoints;
    private boolean mazeActive;
    private boolean showFirstTile;
    private int instanceTime;
    private int mazeSolvedIndex;
    private int movementCount = -1;
    private int mazeStartTick = -1;
    private int mazeEndTick = -1;
    private int soteEntryTick = 0;
    private int allOffMaze = 0;
    private int firstOffMaze = 0;
    private boolean chosen = false;
    private boolean checkingOffMaze = false;
    private boolean checkingAnyOffMaze = false;
    private boolean packetReceived = false;
    private boolean instanceActive = false;
    private boolean started = false;
    private boolean madePlayerList = false;
    private ArrayList<Player> playersRunningMaze;
    public int ballTick = 0;
    public int invisibleTicks = 0;
    public ArrayList<String> invisibleTargets = new ArrayList();
    private boolean mirrorMode;
    public boolean flashScreen = false;
    public boolean chosenTextScreen = false;

    @Provides
    SotetsegConfig getConfig(ConfigManager configManager) {
        return (SotetsegConfig)configManager.getConfig(SotetsegConfig.class);
    }

    protected void startUp() {
        this.sotetsegActive = false;
        this.sotetsegNPC = null;
        this.redTiles = new LinkedHashSet();
        this.cRedTiles = new ArrayList();
        this.solvedRedTiles = new ArrayList();
        this.mazePings = Collections.synchronizedSet(new HashSet());
        this.dispatchCount = 5;
        this.wasInUnderworld = false;
        this.overworldRegionID = -1;
        this.underworldRegionID = -1;
        this.packetReceived = false;
        this.mazePoints = new ArrayList();
        this.mazeTiles = new ArrayList();
        this.mazeSolved = new ArrayList();
        this.overlayManager.add((Overlay)this.overlay);
        this.sotetsegNPC = null;
        this.sotetsegActive = false;
        this.mazeSolvedIndex = -1;
        this.started = false;
        this.checkingOffMaze = false;
        this.checkingAnyOffMaze = false;
        this.instanceTime = 2;
        this.playersRunningMaze = new ArrayList();
        this.flashFlag = true;
        this.invisibleTargets = new ArrayList();
        this.overlayManager.add((Overlay)this.overlay);
        this.overlayManager.add((Overlay)this.mazeOverlay);
        this.overlayManager.add((Overlay)this.ballOverlay);
        this.overlayManager.add((Overlay)this.targetsOverlay);
    }

    protected void shutDown() {
        this.overlayManager.remove((Overlay)this.overlay);
        this.overlayManager.remove((Overlay)this.mazeOverlay);
        this.overlayManager.remove((Overlay)this.ballOverlay);
        this.overlayManager.remove((Overlay)this.targetsOverlay);
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        if (event.getProjectile().getId() == 1604 && event.getProjectile().getEndCycle() - event.getProjectile().getStartCycle() == event.getProjectile().getRemainingCycles()) {
            JSONObject data = new JSONObject();
            data.put("sotetseg-extended-ball", event.getProjectile().getInteracting().getName());
            this.eventBus.post((Object)new SocketBroadcastPacket(data));
        }
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        NPC npc = event.getNpc();
        switch (npc.getId()) {
            case 8388: 
            case 10865: 
            case 10868: {
                this.flashFlag = true;
            }
            case 8387: 
            case 10864: 
            case 10867: {
                this.sotetsegActive = true;
                this.sotetsegNPC = npc;
                break;
            }
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        NPC npc = event.getNpc();
        switch (npc.getId()) {
            case 8387: 
            case 8388: 
            case 10864: 
            case 10865: 
            case 10867: 
            case 10868: {
                if (this.client.getPlane() == 3) break;
                this.sotetsegActive = false;
                this.sotetsegNPC = null;
                this.instanceActive = false;
                this.instanceTime = -1;
                this.started = false;
                this.flashFlag = true;
                int splitTime = this.client.getTickCount() - this.mazeEndTick;
                Object splitMessage = "";
                if (this.mazeEndTick != -1 && this.config.showBetweenSplits()) {
                    splitMessage = (String)splitMessage + "'Sotetseg Phase 3' completed! - Duration: <col=ff0000>" + String.format("%.1f", (double)splitTime * 0.6) + "s";
                    this.client.addChatMessage(ChatMessageType.FRIENDSCHATNOTIFICATION, "", (String)splitMessage, null);
                    this.soteEntryTick = -1;
                }
                if (this.client.getWidget(28, 1) == null) break;
                this.hideWidget(this.client.getWidget(28, 1), false);
                break;
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        this.handleInstanceTimer();
        if (this.sotetsegActive) {
            Player player = this.client.getLocalPlayer();
            if (this.sotetsegNPC != null && (this.sotetsegNPC.getId() == 8388 || this.sotetsegNPC.getId() == 10868 || this.sotetsegNPC.getId() == 10865)) {
                this.redTiles.clear();
                this.cRedTiles.clear();
                this.mazePings.clear();
                this.mazeTiles.clear();
                this.dispatchCount = 5;
                this.flashFlag = true;
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
                this.underworldRegionID = player.getWorldLocation().getRegionID();
                if (this.pluginManager.isPluginEnabled((Plugin)this.socketPlugin)) {
                    JSONArray data = new JSONArray();
                    JSONArray dataUnder = new JSONArray();
                    for (Point p : this.redTiles) {
                        WorldPoint wp = this.translateMazePoint(p);
                        JSONObject jsonwp = new JSONObject();
                        jsonwp.put("x", wp.getX());
                        jsonwp.put("y", wp.getY());
                        jsonwp.put("plane", wp.getPlane());
                        data.put(jsonwp);
                        WorldPoint wp2 = this.translateUnderWorldPoint(p);
                        JSONObject jsonunder = new JSONObject();
                        jsonunder.put("x", wp2.getX());
                        jsonunder.put("y", wp2.getY());
                        jsonunder.put("plane", wp2.getPlane());
                        dataUnder.put(jsonunder);
                    }
                    JSONObject payload = new JSONObject();
                    payload.put("sotetseg-extended", data);
                    this.eventBus.post((Object)new SocketBroadcastPacket(payload));
                    JSONObject payloadunder = new JSONObject();
                    payloadunder.put("sotetseg-extended", dataUnder);
                    this.eventBus.post((Object)new SocketBroadcastPacket(payloadunder));
                }
            }
            if (this.invisibleTicks > 0) {
                --this.invisibleTicks;
            }
            this.updateMovementTimer();
            this.adjustIndexRunners();
            this.adjustMazeState();
            this.checkOffMaze();
            if (this.firstTick + 1 == this.client.getTickCount()) {
                this.solvedRedTiles = this.solveMaze(this.cRedTiles);
            }
        }
    }

    private void checkOffMaze() {
        boolean anyOffMaze;
        List players;
        if (this.mazeActive && this.client.getTickCount() - this.mazeStartTick > 5 && this.checkingOffMaze && this.client.getLocalPlayer().getWorldLocation().getPlane() != 3) {
            players = this.client.getPlayers();
            anyOffMaze = true;

            Iterator playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player partyMember = (Player)playerIterator.next();
                if (partyMember.getWorldLocation().getRegionX() < 8 || partyMember.getWorldLocation().getRegionY() > 36) continue;
                anyOffMaze = false;
            }

            if (anyOffMaze && this.checkingOffMaze) {
                this.checkingOffMaze = false;
                this.allOffMaze = this.client.getTickCount();
            }
        }
        if (this.mazeActive && this.client.getTickCount() - this.mazeStartTick > 5 && this.checkingAnyOffMaze && this.client.getLocalPlayer().getWorldLocation().getPlane() != 3) {
            players = this.client.getPlayers();
            anyOffMaze = false;
            Iterator playerIterator = players.iterator();
            block1: while (true) {
                Player partyMember;
                if (!playerIterator.hasNext()) {
                    if (anyOffMaze && this.checkingAnyOffMaze) {
                        this.checkingAnyOffMaze = false;
                        this.firstOffMaze = this.client.getTickCount();
                    }
                    return;
                }
                partyMember = (Player)playerIterator.next();
                if (partyMember.getWorldLocation().getRegionX() <= 8 || partyMember.getWorldLocation().getRegionY() <= 36) continue;
                if (!this.chosen) {
                    Iterator<Player> iterator = this.playersRunningMaze.iterator();
                    while (true) {
                        if (!iterator.hasNext()) continue block1;
                        Player mazeRunner = iterator.next();
                        if (mazeRunner.getName() == null || !mazeRunner.getName().equals(partyMember.getName())) continue;
                        anyOffMaze = true;
                    }
                }
                if (partyMember.getName() == null || partyMember.getName().equals(this.client.getLocalPlayer().getName())) continue;
                anyOffMaze = true;
            }
        }
    }

    private void adjustMazeState() {
        if (!(this.sotetsegNPC.getId() != 8388 && this.sotetsegNPC.getId() != 10868 && this.sotetsegNPC.getId() != 10865 || this.started)) {
            this.started = true;
            this.mazeActive = false;
            this.soteEntryTick = this.client.getTickCount();
        } else if ((this.sotetsegNPC.getId() == 8388 || this.sotetsegNPC.getId() == 10868 || this.sotetsegNPC.getId() == 10865) && this.mazeActive) {
            this.mazeEnds();
            this.mazePoints.clear();
            this.mazeSolved.clear();
            this.mazeSolvedIndex = -1;
            this.mazeActive = false;
        } else if ((this.sotetsegNPC.getId() == 8387 || this.sotetsegNPC.getId() == 10864 || this.sotetsegNPC.getId() == 10867 || this.isInUnderWorld()) && !this.mazeActive && this.started) {
            this.mazeActive = true;
            this.mazeStarts();
        }
    }

    private void mazeEnds() {
        this.firstTick = -1;
        this.cRedTiles.clear();
        this.solvedRedTiles.clear();
        if (this.client.getWidget(28, 1) != null) {
            Widget[] widgetsOfSotetseg;
            for (Widget widget : widgetsOfSotetseg = this.client.getWidget(28, 1).getChildren()) {
                widget.setText("");
            }
        }
        this.showFirstTile = false;
        this.mazeEndTick = this.client.getTickCount();
        int mazeTicks = this.mazeSolved.size() > 0 ? this.mazeSolved.size() : -1;
        int mazeDiff = this.mazeEndTick - this.mazeStartTick;
        int lost = this.allOffMaze - this.mazeStartTick;
        int firstOff = this.firstOffMaze - this.mazeStartTick;
        this.mazeStartTick = 0;
        int perfectTick = -1;
        if (this.mazeSolved.size() != 0) {
            perfectTick = 5 + (int)Math.ceil((double)Math.abs(this.mazeSolved.get(0).getX() - 12) / 2.0) + mazeTicks;
        }
        int ticksLost = (int)((double)(mazeDiff - perfectTick) / 4.0) * 4;
        if (this.config.showMazeSplits()) {
            String color = "<col=" + ColorUtil.colorToHexCode((Color)this.config.getSplitsMessageColor()) + ">";
            String red = "<col=ff0000>";
            String splitMessage = "Maze Duration: " + red + String.format("%.1f", (double)mazeDiff * 0.6) + "s (";
            if (perfectTick == -1) {
                int cycleEnd = this.client.getTickCount();
                int offset = (cycleEnd - firstOff + this.mazeStartTick) % 4;
                int adjustEnd = firstOff + offset;
                ticksLost = cycleEnd - adjustEnd + this.mazeStartTick;
            }
            splitMessage = splitMessage + String.format("%.1f", (double)ticksLost * 0.6) + "s)" + color;
            if (this.config.showDetailedSplits()) {
                if (this.mazeSolved.size() != 0) {
                    splitMessage = splitMessage + color + ", Maze: " + red + this.mazeSolved.size();
                }
                splitMessage = splitMessage + color + ", First: " + red + firstOff;
                splitMessage = splitMessage + color + ", Last: " + red + lost;
                if (perfectTick != -1) {
                    splitMessage = splitMessage + color + ", Perfect: " + red + perfectTick;
                }
            }
            this.client.addChatMessage(ChatMessageType.FRIENDSCHATNOTIFICATION, "", splitMessage, null);
        }
        this.playersRunningMaze.clear();
        this.madePlayerList = false;
        this.chosen = false;
    }

    private void hideWidget(Widget widget, boolean hidden) {
        if (widget != null) {
            widget.setHidden(hidden);
        }
    }

    private void mazeStarts() {
        this.packetReceived = false;
        if (this.client.getWidget(28, 1) != null) {
            Widget[] widgetsOfSotetseg = this.client.getWidget(28, 1).getChildren();
            if (this.config.hideScreenFlash()) {
                this.hideWidget(this.client.getWidget(28, 1), true);
            }
            for (Widget widget : widgetsOfSotetseg) {
                if (widget.getText().isEmpty() || !widget.getText().contains("Sotetseg chooses you")) continue;
                this.chosen = true;
                if (!this.flashFlag) continue;
                this.flashFlag = false;
                this.flashScreen = true;
                this.chosenTextScreen = true;
            }
        }
        this.movementCount = 5;
        this.showFirstTile = true;
        this.mazeStartTick = this.client.getTickCount();
        this.checkingOffMaze = true;
        this.checkingAnyOffMaze = true;
        Object splitMessage = "";
        if (this.soteEntryTick != -1 && this.config.showBetweenSplits()) {
            int splitTime = this.mazeStartTick - this.soteEntryTick;
            splitMessage = (String)splitMessage + "'Sotetseg Phase 1' completed! - Duration: <col=ff0000>" + String.format("%.1f", (double)splitTime * 0.6) + "s";
            this.client.addChatMessage(ChatMessageType.FRIENDSCHATNOTIFICATION, "", (String)splitMessage, null);
            this.soteEntryTick = -1;
        } else if (this.soteEntryTick == -1 && this.config.showBetweenSplits()) {
            int splitTime = this.mazeStartTick - this.mazeEndTick;
            splitMessage = (String)splitMessage + "'Sotetseg Phase 2' completed! - Duration: <col=ff0000>" + String.format("%.1f", (double)splitTime * 0.6) + "s";
            this.client.addChatMessage(ChatMessageType.FRIENDSCHATNOTIFICATION, "", (String)splitMessage, null);
        }
        this.mazeEndTick = 0;
    }

    private void adjustIndexRunners() {
        if ((this.sotetsegNPC.getId() == 8387 || this.sotetsegNPC.getId() == 10864 || this.sotetsegNPC.getId() == 10867) && this.mazeActive) {
            if (!this.madePlayerList && this.client.getTickCount() - this.mazeStartTick > 5) {
                for (Player runner : this.client.getPlayers()) {
                    if (runner.getWorldLocation().getRegionX() <= 8 || runner.getWorldLocation().getRegionY() <= 14) continue;
                    this.playersRunningMaze.add(runner);
                }
                this.madePlayerList = true;
            }
            WorldPoint wp = this.client.getLocalPlayer().getWorldLocation();
            Point p = new Point(wp.getRegionX(), wp.getRegionY());
            for (int i = 0; i < this.mazeSolved.size(); ++i) {
                if (this.mazeSolved.get(i).getX() != p.getX() || this.mazeSolved.get(i).getY() != p.getY()) continue;
                this.mazeSolvedIndex = i;
            }
        } else if (this.mazeActive) {
            if (!this.showFirstTile) {
                this.showFirstTile = true;
            }
            if (this.client.getLocalPlayer().getWorldLocation().getRegionY() > 21 && this.showFirstTile) {
                this.showFirstTile = false;
            }
        }
    }

    private void updateMovementTimer() {
        if (this.movementCount != -1) {
            this.movementCount = this.movementCount == 0 ? -1 : --this.movementCount;
        }
    }

    private void handleInstanceTimer() {
        if (!this.instanceActive) {
            if (this.inRegion(13122)) {
                this.checkInstanceCreation();
            }
        }
        if (this.instanceActive) {
            ++this.instanceTime;
            if (this.instanceTime > 3) {
                this.instanceTime = 0;
            }
        }
    }

    private void checkInstanceCreation() {
        if (this.inRegion(13122)) {
            for (Player p : this.client.getPlayers()) {
                WorldPoint wp = p.getWorldLocation();
                if (wp.getRegionX() != 32 || wp.getRegionY() != 51 && wp.getRegionY() != 52) continue;
                this.instanceTime = 2;
                this.instanceActive = true;
                return;
            }
        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            JSONObject payload = event.getPayload();
            if (!(payload.has("sotetseg-extended") || payload.has("sotetseg-extended-ball") || payload.has("deathball-target"))) {
                return;
            }
            if (payload.has("sotetseg-extended-ball")) {
                payload.getString("sotetseg-extended-ball");
                if (this.client.getLocalPlayer() != null && this.client.getLocalPlayer().getWorldLocation().getPlane() == 3 && this.ballTick != this.client.getTickCount()) {
                    this.ballTick = this.client.getTickCount();
                    this.invisibleTargets.add(payload.getString("sotetseg-extended-ball"));
                    if (this.config.warnBall()) {
                        this.client.addChatMessage(ChatMessageType.FRIENDSCHATNOTIFICATION, "", "<col=ff0000>Ball thrown while in underworld", "");
                        this.invisibleTicks = 16;
                    }
                }
                return;
            }
            JSONArray data = payload.getJSONArray("sotetseg-extended");
            for (int i = 0; i < data.length(); ++i) {
                JSONObject jsonwp = data.getJSONObject(i);
                int x = jsonwp.getInt("x");
                int y = jsonwp.getInt("y");
                int plane = jsonwp.getInt("plane");
                WorldPoint wp = new WorldPoint(x, y, plane);
                this.mazePings.add(wp);
                Point p = new Point(wp.getRegionX(), wp.getRegionY());
                this.mazeTiles.add(p);
            }
            if (this.packetReceived) {
                return;
            }
            if (this.config.solveMaze()) {
                this.mazePoints.clear();
                this.mazeTiles.sort(Comparator.comparing(Point::getY).thenComparing(Point::getY));
                this.arrangeMazeTiles();
                this.addStartingTiles(this.mazePoints);
                this.mazeSolved.clear();
                this.mazeSolved = this.solveMaze(this.mazePoints);
                if (this.mazeSolved.get(this.mazeSolved.size() - 1).getY() > 35) {
                    this.packetReceived = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        GroundObject o;
        if (this.sotetsegActive && ((o = event.getGroundObject()).getId() == 33035 || o.getId() > 41749 && o.getId() < 41754)) {
            Tile t = event.getTile();
            WorldPoint p = WorldPoint.fromLocal((Client)this.client, (LocalPoint)t.getLocalLocation());
            Point point = new Point(p.getRegionX(), p.getRegionY());
            if (this.firstTick != -1) {
                this.firstTick = this.client.getTickCount();
            }
            if (this.isInOverWorld()) {
                this.redTiles.add(new Point(point.getX() - 9, point.getY() - 22));
                this.cRedTiles.add(new Point(point.getX() - 9, point.getY() - 22));
            }
            if (this.isInUnderWorld()) {
                this.redTiles.add(new Point(point.getX() - 42, point.getY() - 31));
                this.cRedTiles.add(new Point(point.getX() - 42, point.getY() - 31));
                this.wasInUnderworld = true;
            }
        }
    }

    public boolean isInOverWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13123;
    }

    public boolean isInUnderWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13379;
    }

    private WorldPoint translateMazePoint(Point mazePoint) {
        Player p = this.client.getLocalPlayer();
        if (this.overworldRegionID == -1 && p != null) {
            WorldPoint wp = p.getWorldLocation();
            return WorldPoint.fromRegion((int)wp.getRegionID(), (int)(mazePoint.getX() + 9), (int)(mazePoint.getY() + 22), (int)0);
        }
        return WorldPoint.fromRegion((int)this.overworldRegionID, (int)(mazePoint.getX() + 9), (int)(mazePoint.getY() + 22), (int)0);
    }

    private WorldPoint translateUnderWorldPoint(Point mazePoint) {
        return WorldPoint.fromRegion((int)this.underworldRegionID, (int)(mazePoint.getX() + 42), (int)(mazePoint.getY() + 31), (int)3);
    }

    private void addStartingTiles(ArrayList<Point> mazePoints) {
        if (mazePoints.get(0).getX() > 12) {
            for (int i = mazePoints.get(0).getX() - 12; i >= 0; --i) {
                mazePoints.add(0, new Point(12 + i, 21));
            }
        } else if (mazePoints.get(0).getX() < 12) {
            for (int i = 12 - mazePoints.get(0).getX(); i >= 0; --i) {
                mazePoints.add(0, new Point(12 - i, 21));
            }
        } else {
            mazePoints.add(0, new Point(12, 21));
        }
    }

    private void arrangeMazeTiles() {
        IntStream.range(this.mazeTiles.get(0).getY(), this.mazeTiles.get(this.mazeTiles.size() - 1).getY() + 1).forEach(i -> {
            ArrayList<Point> wpl = new ArrayList<Point>(this.mazeTiles);
            wpl.removeIf(x -> i != x.getY());
            if (this.mazePoints.size() != 0 && ((Point)wpl.get(0)).getX() != this.mazePoints.get(this.mazePoints.size() - 1).getX()) {
                Collections.reverse(wpl);
            }
            this.mazePoints.addAll(wpl);
        });
    }

    private ArrayList<Point> solveMaze(ArrayList<Point> tiles) {
        ArrayList<Point> solvedTiles = new ArrayList<Point>();
        if (tiles.size() != 0) {
            solvedTiles.add(tiles.get(0));
            int index = 0;
            while (solvedTiles.get(solvedTiles.size() - 1).getY() != tiles.get(tiles.size() - 1).getY() || solvedTiles.get(solvedTiles.size() - 1).getY() == tiles.get(tiles.size() - 1).getY() && solvedTiles.get(solvedTiles.size() - 1).getY() == 21 && solvedTiles.get(solvedTiles.size() - 1).getX() != tiles.get(tiles.size() - 1).getX()) {
                int i;
                int cPosX = tiles.get(index).getX();
                int cPosY = tiles.get(index).getY();
                int validMove = -1;
                int n = i = tiles.size() - index > 4 ? 4 : tiles.size() - index - 1;
                while (i > 0) {
                    if (validMove == -1) {
                        int xDiff = tiles.get(index + i).getX() - tiles.get(index).getX();
                        int yDiff = tiles.get(index + i).getY() - tiles.get(index).getY();
                        if (i < 3) {
                            validMove = i;
                        } else if (i == 3) {
                            if (yDiff > 1 && cPosY + 1 == tiles.get(index + 1).getY() && Math.abs(xDiff) == 1) {
                                validMove = i;
                            } else if (yDiff == 1 && Math.abs(tiles.get(index + 1).getX() - cPosX) == 1) {
                                validMove = i;
                            }
                        } else if (i == 4 && yDiff == 2 && Math.abs(xDiff) == 2 && Math.abs(tiles.get(index + 2).getX() - tiles.get(index).getX()) == 1 && tiles.get(index + 2).getY() - tiles.get(index).getY() == 1) {
                            validMove = i;
                        }
                    }
                    --i;
                }
                solvedTiles.add(tiles.get(index += validMove));
            }
            if (solvedTiles.size() > 1) {
                Point last = solvedTiles.get(solvedTiles.size() - 1);
                Point secondLast = solvedTiles.get(solvedTiles.size() - 2);
                if (last.getY() - 1 == secondLast.getY()) {
                    if ((last.getX() - 1 == secondLast.getX() || last.getX() + 1 == secondLast.getX()) && last.getX() != 9 && last.getX() != 22) {
                        Point p = new Point(last.getX() + (last.getX() - secondLast.getX()), last.getY() + 1);
                        solvedTiles.set(solvedTiles.size() - 1, p);
                    } else if (last.getX() == secondLast.getX()) {
                        Point p = new Point(last.getX(), last.getY() + 1);
                        solvedTiles.set(solvedTiles.size() - 1, p);
                    }
                }
            }
        }
        return solvedTiles;
    }

    public boolean inRegion(int ... regions) {
        if (this.client.getMapRegions() != null) {
            for (int i : this.client.getMapRegions()) {
                if (!Arrays.stream(regions, 0, regions.length).anyMatch(j -> i == j)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean isSotetsegActive() {
        return this.sotetsegActive;
    }

    public NPC getSotetsegNPC() {
        return this.sotetsegNPC;
    }

    public ArrayList<Point> getSolvedRedTiles() {
        return this.solvedRedTiles;
    }

    public Set<WorldPoint> getMazePings() {
        return this.mazePings;
    }

    public ArrayList<Point> getMazeSolved() {
        return this.mazeSolved;
    }

    public boolean isMazeActive() {
        return this.mazeActive;
    }

    public int getInstanceTime() {
        return this.instanceTime;
    }

    public int getMazeSolvedIndex() {
        return this.mazeSolvedIndex;
    }

    public int getMovementCount() {
        return this.movementCount;
    }

    public boolean isChosen() {
        return this.chosen;
    }
}

