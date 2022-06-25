/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Stopwatch
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ObjectArrays
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.ChatPlayer
 *  net.runelite.api.Client
 *  net.runelite.api.FriendContainer
 *  net.runelite.api.FriendsChatManager
 *  net.runelite.api.FriendsChatMember
 *  net.runelite.api.GameState
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.Player
 *  net.runelite.api.World
 *  net.runelite.api.clan.ClanChannel
 *  net.runelite.api.clan.ClanChannelMember
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.ClientTick
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.HitsplatApplied
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.PlayerDespawned
 *  net.runelite.api.events.PlayerSpawned
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.events.WorldListLoad
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.client.callback.ClientThread
 *  net.runelite.client.chat.ChatColorType
 *  net.runelite.client.chat.ChatMessageBuilder
 *  net.runelite.client.chat.ChatMessageManager
 *  net.runelite.client.chat.QueuedMessage
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.game.WorldService
 *  net.runelite.client.input.KeyListener
 *  net.runelite.client.input.KeyManager
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.ui.ClientToolbar
 *  net.runelite.client.ui.NavigationButton
 *  net.runelite.client.ui.PluginPanel
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  net.runelite.client.util.ExecutorServiceExceptionLogger
 *  net.runelite.client.util.HotkeyListener
 *  net.runelite.client.util.ImageUtil
 *  net.runelite.client.util.Text
 *  net.runelite.client.util.WorldUtil
 *  net.runelite.http.api.worlds.World
 *  net.runelite.http.api.worlds.WorldResult
 *  net.runelite.http.api.worlds.WorldType
 *  org.apache.commons.lang3.ArrayUtils
 *  org.pf4j.Extension
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketworldhopper;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;
import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.ChatMessageType;
import net.runelite.api.ChatPlayer;
import net.runelite.api.Client;
import net.runelite.api.FriendContainer;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.World;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WorldListLoad;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.WorldService;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.maz.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.maz.plugins.socket.plugins.socketworldhopper.ping.Ping;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ExecutorServiceExceptionLogger;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.WorldResult;
import net.runelite.http.api.worlds.WorldType;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Socket - World Hopper", description="Allows you to quickly hop worlds", conflicts={"World Hopper"})
@PluginDependency(value=SocketPlugin.class)
public class SocketWorldHopperPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SocketWorldHopperPlugin.class);
    private static final int WORLD_FETCH_TIMER = 10;
    private static final int REFRESH_THROTTLE = 60000;
    private static final int MAX_PLAYER_COUNT = 1950;
    private static final int TICK_THROTTLE = (int)Duration.ofMinutes(10L).toMillis();
    private static final int DISPLAY_SWITCHER_MAX_ATTEMPTS = 3;
    private static final String HOP_TO = "Hop-to";
    private static final String KICK_OPTION = "Kick";
    private static final ImmutableList<String> BEFORE_OPTIONS = ImmutableList.of("Add friend", "Remove friend", "Kick");
    private static final ImmutableList<String> AFTER_OPTIONS = ImmutableList.of("Message");
    public static boolean allowHopping = true;
    @Inject
    private EventBus eventBus;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ConfigManager configManager;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private KeyManager keyManager;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private WorldService worldService;
    @Inject
    private ScheduledExecutorService executorService;
    @Inject
    private SocketWorldHopperConfig config;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SocketWorldHopperPingOverlay worldHopperOverlay;
    private ScheduledExecutorService hopperExecutorService;
    private ScheduledExecutorService hopBlocked;
    private NavigationButton navButton;
    private SocketWorldSwitcherPanel panel;
    private World quickHopTargetWorld;
    private int displaySwitcherAttempts = 0;
    private int lastWorld;
    private int favoriteWorld1;
    private int favoriteWorld2;
    private ScheduledFuture<?> worldResultFuture;
    private ScheduledFuture<?> pingFuture;
    private ScheduledFuture<?> currPingFuture;
    private WorldResult worldResult;
    private int currentWorld;
    private Instant lastFetch;
    private boolean firstRun;
    private String customWorlds;
    private int logOutNotifTick = -1;
    private long hopDelay = 0L;
    private long hopDelayMS = 0L;
    private boolean allowedToHop = true;
    private int currentPing;
    private final Map<Integer, Integer> storedPings = new HashMap<Integer, Integer>();
    private final HotkeyListener previousKeyListener = new HotkeyListener(() -> this.config.previousKey()){

        public void hotkeyPressed() {
            SocketWorldHopperPlugin.this.clientThread.invoke(() -> SocketWorldHopperPlugin.this.hop(true));
        }
    };
    private final HotkeyListener nextKeyListener = new HotkeyListener(() -> this.config.nextKey()){

        public void hotkeyPressed() {
            SocketWorldHopperPlugin.this.clientThread.invoke(() -> SocketWorldHopperPlugin.this.hop(false));
        }
    };

    @Provides
    SocketWorldHopperConfig getConfig(ConfigManager configManager) {
        return (SocketWorldHopperConfig)configManager.getConfig(SocketWorldHopperConfig.class);
    }

    protected void startUp() throws Exception {
        this.allowedToHop = true;
        this.hopDelay = 0L;
        this.firstRun = true;
        this.currentPing = -1;
        this.customWorlds = this.config.customWorldCycle();
        this.keyManager.registerKeyListener((KeyListener)this.previousKeyListener);
        this.keyManager.registerKeyListener((KeyListener)this.nextKeyListener);
        this.panel = new SocketWorldSwitcherPanel(this);
        BufferedImage icon = ImageUtil.loadImageResource(SocketWorldHopperPlugin.class, (String)"icon.png");
        this.navButton = NavigationButton.builder().tooltip("World Switcher").icon(icon).priority(3).panel((PluginPanel)this.panel).build();
        if (this.config.showSidebar()) {
            this.clientToolbar.addNavigation(this.navButton);
        }
        this.overlayManager.add((Overlay)this.worldHopperOverlay);
        this.panel.setSubscriptionFilterMode(this.config.subscriptionFilter());
        this.panel.setRegionFilterMode(this.config.regionFilter());
        this.hopperExecutorService = new ExecutorServiceExceptionLogger(Executors.newSingleThreadScheduledExecutor());
        this.hopBlocked = new ExecutorServiceExceptionLogger(Executors.newSingleThreadScheduledExecutor());
        this.worldResultFuture = this.executorService.scheduleAtFixedRate(this::tick, 0L, 10L, TimeUnit.MINUTES);
        this.pingFuture = this.hopperExecutorService.scheduleWithFixedDelay(this::pingNextWorld, 15L, 3L, TimeUnit.SECONDS);
        this.currPingFuture = this.hopperExecutorService.scheduleWithFixedDelay(this::pingCurrentWorld, 15L, 1L, TimeUnit.SECONDS);
        this.updateList();
    }

    protected void shutDown() throws Exception {
        this.allowedToHop = true;
        this.hopDelay = 0L;
        this.pingFuture.cancel(true);
        this.pingFuture = null;
        this.currPingFuture.cancel(true);
        this.currPingFuture = null;
        this.overlayManager.remove((Overlay)this.worldHopperOverlay);
        this.keyManager.unregisterKeyListener((KeyListener)this.previousKeyListener);
        this.keyManager.unregisterKeyListener((KeyListener)this.nextKeyListener);
        this.worldResultFuture.cancel(true);
        this.worldResultFuture = null;
        this.worldResult = null;
        this.lastFetch = null;
        this.clientToolbar.removeNavigation(this.navButton);
        this.hopperExecutorService.shutdown();
        this.hopperExecutorService = null;
    }

    @Subscribe
    public void onClientTick(ClientTick event) {
        long x = System.currentTimeMillis() - this.hopDelay;
        this.allowedToHop = x > 11000L;
        this.hopDelayMS = 11000L - x;
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied event) {
        String name = event.getActor().getName();
        if (name != null && event.getActor().getAnimation() != 829 && name.equals(Objects.requireNonNull(this.client.getLocalPlayer()).getName())) {
            this.hopDelay = System.currentTimeMillis();
            this.hopDelayMS = 11000L;
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("socketworldhopper")) {
            switch (event.getKey()) {
                case "showSidebar": {
                    if (this.config.showSidebar()) {
                        this.clientToolbar.addNavigation(this.navButton);
                        break;
                    }
                    this.clientToolbar.removeNavigation(this.navButton);
                    break;
                }
                case "ping": {
                    if (this.config.ping()) {
                        SwingUtilities.invokeLater(() -> this.panel.showPing());
                        break;
                    }
                    SwingUtilities.invokeLater(() -> this.panel.hidePing());
                    break;
                }
                case "subscriptionFilter": {
                    this.panel.setSubscriptionFilterMode(this.config.subscriptionFilter());
                    this.updateList();
                    break;
                }
                case "regionFilter": {
                    this.panel.setRegionFilterMode(this.config.regionFilter());
                    this.updateList();
                    break;
                }
                case "customWorldCycle": {
                    this.customWorlds = this.config.customWorldCycle();
                    String s = this.config.customWorldCycle();
                    JSONArray data = new JSONArray();
                    JSONObject jsonwp = new JSONObject();
                    jsonwp.put("worlds", s);
                    data.put(jsonwp);
                    JSONObject payload = new JSONObject();
                    payload.put("worldhopper-extended", data);
                    this.eventBus.post((Object)new SocketBroadcastPacket(payload));
                    break;
                }
                case "hopperName": 
                case "hopperName2": {
                    for (Player p : this.client.getPlayers()) {
                        String name = p.getName();
                        if (name == null || this.client.getLocalPlayer() == null || name.equalsIgnoreCase(this.client.getLocalPlayer().getName()) || !name.equalsIgnoreCase(this.config.getHopperName()) && !name.equalsIgnoreCase(this.config.getHopperName2())) continue;
                        this.SetHopAbility(false);
                        return;
                    }
                    this.SetHopAbility(true);
                }
            }
        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            JSONObject payload = event.getPayload();
            if (!payload.has("worldhopper-extended")) {
                return;
            }
            JSONArray data = payload.getJSONArray("worldhopper-extended");
            JSONObject jsonwp = data.getJSONObject(0);
            String worlds = jsonwp.getString("worlds");
            this.clientThread.invoke(() -> this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=b4281e>Custom world list: " + worlds + ".", null));
            this.customWorlds = worlds;
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    private void setFavoriteConfig(int world) {
        this.configManager.setConfiguration("socketworldhopper", "favorite_" + world, (Object)true);
    }

    private boolean isFavoriteConfig(int world) {
        Boolean favorite = (Boolean)this.configManager.getConfiguration("socketworldhopper", "favorite_" + world, Boolean.class);
        return favorite != null && favorite != false;
    }

    private void clearFavoriteConfig(int world) {
        this.configManager.unsetConfiguration("socketworldhopper", "favorite_" + world);
    }

    boolean isFavorite(net.runelite.http.api.worlds.World world) {
        int id = world.getId();
        return id == this.favoriteWorld1 || id == this.favoriteWorld2 || this.isFavoriteConfig(id);
    }

    int getCurrentWorld() {
        return this.client.getWorld();
    }

    void hopTo(net.runelite.http.api.worlds.World world) {
        this.clientThread.invoke(() -> this.hop(world.getId()));
    }

    void addToFavorites(net.runelite.http.api.worlds.World world) {
        log.debug("Adding world {} to favorites", (Object)world.getId());
        this.setFavoriteConfig(world.getId());
        this.panel.updateFavoriteMenu(world.getId(), true);
    }

    void removeFromFavorites(net.runelite.http.api.worlds.World world) {
        log.debug("Removing world {} from favorites", (Object)world.getId());
        this.clearFavoriteConfig(world.getId());
        this.panel.updateFavoriteMenu(world.getId(), false);
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged varbitChanged) {
        block3: {
            block2: {
                int old1 = this.favoriteWorld1;
                int old2 = this.favoriteWorld2;
                this.favoriteWorld1 = this.client.getVarbitValue(4597);
                this.favoriteWorld2 = this.client.getVarbitValue(4598);
                if (old1 != this.favoriteWorld1) break block2;
                if (old2 == this.favoriteWorld2) break block3;
            }
            SwingUtilities.invokeLater(this.panel::updateList);
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        if (!this.config.menuOption()) {
            return;
        }
        int componentId = event.getActionParam1();
        int groupId = WidgetInfo.TO_GROUP((int)componentId);
        String option = event.getOption();
        if (groupId == WidgetInfo.FRIENDS_LIST.getGroupId() || groupId == WidgetInfo.FRIENDS_CHAT.getGroupId() || componentId == WidgetInfo.CLAN_MEMBER_LIST.getId() || componentId == WidgetInfo.CLAN_GUEST_MEMBER_LIST.getId()) {
            boolean after;
            if (AFTER_OPTIONS.contains((Object)option)) {
                after = true;
            } else if (BEFORE_OPTIONS.contains((Object)option)) {
                after = false;
            } else {
                return;
            }
            ChatPlayer player = this.getChatPlayerFromName(event.getTarget());
            WorldResult worldResult = this.worldService.getWorlds();
            if (player == null || player.getWorld() == 0 || player.getWorld() == this.client.getWorld() || worldResult == null) {
                return;
            }
            net.runelite.http.api.worlds.World currentWorld = worldResult.findWorld(this.client.getWorld());
            net.runelite.http.api.worlds.World targetWorld = worldResult.findWorld(player.getWorld());
            if (targetWorld == null || currentWorld == null || !currentWorld.getTypes().contains((Object)WorldType.PVP) && targetWorld.getTypes().contains((Object)WorldType.PVP)) {
                return;
            }
            this.client.createMenuEntry(after ? -2 : -1).setOption(HOP_TO).setTarget(event.getTarget()).setType(MenuAction.RUNELITE).onClick(e -> {
                ChatPlayer p = this.getChatPlayerFromName(e.getTarget());
                if (p != null) {
                    this.hop(p.getWorld());
                }
            });
        }
    }

    private void insertMenuEntry(MenuEntry newEntry, MenuEntry[] entries, boolean after) {
        Object[] newMenu = (MenuEntry[])ObjectArrays.concat((Object[])entries, (Object)newEntry);
        if (after) {
            int menuEntryCount = newMenu.length;
            ArrayUtils.swap((Object[])newMenu, (int)(menuEntryCount - 1), (int)(menuEntryCount - 2));
        }
        this.client.setMenuEntries((MenuEntry[])newMenu);
    }

    @Subscribe
    public void onPlayerDespawned(PlayerDespawned event) {
        String name = event.getPlayer().getName();
        if (name != null && this.client.getLocalPlayer() != null && !name.equalsIgnoreCase(this.client.getLocalPlayer().getName()) && (name.equalsIgnoreCase(this.config.getHopperName()) || name.equalsIgnoreCase(this.config.getHopperName2()))) {
            this.SetHopAbility(true);
        }
    }

    @Subscribe
    public void onPlayerSpawned(PlayerSpawned event) {
        String name = event.getPlayer().getName();
        if (name != null && this.client.getLocalPlayer() != null && !name.equalsIgnoreCase(this.client.getLocalPlayer().getName()) && (name.equalsIgnoreCase(this.config.getHopperName()) || name.equalsIgnoreCase(this.config.getHopperName2()))) {
            this.SetHopAbility(false);
        }
    }

    void SetHopAbility(boolean enabled) {
        this.logOutNotifTick = enabled ? this.client.getTickCount() : -1;
        System.out.println("Allow hopping: " + allowHopping);
        allowHopping = enabled;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (this.config.showSidebar() && gameStateChanged.getGameState() == GameState.LOGGED_IN && this.lastWorld != this.client.getWorld()) {
            int newWorld = this.client.getWorld();
            this.panel.switchCurrentHighlight(newWorld, this.lastWorld);
            this.lastWorld = newWorld;
        }
    }

    @Subscribe
    public void onWorldListLoad(WorldListLoad worldListLoad) {
        if (!this.config.showSidebar()) {
            return;
        }
        HashMap<Integer, Integer> worldData = new HashMap<Integer, Integer>();
        for (World w : worldListLoad.getWorlds()) {
            worldData.put(w.getId(), w.getPlayerCount());
        }
        this.panel.updateListData(worldData);
        this.lastFetch = Instant.now();
    }

    private void tick() {
        Instant now = Instant.now();
        if (this.lastFetch != null && now.toEpochMilli() - this.lastFetch.toEpochMilli() < (long)TICK_THROTTLE) {
            log.debug("Throttling world refresh tick");
        } else {
            this.fetchWorlds();
            if (this.firstRun) {
                this.firstRun = false;
                this.hopperExecutorService.execute(this::pingInitialWorlds);
            }
        }
    }

    void refresh() {
        Instant now = Instant.now();
        if (this.lastFetch != null && now.toEpochMilli() - this.lastFetch.toEpochMilli() < 60000L) {
            log.debug("Throttling world refresh");
            return;
        }
        this.lastFetch = now;
        this.worldService.refresh();
    }

    private void fetchWorlds() {
        log.debug("Fetching worlds");
        WorldResult worldResult = this.worldService.getWorlds();
        if (worldResult != null) {
            worldResult.getWorlds().sort(Comparator.comparingInt(net.runelite.http.api.worlds.World::getId));
            this.worldResult = worldResult;
            this.lastFetch = Instant.now();
            this.updateList();
        }
    }

    private void updateList() {
        WorldResult worldResult = this.worldService.getWorlds();
        if (worldResult != null) {
            SwingUtilities.invokeLater(() -> this.panel.populate(worldResult.getWorlds()));
        }
    }

    private void hop(boolean previous) {
        boolean customCyclePresent;
        WorldResult worldResult = this.worldService.getWorlds();
        if (worldResult == null || this.client.getGameState() != GameState.LOGGED_IN || !allowHopping) {
            return;
        }
        net.runelite.http.api.worlds.World currentWorld = worldResult.findWorld(this.client.getWorld());
        if (currentWorld == null) {
            return;
        }
        Object currentWorldTypes = currentWorld.getTypes().clone();
        if (this.config.quickhopOutOfDanger()) {
            ((AbstractCollection)currentWorldTypes).remove((Object)WorldType.PVP);
            ((AbstractCollection)currentWorldTypes).remove((Object)WorldType.HIGH_RISK);
        }
        ((AbstractCollection)currentWorldTypes).remove((Object)WorldType.BOUNTY);
        ((AbstractCollection)currentWorldTypes).remove((Object)WorldType.SKILL_TOTAL);
        ((AbstractCollection)currentWorldTypes).remove((Object)WorldType.LAST_MAN_STANDING);
        List worlds = worldResult.getWorlds();
        int worldIdx = worlds.indexOf((Object)currentWorld);
        int totalLevel = this.client.getTotalLevel();
        Set<RegionFilterMode> regionFilter = this.config.quickHopRegionFilter();
        boolean bl = customCyclePresent = this.customWorlds.length() > 0;
        if (customCyclePresent) {
            int temp;
            String[] customWorldCycleStr = this.customWorlds.split(",");
            ArrayList<Integer> customWorldCycleInt = new ArrayList<Integer>();
            for (String world : customWorldCycleStr) {
                try {
                    int parsedWorld = Integer.parseInt(world);
                    customWorldCycleInt.add(parsedWorld);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            int currentIdx = -1;
            for (int i = 0; i < customWorldCycleInt.size(); ++i) {
                if (((Integer)customWorldCycleInt.get(i)).intValue() != currentWorld.getId()) continue;
                currentIdx = i;
                break;
            }
            if (currentIdx != -1) {
                if (previous) {
                    if (--currentIdx <= -1) {
                        currentIdx = customWorldCycleInt.size() - 1;
                    }
                } else if (++currentIdx >= customWorldCycleInt.size()) {
                    currentIdx = 0;
                }
            }
            int n = temp = currentIdx == -1 ? 0 : currentIdx;
            if (this.config.combatHop()) {
                if (this.allowedToHop) {
                    this.hop((Integer)customWorldCycleInt.get(currentIdx == -1 ? 0 : currentIdx));
                } else {
                    this.hopBlocked.submit(() -> {
                        try {
                            if (this.hopDelayMS > 0L) {
                                Thread.sleep(this.hopDelayMS);
                            }
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.hop((Integer)customWorldCycleInt.get(temp));
                    });
                }
            } else {
                this.hop((Integer)customWorldCycleInt.get(temp));
            }
        } else {
            net.runelite.http.api.worlds.World world;
            do {
                if (previous) {
                    if (--worldIdx < 0) {
                        worldIdx = worlds.size() - 1;
                    }
                } else if (++worldIdx >= worlds.size()) {
                    worldIdx = 0;
                }
                world = (net.runelite.http.api.worlds.World)worlds.get(worldIdx);
                if (!regionFilter.isEmpty() && !regionFilter.contains((Object)RegionFilterMode.of(world.getRegion()))) continue;
                Object types = world.getTypes().clone();
                ((AbstractCollection)types).remove((Object)WorldType.BOUNTY);
                ((AbstractCollection)types).remove((Object)WorldType.LAST_MAN_STANDING);
                if (((AbstractCollection)types).contains((Object)WorldType.SKILL_TOTAL)) {
                    try {
                        int totalRequirement = Integer.parseInt(world.getActivity().substring(0, world.getActivity().indexOf(" ")));
                        if (totalLevel >= totalRequirement) {
                            ((AbstractCollection)types).remove((Object)WorldType.SKILL_TOTAL);
                        }
                    }
                    catch (NumberFormatException ex) {
                        log.warn("Failed to parse total level requirement for target world", (Throwable)ex);
                    }
                }
                if (world.getPlayers() < 1950 && ((AbstractSet)currentWorldTypes).equals(types)) break;
            } while (world != currentWorld);
            if (world == currentWorld) {
                String chatMessage = new ChatMessageBuilder().append(ChatColorType.NORMAL).append("Couldn't find a world to quick-hop to.").build();
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
            } else if (this.config.combatHop()) {
                if (this.allowedToHop) {
                    this.hop(world.getId());
                } else {
                    net.runelite.http.api.worlds.World finalWorld = world;
                    this.hopBlocked.submit(() -> {
                        try {
                            if (this.hopDelayMS > 0L) {
                                Thread.sleep(this.hopDelayMS);
                            }
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.hop(finalWorld.getId());
                    });
                }
            } else {
                this.hop(world.getId());
            }
        }
    }

    private void hop(int worldId) {
        assert (this.client.isClientThread());
        WorldResult worldResult = this.worldService.getWorlds();
        net.runelite.http.api.worlds.World world = worldResult.findWorld(worldId);
        if (world == null) {
            return;
        }
        World rsWorld = this.client.createWorld();
        rsWorld.setActivity(world.getActivity());
        rsWorld.setAddress(world.getAddress());
        rsWorld.setId(world.getId());
        rsWorld.setPlayerCount(world.getPlayers());
        rsWorld.setLocation(world.getLocation());
        rsWorld.setTypes(WorldUtil.toWorldTypes((EnumSet)world.getTypes()));
        if (this.client.getGameState() == GameState.LOGIN_SCREEN) {
            this.client.changeWorld(rsWorld);
            return;
        }
        if (this.config.showWorldHopMessage()) {
            String chatMessage = new ChatMessageBuilder().append(ChatColorType.NORMAL).append("Quick-hopping to World ").append(ChatColorType.HIGHLIGHT).append(Integer.toString(world.getId())).append(ChatColorType.NORMAL).append("..").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
        }
        this.quickHopTargetWorld = rsWorld;
        this.displaySwitcherAttempts = 0;
        this.hopDelay = 0L;
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        this.currentWorld = this.client.getWorld();
        if (this.client.getTickCount() == this.logOutNotifTick) {
            this.logOutNotifTick = -1;
            if (this.config.playSound()) {
                this.client.playSoundEffect(80);
            }
        }
        if (this.quickHopTargetWorld == null) {
            return;
        }
        if (this.client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST) == null) {
            this.client.openWorldHopper();
            if (++this.displaySwitcherAttempts >= 3) {
                String chatMessage = new ChatMessageBuilder().append(ChatColorType.NORMAL).append("Failed to quick-hop after ").append(ChatColorType.HIGHLIGHT).append(Integer.toString(this.displaySwitcherAttempts)).append(ChatColorType.NORMAL).append(" attempts.").build();
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
                this.resetQuickHopper();
            }
        } else {
            this.client.hopToWorld(this.quickHopTargetWorld);
            this.resetQuickHopper();
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }
        if (event.getMessage().equals("Please finish what you're doing before using the World Switcher.")) {
            this.resetQuickHopper();
        }
    }

    private void resetQuickHopper() {
        this.displaySwitcherAttempts = 0;
        this.quickHopTargetWorld = null;
    }

    private ChatPlayer getChatPlayerFromName(String name) {
        ClanChannelMember member;
        FriendsChatMember member2;
        String cleanName = Text.removeTags((String)name);
        FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
        if (friendsChatManager != null && (member2 = (FriendsChatMember)friendsChatManager.findByName(cleanName)) != null) {
            return member2;
        }
        ClanChannel clanChannel = this.client.getClanChannel();
        if (clanChannel != null && (member = clanChannel.findMember(cleanName)) != null) {
            return member;
        }
        clanChannel = this.client.getGuestClanChannel();
        if (clanChannel != null && (member = clanChannel.findMember(cleanName)) != null) {
            return member;
        }
        FriendContainer friendContainer = this.client.getFriendContainer();
        if (friendContainer != null) {
            return (ChatPlayer)friendContainer.findByName(cleanName);
        }
        return null;
    }

    private void pingInitialWorlds() {
        WorldResult worldResult = this.worldService.getWorlds();
        if (worldResult == null || !this.config.showSidebar() || !this.config.ping()) {
            return;
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (net.runelite.http.api.worlds.World world : worldResult.getWorlds()) {
            int ping = this.ping(world);
            SwingUtilities.invokeLater(() -> this.panel.updatePing(world.getId(), ping));
        }
        stopwatch.stop();
        log.debug("Done pinging worlds in {}", (Object)stopwatch.elapsed());
    }

    private void pingNextWorld() {
        boolean displayPing;
        WorldResult worldResult = this.worldService.getWorlds();
        if (worldResult == null || !this.config.showSidebar() || !this.config.ping()) {
            return;
        }
        List worlds = worldResult.getWorlds();
        if (worlds.isEmpty()) {
            return;
        }
        if (this.currentWorld >= worlds.size()) {
            this.currentWorld = 0;
        }
        net.runelite.http.api.worlds.World world = (net.runelite.http.api.worlds.World)worlds.get(this.currentWorld++);
        boolean bl = displayPing = this.config.displayPing() && this.client.getGameState() == GameState.LOGGED_IN;
        if (displayPing && this.client.getWorld() == world.getId()) {
            return;
        }
        int ping = this.ping(world);
        log.trace("Ping for world {} is: {}", (Object)world.getId(), (Object)ping);
        SwingUtilities.invokeLater(() -> this.panel.updatePing(world.getId(), ping));
    }

    private void pingCurrentWorld() {
        WorldResult worldResult = this.worldService.getWorlds();
        if (worldResult == null || !this.config.displayPing() || this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        net.runelite.http.api.worlds.World currentWorld = worldResult.findWorld(this.client.getWorld());
        if (currentWorld == null) {
            log.debug("unable to find current world: {}", (Object)this.client.getWorld());
            return;
        }
        this.currentPing = this.ping(currentWorld);
        log.trace("Ping for current world is: {}", (Object)this.currentPing);
        SwingUtilities.invokeLater(() -> this.panel.updatePing(currentWorld.getId(), this.currentPing));
    }

    Integer getStoredPing(net.runelite.http.api.worlds.World world) {
        if (!this.config.ping()) {
            return null;
        }
        return this.storedPings.get(world.getId());
    }

    private int ping(net.runelite.http.api.worlds.World world) {
        int ping = Ping.ping(world);
        this.storedPings.put(world.getId(), ping);
        return ping;
    }

    public int getLastWorld() {
        return this.lastWorld;
    }

    int getCurrentPing() {
        return this.currentPing;
    }
}

