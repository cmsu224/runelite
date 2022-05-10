/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Alpha
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 *  net.runelite.client.config.ConfigSection
 *  net.runelite.client.config.Range
 */
package net.runelite.client.plugins.socket.plugins.socketicedemon;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(value="icDemon")
public interface SocketIceDemonConfig
extends Config {
    @ConfigSection(name="<html><font color=#00aeef>Brazier", description="Brazier Plugins", position=0, closedByDefault=true)
    public static final String brazierSection = "brazier";

    @ConfigItem(keyName="display4Scav", name="Display Overlay for CM Scaver", description="Displays the overlay when you are in Scavs or Ice Demon. Have to be in Socket to accurately display the information.", position=1)
    default public boolean display4Scav() {
        return true;
    }

    @ConfigItem(keyName="dumpMsg", name="Dump Message", description="Puts a message in game chat of when to dump based off your current raids party size", position=2)
    default public boolean dumpMsg() {
        return true;
    }

    @ConfigItem(keyName="showTeamKindling", name="Show Kindling Needed", description="Shows the amount of kindling needed in the infobox", position=3)
    default public boolean showTeamKindling() {
        return true;
    }

    @ConfigItem(keyName="showNames", name="Show Names", description="Shows the name of players and how many kindling they got", position=4)
    default public boolean showNames() {
        return true;
    }

    @ConfigItem(keyName="iceDemonSpawnTicks", name="Ice Demon Spawn Ticks", description="Displays ticks until Ice Demon activates after finishing lighting", position=5)
    default public boolean iceDemonSpawnTicks() {
        return true;
    }

    @ConfigItem(keyName="iceDemonHp", name="Ice Demon HP", description="Displays Ice Demon HP percent while lighting kindling", position=6)
    default public boolean iceDemonHp() {
        return true;
    }

    @ConfigItem(keyName="highlightUnlitBrazier", name="Highlight Unlit Brazier", description="Draws a tile under unlit braziers", position=0, section="brazier")
    default public boolean highlightUnlitBrazier() {
        return true;
    }

    @Alpha
    @ConfigItem(keyName="highlightBrazierColor", name="Highlight Brazier Color", description="Sets color of highlight unlit brazier plugin", position=1, section="brazier")
    default public Color highlightBrazierColor() {
        return Color.RED;
    }

    @Range(min=0, max=255)
    @ConfigItem(keyName="highlightBrazierOpacity", name="Highlight Brazier Opacity", description="Sets Opacity of highlight unlit brazier plugin", position=2, section="brazier")
    default public int highlightBrazierOpacity() {
        return 50;
    }
}

