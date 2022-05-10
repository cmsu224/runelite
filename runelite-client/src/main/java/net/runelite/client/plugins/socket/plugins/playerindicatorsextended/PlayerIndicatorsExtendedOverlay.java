/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.FriendsChatManager
 *  net.runelite.api.FriendsChatMember
 *  net.runelite.api.FriendsChatRank
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.clan.ClanChannel
 *  net.runelite.api.clan.ClanChannelMember
 *  net.runelite.api.clan.ClanRank
 *  net.runelite.api.clan.ClanSettings
 *  net.runelite.api.clan.ClanTitle
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.game.ChatIconManager
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.OverlayUtil
 *  net.runelite.client.util.Text
 */
package net.runelite.client.plugins.socket.plugins.playerindicatorsextended;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.clan.ClanTitle;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.plugins.socket.plugins.playerindicatorsextended.PlayerIndicatorsExtendedConfig;
import net.runelite.client.plugins.socket.plugins.playerindicatorsextended.PlayerIndicatorsExtendedPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.Text;

public class PlayerIndicatorsExtendedOverlay
extends Overlay {
    private final Client client;
    private final PlayerIndicatorsExtendedPlugin plugin;
    private final PlayerIndicatorsExtendedConfig config;
    private final ChatIconManager chatIconManager;
    @Inject
    ConfigManager configManager;

    @Inject
    private PlayerIndicatorsExtendedOverlay(Client client, PlayerIndicatorsExtendedPlugin plugin, PlayerIndicatorsExtendedConfig config, ChatIconManager chatIconManager) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.chatIconManager = chatIconManager;
        this.setPriority(OverlayPriority.HIGH);
        this.setPosition(OverlayPosition.DYNAMIC);
    }

    public Dimension render(Graphics2D graphics) {
        for (Actor actor : this.plugin.getPlayers()) {
            ClanTitle clanTitle;
            Player p = (Player)actor;
            int zOffset = actor.getLogicalHeight() + 40;
            String name = Text.sanitize((String)actor.getName());
            Point textLocation = actor.getCanvasTextLocation(graphics, name, zOffset);
            if (textLocation == null) {
                return null;
            }
            BufferedImage rankImage = null;
            if (p.isFriendsChatMember() && this.configManager.getConfiguration("playerindicators", "drawClanMemberNames").equals("true") && this.configManager.getConfiguration("playerindicators", "clanMenuIcons").equals("true")) {
                FriendsChatRank rank = this.getFriendsChatRank(actor);
                if (rank != FriendsChatRank.UNRANKED) {
                    rankImage = this.chatIconManager.getRankImage(rank);
                }
            } else if (p.isClanMember() && this.configManager.getConfiguration("playerindicators", "drawClanChatMemberNames").equals("true") && this.configManager.getConfiguration("playerindicators", "clanchatMenuIcons").equals("true") && (clanTitle = this.getClanTitle(actor)) != null) {
                rankImage = this.chatIconManager.getRankImage(clanTitle);
            }
            if (rankImage != null) {
                int imageWidth = rankImage.getWidth();
                int imageTextMargin = imageWidth / 2;
                int imageNegativeMargin = imageWidth / 2;
                int textHeight = graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getMaxDescent();
                Point imageLocation = new Point(textLocation.getX() - imageNegativeMargin - 1, textLocation.getY() - textHeight / 2 - rankImage.getHeight() / 2);
                OverlayUtil.renderImageLocation((Graphics2D)graphics, (Point)imageLocation, (BufferedImage)rankImage);
                textLocation = new Point(textLocation.getX() + imageTextMargin, textLocation.getY());
            }
            OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)textLocation, (String)name, (Color)this.config.nameColor());
        }
        return null;
    }

    ClanTitle getClanTitle(Actor player) {
        ClanChannel clanChannel = this.client.getClanChannel();
        ClanSettings clanSettings = this.client.getClanSettings();
        if (clanChannel == null || clanSettings == null) {
            return null;
        }
        ClanChannelMember member = clanChannel.findMember(player.getName());
        if (member == null) {
            return null;
        }
        ClanRank rank = member.getRank();
        return clanSettings.titleForRank(rank);
    }

    FriendsChatRank getFriendsChatRank(Actor actor) {
        FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
        if (friendsChatManager == null) {
            return FriendsChatRank.UNRANKED;
        }
        FriendsChatMember friendsChatMember = (FriendsChatMember)friendsChatManager.findByName(actor.getName());
        return friendsChatMember != null ? friendsChatMember.getRank() : FriendsChatRank.UNRANKED;
    }
}

