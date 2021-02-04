//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.plugins.cerberus.CerberusConfig;
import net.runelite.client.plugins.cerberus.CerberusPlugin;
import net.runelite.client.plugins.cerberus.CerberusConfig.InfoBoxComponentSize;
import net.runelite.client.plugins.cerberus.domain.Cerberus;
import net.runelite.client.plugins.cerberus.domain.Phase;
import net.runelite.client.plugins.cerberus.util.ImageManager;
import net.runelite.client.plugins.cerberus.util.InfoBoxComponent;
import net.runelite.client.plugins.cerberus.util.Utility;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Singleton
public final class UpcomingAttackOverlay extends Overlay {
    private static final Color COLOR_NEXT_ATTACK_BORDER;
    private static final PanelComponent PANEL_COMPONENT;
    private static final int GAP_SIZE = 2;
    private final CerberusPlugin plugin;
    private final CerberusConfig config;

    @Inject
    public UpcomingAttackOverlay(CerberusPlugin plugin, CerberusConfig config) {
        this.plugin = plugin;
        this.config = config;
        PANEL_COMPONENT.setBackgroundColor((Color)null);
        PANEL_COMPONENT.setBorder(new Rectangle(0, 0, 0, 0));
        this.setPriority(OverlayPriority.HIGH);
        this.setPosition(OverlayPosition.BOTTOM_RIGHT);
        this.determineLayer();
    }

    public Dimension render(Graphics2D graphics2D) {
        Cerberus cerberus = this.plugin.getCerberus();
        if (this.config.showUpcomingAttacks() && cerberus != null) {
            PANEL_COMPONENT.getChildren().clear();
            InfoBoxComponentSize infoBoxComponentSize = this.config.infoBoxComponentSize();
            int size = infoBoxComponentSize.getSize();
            Dimension dimension = new Dimension(size, size);
            PANEL_COMPONENT.setPreferredSize(dimension);
            ComponentOrientation orientation = this.config.upcomingAttacksOrientation().getOrientation();
            PANEL_COMPONENT.setOrientation(orientation);
            boolean horizontal = orientation == ComponentOrientation.HORIZONTAL;
            boolean reverse = this.config.reverseUpcomingAttacks();
            Point gap = new Point(horizontal ? 2 : 0, horizontal ? 0 : 2);
            PANEL_COMPONENT.setGap(gap);
            int attacksShown = this.config.amountOfAttacksShown();

            for(int i = 0; i < attacksShown; ++i) {
                int attack;
                if (reverse ^ !horizontal) {
                    attack = attacksShown - i;
                } else {
                    attack = i + 1;
                }

                int cerberusHp = cerberus.getHp();
                Phase phase = cerberus.getNextAttackPhase(attack, cerberusHp);
                BufferedImage image = ImageManager.getCerberusBufferedImage(phase, this.plugin.getPrayer(), infoBoxComponentSize);
                if (image != null) {
                    InfoBoxComponent infoBoxComponent = new InfoBoxComponent();
                    infoBoxComponent.setFont(Utility.getFontFromInfoboxComponentSize(infoBoxComponentSize));
                    infoBoxComponent.setTextColor(Color.GREEN);
                    infoBoxComponent.setBackgroundColor(Utility.getColorFromPhase(phase));
                    infoBoxComponent.setPreferredSize(dimension);
                    infoBoxComponent.setImage(image);
                    Phase nextThresholdPhase = cerberus.getNextAttackPhase(attack, cerberusHp - 200);
                    if (!nextThresholdPhase.equals(phase)) {
                        String text = infoBoxComponentSize == InfoBoxComponentSize.SMALL ? nextThresholdPhase.name().substring(0, 1) : (infoBoxComponentSize == InfoBoxComponentSize.MEDIUM ? nextThresholdPhase.name().substring(0, 2) : nextThresholdPhase.name());
                        infoBoxComponent.setText(String.format("%s +%d", text, cerberusHp % 200));
                    }

                    if (this.config.showUpcomingAttackNumber()) {
                        infoBoxComponent.setTitle(String.valueOf(cerberus.getPhaseCount() + attack));
                    }

                    PANEL_COMPONENT.getChildren().add(infoBoxComponent);
                }
            }

            return PANEL_COMPONENT.render(graphics2D);
        } else {
            return null;
        }
    }

    private void renderOutlineBorder(Graphics2D graphics2D, int size, boolean horizontalLayout, boolean reverseLayout, Point gap, int numberOfAttacks) {
        int x = -1;
        int y = -1;
        if (horizontalLayout && reverseLayout) {
            x = (int)((double)x + ((double)size + gap.getX()) * (double)(numberOfAttacks - 1));
        } else if (!horizontalLayout && !reverseLayout) {
            y = (int)((double)y + ((double)size + gap.getY()) * (double)(numberOfAttacks - 1));
        }

        Rectangle rectangle = new Rectangle();
        rectangle.setLocation(x, y);
        rectangle.setSize(size + 1, size + 1);
        graphics2D.setColor(COLOR_NEXT_ATTACK_BORDER);
        graphics2D.draw(rectangle);
    }

    public void determineLayer() {
        this.setLayer(this.config.mirrorMode() ? OverlayLayer.AFTER_MIRROR : OverlayLayer.ABOVE_WIDGETS);
    }

    static {
        COLOR_NEXT_ATTACK_BORDER = Color.WHITE;
        PANEL_COMPONENT = new PanelComponent();
    }
}
