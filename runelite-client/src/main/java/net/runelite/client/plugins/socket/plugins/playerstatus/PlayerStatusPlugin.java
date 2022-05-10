//
// Decompiled by Procyon v0.5.36
//

package net.runelite.client.plugins.socket.plugins.playerstatus;

import org.slf4j.LoggerFactory;
import net.runelite.client.plugins.socket.packet.SocketPlayerLeave;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.IndicatorMarker;
import java.util.Comparator;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.api.VarPlayer;
import net.runelite.api.Skill;
import net.runelite.api.GameState;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GameStateChanged;
import java.util.Iterator;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.TimerMarker;
import java.util.Collection;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.Actor;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.Player;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameIndicator;
import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameTimer;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.Overlay;
import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.ArrayList;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.AbstractMarker;
import java.util.List;
import java.util.Map;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.eventbus.EventBus;
import javax.inject.Inject;
import net.runelite.api.Client;
import org.slf4j.Logger;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.Plugin;

@PluginDescriptor(name = "Socket - Player Status", description = "Socket extension for displaying player status to members in your party.", tags = { "socket", "server", "discord", "connection", "broadcast", "player", "status", "venge", "vengeance" }, enabledByDefault = true)
@PluginDependency(SocketPlugin.class)
public class PlayerStatusPlugin extends Plugin
{
    private static final Logger log;
    @Inject
    private Client client;
    @Inject
    private EventBus eventBus;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private SpriteManager spriteManager;
    @Inject
    private PlayerStatusOverlay overlay;
    @Inject
    private PlayerSidebarOverlay sidebar;
    @Inject
    private PlayerStatusConfig config;
    private Map<String, List<AbstractMarker>> statusEffects;
    private Map<String, PlayerStatus> partyStatus;
    private int lastRaidVarb;
    private int lastVengCooldownVarb;
    private int lastIsVengeancedVarb;
    private int lastRefresh;
    public ArrayList<String> playerNames;
    private List<String> whiteList;
    public ArrayList<String> noSocketVenged;
    private boolean mirrorMode;

    public PlayerStatusPlugin() {
        this.statusEffects = new HashMap<String, List<AbstractMarker>>();
        this.partyStatus = new TreeMap<String, PlayerStatus>();
        this.playerNames = new ArrayList<String>();
        this.whiteList = new ArrayList<String>();
        this.noSocketVenged = new ArrayList<String>();
    }

    @Provides
    PlayerStatusConfig getConfig(final ConfigManager configManager) {
        return (PlayerStatusConfig)configManager.getConfig((Class)PlayerStatusConfig.class);
    }

    protected void startUp() {
        this.lastRaidVarb = -1;
        this.lastRefresh = 0;
        this.noSocketVenged.clear();
        synchronized (this.statusEffects) {
            this.statusEffects.clear();
        }
        synchronized (this.partyStatus) {
            this.partyStatus.clear();
        }
        this.overlayManager.add((Overlay)this.overlay);
        this.overlayManager.add((Overlay)this.sidebar);
        if (!this.config.specXferList().equals("")) {
            this.playerNames.clear();
            for (final String name : this.config.specXferList().split(",")) {
                if (!name.trim().equals("")) {
                    this.playerNames.add(name.trim().toLowerCase());
                }
            }
        }
        if (!this.config.showPlayerWhiteList().equals("")) {
            this.whiteList.clear();
            for (final String name : this.config.showPlayerWhiteList().split(",")) {
                if (!name.trim().equals("")) {
                    this.whiteList.add(name.trim().toLowerCase());
                }
            }
        }
    }

    protected void shutDown() {
        this.overlayManager.remove((Overlay)this.overlay);
        this.overlayManager.remove((Overlay)this.sidebar);
        this.noSocketVenged.clear();
    }

    @Subscribe
    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals("Socket Player Status Config v3")) {
            if (event.getKey().equals("specXferList")) {
                if (!this.config.specXferList().equals("")) {
                    this.playerNames.clear();
                    for (final String name : this.config.specXferList().split(",")) {
                        if (!name.trim().equals("")) {
                            this.playerNames.add(name.trim().toLowerCase());
                        }
                    }
                }
            }
            else if (event.getKey().equals("showPlayerWhiteList")) {
                this.whiteList.clear();
                if (!this.config.showPlayerWhiteList().equals("")) {
                    for (final String name : this.config.showPlayerWhiteList().split(",")) {
                        if (!name.trim().equals("")) {
                            this.whiteList.add(name.trim().toLowerCase());
                        }
                    }
                }
            }
        }
    }

    @Subscribe
    public void onVarbitChanged(final VarbitChanged event) {
        final int raidVarb = this.client.getVarbitValue(5432);
        final int vengCooldownVarb = this.client.getVarbitValue(2451);
        final int isVengeancedVarb = this.client.getVarbitValue(2450);
        if (this.lastRaidVarb != raidVarb) {
            this.removeGameTimer(GameTimer.OVERLOAD_RAID);
            this.removeGameTimer(GameTimer.PRAYER_ENHANCE);
            this.lastRaidVarb = raidVarb;
        }
        if (this.lastVengCooldownVarb != vengCooldownVarb) {
            if (vengCooldownVarb == 1) {
                this.createGameTimer(GameTimer.VENGEANCE);
            }
            else {
                this.removeGameTimer(GameTimer.VENGEANCE);
            }
            this.lastVengCooldownVarb = vengCooldownVarb;
        }
        if (this.lastIsVengeancedVarb != isVengeancedVarb) {
            if (isVengeancedVarb == 1) {
                this.createGameIndicator(GameIndicator.VENGEANCE_ACTIVE);
            }
            else {
                this.removeGameIndicator(GameIndicator.VENGEANCE_ACTIVE);
            }
            this.lastIsVengeancedVarb = isVengeancedVarb;
        }
    }

    @Subscribe
    public void onMenuOptionClicked(final MenuOptionClicked event) {
        if (event.getMenuOption().contains("Drink") && (event.getItemId() == 12635 || event.getItemId() == 12633)) {
            this.createGameTimer(GameTimer.STAMINA);
        }
    }

    @Subscribe
    public void onChatMessage(final ChatMessage event) {
        if (event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }
        if (event.getMessage().equals("You drink some of your stamina potion.") || event.getMessage().equals("You have received a shared dose of stamina potion.")) {
            this.createGameTimer(GameTimer.STAMINA);
        }
        if (event.getMessage().equals("<col=8f4808>Your stamina potion has expired.</col>")) {
            this.removeGameTimer(GameTimer.STAMINA);
        }
        if (event.getMessage().startsWith("You drink some of your") && event.getMessage().contains("overload")) {
            if (this.client.getVarbitValue(5432) == 1) {
                this.createGameTimer(GameTimer.OVERLOAD_RAID);
            }
            else {
                this.createGameTimer(GameTimer.OVERLOAD);
            }
        }
        if (event.getMessage().startsWith("You drink some of your") && event.getMessage().contains("prayer enhance")) {
            this.createGameTimer(GameTimer.PRAYER_ENHANCE);
        }
        if (event.getMessage().equals("<col=ef1020>Your imbued heart has regained its magical power.</col>")) {
            this.removeGameTimer(GameTimer.IMBUED_HEART);
        }
        if (event.getMessage().equals("You drink some of your stamina potion.") || event.getMessage().equals("You have received a shared dose of stamina potion.")) {
            this.createGameTimer(GameTimer.STAMINA);
        }
        if (event.getMessage().contains("You drink some of your divine")) {
            if (event.getMessage().contains("divine ranging")) {
                this.createGameTimer(GameTimer.DIVINE_RANGE);
            }
            else if (event.getMessage().contains("divine bastion")) {
                this.createGameTimer(GameTimer.DIVINE_BASTION);
            }
            else if (event.getMessage().contains("divine combat")) {
                this.createGameTimer(GameTimer.DIVINE_SCB);
            }
            else if (event.getMessage().contains("divine super attack")) {
                this.createGameTimer(GameTimer.DIVINE_ATTACK);
            }
            else if (event.getMessage().contains("divine super strength")) {
                this.createGameTimer(GameTimer.DIVINE_STRENGTH);
            }
        }
    }

    @Subscribe
    public void onGraphicChanged(final GraphicChanged event) {
        if (this.client.getLocalPlayer() != null && event.getActor() instanceof Player) {
            final Player player = (Player)event.getActor();
            if (player.getName() != null && !player.getName().equals(this.client.getLocalPlayer().getName()) && this.partyStatus.get(player.getName()) == null && (player.getGraphic() == 726 || player.getGraphic() == 725) && !this.noSocketVenged.contains(player.getName())) {
                this.noSocketVenged.add(player.getName());
            }
            else if (player.getGraphic() == GameTimer.IMBUED_HEART.getGraphicId()) {
                this.createGameTimer(GameTimer.IMBUED_HEART);
            }
        }
    }

    @Subscribe
    public void onOverheadTextChanged(final OverheadTextChanged event) {
        final Actor actor = event.getActor();
        if (actor instanceof Player && actor.getName() != null && this.noSocketVenged.contains(actor.getName()) && actor.getOverheadText().equals("Taste vengeance!")) {
            this.noSocketVenged.remove(actor.getName());
        }
    }

    @Subscribe
    public void onActorDeath(final ActorDeath event) {
        if (event.getActor() == this.client.getLocalPlayer()) {
            synchronized (this.statusEffects) {
                final List<AbstractMarker> activeEffects = this.statusEffects.get(null);
                if (activeEffects != null) {
                    for (final AbstractMarker marker : new ArrayList<AbstractMarker>(activeEffects)) {
                        if (marker instanceof TimerMarker) {
                            final TimerMarker timer = (TimerMarker)marker;
                            if (!timer.getTimer().isRemovedOnDeath()) {
                                continue;
                            }
                            activeEffects.remove(marker);
                        }
                    }
                    if (activeEffects.isEmpty()) {
                        this.statusEffects.remove(null);
                    }
                }
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(final GameStateChanged event) {
        switch (event.getGameState()) {
            case HOPPING:
            case LOGIN_SCREEN:
            case LOGIN_SCREEN_AUTHENTICATOR: {
                synchronized (this.statusEffects) {
                    for (final String s : new ArrayList<String>(this.statusEffects.keySet())) {
                        if (s != null) {
                            this.statusEffects.remove(s);
                        }
                    }
                }
                synchronized (this.partyStatus) {
                    this.partyStatus.clear();
                }
                break;
            }
        }
    }

    @Subscribe
    public void onGameTick(final GameTick event) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        final int currentHealth = this.client.getBoostedSkillLevel(Skill.HITPOINTS);
        final int currentPrayer = this.client.getBoostedSkillLevel(Skill.PRAYER);
        final int maxHealth = this.client.getRealSkillLevel(Skill.HITPOINTS);
        final int maxPrayer = this.client.getRealSkillLevel(Skill.PRAYER);
        final int specialAttack = this.client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10;
        final int runEnergy = this.client.getEnergy();
        final String name = this.client.getLocalPlayer().getName();
        PlayerStatus status;
        synchronized (this.partyStatus) {
            status = this.partyStatus.get(name);
            if (status == null) {
                status = new PlayerStatus(currentHealth, maxHealth, currentPrayer, maxPrayer, runEnergy, specialAttack);
                this.partyStatus.put(name, status);
            }
            else {
                status.setHealth(currentHealth);
                status.setMaxHealth(maxHealth);
                status.setPrayer(currentPrayer);
                status.setMaxPrayer(maxPrayer);
                status.setRun(runEnergy);
                status.setSpecial(specialAttack);
            }
            if (this.config.showSpecXfer() == PlayerStatusConfig.xferIconMode.ALL || (this.config.showSpecXfer() == PlayerStatusConfig.xferIconMode.LIST && this.playerNames.contains(name.toLowerCase()))) {
                if (specialAttack <= this.config.specThreshold()) {
                    this.createGameIndicator(GameIndicator.SPEC_XFER);
                }
                else {
                    this.removeGameIndicator(GameIndicator.SPEC_XFER);
                }
            }
        }
        ++this.lastRefresh;
        if (this.lastRefresh >= Math.max(1, this.config.getStatsRefreshRate())) {
            final JSONObject packet = new JSONObject();
            packet.put("name", name);
            packet.put("player-stats", status.toJSON());
            this.eventBus.post((Object)new SocketBroadcastPacket(packet));
            this.lastRefresh = 0;
        }
    }

    private void sortMarkers(final List<AbstractMarker> markers) {
        markers.sort(new Comparator<AbstractMarker>() {
            @Override
            public int compare(final AbstractMarker o1, final AbstractMarker o2) {
                return Integer.compare(this.getMarkerOrdinal(o1), this.getMarkerOrdinal(o2));
            }

            private int getMarkerOrdinal(final AbstractMarker marker) {
                if (marker == null) {
                    return -1;
                }
                if (marker instanceof IndicatorMarker) {
                    return ((IndicatorMarker)marker).getIndicator().ordinal();
                }
                if (marker instanceof TimerMarker) {
                    return ((TimerMarker)marker).getTimer().ordinal();
                }
                return -1;
            }
        });
    }

    private void createGameTimer(final GameTimer timer) {
        this.createGameTimer(timer, null);
        final JSONObject packet = new JSONObject();
        packet.put("player-status-game-add", this.client.getLocalPlayer().getName());
        packet.put("effect-name", timer.name());
        this.eventBus.post((Object)new SocketBroadcastPacket(packet));
    }

    private void createGameTimer(final GameTimer timer, final String name) {
        final TimerMarker marker = new TimerMarker(timer, System.currentTimeMillis());
        switch (timer.getImageType()) {
            case SPRITE: {
                marker.setBaseImage(this.spriteManager.getSprite(timer.getImageId(), 0));
                break;
            }
            case ITEM: {
                marker.setBaseImage((BufferedImage)this.itemManager.getImage(timer.getImageId()));
                break;
            }
        }
        this.removeGameTimer(timer, name);
        synchronized (this.statusEffects) {
            List<AbstractMarker> activeEffects = this.statusEffects.get(name);
            if (activeEffects == null) {
                activeEffects = new ArrayList<AbstractMarker>();
                this.statusEffects.put(name, activeEffects);
            }
            activeEffects.add(marker);
            this.sortMarkers(activeEffects);
        }
    }

    private void removeGameTimer(final GameTimer timer) {
        this.removeGameTimer(timer, null);
        if (this.client.getLocalPlayer() != null) {
            final JSONObject packet = new JSONObject();
            packet.put("player-status-game-remove", this.client.getLocalPlayer().getName());
            packet.put("effect-name", timer.name());
            this.eventBus.post((Object)new SocketBroadcastPacket(packet));
        }
    }

    private void removeGameTimer(final GameTimer timer, final String name) {
        synchronized (this.statusEffects) {
            final List<AbstractMarker> activeEffects = this.statusEffects.get(name);
            if (activeEffects == null) {
                return;
            }
            for (final AbstractMarker marker : new ArrayList<AbstractMarker>(activeEffects)) {
                if (marker instanceof TimerMarker) {
                    final TimerMarker instance = (TimerMarker)marker;
                    if (instance.getTimer() != timer) {
                        continue;
                    }
                    activeEffects.remove(marker);
                }
            }
            if (activeEffects.isEmpty()) {
                this.statusEffects.remove(name);
            }
        }
    }

    private void createGameIndicator(final GameIndicator gameIndicator) {
        this.createGameIndicator(gameIndicator, null);
        if (this.client.getLocalPlayer() == null) {
            return;
        }
        final JSONObject packet = new JSONObject();
        packet.put("player-status-indicator-add", this.client.getLocalPlayer().getName());
        packet.put("effect-name", gameIndicator.name());
        this.eventBus.post((Object)new SocketBroadcastPacket(packet));
    }

    private void createGameIndicator(final GameIndicator gameIndicator, final String name) {
        final IndicatorMarker marker = new IndicatorMarker(gameIndicator);
        switch (gameIndicator.getImageType()) {
            case SPRITE: {
                marker.setBaseImage(this.spriteManager.getSprite(gameIndicator.getImageId(), 0));
                break;
            }
            case ITEM: {
                marker.setBaseImage((BufferedImage)this.itemManager.getImage(gameIndicator.getImageId()));
                break;
            }
        }
        this.removeGameIndicator(gameIndicator, name);
        synchronized (this.statusEffects) {
            List<AbstractMarker> activeEffects = this.statusEffects.get(name);
            if (activeEffects == null) {
                activeEffects = new ArrayList<AbstractMarker>();
                this.statusEffects.put(name, activeEffects);
            }
            activeEffects.add(marker);
            this.sortMarkers(activeEffects);
        }
    }

    private void removeGameIndicator(final GameIndicator indicator) {
        this.removeGameIndicator(indicator, null);
        final JSONObject packet = new JSONObject();
        packet.put("player-status-indicator-remove", this.client.getLocalPlayer().getName());
        packet.put("effect-name", indicator.name());
        this.eventBus.post((Object)new SocketBroadcastPacket(packet));
    }

    private void removeGameIndicator(final GameIndicator indicator, final String name) {
        synchronized (this.statusEffects) {
            final List<AbstractMarker> activeEffects = this.statusEffects.get(name);
            if (activeEffects == null) {
                return;
            }
            for (final AbstractMarker marker : new ArrayList<AbstractMarker>(activeEffects)) {
                if (marker instanceof IndicatorMarker) {
                    final IndicatorMarker instance = (IndicatorMarker)marker;
                    if (instance.getIndicator() != indicator) {
                        continue;
                    }
                    activeEffects.remove(marker);
                }
            }
            if (activeEffects.isEmpty()) {
                this.statusEffects.remove(name);
            }
        }
    }

    @Subscribe
    public void onSocketReceivePacket(final SocketReceivePacket event) {
        try {
            final JSONObject payload = event.getPayload();
            final String localName = this.client.getLocalPlayer().getName();
            if (payload.has("player-stats")) {
                final String targetName = payload.getString("name");
                if (targetName.equals(localName)) {
                    return;
                }
                final JSONObject statusJson = payload.getJSONObject("player-stats");
                synchronized (this.partyStatus) {
                    PlayerStatus status = this.partyStatus.get(targetName);
                    if (status == null) {
                        status = PlayerStatus.fromJSON(statusJson);
                        this.partyStatus.put(targetName, status);
                    }
                    else {
                        status.parseJSON(statusJson);
                    }
                }
            }
            else if (payload.has("player-status-game-add")) {
                final String targetName = payload.getString("player-status-game-add");
                if (targetName.equals(localName)) {
                    return;
                }
                final String effectName = payload.getString("effect-name");
                final GameTimer timer = GameTimer.valueOf(effectName);
                this.createGameTimer(timer, targetName);
            }
            else if (payload.has("player-status-game-remove")) {
                final String targetName = payload.getString("player-status-game-remove");
                if (targetName.equals(localName)) {
                    return;
                }
                final String effectName = payload.getString("effect-name");
                final GameTimer timer = GameTimer.valueOf(effectName);
                this.removeGameTimer(timer, targetName);
            }
            else if (payload.has("player-status-indicator-add")) {
                final String targetName = payload.getString("player-status-indicator-add");
                if (targetName.equals(localName)) {
                    return;
                }
                final String effectName = payload.getString("effect-name");
                final GameIndicator indicator = GameIndicator.valueOf(effectName);
                this.createGameIndicator(indicator, targetName);
            }
            else if (payload.has("player-status-indicator-remove")) {
                final String targetName = payload.getString("player-status-indicator-remove");
                if (targetName.equals(localName)) {
                    return;
                }
                final String effectName = payload.getString("effect-name");
                final GameIndicator indicator = GameIndicator.valueOf(effectName);
                this.removeGameIndicator(indicator, targetName);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onSocketPlayerLeave(final SocketPlayerLeave event) {
        final String target = event.getPlayerName();
        synchronized (this.statusEffects) {
            if (this.statusEffects.containsKey(target)) {
                this.statusEffects.remove(target);
            }
        }
        synchronized (this.partyStatus) {
            if (this.partyStatus.containsKey(target)) {
                this.partyStatus.remove(target);
            }
        }
    }

    public Map<String, List<AbstractMarker>> getStatusEffects() {
        return this.statusEffects;
    }

    public Map<String, PlayerStatus> getPartyStatus() {
        return this.partyStatus;
    }

    public List<String> getWhiteList() {
        return this.whiteList;
    }

    static {
        log = LoggerFactory.getLogger((Class)PlayerStatusPlugin.class);
    }
}
