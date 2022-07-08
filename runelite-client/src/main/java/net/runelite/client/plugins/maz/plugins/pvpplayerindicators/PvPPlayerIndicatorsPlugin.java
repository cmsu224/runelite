//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators;

import com.google.inject.Provides;
import java.awt.Color;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.clan.ClanTitle;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.maz.plugins.pvpplayerindicators.utils.PvpUtil;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;

@PluginDescriptor(
        name = "Wildy Indicator",
        description = "Highlight players on-screen and/or on the minimap",
        tags = {"highlight", "minimap", "overlay", "players"},
        conflicts = {"Player Indicators"}
)
public class PvPPlayerIndicatorsPlugin extends Plugin {
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private PvPPlayerIndicatorsConfig config;
    @Inject
    private PvPPlayerIndicatorsOverlay playerIndicatorsOverlay;
    @Inject
    private PvPPlayerIndicatorsTileOverlay playerIndicatorsTileOverlay;
    @Inject
    private PvPPlayerIndicatorsTrueTile playerIndicatorsTrueTile;
    @Inject
    private PvPPlayerIndicatorsHullOverlay playerIndicatorsHullOverlay;
    @Inject
    private PvPPlayerIndicatorsMinimapOverlay playerIndicatorsMinimapOverlay;
    @Inject
    private PvPTargetHighlightOverlay targetHighlightOverlay;
    @Inject
    private PvPPlayerIndicatorsService playerIndicatorsService;
    @Inject
    private Client client;
    @Inject
    private ChatIconManager chatIconManager;
    private boolean mirrorMode;
    private LocalPoint GELocation = null;

    public PvPPlayerIndicatorsPlugin() {
    }

    @Provides
    PvPPlayerIndicatorsConfig provideConfig(ConfigManager configManager) {
        return (PvPPlayerIndicatorsConfig)configManager.getConfig(PvPPlayerIndicatorsConfig.class);
    }

    protected void startUp() throws Exception {
        this.overlayManager.add(this.playerIndicatorsOverlay);
        this.overlayManager.add(this.playerIndicatorsTileOverlay);
        this.overlayManager.add(this.playerIndicatorsTrueTile);
        this.overlayManager.add(this.playerIndicatorsHullOverlay);
        this.overlayManager.add(this.playerIndicatorsMinimapOverlay);
        this.overlayManager.add(this.targetHighlightOverlay);
    }

    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.playerIndicatorsOverlay);
        this.overlayManager.remove(this.playerIndicatorsTileOverlay);
        this.overlayManager.remove(this.playerIndicatorsTrueTile);
        this.overlayManager.remove(this.playerIndicatorsHullOverlay);
        this.overlayManager.remove(this.playerIndicatorsMinimapOverlay);
        this.overlayManager.remove(this.targetHighlightOverlay);
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        if (event.getGameObject().getId() == 10063) {
            this.GELocation = event.getTile().getLocalLocation();
        }

    }

    @Subscribe
    private void onPlayerSpawned(PlayerSpawned event) {
        if (this.config.playerAlertSound()) {
            Player player = event.getPlayer();
            if (!player.getName().equalsIgnoreCase(this.client.getLocalPlayer().getName().toLowerCase()) && PvpUtil.isAttackable(this.client, player) && !player.isFriendsChatMember() && !player.isFriend() && PvpUtil.isAttackable(this.client, this.client.getLocalPlayer())) {
                if (this.GELocation != null) {
                    LocalPoint playerloc = this.client.getLocalPlayer().getLocalLocation();
                    if (playerloc.distanceTo(this.GELocation) < 3000) {
                        System.out.println(playerloc.distanceTo(this.GELocation));
                        return;
                    }
                }

                System.out.println(this.config.playerAlertSoundVolume());
                this.client.playSoundEffect(3924, this.config.playerAlertSoundVolume());
            }
        }

    }

    @Subscribe
    public void onClientTick(ClientTick clientTick) {
        if (!this.client.isMenuOpen()) {
            MenuEntry[] menuEntries = this.client.getMenuEntries();
            boolean modified = false;
            MenuEntry[] var4 = menuEntries;
            int var5 = menuEntries.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                MenuEntry entry = var4[var6];
                int type = entry.getType().getId();
                if (type >= 2000) {
                    type -= 2000;
                }

                if (type == MenuAction.WALK.getId() || type == MenuAction.ITEM_USE_ON_PLAYER.getId() || type == MenuAction.PLAYER_FIRST_OPTION.getId() || type == MenuAction.PLAYER_SECOND_OPTION.getId() || type == MenuAction.PLAYER_THIRD_OPTION.getId() || type == MenuAction.PLAYER_FOURTH_OPTION.getId() || type == MenuAction.PLAYER_FIFTH_OPTION.getId() || type == MenuAction.PLAYER_SIXTH_OPTION.getId() || type == MenuAction.PLAYER_SEVENTH_OPTION.getId() || type == MenuAction.PLAYER_EIGTH_OPTION.getId() || type == MenuAction.RUNELITE_PLAYER.getId()) {
                    Player[] players = this.client.getCachedPlayers();
                    Player player = null;
                    int identifier = entry.getIdentifier();
                    if (type == MenuAction.WALK.getId()) {
                        --identifier;
                    }

                    if (identifier >= 0 && identifier < players.length) {
                        player = players[identifier];
                    }

                    if (player != null) {
                        Decorations decorations = this.getDecorations(player);
                        if (decorations != null) {
                            String oldTarget = entry.getTarget();
                            String newTarget = this.decorateTarget(oldTarget, decorations);
                            entry.setTarget(newTarget);
                            modified = true;
                        }
                    }
                }
            }

            if (modified) {
                this.client.setMenuEntries(menuEntries);
            }
        }

    }

    private Decorations getDecorations(Player player) {
        int image = -1;
        Color color = null;
        if (this.config.highlightFriends() && this.client.isFriended(player.getName(), false)) {
            color = this.config.getFriendColor();
        } else if (this.config.drawFriendsChatMemberNames() && player.isFriendsChatMember()) {
            color = this.config.getFriendsChatMemberColor();
            FriendsChatRank rank = this.playerIndicatorsService.getFriendsChatRank(player);
            if (rank != FriendsChatRank.UNRANKED) {
                image = this.chatIconManager.getIconNumber(rank);
            }
        } else if (this.config.highlightTeamMembers() && player.getTeam() > 0 && this.client.getLocalPlayer().getTeam() == player.getTeam()) {
            color = this.config.getTeamMemberColor();
        } else if (player.isClanMember() && this.config.highlightClanMembers()) {
            color = this.config.getClanMemberColor();
            if (this.config.showClanChatRanks()) {
                ClanTitle clanTitle = this.playerIndicatorsService.getClanTitle(player);
                if (clanTitle != null) {
                    image = this.chatIconManager.getIconNumber(clanTitle);
                }
            }
        } else if (this.config.highlightOthers() && !player.isFriendsChatMember() && !player.isClanMember()) {
            color = this.config.getOthersColor();
        } else if (PvpUtil.isAttackable(this.client, player) && !player.isFriendsChatMember() && !player.isFriend()) {
            color = this.config.getTargetColor();
        }

        return image == -1 && color == null ? null : new Decorations(image, color);
    }

    private String decorateTarget(String oldTarget, Decorations decorations) {
        String newTarget = oldTarget;
        int var10000;
        if (decorations.getColor() != null && this.config.colorPlayerMenu()) {
            var10000 = oldTarget.indexOf(62);
            if (var10000 != -1) {
                newTarget = oldTarget.substring(var10000 + 1);
            }

            newTarget = ColorUtil.prependColorTag(newTarget, decorations.getColor());
        }

        if (decorations.getImage() != -1 && this.config.showFriendsChatRanks()) {
            var10000 = decorations.getImage();
            newTarget = "<img=" + var10000 + ">" + newTarget;
        }

        return newTarget;
    }
}
