/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.components.TitleComponent
 */
package net.runelite.client.plugins.socket.plugins.socketba;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.socketba.SocketBAConfig;
import net.runelite.client.plugins.socket.plugins.socketba.SocketBAPlugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class SocketBAPanelOverlay
extends OverlayPanel {
    private SocketBAPlugin plugin;
    private SocketBAConfig config;
    private Client client;

    @Inject
    public SocketBAPanelOverlay(SocketBAPlugin plugin, SocketBAConfig config, Client client) {
        super((Plugin)plugin);
        this.plugin = plugin;
        this.config = config;
        this.client = client;
    }

    public Dimension render(Graphics2D graphics) {
        this.panelComponent.getChildren().clear();
        if (this.config.roleInfobox() && !this.plugin.roleDone && this.client.getVarbitValue(3923) == 1) {
            String displayCall = "";
            Color color = Color.WHITE;
            switch (this.plugin.role) {
                case "Attacker": {
                    if (this.plugin.attCall.toLowerCase().contains("aggressive")) {
                        displayCall = "Agg/Blunt/Earth";
                        color = Color.GREEN;
                        break;
                    }
                    if (this.plugin.attCall.toLowerCase().contains("accurate")) {
                        displayCall = "Acc/Field/Water";
                        color = Color.CYAN;
                        break;
                    }
                    if (this.plugin.attCall.toLowerCase().contains("controlled")) {
                        displayCall = "Ctrl/Bullet/Wind";
                        break;
                    }
                    if (!this.plugin.attCall.toLowerCase().contains("defensive")) break;
                    displayCall = "Def/Barbed/Fire";
                    color = Color.RED;
                    break;
                }
                case "Defender": {
                    displayCall = this.plugin.defCall;
                    break;
                }
                case "Healer": {
                    if (this.plugin.healCall.toLowerCase().contains("tofu")) {
                        displayCall = "Tofu";
                        break;
                    }
                    if (this.plugin.healCall.toLowerCase().contains("worms")) {
                        displayCall = "Worms";
                        break;
                    }
                    if (!this.plugin.healCall.toLowerCase().contains("meat")) break;
                    displayCall = "Schmeat";
                    break;
                }
                case "Collector": {
                    displayCall = this.plugin.colCall;
                }
            }
            if (!displayCall.equals("")) {
                this.panelComponent.getChildren().add(TitleComponent.builder().color(color).text(displayCall).build());
                this.panelComponent.setPreferredSize(new Dimension(23 + displayCall.length() * 5, 24));
            }
        }
        return super.render(graphics);
    }
}

