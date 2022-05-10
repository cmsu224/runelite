/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Alpha
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 */
package net.runelite.client.plugins.socket.plugins.socketDPS;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="socketdpscounter")
public interface SocketDpsConfig
extends Config {
    @ConfigItem(position=0, keyName="displayOverlay", name="Display Plugin Overlay", description="Display's the plugins overlay. - Useful to only display on one client instead of all.")
    default public boolean displayOverlay() {
        return true;
    }

    @ConfigItem(position=1, keyName="showTotal", name="Show total damage", description="Shows total damage done to a boss")
    default public boolean showTotal() {
        return true;
    }

    @ConfigItem(position=1, keyName="clearDamage", name="Clear on boss kill", description="Clears the damage tracker when a boss dies")
    default public clearMode clearDamage() {
        return clearMode.YOUR_WORLD;
    }

    @ConfigItem(position=1, keyName="dmgMessage", name="Damage Message", description="Displays a message that displays your damage, total damage, amount healed, and your damage % <br>Only sends message if you're on the same world as the boss dies")
    default public boolean dmgMessage() {
        return false;
    }

    @ConfigItem(position=2, keyName="onlybossdps", name="Only track boss dps", description="Only tracks boss damage")
    default public boolean onlyBossDps() {
        return false;
    }

    @ConfigItem(position=3, keyName="highlightSelf", name="Highlight self", description="Highlights your name in overlay")
    default public boolean highlightSelf() {
        return true;
    }

    @ConfigItem(position=3, keyName="highlightOtherPlayer", name="Highlight specific players", description="Highlights players in textbox")
    default public boolean highlightOtherPlayer() {
        return true;
    }

    @ConfigItem(position=4, keyName="playersToHighlight", name="Players to Highlight", description="Sets the players to highlight")
    default public String getPlayerToHighlight() {
        return "";
    }

    @ConfigItem(keyName="npcToHighlight", name="", description="")
    public void setPlayerToHighlight(String var1);

    @ConfigItem(position=6, keyName="playerColor", name="Highlight Color", description="Color of the player highlight")
    default public Color getHighlightColor() {
        return Color.YELLOW;
    }

    @ConfigItem(position=7, keyName="backgroundStyle", name="Background Style", description="Sets the background to the style you select")
    default public backgroundMode backgroundStyle() {
        return backgroundMode.STANDARD;
    }

    @Alpha
    @ConfigItem(position=8, keyName="backgroundColor", name="Background Color", description="Sets the overlay color on the custom setting")
    default public Color backgroundColor() {
        return new Color(23, 23, 23, 156);
    }

    @ConfigItem(position=9, keyName="showDifference", name="Show Difference", description="Shows the difference between your damage and the boostee")
    default public boolean showDifference() {
        return false;
    }

    @ConfigItem(position=10, keyName="earlyWarning", name="At Risk Warning", description="Show difference changes color to orange at this threshold")
    default public int earlyWarning() {
        return 100;
    }

    @ConfigItem(position=11, keyName="lateWarning", name="Snipe Warning", description="Show difference changes color to red at this threshold")
    default public int lateWarning() {
        return 50;
    }

    @ConfigItem(position=12, keyName="isMain", name="Is Main Account", description="Set the account as a main account")
    default public boolean isMain() {
        return false;
    }

    @ConfigItem(position=13, keyName="boostedPlayerName", name="Boosted Player Name", description="Sets the name of the player being boosted")
    default public String boostedPlayerName() {
        return "";
    }

    public static enum clearMode {
        ALWAYS,
        YOUR_WORLD,
        ANY_WORLD;

    }

    public static enum backgroundMode {
        STANDARD,
        CUSTOM,
        HIDE;

    }
}

