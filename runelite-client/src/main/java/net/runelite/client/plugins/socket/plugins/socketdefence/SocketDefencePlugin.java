/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.IndexDataBase
 *  net.runelite.api.NPC
 *  net.runelite.api.Skill
 *  net.runelite.api.SpritePixels
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.events.ActorDeath
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.GraphicChanged
 *  net.runelite.api.events.HitsplatApplied
 *  net.runelite.api.events.NpcSpawned
 *  net.runelite.api.events.ScriptPreFired
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.kit.KitType
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.game.SkillIconManager
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.plugins.PluginManager
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  net.runelite.client.ui.overlay.infobox.InfoBox
 *  net.runelite.client.ui.overlay.infobox.InfoBoxManager
 *  net.runelite.client.util.ColorUtil
 *  org.pf4j.Extension
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.socket.plugins.socketdefence;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.IndexDataBase;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.SpritePixels;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.kit.KitType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketMembersUpdate;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.socket.packet.SocketShutdown;
import net.runelite.client.plugins.socket.plugins.socketdefence.DefenceInfoBox;
import net.runelite.client.plugins.socket.plugins.socketdefence.SocketDefenceConfig;
import net.runelite.client.plugins.socket.plugins.socketdefence.SocketDefenceOverlay;
import net.runelite.client.plugins.socket.plugins.socketdefence.VulnerabilityInfoBox;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Socket - Defence", description="Shows defence level for different bosses after specs", tags={"socket", "pvm", "cox", "gwd", "corp", "tob"}, enabledByDefault=false)
@PluginDependency(value=SocketPlugin.class)
public class SocketDefencePlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SocketDefencePlugin.class);
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private EventBus eventBus;
    @Inject
    private SkillIconManager skillIconManager;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private SocketDefenceConfig config;
    @Inject
    private SocketDefenceOverlay overlay;
    @Inject
    ConfigManager configManager;
    @Inject
    PluginManager pluginManager;
    public ArrayList<String> socketPlayerNames = new ArrayList();
    public String specWep = "";
    public String boss = "";
    public double bossDef = -1.0;
    public DefenceInfoBox box = null;
    private VulnerabilityInfoBox vulnBox = null;
    public SpritePixels vuln = null;
    public boolean vulnHit;
    public boolean isInCm = false;
    public ArrayList<String> bossList = new ArrayList<String>(Arrays.asList("Abyssal Sire", "Callisto", "Cerberus", "Chaos Elemental", "Corporeal Beast", "General Graardor", "Giant Mole", "Kalphite Queen", "King Black Dragon", "K'ril Tsutsaroth", "Sarachnis", "Venenatis", "Vet'ion", "Vet'ion Reborn", "The Maiden of Sugadinti", "Pestilent Bloat", "Nylocas Vasilias", "Sotetseg", "Xarpus", "Great Olm (Left claw)", "Tekton", "Tekton (enraged)"));
    public boolean hmXarpus = false;
    private static final int MAIDEN_REGION = 12613;
    private static final int BLOAT_REGION = 13125;
    private static final int NYLOCAS_REGION = 13122;
    private static final int SOTETSEG_REGION = 13123;
    private static final int SOTETSEG_MAZE_REGION = 13379;
    private static final int XARPUS_REGION = 12612;
    private static final int VERZIK_REGION = 12611;
    boolean bloatDown = false;
    private boolean mirrorMode;

    protected void startUp() throws Exception {
        this.reset();
        this.overlayManager.add((Overlay)this.overlay);
    }

    protected void shutDown() throws Exception {
        this.reset();
        this.overlayManager.remove((Overlay)this.overlay);
    }

    protected void reset() {
        this.infoBoxManager.removeInfoBox((InfoBox)this.box);
        this.infoBoxManager.removeInfoBox((InfoBox)this.vulnBox);
        this.boss = "";
        this.bossDef = -1.0;
        this.specWep = "";
        this.box = null;
        this.vulnBox = null;
        this.vuln = null;
        this.vulnHit = false;
        this.isInCm = this.config.cm();
        this.bloatDown = false;
    }

    @Provides
    SocketDefenceConfig getConfig(ConfigManager configManager) {
        return (SocketDefenceConfig)configManager.getConfig(SocketDefenceConfig.class);
    }

    @Subscribe
    public void onScriptPreFired(ScriptPreFired scriptPreFired) {
        if (!this.specWep.contains("bgs") && !this.specWep.contains("dwh")) {
            return;
        }
        if (scriptPreFired.getScriptId() == 996) {
            int[] intStack = this.client.getIntStack();
            int intStackSize = this.client.getIntStackSize();
            int widgetId = intStack[intStackSize - 4];
            try {
                this.processXpDrop(widgetId);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processXpDrop(int widgetId) throws InterruptedException {
        Widget xpdrop = this.client.getWidget(WidgetInfo.TO_GROUP((int)widgetId), WidgetInfo.TO_CHILD((int)widgetId));
        if (xpdrop == null) {
            return;
        }
        Widget[] children = xpdrop.getChildren();
        Widget textWidget = children[0];
        String text = textWidget.getText();
        boolean isDamage = false;
        Actor interacted = Objects.requireNonNull(this.client.getLocalPlayer()).getInteracting();
        String targetName = interacted.getName();
        if (!(targetName.contains("Maiden") || targetName.contains("Sotetseg") || targetName.contains("Xarpus") || targetName.contains("Nylocas Vasilias"))) {
            return;
        }
        Optional<Plugin> o = this.pluginManager.getPlugins().stream().filter(p -> p.getName().equals("damagedrops")).findAny();
        if (o.isPresent() && this.pluginManager.isPluginEnabled(o.get())) {
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
        int weaponUsed = Objects.requireNonNull(this.client.getLocalPlayer()).getPlayerComposition().getEquipmentId(KitType.WEAPON);
        if (Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId).anyMatch(id -> id == 197 || id == 198 || id == 199)) {
            String name;
            if (this.client.getVarbitValue(4696) == 0) {
                if (this.client.getVarbitValue(4696) == 0) {
                    if (this.client.getVar(VarPlayer.ATTACK_STYLE) == 3) {
                        damage = isDamage ? Integer.parseInt(text) : Integer.parseInt(text);
                    }
                } else {
                    damage = isDamage ? Integer.parseInt(text) : Integer.parseInt(text) / 4;
                }
            } else {
                damage = isDamage ? Integer.parseInt(text) : (int)Math.round((double)Integer.parseInt(text) / 5.3333);
            }
            if ((name = this.client.getLocalPlayer().getInteracting().getName()) == null) {
                return;
            }
            JSONObject data = new JSONObject();
            data.put("boss", name);
            data.put("weapon", this.specWep);
            data.put("hit", damage);
            JSONObject payload = new JSONObject();
            payload.put("socketdefence", data);
            this.eventBus.post((Object)new SocketBroadcastPacket(payload));
            this.specWep = "";
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        if (event.getActor() != null && this.client.getLocalPlayer() != null && event.getActor().getName() != null) {
            int animation = event.getActor().getAnimation();
            if (event.getActor().getName().equals(this.client.getLocalPlayer().getName())) {
                this.specWep = animation == 1378 ? "dwh" : (animation == 7642 || animation == 7643 ? "bgs" : (animation == 2890 ? "arclight" : ""));
                if (animation == 1816 && this.boss.equalsIgnoreCase("sotetseg") && (this.isInOverWorld() || this.isInUnderWorld())) {
                    this.infoBoxManager.removeInfoBox((InfoBox)this.box);
                    this.bossDef = 250.0;
                }
            }
        }
        if (event.getActor() instanceof NPC && event.getActor().getName() != null && event.getActor().getName().equalsIgnoreCase("pestilent bloat")) {
            this.bloatDown = event.getActor().getAnimation() == 8082;
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        for (NPC n : this.client.getNpcs()) {
            if (n == null || n.getName() == null || !n.getName().equals(this.boss) || !n.isDead() && n.getHealthRatio() != 0) continue;
            JSONObject data = new JSONObject();
            data.put("boss", this.boss);
            data.put("player", this.client.getLocalPlayer().getName());
            JSONObject payload = new JSONObject();
            payload.put("socketdefencebossdead", data);
            this.eventBus.post((Object)new SocketBroadcastPacket(payload));
            this.reset();
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied event) {
        if (!(event.getActor() instanceof NPC)) {
            return;
        }
        NPC npc = (NPC)event.getActor();
        if (npc.getName().contains("Maiden") || npc.getName().contains("Sotetseg") || npc.getName().contains("Xarpus") || npc.getName().contains("Nylocas Vasilias")) {
            return;
        }
        if (!this.specWep.equals("") && event.getHitsplat().isMine() && event.getActor() instanceof NPC && event.getActor() != null && event.getActor().getName() != null && this.bossList.contains(event.getActor().getName())) {
            String name = event.getActor().getName().contains("Tekton") ? "Tekton" : event.getActor().getName();
            JSONObject data = new JSONObject();
            data.put("boss", name);
            data.put("weapon", this.specWep);
            data.put("hit", event.getHitsplat().getAmount());
            JSONObject payload = new JSONObject();
            payload.put("socketdefence", data);
            this.eventBus.post((Object)new SocketBroadcastPacket(payload));
            this.specWep = "";
        }
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        NPC npc = event.getNpc();
        this.hmXarpus = npc.getId() >= 10770 && npc.getId() <= 10772;
    }

    @Subscribe
    public void onActorDeath(ActorDeath event) {
        if (event.getActor() instanceof NPC && event.getActor().getName() != null && this.client.getLocalPlayer() != null && (event.getActor().getName().equals(this.boss) || event.getActor().getName().contains("Tekton") && this.boss.equals("Tekton"))) {
            JSONObject data = new JSONObject();
            data.put("boss", this.boss);
            data.put("player", this.client.getLocalPlayer().getName());
            JSONObject payload = new JSONObject();
            payload.put("socketdefencebossdead", data);
            this.eventBus.post((Object)new SocketBroadcastPacket(payload));
            this.reset();
        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            JSONObject payload = event.getPayload();
            if (payload.has("socketdefence")) {
                JSONObject data = payload.getJSONObject("socketdefence");
                String bossName = data.getString("boss");
                String weapon = data.getString("weapon");
                int hit = data.getInt("hit");
                if ((bossName.equals("Tekton") || bossName.contains("Great Olm")) && this.client.getVarbitValue(5432) != 1 || (bossName.contains("The Maiden of Sugadinti") || bossName.contains("Pestilent Bloat") || bossName.contains("Nylocas Vasilias") || bossName.contains("Sotetseg") || bossName.contains("Xarpus")) && this.client.getVarbitValue(6440) != 2) {
                    return;
                }
                if (this.boss.equals("") || this.bossDef == -1.0 || !this.boss.equals(bossName)) {
                    switch (bossName) {
                        case "Abyssal Sire": 
                        case "Sotetseg": 
                        case "General Graardor": {
                            this.bossDef = 250.0;
                            break;
                        }
                        case "Callisto": {
                            this.bossDef = 440.0;
                            break;
                        }
                        case "Cerberus": {
                            this.bossDef = 110.0;
                            break;
                        }
                        case "Chaos Elemental": 
                        case "K'ril Tsutsaroth": {
                            this.bossDef = 270.0;
                            break;
                        }
                        case "Corporeal Beast": {
                            this.bossDef = 310.0;
                            break;
                        }
                        case "Giant Mole": 
                        case "The Maiden of Sugadinti": {
                            this.bossDef = 200.0;
                            break;
                        }
                        case "Kalphite Queen": {
                            this.bossDef = 300.0;
                            break;
                        }
                        case "King Black Dragon": {
                            this.bossDef = 240.0;
                            break;
                        }
                        case "Sarachnis": {
                            this.bossDef = 150.0;
                            break;
                        }
                        case "Venenatis": {
                            this.bossDef = 490.0;
                            break;
                        }
                        case "Vet'ion": 
                        case "Vet'ion Reborn": {
                            this.bossDef = 395.0;
                            break;
                        }
                        case "Pestilent Bloat": {
                            this.bossDef = 100.0;
                            break;
                        }
                        case "Nylocas Vasilias": {
                            this.bossDef = 50.0;
                            break;
                        }
                        case "Xarpus": {
                            if (this.hmXarpus) {
                                this.bossDef = 200.0;
                                break;
                            }
                            this.bossDef = 250.0;
                            break;
                        }
                        case "Great Olm (Left claw)": {
                            this.bossDef = 175.0 * (1.0 + 0.01 * (double)(this.client.getVarbitValue(5424) - 1));
                            if (!this.isInCm) break;
                            this.bossDef *= 1.5;
                            break;
                        }
                        case "Tekton": {
                            this.bossDef = 205.0 * (1.0 + 0.01 * (double)(this.client.getVarbitValue(5424) - 1));
                            if (!this.isInCm) break;
                            this.bossDef *= 1.2;
                        }
                    }
                    this.boss = bossName;
                }
                if (weapon.equals("dwh")) {
                    if (hit == 0) {
                        if (this.client.getVarbitValue(5432) == 1 && this.boss.equals("Tekton")) {
                            this.bossDef -= this.bossDef * 0.05;
                        }
                    } else {
                        this.bossDef -= this.bossDef * 0.3;
                    }
                } else if (weapon.equals("bgs")) {
                    if (hit == 0) {
                        if (this.client.getVarbitValue(5432) == 1 && this.boss.equals("Tekton")) {
                            this.bossDef -= 10.0;
                        }
                    } else {
                        this.bossDef = this.boss.equals("Corporeal Beast") || this.isInBloat() && this.boss.equals("Pestilent Bloat") && !this.bloatDown ? (this.bossDef -= (double)(hit * 2)) : (this.bossDef -= (double)hit);
                    }
                } else if (weapon.equals("arclight") && hit > 0) {
                    this.bossDef = this.boss.equals("K'ril Tsutsaroth") ? (this.bossDef -= this.bossDef * 0.1) : (this.bossDef -= this.bossDef * 0.05);
                } else if (weapon.equals("vuln")) {
                    if (this.config.vulnerability()) {
                        this.infoBoxManager.removeInfoBox((InfoBox)this.vulnBox);
                        IndexDataBase sprite = this.client.getIndexSprites();
                        this.vuln = this.client.getSprites(sprite, 56, 0)[0];
                        this.vulnBox = new VulnerabilityInfoBox(this.vuln.toBufferedImage(), this);
                        this.vulnBox.setTooltip(ColorUtil.wrapWithColorTag((String)this.boss, (Color)Color.WHITE));
                        this.infoBoxManager.addInfoBox((InfoBox)this.vulnBox);
                    }
                    this.vulnHit = true;
                    this.bossDef -= this.bossDef * 0.1;
                }
                if (this.bossDef < -1.0) {
                    this.bossDef = 0.0;
                }
                this.infoBoxManager.removeInfoBox((InfoBox)this.box);
                this.box = new DefenceInfoBox(this.skillIconManager.getSkillImage(Skill.DEFENCE), this, Math.round(this.bossDef), this.config);
                this.box.setTooltip(ColorUtil.wrapWithColorTag((String)this.boss, (Color)Color.WHITE));
                this.infoBoxManager.addInfoBox((InfoBox)this.box);
            } else if (payload.has("socketdefencebossdead")) {
                String bossName;
                JSONObject data = payload.getJSONObject("socketdefencebossdead");
                if (this.client.getLocalPlayer() != null && !data.getString("player").equals(this.client.getLocalPlayer().getName()) && (bossName = data.getString("boss")).equals(this.boss)) {
                    this.reset();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    void onSocketMembersUpdate(SocketMembersUpdate event) {
        this.socketPlayerNames.clear();
        this.socketPlayerNames.addAll(event.getMembers());
    }

    @Subscribe
    private void onSocketShutdown(SocketShutdown event) {
        this.socketPlayerNames.clear();
    }

    @Subscribe
    private void onVarbitChanged(VarbitChanged event) {
        if (this.client.getVarbitValue(5432) != 1 && (this.boss.equals("Tekton") || this.boss.equals("Great Olm (Left claw)")) || this.boss.equals("The Maiden of Sugadinti") && !this.isInMaiden() || this.boss.equals("Pestilent Bloat") && !this.isInBloat() || this.boss.equals("Nylocas Vasilias") && !this.isInNylo() || this.boss.equals("Sotetseg") && !this.isInOverWorld() && !this.isInUnderWorld() || this.boss.equals("Xarpus") && !this.isInXarpus()) {
            this.reset();
        }
    }

    @Subscribe
    public void onGraphicChanged(GraphicChanged event) {
        if (event.getActor().getName() != null && event.getActor().getGraphic() == 169 && this.bossList.contains(event.getActor().getName())) {
            this.boss = event.getActor().getName().contains("Tekton") ? "Tekton" : event.getActor().getName();
            JSONObject data = new JSONObject();
            data.put("boss", this.boss);
            data.put("weapon", "vuln");
            data.put("hit", 0);
            JSONObject payload = new JSONObject();
            payload.put("socketdefence", data);
            this.eventBus.post((Object)new SocketBroadcastPacket(payload));
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e) {
        this.isInCm = this.config.cm();
    }

    private boolean isInMaiden() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 12613;
    }

    private boolean isInBloat() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13125;
    }

    private boolean isInNylo() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13122;
    }

    private boolean isInOverWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13123;
    }

    private boolean isInUnderWorld() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 13379;
    }

    private boolean isInXarpus() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 12612;
    }

    private boolean isInVerzik() {
        return this.client.getMapRegions().length > 0 && this.client.getMapRegions()[0] == 12611;
    }
}

