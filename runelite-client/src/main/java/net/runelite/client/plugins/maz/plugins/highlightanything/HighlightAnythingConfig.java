//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.highlightanything;

import java.awt.Color;

import net.runelite.client.config.*;

@ConfigGroup("decorhighlight")
public interface HighlightAnythingConfig extends Config {
    @ConfigSection(
            name = "Toggles",
            description = "Toggle configuration.",
            position = 0,
            closedByDefault = true
    )
    String toggleSection = "Toggle section";

    @ConfigSection(
            name = "Highlight",
            description = "Highlight configuration.",
            position = 1,
            closedByDefault = true
    )
    String highlightSection = "Highlight section";

    @ConfigSection(
            name = "Colors",
            description = "Color configuration.",
            position = 2,
            closedByDefault = true
    )
    String colorSection = "Color section";

    @ConfigSection(
            name = "Debug",
            description = "Debug configuration.",
            position = 3,
            closedByDefault = true
    )
    String debugSection = "Debug section";

    @ConfigSection(
            name = "Ticks",
            description = "Ticks configuration.",
            position = 4,
            closedByDefault = true
    )
    String ticksSection = "Ticks section";



    @ConfigItem(
            keyName = "toggleKeybind",
            name = "Toggle Overlay",
            description = "Binds a key (combination) to toggle the overlay.",
            position = 0,
            section = toggleSection
    )
    default Keybind toggleKeybind()
    {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            position = 1,
            keyName = "highlightColor",
            name = "Highlight Color",
            description = "",
            section = colorSection
    )
    default Color highlightColor() {
        return Color.RED;
    }

    @ConfigItem(
            position = 0,
            keyName = "reqLoc",
            name = "Require RegionID for objects",
            description = "Input list of Region IDs, Instances are always highlighted.  NPCs and projectiles are always highlighted",
            section = toggleSection
    )
    default boolean reqLoc() {
        return false;
    }

    @ConfigItem(
            position = 2,
            keyName = "decorLocToHighlight",
            name = "Location IDs for Decor",
            description = "ID must also be in the list above",
            section = toggleSection
    )
    default String decorLocToHighlight() {
        return "";
    }

    @ConfigItem(
            position = 1,
            keyName = "mapLocToHighlight",
            name = "Location IDs for Objects",
            description = "Draw highlight at only these locations. Instances will always be highlighted.",
            section = toggleSection
    )
    default String mapLocToHighlight() {
        return "6198,13113";
    }

    @ConfigItem(
            position = 2,
            keyName = "graphicsObjectsToHighlight",
            name = "Highlight graphics objects",
            description = "",
            section = highlightSection
    )
    default String graphicsObjectsToHighlight() {
        return "Maiden, 1579,\n" +
                "\n" +
                "Bloat, 1570,1571,1572,1573,1574";
    }

    @ConfigItem(
            position = 3,
            keyName = "groundDecorToHighlight",
            name = "Highlight ground objects (decor)",
            description = "",
            section = highlightSection
    )
    default String groundDecorToHighlight() {
        return "20584,\n" +
                "xarpus,32743/3";
    }

    @ConfigItem(
            position = 4,
            keyName = "gameObjectsToHighlight",
            name = "Highlight game objects",
            description = "",
            section = highlightSection
    )
    default String gameObjectsToHighlight() {
        return "Corrupted,\n" +
                "35967, 35971/7, 35969/3,35975/4, 35973/6\n" +
                ",4886\n" +
                ",traps,8734/3,8732,9000,8996/3,\n" +
                "redwoods,29670-1,29668-1";
    }

    @ConfigItem(
            position = 5,
            keyName = "npcHighlight",
            name = "Highlight game npcs (ID)",
            description = "",
            section = highlightSection
    )
    default String npcHighlight() {
        return "Hunleff,\n" +
                "9039,9025,\n" +
                "Maiden,\n" +
                "10829,8367,\n" +
                "Xarpus,\n" +
                "8340/5, 10772/5,\n" +
                "Verzik,\n" +
                "8372/2, 8374/2,10850/2,10852/2\n" +
                "\n" +
                ",ShayzienInfirmry,\n" +
                "6838-1, 6830-1, 6848-1, 6852-1, 6844-1, 6850-1,6842-1,6828-1, 6856-1,6826-1,6834-1,6836-1\n" +
                "\n" +
                ",WarriorGuildLowLvlGiants,\n" +
                "2465,2467,2463\n" +
                "\n" +
                ",nex,11278/3,11280/3,11281/3,11283/7,11284/7,11285/7,11286/7";
    }

    @ConfigItem(
            position = 6,
            keyName = "projectileHighlight",
            name = "Highlight Projectiles [NPCID-size/color(1-7)]]",
            description = "",
            section = highlightSection
    )
    default String projectileHighlight() {
        return "Hunleff,\n" +
                "x1711-1/3, x1707-1/7, x1713-1,\n" +
                "x1712-1/3,x1708-1/7, x1714-1,\n" +
                "Shamans,\n" +
                "x1293-1/3,\n" +
                "Sotetseg,\n" +
                "1606-0, 1607-0/3, 1604-0/6,\n" +
                "Verzik,\n" +
                "1598-1/3,1585-0/7";
    }

    @ConfigItem(
            position = 7,
            keyName = "debugDecor",
            name = "Highlight Ground Objects (Decor)",
            description = "",
            section = debugSection
    )
    default boolean debugDecor() {
        return false;
    }

    @ConfigItem(
            position = 8,
            keyName = "debugHighlight",
            name = "Highlight Graphic Objects",
            description = "",
            section = debugSection
    )
    default boolean debugGraphicObjects() {
        return false;
    }

    @ConfigItem(
            position = 9,
            keyName = "debugGameObjects",
            name = "Highlight Game Objects",
            description = "",
            section = debugSection
    )
    default boolean debugGameObjects() {
        return false;
    }

    @ConfigItem(
            position = 10,
            keyName = "debugNPCs",
            name = "Highlight NPCs",
            description = "",
            section = debugSection
    )
    default boolean debugNPCs() {
        return false;
    }

    @ConfigItem(
            position = 11,
            keyName = "debugProjectiles",
            name = "Highlight Projectiles",
            description = "",
            section = debugSection
    )
    default boolean debugProjectiles() {
        return false;
    }

    @ConfigItem(
            position = 12,
            keyName = "trackProjectiles",
            name = "Track Projectiles",
            description = "",
            section = toggleSection
    )
    default boolean trackProjectiles() {
        return true;
    }

    @ConfigItem(
            position = 13,
            keyName = "highlightColor2",
            name = "Highlight Color 2",
            description = "",
            section = colorSection
    )
    default Color highlightColor2() {
        return Color.blue;
    }

    @ConfigItem(
            position = 14,
            keyName = "highlightColor3",
            name = "Highlight Color 3",
            description = "",
            section = colorSection
    )
    default Color highlightColor3() {
        return Color.GREEN;
    }

    @ConfigItem(
            position = 15,
            keyName = "highlightColor4",
            name = "Highlight Color 4",
            description = "",
            section = colorSection
    )
    default Color highlightColor4() {
        return Color.WHITE;
    }

    @ConfigItem(
            position = 16,
            keyName = "highlightColor5",
            name = "Highlight Color 5",
            description = "",
            section = colorSection
    )
    default Color highlightColor5() {
        return Color.BLACK;
    }

    @ConfigItem(
            position = 17,
            keyName = "highlightColor6",
            name = "Highlight Color 6",
            description = "",
            section = colorSection
    )
    default Color highlightColor6() {
        return Color.yellow;
    }

    @ConfigItem(
            position = 18,
            keyName = "highlightColor7",
            name = "Highlight Color 7",
            description = "",
            section = colorSection
    )
    default Color highlightColor7() {
        return Color.cyan;
    }

    @ConfigItem(
            position = 19,
            keyName = "displayTick",
            name = "Display NPC ticks",
            description = "",
            section = ticksSection
    )
    default boolean displayTick() {
        return false;
    }

    @ConfigItem(
            position = 21,
            keyName = "gameTickOverlay",
            name = "NPCs to Display ticks (ID-tick/color)",
            description = "",
            section = ticksSection
    )
    default String gameTickOverlay() {
        return "Xarpus,\n" +
                "8339-4/3";
    }

    @ConfigItem(
            position = 22,
            keyName = "fontSize",
            name = "Font Size",
            description = "",
            section = ticksSection
    )
    default int fontSize() {
        return 12;
    }
}
