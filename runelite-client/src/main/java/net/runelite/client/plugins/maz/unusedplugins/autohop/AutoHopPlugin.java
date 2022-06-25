//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.unusedplugins.autohop;

import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.World;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.WorldResult;
import net.runelite.http.api.worlds.WorldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "[S] Auto hop",
        description = "Automatically hops away from people",
        enabledByDefault = false
)
public class AutoHopPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(AutoHopPlugin.class);
    private static final int DISPLAY_SWITCHER_MAX_ATTEMPTS = 3;
    private static final int GRAND_EXCHANGE_REGION = 12598;
    @Inject
    private Client client;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private WorldService worldService;
    @Inject
    private ClientThread clientThread;
    @Inject
    private AutoHopConfig config;
    private World quickHopTargetWorld;
    private int displaySwitcherAttempts = 0;

    public AutoHopPlugin() {
    }

    @Provides
    AutoHopConfig getConfig(ConfigManager configManager) {
        return (AutoHopConfig)configManager.getConfig(AutoHopConfig.class);
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        Player local = this.client.getLocalPlayer();
        if (event.getGameState() == GameState.LOGGED_IN && local != null && (!this.config.disableGrandExchange() || local.getWorldLocation().getRegionID() != 12598)) {
            Iterator var3 = this.client.getPlayers().iterator();

            while(true) {
                while(true) {
                    Player player;
                    do {
                        do {
                            do {
                                do {
                                    if (!var3.hasNext()) {
                                        return;
                                    }

                                    player = (Player)var3.next();
                                } while(player == null);
                            } while(player.equals(local));
                        } while(this.config.cmbBracket() && !PvPUtil.isAttackable(this.client, player));
                    } while(this.config.hopRadius() && player.getWorldLocation().distanceTo(local.getWorldLocation()) > this.config.playerRadius());

                    if (this.config.alwaysHop()) {
                        this.shouldHop(player);
                    } else if (this.config.underHop() && local.getWorldLocation() == player.getWorldLocation()) {
                        this.shouldHop(player);
                    } else if (this.config.skulledHop() && player.getSkullIcon() != null) {
                        this.shouldHop(player);
                    }
                }
            }
        }
    }

    @Subscribe
    private void onPlayerSpawned(PlayerSpawned event) {
        Player local = this.client.getLocalPlayer();
        Player player = event.getPlayer();
        if (local != null && player != null && !player.equals(local) && (!this.config.cmbBracket() || PvPUtil.isAttackable(this.client, player))) {
            if (this.config.alwaysHop()) {
                this.shouldHop(player);
            } else if (this.config.underHop() && local.getWorldLocation() == player.getWorldLocation()) {
                this.shouldHop(player);
            } else if (this.config.skulledHop() && player.getSkullIcon() != null) {
                this.shouldHop(player);
            }

        }
    }

    private void shouldHop(Player player) {
        if ((!this.config.friends() || !player.isFriend()) && (!this.config.clanmember() || !player.isFriendsChatMember()) && (!this.config.hopRadius() || player.getWorldLocation().distanceTo(this.client.getLocalPlayer().getWorldLocation()) <= this.config.playerRadius()) && (!this.config.disableGrandExchange() || player.getWorldLocation().getRegionID() != 12598)) {
            this.hop();
        }
    }

    private net.runelite.http.api.worlds.World findWorld(List<net.runelite.http.api.worlds.World> worlds, EnumSet<WorldType> currentWorldTypes, int totalLevel) {
        net.runelite.http.api.worlds.World world = (net.runelite.http.api.worlds.World)worlds.get((new Random()).nextInt(worlds.size()));
        EnumSet<WorldType> types = world.getTypes().clone();
        types.remove(WorldType.LAST_MAN_STANDING);
        int worldLocation;
        if (types.contains(WorldType.SKILL_TOTAL)) {
            try {
                worldLocation = Integer.parseInt(world.getActivity().substring(0, world.getActivity().indexOf(" ")));
                if (totalLevel >= worldLocation) {
                    types.remove(WorldType.SKILL_TOTAL);
                }
            } catch (NumberFormatException var7) {
                log.warn("Failed to parse total level requirement for target world", var7);
            }
        }

        if (currentWorldTypes.equals(types)) {
            worldLocation = world.getLocation();
            if (this.config.american() && worldLocation == 0) {
                return world;
            }

            if (this.config.unitedkingdom() && worldLocation == 1) {
                return world;
            }

            if (this.config.australia() && worldLocation == 3) {
                return world;
            }

            if (this.config.germany() && worldLocation == 7) {
                return world;
            }
        }

        return null;
    }

    private void hop() {
        WorldResult worldResult = this.worldService.getWorlds();
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
                this.client.runScript(new Object[]{915, 10});
                String chatMessage = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Hopping away from a player. New world: ").append(ChatColorType.HIGHLIGHT).append(Integer.toString(world.getId())).append(ChatColorType.NORMAL).append("..").build();
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
                this.quickHopTargetWorld = rsWorld;
                this.displaySwitcherAttempts = 0;
            }
        }
    }

    private void pressKey() {
        KeyEvent keyPress = new KeyEvent(this.client.getCanvas(), 401, System.currentTimeMillis(), 0, 32);
        this.client.getCanvas().dispatchEvent(keyPress);
        KeyEvent keyRelease = new KeyEvent(this.client.getCanvas(), 402, System.currentTimeMillis(), 0, 32);
        this.client.getCanvas().dispatchEvent(keyRelease);
        KeyEvent keyTyped = new KeyEvent(this.client.getCanvas(), 400, System.currentTimeMillis(), 0, 32);
        this.client.getCanvas().dispatchEvent(keyTyped);
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (this.config.autoCloseChatbox() && (this.client.getVar(Varbits.IN_WILDERNESS) == 1 || net.runelite.api.WorldType.isPvpWorld(this.client.getWorldType()))) {
            Widget x = this.client.getWidget(162, 56);
            Widget y = this.client.getWidget(162, 53);
            if (y != null && !y.isHidden() && !y.isSelfHidden() && (x == null || x.isHidden() || x.isSelfHidden())) {
                Executors.newSingleThreadExecutor().submit(this::pressKey);
            }
        }

        if (this.quickHopTargetWorld != null) {
            if (this.client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST) == null) {
                this.client.openWorldHopper();
                if (++this.displaySwitcherAttempts >= 3) {
                    String chatMessage = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Failed to quick-hop after ").append(ChatColorType.HIGHLIGHT).append(Integer.toString(this.displaySwitcherAttempts)).append(ChatColorType.NORMAL).append(" attempts.").build();
                    this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
                    this.resetQuickHopper();
                }
            } else {
                this.client.hopToWorld(this.quickHopTargetWorld);
                this.resetQuickHopper();
            }

        }
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        Player local = this.client.getLocalPlayer();
        String eventName = Text.sanitize(event.getName());
        if ((!this.config.disableGrandExchange() || local.getWorldLocation().getRegionID() != 12598) && (event.getType() == ChatMessageType.GAMEMESSAGE || this.config.chatHop() && event.getType() == ChatMessageType.PUBLICCHAT && eventName != local.getName() || local.getName() == null)) {
            if (event.getMessage().equals("Please finish what you're doing before using the World Switcher.")) {
                this.resetQuickHopper();
            } else {
                if (this.config.chatHop() && event.getType() == ChatMessageType.PUBLICCHAT && !eventName.equals(this.client.getLocalPlayer().getName()) && this.client.getLocalPlayer().getName() != null) {
                    log.info("Chat message found -> Hopping");
                    this.hop();
                }

            }
        }
    }

    private void resetQuickHopper() {
        this.displaySwitcherAttempts = 0;
        this.quickHopTargetWorld = null;
    }
}
