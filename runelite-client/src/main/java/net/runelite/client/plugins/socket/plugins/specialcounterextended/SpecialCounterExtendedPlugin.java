/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.EquipmentInventorySlot
 *  net.runelite.api.GameState
 *  net.runelite.api.Hitsplat
 *  net.runelite.api.Hitsplat$HitsplatType
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Skill
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.HitsplatApplied
 *  net.runelite.api.events.InteractingChanged
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.client.callback.ClientThread
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.game.ItemManager
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  net.runelite.client.ui.overlay.infobox.InfoBox
 *  net.runelite.client.ui.overlay.infobox.InfoBoxManager
 *  net.runelite.client.util.AsyncBufferedImage
 *  org.pf4j.Extension
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.socket.plugins.specialcounterextended;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.Hitsplat;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.socket.plugins.specialcounterextended.Boss;
import net.runelite.client.plugins.socket.plugins.specialcounterextended.SpecialCounter;
import net.runelite.client.plugins.socket.plugins.specialcounterextended.SpecialCounterExtendedConfig;
import net.runelite.client.plugins.socket.plugins.specialcounterextended.SpecialCounterOverlay;
import net.runelite.client.plugins.socket.plugins.specialcounterextended.SpecialIcon;
import net.runelite.client.plugins.socket.plugins.specialcounterextended.SpecialWeapon;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.AsyncBufferedImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@PluginDescriptor(name="Socket - Special Attack Counter", description="Track DWH, Arclight, Darklight, and BGS special attacks used on NPCs using server sockets.", tags={"socket", "server", "discord", "connection", "broadcast", "combat", "npcs", "overlay"}, enabledByDefault=true)
@PluginDependency(value=SocketPlugin.class)
public class SpecialCounterExtendedPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SpecialCounterExtendedPlugin.class);
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private EventBus eventBus;
    @Inject
    private SpecialCounterOverlay overlay;
    @Inject
    private SpecialCounterExtendedConfig config;
    private int currentWorld;
    private int specialPercentage;
    private Actor lastSpecTarget;
    private int lastSpecTick;
    private SpecialWeapon specialWeapon;
    private final Set<Integer> interactedNpcIds = new HashSet<Integer>();
    private final SpecialCounter[] specialCounter = new SpecialCounter[SpecialWeapon.values().length];
    private boolean specialUsed = false;
    private long specialExperience = -1L;
    private long magicExperience = -1L;
    private int[] sotetsegIDs = new int[]{8387, 8388, 10867, 10868, 10864, 10865};

    @Provides
    SpecialCounterExtendedConfig getConfig(ConfigManager configManager) {
        return (SpecialCounterExtendedConfig)configManager.getConfig(SpecialCounterExtendedConfig.class);
    }

    protected void startUp() {
        this.currentWorld = -1;
        this.specialPercentage = -1;
        this.lastSpecTarget = null;
        this.lastSpecTick = -1;
        this.interactedNpcIds.clear();
        this.specialUsed = false;
        this.specialExperience = -1L;
        this.magicExperience = -1L;
        this.overlayManager.add((Overlay)this.overlay);
    }

    protected void shutDown() {
        this.removeCounters();
        this.overlayManager.remove((Overlay)this.overlay);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        GameState state = event.getGameState();
        if (state == GameState.LOGGED_IN) {
            if (this.currentWorld == -1) {
                this.currentWorld = this.client.getWorld();
            } else if (this.currentWorld != this.client.getWorld()) {
                this.currentWorld = this.client.getWorld();
                this.removeCounters();
            }
        } else if (state == GameState.LOGIN_SCREEN) {
            this.removeCounters();
        }
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged interactingChanged) {
        Actor source = interactingChanged.getSource();
        Actor target = interactingChanged.getTarget();
        if (this.lastSpecTick != this.client.getTickCount() || source != this.client.getLocalPlayer() || target == null) {
            return;
        }
        this.lastSpecTarget = target;
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        int specialPercentage = this.client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT);
        if (this.specialPercentage == -1 || specialPercentage >= this.specialPercentage) {
            this.specialPercentage = specialPercentage;
            return;
        }
        this.specialPercentage = specialPercentage;
        this.specialWeapon = this.usedSpecialWeapon();
        this.lastSpecTarget = this.client.getLocalPlayer().getInteracting();
        this.lastSpecTick = this.client.getTickCount();
        this.specialUsed = true;
        this.specialExperience = this.client.getOverallExperience();
        this.magicExperience = this.client.getSkillExperience(Skill.MAGIC);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        if (!this.config.guessDawnbringer()) {
            return;
        }
        if (this.specialExperience != -1L && this.specialUsed && this.lastSpecTarget != null && this.lastSpecTarget instanceof NPC) {
            this.specialUsed = false;
            long deltaExp = this.client.getOverallExperience() - this.specialExperience;
            this.specialExperience = -1L;
            long deltaMagicExp = (long)this.client.getSkillExperience(Skill.MAGIC) - this.magicExperience;
            this.magicExperience = -1L;
            if (this.specialWeapon != null && this.specialWeapon == SpecialWeapon.DAWNBRINGER) {
                int currentAttackStyleVarbit = this.client.getVar(VarPlayer.ATTACK_STYLE);
                int damage = currentAttackStyleVarbit == 3 ? (int)Math.round((double)deltaMagicExp / 1.4) : (int)Math.round((double)deltaExp / 3.5);
                String pName = this.client.getLocalPlayer().getName();
                this.updateCounter(pName, this.specialWeapon, null, damage);
                JSONObject data = new JSONObject();
                data.put("player", pName);
                data.put("target", ((NPC)this.lastSpecTarget).getId());
                data.put("weapon", this.specialWeapon.ordinal());
                data.put("hit", damage);
                JSONObject payload = new JSONObject();
                payload.put("special-extended", data);
                this.eventBus.post((Object)new SocketBroadcastPacket(payload));
                this.lastSpecTarget = null;
            }
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        Actor target = hitsplatApplied.getActor();
        Hitsplat hitsplat = hitsplatApplied.getHitsplat();
        Hitsplat.HitsplatType hitsplatType = hitsplat.getHitsplatType();
        if (!hitsplat.isMine() || target == this.client.getLocalPlayer()) {
            return;
        }
        log.debug("Hitsplat target: {} spec target: {}", (Object)target, (Object)this.lastSpecTarget);
        if (this.lastSpecTarget != null && this.lastSpecTarget != target) {
            return;
        }
        boolean wasSpec = this.lastSpecTarget != null;
        this.lastSpecTarget = null;
        this.specialUsed = false;
        this.specialExperience = -1L;
        this.magicExperience = -1L;
        if (!(target instanceof NPC)) {
            return;
        }
        NPC npc = (NPC)target;
        int interactingId = npc.getId();
        if (!this.interactedNpcIds.contains(interactingId)) {
            this.removeCounters();
            this.addInteracting(interactingId);
        }
        if (wasSpec && this.specialWeapon != null && hitsplat.getAmount() > 0) {
            int hit = this.getHit(this.specialWeapon, hitsplat);
            log.debug("Special attack target: id: {} - target: {} - weapon: {} - amount: {}", new Object[]{interactingId, target, this.specialWeapon, hit});
            String pName = this.client.getLocalPlayer().getName();
            this.updateCounter(pName, this.specialWeapon, null, hit);
            JSONObject data = new JSONObject();
            data.put("player", pName);
            data.put("target", interactingId);
            data.put("weapon", this.specialWeapon.ordinal());
            data.put("hit", hit);
            JSONObject payload = new JSONObject();
            payload.put("special-extended", data);
            this.eventBus.post((Object)new SocketBroadcastPacket(payload));
        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            if (this.client.getGameState() != GameState.LOGGED_IN) {
                return;
            }
            JSONObject payload = event.getPayload();
            if (payload.has("special-extended")) {
                String pName = this.client.getLocalPlayer().getName();
                JSONObject data = payload.getJSONObject("special-extended");
                if (data.getString("player").equals(pName)) {
                    return;
                }
                this.clientThread.invoke(() -> {
                    SpecialWeapon weapon = SpecialWeapon.values()[data.getInt("weapon")];
                    String attacker = data.getString("player");
                    int targetId = data.getInt("target");
                    if (!this.interactedNpcIds.contains(targetId)) {
                        this.removeCounters();
                        this.addInteracting(targetId);
                    }
                    this.updateCounter(attacker, weapon, attacker, data.getInt("hit"));
                });
            } else if (payload.has("special-extended-bossdead")) {
                this.removeCounters();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addInteracting(int npcId) {
        this.interactedNpcIds.add(npcId);
        Boss boss = Boss.getBoss(npcId);
        if (boss != null) {
            this.interactedNpcIds.addAll(boss.getIds());
        }
    }

    private int getHit(SpecialWeapon specialWeapon, Hitsplat hitsplat) {
        return specialWeapon.isDamage() ? hitsplat.getAmount() : 1;
    }

    private SpecialWeapon usedSpecialWeapon() {
        ItemContainer equipment = this.client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipment == null) {
            return null;
        }
        Item[] items = equipment.getItems();
        int weaponIdx = EquipmentInventorySlot.WEAPON.getSlotIdx();
        if (items == null || weaponIdx >= items.length) {
            return null;
        }
        Item weapon = items[weaponIdx];
        for (SpecialWeapon specialWeapon : SpecialWeapon.values()) {
            if (specialWeapon.getItemID() != weapon.getId()) continue;
            return specialWeapon;
        }
        return null;
    }

    private void updateCounter(String player, SpecialWeapon specialWeapon, String name, int hit) {
        if (specialWeapon == SpecialWeapon.BANDOS_GODSWORD_OR) {
            specialWeapon = SpecialWeapon.BANDOS_GODSWORD;
        }
        if (specialWeapon == SpecialWeapon.ZARYTE_CROSSBOW && !this.inTobRegion()) {
            return;
        }
        SpecialCounter counter = this.specialCounter[specialWeapon.ordinal()];
        AsyncBufferedImage image = this.itemManager.getImage(specialWeapon.getItemID());
        this.overlay.addOverlay(player, new SpecialIcon((BufferedImage)image, Integer.toString(hit), System.currentTimeMillis()));
        if (counter == null) {
            counter = new SpecialCounter((BufferedImage)image, this, hit, specialWeapon);
            this.infoBoxManager.addInfoBox((InfoBox)counter);
            this.specialCounter[specialWeapon.ordinal()] = counter;
        } else {
            counter.addHits(hit);
        }
        Map<String, Integer> partySpecs = counter.getPartySpecs();
        if (partySpecs.containsKey(name)) {
            partySpecs.put(name, hit + partySpecs.get(name));
        } else {
            partySpecs.put(name, hit);
        }
    }

    private void removeCounters() {
        this.interactedNpcIds.clear();
        for (int i = 0; i < this.specialCounter.length; ++i) {
            SpecialCounter counter = this.specialCounter[i];
            if (counter == null) continue;
            this.infoBoxManager.removeInfoBox((InfoBox)counter);
            this.specialCounter[i] = null;
        }
    }

    public boolean isSotetseg(int id) {
        for (int i : this.sotetsegIDs) {
            if (i != id) continue;
            return true;
        }
        return false;
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        if (this.isSotetseg(npcDespawned.getNpc().getId()) && this.client.getLocalPlayer().getWorldLocation().getPlane() == 3) {
            return;
        }
        NPC actor = npcDespawned.getNpc();
        if (this.lastSpecTarget == actor) {
            this.lastSpecTarget = null;
        }
        if (actor.isDead() && this.interactedNpcIds.contains(actor.getId())) {
            this.removeCounters();
            JSONObject payload = new JSONObject();
            payload.put("special-extended-bossdead", "dead");
            this.eventBus.post((Object)new SocketBroadcastPacket(payload));
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        Player localPlayer = this.client.getLocalPlayer();
        if (localPlayer != event.getActor()) {
            return;
        }
        if (localPlayer.getAnimation() == 1816 && (this.isInOverWorld() || this.isInUnderWorld())) {
            this.removeCounters();
        }
    }

    private boolean isInOverWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13123;
    }

    private boolean isInUnderWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13379;
    }

    public boolean inTobRegion() {
        return this.inRegion(12611, 12612, 12613, 12687, 13122, 13123, 13125, 13379);
    }

    public boolean inRegion(int ... regions) {
        if (this.client.getMapRegions() != null) {
            for (int i : this.client.getMapRegions()) {
                for (int j : regions) {
                    if (i != j) continue;
                    return true;
                }
            }
        }
        return false;
    }
}

