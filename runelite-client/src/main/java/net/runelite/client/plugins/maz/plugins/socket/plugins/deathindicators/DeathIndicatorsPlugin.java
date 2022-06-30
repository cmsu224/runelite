package net.runelite.client.plugins.maz.plugins.socket.plugins.deathindicators;


import com.google.inject.Provides;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.BLEntity;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.VarPlayer;
import net.runelite.api.Hitsplat.HitsplatType;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.kit.KitType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "Socket - Death Indicators",
        description = "Shows you NPCs that have been killed",
        tags = {"Socket, death, kill"},
        enabledByDefault = false
)
public class DeathIndicatorsPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(DeathIndicatorsPlugin.class);
    @Inject
    private DeathIndicatorsConfig config;
    @Inject
    ConfigManager configManager;
    @Inject
    PluginManager pluginManager;
    @Inject
    private DeathIndicatorsOverlay overlay;
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private EventBus eventBus;
    private ArrayList<NyloQ> nylos;
    private ArrayList<Method> reflectedMethods;
    private ArrayList<Plugin> reflectedPlugins;
    private ArrayList<NPC> deadNylos;
    private NyloQ maidenNPC;
    private int partySize;
    private boolean inNylo = false;

    public DeathIndicatorsPlugin() {
    }

    @Provides
    DeathIndicatorsConfig getConfig(ConfigManager configManager) {
        return (DeathIndicatorsConfig)configManager.getConfig(DeathIndicatorsConfig.class);
    }

    protected void startUp() {
        this.deadNylos = new ArrayList();
        this.overlayManager.add(this.overlay);
        this.nylos = new ArrayList();
        this.reflectedMethods = new ArrayList();
        this.reflectedPlugins = new ArrayList();
        Iterator var1 = this.pluginManager.getPlugins().iterator();

        while(var1.hasNext()) {
            Plugin p = (Plugin)var1.next();
            Method m = null;

            try {
                m = p.getClass().getDeclaredMethod("SocketDeathIntegration", Integer.TYPE);
            } catch (NoSuchMethodException var5) {
                continue;
            }

            this.reflectedMethods.add(m);
            this.reflectedPlugins.add(p);
        }

    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        int smSmallHP = -1;
        int smBigHP = -1;
        int bigHP = -1;
        int smallHP = -1;
        int maidenHP = -1;
        if (this.partySize == 1) {
            bigHP = 16;
            smallHP = 8;
            maidenHP = 2625;
            smSmallHP = 2;
            smBigHP = 3;
        } else if (this.partySize == 2) {
            bigHP = 16;
            smallHP = 8;
            maidenHP = 2625;
            smSmallHP = 4;
            smBigHP = 6;
        } else if (this.partySize == 3) {
            bigHP = 16;
            smallHP = 8;
            maidenHP = 2625;
            smSmallHP = 6;
            smBigHP = 9;
        } else if (this.partySize == 4) {
            bigHP = 19;
            smallHP = 9;
            maidenHP = 3062;
            smSmallHP = 8;
            smBigHP = 12;
        } else if (this.partySize == 5) {
            bigHP = 22;
            smallHP = 11;
            maidenHP = 3500;
            smSmallHP = 10;
            smBigHP = 15;
        }

        int id = event.getNpc().getId();
        switch(id) {
            case 8342:
            case 8343:
            case 8344:
            case 10791:
            case 10792:
            case 10793:
                this.nylos.add(new NyloQ(event.getNpc(), 0, smallHP));
                break;
            case 8345:
            case 8346:
            case 8347:
            case 8351:
            case 8352:
            case 8353:
            case 10783:
            case 10784:
            case 10785:
            case 10794:
            case 10795:
            case 10796:
            case 10800:
            case 10801:
            case 10802:
                this.nylos.add(new NyloQ(event.getNpc(), 0, bigHP));
                break;
            case 8360:
                NyloQ maidenTemp = new NyloQ(event.getNpc(), 0, maidenHP);
                this.nylos.add(maidenTemp);
                this.maidenNPC = maidenTemp;
                break;
            case 10774:
            case 10775:
            case 10776:
                this.nylos.add(new NyloQ(event.getNpc(), 0, smSmallHP));
                break;
            case 10777:
            case 10778:
            case 10779:
                this.nylos.add(new NyloQ(event.getNpc(), 0, smBigHP));
        }

    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        if (this.nylos.size() != 0) {
            this.nylos.removeIf((q) -> {
                return q.npc.equals(event.getNpc());
            });
        }

        if (this.deadNylos.size() != 0) {
            this.deadNylos.removeIf((q) -> {
                return q.equals(event.getNpc());
            });
        }

    }

    @Subscribe
    public void onScriptPreFired(ScriptPreFired scriptPreFired) {
        if (this.inNylo) {
            if (scriptPreFired.getScriptId() == 996) {
                int[] intStack = this.client.getIntStack();
                int intStackSize = this.client.getIntStackSize();
                int widgetId = intStack[intStackSize - 4];

                try {
                    this.processXpDrop(widgetId);
                } catch (InterruptedException var6) {
                    var6.printStackTrace();
                }
            }

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

    private void postHit(int index, int dmg) {
        JSONArray data = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("index", index);
        message.put("damage", dmg);
        data.put(message);
        JSONObject send = new JSONObject();
        send.put("sDeath", data);
        this.eventBus.post(new SocketBroadcastPacket(send));
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        if (this.inNylo) {
            Iterator var2 = this.nylos.iterator();

            while(true) {
                while(true) {
                    NyloQ q;
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        q = (NyloQ)var2.next();
                    } while(!hitsplatApplied.getActor().equals(q.npc));

                    if (hitsplatApplied.getHitsplat().getHitsplatType().equals(HitsplatType.HEAL)) {
                        q.hp += hitsplatApplied.getHitsplat().getAmount();
                    } else {
                        q.hp -= hitsplatApplied.getHitsplat().getAmount();
                    }

                    q.queuedDamage -= hitsplatApplied.getHitsplat().getAmount();
                    if (q.hp <= 0) {
                        NyloQ finalQ = q;
                        this.deadNylos.removeIf((o) -> {
                            return o.equals(finalQ.npc);
                        });
                    } else if (q.npc.getId() == 8360 || q.npc.getId() == 8361 || q.npc.getId() == 8362 || q.npc.getId() == 8363) {
                        double percent = (double)q.hp / (double)q.maxHP;
                        if (percent < 0.7D) {
                            q.phase = 1;
                        }

                        if (percent < 0.5D) {
                            q.phase = 2;
                        }

                        if (percent < 0.3D) {
                            q.phase = 3;
                        }
                    }
                }
            }
        }
    }

    private void hideNyloQ(NPC npc, boolean hide) {
        try {
            if (npc instanceof BLEntity) {
                ((BLEntity)npc).setHidden(hide);
            }
        } catch (AbstractMethodError var4) {
        }

    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        if (this.inNylo) {
            try {
                JSONObject payload = event.getPayload();
                if (payload.has("sDeath")) {
                    JSONArray data = payload.getJSONArray("sDeath");
                    JSONObject jsonmsg = data.getJSONObject(0);
                    int index = jsonmsg.getInt("index");
                    int damage = jsonmsg.getInt("damage");
                    Iterator var7 = this.nylos.iterator();

                    while(true) {
                        NyloQ q;
                        do {
                            do {
                                if (!var7.hasNext()) {
                                    return;
                                }

                                q = (NyloQ)var7.next();
                            } while(q.npc.getIndex() != index);

                            q.queuedDamage += damage;
                            NyloQ finalQ = q;
                            if (q.hp - q.queuedDamage <= 0 && this.deadNylos.stream().noneMatch((o) -> {
                                return o.getIndex() == finalQ.npc.getIndex();
                            })) {
                                this.deadNylos.add(q.npc);
                                if (this.config.hideNylo()) {
                                    this.hideNyloQ(q.npc, true);
                                    q.hidden = true;
                                    if (this.reflectedPlugins.size() == this.reflectedMethods.size()) {
                                        for(int i = 0; i < this.reflectedPlugins.size(); ++i) {
                                            try {
                                                Method tm = (Method)this.reflectedMethods.get(i);
                                                tm.setAccessible(true);
                                                tm.invoke(this.reflectedPlugins.get(i), q.npc.getIndex());
                                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ExceptionInInitializerError | NullPointerException var11) {
                                                Logger var10000 = log;
                                                Object var10001 = this.reflectedPlugins.get(i);
                                                var10000.debug("Failed on plugin: " + ((Plugin)var10001).getName());
                                            }
                                        }
                                    }
                                }
                            }
                        } while(q.npc.getId() != 8360 && q.npc.getId() != 8361 && q.npc.getId() != 8362 && q.npc.getId() != 8363);

                        double percent = ((double)q.hp - (double)q.queuedDamage) / (double)q.maxHP;
                        if (percent < 0.7D) {
                            q.phase = 1;
                        }

                        if (percent < 0.5D) {
                            q.phase = 2;
                        }

                        if (percent < 0.3D) {
                            q.phase = 3;
                        }
                    }
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }
        }
    }

    void addToDamageQueue(int damage) {
        if (damage != -1) {
            Actor interacted = ((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getInteracting();
            if (interacted instanceof NPC) {
                NPC interactedNPC = (NPC)interacted;
                this.postHit(interactedNPC.getIndex(), damage);
            }

        }
    }

    private void processXpDrop(int widgetId) throws InterruptedException {
        Widget xpdrop = this.client.getWidget(WidgetInfo.TO_GROUP(widgetId), WidgetInfo.TO_CHILD(widgetId));
        if (xpdrop != null) {
            Widget[] children = xpdrop.getChildren();
            Widget textWidget = children[0];
            String text = textWidget.getText();
            boolean isDamage = false;
            Optional<Plugin> o = this.pluginManager.getPlugins().stream().filter((p) -> {
                return p.getName().equals("damagedrops");
            }).findAny();
            if (o.isPresent() && this.pluginManager.isPluginEnabled((Plugin)o.get())) {
                isDamage = this.configManager.getConfiguration("damagedrops", "replaceEXPDrop").equals("true");
            }

            if (text.contains("<")) {
                if (text.contains("<img=11>")) {
                    text = text.substring(9);
                }

                if (text.contains("<")) {
                    text = text.substring(0, text.indexOf("<"));
                }
            }

            int damage = -1;
            int weaponUsed = ((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getPlayerComposition().getEquipmentId(KitType.WEAPON);
            if (this.client.getLocalPlayer().getAnimation() != 1979) {
                if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch((id) -> {
                    return id == 202;
                })) {
                    if (weaponUsed == 22323 || weaponUsed == 11905 || weaponUsed == 11907 || weaponUsed == 12899 || weaponUsed == 22292 || weaponUsed == 25731) {
                        if (this.client.getVarbitValue(4696) == 0) {
                            if (this.client.getVar(VarPlayer.ATTACK_STYLE) != 3) {
                                if (isDamage) {
                                    damage = Integer.parseInt(text);
                                } else {
                                    damage = (int)((double)Integer.parseInt(text) / 2.0D);
                                }
                            }
                        } else if (this.client.getVar(VarPlayer.ATTACK_STYLE) == 3) {
                            if (isDamage) {
                                damage = Integer.parseInt(text);
                            } else {
                                damage = (int)Math.round((double)Integer.parseInt(text) / 3.6667D);
                            }
                        } else if (isDamage) {
                            damage = Integer.parseInt(text);
                        } else {
                            damage = (int)Math.round((double)Integer.parseInt(text) / 3.3334D);
                        }
                    }
                } else if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch((id) -> {
                    return id == 197 || id == 198 || id == 199;
                })) {
                    if (weaponUsed == 22325 || weaponUsed == 25739 || weaponUsed == 25736) {
                        return;
                    }

                    if (this.client.getVarbitValue(4696) == 0) {
                        if (weaponUsed != 22323 && weaponUsed != 11905 && weaponUsed != 11907 && weaponUsed != 12899 && weaponUsed != 22292 && weaponUsed != 25731) {
                            if (weaponUsed == 12006) {
                                if (this.client.getVar(VarPlayer.ATTACK_STYLE) == 1) {
                                    if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch((id) -> {
                                        return id == 197;
                                    })) {
                                        if (isDamage) {
                                            damage = Integer.parseInt(text);
                                        } else {
                                            damage = (int)Math.round(3.0D * (double)Integer.parseInt(text) / 4.0D);
                                        }
                                    }
                                } else if (isDamage) {
                                    damage = Integer.parseInt(text);
                                } else {
                                    damage = Integer.parseInt(text) / 4;
                                }
                            } else if (isDamage) {
                                damage = Integer.parseInt(text);
                            } else {
                                damage = Integer.parseInt(text) / 4;
                            }
                        } else {
                            if (this.client.getLocalPlayer().getAnimation() == 1979) {
                                return;
                            }

                            if (this.client.getVarbitValue(4696) == 0 && this.client.getVar(VarPlayer.ATTACK_STYLE) == 3) {
                                if (isDamage) {
                                    damage = Integer.parseInt(text);
                                } else {
                                    damage = Integer.parseInt(text);
                                }
                            }
                        }
                    } else if (isDamage) {
                        damage = Integer.parseInt(text);
                    } else {
                        damage = (int)Math.round((double)Integer.parseInt(text) / 5.3333D);
                    }
                } else if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch((id) -> {
                    return id == 200;
                })) {
                    if (weaponUsed == 11959) {
                        return;
                    }

                    if (this.client.getVarbitValue(4696) == 0) {
                        if (isDamage) {
                            damage = Integer.parseInt(text);
                        } else {
                            damage = (int)((double)Integer.parseInt(text) / 4.0D);
                        }
                    } else if (isDamage) {
                        damage = Integer.parseInt(text);
                    } else {
                        damage = (int)Math.round((double)Integer.parseInt(text) / 5.333D);
                    }
                }

                this.addToDamageQueue(damage);
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (!this.inNylo) {
            if (this.inRegion(13122)) {
                this.inNylo = true;
                this.partySize = 0;

                for(int i = 330; i < 335; ++i) {
                    if (this.client.getVarcStrValue(i) != null && !this.client.getVarcStrValue(i).equals("")) {
                        ++this.partySize;
                    }
                }
            }
        } else if (!this.inRegion(13122)) {
            this.inNylo = false;
        }

        Iterator var4 = this.nylos.iterator();

        while(var4.hasNext()) {
            NyloQ q = (NyloQ)var4.next();
            if (q.hidden) {
                ++q.hiddenTicks;
                if (q.npc.getHealthRatio() != 0 && q.hiddenTicks > 5) {
                    q.hiddenTicks = 0;
                    q.hidden = false;
                    this.hideNyloQ(q.npc, false);
                    this.deadNylos.removeIf((x) -> {
                        return x.equals(q.npc);
                    });
                }
            }
        }

    }

    public ArrayList<NPC> getDeadNylos() {
        return this.deadNylos;
    }

    public NyloQ getMaidenNPC() {
        return this.maidenNPC;
    }
}
