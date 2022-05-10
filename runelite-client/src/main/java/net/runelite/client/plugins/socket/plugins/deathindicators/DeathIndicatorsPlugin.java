//
// Decompiled by Procyon v0.5.36
//

package net.runelite.client.plugins.socket.plugins.deathindicators;

import net.runelite.client.util.Text;
import org.slf4j.LoggerFactory;
import net.runelite.api.events.ClientTick;
import java.util.Collection;
import net.runelite.api.events.GameTick;
import net.runelite.api.VarPlayer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.kit.KitType;
import net.runelite.api.PlayerComposition;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.Actor;
import java.util.Objects;
import net.runelite.api.Player;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.api.Hitsplat;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import java.util.Arrays;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.NpcSpawned;
import java.util.Iterator;
import net.runelite.client.ui.overlay.Overlay;
import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.api.MenuEntry;
import java.util.function.Predicate;
import java.lang.reflect.Method;
import net.runelite.client.plugins.PluginManager;
import net.runelite.api.NPC;
import net.runelite.client.eventbus.EventBus;
import java.util.ArrayList;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.Client;
import javax.inject.Inject;
import org.slf4j.Logger;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.Plugin;

@PluginDescriptor(name = "Socket - Death Indicators", description = "Removes Nylos that have been killed", tags = { "Socket, death, kill", "nylo" }, enabledByDefault = false)
@PluginDependency(SocketPlugin.class)
public class DeathIndicatorsPlugin extends Plugin
{
    private static final Logger log;
    @Inject
    private DeathIndicatorsConfig config;
    @Inject
    private DeathIndicatorsOverlay overlay;
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    private ArrayList<NyloQ> nylos;
    @Inject
    private EventBus eventBus;
    private boolean inNylo;
    private ArrayList<NPC> deadNylos;
    private NyloQ maidenNPC;
    private ArrayList<Integer> hiddenIndices;
    private int partySize;
    @Inject
    PluginManager pluginManager;
    private ArrayList<Method> reflectedMethods;
    private ArrayList<Plugin> reflectedPlugins;
    private final Predicate<MenuEntry> filterMenuEntries;

    public DeathIndicatorsPlugin() {
        this.inNylo = false;
        final int[] id = new int[1];
        final String[] option = new String[1];
        this.filterMenuEntries = (entry -> {
            id[0] = entry.getIdentifier();
            option[0] = Text.standardize(entry.getOption(), true).toLowerCase();
            if (option[0].contains("attack") && this.deadNylos.contains(this.client.getCachedNPCs()[id[0]])) {
                entry.setDeprioritized(true);
            }
            return true;
        });
    }

    @Provides
    DeathIndicatorsConfig getConfig(final ConfigManager configManager) {
        return (DeathIndicatorsConfig)configManager.getConfig((Class)DeathIndicatorsConfig.class);
    }

    protected void startUp() {
        this.deadNylos = new ArrayList<NPC>();
        this.nylos = new ArrayList<NyloQ>();
        this.hiddenIndices = new ArrayList<Integer>();
        this.overlayManager.add((Overlay)this.overlay);
        this.reflectedMethods = new ArrayList<Method>();
        this.reflectedPlugins = new ArrayList<Plugin>();
        for (final Plugin p : this.pluginManager.getPlugins()) {
            Method m;
            try {
                m = p.getClass().getDeclaredMethod("SocketDeathIntegration", Integer.TYPE);
            }
            catch (NoSuchMethodException var5) {
                continue;
            }
            this.reflectedMethods.add(m);
            this.reflectedPlugins.add(p);
        }
    }

    protected void shutDown() {
        this.deadNylos = null;
        this.nylos = null;
        this.hiddenIndices = null;
        this.overlayManager.remove((Overlay)this.overlay);
    }

    @Subscribe
    public void onNpcSpawned(final NpcSpawned event) {
        if (this.partySize != -1) {
            int bigHP = -1;
            int smallHP = -1;
            int maidenHP = -1;
            if (this.partySize < 4) {
                bigHP = 16;
                smallHP = 8;
                maidenHP = 2625;
            }
            else if (this.partySize == 4) {
                bigHP = 19;
                smallHP = 9;
                maidenHP = 3062;
            }
            else if (this.partySize == 5) {
                bigHP = 22;
                smallHP = 11;
                maidenHP = 3500;
            }
            final int id = event.getNpc().getId();
            switch (id) {
                case 8342:
                case 8343:
                case 8344:
                case 10791:
                case 10792:
                case 10793:
                case 10797:
                case 10798:
                case 10799: {
                    this.nylos.add(new NyloQ(event.getNpc(), 0, smallHP));
                    break;
                }
                case 8345:
                case 8346:
                case 8347:
                case 8351:
                case 8352:
                case 8353:
                case 10794:
                case 10795:
                case 10796:
                case 10800:
                case 10801:
                case 10802: {
                    this.nylos.add(new NyloQ(event.getNpc(), 0, bigHP));
                    break;
                }
                case 8360:
                case 10822: {
                    final NyloQ maidenTemp = new NyloQ(event.getNpc(), 0, maidenHP);
                    this.nylos.add(maidenTemp);
                    this.maidenNPC = maidenTemp;
                    break;
                }
            }
        }
    }

    @Subscribe
    public void onNpcDespawned(final NpcDespawned event) {
        if (this.nylos.size() != 0) {
            this.nylos.removeIf(q -> q.npc.equals(event.getNpc()));
        }
        if (this.deadNylos.size() != 0) {
            this.deadNylos.removeIf(q -> q.equals(event.getNpc()));
        }
        final int id = event.getNpc().getId();
        switch (id) {
            case 8360:
            case 8361:
            case 8362:
            case 8363:
            case 8364:
            case 8365: {
                this.maidenNPC = null;
                break;
            }
        }
    }

    @Subscribe
    public void onScriptPreFired(final ScriptPreFired scriptPreFired) {
        if (this.inNylo && scriptPreFired.getScriptId() == 996) {
            final int[] intStack = this.client.getIntStack();
            final int intStackSize = this.client.getIntStackSize();
            final int widgetId = intStack[intStackSize - 4];
            try {
                this.processXpDrop(widgetId);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean inRegion(final int... regions) {
        if (this.client.getMapRegions() != null) {
            final int[] mapRegions = this.client.getMapRegions();
            return Arrays.stream(mapRegions).anyMatch(i -> Arrays.stream(regions).anyMatch(j -> i == j));
        }
        return false;
    }

    private void postHit(final int index, final int dmg) {
        final JSONArray data = new JSONArray();
        final JSONObject message = new JSONObject();
        message.put("index", index);
        message.put("damage", dmg);
        data.put(message);
        final JSONObject send = new JSONObject();
        send.put("sDeath", data);
        this.eventBus.post((Object)new SocketBroadcastPacket(send));
    }

    @Subscribe
    public void onHitsplatApplied(final HitsplatApplied hitsplatApplied) {
        if (this.inNylo) {
            for (final NyloQ q : this.nylos) {
                if (hitsplatApplied.getActor().equals(q.npc)) {
                    if (hitsplatApplied.getHitsplat().getHitsplatType().equals((Object)Hitsplat.HitsplatType.HEAL)) {
                        final NyloQ nyloQ = q;
                        nyloQ.hp += hitsplatApplied.getHitsplat().getAmount();
                    }
                    else {
                        final NyloQ nyloQ2 = q;
                        nyloQ2.hp -= hitsplatApplied.getHitsplat().getAmount();
                        final NyloQ nyloQ3 = q;
                        nyloQ3.queuedDamage -= hitsplatApplied.getHitsplat().getAmount();
                    }
                    if (q.hp <= 0) {
                        final NyloQ finalQ = q;
                        this.deadNylos.removeIf(o -> o.equals(finalQ.npc));
                    }
                    else {
                        if (q.npc.getId() != 8360 && q.npc.getId() != 8361 && q.npc.getId() != 8362 && q.npc.getId() != 8363 && q.npc.getId() != 10822 && q.npc.getId() != 10823 && q.npc.getId() != 10824 && q.npc.getId() != 10825) {
                            continue;
                        }
                        final double percent = q.hp / (double)q.maxHP;
                        if (percent < 0.7 && q.phase == 0) {
                            q.phase = 1;
                        }
                        if (percent < 0.5 && q.phase == 1) {
                            q.phase = 2;
                        }
                        if (percent >= 0.3 || q.phase != 2) {
                            continue;
                        }
                        q.phase = 3;
                    }
                }
            }
        }
    }

    @Subscribe
    public void onSocketReceivePacket(final SocketReceivePacket event) {
        if (this.inNylo) {
            try {
                final JSONObject payload = event.getPayload();
                if (payload.has("sDeath")) {
                    final JSONArray data = payload.getJSONArray("sDeath");
                    final JSONObject jsonmsg = data.getJSONObject(0);
                    final int index = jsonmsg.getInt("index");
                    final int damage = jsonmsg.getInt("damage");
                    for (final NyloQ q : this.nylos) {
                        if (q.npc.getIndex() == index) {
                            final NyloQ nyloQ = q;
                            nyloQ.queuedDamage += damage;
                            final NyloQ finalQ = q;
                            if (q.npc.getId() == 8360 || q.npc.getId() == 8361 || q.npc.getId() == 8362 || q.npc.getId() == 8363 || q.npc.getId() == 10822 || q.npc.getId() == 10823 || q.npc.getId() == 10824 || q.npc.getId() == 10825) {
                                if (q.queuedDamage <= 0) {
                                    continue;
                                }
                                final double percent = (q.hp - (double)q.queuedDamage) / q.maxHP;
                                if (percent < 0.7 && q.phase == 0) {
                                    q.phase = 1;
                                }
                                if (percent < 0.5 && q.phase == 1) {
                                    q.phase = 2;
                                }
                                if (percent >= 0.3 || q.phase != 2) {
                                    continue;
                                }
                                q.phase = 3;
                            }
                            else {
                                if (q.hp - q.queuedDamage > 0 || !this.deadNylos.stream().noneMatch(o -> o.getIndex() == finalQ.npc.getIndex())) {
                                    continue;
                                }
                                this.deadNylos.add(q.npc);
                                if (!this.config.hideNylo()) {
                                    continue;
                                }
                                //this.setHiddenNpc(q.npc, true);
                                q.hidden = true;
                                if (this.reflectedPlugins.size() != this.reflectedMethods.size()) {
                                    continue;
                                }
                                for (int i = 0; i < this.reflectedPlugins.size(); ++i) {
                                    try {
                                        final Method tm = this.reflectedMethods.get(i);
                                        tm.setAccessible(true);
                                        tm.invoke(this.reflectedPlugins.get(i), q.npc.getIndex());
                                    }
                                    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ExceptionInInitializerError | NullPointerException ex) {
                                        final Throwable t = null;
                                        final Throwable var11 = t;
                                        DeathIndicatorsPlugin.log.debug(this.reflectedPlugins.get(i).getName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    private void setHiddenNpc(final NPC npc, final boolean hidden) {
        final List<Integer> newHiddenNpcIndicesList = (List<Integer>)this.client.getHiddenNpcIndices();
        if (hidden) {
            newHiddenNpcIndicesList.add(npc.getIndex());
            this.hiddenIndices.add(npc.getIndex());
        }
        else if (newHiddenNpcIndicesList.contains(npc.getIndex())) {
            newHiddenNpcIndicesList.remove((Object)npc.getIndex());
        }
        this.client.setHiddenNpcIndices((List)newHiddenNpcIndicesList);
    }*/

    void addToDamageQueue(final int damage) {
        if (damage != -1) {
            final Actor interacted = Objects.requireNonNull(this.client.getLocalPlayer()).getInteracting();
            if (interacted instanceof NPC) {
                final NPC interactedNPC = (NPC)interacted;
                this.postHit(interactedNPC.getIndex(), damage);
            }
        }
    }

    private void processXpDrop(final int widgetId) throws InterruptedException {
        final Widget xpDrop = this.client.getWidget(WidgetInfo.TO_GROUP(widgetId), WidgetInfo.TO_CHILD(widgetId));
        if (xpDrop != null) {
            final Widget[] children = xpDrop.getChildren();
            final Widget text = children[0];
            final String cleansedXpDrop = this.cleanseXpDrop(text.getText());
            int damage = -1;
            final int weaponUsed = Objects.requireNonNull(Objects.requireNonNull(this.client.getLocalPlayer()).getPlayerComposition()).getEquipmentId(KitType.WEAPON);
            if (this.client.getLocalPlayer().getAnimation() != 1979) {
                if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch(id -> id == 202)) {
                    if (weaponUsed == 22323 || weaponUsed == 11905 || weaponUsed == 11907 || weaponUsed == 12899 || weaponUsed == 22292 || weaponUsed == 25731) {
                        if (this.client.getLocalPlayer().getAnimation() == 1979) {
                            return;
                        }
                        if (this.client.getVarbitValue(4696) == 0) {
                            if (this.client.getVar(VarPlayer.ATTACK_STYLE) != 3) {
                                damage = (int)(Integer.parseInt(cleansedXpDrop) / 2.0);
                            }
                        }
                        else if (this.client.getVar(VarPlayer.ATTACK_STYLE) == 3) {
                            damage = (int)Math.round(Integer.parseInt(cleansedXpDrop) / 3.6667);
                        }
                        else {
                            damage = (int)Math.round(Integer.parseInt(cleansedXpDrop) / 3.3334);
                        }
                    }
                }
                else if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch(id -> id == 197 || id == 198 || id == 199)) {
                    if (weaponUsed == 22325 || weaponUsed == 25739 || weaponUsed == 25736 || weaponUsed == 21015) {
                        return;
                    }
                    if (this.client.getVarbitValue(4696) == 0) {
                        if (weaponUsed != 22323 && weaponUsed != 11905 && weaponUsed != 11907 && weaponUsed != 12899 && weaponUsed != 22292 && weaponUsed != 25731) {
                            if (weaponUsed == 12006) {
                                if (this.client.getVar(VarPlayer.ATTACK_STYLE) == 1) {
                                    if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch(id -> id == 197)) {
                                        damage = (int)Math.round(3.0 * Integer.parseInt(cleansedXpDrop) / 4.0);
                                    }
                                }
                                else {
                                    damage = Integer.parseInt(cleansedXpDrop) / 4;
                                }
                            }
                            else {
                                damage = Integer.parseInt(cleansedXpDrop) / 4;
                            }
                        }
                        else {
                            if (this.client.getLocalPlayer().getAnimation() == 1979) {
                                return;
                            }
                            if (this.client.getVarbitValue(4696) == 0 && this.client.getVar(VarPlayer.ATTACK_STYLE) == 3) {
                                damage = Integer.parseInt(cleansedXpDrop);
                            }
                        }
                    }
                    else {
                        damage = (int)Math.round(Integer.parseInt(cleansedXpDrop) / 5.3333);
                    }
                }
                else if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch(id -> id == 200)) {
                    if (weaponUsed == 11959) {
                        return;
                    }
                    if (this.client.getVarbitValue(4696) == 0) {
                        damage = (int)(Integer.parseInt(cleansedXpDrop) / 4.0);
                    }
                    else {
                        damage = (int)Math.round(Integer.parseInt(cleansedXpDrop) / 5.333);
                    }
                }
                this.addToDamageQueue(damage);
            }
        }
    }

    private String cleanseXpDrop(String text) {
        if (text.contains("<")) {
            if (text.contains("<img=11>")) {
                text = text.substring(9);
            }
            if (text.contains("<")) {
                text = text.substring(0, text.indexOf("<"));
            }
        }
        return text;
    }

    @Subscribe
    public void onGameTick(final GameTick event) {
        if (this.inRegion(13122, 12613)) {
            this.inNylo = true;
            this.partySize = 0;
            for (int i = 330; i < 335; ++i) {
                if (this.client.getVarcStrValue(i) != null && !this.client.getVarcStrValue(i).equals("")) {
                    ++this.partySize;
                }
            }
            for (final NyloQ q : this.nylos) {
                if (q.hidden) {
                    final NyloQ nyloQ = q;
                    ++nyloQ.hiddenTicks;
                    if (q.npc.getHealthRatio() == 0 || q.hiddenTicks <= 5) {
                        continue;
                    }
                    q.hiddenTicks = 0;
                    q.hidden = false;
                    //this.setHiddenNpc(q.npc, false);
                    this.deadNylos.removeIf(x -> x.equals(q.npc));
                }
            }
        }
        else {
            this.inNylo = false;
            if (!this.hiddenIndices.isEmpty()) {
                //final List<Integer> newHiddenNpcIndicesList = (List<Integer>)this.client.getHiddenNpcIndices();
                //newHiddenNpcIndicesList.removeAll(this.hiddenIndices);
                //this.client.setHiddenNpcIndices((List)newHiddenNpcIndicesList);
                //this.hiddenIndices.clear();
            }
            if (!this.nylos.isEmpty() || !this.deadNylos.isEmpty()) {
                this.nylos.clear();
                this.deadNylos.clear();
            }
        }
    }

    @Subscribe
    public void onClientTick(final ClientTick event) {
        if (this.config.deprioNylo()) {
            this.client.setMenuEntries(this.updateMenuEntries(this.client.getMenuEntries()));
        }
    }

    private MenuEntry[] updateMenuEntries(final MenuEntry[] menuEntries) {
        return Arrays.stream(menuEntries).filter(this.filterMenuEntries).sorted((o1, o2) -> 0).toArray(MenuEntry[]::new);
    }

    public ArrayList<NPC> getDeadNylos() {
        return this.deadNylos;
    }

    public NyloQ getMaidenNPC() {
        return this.maidenNPC;
    }

    static {
        log = LoggerFactory.getLogger((Class)DeathIndicatorsPlugin.class);
    }
}
