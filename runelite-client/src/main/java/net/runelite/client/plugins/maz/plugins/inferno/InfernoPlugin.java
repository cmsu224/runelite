//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.inferno;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.NPCManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.maz.plugins.inferno.InfernoConfig.FontStyle;
import net.runelite.client.plugins.maz.plugins.inferno.InfernoNPC.Attack;
import net.runelite.client.plugins.maz.plugins.inferno.InfernoNPC.Type;
import net.runelite.client.plugins.maz.plugins.inferno.displaymodes.InfernoPrayerDisplayMode;
import net.runelite.client.plugins.maz.plugins.inferno.displaymodes.InfernoSafespotDisplayMode;
import net.runelite.client.plugins.maz.plugins.inferno.displaymodes.InfernoWaveDisplayMode;
import net.runelite.client.plugins.maz.plugins.inferno.displaymodes.InfernoZukShieldDisplayMode;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "[Maz] Inferno",
        enabledByDefault = false,
        description = "Inferno helper",
        tags = {"combat", "overlay", "pve", "pvm"}
)
public class InfernoPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(InfernoPlugin.class);
    private static final int INFERNO_REGION = 9043;
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private NPCManager npcManager;
    @Inject
    private InfernoOverlay infernoOverlay;
    @Inject
    private InfernoWaveOverlay waveOverlay;
    @Inject
    private InfernoInfoBoxOverlay jadOverlay;
    @Inject
    private InfernoOverlay prayerOverlay;
    @Inject
    private InfernoConfig config;
    private FontStyle fontStyle;
    private int textSize;
    private WorldPoint lastLocation;
    private int currentWaveNumber;
    private final List<InfernoNPC> infernoNpcs;
    private final Map<Integer, Map<Attack, Integer>> upcomingAttacks;
    private Attack closestAttack;
    private final List<WorldPoint> obstacles;
    private boolean finalPhase;
    private boolean finalPhaseTick;
    private int ticksSinceFinalPhase;
    private NPC zukShield;
    private NPC zuk;
    private WorldPoint zukShieldLastPosition;
    private WorldPoint zukShieldBase;
    private int zukShieldCornerTicks;
    private int zukShieldNegativeXCoord;
    private int zukShieldPositiveXCoord;
    private int zukShieldLastNonZeroDelta;
    private int zukShieldLastDelta;
    private int zukShieldTicksLeftInCorner;
    private InfernoNPC centralNibbler;
    private final Map<WorldPoint, Integer> safeSpotMap;
    private final Map<Integer, List<WorldPoint>> safeSpotAreas;
    private long lastTick;
    private InfernoSpawnTimerInfobox spawnTimerInfoBox;

    public InfernoPlugin() {
        this.fontStyle = FontStyle.BOLD;
        this.textSize = 32;
        this.lastLocation = new WorldPoint(0, 0, 0);
        this.infernoNpcs = new ArrayList();
        this.upcomingAttacks = new HashMap();
        this.closestAttack = null;
        this.obstacles = new ArrayList();
        this.finalPhase = false;
        this.finalPhaseTick = false;
        this.ticksSinceFinalPhase = 0;
        this.zukShield = null;
        this.zuk = null;
        this.zukShieldLastPosition = null;
        this.zukShieldBase = null;
        this.zukShieldCornerTicks = -2;
        this.zukShieldNegativeXCoord = -1;
        this.zukShieldPositiveXCoord = -1;
        this.zukShieldLastNonZeroDelta = 0;
        this.zukShieldLastDelta = 0;
        this.zukShieldTicksLeftInCorner = -1;
        this.centralNibbler = null;
        this.safeSpotMap = new HashMap();
        this.safeSpotAreas = new HashMap();
    }

    @Provides
    InfernoConfig provideConfig(ConfigManager configManager) {
        return (InfernoConfig)configManager.getConfig(InfernoConfig.class);
    }

    protected void startUp() {
        this.waveOverlay.setDisplayMode(this.config.waveDisplay());
        this.waveOverlay.setWaveHeaderColor(this.config.getWaveOverlayHeaderColor());
        this.waveOverlay.setWaveTextColor(this.config.getWaveTextColor());
        if (this.isInInferno()) {
            this.overlayManager.add(this.infernoOverlay);
            if (this.config.waveDisplay() != InfernoWaveDisplayMode.NONE) {
                this.overlayManager.add(this.waveOverlay);
            }

            this.overlayManager.add(this.jadOverlay);
            this.overlayManager.add(this.prayerOverlay);
        }

    }

    protected void shutDown() {
        this.overlayManager.remove(this.infernoOverlay);
        this.overlayManager.remove(this.waveOverlay);
        this.overlayManager.remove(this.jadOverlay);
        this.overlayManager.remove(this.prayerOverlay);
        this.infoBoxManager.removeInfoBox(this.spawnTimerInfoBox);
        this.currentWaveNumber = -1;
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if ("inferno".equals(event.getGroup())) {
            if (event.getKey().endsWith("color")) {
                this.waveOverlay.setWaveHeaderColor(this.config.getWaveOverlayHeaderColor());
                this.waveOverlay.setWaveTextColor(this.config.getWaveTextColor());
            } else if ("waveDisplay".equals(event.getKey())) {
                this.overlayManager.remove(this.waveOverlay);
                this.waveOverlay.setDisplayMode(this.config.waveDisplay());
                if (this.isInInferno() && this.config.waveDisplay() != InfernoWaveDisplayMode.NONE) {
                    this.overlayManager.add(this.waveOverlay);
                }
            }

            if (event.getKey().equals("mirrorMode") && this.isInInferno()) {
                this.overlayManager.remove(this.infernoOverlay);
                this.overlayManager.remove(this.jadOverlay);
                this.overlayManager.remove(this.prayerOverlay);
                this.overlayManager.add(this.infernoOverlay);
                this.overlayManager.add(this.jadOverlay);
                this.overlayManager.add(this.prayerOverlay);
                if (this.config.waveDisplay() != InfernoWaveDisplayMode.NONE) {
                    this.overlayManager.remove(this.waveOverlay);
                    this.overlayManager.add(this.waveOverlay);
                }
            }

        }
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (this.isInInferno()) {
            this.lastTick = System.currentTimeMillis();
            this.upcomingAttacks.clear();
            this.calculateUpcomingAttacks();
            this.closestAttack = null;
            this.calculateClosestAttack();
            this.safeSpotMap.clear();
            this.calculateSafespots();
            this.safeSpotAreas.clear();
            this.calculateSafespotAreas();
            this.obstacles.clear();
            this.calculateObstacles();
            this.centralNibbler = null;
            this.calculateCentralNibbler();
            this.calculateSpawnTimerInfobox();
            if (this.finalPhaseTick) {
                this.finalPhaseTick = false;
            } else if (this.finalPhase) {
                ++this.ticksSinceFinalPhase;
            }

        }
    }

    @Subscribe
    private void onNpcSpawned(NpcSpawned event) {
        if (this.isInInferno()) {
            int npcId = event.getNpc().getId();
            if (npcId == 7707) {
                this.zukShield = event.getNpc();
            } else {
                Type infernoNPCType = Type.typeFromId(npcId);
                if (infernoNPCType != null) {
                    switch(infernoNPCType) {
                        case BLOB:
                            this.infernoNpcs.add(new InfernoNPC(event.getNpc()));
                            return;
                        case MAGE:
                            if (this.zuk != null && this.spawnTimerInfoBox != null) {
                                this.spawnTimerInfoBox.reset();
                                this.spawnTimerInfoBox.run();
                            }
                            break;
                        case ZUK:
                            this.finalPhase = false;
                            this.zukShieldCornerTicks = -2;
                            this.zukShieldLastPosition = null;
                            this.zukShieldBase = null;
                            log.debug("[INFERNO] Zuk spawn detected, not in final phase");
                            if (this.config.spawnTimerInfobox()) {
                                this.zuk = event.getNpc();
                                if (this.spawnTimerInfoBox != null) {
                                    this.infoBoxManager.removeInfoBox(this.spawnTimerInfoBox);
                                }

                                this.spawnTimerInfoBox = new InfernoSpawnTimerInfobox(this.itemManager.getImage(22319), this);
                                this.infoBoxManager.addInfoBox(this.spawnTimerInfoBox);
                            }
                            break;
                        case HEALER_ZUK:
                            this.finalPhase = true;
                            this.ticksSinceFinalPhase = 1;
                            this.finalPhaseTick = true;
                            Iterator var4 = this.infernoNpcs.iterator();

                            while(var4.hasNext()) {
                                InfernoNPC infernoNPC = (InfernoNPC)var4.next();
                                if (infernoNPC.getType() == Type.ZUK) {
                                    infernoNPC.setTicksTillNextAttack(-1);
                                }
                            }

                            log.debug("[INFERNO] Final phase detected!");
                    }

                    this.infernoNpcs.add(0, new InfernoNPC(event.getNpc()));
                }
            }
        }
    }

    @Subscribe
    private void onNpcDespawned(NpcDespawned event) {
        if (this.isInInferno()) {
            int npcId = event.getNpc().getId();
            switch(npcId) {
                case 7706:
                    this.zuk = null;
                    if (this.spawnTimerInfoBox != null) {
                        this.infoBoxManager.removeInfoBox(this.spawnTimerInfoBox);
                    }

                    this.spawnTimerInfoBox = null;
                default:
                    this.infernoNpcs.removeIf((infernoNPC) -> {
                        return infernoNPC.getNpc() == event.getNpc();
                    });
                    return;
                case 7707:
                    this.zukShield = null;
            }
        }
    }

    @Subscribe
    private void onAnimationChanged(AnimationChanged event) {
        if (this.isInInferno()) {
            if (event.getActor() instanceof NPC) {
                NPC npc = (NPC)event.getActor();
                if (ArrayUtils.contains(Type.NIBBLER.getNpcIds(), npc.getId()) && npc.getAnimation() == 7576) {
                    this.infernoNpcs.removeIf((infernoNPC) -> {
                        return infernoNPC.getNpc() == npc;
                    });
                }
            }

        }
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            if (!this.isInInferno()) {
                this.infernoNpcs.clear();
                this.currentWaveNumber = -1;
                this.overlayManager.remove(this.infernoOverlay);
                this.overlayManager.remove(this.waveOverlay);
                this.overlayManager.remove(this.jadOverlay);
                this.overlayManager.remove(this.prayerOverlay);
                this.zukShield = null;
                this.zuk = null;
                if (this.spawnTimerInfoBox != null) {
                    this.infoBoxManager.removeInfoBox(this.spawnTimerInfoBox);
                }

                this.spawnTimerInfoBox = null;
            } else if (this.currentWaveNumber == -1) {
                this.infernoNpcs.clear();
                this.currentWaveNumber = 1;
                this.overlayManager.add(this.infernoOverlay);
                this.overlayManager.add(this.jadOverlay);
                this.overlayManager.add(this.prayerOverlay);
                if (this.config.waveDisplay() != InfernoWaveDisplayMode.NONE) {
                    this.overlayManager.add(this.waveOverlay);
                }
            }

        }
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        if (this.isInInferno() && event.getType() == ChatMessageType.GAMEMESSAGE) {
            String message = event.getMessage();
            if (event.getMessage().contains("Wave:")) {
                message = message.substring(message.indexOf(": ") + 2);
                this.currentWaveNumber = Integer.parseInt(message.substring(0, message.indexOf(60)));
            }

        }
    }

    private boolean isInInferno() {
        return ArrayUtils.contains(this.client.getMapRegions(), 9043);
    }

    int getNextWaveNumber() {
        return this.currentWaveNumber != -1 && this.currentWaveNumber != 69 ? this.currentWaveNumber + 1 : -1;
    }

    private void calculateUpcomingAttacks() {
        Iterator var1 = this.infernoNpcs.iterator();

        while(true) {
            while(true) {
                while(true) {
                    InfernoNPC infernoNPC;
                    do {
                        do {
                            do {
                                if (!var1.hasNext()) {
                                    return;
                                }

                                infernoNPC = (InfernoNPC)var1.next();
                                infernoNPC.gameTick(this.client, this.lastLocation, this.finalPhase, this.ticksSinceFinalPhase);
                                if (infernoNPC.getType() == Type.ZUK && this.zukShieldCornerTicks == -1) {
                                    infernoNPC.updateNextAttack(Attack.UNKNOWN, 12);
                                    this.zukShieldCornerTicks = 0;
                                }
                            } while(infernoNPC.getTicksTillNextAttack() <= 0);
                        } while(!this.isPrayerHelper(infernoNPC));
                    } while(infernoNPC.getNextAttack() == Attack.UNKNOWN && (!this.config.indicateBlobDetectionTick() || infernoNPC.getType() != Type.BLOB || infernoNPC.getTicksTillNextAttack() < 4));

                    this.upcomingAttacks.computeIfAbsent(infernoNPC.getTicksTillNextAttack(), (k) -> {
                        return new HashMap();
                    });
                    if (this.config.indicateBlobDetectionTick() && infernoNPC.getType() == Type.BLOB && infernoNPC.getTicksTillNextAttack() >= 4) {
                        this.upcomingAttacks.computeIfAbsent(infernoNPC.getTicksTillNextAttack() - 3, (k) -> {
                            return new HashMap();
                        });
                        this.upcomingAttacks.computeIfAbsent(infernoNPC.getTicksTillNextAttack() - 4, (k) -> {
                            return new HashMap();
                        });
                        if (((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).containsKey(Attack.MAGIC)) {
                            if ((Integer)((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).get(Attack.MAGIC) > Type.BLOB.getPriority()) {
                                ((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).put(Attack.MAGIC, Type.BLOB.getPriority());
                            }
                        } else if (((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).containsKey(Attack.RANGED)) {
                            if ((Integer)((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).get(Attack.RANGED) > Type.BLOB.getPriority()) {
                                ((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).put(Attack.RANGED, Type.BLOB.getPriority());
                            }
                        } else if (!((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack())).containsKey(Attack.MAGIC) && !((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 4)).containsKey(Attack.MAGIC)) {
                            if (!((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack())).containsKey(Attack.RANGED) && !((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 4)).containsKey(Attack.RANGED)) {
                                ((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).put(Attack.MAGIC, Type.BLOB.getPriority());
                            } else if (!((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).containsKey(Attack.MAGIC) || (Integer)((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).get(Attack.MAGIC) > Type.BLOB.getPriority()) {
                                ((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).put(Attack.MAGIC, Type.BLOB.getPriority());
                            }
                        } else if (!((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).containsKey(Attack.RANGED) || (Integer)((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).get(Attack.RANGED) > Type.BLOB.getPriority()) {
                            ((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack() - 3)).put(Attack.RANGED, Type.BLOB.getPriority());
                        }
                    } else {
                        Attack attack = infernoNPC.getNextAttack();
                        int priority = infernoNPC.getType().getPriority();
                        if (!((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack())).containsKey(attack) || (Integer)((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack())).get(attack) > priority) {
                            ((Map)this.upcomingAttacks.get(infernoNPC.getTicksTillNextAttack())).put(attack, priority);
                        }
                    }
                }
            }
        }
    }

    private void calculateClosestAttack() {
        if (this.config.prayerDisplayMode() == InfernoPrayerDisplayMode.PRAYER_TAB || this.config.prayerDisplayMode() == InfernoPrayerDisplayMode.BOTH) {
            int closestTick = 999;
            int closestPriority = 999;
            Iterator var3 = this.upcomingAttacks.keySet().iterator();

            label33:
            while(var3.hasNext()) {
                Integer tick = (Integer)var3.next();
                Map<Attack, Integer> attackPriority = (Map)this.upcomingAttacks.get(tick);
                Iterator var6 = attackPriority.keySet().iterator();

                while(true) {
                    Attack currentAttack;
                    int currentPriority;
                    do {
                        if (!var6.hasNext()) {
                            continue label33;
                        }

                        currentAttack = (Attack)var6.next();
                        currentPriority = (Integer)attackPriority.get(currentAttack);
                    } while(tick >= closestTick && (tick != closestTick || currentPriority >= closestPriority));

                    this.closestAttack = currentAttack;
                    closestPriority = currentPriority;
                    closestTick = tick;
                }
            }
        }

    }

    private void calculateSafespots() {
        int zukShieldDelta;
        if (this.currentWaveNumber < 69) {
            if (this.config.safespotDisplayMode() != InfernoSafespotDisplayMode.OFF) {
                int checkSize = (int)Math.floor((double)this.config.safespotsCheckSize() / 2.0D);

                for(zukShieldDelta = -checkSize; zukShieldDelta <= checkSize; ++zukShieldDelta) {
                    label218:
                    for(int y = -checkSize; y <= checkSize; ++y) {
                        WorldPoint checkLoc = this.client.getLocalPlayer().getWorldLocation().dx(zukShieldDelta).dy(y);
                        if (!this.obstacles.contains(checkLoc)) {
                            Iterator var5 = this.infernoNpcs.iterator();

                            while(true) {
                                InfernoNPC infernoNPC;
                                do {
                                    do {
                                        if (!var5.hasNext()) {
                                            continue label218;
                                        }

                                        infernoNPC = (InfernoNPC)var5.next();
                                    } while(!this.isNormalSafespots(infernoNPC));

                                    if (!this.safeSpotMap.containsKey(checkLoc)) {
                                        this.safeSpotMap.put(checkLoc, 0);
                                    }
                                } while(!infernoNPC.canAttack(this.client, checkLoc) && !infernoNPC.canMoveToAttack(this.client, checkLoc, this.obstacles));

                                if (infernoNPC.getType().getDefaultAttack() == Attack.MELEE) {
                                    if ((Integer)this.safeSpotMap.get(checkLoc) == 0) {
                                        this.safeSpotMap.put(checkLoc, 1);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 2) {
                                        this.safeSpotMap.put(checkLoc, 4);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 3) {
                                        this.safeSpotMap.put(checkLoc, 5);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 6) {
                                        this.safeSpotMap.put(checkLoc, 7);
                                    }
                                }

                                if (infernoNPC.getType().getDefaultAttack() == Attack.MAGIC || infernoNPC.getType() == Type.BLOB && (Integer)this.safeSpotMap.get(checkLoc) != 2 && (Integer)this.safeSpotMap.get(checkLoc) != 4) {
                                    if ((Integer)this.safeSpotMap.get(checkLoc) == 0) {
                                        this.safeSpotMap.put(checkLoc, 3);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 1) {
                                        this.safeSpotMap.put(checkLoc, 5);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 2) {
                                        this.safeSpotMap.put(checkLoc, 6);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 5) {
                                        this.safeSpotMap.put(checkLoc, 7);
                                    }
                                }

                                if (infernoNPC.getType().getDefaultAttack() == Attack.RANGED || infernoNPC.getType() == Type.BLOB && (Integer)this.safeSpotMap.get(checkLoc) != 3 && (Integer)this.safeSpotMap.get(checkLoc) != 5) {
                                    if ((Integer)this.safeSpotMap.get(checkLoc) == 0) {
                                        this.safeSpotMap.put(checkLoc, 2);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 1) {
                                        this.safeSpotMap.put(checkLoc, 4);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 3) {
                                        this.safeSpotMap.put(checkLoc, 6);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 4) {
                                        this.safeSpotMap.put(checkLoc, 7);
                                    }
                                }

                                if (infernoNPC.getType() == Type.JAD && infernoNPC.getNpc().getWorldArea().isInMeleeDistance(checkLoc)) {
                                    if ((Integer)this.safeSpotMap.get(checkLoc) == 0) {
                                        this.safeSpotMap.put(checkLoc, 1);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 2) {
                                        this.safeSpotMap.put(checkLoc, 4);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 3) {
                                        this.safeSpotMap.put(checkLoc, 5);
                                    } else if ((Integer)this.safeSpotMap.get(checkLoc) == 6) {
                                        this.safeSpotMap.put(checkLoc, 7);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (this.currentWaveNumber == 69 && this.zukShield != null) {
            WorldPoint zukShieldCurrentPosition = this.zukShield.getWorldLocation();
            if (this.zukShieldLastPosition != null && this.zukShieldLastPosition.getX() != zukShieldCurrentPosition.getX() && this.zukShieldCornerTicks == -2) {
                this.zukShieldBase = this.zukShieldLastPosition;
                this.zukShieldCornerTicks = -1;
            }

            if (this.zukShieldLastPosition != null) {
                zukShieldDelta = zukShieldCurrentPosition.getX() - this.zukShieldLastPosition.getX();
                if (zukShieldDelta != 0) {
                    this.zukShieldLastNonZeroDelta = zukShieldDelta;
                }

                if (this.zukShieldLastDelta == 0 && zukShieldDelta != 0) {
                    this.zukShieldTicksLeftInCorner = 4;
                }

                if (zukShieldDelta == 0) {
                    if (this.zukShieldLastNonZeroDelta > 0) {
                        this.zukShieldPositiveXCoord = zukShieldCurrentPosition.getX();
                    } else if (this.zukShieldLastNonZeroDelta < 0) {
                        this.zukShieldNegativeXCoord = zukShieldCurrentPosition.getX();
                    }

                    if (this.zukShieldTicksLeftInCorner > 0) {
                        --this.zukShieldTicksLeftInCorner;
                    }
                }

                this.zukShieldLastDelta = zukShieldDelta;
            }

            this.zukShieldLastPosition = zukShieldCurrentPosition;
            if (this.config.safespotDisplayMode() != InfernoSafespotDisplayMode.OFF) {
                if (this.finalPhase && this.config.safespotsZukShieldAfterHealers() == InfernoZukShieldDisplayMode.LIVE || !this.finalPhase && this.config.safespotsZukShieldBeforeHealers() == InfernoZukShieldDisplayMode.LIVE) {
                    this.drawZukSafespot(this.zukShield.getWorldLocation().getX(), this.zukShield.getWorldLocation().getY(), 0);
                }

                if (this.finalPhase && this.config.safespotsZukShieldAfterHealers() == InfernoZukShieldDisplayMode.LIVEPLUSPREDICT || !this.finalPhase && this.config.safespotsZukShieldBeforeHealers() == InfernoZukShieldDisplayMode.LIVEPLUSPREDICT) {
                    this.drawZukSafespot(this.zukShield.getWorldLocation().getX(), this.zukShield.getWorldLocation().getY(), 0);
                    this.drawZukPredictedSafespot();
                } else if (this.finalPhase && this.config.safespotsZukShieldAfterHealers() == InfernoZukShieldDisplayMode.PREDICT || !this.finalPhase && this.config.safespotsZukShieldBeforeHealers() == InfernoZukShieldDisplayMode.PREDICT) {
                    this.drawZukPredictedSafespot();
                }
            }
        }

    }

    private void drawZukPredictedSafespot() {
        WorldPoint zukShieldCurrentPosition = this.zukShield.getWorldLocation();
        if (this.zukShieldPositiveXCoord != -1 && this.zukShieldNegativeXCoord != -1) {
            int nextShieldXCoord = zukShieldCurrentPosition.getX();
            Iterator var3 = this.infernoNpcs.iterator();

            while(var3.hasNext()) {
                InfernoNPC infernoNPC = (InfernoNPC)var3.next();
                if (infernoNPC.getType() == Type.ZUK) {
                    int ticksTilZukAttack = this.finalPhase ? infernoNPC.getTicksTillNextAttack() : infernoNPC.getTicksTillNextAttack() - 1;
                    if (ticksTilZukAttack < 1) {
                        if (this.finalPhase) {
                            return;
                        }

                        ticksTilZukAttack = 10;
                    }

                    if (this.zukShieldLastNonZeroDelta > 0) {
                        nextShieldXCoord += ticksTilZukAttack;
                        if (nextShieldXCoord > this.zukShieldPositiveXCoord) {
                            nextShieldXCoord -= this.zukShieldTicksLeftInCorner;
                            if (nextShieldXCoord <= this.zukShieldPositiveXCoord) {
                                nextShieldXCoord = this.zukShieldPositiveXCoord;
                            } else {
                                nextShieldXCoord = this.zukShieldPositiveXCoord - nextShieldXCoord + this.zukShieldPositiveXCoord;
                            }
                        }
                    } else {
                        nextShieldXCoord -= ticksTilZukAttack;
                        if (nextShieldXCoord < this.zukShieldNegativeXCoord) {
                            nextShieldXCoord += this.zukShieldTicksLeftInCorner;
                            if (nextShieldXCoord >= this.zukShieldNegativeXCoord) {
                                nextShieldXCoord = this.zukShieldNegativeXCoord;
                            } else {
                                nextShieldXCoord = this.zukShieldNegativeXCoord - nextShieldXCoord + this.zukShieldNegativeXCoord;
                            }
                        }
                    }
                }
            }

            this.drawZukSafespot(nextShieldXCoord, this.zukShield.getWorldLocation().getY(), 2);
        }

    }

    private void drawZukSafespot(int xCoord, int yCoord, int colorSafeSpotId) {
        for(int x = xCoord - 1; x <= xCoord + 3; ++x) {
            for(int y = yCoord - 4; y <= yCoord - 2; ++y) {
                this.safeSpotMap.put(new WorldPoint(x, y, this.client.getPlane()), colorSafeSpotId);
            }
        }

    }

    private void calculateSafespotAreas() {
        WorldPoint worldPoint;
        if (this.config.safespotDisplayMode() == InfernoSafespotDisplayMode.AREA) {
            for(Iterator var1 = this.safeSpotMap.keySet().iterator(); var1.hasNext(); ((List)this.safeSpotAreas.get(this.safeSpotMap.get(worldPoint))).add(worldPoint)) {
                worldPoint = (WorldPoint)var1.next();
                if (!this.safeSpotAreas.containsKey(this.safeSpotMap.get(worldPoint))) {
                    this.safeSpotAreas.put((Integer)this.safeSpotMap.get(worldPoint), new ArrayList());
                }
            }
        }

        this.lastLocation = this.client.getLocalPlayer().getWorldLocation();
    }

    private void calculateObstacles() {
        Iterator var1 = this.client.getNpcs().iterator();

        while(var1.hasNext()) {
            NPC npc = (NPC)var1.next();
            this.obstacles.addAll(npc.getWorldArea().toWorldPointList());
        }

    }

    private void calculateCentralNibbler() {
        InfernoNPC bestNibbler = null;
        int bestAmountInArea = 0;
        int bestDistanceToPlayer = 999;
        Iterator var4 = this.infernoNpcs.iterator();

        while(true) {
            InfernoNPC infernoNPC;
            int amountInArea;
            int distanceToPlayer;
            do {
                do {
                    if (!var4.hasNext()) {
                        if (bestNibbler != null) {
                            this.centralNibbler = bestNibbler;
                        }

                        return;
                    }

                    infernoNPC = (InfernoNPC)var4.next();
                } while(infernoNPC.getType() != Type.NIBBLER);

                amountInArea = 0;
                distanceToPlayer = infernoNPC.getNpc().getWorldLocation().distanceTo(this.client.getLocalPlayer().getWorldLocation());
                Iterator var8 = this.infernoNpcs.iterator();

                while(var8.hasNext()) {
                    InfernoNPC checkNpc = (InfernoNPC)var8.next();
                    if (checkNpc.getType() == Type.NIBBLER && checkNpc.getNpc().getWorldArea().distanceTo(infernoNPC.getNpc().getWorldArea()) <= 1) {
                        ++amountInArea;
                    }
                }
            } while(amountInArea <= bestAmountInArea && (amountInArea != bestAmountInArea || distanceToPlayer >= bestDistanceToPlayer));

            bestNibbler = infernoNPC;
        }
    }

    private void calculateSpawnTimerInfobox() {
        if (this.zuk != null && !this.finalPhase && this.spawnTimerInfoBox != null) {
            boolean pauseHp = true;
            boolean resumeHp = true;
            int hp = calculateNpcHp(this.zuk.getHealthRatio(), this.zuk.getHealthScale(), this.npcManager.getHealth(this.zuk.getId()));
            if (hp > 0) {
                if (this.spawnTimerInfoBox.isRunning()) {
                    if (hp >= 480 && hp < 600) {
                        this.spawnTimerInfoBox.pause();
                    }
                } else if (hp < 480) {
                    this.spawnTimerInfoBox.run();
                }

            }
        }
    }

    private static int calculateNpcHp(int ratio, int health, int maxHp) {
        if (ratio >= 0 && health > 0 && maxHp != -1) {
            int exactHealth = 0;
            if (ratio > 0) {
                int minHealth = 1;
                int maxHealth;
                if (health > 1) {
                    if (ratio > 1) {
                        minHealth = (maxHp * (ratio - 1) + health - 2) / (health - 1);
                    }

                    maxHealth = (maxHp * ratio - 1) / (health - 1);
                    if (maxHealth > maxHp) {
                        maxHealth = maxHp;
                    }
                } else {
                    maxHealth = maxHp;
                }

                exactHealth = (minHealth + maxHealth + 1) / 2;
            }

            return exactHealth;
        } else {
            return -1;
        }
    }

    private boolean isPrayerHelper(InfernoNPC infernoNPC) {
        switch(infernoNPC.getType()) {
            case BLOB:
                return this.config.prayerBlob();
            case MAGE:
                return this.config.prayerMage();
            case ZUK:
            case HEALER_ZUK:
            default:
                return false;
            case BAT:
                return this.config.prayerBat();
            case MELEE:
                return this.config.prayerMeleer();
            case RANGER:
                return this.config.prayerRanger();
            case HEALER_JAD:
                return this.config.prayerHealerJad();
            case JAD:
                return this.config.prayerJad();
        }
    }

    boolean isTicksOnNpc(InfernoNPC infernoNPC) {
        switch(infernoNPC.getType()) {
            case BLOB:
                return this.config.ticksOnNpcBlob();
            case MAGE:
                return this.config.ticksOnNpcMage();
            case ZUK:
                return this.config.ticksOnNpcZuk();
            case HEALER_ZUK:
            default:
                return false;
            case BAT:
                return this.config.ticksOnNpcBat();
            case MELEE:
                return this.config.ticksOnNpcMeleer();
            case RANGER:
                return this.config.ticksOnNpcRanger();
            case HEALER_JAD:
                return this.config.ticksOnNpcHealerJad();
            case JAD:
                return this.config.ticksOnNpcJad();
        }
    }

    boolean isNormalSafespots(InfernoNPC infernoNPC) {
        switch(infernoNPC.getType()) {
            case BLOB:
                return this.config.safespotsBlob();
            case MAGE:
                return this.config.safespotsMage();
            case ZUK:
            case HEALER_ZUK:
            default:
                return false;
            case BAT:
                return this.config.safespotsBat();
            case MELEE:
                return this.config.safespotsMeleer();
            case RANGER:
                return this.config.safespotsRanger();
            case HEALER_JAD:
                return this.config.safespotsHealerJad();
            case JAD:
                return this.config.safespotsJad();
        }
    }

    boolean isIndicateNpcPosition(InfernoNPC infernoNPC) {
        switch(infernoNPC.getType()) {
            case BLOB:
                return this.config.indicateNpcPositionBlob();
            case MAGE:
                return this.config.indicateNpcPositionMage();
            case ZUK:
            case HEALER_ZUK:
            default:
                return false;
            case BAT:
                return this.config.indicateNpcPositionBat();
            case MELEE:
                return this.config.indicateNpcPositionMeleer();
            case RANGER:
                return this.config.indicateNpcPositionRanger();
        }
    }

    FontStyle getFontStyle() {
        return this.fontStyle;
    }

    int getTextSize() {
        return this.textSize;
    }

    int getCurrentWaveNumber() {
        return this.currentWaveNumber;
    }

    List<InfernoNPC> getInfernoNpcs() {
        return this.infernoNpcs;
    }

    Map<Integer, Map<Attack, Integer>> getUpcomingAttacks() {
        return this.upcomingAttacks;
    }

    Attack getClosestAttack() {
        return this.closestAttack;
    }

    List<WorldPoint> getObstacles() {
        return this.obstacles;
    }

    boolean isFinalPhase() {
        return this.finalPhase;
    }

    NPC getZukShield() {
        return this.zukShield;
    }

    InfernoNPC getCentralNibbler() {
        return this.centralNibbler;
    }

    Map<WorldPoint, Integer> getSafeSpotMap() {
        return this.safeSpotMap;
    }

    Map<Integer, List<WorldPoint>> getSafeSpotAreas() {
        return this.safeSpotAreas;
    }

    long getLastTick() {
        return this.lastTick;
    }
}
