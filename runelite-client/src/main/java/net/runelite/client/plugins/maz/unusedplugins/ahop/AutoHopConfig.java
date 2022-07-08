//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.unusedplugins.ahop;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chinautohop")
public interface AutoHopConfig extends Config {
    @ConfigItem(
            keyName = "disableGrandExchange",
            name = "Disable at Grand Exchange",
            description = "Don't hop if your player is at the grand exchange",
            position = 2
    )
    default boolean disableGrandExchange() {
        return false;
    }

    @ConfigItem(
            keyName = "cmbBracket",
            name = "Within combat bracket",
            description = "Only hop if the player is within your combat bracket",
            position = 3
    )
    default boolean cmbBracket() {
        return true;
    }

    @ConfigItem(
            keyName = "alwaysHop",
            name = "Hop on player spawn",
            description = "Hop when a player  spawns",
            position = 4
    )
    default boolean alwaysHop() {
        return true;
    }

    @ConfigItem(
            keyName = "chatHop",
            name = "Hop on chat message",
            description = "Hop whenever any message is entered into chat",
            position = 5
    )
    default boolean chatHop() {
        return false;
    }

    @ConfigItem(
            keyName = "hopRadius",
            name = "Hop radius",
            description = "Hop only when another player enters radius",
            position = 6
    )
    default boolean hopRadius() {
        return false;
    }

    @ConfigItem(
            keyName = "playerRadius",
            name = "Player radius",
            description = "Radius (tiles) for player to be within to trigger hop",
            position = 7
    )
    default int playerRadius() {
        return 0;
    }

    @ConfigItem(
            keyName = "skulledHop",
            name = "Skulled",
            description = "Hop when a player within your combat bracket spawns that has a skull",
            position = 8
    )
    default boolean skulledHop() {
        return true;
    }

    @ConfigItem(
            keyName = "underHop",
            name = "Log under",
            description = "Hop when a player within your combat bracket spawns underneath you",
            position = 9
    )
    default boolean underHop() {
        return true;
    }

    @ConfigItem(
            keyName = "american",
            name = "American",
            description = "Allow hopping to American worlds",
            position = 11
    )
    default boolean american() {
        return true;
    }

    @ConfigItem(
            keyName = "unitedkingdom",
            name = "UK",
            description = "Allow hopping to UK worlds",
            position = 12
    )
    default boolean unitedkingdom() {
        return true;
    }

    @ConfigItem(
            keyName = "germany",
            name = "German",
            description = "Allow hopping to German worlds",
            position = 13
    )
    default boolean germany() {
        return true;
    }

    @ConfigItem(
            keyName = "australia",
            name = "Australian",
            description = "Allow hopping to Australian worlds",
            position = 14
    )
    default boolean australia() {
        return true;
    }

    @ConfigItem(
            keyName = "friends",
            name = "Friends",
            description = "Don't hop when the player spawned is on your friend list",
            position = 16
    )
    default boolean friends() {
        return true;
    }

    @ConfigItem(
            keyName = "clanmembers",
            name = "Clan members",
            description = "Don't hop when the player spawned is in your clan chat",
            position = 17
    )
    default boolean clanmember() {
        return true;
    }

    @ConfigItem(
            keyName = "autoCloseChatbox",
            name = "Auto Close Chatbox",
            description = "Automatically close chatbox messages that prevent you from world hopping such as inventory full message when fishing dark crabs.",
            position = 18
    )
    default boolean autoCloseChatbox() {
        return false;
    }
}
