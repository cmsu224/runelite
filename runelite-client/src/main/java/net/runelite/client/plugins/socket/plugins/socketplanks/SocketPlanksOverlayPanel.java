/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.components.LineComponent
 */
package net.runelite.client.plugins.socket.plugins.socketplanks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.socketplanks.SocketPlanksConfig;
import net.runelite.client.plugins.socket.plugins.socketplanks.SocketPlanksPlugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

public class SocketPlanksOverlayPanel
extends OverlayPanel {
    private final Client client;
    private final SocketPlanksPlugin plugin;
    private final SocketPlanksConfig config;

    @Inject
    private SocketPlanksOverlayPanel(Client client, SocketPlanksPlugin plugin, SocketPlanksConfig config) {
        super((Plugin)plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.splitTimer() && this.client.getVarbitValue(5432) == 1 && this.plugin.planksDropped && this.plugin.splitTimerDelay > 0) {
            this.panelComponent.getChildren().clear();
            int seconds = (int)Math.floor((double)this.client.getVarbitValue(6386) * 0.6);
            if (this.plugin.chestBuiltTime == -1) {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Total Time: ").right(this.plugin.secondsToTime(seconds - this.plugin.planksDroppedTime)).build());
            } else {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Total Time: ").right(this.plugin.secondsToTime(this.plugin.chestBuiltTime - this.plugin.planksDroppedTime)).build());
            }
            if (this.plugin.planksPickedUp) {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Picked up: ").right(this.plugin.planksPickedUpTimeStr).build());
            }
            if (this.plugin.chestBuilt) {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Chest built: ").right(this.plugin.chestBuiltTimeStr).build());
            }
        }
        return super.render(graphics);
    }
}

