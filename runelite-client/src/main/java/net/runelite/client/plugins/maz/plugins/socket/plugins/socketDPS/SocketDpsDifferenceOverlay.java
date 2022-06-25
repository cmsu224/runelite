/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.OverlayMenuEntry
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.components.ComponentConstants
 *  net.runelite.client.ui.overlay.components.TitleComponent
 *  net.runelite.client.util.QuantityFormatter
 *  net.runelite.client.ws.PartyService
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketDPS;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.party.PartyService;

class SocketDpsDifferenceOverlay
extends OverlayPanel {
    static final OverlayMenuEntry RESET_ENTRY = new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "DPS counter");
    private final SocketDpsCounterPlugin plugin;
    private final SocketDpsConfig config;
    private final Client client;

    @Inject
    SocketDpsDifferenceOverlay(SocketDpsCounterPlugin socketDpsCounterPlugin, SocketDpsConfig socketDpsConfig, PartyService partyService, Client client) {
        super((Plugin)socketDpsCounterPlugin);
        this.plugin = socketDpsCounterPlugin;
        this.config = socketDpsConfig;
        this.client = client;
        this.getMenuEntries().add(RESET_ENTRY);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.showDifference() && this.client.getLocalPlayer() != null && this.client.getLocalPlayer().getName() != null) {
            this.panelComponent.getChildren().clear();
            AtomicReference<String> highlightedPlayer = new AtomicReference<String>("");
            int difference = 0;
            if (!this.plugin.getMembers().isEmpty()) {
                Map<String, Integer> dpsMembers = this.plugin.getMembers();
                dpsMembers.forEach((k, v) -> {
                    if (this.config.boostedPlayerName().equalsIgnoreCase(k.toLowerCase())) {
                        highlightedPlayer.set((String)k);
                    }
                });
                int personalDmg = dpsMembers.get(this.client.getLocalPlayer().getName()) != null ? dpsMembers.get(this.client.getLocalPlayer().getName()) : 0;
                int boostedDmg = dpsMembers.get(highlightedPlayer.toString()) != null ? dpsMembers.get(highlightedPlayer.toString()) : 0;
                difference = boostedDmg - personalDmg;
                Color color = this.config.isMain() ? Color.WHITE : (difference < this.config.lateWarning() ? Color.RED : (difference < this.config.earlyWarning() ? Color.ORANGE : Color.GREEN));
                this.panelComponent.getChildren().add(TitleComponent.builder().color(color).text(QuantityFormatter.formatNumber((long)difference)).build());
            } else {
                this.panelComponent.getChildren().add(TitleComponent.builder().color(this.config.isMain() ? Color.WHITE : Color.RED).text(String.valueOf(0)).build());
            }
            this.panelComponent.setPreferredSize(new Dimension(55, 0));
            if (this.config.backgroundStyle() == SocketDpsConfig.backgroundMode.HIDE) {
                this.panelComponent.setBackgroundColor(null);
            } else if (this.config.backgroundStyle() == SocketDpsConfig.backgroundMode.STANDARD) {
                this.panelComponent.setBackgroundColor(ComponentConstants.STANDARD_BACKGROUND_COLOR);
            } else if (this.config.backgroundStyle() == SocketDpsConfig.backgroundMode.CUSTOM) {
                this.panelComponent.setBackgroundColor(new Color(this.config.backgroundColor().getRed(), this.config.backgroundColor().getGreen(), this.config.backgroundColor().getBlue(), this.config.backgroundColor().getAlpha()));
            }
            return this.panelComponent.render(graphics);
        }
        return null;
    }
}

