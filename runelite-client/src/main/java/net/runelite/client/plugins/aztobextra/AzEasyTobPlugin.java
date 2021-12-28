//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.GraphicsObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Hitsplat;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.Skill;
import net.runelite.api.Tile;
import net.runelite.api.VarPlayer;
import net.runelite.api.Hitsplat.HitsplatType;
import net.runelite.api.coords.Angle;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.aztobextra.config.PillarHide;
import net.runelite.client.plugins.aztobextra.meta.DiscordWebhook;
import net.runelite.client.plugins.aztobextra.meta.MaidenPhase;
import net.runelite.client.plugins.aztobextra.meta.MatomenosDetails;
import net.runelite.client.plugins.aztobextra.meta.MemorizedTornado;
import net.runelite.client.plugins.aztobextra.meta.PurpleAttackInfobox;
import net.runelite.client.plugins.aztobextra.meta.stomp.BloatDown;
import net.runelite.client.plugins.aztobextra.meta.stomp.def.BloatChunk;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.Counter;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageCapture;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "[AZ] Theatre Extras",
        description = "Extra tob stuff.",
        tags = {"azo"},
        enabledByDefault = false
)
public class AzEasyTobPlugin extends Plugin {
    private static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");
    public int partySize = 0;
    public boolean spectating;
    private static final Logger log = LoggerFactory.getLogger(AzEasyTobPlugin.class);
    private static final Point swMazeSquareOverWorld = new Point(9, 22);
    private static final Point swMazeSquareUnderWorld = new Point(42, 31);
    public int verzikTicksUntilAttack = -1;
    private int verzikAttackCount;
    @Inject
    private Client client;
    @Inject
    private AzEasyTobConfig config;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private AzEasyTobOverlay overlay;
    @Inject
    private MaidenNyloWheelchairOverlay overlay2;
    @Inject
    private SituationalTickOverlay overlay3;
    @Inject
    private EventBus eventBus;
    private HashMap<String, Long> warnCooldown = new HashMap();
    private boolean inTheatre = false;
    private int bombAttacksRemaining = 10;
    private int lightningAttacksRemaining = 4;
    private int ticksSincePurple = 1000;
    private NPC soteNPC;
    private NPC verzNPC;
    private NPC bloatNPC;
    private NPC maidenNPC;
    private NPC xarpNPC;
    private int exhumesLeft;
    private int xarpInitiateTickdown;
    private Map<Projectile, WorldPoint> soteyProjectiles = new HashMap();
    private Map<Projectile, Integer> soteyProjectileFilter = new HashMap();
    private final Map<GroundObject, Integer> xarpusExhumeds = new HashMap();
    private boolean wasInUnderWorld = false;
    private int overWorldRegionID = -1;
    private LinkedHashSet<Point> redTiles = new LinkedHashSet();
    private final List<Projectile> verzikRangedAttacks = new ArrayList();
    private boolean verzikRedPhase = false;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private SkillIconManager iconManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private ImageCapture imageCapture;
    @Inject
    private DrawManager drawManager;
    @Inject
    private ScheduledExecutorService executor;
    private PurpleAttackInfobox purplecounter;
    private Counter p3healCounter;
    private ArrayList<WorldPoint> yellows = new ArrayList();
    public ArrayList<ArrayList<WorldPoint>> yellowGroups = new ArrayList();
    public ArrayList<WorldPoint> yellowsList;
    public boolean yellowsOut;
    public int yellowTimer;
    public int hmYellowSpotNum;
    private ArrayList<String> partyMembersNames = new ArrayList();
    public static BufferedImage PURP_TICK_COUNT_ICON;
    public static BufferedImage HEAL_ICON;
    public static BufferedImage MAGE_HAT;
    public static BufferedImage RANGE_HAT;
    public static BufferedImage DEATH_RUNE;
    public static BufferedImage NUKE;
    public static BufferedImage GNUKE;
    private final Map<Integer, MemorizedTornado> memorizedTornados = new HashMap();
    private WorldPoint last0PlayerLocation;
    private WorldPoint last1PlayerLocation;
    private boolean enraged;
    private List<NPC> verzikTornados = new ArrayList();
    private HashMap<NPC, WorldPoint> verzikTornadoLocations = new HashMap();
    private HashMap<NPC, WorldPoint> verzikTornadoTrailingLocations = new HashMap();
    private final Map<LocalPoint, Integer> yellowPools = new HashMap();
    private HashMap<Player, Integer> attAnimTicks = new HashMap();
    private final HashMap<NPC, Integer> aliveTimer = new HashMap();
    private int bloatState = 0;
    private int bloatDownCount = 0;
    private int bloatUpTimer = 0;
    private int bloatVar = 0;
    private BloatDown bloatDown = null;
    private long bloatSecondsTimer;
    private int attsAfterPurpleSpawn = 0;
    private boolean scuffedPurpCheck;
    private long thrallUp = 0L;
    private long thrallUpClientTick;
    private int weaponId;
    private int lastNyloBossId;
    private NPC nyloBossNPC;
    private String nyloBossStyle = "";
    private int nyloBossChangeTick;
    @Inject
    private KeyManager keyManager;
    private boolean hotKeyPressed = false;
    private boolean maidenNyloWheelchairState = true;
    private boolean n1Orn2Alive;
    public GameObject rewardChest;
    private int instanceTimer;
    private boolean nextInstance;
    private boolean isInstanceTimerRunning = false;
    /*
    private final HotkeyListener hotkeyListener = new 1(this, () -> {
        return this.config.hotkey();
    });*/
    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.hotkey())
    {
        @Override
        public void hotkeyPressed()
        {
            return;
        }
    };

    @Inject
    private SpriteManager spriteManager;
    private long verzYellowHidden;
    private int verzikLastAnimation;
    private boolean hiddenPills = false;
    private long rollOverCheck;
    private List<Renderable> hidden = new ArrayList();
    private static final String RESURRECT_THRALL_MESSAGE_START = ">You resurrect a ";
    private static final String RESURRECT_THRALL_MESSAGE_END = " thrall.</col>";
    private transient long instanceTimerStartedMessage;
    private long flagUnpot;
    private net.runelite.client.plugins.aztobextra.DeferredCheck deferredCheck;
    private boolean verzikTickPaused = true;
    private int purpleSafetyCheck;
    private boolean initPurpleBox;
    protected int lastVarp6447 = 0;
    private final Map<Integer, MatomenosDetails> maidenMatos = new HashMap();
    private final Map<Integer, MatomenosDetails> verzMatos = new HashMap();
    public static final Map<Integer, Integer> GRAPHICS_MAP = ImmutableMap.of(181, 8, 180, 16, 179, 24, 369, 33, 367, 25);
    private MaidenPhase maidenPhase;

    @Provides
    AzEasyTobConfig provideConfig(ConfigManager configManager) {
        return (AzEasyTobConfig)configManager.getConfig(AzEasyTobConfig.class);
    }

    public AzEasyTobPlugin() {
    }

    public BufferedImage thrallImg() {
        return this.spriteManager.getSprite(2981, 0);
    }

    public void checkIcon() {
        //PURP_TICK_COUNT_ICON = ImageUtil.resizeCanvas(ImageUtil.loadImageResource(AzEasyTobPlugin.class, "png_nylo_purple.png"), 26, 26);
        //HEAL_ICON = ImageUtil.resizeCanvas(ImageUtil.loadImageResource(AzEasyTobPlugin.class, "png_heal.png"), 26, 26);
        PURP_TICK_COUNT_ICON =  this.itemManager.getImage(560);
        HEAL_ICON =  this.itemManager.getImage(560);
        int hatSize = 30;
        MAGE_HAT = ImageUtil.resizeCanvas(this.iconManager.getSkillImage(Skill.MAGIC, true), hatSize, hatSize);
        RANGE_HAT = ImageUtil.resizeCanvas(this.iconManager.getSkillImage(Skill.RANGED, true), hatSize, hatSize);
        DEATH_RUNE = this.itemManager.getImage(560);
        //NUKE = ImageUtil.loadImageResource(AzEasyTobPlugin.class, "png_nuke.png");
        //GNUKE = ImageUtil.loadImageResource(AzEasyTobPlugin.class, "png_greennuke.png");
        NUKE = this.itemManager.getImage(560);
        GNUKE = this.itemManager.getImage(560);
    }

    private int getSote66hp(int scale) {
        return this.getSoteTotalhp(scale) * 0;
    }

    private int getSote33hp(int scale) {
        return this.getSoteTotalhp(scale) * 0;
    }

    private int getSoteTotalhp(int scale) {
        if (scale == 5) {
            return 4000;
        } else {
            return scale == 4 ? 3500 : 3000;
        }
    }

    private boolean inRegion(int... regions) {
        if (this.client.getMapRegions() != null) {
            int[] var2 = this.client.getMapRegions();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                int i = var2[var4];
                int[] var6 = regions;
                int var7 = regions.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    int j = var6[var8];
                    if (i == j) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean enforceRegion() {
        return this.inRegion(12613, 13125, 13123, 12612, 12611, 13122);
    }

    protected void startUp() throws Exception {
        this.verzYellowHidden = 0L;
        this.yellowsOut = false;
        this.yellowTimer = 14;
        this.hmYellowSpotNum = 1;
        this.yellowGroups = new ArrayList();
        this.yellows = new ArrayList();
        this.yellowsList = new ArrayList();
        this.rewardChest = null;
        this.verzikLastAnimation = -1;
        this.overlayManager.add(this.overlay);
        this.overlayManager.add(this.overlay2);
        this.overlayManager.add(this.overlay3);
        this.keyManager.registerKeyListener(this.hotkeyListener);
        this.checkIcon();
        this.deferredCheck = null;
    }

    protected void shutDown() throws Exception {
        this.verzYellowHidden = 0L;
        this.rewardChest = null;
        this.overlayManager.remove(this.overlay);
        this.overlayManager.remove(this.overlay2);
        this.overlayManager.remove(this.overlay3);
        this.keyManager.unregisterKeyListener(this.hotkeyListener);
        this.attAnimTicks = new HashMap();
        this.maidenNyloWheelchairState = true;
        this.partySize = 0;
        this.n1Orn2Alive = false;
        this.spectating = false;
        this.deferredCheck = null;
        this.resetXarp();
        this.resetMaiden();
        this.resetSote();
        this.resetVerz();
        this.resetBloat();
        this.resetNyloBoss();
        if (this.purplecounter != null) {
            this.infoBoxManager.removeInfoBox(this.purplecounter);
            this.purplecounter = null;
        }

        if (this.p3healCounter != null) {
            this.infoBoxManager.removeInfoBox(this.p3healCounter);
            this.p3healCounter = null;
        }

    }

    private void resetNyloBoss() {
        this.nyloBossNPC = null;
        this.weaponId = 0;
        this.nyloBossStyle = "";
        this.nyloBossChangeTick = 0;
        this.lastNyloBossId = 0;
    }

    private void resetMaiden() {
        this.maidenPhase = MaidenPhase.OTHER;
        this.maidenNPC = null;
        this.maidenMatos.clear();
    }

    private void resetBloat() {
        this.bloatDownCount = 0;
        this.bloatState = 0;
        this.bloatUpTimer = 0;
        this.bloatNPC = null;
        this.bloatDown = null;
        this.bloatSecondsTimer = 0L;
    }

    private void resetSote() {
        this.isInstanceTimerRunning = false;
        this.bombAttacksRemaining = 10;
        this.soteNPC = null;
    }

    private void resetXarp() {
        this.xarpusExhumeds.clear();
        this.xarpNPC = null;
        this.exhumesLeft = -1;
        this.xarpInitiateTickdown = -1;
    }

    private void resetVerz() {
        this.yellowsList.clear();
        this.yellowGroups.clear();
        this.yellowsOut = false;
        this.yellowTimer = 14;
        this.hmYellowSpotNum = 1;
        this.verzYellowHidden = 0L;
        this.verzMatos.clear();
        this.initPurpleBox = false;
        this.scuffedPurpCheck = false;
        this.attsAfterPurpleSpawn = 0;
        this.verzikLastAnimation = -1;
        this.verzikTickPaused = true;
        this.hiddenPills = false;
        this.yellowPools.clear();
        this.aliveTimer.clear();
        this.verzikTornadoLocations.clear();
        this.verzikTornadoTrailingLocations.clear();
        this.verzikTornados.clear();
        this.enraged = false;
        this.lightningAttacksRemaining = 4;
        this.verzNPC = null;
        this.verzikRangedAttacks.clear();
        this.verzikRedPhase = false;
        this.ticksSincePurple = 1000;
        this.last0PlayerLocation = null;
        this.last1PlayerLocation = null;
        this.memorizedTornados.clear();
        if (this.purplecounter != null) {
            this.infoBoxManager.removeInfoBox(this.purplecounter);
            this.purplecounter = null;
        }

        if (this.p3healCounter != null) {
            this.infoBoxManager.removeInfoBox(this.p3healCounter);
            this.p3healCounter = null;
        }

    }

    Color getBloatStateColor() {
        Color col = Color.decode("#A466C3");
        switch(this.bloatState) {
            case 2:
                col = Color.decode("#3DB646");
                break;
            case 3:
                col = Color.decode("#DDDA31");
        }

        return col;
    }

    public boolean rollOver() {
        return System.currentTimeMillis() <= this.rollOverCheck;
    }

    private void addRollOver() {
        if (!this.rollOver()) {
            this.rollOverCheck = System.currentTimeMillis() + 300L;
        }
    }

    @Subscribe
    public void onFocusChanged(FocusChanged event) {
        if (!event.isFocused()) {
            this.hotKeyPressed = false;
        }

    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (!this.isInNyloRegion()) {
            this.weaponId = 0;
        } else {
            if (!event.getMenuOption().equalsIgnoreCase("hold") && !event.getMenuOption().equalsIgnoreCase("equip") && !event.getMenuOption().equalsIgnoreCase("wield")) {
                if (this.config.easyNylo() && !this.nyloBossStyle.equals("") && event.getMenuTarget().toLowerCase().contains("nylocas vasilias") && event.getMenuOption().equalsIgnoreCase("attack") && this.nyloBossNPC != null && (Maintainance.rangeWeaponId.contains(this.weaponId) && (this.nyloBossStyle.equals("mage") || this.nyloBossStyle.equals("melee")) || Maintainance.meleeWeaponId.contains(this.weaponId) && (this.nyloBossStyle.equals("mage") || this.nyloBossStyle.equals("range")) || Maintainance.magicWeaponId.contains(this.weaponId) && (this.nyloBossStyle.equals("range") || this.nyloBossStyle.equals("melee")))) {
                    event.consume();
                }
            } else if (Maintainance.rangeWeaponId.contains(event.getId()) || Maintainance.magicWeaponId.contains(event.getId()) || Maintainance.meleeWeaponId.contains(event.getId())) {
                this.weaponId = event.getId();
            }

        }
    }

    @Subscribe
    public void onClientTick(ClientTick event) {
        int alive;
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            int entId;
            int type;
            if (this.inTheatre) {
                List<MenuEntry> restEntries = new ArrayList();
                MenuEntry[] var3 = this.client.getMenuEntries();
                alive = var3.length;
                int var5 = 0;

                while(true) {
                    if (var5 >= alive) {
                        this.client.setMenuEntries((MenuEntry[])restEntries.toArray(new MenuEntry[0]));
                        break;
                    }

                    MenuEntry ent = var3[var5];
                    String entTarget = ent.getTarget().toLowerCase();
                    String entOption = ent.getOption();
                    entId = ent.getType().getId();
                    type = ent.getType().getId();
                    //type = ent.getIdentifier();
                    boolean add = true;
                    if ((type == MenuAction.SPELL_CAST_ON_NPC.getId() || type == MenuAction.EXAMINE_NPC.getId() || type == MenuAction.NPC_FIRST_OPTION.getId() || type == MenuAction.NPC_SECOND_OPTION.getId() || type == MenuAction.NPC_THIRD_OPTION.getId() || type == MenuAction.NPC_FOURTH_OPTION.getId() || type == MenuAction.NPC_FIFTH_OPTION.getId() || type == MenuAction.ITEM_USE_ON_NPC.getId()) && entId < this.client.getCachedNPCs().length) {
                        NPC entNpc = this.client.getCachedNPCs()[entId];
                        if (entNpc != null && (entNpc.isDead() || this.hidden.contains(entNpc))) {
                            add = false;
                        }
                    }

                    if (add) {
                        restEntries.add(ent);
                    }

                    ++var5;
                }
            }

            boolean nyloWaves = this.inRegion(13122, 12613);
            String target;
            ArrayList smallNyloEntries;
            ArrayList smallNyloEntries3;
            List newEntries;
            String entTarget;
            if ((this.config.easyNylo() || this.config.crispNylo()) && nyloWaves) {
                //smallNyloEntries = new ArrayList();
                smallNyloEntries = new ArrayList();
                smallNyloEntries3 = new ArrayList();
                List<MenuEntry> restEntries = new ArrayList();
                HashMap<MenuEntry, Integer> comparatorEntry = new HashMap();
                MenuEntry[] var41 = this.client.getMenuEntries();
                entId = var41.length;
                type = 0;

                while(true) {
                    if (type >= entId) {
                        if (this.config.easyNylo()) {
                            smallNyloEntries.clear();
                            Stream<Entry<MenuEntry, Integer>> sorted = comparatorEntry.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByValue()));
                            List<MenuEntry> finalSmallNyloEntries = smallNyloEntries;
                            sorted.forEach((e) -> {
                                finalSmallNyloEntries.add((MenuEntry)e.getKey());
                            });
                        }

                        newEntries = (List)Stream.concat(smallNyloEntries3.stream(), smallNyloEntries.stream()).collect(Collectors.toList());
                        List<MenuEntry> listB = (List)Stream.concat(newEntries.stream(), smallNyloEntries.stream()).collect(Collectors.toList());
                        newEntries = (List)Stream.concat(restEntries.stream(), listB.stream()).collect(Collectors.toList());
                        this.client.setMenuEntries((MenuEntry[])newEntries.toArray(new MenuEntry[0]));
                        break;
                    }

                    MenuEntry ent = var41[type];
                    entTarget = ent.getTarget().toLowerCase();
                    String entOption = ent.getOption();
                    if (entOption.toLowerCase().contains("attack") && (entTarget.toLowerCase().contains("nylocas hag") || entTarget.toLowerCase().contains("nylocas toxo") || entTarget.toLowerCase().contains("nylocas isch") || entTarget.toLowerCase().contains("nylocas prin")) || entTarget.contains("nylocas hag") && entTarget.contains("->") && entTarget.contains("barrage")) {
                        boolean isSmall;
                        //int entId;
                        NPC entNpc;
                        boolean darker;
                        if (this.config.crispNylo()) {
                            isSmall = false;
                            entId = ent.getType().getId();
                            entNpc = this.client.getCachedNPCs()[entId];
                            if (entNpc != null) {
                                isSmall = entNpc.getComposition().getSize() == 1;
                            }

                            target = Text.removeTags(ent.getTarget());
                            darker = !isSmall;
                            Color color = null;
                            if (target.contains("Nylocas Ischyros")) {
                                target = "Nylocas Ischyros";
                                color = darker ? new Color(190, 150, 150) : new Color(255, 188, 188);
                            } else if (target.contains("Nylocas Toxobolos")) {
                                target = "Nylocas Toxobolos";
                                color = darker ? new Color(0, 190, 0) : new Color(0, 255, 0);
                            } else if (target.contains("Nylocas Hagios")) {
                                target = "Nylocas Hagios";
                                color = darker ? new Color(0, 190, 190) : new Color(0, 255, 255);
                            } else if (target.contains("Nylocas Prinkipas")) {
                                target = "Nylocas Prinkipas";
                                color = JagexColors.CHAT_FC_TEXT_OPAQUE_BACKGROUND;
                            }

                            if (color != null) {
                                if (entTarget.contains("barrage")) {
                                    String[] split = ent.getTarget().split("->");
                                    String nextTarget = split[0] + "->" + ColorUtil.prependColorTag(target, color);
                                    ent.setTarget(nextTarget);
                                } else {
                                    ent.setTarget(ColorUtil.prependColorTag(target, color));
                                }
                            }
                        }

                        if (!this.config.easyNylo()) {
                            restEntries.add(ent);
                        } else {
                            isSmall = false;
                            entId = ent.getType().getId();
                            entNpc = this.client.getCachedNPCs()[entId];
                            boolean hide = this.shouldHideNyloEntry(ent, entNpc);
                            if (entNpc != null) {
                                isSmall = entNpc.getComposition().getSize() == 1;
                                darker = entNpc.getName() != null && entNpc.getName().toLowerCase().contains("prinkipas");
                                if (!isSmall && !darker) {
                                    if (entTarget.contains("barrage")) {
                                        if (!hide) {
                                            smallNyloEntries3.add(ent);
                                        }
                                    } else if (!hide) {
                                        restEntries.add(ent);
                                    }
                                } else {
                                    Integer alive2 = darker ? 1 : (Integer)this.aliveTimer.get(entNpc);
                                    if (alive2 != null) {
                                        if (!hide) {
                                            comparatorEntry.put(ent, alive2);
                                        }
                                    } else {
                                        this.aliveTimer.put(entNpc, 0);
                                        if (!hide) {
                                            comparatorEntry.put(ent, 0);
                                        }
                                    }
                                }
                            } else {
                                restEntries.add(ent);
                            }
                        }
                    } else if (!entOption.equals("Examine")) {
                        restEntries.add(ent);
                    }

                    ++type;
                }
            }

            boolean maidenRoom = WorldPoint.fromLocalInstance(this.client, this.client.getLocalPlayer().getLocalLocation()).getRegionID() == 12613;
            if (this.config.maidenRedsMenuWC() && maidenRoom) {
                smallNyloEntries = new ArrayList();
                smallNyloEntries3 = new ArrayList();
                HashMap<MenuEntry, Double> comparatorEntry = new HashMap();
                MenuEntry[] var39 = this.client.getMenuEntries();
                int var46 = var39.length;

                for(entId = 0; entId < var46; ++entId) {
                    MenuEntry ent = var39[entId];
                    entTarget = ent.getTarget().toLowerCase();
                    entTarget = ent.getOption();
                    if ((!entTarget.toLowerCase().contains("attack") || !entTarget.toLowerCase().contains("nylocas mat")) && (!entTarget.contains("nylocas mat") || !entTarget.contains("->") || !entTarget.contains("barrage"))) {
                        smallNyloEntries3.add(ent);
                    } else {
                        entId = ent.getType().getId();
                        NPC entNpc = this.client.getCachedNPCs()[entId];
                        if (entNpc != null) {
                            MatomenosDetails dtx = (MatomenosDetails)this.maidenMatos.get(entId);
                            if (dtx != null && dtx.getMatomenosNpc() == entNpc) {
                                float hitpoints = dtx.calculateHitpoints();
                                target = dtx.formatHitpoints(hitpoints, this.config.crab1deci());
                                comparatorEntry.put(ent, Double.valueOf(target));
                                if (entTarget.contains("barrage")) {
                                    String[] split = ent.getTarget().split("->");
                                    String nextTarget = split[0] + "->" + ColorUtil.prependColorTag(" Nylocas Matomenos (" + target + ")", dtx.calculateHitpointsColor(hitpoints));
                                    ent.setTarget(nextTarget);
                                } else {
                                    ent.setTarget(ColorUtil.prependColorTag("Nylocas Matomenos (" + target + ")", dtx.calculateHitpointsColor(hitpoints)));
                                }
                            } else {
                                smallNyloEntries3.add(ent);
                            }
                        } else {
                            smallNyloEntries3.add(ent);
                        }
                    }
                }

                Stream<Entry<MenuEntry, Double>> sorted = comparatorEntry.entrySet().stream().sorted(Entry.comparingByValue());
                ArrayList finalSmallNyloEntries1 = smallNyloEntries;
                sorted.forEach((e) -> {
                    finalSmallNyloEntries1.add((MenuEntry)e.getKey());
                });
                newEntries = (List)Stream.concat(smallNyloEntries3.stream(), smallNyloEntries.stream()).collect(Collectors.toList());
                this.client.setMenuEntries((MenuEntry[])newEntries.toArray(new MenuEntry[0]));
            }
        }

        Iterator var23;
        if (this.client.getGameState() == GameState.LOGGED_IN && !this.aliveTimer.isEmpty() && (this.hotKeyPressed || this.rollOver())) {
            this.addRollOver();
            var23 = this.aliveTimer.keySet().iterator();

            while(var23.hasNext()) {
                NPC npc = (NPC)var23.next();
                alive = (Integer)this.aliveTimer.get(npc);
                if (alive > this.config.thresholdHotkey() && !this.hidden.contains(npc)) {
                    this.hidden.add(npc);
                    setHidden(npc, true);
                }
            }
        }

        if (this.client.getGameState() == GameState.LOGGED_IN && !this.hidden.isEmpty() && !this.hotKeyPressed && !this.rollOver()) {
            var23 = this.hidden.iterator();

            while(var23.hasNext()) {
                Renderable r = (Renderable)var23.next();
                if (r instanceof NPC) {
                    NPC npc = (NPC)r;
                    if (npc.getName() != null && npc.getName().toLowerCase().contains("nylo")) {
                        setHidden(npc, false);
                        this.hidden.remove(npc);
                    }
                }
            }
        }

        if (this.client.getLocalPlayer() != null) {
            List<Player> players = this.client.getPlayers();
            Iterator var3 = players.iterator();

            while(true) {
                Point point;
                do {
                    do {
                        do {
                            LocalPoint lp;
                            LocalPoint lp1;
                            do {
                                Player player;
                                do {
                                    if (!var3.hasNext()) {
                                        return;
                                    }

                                    player = (Player)var3.next();
                                } while(player.getWorldLocation() == null);

                                lp = player.getLocalLocation();
                                WorldPoint wp = WorldPoint.fromRegion(player.getWorldLocation().getRegionID(), 31, 54, 0);
                                lp1 = LocalPoint.fromWorld(this.client, wp.getX(), wp.getY());
                            } while(lp1 == null);

                            Point base = new Point(lp1.getSceneX(), lp1.getSceneY());
                            point = new Point(lp.getSceneX() - base.getX(), lp.getSceneY() - base.getY());
                        } while(!this.isInNyloRegion());
                    } while(point.getX() != 1);
                } while(point.getY() != -1 && point.getY() != -2 && point.getY() != -3 && point.getY() != -4);

                if (this.nextInstance) {
                    if (this.config.soteStepCountdown()) {
                        this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Sotetseg instance timer started", "");
                    }

                    this.instanceTimer = 2;
                    this.isInstanceTimerRunning = true;
                    this.nextInstance = false;
                }
            }
        }
    }

    private boolean shouldHideNyloEntry(MenuEntry entry, NPC npc) {
        if (this.weaponId == 0) {
            this.weaponId = this.client.getLocalPlayer().getPlayerComposition().getEquipmentId(KitType.WEAPON);
        }

        if (npc == null) {
            return false;
        } else {
            boolean hide = false;
            if ((!Maintainance.ignoreChins || Maintainance.ignoreChins && this.weaponId != 10034 && this.weaponId != 11959) && entry.getType().getId() >= 7 && entry.getType().getId() <= 13 && entry.getType().getId() != 8 && npc != null && npc.getName() != null) {
                String name = npc.getName().toLowerCase();
                if (Maintainance.rangeWeaponId.contains(this.weaponId)) {
                    if (name.contains("ischyros") || name.contains("hagios") || name.contains("nylocas prinkipas") && npc.getId() != 10806) {
                        hide = true;
                    }
                } else if (Maintainance.meleeWeaponId.contains(this.weaponId)) {
                    if (name.contains("toxobolos") || name.contains("hagios") || name.contains("nylocas prinkipas") && npc.getId() != 10804) {
                        hide = true;
                    }
                } else if (Maintainance.magicWeaponId.contains(this.weaponId) && ((name.contains("ischyros") || name.contains("toxobolos")) && !entry.getOption().contains("barrage") && !entry.getOption().contains("burst") || name.contains("nylocas prinkipas") && npc.getId() != 10805)) {
                    hide = true;
                }
            }

            return hide;
        }
    }

    protected boolean isInNyloRegion() {
        return this.client.getMapRegions() != null && this.client.getMapRegions().length > 0 && Arrays.stream(this.client.getMapRegions()).anyMatch((s) -> {
            return s == 13122;
        });
    }

    public Color calculateHitpointsColor(float hpPercent) {
        hpPercent = Math.max(Math.min(100.0F, hpPercent), 0.0F);
        double rMod = 130.0D * (double)hpPercent / 100.0D;
        double gMod = 235.0D * (double)hpPercent / 100.0D;
        double bMod = 125.0D * (double)hpPercent / 100.0D;
        int r = (int)Math.min(255.0D, 255.0D - rMod);
        int g = Math.min(255, (int)(20.0D + gMod));
        int b = Math.min(255, (int)(0.0D + bMod));
        return new Color(r, g, b);
    }

    @Subscribe
    private void onGraphicsObjectCreated(GraphicsObjectCreated e) {
        GraphicsObject obj;
        if (this.verzNPC != null && (this.verzNPC.getId() == 8374 || this.verzNPC.getId() == 10835 || this.verzNPC.getId() == 10852)) {
            obj = e.getGraphicsObject();
            boolean hm = this.verzNPC.getId() == 10852;
            if (obj.getId() == 1595) {
                if (this.verzYellowHidden + 3000L < System.currentTimeMillis() && this.config.hideVerzYellow()) {
                    setHidden(this.verzNPC, true);
                    this.hidden.add(this.verzNPC);
                    this.verzYellowHidden = System.currentTimeMillis() + (long)(hm ? 11000 : 7000);
                }

                this.yellowPools.putIfAbsent(obj.getLocation(), 20);
                WorldPoint wp = WorldPoint.fromLocal(this.client, e.getGraphicsObject().getLocation());
                if (!this.yellows.contains(wp)) {
                    this.yellows.add(wp);
                }

                if (!this.yellowsList.contains(wp)) {
                    this.yellowsList.add(wp);
                }
            }
        }

        if (this.config.easyNylo() && this.isInNyloRegion()) {
            obj = e.getGraphicsObject();
            int id = obj.getId();
            if (id == 1562 || id == 1563 || id == 1564) {
                setHidden(obj, true);
                this.hidden.add(obj);
            }
        }

    }

    public int getDisplayIntForYellow(int y) {
        if (y >= 6) {
            return y - 6;
        } else {
            return y >= 3 ? y - 3 : y;
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String message = event.getMessage();
        if (this.thrallUp != -1L && this.thrallUp < System.currentTimeMillis() && message.contains(">You resurrect a ") && message.endsWith(" thrall.</col>")) {
            this.thrallUp = System.currentTimeMillis() + 57000L;
        }

        if (this.config.oldRoomMessages()) {
            String next;
            if (message.contains("Normal Mode")) {
                next = message.replaceAll("\\(", "");
                next = next.replaceAll("\\)", "");
                next = next.replaceAll(" Normal Mode", "");
                next = next.replaceAll("<br>", " ");
                event.setMessage(next);
                event.getMessageNode().setValue(next);
            } else if (message.contains("(Hard Mode)")) {
                next = message.replaceAll("\\(", "");
                next = next.replaceAll("\\)", "");
                next = next.replaceAll(" Hard Mode", "");
                next = next.replaceAll("<br>", " ");
                event.setMessage(next);
                event.getMessageNode().setValue(next);
            }
        }

    }

    public boolean isInThrallOverlayArea() {
        if (!this.inTheatre) {
            return false;
        } else if (this.isInNyloRegion()) {
            return false;
        } else {
            int bosshp = this.client.getVarbitValue(6448);
            if (bosshp <= 1) {
                return false;
            } else {
                if (this.xarpNPC != null) {
                    int xarpusid = this.xarpNPC.getId();
                    if (xarpusid == 8339 || xarpusid == 10767 || xarpusid == 10771) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private void wmes(String s) {
        if (this.config.leechWarnMes()) {
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=0000ff>" + s, "");
        }

    }

    private boolean otherShitBow(int i) {
        int[] e = new int[]{861, 12788, 22550, 22547};
        int[] var3 = e;
        int var4 = e.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int i2 = var3[var5];
            if (i2 == i) {
                return true;
            }
        }

        return false;
    }

    @Subscribe
    public void onGraphicChanged(GraphicChanged event) {
        if (event.getActor() instanceof Player) {
            if (this.config.nadohealcount()) {
                Player p = (Player)event.getActor();
                if (p.getGraphic() == 1602) {
                    float heal = (float)p.getHealthRatio() / (float)p.getHealthScale() * 100.0F;
                    this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "" + p.getName() + " healed verzik " + (int)((double)heal * 0.99D / 2.0D * 3.0D) + " health. (" + (int)((double)heal * 0.99D) + " hp tornado)", "");
                }
            }

            if (event.getActor() == this.client.getLocalPlayer()) {
                int gfx = event.getActor().getGraphic();
                if (gfx == 1873 || gfx == 1874 || gfx == 1875) {
                    this.thrallUp = -1L;
                    this.thrallUpClientTick = (long)this.client.getTickCount();
                }
            }
        } else if (event.getActor() instanceof NPC) {
            NPC npc = (NPC)event.getActor();
            boolean maidenRoom = WorldPoint.fromLocalInstance(this.client, this.client.getLocalPlayer().getLocalLocation()).getRegionID() == 12613;
            if (npc.getName() != null && maidenRoom) {
                MatomenosDetails details = (MatomenosDetails)this.maidenMatos.getOrDefault(npc.getIndex(), null);
                if (details != null) {
                    details.setFrozenTicks((Integer)GRAPHICS_MAP.getOrDefault(npc.getGraphic(), -1));
                }
            }
        }

    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        if (event.getActor() instanceof Player && (this.enforceRegion() || this.config.debug())) {
            Player p = (Player)event.getActor();
            if (p == null) {
                return;
            }

            int anim = p.getAnimation();
            if (anim == 8973 && event.getActor() == this.client.getLocalPlayer()) {
                this.thrallUp = -1L;
                this.thrallUpClientTick = (long)this.client.getTickCount();
            }

            if (p.getPlayerComposition() == null) {
                return;
            }

            int wep = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
            int hammerBop = 401;
            int godBop = 7045;
            int bow = 426;
            int clawSpec = 7514;
            int clawBop = 393;
            int whip = 1658;
            int chalyBop = 440;
            int chalySpec = 1203;
            int scy = 8056;
            int bggsSpec = 7643;
            int bggsSpec2 = 7642;
            int hammerSpec = 1378;
            int trident = 1167;
            int surge = 7855;
            int ticks = 0;
            if (anim == scy) {
                ticks = 5;
            }

            if (anim == clawBop || anim == whip || anim == clawSpec || anim == trident || anim == surge) {
                ticks = 4;
            }

            if (anim == chalySpec || anim == chalyBop) {
                ticks = 7;
            }

            if (anim == hammerSpec || anim == hammerBop || anim == bggsSpec || anim == bggsSpec2 || anim == godBop || anim == 7516) {
                ticks = 6;
            }

            if (ticks != 0) {
                this.attAnimTicks.put(p, ticks + 1);
            }

            Actor interacting = p.getInteracting();
            NPC target = interacting == null ? null : (NPC)interacting;
            if (p.equals(this.client.getLocalPlayer())) {
                int del = 1100;
                if (anim != 0 && anim != -1) {
                    if (!this.nyloSlaveInteracting(target)) {
                        int style = this.client.getVar(VarPlayer.ATTACK_STYLE);
                        if (anim == scy && style == 2) {
                            String a = style == 0 ? "accurate" : (style == 2 ? "crush" : "defensive");
                            this.flagUnpot = System.currentTimeMillis() + (long)del;
                            this.wmes("You scythed on " + a + ".");
                        } else if (anim == bow && !this.otherShitBow(wep) && !this.client.isPrayerActive(Prayer.RIGOUR)) {
                            this.flagUnpot = System.currentTimeMillis() + (long)del;
                            this.wmes("You bowed without rigour active.");
                        } else if (anim == hammerBop && wep == 13576) {
                            this.flagUnpot = System.currentTimeMillis() + (long)del;
                            this.wmes("You hammer bopped.");
                        } else if (anim == godBop) {
                            this.flagUnpot = System.currentTimeMillis() + (long)del;
                            this.wmes("You godsword bopped.");
                        } else if (anim == chalyBop) {
                            this.flagUnpot = System.currentTimeMillis() + (long)del;
                            this.wmes("You chaly poked.");
                        }
                    }

                    this.deferredCheck = new net.runelite.client.plugins.aztobextra.DeferredCheck(this.client.getTickCount(), anim, this.client.isPrayerActive(Prayer.PIETY));
                }
            }
        }

    }

    private boolean nyloSlaveInteracting(NPC target) {
        if (target != null && target.getName() != null && target.getName().toLowerCase().contains("nylocas")) {
            return !target.getName().toLowerCase().contains("vasil");
        } else {
            return false;
        }
    }

    public NPC getMaiden() {
        return this.maidenNPC;
    }

    public boolean isVerzP2Alive() {
        if (this.verzNPC == null) {
            return false;
        } else if (this.verzNPC.isDead()) {
            return false;
        } else {
            int i = this.verzNPC.getId();
            switch(i) {
                case 8372:
                case 10833:
                case 10850:
                    return true;
                default:
                    return false;
            }
        }
    }

    public boolean isVerzP1Dead() {
        if (this.verzNPC == null) {
            return false;
        } else {
            int i = this.verzNPC.getId();
            switch(i) {
                case 8370:
                case 10831:
                case 10848:
                    if (this.verzNPC.getPoseAnimation() != this.verzNPC.getIdlePoseAnimation()) {
                        return true;
                    }

                    return false;
                default:
                    return false;
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.thrallUp == -1L && this.thrallUpClientTick != (long)this.client.getTickCount()) {
            this.thrallUp = System.currentTimeMillis() + 60000L;
        }

        if (!this.inTheatre) {
            if (this.enforceRegion()) {
                this.inTheatre = true;
            }
        } else if (!this.enforceRegion()) {
            this.inTheatre = false;
        }

        if (this.xarpInitiateTickdown >= 0) {
            --this.xarpInitiateTickdown;
        }

        int size;
        if (!this.soteyProjectiles.isEmpty()) {
            size = ((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getLocalLocation().getX();
            int playerY = ((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getLocalLocation().getY();
            int finalSize = size;
            this.soteyProjectiles.entrySet().removeIf((projectilex) -> {
                return ((Projectile)projectilex.getKey()).getRemainingCycles() < 1 || ((WorldPoint)projectilex.getValue()).getX() != finalSize || ((WorldPoint)projectilex.getValue()).getY() != playerY;
            });
        }

        if (this.xarpNPC != null) {
            Iterator it = this.xarpusExhumeds.keySet().iterator();

            while(it.hasNext()) {
                GroundObject key = (GroundObject)it.next();
                this.xarpusExhumeds.replace(key, (Integer)this.xarpusExhumeds.get(key) - 1);
                if ((Integer)this.xarpusExhumeds.get(key) < 0) {
                    it.remove();
                }
            }
        }

        if (!this.maidenMatos.isEmpty()) {
            this.maidenMatos.values().forEach(MatomenosDetails::decrementFrozenTicks);
        }

        int id;
        if (this.inTheatre && this.client.getLocalPlayer() != null) {
            size = 0;
            boolean spectator = true;

            for(id = 330; id < 335; ++id) {
                String name = Text.standardize(this.client.getVarcStrValue(id));
                if (!Strings.isNullOrEmpty(name)) {
                    if (name.equalsIgnoreCase(this.client.getLocalPlayer().getName())) {
                        spectator = false;
                    }

                    String vstr = Integer.toString(id);
                    int healthValue = this.client.getVarbitValue(6440 + Integer.parseInt(vstr.substring(vstr.length() - 1)) + 2);
                    switch(healthValue) {
                        case 0:
                            return;
                        default:
                            ++size;
                    }
                }
            }

            this.partySize = size;
            this.spectating = spectator;
            if (this.deferredCheck != null && this.client.getTickCount() == this.deferredCheck.getTick()) {
                this.checkStats();
                this.deferredCheck = null;
            }
        }

        ArrayList<Player> toRemove0 = new ArrayList();
        Iterator var19 = this.attAnimTicks.keySet().iterator();

        Player n;
        int animationID;
        while(var19.hasNext()) {
            n = (Player)var19.next();
            animationID = (Integer)this.attAnimTicks.get(n);
            if (animationID - 1 == 0) {
                toRemove0.add(n);
            } else {
                this.attAnimTicks.put(n, animationID - 1);
            }
        }

        var19 = toRemove0.iterator();

        while(var19.hasNext()) {
            n = (Player)var19.next();
            this.attAnimTicks.remove(n);
        }

        ArrayList<NPC> toRemove = new ArrayList();
        Iterator var2 = this.aliveTimer.keySet().iterator();

        NPC nado;
        while(var2.hasNext()) {
            nado = (NPC)var2.next();
            int i = (Integer)this.aliveTimer.get(nado);
            if (i >= 54) {
                toRemove.add(nado);
            } else {
                this.aliveTimer.put(nado, i + 1);
            }
        }

        var2 = toRemove.iterator();

        while(var2.hasNext()) {
            nado = (NPC)var2.next();
            this.aliveTimer.remove(nado);
        }

        if (this.bloatNPC != null) {
            ++this.bloatDownCount;
            if (this.bloatNPC.getAnimation() == -1) {
                this.bloatDownCount = 0;
                ++this.bloatUpTimer;
                if (this.bloatNPC.getHealthScale() == 0) {
                    this.bloatState = 2;
                } else {
                    this.bloatState = 1;
                }
            } else {
                Supplier chunk;
                WorldPoint sw;
                Direction dir;
                if (25 < this.bloatDownCount && this.bloatDownCount < 35) {
                    this.bloatState = 3;
                    this.bloatSecondsTimer = System.currentTimeMillis();
                    sw = this.bloatNPC.getWorldLocation();
                    dir = (new Angle(this.bloatNPC.getOrientation())).getNearestDirection();
                    chunk = () -> {
                        LocalPoint lp = LocalPoint.fromWorld(this.client, sw);
                        if (lp != null && this.client.isInInstancedRegion()) {
                            int zone = this.client.getInstanceTemplateChunks()[0][lp.getSceneX() >> 3][lp.getSceneY() >> 3];
                            return BloatChunk.getOccupiedChunk(zone);
                        } else {
                            return BloatChunk.UNKNOWN;
                        }
                    };
                    this.bloatDown = new BloatDown(this.client, sw, dir, (BloatChunk)chunk.get());
                } else if (this.bloatDownCount < 26) {
                    this.bloatUpTimer = 0;
                    this.bloatState = 2;
                    this.bloatSecondsTimer = System.currentTimeMillis();
                    sw = this.bloatNPC.getWorldLocation();
                    dir = (new Angle(this.bloatNPC.getOrientation())).getNearestDirection();
                    chunk = () -> {
                        LocalPoint lp = LocalPoint.fromWorld(this.client, sw);
                        if (lp != null && this.client.isInInstancedRegion()) {
                            int zone = this.client.getInstanceTemplateChunks()[0][lp.getSceneX() >> 3][lp.getSceneY() >> 3];
                            return BloatChunk.getOccupiedChunk(zone);
                        } else {
                            return BloatChunk.UNKNOWN;
                        }
                    };
                    this.bloatDown = new BloatDown(this.client, sw, dir, (BloatChunk)chunk.get());
                } else if (this.bloatNPC.getModelHeight() == 568) {
                    this.bloatUpTimer = 0;
                    this.bloatState = 2;
                    this.bloatSecondsTimer = System.currentTimeMillis();
                    sw = this.bloatNPC.getWorldLocation();
                    dir = (new Angle(this.bloatNPC.getOrientation())).getNearestDirection();
                    chunk = () -> {
                        LocalPoint lp = LocalPoint.fromWorld(this.client, sw);
                        if (lp != null && this.client.isInInstancedRegion()) {
                            int zone = this.client.getInstanceTemplateChunks()[0][lp.getSceneX() >> 3][lp.getSceneY() >> 3];
                            return BloatChunk.getOccupiedChunk(zone);
                        } else {
                            return BloatChunk.UNKNOWN;
                        }
                    };
                    this.bloatDown = new BloatDown(this.client, sw, dir, (BloatChunk)chunk.get());
                } else {
                    this.bloatState = 1;
                    this.bloatSecondsTimer = System.currentTimeMillis();
                }
            }
        }

        if (this.verzNPC != null) {
            this.verzikTornadoTrailingLocations.clear();
            var2 = this.verzikTornadoLocations.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<NPC, WorldPoint> entry = (Entry)var2.next();
                this.verzikTornadoTrailingLocations.put((NPC)entry.getKey(), (WorldPoint)entry.getValue());
            }

            this.verzikTornadoLocations.clear();
            var2 = this.verzikTornados.iterator();

            while(var2.hasNext()) {
                nado = (NPC)var2.next();
                this.verzikTornadoLocations.put(nado, nado.getWorldLocation());
            }

            ++this.ticksSincePurple;
            animationID = this.verzNPC.getAnimation();
            switch(this.verzNPC.getId()) {
                case 8370:
                case 10831:
                case 10848:
                    if (this.verzikTickPaused) {
                        this.verzikTickPaused = false;
                        this.verzikTicksUntilAttack = 18;
                        this.verzikAttackCount = 0;
                    } else {
                        this.verzikTicksUntilAttack = Math.max(0, this.verzikTicksUntilAttack - 1);
                        if (animationID > -1 && this.verzikTicksUntilAttack < 5 && animationID != this.verzikLastAnimation && animationID == 8109) {
                            this.verzikTicksUntilAttack = 14;
                        }
                    }
                    break;
                case 8372:
                case 10833:
                case 10850:
                    this.client.getProjectiles().stream().filter((px) -> {
                        return px.getRemainingCycles() > 0 && px.getId() == 1583;
                    }).findAny().filter((px) -> {
                        return !this.verzikRangedAttacks.contains(px);
                    }).ifPresent((px) -> {
                        this.verzikRangedAttacks.add(px);
                        --this.lightningAttacksRemaining;
                    });
                    this.verzikRangedAttacks.removeIf((px) -> {
                        return px.getRemainingCycles() <= 0;
                    });
                    if (!this.verzikRedPhase && this.verzNPC.getAnimation() == 8117) {
                        this.verzikRedPhase = true;
                    }

                    if (this.verzikRedPhase && this.purplecounter != null) {
                        this.attsAfterPurpleSpawn = 0;
                        this.infoBoxManager.removeInfoBox(this.purplecounter);
                        this.purplecounter = null;
                        this.initPurpleBox = false;
                        this.scuffedPurpCheck = false;
                    }

                    this.verzikTicksUntilAttack = Math.max(0, this.verzikTicksUntilAttack - 1);
                    boolean didp2Att = false;
                    if (animationID > -1 && this.verzikTicksUntilAttack < 3 && animationID != this.verzikLastAnimation) {
                        switch(animationID) {
                            case 8114:
                            case 8116:
                                this.verzikTicksUntilAttack = 4;
                                ++this.verzikAttackCount;
                                didp2Att = true;
                        }
                    }

                    if (this.config.verzPurpleTicks()) {
                        if (this.purplecounter == null) {
                            if (this.initPurpleBox) {
                                this.attsAfterPurpleSpawn = 1;
                                this.purplecounter = new PurpleAttackInfobox(PURP_TICK_COUNT_ICON, this);
                                this.purplecounter.set(this.attsAfterPurpleSpawn, this.attsAfterPurpleSpawn + (this.scuffedPurpCheck ? "*" : ""));
                                this.infoBoxManager.addInfoBox(this.purplecounter);
                                this.initPurpleBox = false;
                            }
                        } else {
                            this.attsAfterPurpleSpawn += didp2Att ? 1 : 0;
                            this.purplecounter.set(this.attsAfterPurpleSpawn, this.attsAfterPurpleSpawn + (this.scuffedPurpCheck ? "*" : ""));
                        }
                    }
                    break;
                case 8374:
                case 10835:
                case 10852:
                    Iterator var10;
                    if (this.isVerzikEnraged()) {
                        if (this.config.nadohealcount() && this.p3healCounter == null) {
                            this.p3healCounter = new Counter(HEAL_ICON, this, 0);
                            this.p3healCounter.setTooltip("Verzik p3 tornado heals");
                            this.infoBoxManager.addInfoBox(this.p3healCounter);
                        }

                        if (this.last1PlayerLocation != null) {
                            var10 = this.memorizedTornados.values().iterator();

                            while(var10.hasNext()) {
                                MemorizedTornado mt = (MemorizedTornado)var10.next();
                                NPC mtNpc = mt.getNpc();
                                WorldPoint npcLocation = mtNpc.getWorldLocation();
                                if (mt.getCurrentPosition() == null) {
                                    mt.setCurrentPosition(npcLocation);
                                } else {
                                    mt.setLastPosition(mt.getCurrentPosition());
                                    mt.setCurrentPosition(npcLocation);
                                }
                            }
                        }

                        if (this.last1PlayerLocation == null) {
                            this.last1PlayerLocation = this.client.getLocalPlayer().getWorldLocation();
                            this.last0PlayerLocation = this.last1PlayerLocation;
                        } else {
                            this.last1PlayerLocation = this.last0PlayerLocation;
                            this.last0PlayerLocation = this.client.getLocalPlayer().getWorldLocation();
                            this.memorizedTornados.entrySet().removeIf((entryx) -> {
                                boolean remov = ((MemorizedTornado)entryx.getValue()).getRelativeDelta(this.last1PlayerLocation) != -1;
                                if (remov && this.config.smartNado()) {
                                    setHidden(((MemorizedTornado)entryx.getValue()).getNpc(), true);
                                    this.hidden.add(((MemorizedTornado)entryx.getValue()).getNpc());
                                    return true;
                                } else {
                                    return false;
                                }
                            });
                        }
                    }

                    if (this.yellowsList.size() > 0) {
                        if (!this.yellowsOut) {
                            if (this.verzNPC.getId() == 10852) {
                                this.yellowGroups = this.findYellows(this.yellows);
                            }

                            this.yellowsOut = true;
                        } else {
                            --this.yellowTimer;
                            if (this.yellowTimer <= 0) {
                                if (this.verzNPC.getId() != 10852) {
                                    this.yellowsOut = false;
                                    this.yellowTimer = 14;
                                    this.yellows.clear();
                                    this.yellowsList.clear();
                                    this.yellowGroups.clear();
                                } else if (this.hmYellowSpotNum >= 3) {
                                    this.yellowsOut = false;
                                    this.yellowTimer = 14;
                                    this.hmYellowSpotNum = 1;
                                    this.yellows.clear();
                                    this.yellowsList.clear();
                                    this.yellowGroups.clear();
                                } else {
                                    this.yellowTimer = 3;
                                    ++this.hmYellowSpotNum;
                                    var10 = this.client.getPlayers().iterator();

                                    label307:
                                    while(true) {
                                        while(true) {
                                            Player p;
                                            do {
                                                do {
                                                    if (!var10.hasNext()) {
                                                        break label307;
                                                    }

                                                    p = (Player)var10.next();
                                                } while(p.getName() == null);
                                            } while(!this.partyMembersNames.contains(p.getName()));

                                            WorldPoint wp = WorldPoint.fromLocal(this.client, p.getLocalLocation());
                                            int index = 0;

                                            for(Iterator var6 = this.yellowGroups.iterator(); var6.hasNext(); ++index) {
                                                ArrayList<WorldPoint> yg = (ArrayList)var6.next();
                                                if (yg.contains(wp)) {
                                                    ((ArrayList)this.yellowGroups.get(index)).remove(wp);
                                                    break;
                                                }

                                                boolean exitLoop = false;

                                                for(int i = yg.size() - 1; i >= 0; --i) {
                                                    if (((WorldPoint)yg.get(i)).distanceTo(wp) <= 1) {
                                                        ((ArrayList)this.yellowGroups.get(index)).remove(i);
                                                        exitLoop = true;
                                                        break;
                                                    }
                                                }

                                                if (exitLoop) {
                                                    break;
                                                }
                                            }

                                            if (this.yellowsList.contains(wp)) {
                                                this.yellowsList.remove(wp);
                                            } else {
                                                for(int i = this.yellowsList.size() - 1; i >= 0; --i) {
                                                    if (((WorldPoint)this.yellowsList.get(i)).distanceTo(wp) <= 1) {
                                                        this.yellowsList.remove(i);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }

            this.verzikLastAnimation = animationID;
        }

        if (this.verzNPC != null && this.hidden.contains(this.verzNPC) && this.verzYellowHidden < System.currentTimeMillis()) {
            setHidden(this.verzNPC, false);
            this.hidden.remove(this.verzNPC);
        }

        if (this.soteNPC != null && this.soteNPC.getId() == 8388) {
            if (!this.redTiles.isEmpty()) {
                this.redTiles.clear();
            }

            if (this.isInOverWorld()) {
                this.wasInUnderWorld = false;
                if (this.client.getLocalPlayer() != null && this.client.getLocalPlayer().getWorldLocation() != null) {
                    this.overWorldRegionID = this.client.getLocalPlayer().getWorldLocation().getRegionID();
                }
            }
        }

        if (this.nyloBossNPC != null) {
            --this.nyloBossChangeTick;
            id = this.nyloBossNPC.getId();
            if (this.nyloBossNPC.getId() != this.lastNyloBossId) {
                this.lastNyloBossId = id;
                if (id != 10787 && id != 10788 && id != 10789) {
                    this.nyloBossChangeTick = 10;
                } else {
                    this.nyloBossChangeTick = 15;
                }
            }
        }

        this.instanceTimer = (this.instanceTimer + 1) % 4;
    }

    public WorldPoint getNextValidPoint(ArrayList<WorldPoint> points) {
        Iterator var2 = points.iterator();

        while(var2.hasNext()) {
            WorldPoint p = (WorldPoint)var2.next();
            if (this.isSetSpawn(p) != -1) {
                return p;
            }
        }

        return null;
    }

    public WorldPoint getNearestPoint(WorldPoint corner, ArrayList<WorldPoint> points) {
        double minDistance = 2.147483647E9D;
        WorldPoint point = new WorldPoint(corner.getX(), corner.getY(), corner.getPlane());
        Iterator var6 = points.iterator();

        while(var6.hasNext()) {
            WorldPoint p = (WorldPoint)var6.next();
            double distance = this.distanceBetween(p, corner);
            if (distance < minDistance) {
                minDistance = distance;
                point = p;
            }
        }

        return point;
    }

    public double distanceBetween(WorldPoint a, WorldPoint b) {
        return Math.sqrt(Math.pow((double)(a.getRegionX() - b.getRegionX()), 2.0D) + Math.pow((double)(a.getRegionY() - b.getRegionY()), 2.0D));
    }

    public int isSetSpawn(WorldPoint p) {
        if (p.getRegionX() == 7 && p.getRegionY() == 11) {
            return 1;
        } else if (p.getRegionX() == 16 && p.getRegionY() == 17) {
            return 2;
        } else if (p.getRegionX() == 25 && p.getRegionY() == 11) {
            return 3;
        } else if (p.getRegionX() == 7 && p.getRegionY() == 23) {
            return 4;
        } else {
            return p.getRegionX() == 25 && p.getRegionY() == 23 ? 5 : -1;
        }
    }

    public ArrayList<ArrayList<WorldPoint>> findYellows(ArrayList<WorldPoint> points) {
        ArrayList groups = new ArrayList();

        while(points.size() > 0) {
            ArrayList<WorldPoint> group = new ArrayList();
            WorldPoint initial = this.getNextValidPoint(points);
            group.add(initial);
            points.remove(initial);
            WorldPoint second = this.getNearestPoint(initial, points);
            group.add(second);
            points.remove(second);
            WorldPoint third = this.getNearestPoint(initial, points);
            group.add(third);
            points.remove(third);
            groups.add(group);
        }

        return groups;
    }

    private void checkStats() {
        int anim = this.deferredCheck.getAnim();
        boolean hammerBop = true;
        boolean godBop = true;
        boolean bow = true;
        int clawSpec = 7514;
        int clawBop = 393;
        int whip = 1658;
        boolean chalyBop = true;
        int chalySpec = 1203;
        int scy = 8056;
        int bggsSpec = 7643;
        int hammerSpec = 1378;
        int del = 1100;
        int[] hits = new int[]{clawSpec, clawBop, whip, chalySpec, scy, bggsSpec, 7642, hammerSpec};
        int[] var15 = hits;
        int var16 = hits.length;

        for(int var17 = 0; var17 < var16; ++var17) {
            int i = var15[var17];
            if (anim == i) {
                int lvl = this.client.getBoostedSkillLevel(Skill.STRENGTH);
                boolean piety = this.deferredCheck.isPiety();
                boolean is118 = lvl == 118;
                if (!piety || !is118) {
                    String s = "attacked";
                    if (i == clawSpec) {
                        s = "claw speced";
                    } else if (i == chalySpec) {
                        s = "chaly speced";
                    } else if (i == bggsSpec) {
                        s = "bgs speced";
                    } else if (i == hammerSpec) {
                        s = "hammer speced";
                    }

                    String s2 = "";
                    if (!piety) {
                        if (!is118) {
                            s2 = " with " + lvl + " strength and without piety.";
                        } else {
                            s2 = " without piety.";
                        }
                    } else if (!is118) {
                        s2 = " with " + lvl + " strength.";
                    }

                    this.wmes("You " + s + s2);
                    this.flagUnpot = System.currentTimeMillis() + (long)del;
                }
                break;
            }
        }

    }

    @Subscribe
    private void onHitsplatApplied(HitsplatApplied e) {
        if (this.verzNPC != null && e.getActor() instanceof NPC) {
            NPC npc = (NPC)e.getActor();
            Hitsplat hitsplat = e.getHitsplat();
            if (npc == this.verzNPC && hitsplat.getHitsplatType() == HitsplatType.HEAL && this.isVerzikEnraged() && hitsplat.getAmount() < 150 && this.p3healCounter != null) {
                this.p3healCounter.setCount(this.p3healCounter.getCount() + hitsplat.getAmount());
            }
        }

    }

    boolean isVerzikEnraged() {
        return this.enraged;
    }

    static String stripColor(String str) {
        return str.replaceAll("(<col=[0-9a-f]+>|</col>)", "");
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded entry) {
    }

    private static <X> X[] arrayOfNotNull(X[] array) {
        int p = 0;

        for(int N = array.length; p < N; ++p) {
            if (array[p] == null) {
                int m = p;

                for(int i = p + 1; i < N; ++i) {
                    if (array[i] != null) {
                        ++m;
                    }
                }

                X[] res = Arrays.copyOf(array, m);

                for(int i = p + 1; i < N; ++i) {
                    if (array[i] != null) {
                        res[p++] = array[i];
                    }
                }

                return res;
            }
        }

        return array;
    }

    @Subscribe
    private void onNpcSpawned(NpcSpawned e) {
        NPC npc = e.getNpc();
        boolean isHM;
        if (this.isNpcFromName(npc, "nylocas vasil")) {
            this.nyloBossNPC = npc;
            this.nyloBossStyle = "melee";
            this.nyloBossChangeTick = 10;
            this.lastNyloBossId = npc.getId();
        } else if (this.isNpcFromName(npc, "sotetseg")) {
            this.soteNPC = npc;
        } else if (this.isNpcFromName(npc, "xarpus")) {
            this.xarpNPC = npc;
            isHM = false;
            switch(npc.getId()) {
                case 10770:
                case 10771:
                case 10772:
                case 10773:
                    isHM = true;
                default:
                    int playerCount = this.getPartySize();
                    //int exhumeCount = false;
                    byte exhumeCount;
                    if (playerCount == 5) {
                        exhumeCount = 18;
                        if (isHM) {
                            exhumeCount = 24;
                        }
                    } else if (playerCount == 4) {
                        exhumeCount = 15;
                        if (isHM) {
                            exhumeCount = 20;
                        }
                    } else if (playerCount == 3) {
                        exhumeCount = 12;
                        if (isHM) {
                            exhumeCount = 16;
                        }
                    } else if (playerCount == 2) {
                        exhumeCount = 9;
                        if (isHM) {
                            exhumeCount = 12;
                        }
                    } else {
                        exhumeCount = 7;
                    }

                    this.exhumesLeft = exhumeCount;
            }
        } else if (this.isNpcFromName(npc, "verzik vitur")) {
            this.resetVerz();
            this.verzNPC = npc;
        } else if (this.isNpcFromName(npc, "athanatos")) {
            this.ticksSincePurple = 0;
            this.spawnPurple(true);
        } else if (npc.getId() != 8386 && npc.getId() != 10863 && npc.getId() != 10845) {
            if (npc.getId() != 8359 && npc.getId() != 10812 && npc.getId() != 10813) {
                if (this.isNpcFromName(npc, "maiden of ")) {
                    this.maidenNPC = npc;
                    this.maidenPhase = MaidenPhase.of(npc);
                }
            } else {
                this.bloatNPC = npc;
                this.bloatUpTimer = 0;
                this.bloatSecondsTimer = System.currentTimeMillis();
            }
        } else {
            this.verzikTornados.add(npc);
            this.enraged = true;
            this.memorizedTornados.putIfAbsent(npc.getIndex(), new MemorizedTornado(npc));
        }

        if (this.isNpcFromName(npc, "matomenos")) {
            if (this.verzNPC == null) {
                this.maidenMatos.put(npc.getIndex(), new MatomenosDetails(this.client, npc, this.maidenPhase.getPhaseKey()));
            } else {
                this.verzMatos.put(npc.getIndex(), new MatomenosDetails(this.client, npc, "0"));
            }
        }

        isHM = this.inRegion(13122, 12613);
        if (isHM && (this.isNpcFromName(npc, "nylocas hag") || this.isNpcFromName(npc, "nylocas tox") || this.isNpcFromName(npc, "nylocas isc"))) {
            this.aliveTimer.put(npc, 0);
        }

    }

    private boolean isNpcFromName(NPC npc, String sotetseg) {
        return npc != null && npc.getName() != null && npc.getName().toLowerCase().contains(sotetseg);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            this.nextInstance = true;
            if (this.isInNyloRegion() && this.config.nyloPillarHide()) {
                this.removeGameObjectsFromScene(ImmutableSet.of(32862), 0);
            }
        }

    }

    public void removeGameObjectsFromScene(Set<Integer> objectIDs, int plane) {
        Scene scene = this.client.getScene();
        Tile[][] tiles = scene.getTiles()[plane];

        for(int x = 0; x < 104; ++x) {
            for(int y = 0; y < 104; ++y) {
                Tile tile = tiles[x][y];
                if (tile != null && objectIDs != null) {
                    GameObject[] var8 = tile.getGameObjects();
                    int var9 = var8.length;

                    for(int var10 = 0; var10 < var9; ++var10) {
                        GameObject o = var8[var10];
                        if (o != null && objectIDs.contains(o.getId())) {
                            scene.removeGameObject(o);
                        }
                    }
                }
            }
        }

    }

    @Subscribe
    private void onNpcChanged(NpcChanged e) {
        if (this.maidenNPC != null) {
            NPC npc = e.getNpc();
            if (this.isNpcFromName(npc, "The Maiden of Sugadinti")) {
                this.maidenPhase = MaidenPhase.of(npc);
            }
        }

        int id = e.getNpc().getId();
        if (id != 8375 && id != 10853 && id != 10836 && (id == 8370 || id == 10831 || id == 10848)) {
            this.partyMembersNames.clear();

            for(int i = 330; i < 335; ++i) {
                if (this.client.getVarcStrValue(i) != null && !this.client.getVarcStrValue(i).equals("")) {
                    this.partyMembersNames.add(this.client.getVarcStrValue(i).replaceAll("[^A-Za-z0-9_-]", " ").trim());
                }
            }
        }

        if (id != 8355 && id != 10787 && id != 10808) {
            if (id != 8356 && id != 10788 && id != 10809) {
                if (id == 8357 || id == 10789 || id == 10810) {
                    this.nyloBossStyle = "range";
                }
            } else {
                this.nyloBossStyle = "mage";
            }
        } else {
            this.nyloBossStyle = "melee";
        }

    }

    @Subscribe
    private void onNpcDespawned(NpcDespawned e) {
        NPC npc = e.getNpc();
        this.aliveTimer.remove(npc);
        if (this.isNpcFromName(npc, "nylocas vasil")) {
            this.resetNyloBoss();
        } else if (this.isNpcFromName(npc, "sotetseg")) {
            this.isInstanceTimerRunning = false;
        }

        if (this.isNpcFromName(npc, "sotetseg") && this.client.getPlane() != 3) {
            this.resetSote();
        } else if (this.isNpcFromName(npc, "xarpus")) {
            this.resetXarp();
        } else if (this.isNpcFromName(npc, "verzik vitur")) {
            this.resetVerz();
        } else if (npc.getId() != 8386 && npc.getId() != 10863 && npc.getId() != 10845) {
            if (this.isNpcFromName(npc, "ent bloat")) {
                this.resetBloat();
            }
        } else {
            this.verzikTornados.remove(npc);
            if (this.memorizedTornados.containsKey(npc.getIndex())) {
                this.memorizedTornados.remove(npc.getIndex());
            }
        }

        if (this.isNpcFromName(npc, "maiden of s")) {
            this.maidenNyloWheelchairState = true;
            this.resetMaiden();
        } else if (this.isNpcFromName(npc, "matomenos") || npc.getId() == 8385) {
            this.maidenMatos.remove(npc.getIndex());
            this.verzMatos.remove(npc.getIndex());
        }

    }

    @Subscribe
    private void onProjectileMoved(ProjectileMoved e) {
        Projectile p = e.getProjectile();
        if (p != null) {
            if (this.soteNPC != null && this.client.getGameCycle() < p.getStartMovementCycle()) {
                switch(p.getId()) {
                    case 1604:
                        this.bombAttacksRemaining = 10;
                        break;
                    case 1606:
                        WorldPoint soteWp = WorldPoint.fromLocal(this.client, this.soteNPC.getLocalLocation());
                        WorldPoint projWp = WorldPoint.fromLocal(this.client, p.getX1(), p.getY1(), this.client.getPlane());
                        if (this.soteNPC.getAnimation() == 8139 && projWp.equals(soteWp)) {
                            --this.bombAttacksRemaining;
                        }
                }
            }

            int id = e.getProjectile().getId();
            Actor interacting = this.findActor(Maintainance.getInteractingIndex(p));
            if (this.config.debug() || this.soteNPC != null && interacting == this.client.getLocalPlayer() || id == 1604 || id == 1605 || id == 1598) {
                WorldPoint point = WorldPoint.fromLocal(this.client, e.getPosition());
                this.soteyProjectiles.put(p, point);
                if (this.client.getGameCycle() < p.getStartMovementCycle()) {
                    this.soteyProjectileFilter.put(p, this.client.getTickCount());
                }
            }

            if (this.verzNPC != null) {
                if (p.getId() == 1586) {
                    this.spawnPurple(false);
                }

                if (this.client.getGameCycle() < p.getStartMovementCycle()) {
                    switch(p.getId()) {
                        case 1585:
                            if (this.lightningAttacksRemaining <= 0) {
                                this.lightningAttacksRemaining = 4;
                            }
                        case 1586:
                    }
                }
            }

        }
    }

    Actor findActor(int idx) {
        if (idx == 0) {
            return null;
        } else {
            int index;
            if (idx > 0) {
                index = idx - 1;
                NPC[] npcs = this.client.getCachedNPCs();
                return index < npcs.length && index >= 0 ? npcs[index] : null;
            } else {
                index = -idx - 1;
                Player[] players = this.client.getCachedPlayers();
                return index < players.length && index >= 0 ? players[index] : null;
            }
        }
    }

    private boolean spawnPurple(boolean safety) {
        if (this.purpleSafetyCheck > this.client.getTickCount()) {
            return false;
        } else {
            if (safety && this.config.debug()) {
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Failed to trigger purple with projectile only.", "");
            }

            this.attsAfterPurpleSpawn = 0;
            this.purpleSafetyCheck = this.client.getTickCount() + 20;
            this.initPurpleBox = true;
            this.scuffedPurpCheck = safety;
            return true;
        }
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        GroundObject o;
        if (this.soteNPC != null) {
            o = event.getGroundObject();
            if (o.getId() == 33035) {
                Tile t = event.getTile();
                WorldPoint p = WorldPoint.fromLocal(this.client, t.getLocalLocation());
                Point point = new Point(p.getRegionX(), p.getRegionY());
                if (this.isInOverWorld()) {
                    this.redTiles.add(new Point(point.getX() - swMazeSquareOverWorld.getX(), point.getY() - swMazeSquareOverWorld.getY()));
                }

                if (this.isInUnderWorld()) {
                    this.redTiles.add(new Point(point.getX() - swMazeSquareUnderWorld.getX(), point.getY() - swMazeSquareUnderWorld.getY()));
                    this.wasInUnderWorld = true;
                }
            }
        }

        if (this.xarpNPC != null) {
            o = event.getGroundObject();
            if (o.getId() == 32743) {
                boolean isHM = false;
                switch(this.xarpNPC.getId()) {
                    case 10770:
                    case 10771:
                    case 10772:
                    case 10773:
                        isHM = true;
                    default:
                        if (isHM) {
                            this.xarpusExhumeds.put(o, 9);
                        } else {
                            this.xarpusExhumeds.put(o, 11);
                        }

                        --this.exhumesLeft;
                        if (this.exhumesLeft == 0) {
                            this.xarpInitiateTickdown = isHM ? 17 : 21;
                        }
                }
            }
        }

    }

    @Subscribe
    protected void onVarbitChanged(VarbitChanged event) {
        if (this.isInBloatRegion()) {
            int varp6447 = this.client.getVarbitValue(this.client.getVarps(), 6447);
            if (varp6447 != this.lastVarp6447 && varp6447 > 0) {
                this.bloatUpTimer = 0;
                this.bloatSecondsTimer = System.currentTimeMillis();
                this.bloatVar = 1;
            }

            this.lastVarp6447 = varp6447;
            if (this.client.getVarbitValue(6447) == 0) {
                this.bloatVar = 0;
            }
        }

    }

    private boolean isInOverWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13123;
    }

    private boolean isInUnderWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13379;
    }

    WorldPoint worldPointFromMazePoint(Point mazePoint) {
        return this.overWorldRegionID == -1 && this.client.getLocalPlayer() != null ? WorldPoint.fromRegion(this.client.getLocalPlayer().getWorldLocation().getRegionID(), mazePoint.getX() + getSwMazeSquareOverWorld().getX(), mazePoint.getY() + getSwMazeSquareOverWorld().getY(), 0) : WorldPoint.fromRegion(this.overWorldRegionID, mazePoint.getX() + getSwMazeSquareOverWorld().getX(), mazePoint.getY() + getSwMazeSquareOverWorld().getY(), 0);
    }

    public static Point getSwMazeSquareOverWorld() {
        return swMazeSquareOverWorld;
    }

    public static Point getSwMazeSquareUnderWorld() {
        return swMazeSquareUnderWorld;
    }

    public boolean isWasInUnderWorld() {
        return this.wasInUnderWorld;
    }

    private boolean isInBloatRegion() {
        return this.client.getMapRegions() != null && this.client.getMapRegions().length > 0 && Arrays.stream(this.client.getMapRegions()).anyMatch((s) -> {
            return s == 13125;
        });
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        if (event.getGameObject().getId() == 41437) {
            this.rewardChest = event.getGameObject();
        }

        if (this.config.hidePillar() != PillarHide.No && event.getGameObject().getId() == 32687 && (this.config.hidePillar() == PillarHide.All_Pillars || event.getGameObject().getWorldLocation().getRegionY() < 21)) {
            this.client.getScene().removeGameObject(event.getGameObject());
        }

    }

    public static void setHidden(Renderable renderable, boolean hidden) {
        //boolean steroid = true;
        //if (steroid) {
            /*
            if (DeathIndicatorsPlugin.set == null) {
                DeathIndicatorsPlugin.set = new HashSet();
            }

            if (ClientLoader.hiddenEntities == null) {
                ClientLoader.hiddenEntities = new HashSet();
            }

            if (hidden) {
                DeathIndicatorsPlugin.set.add(renderable);
                ClientLoader.hiddenEntities.add(renderable);
            } else {
                DeathIndicatorsPlugin.set.remove(renderable);
                ClientLoader.hiddenEntities.remove(renderable);
            }
            */

        //} else {
            Method setHidden = null;

            try {
                setHidden = renderable.getClass().getMethod("setHidden", Boolean.TYPE);
            } catch (NoSuchMethodException var6) {
                log.debug("Couldn't find method setHidden for class {}", renderable.getClass());
                return;
            }

            try {
                setHidden.invoke(renderable, hidden);
            } catch (IllegalAccessException | InvocationTargetException var5) {
                log.debug("Couldn't call method setHidden for class {}", renderable.getClass());
            }

        //}
    }

    public static boolean getHidden(Renderable renderable) {

            Method getHidden = null;

            try {
                getHidden = renderable.getClass().getMethod("getHidden");
            } catch (NoSuchMethodException var5) {
                log.debug("Couldn't find method getHidden for class {}", renderable.getClass());
                return false;
            }

            try {
                return (Boolean)getHidden.invoke(renderable);
            } catch (IllegalAccessException | InvocationTargetException var4) {
                log.debug("Couldn't call method getHidden for class {}", renderable.getClass());
                return false;
            }
    }

    private boolean isInLobbyRegion() {
        return this.client.getMapRegions() != null && this.client.getMapRegions().length > 0 && Arrays.stream(this.client.getMapRegions()).anyMatch((s) -> {
            return s == 14642;
        });
    }

    public Map<Integer, MatomenosDetails> getMaidenMatos() {
        return this.maidenMatos;
    }

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        Actor actor = actorDeath.getActor();
        if (this.verzNPC != null && actorDeath.getActor() instanceof Player && actorDeath.getActor().getName() != null) {
            this.partyMembersNames.remove(actorDeath.getActor().getName());
        }

        if (actor instanceof Player) {
            Player player = (Player)actor;
            if (player == this.client.getLocalPlayer()) {
                this.takeScreenshot(this.client.getLocalPlayer().getName() + "---death");
            } else if (player != this.client.getLocalPlayer() && player.getCanvasTilePoly() != null) {
                this.takeScreenshot(this.client.getLocalPlayer().getName() + "---death-of-" + player.getName());
            }
        }

    }

    private void takeScreenshot(String fileName) {
        String hook = DiscordWebhook.requireNonNull(this.config.webhookId());
        Consumer<Image> imageCallback = (img) -> {
            this.executor.submit(() -> {
                try {
                    this.takeScreenshot(fileName, img, hook);
                } catch (IOException var5) {
                    var5.printStackTrace();
                }

            });
        };
        this.drawManager.requestNextFrameListener(imageCallback);
    }

    private void takeScreenshot(String fileName, Image image, String hook) throws IOException {
        BufferedImage screenshot = new BufferedImage(image.getWidth((ImageObserver)null), image.getHeight((ImageObserver)null), 2);
        Graphics graphics = screenshot.getGraphics();
        int gameOffsetX = 0;
        int gameOffsetY = 0;
        graphics.drawImage(image, gameOffsetX, gameOffsetY, (ImageObserver)null);
        ByteArrayOutputStream screenshotOutput = new ByteArrayOutputStream();
        ImageIO.write(screenshot, "png", screenshotOutput);
        DiscordWebhook FileSender = new DiscordWebhook();
        FileSender.SendWebhook(screenshotOutput, fileName, hook);
    }

    public int getPartySize() {
        return this.partySize;
    }

    public boolean isSpectating() {
        return this.spectating;
    }

    public Client getClient() {
        return this.client;
    }

    public AzEasyTobConfig getConfig() {
        return this.config;
    }

    public HashMap<String, Long> getWarnCooldown() {
        return this.warnCooldown;
    }

    public void setWarnCooldown(HashMap<String, Long> warnCooldown) {
        this.warnCooldown = warnCooldown;
    }

    public boolean isInTheatre() {
        return this.inTheatre;
    }

    public int getBombAttacksRemaining() {
        return this.bombAttacksRemaining;
    }

    public int getLightningAttacksRemaining() {
        return this.lightningAttacksRemaining;
    }

    public int getTicksSincePurple() {
        return this.ticksSincePurple;
    }

    public NPC getSoteNPC() {
        return this.soteNPC;
    }

    public NPC getVerzNPC() {
        return this.verzNPC;
    }

    public NPC getBloatNPC() {
        return this.bloatNPC;
    }

    public NPC getMaidenNPC() {
        return this.maidenNPC;
    }

    public NPC getXarpNPC() {
        return this.xarpNPC;
    }

    public int getExhumesLeft() {
        return this.exhumesLeft;
    }

    public int getXarpInitiateTickdown() {
        return this.xarpInitiateTickdown;
    }

    public Map<Projectile, WorldPoint> getSoteyProjectiles() {
        return this.soteyProjectiles;
    }

    public Map<Projectile, Integer> getSoteyProjectileFilter() {
        return this.soteyProjectileFilter;
    }

    public Map<GroundObject, Integer> getXarpusExhumeds() {
        return this.xarpusExhumeds;
    }

    public LinkedHashSet<Point> getRedTiles() {
        return this.redTiles;
    }

    public SkillIconManager getIconManager() {
        return this.iconManager;
    }

    public Map<Integer, MemorizedTornado> getMemorizedTornados() {
        return this.memorizedTornados;
    }

    public WorldPoint getLast0PlayerLocation() {
        return this.last0PlayerLocation;
    }

    public WorldPoint getLast1PlayerLocation() {
        return this.last1PlayerLocation;
    }

    public HashMap<NPC, WorldPoint> getVerzikTornadoLocations() {
        return this.verzikTornadoLocations;
    }

    public HashMap<NPC, WorldPoint> getVerzikTornadoTrailingLocations() {
        return this.verzikTornadoTrailingLocations;
    }

    public Map<LocalPoint, Integer> getYellowPools() {
        return this.yellowPools;
    }

    public HashMap<Player, Integer> getAttAnimTicks() {
        return this.attAnimTicks;
    }

    public HashMap<NPC, Integer> getAliveTimer() {
        return this.aliveTimer;
    }

    public int getBloatState() {
        return this.bloatState;
    }

    public int getBloatDownCount() {
        return this.bloatDownCount;
    }

    public int getBloatUpTimer() {
        return this.bloatUpTimer;
    }

    public int getBloatVar() {
        return this.bloatVar;
    }

    public BloatDown getBloatDown() {
        return this.bloatDown;
    }

    public long getBloatSecondsTimer() {
        return this.bloatSecondsTimer;
    }

    public long getThrallUp() {
        return this.thrallUp;
    }

    public long getThrallUpClientTick() {
        return this.thrallUpClientTick;
    }

    public NPC getNyloBossNPC() {
        return this.nyloBossNPC;
    }

    public int getNyloBossChangeTick() {
        return this.nyloBossChangeTick;
    }

    public void setHotKeyPressed(boolean hotKeyPressed) {
        this.hotKeyPressed = hotKeyPressed;
    }

    public boolean isHotKeyPressed() {
        return this.hotKeyPressed;
    }

    public boolean isMaidenNyloWheelchairState() {
        return this.maidenNyloWheelchairState;
    }

    public void setMaidenNyloWheelchairState(boolean maidenNyloWheelchairState) {
        this.maidenNyloWheelchairState = maidenNyloWheelchairState;
    }

    public boolean isN1Orn2Alive() {
        return this.n1Orn2Alive;
    }

    public void setN1Orn2Alive(boolean n1Orn2Alive) {
        this.n1Orn2Alive = n1Orn2Alive;
    }

    public int getInstanceTimer() {
        return this.instanceTimer;
    }

    public boolean isHiddenPills() {
        return this.hiddenPills;
    }

    public void setHiddenPills(boolean hiddenPills) {
        this.hiddenPills = hiddenPills;
    }

    public long getFlagUnpot() {
        return this.flagUnpot;
    }

    public Map<Integer, MatomenosDetails> getVerzMatos() {
        return this.verzMatos;
    }

    public MaidenPhase getMaidenPhase() {
        return this.maidenPhase;
    }
}
