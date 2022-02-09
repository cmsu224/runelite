//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.pvpplayerindicators;

import java.awt.Color;
import java.util.Iterator;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.Player;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.clan.ClanTitle;
import net.runelite.client.plugins.pvpplayerindicators.TargetHighlightMode;
import net.runelite.client.plugins.pvpplayerindicators.utils.PvpUtil;

@Singleton
public class PvPPlayerIndicatorsService {
    private final Client client;
    private final PvPPlayerIndicatorsConfig config;

    @Inject
    private PvPPlayerIndicatorsService(Client client, PvPPlayerIndicatorsConfig config) {
        this.config = config;
        this.client = client;
    }

    public void forEachPlayer(BiConsumer<Player, Color> consumer) {
        if (this.config.highlightOwnPlayer() || this.config.drawFriendsChatMemberNames() || this.config.highlightFriends() || this.config.highlightOthers() || this.config.highlightTargets() != TargetHighlightMode.OFF || this.config.highlightClanMembers()) {
            Player localPlayer = this.client.getLocalPlayer();
            Iterator var3 = this.client.getPlayers().iterator();

            while(true) {
                while(true) {
                    Player player;
                    do {
                        do {
                            if (!var3.hasNext()) {
                                return;
                            }

                            player = (Player)var3.next();
                        } while(player == null);
                    } while(player.getName() == null);

                    boolean isFriendsChatMember = player.isFriendsChatMember();
                    boolean isClanMember = player.isClanMember();
                    if (player == localPlayer) {
                        if (this.config.highlightOwnPlayer()) {
                            consumer.accept(player, this.config.getOwnPlayerColor());
                        }
                    } else if (this.config.highlightFriends() && this.client.isFriended(player.getName(), false)) {
                        consumer.accept(player, this.config.getFriendColor());
                    } else if (this.config.drawFriendsChatMemberNames() && isFriendsChatMember) {
                        consumer.accept(player, this.config.getFriendsChatMemberColor());
                    } else if (this.config.highlightTeamMembers() && localPlayer.getTeam() > 0 && localPlayer.getTeam() == player.getTeam()) {
                        consumer.accept(player, this.config.getTeamMemberColor());
                    } else if (this.config.highlightClanMembers() && isClanMember) {
                        consumer.accept(player, this.config.getClanMemberColor());
                    } else if (this.config.highlightOthers() && !isFriendsChatMember && !isClanMember) {
                        consumer.accept(player, this.config.getOthersColor());
                    } else if (this.config.highlightTargets() != TargetHighlightMode.OFF && PvpUtil.isAttackable(this.client, player) && !this.client.isFriended(player.getName(), false) && !player.isFriendsChatMember()) {
                        consumer.accept(player, this.config.getTargetColor());
                    }
                }
            }
        }
    }

    ClanTitle getClanTitle(Player player) {
        ClanChannel clanChannel = this.client.getClanChannel();
        ClanSettings clanSettings = this.client.getClanSettings();
        if (clanChannel != null && clanSettings != null) {
            ClanChannelMember member = clanChannel.findMember(player.getName());
            if (member == null) {
                return null;
            } else {
                ClanRank rank = member.getRank();
                return clanSettings.titleForRank(rank);
            }
        } else {
            return null;
        }
    }

    FriendsChatRank getFriendsChatRank(Player player) {
        FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
        if (friendsChatManager == null) {
            return FriendsChatRank.UNRANKED;
        } else {
            FriendsChatMember friendsChatMember = (FriendsChatMember)friendsChatManager.findByName(player.getName());
            return friendsChatMember != null ? friendsChatMember.getRank() : FriendsChatRank.UNRANKED;
        }
    }
}
