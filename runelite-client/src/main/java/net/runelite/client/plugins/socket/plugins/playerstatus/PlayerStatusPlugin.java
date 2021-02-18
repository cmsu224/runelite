//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.playerstatus;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.Varbits;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketPlayerLeave;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
//import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatusPlugin.1;
//import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatusPlugin.2;
import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameIndicator;
import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameTimer;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.AbstractMarker;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.IndicatorMarker;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.TimerMarker;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "Socket - Player Status",
        description = "Socket extension for displaying player status to members in your party.",
        tags = {"socket", "server", "discord", "connection", "broadcast", "player", "status", "venge", "vengeance"},
        enabledByDefault = true
)
public class PlayerStatusPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(PlayerStatusPlugin.class);
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
    private Map<String, List<AbstractMarker>> statusEffects = new HashMap();
    private Map<String, PlayerStatus> partyStatus = new TreeMap();
    private int lastRaidVarb;
    private int lastVengCooldownVarb;
    private int lastIsVengeancedVarb;
    private int lastRefresh;

    public PlayerStatusPlugin() {
    }

    @Provides
    PlayerStatusConfig getConfig(ConfigManager configManager) {
        return (PlayerStatusConfig)configManager.getConfig(PlayerStatusConfig.class);
    }

    protected void startUp() {
        this.lastRaidVarb = -1;
        this.lastRefresh = 0;
        synchronized(this.statusEffects) {
            this.statusEffects.clear();
        }

        synchronized(this.partyStatus) {
            this.partyStatus.clear();
        }

        this.overlayManager.add(this.overlay);
        this.overlayManager.add(this.sidebar);
    }

    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
        this.overlayManager.remove(this.sidebar);
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        int raidVarb = this.client.getVar(Varbits.IN_RAID);
        int vengCooldownVarb = this.client.getVar(Varbits.VENGEANCE_COOLDOWN);
        int isVengeancedVarb = this.client.getVar(Varbits.VENGEANCE_ACTIVE);
        if (this.lastRaidVarb != raidVarb) {
            this.removeGameTimer(GameTimer.OVERLOAD_RAID);
            this.removeGameTimer(GameTimer.PRAYER_ENHANCE);
            this.lastRaidVarb = raidVarb;
        }

        if (this.lastVengCooldownVarb != vengCooldownVarb) {
            if (vengCooldownVarb == 1) {
                this.createGameTimer(GameTimer.VENGEANCE);
            } else {
                this.removeGameTimer(GameTimer.VENGEANCE);
            }

            this.lastVengCooldownVarb = vengCooldownVarb;
        }

        if (this.lastIsVengeancedVarb != isVengeancedVarb) {
            if (isVengeancedVarb == 1) {
                this.createGameIndicator(GameIndicator.VENGEANCE_ACTIVE);
            } else {
                this.removeGameIndicator(GameIndicator.VENGEANCE_ACTIVE);
            }

            this.lastIsVengeancedVarb = isVengeancedVarb;
        }

    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (event.getMenuOption().contains("Drink") && (event.getId() == 12635 || event.getId() == 12633)) {
            this.createGameTimer(GameTimer.STAMINA);
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
            if (event.getMessage().equals("You drink some of your stamina potion.") || event.getMessage().equals("You have received a shared dose of stamina potion.")) {
                this.createGameTimer(GameTimer.STAMINA);
            }

            if (event.getMessage().equals("<col=8f4808>Your stamina potion has expired.</col>")) {
                this.removeGameTimer(GameTimer.STAMINA);
            }

            if (event.getMessage().startsWith("You drink some of your") && event.getMessage().contains("overload")) {
                if (this.client.getVar(Varbits.IN_RAID) == 1) {
                    this.createGameTimer(GameTimer.OVERLOAD_RAID);
                } else {
                    this.createGameTimer(GameTimer.OVERLOAD);
                }
            }

            if (event.getMessage().startsWith("You drink some of your") && event.getMessage().contains("prayer enhance")) {
                this.createGameTimer(GameTimer.PRAYER_ENHANCE);
            }

            if (event.getMessage().equals("<col=ef1020>Your imbued heart has regained its magical power.</col>")) {
                this.removeGameTimer(GameTimer.IMBUED_HEART);
            }

        }
    }

    @Subscribe
    public void onGraphicChanged(GraphicChanged event) {
        Actor actor = event.getActor();
        if (actor == this.client.getLocalPlayer()) {
            if (actor.getGraphic() == GameTimer.IMBUED_HEART.getGraphicId()) {
                this.createGameTimer(GameTimer.IMBUED_HEART);
            }

        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath event) {
        if (event.getActor() == this.client.getLocalPlayer()) {
            synchronized(this.statusEffects) {
                List<AbstractMarker> activeEffects = (List)this.statusEffects.get((Object)null);
                if (activeEffects != null) {
                    Iterator var4 = (new ArrayList(activeEffects)).iterator();

                    while(var4.hasNext()) {
                        AbstractMarker marker = (AbstractMarker)var4.next();
                        if (marker instanceof TimerMarker) {
                            TimerMarker timer = (TimerMarker)marker;
                            if (timer.getTimer().isRemovedOnDeath()) {
                                activeEffects.remove(marker);
                            }
                        }
                    }

                    if (activeEffects.isEmpty()) {
                        this.statusEffects.remove((Object)null);
                    }

                }
            }
        }
    }
/*
    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        switch(2.$SwitchMap$net$runelite$api$GameState[event.getGameState().ordinal()]) {
            case 1:
            case 2:
            case 3:
                synchronized(this.statusEffects) {
                    Iterator var3 = (new ArrayList(this.statusEffects.keySet())).iterator();

                    while(var3.hasNext()) {
                        String s = (String)var3.next();
                        if (s != null) {
                            this.statusEffects.remove(s);
                        }
                    }
                }

                synchronized(this.partyStatus) {
                    this.partyStatus.clear();
                }
            default:
        }
    }*/

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            int currentHealth = this.client.getBoostedSkillLevel(Skill.HITPOINTS);
            int currentPrayer = this.client.getBoostedSkillLevel(Skill.PRAYER);
            int maxHealth = this.client.getRealSkillLevel(Skill.HITPOINTS);
            int maxPrayer = this.client.getRealSkillLevel(Skill.PRAYER);
            int specialAttack = this.client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10;
            int runEnergy = this.client.getEnergy();
            String name = this.client.getLocalPlayer().getName();
            PlayerStatus status;
            synchronized(this.partyStatus) {
                status = (PlayerStatus)this.partyStatus.get(name);
                if (status == null) {
                    status = new PlayerStatus(currentHealth, maxHealth, currentPrayer, maxPrayer, runEnergy, specialAttack);
                    this.partyStatus.put(name, status);
                } else {
                    status.setHealth(currentHealth);
                    status.setMaxHealth(maxHealth);
                    status.setPrayer(currentPrayer);
                    status.setMaxPrayer(maxPrayer);
                    status.setRun(runEnergy);
                    status.setSpecial(specialAttack);
                }
            }

            ++this.lastRefresh;
            if (this.lastRefresh >= Math.max(1, this.config.getStatsRefreshRate())) {
                JSONObject packet = new JSONObject();
                packet.put("name", name);
                packet.put("player-stats", status.toJSON());
                this.eventBus.post(new SocketBroadcastPacket(packet));
                this.lastRefresh = 0;
            }

        }
    }

    private void sortMarkers(List<AbstractMarker> markers) {
        //markers.sort(new 1(this));
    }

    private void createGameTimer(GameTimer timer) {
        //this.createGameTimer(timer, (String)null);
        JSONObject packet = new JSONObject();
        packet.put("player-status-game-add", this.client.getLocalPlayer().getName());
        packet.put("effect-name", timer.name());
        this.eventBus.post(new SocketBroadcastPacket(packet));
    }
/*
    private void createGameTimer(GameTimer timer, String name) {
        TimerMarker marker = new TimerMarker(timer, System.currentTimeMillis());
        switch(2.$SwitchMap$net$runelite$client$plugins$socket$plugins$playerstatus$gametimer$GameTimerImageType[timer.getImageType().ordinal()]) {
            case 1:
                marker.setBaseImage(this.spriteManager.getSprite(timer.getImageId(), 0));
                break;
            case 2:
                marker.setBaseImage(this.itemManager.getImage(timer.getImageId()));
        }

        this.removeGameTimer(timer, name);
        synchronized(this.statusEffects) {
            List<AbstractMarker> activeEffects = (List)this.statusEffects.get(name);
            if (activeEffects == null) {
                activeEffects = new ArrayList();
                this.statusEffects.put(name, activeEffects);
            }

            ((List)activeEffects).add(marker);
            this.sortMarkers((List)activeEffects);
        }
    }*/

    private void removeGameTimer(GameTimer timer) {
        this.removeGameTimer(timer, (String)null);
        if (this.client.getLocalPlayer() != null) {
            JSONObject packet = new JSONObject();
            packet.put("player-status-game-remove", this.client.getLocalPlayer().getName());
            packet.put("effect-name", timer.name());
            this.eventBus.post(new SocketBroadcastPacket(packet));
        }
    }

    private void removeGameTimer(GameTimer timer, String name) {
        synchronized(this.statusEffects) {
            List<AbstractMarker> activeEffects = (List)this.statusEffects.get(name);
            if (activeEffects != null) {
                Iterator var5 = (new ArrayList(activeEffects)).iterator();

                while(var5.hasNext()) {
                    AbstractMarker marker = (AbstractMarker)var5.next();
                    if (marker instanceof TimerMarker) {
                        TimerMarker instance = (TimerMarker)marker;
                        if (instance.getTimer() == timer) {
                            activeEffects.remove(marker);
                        }
                    }
                }

                if (activeEffects.isEmpty()) {
                    this.statusEffects.remove(name);
                }

            }
        }
    }

    private void createGameIndicator(GameIndicator gameIndicator) {
        //this.createGameIndicator(gameIndicator, (String)null);
        if (this.client.getLocalPlayer() != null) {
            JSONObject packet = new JSONObject();
            packet.put("player-status-indicator-add", this.client.getLocalPlayer().getName());
            packet.put("effect-name", gameIndicator.name());
            this.eventBus.post(new SocketBroadcastPacket(packet));
        }
    }
/*
    private void createGameIndicator(GameIndicator gameIndicator, String name) {
        IndicatorMarker marker = new IndicatorMarker(gameIndicator);
        switch(2.$SwitchMap$net$runelite$client$plugins$socket$plugins$playerstatus$gametimer$GameTimerImageType[gameIndicator.getImageType().ordinal()]) {
            case 1:
                marker.setBaseImage(this.spriteManager.getSprite(gameIndicator.getImageId(), 0));
                break;
            case 2:
                marker.setBaseImage(this.itemManager.getImage(gameIndicator.getImageId()));
        }

        this.removeGameIndicator(gameIndicator, name);
        synchronized(this.statusEffects) {
            List<AbstractMarker> activeEffects = (List)this.statusEffects.get(name);
            if (activeEffects == null) {
                activeEffects = new ArrayList();
                this.statusEffects.put(name, activeEffects);
            }

            ((List)activeEffects).add(marker);
            this.sortMarkers((List)activeEffects);
        }
    }*/

    private void removeGameIndicator(GameIndicator indicator) {
        this.removeGameIndicator(indicator, (String)null);
        JSONObject packet = new JSONObject();
        packet.put("player-status-indicator-remove", this.client.getLocalPlayer().getName());
        packet.put("effect-name", indicator.name());
        this.eventBus.post(new SocketBroadcastPacket(packet));
    }

    private void removeGameIndicator(GameIndicator indicator, String name) {
        synchronized(this.statusEffects) {
            List<AbstractMarker> activeEffects = (List)this.statusEffects.get(name);
            if (activeEffects != null) {
                Iterator var5 = (new ArrayList(activeEffects)).iterator();

                while(var5.hasNext()) {
                    AbstractMarker marker = (AbstractMarker)var5.next();
                    if (marker instanceof IndicatorMarker) {
                        IndicatorMarker instance = (IndicatorMarker)marker;
                        if (instance.getIndicator() == indicator) {
                            activeEffects.remove(marker);
                        }
                    }
                }

                if (activeEffects.isEmpty()) {
                    this.statusEffects.remove(name);
                }

            }
        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            JSONObject payload = event.getPayload();
            String localName = this.client.getLocalPlayer().getName();
            String targetName;
            if (payload.has("player-stats")) {
                targetName = payload.getString("name");
                if (targetName.equals(localName)) {
                    return;
                }

                JSONObject statusJson = payload.getJSONObject("player-stats");
                synchronized(this.partyStatus) {
                    PlayerStatus status = (PlayerStatus)this.partyStatus.get(targetName);
                    if (status == null) {
                        status = PlayerStatus.fromJSON(statusJson);
                        this.partyStatus.put(targetName, status);
                    } else {
                        status.parseJSON(statusJson);
                    }
                }
            } else {
                String effectName;
                GameTimer timer;
                if (payload.has("player-status-game-add")) {
                    targetName = payload.getString("player-status-game-add");
                    if (targetName.equals(localName)) {
                        return;
                    }

                    effectName = payload.getString("effect-name");
                    timer = GameTimer.valueOf(effectName);
                    //this.createGameTimer(timer, targetName);
                } else if (payload.has("player-status-game-remove")) {
                    targetName = payload.getString("player-status-game-remove");
                    if (targetName.equals(localName)) {
                        return;
                    }

                    effectName = payload.getString("effect-name");
                    timer = GameTimer.valueOf(effectName);
                    this.removeGameTimer(timer, targetName);
                } else {
                    GameIndicator indicator;
                    if (payload.has("player-status-indicator-add")) {
                        targetName = payload.getString("player-status-indicator-add");
                        if (targetName.equals(localName)) {
                            return;
                        }

                        effectName = payload.getString("effect-name");
                        indicator = GameIndicator.valueOf(effectName);
                        //this.createGameIndicator(indicator, targetName);
                    } else if (payload.has("player-status-indicator-remove")) {
                        targetName = payload.getString("player-status-indicator-remove");
                        if (targetName.equals(localName)) {
                            return;
                        }

                        effectName = payload.getString("effect-name");
                        indicator = GameIndicator.valueOf(effectName);
                        this.removeGameIndicator(indicator, targetName);
                    }
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    @Subscribe
    public void onSocketPlayerLeave(SocketPlayerLeave event) {
        String target = event.getPlayerName();
        synchronized(this.statusEffects) {
            if (this.statusEffects.containsKey(target)) {
                this.statusEffects.remove(target);
            }
        }

        synchronized(this.partyStatus) {
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
}
