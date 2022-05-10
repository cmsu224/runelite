/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 */
package net.runelite.client.plugins.socket.plugins.deathindicators;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="deathindicators")
public interface DeathIndicatorsConfig
extends Config {
    @ConfigItem(position=0, keyName="showOutline", name="Show outline", description="Shows outline when killed")
    default public boolean showOutline() {
        return false;
    }

    @ConfigItem(position=1, keyName="hideNylo", name="Hide Nylo", description="Hides nylo when killed")
    default public boolean hideNylo() {
        return true;
    }

    @ConfigItem(position=2, keyName="deprioNylo", name="Deprioritize Dead Nylo", description="Deprioritizes attack option on Nylos when dead")
    default public boolean deprioNylo() {
        return false;
    }
}

