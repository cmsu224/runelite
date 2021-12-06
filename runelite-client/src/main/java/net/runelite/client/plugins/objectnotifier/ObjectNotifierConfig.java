//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.objectnotifier;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("npcnotifier")
public interface ObjectNotifierConfig extends Config {
    @ConfigItem(
            position = 0,
            keyName = "objectList",
            name = "Object Names",
            description = "List the Object names to highlight, seperated by a comma with no space."
    )
    default String objectNames() {
        return "";
    }

    @ConfigItem(
            position = 1,
            keyName = "notifyOnSpawn",
            name = "Notify on Spawn",
            description = "Use Runelite notifier to notify you of Object spawn"
    )
    default boolean objectSpawnNotifier() {
        return false;
    }

    @ConfigItem(
            position = 2,
            keyName = "autoWorldHopper",
            name = "Auto Hop",
            description = "Automatically hop to a random world if no items were found matching the object names"
    )
    default boolean autoWorldHop() {
        return false;
    }
}
