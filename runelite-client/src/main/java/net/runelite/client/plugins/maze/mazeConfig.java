package net.runelite.client.plugins.maze;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(mazePlugin.CONFIG_GROUP)
public interface mazeConfig extends Config {
    @ConfigItem(
            keyName = "gid",
            name = "Ground ID",
            description = "Ground id of the tile"
    )
    default int gID()
    {
        return 41750;
    }

    @ConfigItem(
            keyName = "regionID",
            name = "Region ID",
            description = "Region id of the tile"
    )
    default int regionID()
    {
        return 13379;
    }

    @ConfigItem(
            keyName = "debug",
            name = "Debug",
            description = "Debug"
    )
    default boolean debug()
    {
        return false;
    }
}
