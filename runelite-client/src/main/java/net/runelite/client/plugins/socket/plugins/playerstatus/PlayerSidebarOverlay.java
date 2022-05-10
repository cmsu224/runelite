/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Inject
 *  net.runelite.api.MenuAction
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.OverlayMenuEntry
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.components.PanelComponent
 *  net.runelite.client.ui.overlay.components.ProgressBarComponent
 *  net.runelite.client.ui.overlay.components.ProgressBarComponent$LabelDisplayMode
 *  net.runelite.client.ui.overlay.components.TitleComponent
 */
package net.runelite.client.plugins.socket.plugins.playerstatus;

import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatus;
import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatusConfig;
import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatusPlugin;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class PlayerSidebarOverlay
extends OverlayPanel {
    private static final Color HP_FG = new Color(0, 146, 54, 230);
    private static final Color HP_BG = new Color(102, 15, 16, 230);
    private static final Color PRAY_FG = new Color(0, 149, 151);
    private static final Color PRAY_BG = Color.black;
    private static final Color RUN_FG = new Color(200, 90, 0);
    private static final Color RUN_BG = Color.black;
    private static final Color SPEC_FG = new Color(200, 180, 0);
    private static final Color SPEC_BG = Color.black;
    private final PlayerStatusPlugin plugin;
    private final PlayerStatusConfig config;

    @Inject
    private PlayerSidebarOverlay(PlayerStatusPlugin plugin, PlayerStatusConfig config) {
        super((Plugin)plugin);
        this.plugin = plugin;
        this.config = config;
        this.panelComponent.setBorder(new Rectangle());
        this.panelComponent.setGap(new Point(0, 2));
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Player Stats Sidebar Overlay"));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Dimension render(Graphics2D graphics) {
        Map<String, PlayerStatus> partyStatus = this.plugin.getPartyStatus();
        if (partyStatus.size() > 1 && (this.config.showPlayerHealth() || this.config.showPlayerPrayer() || this.config.showPlayerSpecial() || this.config.showPlayerRunEnergy())) {
            this.panelComponent.setBackgroundColor(null);
            Map<String, PlayerStatus> map = partyStatus;
            synchronized (map) {
                partyStatus.forEach((targetName, targetStatus) -> {
                    if (this.plugin.getWhiteList().contains(targetName.toLowerCase()) || this.plugin.getWhiteList().isEmpty()) {
                        PanelComponent panel = targetStatus.getPanel();
                        panel.getChildren().clear();
                        TitleComponent name = TitleComponent.builder().text(targetName).color(Color.WHITE).build();
                        panel.getChildren().add(name);
                        if (this.config.showPlayerHealth()) {
                            ProgressBarComponent hpBar = new ProgressBarComponent();
                            hpBar.setBackgroundColor(HP_BG);
                            hpBar.setForegroundColor(HP_FG);
                            hpBar.setMaximum((long)targetStatus.getMaxHealth());
                            hpBar.setValue((double)targetStatus.getHealth());
                            hpBar.setLabelDisplayMode(ProgressBarComponent.LabelDisplayMode.FULL);
                            panel.getChildren().add(hpBar);
                        }
                        if (this.config.showPlayerPrayer()) {
                            ProgressBarComponent prayBar = new ProgressBarComponent();
                            prayBar.setBackgroundColor(PRAY_BG);
                            prayBar.setForegroundColor(PRAY_FG);
                            prayBar.setMaximum((long)targetStatus.getMaxPrayer());
                            prayBar.setValue((double)targetStatus.getPrayer());
                            prayBar.setLabelDisplayMode(ProgressBarComponent.LabelDisplayMode.FULL);
                            panel.getChildren().add(prayBar);
                        }
                        if (this.config.showPlayerRunEnergy()) {
                            ProgressBarComponent runBar = new ProgressBarComponent();
                            runBar.setBackgroundColor(RUN_BG);
                            runBar.setForegroundColor(RUN_FG);
                            runBar.setMaximum(100L);
                            runBar.setValue((double)targetStatus.getRun());
                            runBar.setLabelDisplayMode(ProgressBarComponent.LabelDisplayMode.PERCENTAGE);
                            panel.getChildren().add(runBar);
                        }
                        if (this.config.showPlayerSpecial()) {
                            ProgressBarComponent specBar = new ProgressBarComponent();
                            specBar.setBackgroundColor(SPEC_BG);
                            specBar.setForegroundColor(SPEC_FG);
                            specBar.setMaximum(100L);
                            specBar.setValue((double)targetStatus.getSpecial());
                            specBar.setLabelDisplayMode(ProgressBarComponent.LabelDisplayMode.PERCENTAGE);
                            panel.getChildren().add(specBar);
                        }
                        this.panelComponent.getChildren().add(panel);
                    }
                });
            }
        }
        return super.render(graphics);
    }
}

