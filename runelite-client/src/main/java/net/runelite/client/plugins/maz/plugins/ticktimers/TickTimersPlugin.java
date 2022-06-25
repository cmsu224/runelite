//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.ticktimers;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.inject.Provides;

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
    Set<String> gameNPCsWhitelist;

    public TickTimersPlugin() {
    }

    @Provides
    TickTimersConfig getConfig(ConfigManager configManager) {
        return (TickTimersConfig)configManager.getConfig(TickTimersConfig.class);
    }

    public void startUp() {
        this.gameNPCsWhitelist = new HashSet();
        this.parse_list(this.gameNPCsWhitelist, this.config.gameTickOverlay());
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
        return true;
        //return Arrays.stream(this.client.getMapRegions()).anyMatch((x) -> {
            //return x == 11346 || x == 11347 || x == 11603 || x == 11602;
        //});
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("TickTimers")) {
            this.gameNPCsWhitelist.clear();
            this.parse_list(this.gameNPCsWhitelist, this.config.gameTickOverlay());
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
                    parse_string(gameNPCsWhitelist, npc.getId(), npc);
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
                    this.npcContainers.removeIf((c) -> {
                        return c.getNpc() == npc;
                    });
            }
        }
    }

    Set<NPCContainer> getNpcContainers() {
        return this.npcContainers;
    }

    long getLastTickTime() {
        return this.lastTickTime;
    }

    private void parse_list(Set<String> list, String src) {
        String[] split = src.split(",");

        for(int i = 0; i < split.length; ++i) {
            String s = split[i].trim();

            try {
                //int n = Integer.parseInt(n);
                list.add(s);
            } catch (NumberFormatException var7) {
            }
        }

    }

    private boolean parse_string(Set<String> str, int hlID, NPC npc){
        String tID = "";
        String junk = "";

        for(String strMain : str){
            String[] sID = strMain.split(",");
            tID = sID[0].trim();
            if(tID.equals("")){
                break;
            }
            if(tID.contains("/")){
                sID = tID.split("/");
                tID = sID[0].trim();
            }
            if(tID.contains("-")){
                sID = strMain.split("-");
                tID = sID[0].trim();
            }

            try {
                if(Integer.parseInt(tID) == hlID){
                    set_vars(strMain, npc);
                    return true;
                }
            } catch (NumberFormatException ignored) {
            }

        }
        return false;
    }

    private void set_vars(String src, NPC npc) {
        String[] split = src.split("/");
        ImmutableSet<Integer> animations = new ImmutableSet.Builder<Integer>().build();

        NPCContainer.AttackStyle style = NPCContainer.AttackStyle.UNKNOWN;
        int attackSpeed = 4;

        //attack style
        for(int i = 1; i < split.length; ++i) {
            String s = split[i].trim();
            String[] s1 = s.split("@");
            int c = Integer.parseInt(s1[0]);

            attackSpeed = Integer.parseInt(s1[0]);

        }

        //attack speed
        String[] split2 = src.split("-");
        for(int i = 0; i < split2.length; ++i) {
            if(i == 1){
                String s = split2[i].trim();
                String[] split3 = s.split("/");
                String[] split4 = split3[1].split("@");

                int c = Integer.parseInt(split3[0]);
                try {
                    if(c == 1){
                        style = NPCContainer.AttackStyle.MELEE;
                    }else if(c == 2){
                        style = NPCContainer.AttackStyle.MAGE;
                    }else if(c == 3){
                        style = NPCContainer.AttackStyle.RANGE;
                    }
                } catch (NumberFormatException ignored) {
                }

                for(int j = 1; j < split2.length; ++j) {
                    int a1 = Integer.parseInt(split4[j]);
                    animations = new ImmutableSet.Builder<Integer>()
                            .addAll(animations)
                            .add(a1)
                            .build();
                }

            }
        }

        this.npcContainers.add(new NPCContainer(npc, attackSpeed, style, animations));

    }

    public static <T> Set<T> setWith(Set<T> old, T item) {
        return new ImmutableSet.Builder<T>().addAll(old).add(item).build();
    }

}
