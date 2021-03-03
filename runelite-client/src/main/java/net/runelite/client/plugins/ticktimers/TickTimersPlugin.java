//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.ticktimers;

import com.google.common.collect.UnmodifiableIterator;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.NPCManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "[Maz] Boss Tick Timers",
        enabledByDefault = false,
        description = "Tick timers for bosses",
        tags = {"pvm", "bossing"}
)
public class TickTimersPlugin extends Plugin {
    private static final int GENERAL_REGION = 11347;
    private static final int ARMA_REGION = 11346;
    private static final int SARA_REGION = 11602;
    private static final int ZAMMY_REGION = 11603;
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private TimersOverlay timersOverlay;
    @Inject
    private TickTimersConfig config;
    @Inject
    private NPCManager npcManager;
    private Set<NPCContainer> npcContainers = new HashSet();
    private boolean validRegion;
    private long lastTickTime;

    public TickTimersPlugin() {
    }

    @Provides
    TickTimersConfig getConfig(ConfigManager configManager) {
        return (TickTimersConfig)configManager.getConfig(TickTimersConfig.class);
    }

    public void startUp() {
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            if (this.regionCheck()) {
                this.npcContainers.clear();
                Iterator var1 = this.client.getNpcs().iterator();

                while(var1.hasNext()) {
                    NPC npc = (NPC)var1.next();
                    this.addNpc(npc);
                }

                this.validRegion = true;
                this.overlayManager.add(this.timersOverlay);
            } else if (!this.regionCheck()) {
                this.validRegion = false;
                this.overlayManager.remove(this.timersOverlay);
                this.npcContainers.clear();
            }

        }
    }

    public void shutDown() {
        this.npcContainers.clear();
        this.overlayManager.remove(this.timersOverlay);
        this.validRegion = false;
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
            if (this.regionCheck()) {
                this.npcContainers.clear();
                Iterator var2 = this.client.getNpcs().iterator();

                while(var2.hasNext()) {
                    NPC npc = (NPC)var2.next();
                    this.addNpc(npc);
                }

                this.validRegion = true;
                this.overlayManager.add(this.timersOverlay);
            } else if (!this.regionCheck()) {
                this.validRegion = false;
                this.overlayManager.remove(this.timersOverlay);
                this.npcContainers.clear();
            }

        }
    }

    @Subscribe
    private void onNpcSpawned(NpcSpawned event) {
        if (this.validRegion) {
            this.addNpc(event.getNpc());
        }
    }

    @Subscribe
    private void onNpcDespawned(NpcDespawned event) {
        if (this.validRegion) {
            this.removeNpc(event.getNpc());
        }
    }

    @Subscribe
    public void onGameTick(GameTick Event) {
        this.lastTickTime = System.currentTimeMillis();
        if (this.validRegion) {
            this.handleBosses();
        }
    }

    private void handleBosses() {
        Iterator var1 = this.getNpcContainers().iterator();

        while(var1.hasNext()) {
            NPCContainer npc = (NPCContainer)var1.next();
            npc.setNpcInteracting(npc.getNpc().getInteracting());
            if (npc.getTicksUntilAttack() >= 0) {
                npc.setTicksUntilAttack(npc.getTicksUntilAttack() - 1);
            }

            UnmodifiableIterator var3 = npc.getAnimations().iterator();

            while(var3.hasNext()) {
                int animation = (Integer)var3.next();
                if (animation == npc.getNpc().getAnimation() && npc.getTicksUntilAttack() < 1) {
                    npc.setTicksUntilAttack(npc.getAttackSpeed());
                }
            }
        }

    }

    private boolean regionCheck() {
        return Arrays.stream(this.client.getMapRegions()).anyMatch((x) -> {
            return x == 11346 || x == 11347 || x == 11603 || x == 11602;
        });
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("TickTimers")) {
            if (event.getKey().equals("mirrorMode") && this.regionCheck()) {
                this.overlayManager.remove(this.timersOverlay);
                this.overlayManager.add(this.timersOverlay);
            }

        }
    }

    private void addNpc(NPC npc) {
        if (npc != null) {
            switch(npc.getId()) {
                //god wars commanders
                case 2205:
                case 2215:
                case 3129:
                case 3162:
                    if (this.config.gwd()) {
                        this.npcContainers.add(new NPCContainer(npc, 6));
                    }
                    //god wars minions
                case 2206:
                case 2207:
                case 2208:
                case 2216:
                case 2217:
                case 2218:
                case 3130:
                case 3131:
                case 3132:
                case 3163:
                case 3164:
                case 3165:
                    if (this.config.gwd()) {
                        this.npcContainers.add(new NPCContainer(npc, 5));
                    }

                    //case 2205:
                    //case 2206:
                    //case 2207:
                    //case 2208:
                    //case 2215:
                    //case 2216:
                    //case 2217:
                    //case 2218:
                    //case 3129:
                    //case 3130:
                    //case 3131:
                    //case 3132:
                    //case 3162:
                    //case 3163:
                    //case 3164:
                    //case 3165:
                    //if (this.config.gwd()) {
                    //this.npcContainers.add(new NPCContainer(npc, this.npcManager.getAttackSpeed(npc.getId())));
                    //}
                default:
            }
        }
    }

    private void removeNpc(NPC npc) {
        if (npc != null) {
            switch(npc.getId()) {
                case 2205:
                case 2206:
                case 2207:
                case 2208:
                case 2215:
                case 2216:
                case 2217:
                case 2218:
                case 3129:
                case 3130:
                case 3131:
                case 3132:
                case 3162:
                case 3163:
                case 3164:
                case 3165:
                    this.npcContainers.removeIf((c) -> {
                        return c.getNpc() == npc;
                    });
                default:
            }
        }
    }

    Set<NPCContainer> getNpcContainers() {
        return this.npcContainers;
    }

    long getLastTickTime() {
        return this.lastTickTime;
    }
}
