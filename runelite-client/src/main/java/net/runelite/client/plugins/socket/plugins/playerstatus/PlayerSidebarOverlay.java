//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.playerstatus;

import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.components.ProgressBarComponent.LabelDisplayMode;

public class PlayerSidebarOverlay extends OverlayPanel {
    private static final Color HP_FG = new Color(0, 146, 54, 230);
    private static final Color HP_BG = new Color(102, 15, 16, 230);
    private static final Color PRAY_FG = new Color(0, 149, 151);
    private static final Color PRAY_BG;
    private static final Color RUN_FG;
    private static final Color RUN_BG;
    private static final Color SPEC_FG;
    private static final Color SPEC_BG;
    private final PlayerStatusPlugin plugin;
    private final PlayerStatusConfig config;

    @Inject
    private PlayerSidebarOverlay(PlayerStatusPlugin plugin, PlayerStatusConfig config) {
        super(plugin);
        this.plugin = plugin;
        this.config = config;
        this.panelComponent.setBorder(new Rectangle());
        this.panelComponent.setGap(new Point(0, 2));
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Player Stats Sidebar Overlay"));
    }

    public Dimension render(Graphics2D graphics) {
        Map<String, PlayerStatus> partyStatus = this.plugin.getPartyStatus();
        if (partyStatus.size() <= 1) {
            return null;
        } else if (!this.config.showPlayerHealth() && !this.config.showPlayerPrayer() && !this.config.showPlayerSpecial() && !this.config.showPlayerRunEnergy()) {
            return null;
        } else {
            this.panelComponent.setBackgroundColor((Color)null);
            synchronized(partyStatus) {
                partyStatus.forEach((targetName, targetStatus) -> {
                    PanelComponent panel = targetStatus.getPanel();
                    panel.getChildren().clear();
                    TitleComponent name = TitleComponent.builder().text(targetName).color(Color.WHITE).build();
                    panel.getChildren().add(name);
                    ProgressBarComponent specBar;
                    if (this.config.showPlayerHealth()) {
                        specBar = new ProgressBarComponent();
                        specBar.setBackgroundColor(HP_BG);
                        specBar.setForegroundColor(HP_FG);
                        specBar.setMaximum((long)targetStatus.getMaxHealth());
                        specBar.setValue((double)targetStatus.getHealth());
                        specBar.setLabelDisplayMode(LabelDisplayMode.FULL);
                        panel.getChildren().add(specBar);
                    }

                    if (this.config.showPlayerPrayer()) {
                        specBar = new ProgressBarComponent();
                        specBar.setBackgroundColor(PRAY_BG);
                        specBar.setForegroundColor(PRAY_FG);
                        specBar.setMaximum((long)targetStatus.getMaxPrayer());
                        specBar.setValue((double)targetStatus.getPrayer());
                        specBar.setLabelDisplayMode(LabelDisplayMode.FULL);
                        panel.getChildren().add(specBar);
                    }

                    if (this.config.showPlayerRunEnergy()) {
                        specBar = new ProgressBarComponent();
                        specBar.setBackgroundColor(RUN_BG);
                        specBar.setForegroundColor(RUN_FG);
                        specBar.setMaximum(100L);
                        specBar.setValue((double)targetStatus.getRun());
                        specBar.setLabelDisplayMode(LabelDisplayMode.PERCENTAGE);
                        panel.getChildren().add(specBar);
                    }

                    if (this.config.showPlayerSpecial()) {
                        specBar = new ProgressBarComponent();
                        specBar.setBackgroundColor(SPEC_BG);
                        specBar.setForegroundColor(SPEC_FG);
                        specBar.setMaximum(100L);
                        specBar.setValue((double)targetStatus.getSpecial());
                        specBar.setLabelDisplayMode(LabelDisplayMode.PERCENTAGE);
                        panel.getChildren().add(specBar);
                    }

                    this.panelComponent.getChildren().add(panel);
                });
            }

            return super.render(graphics);
        }
    }

    static {
        PRAY_BG = Color.black;
        RUN_FG = new Color(200, 90, 0);
        RUN_BG = Color.black;
        SPEC_FG = new Color(200, 180, 0);
        SPEC_BG = Color.black;
    }
}
