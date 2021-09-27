//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.decorhighlight;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("decorhighlight")
public interface DecorHighlightConfig extends Config {
    @ConfigItem(
            keyName = "toggleKeybind",
            name = "Toggle Overlay",
            description = "Binds a key (combination) to toggle the overlay.",
            position = 0
    )
    default Keybind toggleKeybind()
    {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            position = 1,
            keyName = "highlightColor",
            name = "Highlight Color",
            description = ""
    )
    default Color highlightColor() {
        return Color.RED;
    }

    @ConfigItem(
            position = 2,
            keyName = "graphicsObjectsToHighlight",
            name = "Highlight graphics objects",
            description = ""
    )
    default String graphicsObjectsToHighlight() {
        return "";
    }

    @ConfigItem(
            position = 3,
            keyName = "groundDecorToHighlight",
            name = "Highlight ground objects (decor)",
            description = ""
    )
    default String groundDecorToHighlight() {
        return "";
    }

    @ConfigItem(
            position = 4,
            keyName = "gameObjectsToHighlight",
            name = "Highlight game objects",
            description = ""
    )
    default String gameObjectsToHighlight() {
        return "";
    }

    @ConfigItem(
            position = 5,
            keyName = "npcHighlight",
            name = "Highlight game npcs (ID)",
            description = ""
    )
    default String npcHighlight() {
        return "";
    }

    @ConfigItem(
            position = 6,
            keyName = "debugDecor",
            name = "Highlight Decors",
            description = ""
    )
    default boolean debugDecor() {
        return false;
    }

    @ConfigItem(
            position = 7,
            keyName = "debugHighlight",
            name = "Highlight Graphic Objects",
            description = ""
    )
    default boolean debugGraphicObjects() {
        return false;
    }

    @ConfigItem(
            position = 8,
            keyName = "debugGameObjects",
            name = "Highlight Game Objects",
            description = ""
    )
    default boolean debugGameObjects() {
        return false;
    }

    @ConfigItem(
            position = 9,
            keyName = "debugNPCs",
            name = "Highlight NPCs",
            description = ""
    )
    default boolean debugNPCs() {
        return false;
    }
}
