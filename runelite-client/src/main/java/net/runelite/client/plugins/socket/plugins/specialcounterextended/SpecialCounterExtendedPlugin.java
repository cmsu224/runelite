//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.Hitsplat.HitsplatType;
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
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "Socket - Special Attack Counter",
        description = "Track DWH, Arclight, Darklight, and BGS special attacks used on NPCs using server sockets.",
        tags = {"socket", "server", "discord", "connection", "broadcast", "combat", "npcs", "overlay"},
        enabledByDefault = false
)
public class SpecialCounterExtendedPlugin extends Plugin {
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
    private final Set<Integer> interactedNpcIds = new HashSet();
    private final SpecialCounter[] specialCounter = new SpecialCounter[SpecialWeapon.values().length];
    private boolean specialUsed = false;
    private long specialExperience = -1L;
    private long magicExperience = -1L;

    public SpecialCounterExtendedPlugin() {
    }

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
        this.overlayManager.add(this.overlay);
    }

    protected void shutDown() {
        this.removeCounters();
        this.overlayManager.remove(this.overlay);
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
        if (this.lastSpecTick == this.client.getTickCount() && source == this.client.getLocalPlayer() && target != null) {
            this.lastSpecTarget = target;
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        int specialPercentage = this.client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT);
        if (this.specialPercentage != -1 && specialPercentage < this.specialPercentage) {
            this.specialPercentage = specialPercentage;
            this.specialWeapon = this.usedSpecialWeapon();
            this.lastSpecTarget = this.client.getLocalPlayer().getInteracting();
            this.lastSpecTick = this.client.getTickCount();
            this.specialUsed = true;
            this.specialExperience = this.client.getOverallExperience();
            this.magicExperience = (long)this.client.getSkillExperience(Skill.MAGIC);
        } else {
            this.specialPercentage = specialPercentage;
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            if (this.config.guessDawnbringer()) {
                if (this.specialExperience != -1L && this.specialUsed && this.lastSpecTarget != null && this.lastSpecTarget instanceof NPC) {
                    this.specialUsed = false;
                    long deltaExp = this.client.getOverallExperience() - this.specialExperience;
                    this.specialExperience = -1L;
                    long deltaMagicExp = (long)this.client.getSkillExperience(Skill.MAGIC) - this.magicExperience;
                    this.magicExperience = -1L;
                    if (this.specialWeapon != null && this.specialWeapon == SpecialWeapon.DAWNBRINGER) {
                        int currentAttackStyleVarbit = this.client.getVar(VarPlayer.ATTACK_STYLE);
                        int damage;
                        if (currentAttackStyleVarbit == 3) {
                            damage = (int)Math.round((double)deltaMagicExp / 1.4D);
                        } else {
                            damage = (int)Math.round((double)deltaExp / 3.5D);
                        }

                        String pName = this.client.getLocalPlayer().getName();
                        this.updateCounter(pName, this.specialWeapon, (String)null, damage);
                        JSONObject data = new JSONObject();
                        data.put("player", pName);
                        data.put("target", ((NPC)this.lastSpecTarget).getId());
                        data.put("weapon", this.specialWeapon.ordinal());
                        data.put("hit", damage);
                        JSONObject payload = new JSONObject();
                        payload.put("special-extended", data);
                        this.eventBus.post(new SocketBroadcastPacket(payload));
                        this.lastSpecTarget = null;
                    }
                }

            }
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        Actor target = hitsplatApplied.getActor();
        Hitsplat hitsplat = hitsplatApplied.getHitsplat();
        HitsplatType hitsplatType = hitsplat.getHitsplatType();
        if (hitsplat.isMine() && target != this.client.getLocalPlayer()) {
            log.debug("Hitsplat target: {} spec target: {}", target, this.lastSpecTarget);
            if (this.lastSpecTarget == null || this.lastSpecTarget == target) {
                boolean wasSpec = this.lastSpecTarget != null;
                this.lastSpecTarget = null;
                this.specialUsed = false;
                this.specialExperience = -1L;
                this.magicExperience = -1L;
                if (target instanceof NPC) {
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
                        this.updateCounter(pName, this.specialWeapon, (String)null, hit);
                        JSONObject data = new JSONObject();
                        data.put("player", pName);
                        data.put("target", interactingId);
                        data.put("weapon", this.specialWeapon.ordinal());
                        data.put("hit", hit);
                        JSONObject payload = new JSONObject();
                        payload.put("special-extended", data);
                        this.eventBus.post(new SocketBroadcastPacket(payload));
                    }

                }
            }
        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            if (this.client.getGameState() != GameState.LOGGED_IN) {
                return;
            }

            JSONObject payload = event.getPayload();
            if (!payload.has("special-extended")) {
                return;
            }

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
        } catch (Exception var5) {
            var5.printStackTrace();
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
        } else {
            Item[] items = equipment.getItems();
            int weaponIdx = EquipmentInventorySlot.WEAPON.getSlotIdx();
            if (items != null && weaponIdx < items.length) {
                Item weapon = items[weaponIdx];
                SpecialWeapon[] var5 = SpecialWeapon.values();
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    SpecialWeapon specialWeapon = var5[var7];
                    if (specialWeapon.getItemID() == weapon.getId()) {
                        return specialWeapon;
                    }
                }

                return null;
            } else {
                return null;
            }
        }
    }

    private void updateCounter(String player, SpecialWeapon specialWeapon, String name, int hit) {
        if (specialWeapon == SpecialWeapon.BANDOS_GODSWORD_OR) {
            specialWeapon = SpecialWeapon.BANDOS_GODSWORD;
        }

        SpecialCounter counter = this.specialCounter[specialWeapon.ordinal()];
        BufferedImage image = this.itemManager.getImage(specialWeapon.getItemID());
        this.overlay.addOverlay(player, new SpecialIcon(image, Integer.toString(hit), System.currentTimeMillis()));
        if (counter == null) {
            counter = new SpecialCounter(image, this, hit, specialWeapon);
            this.infoBoxManager.addInfoBox(counter);
            this.specialCounter[specialWeapon.ordinal()] = counter;
        } else {
            counter.addHits((double)hit);
        }

        Map<String, Integer> partySpecs = counter.getPartySpecs();
        if (partySpecs.containsKey(name)) {
            partySpecs.put(name, hit + (Integer)partySpecs.get(name));
        } else {
            partySpecs.put(name, hit);
        }

    }

    private void removeCounters() {
        this.interactedNpcIds.clear();

        for(int i = 0; i < this.specialCounter.length; ++i) {
            SpecialCounter counter = this.specialCounter[i];
            if (counter != null) {
                this.infoBoxManager.removeInfoBox(counter);
                this.specialCounter[i] = null;
            }
        }

    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC actor = npcDespawned.getNpc();
        if (this.lastSpecTarget == actor) {
            this.lastSpecTarget = null;
        }

        if (actor.isDead() && this.interactedNpcIds.contains(actor.getId())) {
            this.removeCounters();
        }

    }
}
