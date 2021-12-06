package net.runelite.client.plugins.objectnotifier;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.Provides;

import java.util.*;
import javax.inject.Inject;

import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.WorldResult;
import net.runelite.http.api.worlds.WorldType;
import net.runelite.client.game.ItemManager;

@PluginDescriptor(
        name = "[Maz] Object Notifier",
        description = "Notifies you when the desired items spawns. Optional config settings to send notification on object spawning.",
        tags = {"object, notifier"},
        enabledByDefault = false
)
public class ObjectNotifierPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    private ObjectNotifierConfig config;
    @Inject
    private Notifier notifier;
    @Inject
    private ClientThread clientThread;
    @Inject
    private WorldService worldService;
    private World quickHopTargetWorld;
    public List<String> ObjectNames;
    @Inject
    private ItemManager itemManager;
    @Getter
    private final Table<WorldPoint, Integer, TileItem> collectedGroundItems = HashBasedTable.create();

    public ObjectNotifierPlugin() {
    }

    @Provides
    ObjectNotifierConfig getConfig(ConfigManager configManager) {
        return (ObjectNotifierConfig)configManager.getConfig(ObjectNotifierConfig.class);
    }

    protected void startUp() throws Exception {
        this.ObjectNames = this.parseConfigList(this.config.objectNames());
    }

    protected void shutDown() throws Exception {
        collectedGroundItems.clear();
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned itemSpawned)
    {
        TileItem item = itemSpawned.getItem();
        ItemComposition itemComposition = itemManager.getItemComposition(item.getId());

        String test2 = this.ObjectNames.toString();
        String test = itemComposition.getName();

        if (item != null && this.ObjectNames.contains(itemComposition.getName().toLowerCase()) && this.config.objectSpawnNotifier()){
            this.notifier.notify("[" + itemComposition.getName() + "] has spawned!");
        }

        collectedGroundItems.put(itemSpawned.getTile().getWorldLocation(), item.getId(), item);
    }

    @Subscribe
    public void onItemDespawned(ItemDespawned itemDespawned)
    {
        TileItem item = itemDespawned.getItem();
        Tile tile = itemDespawned.getTile();

        TileItem groundItem = collectedGroundItems.get(tile.getWorldLocation(), item.getId());
        if (groundItem == null)
        {
            return;
        }

        if (groundItem.getQuantity() <= item.getQuantity())
        {
            collectedGroundItems.remove(tile.getWorldLocation(), item.getId());
        }
    }

    @Subscribe
    public void onGameStateChanged(final GameStateChanged event)
    {
        if (event.getGameState() == GameState.LOADING)
        {
            collectedGroundItems.clear();
        }
    }

    private boolean shouldHop() {
        boolean hop = true;
        Iterator var2 = this.client.getNpcs().iterator();
        Collection<TileItem> groundItemList = getCollectedGroundItems().values();
        groundItemList = new ArrayList<>(groundItemList);
        for (TileItem item : groundItemList)
        {
            ItemComposition itemComposition = itemManager.getItemComposition(item.getId());
            if(item != null && itemComposition.getName() != null && this.ObjectNames.size() > 0 && this.ObjectNames.contains(itemComposition.getName().toLowerCase())){hop = false;}
        }

        if (!hop) {
            return false;
        } else {
            this.hop();
            return true;
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.shouldHop() && this.config.autoWorldHop()) {
            this.client.openWorldHopper();
            this.client.hopToWorld(this.quickHopTargetWorld);
            this.resetQuickHopper();
        }

    }

    private void resetQuickHopper() {
        this.quickHopTargetWorld = null;
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (configChanged.getGroup().equals("npcnotifier") && configChanged.getKey().equals("npcList")) {
            this.ObjectNames = this.parseConfigList(this.config.objectNames());
        }

    }

    private void hop() {
        WorldResult worldResult = worldService.getWorlds();
        if (worldResult != null && this.client.getGameState() == GameState.LOGGED_IN) {
            net.runelite.http.api.worlds.World currentWorld = worldResult.findWorld(this.client.getWorld());
            if (currentWorld != null) {
                EnumSet<WorldType> currentWorldTypes = currentWorld.getTypes().clone();
                currentWorldTypes.remove(WorldType.PVP);
                currentWorldTypes.remove(WorldType.HIGH_RISK);
                currentWorldTypes.remove(WorldType.BOUNTY);
                currentWorldTypes.remove(WorldType.SKILL_TOTAL);
                currentWorldTypes.remove(WorldType.LAST_MAN_STANDING);
                List<net.runelite.http.api.worlds.World> worlds = worldResult.getWorlds();
                int totalLevel = this.client.getTotalLevel();

                net.runelite.http.api.worlds.World world;
                do {
                    do {
                        world = this.findWorld(worlds, currentWorldTypes, totalLevel);
                    } while(world == null);
                } while(world == currentWorld);

                this.hop(world.getId());
            }
        }
    }

    private void hop(int worldId) {
        WorldResult worldResult = this.worldService.getWorlds();
        net.runelite.http.api.worlds.World world = worldResult.findWorld(worldId);
        if (world != null) {
            World rsWorld = this.client.createWorld();
            rsWorld.setActivity(world.getActivity());
            rsWorld.setAddress(world.getAddress());
            rsWorld.setId(world.getId());
            rsWorld.setPlayerCount(world.getPlayers());
            rsWorld.setLocation(world.getLocation());
            rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));
            if (this.client.getGameState() == GameState.LOGIN_SCREEN) {
                this.client.changeWorld(rsWorld);
            } else {
                this.quickHopTargetWorld = rsWorld;
            }
        }
    }

    private net.runelite.http.api.worlds.World findWorld(List<net.runelite.http.api.worlds.World> worlds, EnumSet<WorldType> currentWorldTypes, int totalLevel) {
        net.runelite.http.api.worlds.World world = (net.runelite.http.api.worlds.World)worlds.get((new Random()).nextInt(worlds.size()));
        EnumSet<WorldType> types = world.getTypes().clone();
        types.remove(WorldType.LAST_MAN_STANDING);
        if (types.contains(WorldType.SKILL_TOTAL)) {
            try {
                int totalRequirement = Integer.parseInt(world.getActivity().substring(0, world.getActivity().indexOf(" ")));
                if (totalLevel >= totalRequirement) {
                    types.remove(WorldType.SKILL_TOTAL);
                }
            } catch (NumberFormatException var7) {
                System.out.println("Failed to parse total level requirement for target world");
            }
        }

        return currentWorldTypes.equals(types) ? world : null;
    }

    private List<String> parseConfigList(String playerList) {
        ArrayList<String> returnList = new ArrayList();
        String[] var3 = playerList.split(",");
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String s = var3[var5];
            returnList.add(s.toLowerCase());
        }

        return returnList;
    }
}
