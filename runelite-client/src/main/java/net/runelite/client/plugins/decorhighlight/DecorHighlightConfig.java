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
            position = 1,
            keyName = "reqLoc",
            name = "Require RegionID for objects",
            description = "Input list of Region IDs, Instances are always highlighted"
    )
    default boolean reqLoc() {
        return false;
    }

    @ConfigItem(
            position = 1,
            keyName = "mapLocToHighlight",
            name = "Required Locations",
            description = ""
    )
    default String mapLocToHighlight() {
        return "";
    }

    @ConfigItem(
            position = 2,
            keyName = "graphicsObjectsToHighlight",
            name = "Highlight graphics objects",
            description = ""
    )
    default String graphicsObjectsToHighlight() {
        return "Maiden, 1579,\n" +
                "\n" +
                "Bloat, 1570,1571,1572,1573,1574,1575,1576";
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
        return "Corrupted,\n" +
                "35967, 35971/7, 35969/3,35975/4, 35973/3";
    }

    @ConfigItem(
            position = 5,
            keyName = "npcHighlight",
            name = "Highlight game npcs (ID)",
            description = ""
    )
    default String npcHighlight() {
        return "Hunleff,\n" +
                "9036, 9039,\n" +
                "\n" +
                "Verzik,\n" +
                "8372/5, 8374/5";
    }

    @ConfigItem(
            position = 6,
            keyName = "projectileHighlight",
            name = "Highlight Projectiles [NPCID-size/color(1-7)]]",
            description = ""
    )
    default String projectileHighlight() {
        return "Hunleff,\n" +
                "1711/3, 1707/2, 1713,\n" +
                "1712/3,1708/2, 1714,\n" +
                "\n" +
                "Shamans,\n" +
                "1293/3,\n" +
                "\n" +
                "Sotetseg,\n" +
                "1606-0, 1607-0/3, 1604-0/6,\n" +
                "\n" +
                "Verzik,\n" +
                "1598/3";
    }

    @ConfigItem(
            position = 7,
            keyName = "debugDecor",
            name = "Highlight Decors",
            description = ""
    )
    default boolean debugDecor() {
        return false;
    }

    @ConfigItem(
            position = 8,
            keyName = "debugHighlight",
            name = "Highlight Graphic Objects",
            description = ""
    )
    default boolean debugGraphicObjects() {
        return false;
    }

    @ConfigItem(
            position = 9,
            keyName = "debugGameObjects",
            name = "Highlight Game Objects",
            description = ""
    )
    default boolean debugGameObjects() {
        return false;
    }

    @ConfigItem(
            position = 10,
            keyName = "debugNPCs",
            name = "Highlight NPCs",
            description = ""
    )
    default boolean debugNPCs() {
        return false;
    }

    @ConfigItem(
            position = 11,
            keyName = "debugProjectiles",
            name = "Highlight Projectiles",
            description = ""
    )
    default boolean debugProjectiles() {
        return false;
    }

    @ConfigItem(
            position = 12,
            keyName = "trackProjectiles",
            name = "Track Projectiles",
            description = ""
    )
    default boolean trackProjectiles() {
        return true;
    }

    @ConfigItem(
            position = 13,
            keyName = "highlightColor2",
            name = "Highlight Color 2",
            description = ""
    )
    default Color highlightColor2() {
        return Color.blue;
    }

    @ConfigItem(
            position = 14,
            keyName = "highlightColor3",
            name = "Highlight Color 3",
            description = ""
    )
    default Color highlightColor3() {
        return Color.GREEN;
    }

    @ConfigItem(
            position = 15,
            keyName = "highlightColor4",
            name = "Highlight Color 4",
            description = ""
    )
    default Color highlightColor4() {
        return Color.WHITE;
    }

    @ConfigItem(
            position = 16,
            keyName = "highlightColor5",
            name = "Highlight Color 5",
            description = ""
    )
    default Color highlightColor5() {
        return Color.BLACK;
    }

    @ConfigItem(
            position = 17,
            keyName = "highlightColor6",
            name = "Highlight Color 6",
            description = ""
    )
    default Color highlightColor6() {
        return Color.yellow;
    }

    @ConfigItem(
            position = 18,
            keyName = "highlightColor7",
            name = "Highlight Color 7",
            description = ""
    )
    default Color highlightColor7() {
        return Color.cyan;
    }

    @ConfigItem(
            position = 19,
            keyName = "displayTick",
            name = "Display NPC ticks",
            description = ""
    )
    default boolean displayTick() {
        return false;
    }

    @ConfigItem(
            position = 20,
            keyName = "PrayerHelper",
            name = "Prayer Helper",
            description = ""
    )
    default boolean PrayerHelper() {
        return false;
    }

    @ConfigItem(
            position = 21,
            keyName = "gameTickOverlay",
            name = "NPCs to Display ticks (ID-tick/color)",
            description = ""
    )
    default String gameTickOverlay() {
        return "Xarpus,\n" +
                "8339-4/3";
    }

    @ConfigItem(
            position = 22,
            keyName = "fontSize",
            name = "Font Size",
            description = ""
    )
    default int fontSize() {
        return 12;
    }
}
