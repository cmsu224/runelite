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
package net.runelite.client.plugins.socket.plugins.socketicedemon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.socketicedemon.SocketIceDemonConfig;
import net.runelite.client.plugins.socket.plugins.socketicedemon.SocketIceDemonPlugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

public class SocketIceDemonPanelOverlay
extends OverlayPanel {
    private SocketIceDemonPlugin plugin;
    private SocketIceDemonConfig config;
    private Client client;

    @Inject
    public SocketIceDemonPanelOverlay(SocketIceDemonPlugin plugin, SocketIceDemonConfig config, Client client) {
        super((Plugin)plugin);
        this.plugin = plugin;
        this.config = config;
        this.client = client;
    }

    public Dimension render(Graphics2D graphics) {
        this.panelComponent.getChildren().clear();
        if (this.config.showTeamKindling()) {
            if (this.plugin.teamTotalKindlingCut >= this.plugin.teamKindlingNeeded) {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Cut: ").rightColor(Color.GREEN).right(this.plugin.teamTotalKindlingCut + "/" + this.plugin.teamKindlingNeeded).build());
            } else {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Cut: ").right(this.plugin.teamTotalKindlingCut + "/" + this.plugin.teamKindlingNeeded).build());
            }
            if (this.plugin.teamTotalKindlingLit >= this.plugin.teamKindlingNeeded) {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Lit: ").rightColor(Color.GREEN).right(this.plugin.teamTotalKindlingLit + "/" + this.plugin.teamKindlingNeeded).build());
            } else {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Lit: ").right(this.plugin.teamTotalKindlingLit + "/" + this.plugin.teamKindlingNeeded).build());
            }
        } else {
            if (this.plugin.teamTotalKindlingCut >= this.plugin.teamKindlingNeeded) {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Cut: ").rightColor(Color.GREEN).right(String.valueOf(this.plugin.teamTotalKindlingCut)).build());
            } else {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Cut: ").right(String.valueOf(this.plugin.teamTotalKindlingCut)).build());
            }
            if (this.plugin.teamTotalKindlingLit >= this.plugin.teamKindlingNeeded) {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Lit: ").rightColor(Color.GREEN).right(String.valueOf(this.plugin.teamTotalKindlingLit)).build());
            } else {
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Lit: ").right(String.valueOf(this.plugin.teamTotalKindlingLit)).build());
            }
        }
        if (this.config.showNames()) {
            for (String name : this.plugin.playerNameList) {
                int index = this.plugin.playerNameList.indexOf(name);
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("    " + name + ":").right(String.valueOf(this.plugin.playerKindlingList.get(index))).build());
            }
        }
        this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Braziers: ").right(String.valueOf(this.plugin.litBraziers)).build());
        return super.render(graphics);
    }
}

