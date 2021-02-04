//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus;

import com.google.common.collect.ComparisonChain;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.ProjectileSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.cerberus.domain.Arena;
import net.runelite.client.plugins.cerberus.domain.Cerberus;
import net.runelite.client.plugins.cerberus.domain.CerberusAttack;
import net.runelite.client.plugins.cerberus.domain.Ghost;
import net.runelite.client.plugins.cerberus.domain.Phase;
import net.runelite.client.plugins.cerberus.domain.Cerberus.Attack;
import net.runelite.client.plugins.cerberus.overlays.CurrentAttackOverlay;
import net.runelite.client.plugins.cerberus.overlays.PrayerOverlay;
import net.runelite.client.plugins.cerberus.overlays.SceneOverlay;
import net.runelite.client.plugins.cerberus.overlays.UpcomingAttackOverlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.http.api.item.ItemEquipmentStats;
import net.runelite.http.api.item.ItemStats;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Extension
@PluginDescriptor(
        name = "Cerberus",
        enabledByDefault = false,
        description = "A plugin for the Cerberus boss.",
        tags = {"cerberus", "hellhound"},
        type = PluginType.PVM
)
public class CerberusPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(CerberusPlugin.class);
    private static final int ANIMATION_ID_IDLE = -1;
    private static final int ANIMATION_ID_STAND_UP = 4486;
    private static final int ANIMATION_ID_SIT_DOWN = 4487;
    private static final int ANIMATION_ID_FLINCH = 4489;
    private static final int ANIMATION_ID_RANGED = 4490;
    private static final int ANIMATION_ID_MELEE = 4491;
    private static final int ANIMATION_ID_LAVA = 4493;
    private static final int ANIMATION_ID_GHOSTS = 4494;
    private static final int ANIMATION_ID_DEATH = 4495;
    private static final int PROJECTILE_ID_MAGIC = 1242;
    private static final int PROJECTILE_ID_RANGE = 1245;
    private static final int GHOST_PROJECTILE_ID_RANGE = 34;
    private static final int GHOST_PROJECTILE_ID_MAGIC = 100;
    private static final int GHOST_PROJECTILE_ID_MELEE = 1248;
    private static final int PROJECTILE_ID_NO_FUCKING_IDEA = 15;
    private static final int PROJECTILE_ID_LAVA = 1247;
    private static final Set<Integer> REGION_IDS = Set.of(4883, 5140, 5395);
    @Inject
    private Client client;
    @Inject
    private EventBus eventBus;
    @Inject
    private CerberusConfig config;
    @Inject
    private ItemManager itemManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SceneOverlay sceneOverlay;
    @Inject
    private PrayerOverlay prayerOverlay;
    @Inject
    private CurrentAttackOverlay currentAttackOverlay;
    @Inject
    private UpcomingAttackOverlay upcomingAttackOverlay;
    private final List<NPC> ghosts = new ArrayList();
    private final List<CerberusAttack> upcomingAttacks = new ArrayList();
    private final List<Long> tickTimestamps = new ArrayList();
    @Nullable
    private Prayer prayer;
    @Nullable
    private Cerberus cerberus;
    private int gameTick;
    private int tickTimestampIndex;
    private long lastTick;
    private boolean inArena;

    public CerberusPlugin() {
        this.prayer = Prayer.PROTECT_FROM_MAGIC;
    }

    @Provides
    CerberusConfig getConfig(ConfigManager configManager) {
        return (CerberusConfig)configManager.getConfig(CerberusConfig.class);
    }

    protected void startUp() {
        if (this.client.getGameState() == GameState.LOGGED_IN && this.inCerberusRegion()) {
            this.init();
        }
    }

    private void init() {
        this.inArena = true;
        this.overlayManager.add(this.sceneOverlay);
        this.overlayManager.add(this.prayerOverlay);
        this.overlayManager.add(this.currentAttackOverlay);
        this.overlayManager.add(this.upcomingAttackOverlay);
        this.eventBus.subscribe(GameTick.class, this, this::onGameTick);
        this.eventBus.subscribe(ProjectileSpawned.class, this, this::onProjectileSpawned);
        this.eventBus.subscribe(AnimationChanged.class, this, this::onAnimationChanged);
        this.eventBus.subscribe(NpcSpawned.class, this, this::onNpcSpawned);
        this.eventBus.subscribe(NpcDespawned.class, this, this::onNpcDespawned);
    }

    protected void shutDown() {
        this.inArena = false;
        this.eventBus.unregister(this);
        this.overlayManager.remove(this.sceneOverlay);
        this.overlayManager.remove(this.prayerOverlay);
        this.overlayManager.remove(this.currentAttackOverlay);
        this.overlayManager.remove(this.upcomingAttackOverlay);
        this.ghosts.clear();
        this.upcomingAttacks.clear();
        this.tickTimestamps.clear();
        this.prayer = Prayer.PROTECT_FROM_MAGIC;
        this.cerberus = null;
        this.gameTick = 0;
        this.tickTimestampIndex = 0;
        this.lastTick = 0L;
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("cerberus")) {
            if (event.getKey().equals("mirrorMode")) {
                this.sceneOverlay.determineLayer();
                this.prayerOverlay.determineLayer();
                this.currentAttackOverlay.determineLayer();
                this.upcomingAttackOverlay.determineLayer();
                if (this.inArena) {
                    this.overlayManager.remove(this.sceneOverlay);
                    this.overlayManager.remove(this.prayerOverlay);
                    this.overlayManager.remove(this.currentAttackOverlay);
                    this.overlayManager.remove(this.upcomingAttackOverlay);
                    this.overlayManager.add(this.sceneOverlay);
                    this.overlayManager.add(this.prayerOverlay);
                    this.overlayManager.add(this.currentAttackOverlay);
                    this.overlayManager.add(this.upcomingAttackOverlay);
                }
            }

        }
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        GameState gameState = event.getGameState();
        switch(gameState) {
            case LOGGED_IN:
                if (this.inCerberusRegion()) {
                    if (!this.inArena) {
                        this.init();
                    }
                } else if (this.inArena) {
                    this.shutDown();
                }
                break;
            case HOPPING:
            case LOGIN_SCREEN:
                if (this.inArena) {
                    this.shutDown();
                }
        }

    }

    private void onGameTick(GameTick event) {
        if (this.cerberus != null) {
            if (this.tickTimestamps.size() <= this.tickTimestampIndex) {
                this.tickTimestamps.add(System.currentTimeMillis());
            } else {
                this.tickTimestamps.set(this.tickTimestampIndex, System.currentTimeMillis());
            }

            long min = 0L;

            for(int i = 0; i < this.tickTimestamps.size(); ++i) {
                if (min == 0L) {
                    min = (Long)this.tickTimestamps.get(i) + (long)(600 * ((this.tickTimestampIndex - i + 5) % 5));
                } else {
                    min = Math.min(min, (Long)this.tickTimestamps.get(i) + (long)(600 * ((this.tickTimestampIndex - i + 5) % 5)));
                }
            }

            this.tickTimestampIndex = (this.tickTimestampIndex + 1) % 5;
            this.lastTick = min;
            ++this.gameTick;
            if (this.config.calculateAutoAttackPrayer() && this.gameTick % 10 == 3) {
                this.setAutoAttackPrayer();
            }

            this.calculateUpcomingAttacks();
            if (this.ghosts.size() > 1) {
                this.ghosts.sort((a, b) -> {
                    return ComparisonChain.start().compare(a.getLocalLocation().getY(), b.getLocalLocation().getY()).compare(a.getLocalLocation().getX(), b.getLocalLocation().getX()).result();
                });
            }

        }
    }

    private void onProjectileSpawned(ProjectileSpawned event) {
        if (this.cerberus != null) {
            Projectile projectile = event.getProjectile();
            int hp = this.cerberus.getHp();
            Phase expectedAttack = this.cerberus.getNextAttackPhase(1, hp);
            switch(projectile.getId()) {
                case 15:
                case 1247:
                default:
                    break;
                case 34:
                    if (!this.ghosts.isEmpty()) {
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", new Object[]{this.gameTick, this.cerberus.getPhaseCount() + 1, hp, expectedAttack, "RANGED"});
                    }
                    break;
                case 100:
                    if (!this.ghosts.isEmpty()) {
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", new Object[]{this.gameTick, this.cerberus.getPhaseCount() + 1, hp, expectedAttack, "MAGIC"});
                    }
                    break;
                case 1242:
                    log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbProjectile={}", new Object[]{this.gameTick, this.cerberus.getPhaseCount() + 1, hp, expectedAttack, "MAGIC"});
                    if (expectedAttack != Phase.TRIPLE) {
                        this.cerberus.nextPhase(Phase.AUTO);
                    } else {
                        this.cerberus.setLastTripleAttack(Attack.MAGIC);
                    }

                    this.cerberus.doProjectileOrAnimation(this.gameTick, Attack.MAGIC);
                    break;
                case 1245:
                    log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbProjectile={}", new Object[]{this.gameTick, this.cerberus.getPhaseCount() + 1, hp, expectedAttack, "RANGED"});
                    if (expectedAttack != Phase.TRIPLE) {
                        this.cerberus.nextPhase(Phase.AUTO);
                    } else {
                        this.cerberus.setLastTripleAttack(Attack.RANGED);
                    }

                    this.cerberus.doProjectileOrAnimation(this.gameTick, Attack.RANGED);
                    break;
                case 1248:
                    if (!this.ghosts.isEmpty()) {
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", new Object[]{this.gameTick, this.cerberus.getPhaseCount() + 1, hp, expectedAttack, "MELEE"});
                    }
            }

        }
    }

    private void onAnimationChanged(AnimationChanged event) {
        if (this.cerberus != null) {
            Actor actor = event.getActor();
            NPC npc = this.cerberus.getNpc();
            if (npc != null && actor == npc) {
                int animationId = npc.getAnimation();
                int hp = this.cerberus.getHp();
                Phase expectedAttack = this.cerberus.getNextAttackPhase(1, hp);
                switch(animationId) {
                    case -1:
                    case 4489:
                    case 4490:
                        break;
                    case 4486:
                    case 4487:
                        this.cerberus = new Cerberus(this.cerberus.getNpc());
                        this.gameTick = 0;
                        this.lastTick = System.currentTimeMillis();
                        this.upcomingAttacks.clear();
                        this.tickTimestamps.clear();
                        this.tickTimestampIndex = 0;
                        this.cerberus.doProjectileOrAnimation(this.gameTick, Attack.SPAWN);
                        break;
                    case 4491:
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", new Object[]{this.gameTick, this.cerberus.getPhaseCount() + 1, hp, expectedAttack, "MELEE"});
                        this.cerberus.setLastTripleAttack((Attack)null);
                        this.cerberus.nextPhase(expectedAttack);
                        this.cerberus.doProjectileOrAnimation(this.gameTick, Attack.MELEE);
                        break;
                    case 4493:
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", new Object[]{this.gameTick, this.cerberus.getPhaseCount() + 1, hp, expectedAttack, "LAVA"});
                        this.cerberus.nextPhase(Phase.LAVA);
                        this.cerberus.doProjectileOrAnimation(this.gameTick, Attack.LAVA);
                        break;
                    case 4494:
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", new Object[]{this.gameTick, this.cerberus.getPhaseCount() + 1, hp, expectedAttack, "GHOSTS"});
                        this.cerberus.nextPhase(Phase.GHOSTS);
                        this.cerberus.setLastGhostYellTick(this.gameTick);
                        this.cerberus.setLastGhostYellTime(System.currentTimeMillis());
                        this.cerberus.doProjectileOrAnimation(this.gameTick, Attack.GHOSTS);
                        break;
                    case 4495:
                        this.cerberus = null;
                        this.ghosts.clear();
                        break;
                    default:
                        log.debug("gameTick={}, animationId={} (UNKNOWN)", this.gameTick, animationId);
                }

            }
        }
    }

    private void onNpcSpawned(NpcSpawned event) {
        NPC npc = event.getNpc();
        if (this.cerberus == null && npc != null && npc.getName() != null && npc.getName().toLowerCase().contains("cerberus")) {
            log.debug("onNpcSpawned name={}, id={}", npc.getName(), npc.getId());
            this.cerberus = new Cerberus(npc);
            this.gameTick = 0;
            this.tickTimestampIndex = 0;
            this.lastTick = System.currentTimeMillis();
            this.upcomingAttacks.clear();
            this.tickTimestamps.clear();
        }

        if (this.cerberus != null) {
            Ghost ghost = Ghost.fromNPC(npc);
            if (ghost != null) {
                this.ghosts.add(npc);
            }

        }
    }

    private void onNpcDespawned(NpcDespawned event) {
        NPC npc = event.getNpc();
        if (npc != null && npc.getName() != null && npc.getName().toLowerCase().contains("cerberus")) {
            this.cerberus = null;
            this.ghosts.clear();
            log.debug("onNpcDespawned name={}, id={}", npc.getName(), npc.getId());
        }

        if (this.cerberus == null && !this.ghosts.isEmpty()) {
            this.ghosts.clear();
        } else {
            this.ghosts.remove(event.getNpc());
        }
    }

    private void calculateUpcomingAttacks() {
        this.upcomingAttacks.clear();
        Attack lastCerberusAttack = this.cerberus.getLastAttack();
        if (lastCerberusAttack != null) {
            int lastCerberusAttackTick = this.cerberus.getLastAttackTick();
            int hp = this.cerberus.getHp();
            Phase expectedPhase = this.cerberus.getNextAttackPhase(1, hp);
            Phase lastPhase = this.cerberus.getLastAttackPhase();
            int tickDelay = 0;
            if (lastPhase != null) {
                tickDelay = lastPhase.getTickDelay();
            }

            for(int tick = this.gameTick + 1; tick <= this.gameTick + 10; ++tick) {
                if (this.ghosts.size() == 3) {
                    Ghost ghost;
                    if (this.cerberus.getLastGhostYellTick() == tick - 13) {
                        ghost = Ghost.fromNPC((NPC)this.ghosts.get(this.ghosts.size() - 3));
                    } else if (this.cerberus.getLastGhostYellTick() == tick - 15) {
                        ghost = Ghost.fromNPC((NPC)this.ghosts.get(this.ghosts.size() - 2));
                    } else if (this.cerberus.getLastGhostYellTick() == tick - 17) {
                        ghost = Ghost.fromNPC((NPC)this.ghosts.get(this.ghosts.size() - 1));
                    } else {
                        ghost = null;
                    }

                    if (ghost != null) {
                        switch(ghost.getType()) {
                            case ATTACK:
                                this.upcomingAttacks.add(new CerberusAttack(tick, Attack.GHOST_MELEE));
                                continue;
                            case RANGED:
                                this.upcomingAttacks.add(new CerberusAttack(tick, Attack.GHOST_RANGED));
                                continue;
                            case MAGIC:
                                this.upcomingAttacks.add(new CerberusAttack(tick, Attack.GHOST_MAGIC));
                            default:
                                continue;
                        }
                    }
                }

                if (expectedPhase == Phase.TRIPLE) {
                    if (this.cerberus.getLastTripleAttack() == Attack.MAGIC) {
                        if (lastCerberusAttackTick + 4 == tick) {
                            this.upcomingAttacks.add(new CerberusAttack(tick, Attack.RANGED));
                        } else if (lastCerberusAttackTick + 7 == tick) {
                            this.upcomingAttacks.add(new CerberusAttack(tick, Attack.MELEE));
                        }
                    } else if (this.cerberus.getLastTripleAttack() == Attack.RANGED) {
                        if (lastCerberusAttackTick + 4 == tick) {
                            this.upcomingAttacks.add(new CerberusAttack(tick, Attack.MELEE));
                        }
                    } else if (this.cerberus.getLastTripleAttack() == null) {
                        if (lastCerberusAttackTick + tickDelay + 2 == tick) {
                            this.upcomingAttacks.add(new CerberusAttack(tick, Attack.MAGIC));
                        } else if (lastCerberusAttackTick + tickDelay + 5 == tick) {
                            this.upcomingAttacks.add(new CerberusAttack(tick, Attack.RANGED));
                        }
                    }
                } else if (expectedPhase == Phase.AUTO && lastCerberusAttackTick + tickDelay + 1 == tick) {
                    if (this.prayer == Prayer.PROTECT_FROM_MAGIC) {
                        this.upcomingAttacks.add(new CerberusAttack(tick, Attack.MAGIC));
                    } else if (this.prayer == Prayer.PROTECT_FROM_MISSILES) {
                        this.upcomingAttacks.add(new CerberusAttack(tick, Attack.RANGED));
                    } else if (this.prayer == Prayer.PROTECT_FROM_MELEE) {
                        this.upcomingAttacks.add(new CerberusAttack(tick, Attack.MELEE));
                    }
                }
            }

        }
    }

    private void setAutoAttackPrayer() {
        int defenseStab = 0;
        int defenseMagic = 0;
        int defenseRange = 0;
        ItemContainer itemContainer = this.client.getItemContainer(InventoryID.EQUIPMENT);
        int magicDefenseTotal;
        int rangeDefenseTotal;
        if (itemContainer != null) {
            Item[] items = itemContainer.getItems();
            Item[] var6 = items;
            magicDefenseTotal = items.length;

            for(rangeDefenseTotal = 0; rangeDefenseTotal < magicDefenseTotal; ++rangeDefenseTotal) {
                Item item = var6[rangeDefenseTotal];
                if (item != null) {
                    ItemStats itemStats = this.itemManager.getItemStats(item.getId(), false);
                    if (itemStats != null) {
                        ItemEquipmentStats itemStatsEquipment = itemStats.getEquipment();
                        if (itemStatsEquipment != null) {
                            defenseStab += itemStatsEquipment.getDstab();
                            defenseMagic += itemStatsEquipment.getDmagic();
                            defenseRange += itemStatsEquipment.getDrange();
                        }
                    }
                }
            }
        }

        int magicLvl = this.client.getBoostedSkillLevel(Skill.MAGIC);
        int defenseLvl = this.client.getBoostedSkillLevel(Skill.DEFENCE);
        magicDefenseTotal = (int)((double)magicLvl * 0.7D + (double)defenseLvl * 0.3D) + defenseMagic;
        rangeDefenseTotal = defenseLvl + defenseRange;
        int meleeDefenseTotal = defenseLvl + defenseStab;
        Player player = this.client.getLocalPlayer();
        if (player != null) {
            WorldPoint worldPointPlayer = this.client.getLocalPlayer().getWorldLocation();
            WorldPoint worldPointCerberus = this.cerberus.getNpc().getWorldLocation();
            if (worldPointPlayer.getX() < worldPointCerberus.getX() - 1 || worldPointPlayer.getX() > worldPointCerberus.getX() + 5 || worldPointPlayer.getY() < worldPointCerberus.getY() - 1 || worldPointPlayer.getY() > worldPointCerberus.getY() + 5) {
                meleeDefenseTotal = 2147483647;
            }
        }

        if (magicDefenseTotal <= rangeDefenseTotal && magicDefenseTotal <= meleeDefenseTotal) {
            this.prayer = Prayer.PROTECT_FROM_MAGIC;
        } else if (rangeDefenseTotal <= meleeDefenseTotal) {
            this.prayer = Prayer.PROTECT_FROM_MISSILES;
        } else {
            this.prayer = Prayer.PROTECT_FROM_MELEE;
        }

    }

    private boolean inCerberusArena() {
        Player player = this.client.getLocalPlayer();
        if (player == null) {
            return false;
        } else {
            return Arena.getArena(player.getWorldLocation()) != null;
        }
    }

    private boolean inCerberusRegion() {
        int[] var1 = this.client.getMapRegions();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            int regionId = var1[var3];
            if (REGION_IDS.contains(regionId)) {
                return true;
            }
        }

        return false;
    }

    public List<NPC> getGhosts() {
        return this.ghosts;
    }

    public List<CerberusAttack> getUpcomingAttacks() {
        return this.upcomingAttacks;
    }

    @Nullable
    public Prayer getPrayer() {
        return this.prayer;
    }

    @Nullable
    public Cerberus getCerberus() {
        return this.cerberus;
    }

    public int getGameTick() {
        return this.gameTick;
    }

    public long getLastTick() {
        return this.lastTick;
    }
}
