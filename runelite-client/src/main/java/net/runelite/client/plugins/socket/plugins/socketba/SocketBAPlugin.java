/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.GameState
 *  net.runelite.api.GroundObject
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.NPC
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.ClientTick
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.GroundObjectDespawned
 *  net.runelite.api.events.GroundObjectSpawned
 *  net.runelite.api.events.HitsplatApplied
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.NpcSpawned
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.events.WidgetLoaded
 *  net.runelite.api.util.Text
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  org.pf4j.Extension
 */
package net.runelite.client.plugins.socket.plugins.socketba;

import com.google.common.collect.ArrayListMultimap;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.GroundObject;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.socket.plugins.socketba.AttackStyle;
import net.runelite.client.plugins.socket.plugins.socketba.SocketBAConfig;
import net.runelite.client.plugins.socket.plugins.socketba.SocketBAItemOverlay;
import net.runelite.client.plugins.socket.plugins.socketba.SocketBAOverlay;
import net.runelite.client.plugins.socket.plugins.socketba.SocketBAPanelOverlay;
import net.runelite.client.plugins.socket.plugins.socketba.WeaponType;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

@PluginDescriptor(name="Socket - Barbarian Assault", description="Socket BA", tags={"ba", "barb assault", "spoon", "spoonlite"}, enabledByDefault=false)
@PluginDependency(value=SocketPlugin.class)
public class SocketBAPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SocketBAOverlay overlay;
    @Inject
    private SocketBAPanelOverlay panelOverlay;
    @Inject
    private SocketBAItemOverlay itemOverlay;
    @Inject
    private SocketBAConfig config;
    @Inject
    private EventBus eventBus;
    private final ArrayListMultimap<String, Integer> optionIndexes = ArrayListMultimap.create();
    public NPC queen = null;
    public String role = "";
    public String otherRole = "";
    public String attCall = "";
    public String defCall = "";
    public String colCall = "";
    public String healCall = "";
    public boolean roleDone = false;
    public boolean fightersDead = false;
    public boolean rangersDead = false;
    public ArrayList<String> hornList = new ArrayList<String>(Arrays.asList("attacker horn", "healer horn", "defender horn", "collector horn"));
    public int arrowEquiped = 0;
    private int attackStyleVarbit = -1;
    public int equippedWeaponTypeVarbit = -1;
    public AttackStyle attackStyle;
    public String roleWidgetText = "";
    public ArrayList<GameObject> vendingMachines = new ArrayList();
    public Map<GameObject, Color> eggHoppers = new HashMap<GameObject, Color>();
    public Map<NPC, Color> cannons = new HashMap<NPC, Color>();
    public double cannonWidth = 1.0;
    public boolean cannonWidthUp = true;
    public Map<GroundObject, Color> discoTiles = new HashMap<GroundObject, Color>();
    private final Predicate<MenuEntry> filterMenuEntries = entry -> {
        int id = entry.getIdentifier();
        String option = Text.standardize(entry.getOption(), true).toLowerCase();
        String target = Text.standardize(entry.getTarget(), true).toLowerCase();
        int type = entry.getType().getId();
        if (this.config.leftClickEggs() && type >= 18 && type <= 22 && (id == 10531 || id == 10532 || id == 10533 || id == 10534) && (!this.role.equals("Collector") || this.colCall.equalsIgnoreCase("Green eggs") && (id == 10532 || id == 10533) || this.colCall.equalsIgnoreCase("Red eggs") && (id == 10531 || id == 10533) || this.colCall.equalsIgnoreCase("Blue eggs") && (id == 10531 || id == 10532))) {
            return false;
        }
        if (this.config.hideAttack() && (target.contains("penance fighter") || target.contains("penance ranger"))) {
            if (option.contains("attack")) {
                WeaponType wepType = WeaponType.getWeaponType(this.equippedWeaponTypeVarbit);
                if (!this.role.equals("Attacker")) {
                    return false;
                }
                if (wepType == WeaponType.TYPE_3 || wepType == WeaponType.TYPE_5 || wepType == WeaponType.TYPE_7 || wepType == WeaponType.TYPE_19 ? this.arrowEquiped == 22227 && !this.attCall.contains("Controlled") || this.arrowEquiped == 22228 && !this.attCall.contains("Accurate") || this.arrowEquiped == 22229 && !this.attCall.contains("Aggressive") || this.arrowEquiped == 22230 && !this.attCall.contains("Defensive") : !this.attCall.contains(this.attackStyle.getName())) {
                    return false;
                }
            } else if (option.contains("cast ") && target.contains(" -> ") && (!this.role.equals("Attacker") || option.contains(" wind ") && !this.attCall.contains("Controlled") || option.contains(" water ") && !this.attCall.contains("Accurate") || option.contains(" earth ") && !this.attCall.contains("Aggressive") || option.contains(" fire ") && !this.attCall.contains("Defensive"))) {
                return false;
            }
        }
        if (this.config.hideAttack() && option.contains("attack") && target.contains("penance queen")) {
            return false;
        }
        if (this.config.removeUseFood() && option.contains("use") && target.contains("poisoned ") && (target.contains(" meat ->") || target.contains(" tofu ->") || target.contains(" worms ->")) && !target.contains("penance healer") && this.role.equals("Healer")) {
            return false;
        }
        return !this.config.highlightVendingMachine() || !option.contains("stock-up") && !option.contains("take-") && !option.contains("convert") || !target.contains(" item machine") && !target.contains("collector converter") || !(this.role.equals("Attacker") && !target.contains("attacker item machine") || this.role.equals("Defender") && !target.contains("defender item machine") || this.role.equals("Healer") && !target.contains("healer item machine")) && (this.role.equals("Collector") || !target.contains("collector converter"));
    };

    @Provides
    SocketBAConfig getConfig(ConfigManager configManager) {
        return (SocketBAConfig)configManager.getConfig(SocketBAConfig.class);
    }

    protected void startUp() throws Exception {
        this.overlayManager.add((Overlay)this.overlay);
        this.overlayManager.add((Overlay)this.panelOverlay);
        this.overlayManager.add((Overlay)this.itemOverlay);
        this.reset();
        this.attCall = "";
        this.colCall = "";
        this.healCall = "";
        this.defCall = "";
        this.roleWidgetText = "";
        this.vendingMachines.clear();
        this.cannons.clear();
        this.eggHoppers.clear();
        this.discoTiles.clear();
    }

    protected void shutDown() throws Exception {
        this.overlayManager.remove((Overlay)this.overlay);
        this.overlayManager.remove((Overlay)this.panelOverlay);
        this.overlayManager.remove((Overlay)this.itemOverlay);
        this.reset();
        this.attCall = "";
        this.colCall = "";
        this.healCall = "";
        this.defCall = "";
        this.roleWidgetText = "";
        this.attackStyleVarbit = -1;
        this.equippedWeaponTypeVarbit = -1;
        this.vendingMachines.clear();
        this.cannons.clear();
        this.eggHoppers.clear();
    }

    protected void reset() {
        this.role = "";
        this.otherRole = "";
        this.roleDone = false;
        this.fightersDead = false;
        this.rangersDead = false;
        this.arrowEquiped = 0;
        this.roleWidgetText = "";
        this.queen = null;
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        Widget hpWidget;
        if (event.getGroup().equals("socketBa") && this.client.getVarbitValue(3923) == 1 && (hpWidget = this.client.getWidget(WidgetInfo.BA_HEAL_TEAMMATES.getGroupId(), WidgetInfo.BA_HEAL_TEAMMATES.getChildId())) != null && event.getKey().equals("hideHpOverlay")) {
            hpWidget.setHidden(this.config.hideHpOverlay());
        }
    }

    @Subscribe
    private void onGameObjectSpawned(GameObjectSpawned event) {
        int id = event.getGameObject().getId();
        if (id == 20241 || id == 20242 || id == 20243) {
            this.vendingMachines.removeIf(obj -> obj.getId() == id);
            this.vendingMachines.add(event.getGameObject());
        } else if (id == 20267) {
            this.eggHoppers.remove((Object)event.getGameObject());
            this.eggHoppers.put(event.getGameObject(), Color.getHSBColor(new Random().nextFloat(), 1.0f, 1.0f));
        }
    }

    @Subscribe
    private void onGroundObjectSpawned(GroundObjectSpawned event) {
        if (event.getGroundObject().getId() >= 20136 && event.getGroundObject().getId() <= 20147) {
            this.discoTiles.put(event.getGroundObject(), Color.getHSBColor(new Random().nextFloat(), 1.0f, 1.0f));
        }
    }

    @Subscribe
    private void onGroundObjectDespawned(GroundObjectDespawned event) {
        if (event.getGroundObject().getId() >= 20136 && event.getGroundObject().getId() <= 20147) {
            this.discoTiles.remove((Object)event.getGroundObject());
        }
    }

    /*
     * WARNING - void declaration
     */
    @Subscribe
    private void onGameTick(GameTick event) {
        if (this.client.getVarbitValue(3923) == 1) {
            for (Map.Entry<GameObject, Color> entry : this.eggHoppers.entrySet()) {
                entry.setValue(Color.getHSBColor(new Random().nextFloat(), 1.0f, 1.0f));
            }
            for (Map.Entry<NPC, Color> entry : this.cannons.entrySet()) {
                entry.setValue(Color.getHSBColor(new Random().nextFloat(), 1.0f, 1.0f));
            }
            for (Map.Entry<GroundObject, Color> entry : this.discoTiles.entrySet()) {
                entry.setValue(Color.getHSBColor(new Random().nextFloat(), 1.0f, 1.0f));
            }
            if (!this.role.equals("") && !this.otherRole.equals("") && this.client.getLocalPlayer() != null) {
                String var3_13 = null;
                Widget otherWidget = null;
                Object var3_8 = null;
                if (this.role.equals("Attacker")) {
                    otherWidget = this.client.getWidget(31784970);
                    Widget widget = this.client.getWidget(31784967);
                } else if (this.role.equals("Defender")) {
                    otherWidget = this.client.getWidget(31916041);
                    Widget widget = this.client.getWidget(31916039);
                } else if (this.role.equals("Healer")) {
                    otherWidget = this.client.getWidget(31981577);
                    Widget widget = this.client.getWidget(31981575);
                } else if (this.role.equals("Collector")) {
                    otherWidget = this.client.getWidget(31850505);
                    Widget widget = this.client.getWidget(31850503);
                }
                /*
                if (var3_13 != null && !var3_13.getText().equals(this.roleWidgetText) && !var3_13.getText().equals("- - -")) {
                    this.roleWidgetText = var3_13.getText();
                    if (this.role.equals("Attacker")) {
                        if (this.roleWidgetText.contains("Aggressive")) {
                            this.attCall = "Aggressive/Blunt/Earth";
                        } else if (this.roleWidgetText.contains("Accurate")) {
                            this.attCall = "Accurate/Field/Water";
                        } else if (this.roleWidgetText.contains("Controlled")) {
                            this.attCall = "Controlled/Bullet/Wind";
                        } else if (this.roleWidgetText.contains("Defensive")) {
                            this.attCall = "Defensive/Barbed/Fire";
                        }
                    } else if (this.role.equals("Defender")) {
                        this.defCall = this.roleWidgetText;
                    } else if (this.role.equals("Healer")) {
                        this.healCall = this.roleWidgetText;
                    } else if (this.role.equals("Collector")) {
                        this.colCall = this.roleWidgetText;
                    }
                }*/
                if (otherWidget != null) {
                    String otherCall = "";
                    if (this.role.equals("Attacker") && !this.colCall.equalsIgnoreCase(otherWidget.getText())) {
                        otherCall = this.colCall = otherWidget.getText();
                    } else if (this.role.equals("Defender") && !this.healCall.equalsIgnoreCase(otherWidget.getText())) {
                        otherCall = this.healCall = otherWidget.getText();
                    } else if (this.role.equals("Healer") && !this.defCall.equalsIgnoreCase(otherWidget.getText())) {
                        otherCall = this.defCall = otherWidget.getText();
                    } else if (this.role.equals("Collector") && !this.attCall.equalsIgnoreCase(otherWidget.getText())) {
                        otherCall = this.attCall = otherWidget.getText();
                    }
                    if (!otherCall.equals("")) {
                        JSONObject data = new JSONObject();
                        data.put("player", this.client.getLocalPlayer().getName());
                        data.put("role", this.otherRole);
                        data.put("call", otherCall);
                        JSONObject payload = new JSONObject();
                        payload.put("socketbarole", data);
                        this.eventBus.post((Object)new SocketBroadcastPacket(payload));
                    }
                }
            }
        }
    }

    @Subscribe
    private void onNpcSpawned(NpcSpawned event) {
        if (this.client.getVarbitValue(3923) == 1) {
            if (event.getNpc().getId() == 5775) {
                this.queen = event.getNpc();
            } else if (event.getNpc().getId() == 1655) {
                this.cannons.put(event.getNpc(), Color.getHSBColor(new Random().nextFloat(), 1.0f, 1.0f));
            }
        }
    }

    @Subscribe
    private void onNpcDespawned(NpcDespawned event) {
        if (this.client.getVarbitValue(3923) == 1) {
            if (event.getNpc().getId() == 5775) {
                this.queen = null;
            } else if (event.getNpc().getId() == 1655) {
                this.cannons.remove((Object)event.getNpc());
            }
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (this.client.getVarbitValue(3923) == 1 && event.getMenuOption().equalsIgnoreCase("wield") && (event.getItemId() == 22227 || event.getItemId() == 22228 || event.getItemId() == 22229 || event.getItemId() == 22230)) {
            this.arrowEquiped = event.getItemId();
        }
    }

    private void sendFlag(String msg) {
        JSONArray data = new JSONArray();
        JSONObject jsonmsg = new JSONObject();
        jsonmsg.put("msg", msg);
        data.put(jsonmsg);
        JSONObject send = new JSONObject();
        send.put("socketbaalt", data);
        this.eventBus.post((Object)new SocketBroadcastPacket(send));
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        if (this.client.getLocalPlayer() != null) {
            try {
                JSONObject payload = event.getPayload();
                if (payload.has("socketbarole")) {
                    JSONObject data = payload.getJSONObject("socketbarole");
                    if (!data.getString("player").equals(this.client.getLocalPlayer().getName())) {
                        if (data.getString("role").equals("Attacker")) {
                            this.attCall = data.getString("call");
                            if (this.attCall.contains("Defensive")) {
                                Objects.requireNonNull(this.client.getWidget(31784967)).setText("Defensive/");
                            } else if (this.attCall.contains("Aggressive")) {
                                Objects.requireNonNull(this.client.getWidget(31784967)).setText("Aggressive/");
                            } else if (this.attCall.contains("Accurate")) {
                                Objects.requireNonNull(this.client.getWidget(31784967)).setText("Accurate/");
                            } else {
                                Objects.requireNonNull(this.client.getWidget(31784967)).setText("Controlled/");
                            }
                        } else if (data.getString("role").equals("Defender")) {
                            this.defCall = data.getString("call");
                            Objects.requireNonNull(this.client.getWidget(31916039)).setText(this.defCall);
                        } else if (data.getString("role").equals("Healer")) {
                            this.healCall = data.getString("call");
                            Objects.requireNonNull(this.client.getWidget(31981575)).setText(this.healCall);
                        } else if (data.getString("role").equals("Collector")) {
                            this.colCall = data.getString("call");
                            Objects.requireNonNull(this.client.getWidget(31850503)).setText(this.colCall);
                        }
                    }
                } else if (payload.has("socketbaalt") && this.config.bmMessages()) {
                    JSONArray data = payload.getJSONArray("socketbaalt");
                    JSONObject jsonmsg = data.getJSONObject(0);
                    String msg = jsonmsg.getString("msg");
                    this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        if (event.getGroupId() == 485) {
            this.reset();
            this.role = "Attacker";
            this.otherRole = "Collector";
            this.colCall = "";
        } else if (event.getGroupId() == 487) {
            this.reset();
            this.role = "Defender";
            this.otherRole = "Healer";
            this.healCall = "";
        } else if (event.getGroupId() == 488) {
            this.reset();
            this.role = "Healer";
            this.otherRole = "Defender";
            this.defCall = "";
        } else if (event.getGroupId() == 486) {
            this.reset();
            this.role = "Collector";
            this.otherRole = "Attacker";
            this.attCall = "";
        }
        Widget hpWidget = this.client.getWidget(WidgetInfo.BA_HEAL_TEAMMATES.getGroupId(), WidgetInfo.BA_HEAL_TEAMMATES.getChildId());
        if (hpWidget != null) {
            hpWidget.setHidden(this.config.hideHpOverlay());
        }
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        if (event.getType() == ChatMessageType.GAMEMESSAGE && this.client.getVarbitValue(3923) == 1 && this.client.getLocalPlayer() != null) {
            String msg = Text.removeTags((String)event.getMessage());
            if (msg.toLowerCase().contains("all of the penance runners have been killed!") && this.role.equals("Defender") || msg.toLowerCase().contains("all of the penance healers have been killed!") && this.role.equals("Healer")) {
                this.roleDone = true;
            } else if (msg.toLowerCase().contains("all of the penance fighters have been killed!") && this.role.equals("Attack")) {
                this.fightersDead = true;
                if (this.rangersDead) {
                    this.roleDone = true;
                }
            } else if (msg.toLowerCase().contains("all of the penance rangers have been killed!") && this.role.equals("Attack")) {
                this.rangersDead = true;
                if (this.fightersDead) {
                    this.roleDone = true;
                }
            } else if (msg.equalsIgnoreCase("the egg exploded.")) {
                int rng = new Random().nextInt(3);
                if (rng == 0) {
                    this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " picked up the wrong egg.");
                } else if (rng == 1) {
                    this.sendFlag("<col=ff0000>... Really? Just click the right egg. " + this.client.getLocalPlayer().getName() + " has got no hands");
                } else {
                    this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " is colorblind");
                }
            } else if (msg.equalsIgnoreCase("that's the wrong type of poisoned food to use! penalty!")) {
                int rng = new Random().nextInt(3);
                if (rng == 0) {
                    this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " used the wrong poisoned food.");
                } else if (rng == 1) {
                    this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " has room temp IQ");
                } else {
                    this.sendFlag("<col=ff0000>Either they are greedy with the ticks.... or " + this.client.getLocalPlayer().getName() + " can't tell whats " + this.healCall.replace("Pois. ", ""));
                }
            }
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied event) {
        if (this.client.getVarbitValue(3923) == 1 && this.client.getLocalPlayer() != null && this.role.equals("Attacker") && event.getHitsplat().getAmount() == 0 && event.getActor() instanceof NPC && event.getHitsplat().isMine() && event.getActor().getName() != null && event.getActor().getName().contains("Penance ") && this.client.getLocalPlayer().getAnimation() != 7511) {
            int rng = new Random().nextInt(3);
            if (rng == 0) {
                this.sendFlag("<col=ff0000>" + this.client.getLocalPlayer().getName() + " is being a brainlet");
            } else if (rng == 1) {
                this.sendFlag("<col=ff0000>Hehe point go brrrrrrrrrrrrrrrrrrr  -" + this.client.getLocalPlayer().getName());
            } else {
                this.sendFlag("<col=ff0000>Just kick him now. " + this.client.getLocalPlayer().getName() + " is bing chillin'");
            }
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        int currentAttackStyleVarbit = this.client.getVar(VarPlayer.ATTACK_STYLE);
        int currentEquippedWeaponTypeVarbit = this.client.getVarbitValue(357);
        if (this.attackStyleVarbit != currentAttackStyleVarbit || this.equippedWeaponTypeVarbit != currentEquippedWeaponTypeVarbit) {
            this.attackStyleVarbit = currentAttackStyleVarbit;
            this.equippedWeaponTypeVarbit = currentEquippedWeaponTypeVarbit;
            this.updateAttackStyle(this.equippedWeaponTypeVarbit, this.attackStyleVarbit);
        }
    }

    private void updateAttackStyle(int equippedWeaponType, int attackStyleIndex) {
        AttackStyle[] attackStyles = WeaponType.getWeaponType(equippedWeaponType).getAttackStyles();
        if (attackStyleIndex < attackStyles.length) {
            this.attackStyle = attackStyles[attackStyleIndex];
            if (this.attackStyle == null) {
                this.attackStyle = AttackStyle.OTHER;
            }
        }
    }

    @Subscribe
    public void onClientTick(ClientTick clientTick) {
        if (this.client.getGameState() != GameState.LOGGED_IN || this.client.isMenuOpen()) {
            return;
        }
        if (this.client.getVarbitValue(3923) == 1) {
            if (this.cannonWidthUp) {
                this.cannonWidth += 0.02;
                if (this.cannonWidth >= 20.0) {
                    this.cannonWidthUp = false;
                }
            } else {
                this.cannonWidth -= 0.02;
                if (this.cannonWidth <= 1.0) {
                    this.cannonWidthUp = true;
                }
            }
            MenuEntry[] menuEntries = this.client.getMenuEntries();
            int idx = 0;
            this.optionIndexes.clear();
            for (MenuEntry entry : menuEntries) {
                String option = Text.removeTags((String)entry.getOption()).toLowerCase();
                this.optionIndexes.put(option, idx++);
            }
            idx = 0;
            for (MenuEntry entry : menuEntries) {
                this.swapMenuEntry(idx++, entry);
            }
            this.client.setMenuEntries(this.updateMenuEntries(this.client.getMenuEntries()));
        }
    }

    private void swapMenuEntry(int index, MenuEntry menuEntry) {
        String option = Text.removeTags((String)menuEntry.getOption()).toLowerCase();
        String target = Text.removeTags((String)menuEntry.getTarget()).toLowerCase();
        if (this.config.leftClickHorn() && option.contains("tell-") && this.hornList.contains(target)) {
            Object newSwap = "";
            if (this.role.equalsIgnoreCase("attacker")) {
                newSwap = "tell-" + this.colCall.substring(0, this.colCall.indexOf(" egg")).toLowerCase();
            } else if (this.role.equalsIgnoreCase("healer")) {
                newSwap = "tell-" + this.defCall.toLowerCase();
            } else if (this.role.equalsIgnoreCase("defender")) {
                newSwap = "tell-" + this.healCall.replace("Pois. ", "").toLowerCase();
            } else if (this.role.equalsIgnoreCase("collector")) {
                newSwap = "tell-" + this.attCall.substring(0, this.attCall.indexOf("/")).toLowerCase();
            }
            this.swap((String)newSwap, option, target, index, false);
        }
    }

    private void swap(String optionA, String optionB, String target, int index, boolean strict) {
        MenuEntry[] menuEntries = this.client.getMenuEntries();
        int thisIndex = this.findIndex(menuEntries, index, optionB, target, strict);
        int optionIdx = this.findIndex(menuEntries, thisIndex, optionA, target, strict);
        if (thisIndex >= 0 && optionIdx >= 0) {
            this.swap(this.optionIndexes, menuEntries, optionIdx, thisIndex);
        }
    }

    private int findIndex(MenuEntry[] entries, int limit, String option, String target, boolean strict) {
        if (strict) {
            List indexes = this.optionIndexes.get(option);
            for (int i = indexes.size() - 1; i >= 0; --i) {
                int idx = (Integer)indexes.get(i);
                MenuEntry entry = entries[idx];
                String entryTarget = Text.removeTags((String)entry.getTarget()).toLowerCase();
                if (idx > limit || !entryTarget.equals(target)) continue;
                return idx;
            }
        } else {
            for (int i = limit; i >= 0; --i) {
                MenuEntry entry = entries[i];
                String entryOption = Text.removeTags((String)entry.getOption()).toLowerCase();
                String entryTarget = Text.removeTags((String)entry.getTarget()).toLowerCase();
                if (!entryOption.contains(option.toLowerCase()) || !entryTarget.equals(target)) continue;
                return i;
            }
        }
        return -1;
    }

    private void swap(ArrayListMultimap<String, Integer> optionIndexes, MenuEntry[] entries, int index1, int index2) {
        MenuEntry entry = entries[index1];
        entries[index1] = entries[index2];
        entries[index2] = entry;
        this.client.setMenuEntries(entries);
        optionIndexes.clear();
        int idx = 0;
        for (MenuEntry menuEntry : entries) {
            String option = Text.removeTags((String)menuEntry.getOption()).toLowerCase();
            optionIndexes.put(option, idx++);
        }
    }

    private MenuEntry[] updateMenuEntries(MenuEntry[] menuEntries) {
        return (MenuEntry[])Arrays.stream(menuEntries).filter(this.filterMenuEntries).sorted((o1, o2) -> 0).toArray(MenuEntry[]::new);
    }
}

