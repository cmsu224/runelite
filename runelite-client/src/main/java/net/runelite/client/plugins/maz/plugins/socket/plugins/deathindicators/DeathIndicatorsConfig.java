package net.runelite.client.plugins.maz.plugins.socket.plugins.deathindicators;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("deathindicators")
public interface DeathIndicatorsConfig extends Config {
    @ConfigItem(
            position = 0,
            keyName = "showOutline",
            name = "Show outline",
            description = "Shows outline when killed"
    )
    default boolean showOutline() {
        return false;
    }

    @ConfigItem(
            position = 1,
            keyName = "hideNylo",
            name = "Hide Nylo",
            description = "Hides nylo when killed"
    )
    default boolean hideNylo() {
        return true;
    }
}
