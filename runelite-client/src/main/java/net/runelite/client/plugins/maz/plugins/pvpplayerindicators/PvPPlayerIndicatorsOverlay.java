//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Varbits;
import net.runelite.api.WorldType;
import net.runelite.api.clan.ClanTitle;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.hiscore.HiscoreEndpoint;
import net.runelite.client.hiscore.HiscoreManager;
import net.runelite.client.hiscore.HiscoreResult;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

@Singleton
public class PvPPlayerIndicatorsOverlay extends Overlay {
    private static final int ACTOR_OVERHEAD_TEXT_MARGIN = 40;
    private static final int ACTOR_HORIZONTAL_TEXT_MARGIN = 10;
    private final PvPPlayerIndicatorsService playerIndicatorsService;
    private final PvPPlayerIndicatorsConfig config;
    private final ChatIconManager chatIconManager;
    private final HiscoreManager hiscoreManager;
    private final BufferedImage agilityIcon = ImageUtil.loadImageResource(getClass(), "agility.png");
    private final BufferedImage noAgilityIcon = ImageUtil.loadImageResource(getClass(), "no-agility.png");
    @Inject
    private Client client;
    @Inject
    private ItemManager itemManager;

    @Inject
    private PvPPlayerIndicatorsOverlay(PvPPlayerIndicatorsConfig config, PvPPlayerIndicatorsService playerIndicatorsService, ChatIconManager chatIconManager, HiscoreManager hiscoreManager) {
        this.config = config;
        this.playerIndicatorsService = playerIndicatorsService;
        this.chatIconManager = chatIconManager;
        this.hiscoreManager = hiscoreManager;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.MED);
    }

    public Dimension render(Graphics2D graphics) {
        this.playerIndicatorsService.forEachPlayer((player, color) -> {
            this.renderPlayerOverlay(graphics, player, color);
        });
        return null;
    }

    private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color) {
        PvPPlayerNameLocation drawPlayerNamesConfig = this.config.playerNamePosition();
        if (drawPlayerNamesConfig != PvPPlayerNameLocation.DISABLED) {
            int zOffset = actor.getLogicalHeight() + 40;
            /*
            switch(1.$SwitchMap$net$runelite$client$plugins$pvpplayerindicators$PvPPlayerNameLocation[drawPlayerNamesConfig.ordinal()]) {
                case 1:
                case 2:
                    zOffset = actor.getLogicalHeight() / 2;
                    break;
                default:
                    zOffset = actor.getLogicalHeight() + 40;
            }*/

            String name = Text.sanitize(actor.getName());
            Point textLocation = actor.getCanvasTextLocation(graphics, name, zOffset);
            if (drawPlayerNamesConfig == PvPPlayerNameLocation.MODEL_RIGHT) {
                textLocation = actor.getCanvasTextLocation(graphics, "", zOffset);
                if (textLocation == null) {
                    return;
                }

                textLocation = new Point(textLocation.getX() + 10, textLocation.getY());
            }

            if (textLocation != null) {
                BufferedImage rankImage = null;
                if (actor.isFriendsChatMember() && this.config.drawFriendsChatMemberNames() && this.config.showFriendsChatRanks()) {
                    FriendsChatRank rank = this.playerIndicatorsService.getFriendsChatRank(actor);
                    if (rank != FriendsChatRank.UNRANKED) {
                        rankImage = this.chatIconManager.getRankImage(rank);
                    }
                } else if (actor.isClanMember() && this.config.highlightClanMembers() && this.config.showClanChatRanks()) {
                    ClanTitle clanTitle = this.playerIndicatorsService.getClanTitle(actor);
                    if (clanTitle != null) {
                        rankImage = this.chatIconManager.getRankImage(clanTitle);
                    }
                }

                int level;
                int width;
                int height;
                if (rankImage != null) {
                    int imageWidth = rankImage.getWidth();
                    if (drawPlayerNamesConfig == PvPPlayerNameLocation.MODEL_RIGHT) {
                        level = imageWidth;
                        width = 0;
                    } else {
                        level = imageWidth / 2;
                        width = imageWidth / 2;
                    }

                    height = graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getMaxDescent();
                    Point imageLocation = new Point(textLocation.getX() - width - 1, textLocation.getY() - height / 2 - rankImage.getHeight() / 2);
                    OverlayUtil.renderImageLocation(graphics, imageLocation, rankImage);
                    textLocation = new Point(textLocation.getX() + level, textLocation.getY());
                }

                if (this.config.showCombatLevel()) {
                    name = name + " (" + actor.getCombatLevel() + ")";
                }

                if (this.config.showAgilityLevel() && this.checkWildy()) {
                    HiscoreResult hiscoreResult = this.hiscoreManager.lookupAsync(actor.getName(), HiscoreEndpoint.NORMAL);
                    if (hiscoreResult != null) {
                        level = hiscoreResult.getSkill(HiscoreSkill.AGILITY).getLevel();
                        if (this.config.agilityFormat() == AgilityFormats.ICONS) {
                            width = graphics.getFontMetrics().stringWidth(name);
                            height = graphics.getFontMetrics().getHeight();
                            if (level >= this.config.agilityFirstThreshold()) {
                                OverlayUtil.renderImageLocation(graphics, new Point(textLocation.getX() + 5 + width, textLocation.getY() - height), ImageUtil.resizeImage(this.agilityIcon, height, height));
                            }

                            if (level >= this.config.agilitySecondThreshold()) {
                                OverlayUtil.renderImageLocation(graphics, new Point(textLocation.getX() + this.agilityIcon.getWidth() + width, textLocation.getY() - height), ImageUtil.resizeImage(this.agilityIcon, height, height));
                            }

                            if (level < this.config.agilityFirstThreshold()) {
                                OverlayUtil.renderImageLocation(graphics, new Point(textLocation.getX() + 5 + width, textLocation.getY() - height), ImageUtil.resizeImage(this.noAgilityIcon, height, height));
                            }
                        } else {
                            name = name + " " + level;
                            width = graphics.getFontMetrics().stringWidth(name);
                            height = graphics.getFontMetrics().getHeight();
                            OverlayUtil.renderImageLocation(graphics, new Point(textLocation.getX() + 5 + width, textLocation.getY() - height), ImageUtil.resizeImage(this.agilityIcon, height, height));
                        }
                    }
                }

                OverlayUtil.renderTextLocation(graphics, textLocation, name, color);
            }
        }
    }

    private boolean checkWildy() {
        return this.client.getVar(Varbits.IN_WILDERNESS) == 1 || WorldType.isPvpWorld(this.client.getWorldType());
    }
}
