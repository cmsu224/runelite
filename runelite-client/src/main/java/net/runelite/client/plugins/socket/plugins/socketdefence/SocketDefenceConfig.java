/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 *  net.runelite.client.config.ConfigSection
 *  net.runelite.client.config.Range
 */
package net.runelite.client.plugins.socket.plugins.socketdefence;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(value="socketdefence")
public interface SocketDefenceConfig
extends Config {
    @ConfigSection(name="Corp", description="Corp settings", position=0, closedByDefault=true)
    public static final String corpSection = "corp";

    @Range(max=50, min=2)
    @ConfigItem(name="Low Defence Threshold", keyName="lowDef", description="Sets when you want the defence to appear as yellow (low defence).", position=1)
    default public int lowDef() {
        return 10;
    }

    @ConfigItem(keyName="cm", name="Challenge Mode", description="Toggle this to set the defence to Challenge Mode when doing Cox", position=2)
    default public boolean cm() {
        return true;
    }

    @ConfigItem(keyName="vulnerability", name="Show Vulnerability", description="Displays an infobox when you successfully land vulnerability", position=3)
    default public boolean vulnerability() {
        return true;
    }

    @ConfigItem(keyName="vulnOutline", name="Vulnerability Highlight", description="Bosses will be highlighted when vulnerability lands", position=3)
    default public boolean vulnOutline() {
        return false;
    }

    @ConfigItem(keyName="vulnColor", name="Vuln Highlight Color", description="Color of the successful vulnerability highlight", position=4)
    default public Color vulnColor() {
        return new Color(255, 96, 0);
    }

    @ConfigItem(keyName="corpChally", name="Corp Chally Highlight", description="Highlight corp when you should chally spec", position=0, section="corp")
    default public CorpTileMode corpChally() {
        return CorpTileMode.OFF;
    }

    @Range(min=0, max=255)
    @ConfigItem(keyName="corpChallyOpacity", name="Corp Chally Opactiy", description="Toggles opacity of Corp Chally Highlight", position=1, section="corp")
    default public int corpChallyOpacity() {
        return 20;
    }

    @Range(min=1, max=5)
    @ConfigItem(keyName="corpChallyThicc", name="Corp Chally Width", description="Toggles girth of Corp Chally Highlight", position=2, section="corp")
    default public int corpChallyThicc() {
        return 2;
    }

    @Range(min=0, max=4)
    @ConfigItem(keyName="corpGlow", name="Corp Chally Glow", description="Adjusts glow of Corp Chally Outline Highlight", position=3, section="corp")
    default public int corpGlow() {
        return 4;
    }

    public static enum CorpTileMode {
        OFF,
        AREA,
        HULL,
        TILE,
        TRUE_LOCATION,
        OUTLINE;

    }
}

