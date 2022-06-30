package net.runelite.client.plugins.maz.plugins.gameobjectremover;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("gameobjectremover")
public interface GameObjectRemoverConfig extends Config {
    @ConfigItem(
            position = 1,
            keyName = "objectList",
            name = "Game Object List",
            description = "List of game objects to hide, seperated by a comma."
    )
    default String objectList() {
        return "";
    }

    @ConfigItem(
            position = 2,
            keyName = "objectList2",
            name = "Game Object List Higher Plane",
            description = "List of game objects to hide, seperated by a comma on Plane 1. MUST INCLUDE ID IN THE ABOVE LIST AS WELL"
    )
    default String objectList2() {
        return "";
    }
}
