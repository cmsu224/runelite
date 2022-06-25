/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.playerindicatorsextended;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="PlayerNamesConfig")
public interface PlayerIndicatorsExtendedConfig
extends Config {
    @ConfigItem(position=0, keyName="nameColor", name="Socket Player Name Color", description="Name color")
    default public Color nameColor() {
        return Color.decode("0x8686BE");
    }

    @ConfigItem(position=1, keyName="drawMinimap", name="Show on Mini Map", description="Show on Mini map")
    default public boolean drawMinimap() {
        return true;
    }
}

