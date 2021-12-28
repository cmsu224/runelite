//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.plugins.aztobextra.config.MaidenRedWCType;
import net.runelite.client.plugins.aztobextra.config.FontMets;
import net.runelite.client.plugins.aztobextra.config.P1PillarTicks;
import net.runelite.client.plugins.aztobextra.config.PillarHide;
import net.runelite.client.plugins.aztobextra.config.SoteProjectileType;
import net.runelite.client.plugins.aztobextra.config.VerzYellowsMode;

@ConfigGroup("Atobwarningshmretard")
public interface AzEasyTobConfig extends Config {
    @ConfigSection(
            name = "Maiden",
            description = "",
            position = 0,
            closedByDefault = true
    )
    String mSec = "maidenSect";
    @ConfigSection(
            name = "Nylo",
            description = "",
            position = 1,
            closedByDefault = true
    )
    String nSec = "nyloSect";
    @ConfigSection(
            name = "Sote",
            description = "",
            position = 2,
            closedByDefault = true
    )
    String sSec = "soteSect";
    @ConfigSection(
            name = "Xarp",
            description = "",
            position = 3,
            closedByDefault = true
    )
    String xSec = "xarpSect";
    @ConfigSection(
            name = "Verz",
            description = "",
            position = 4,
            closedByDefault = true
    )
    String vSec = "verzSect";
    @ConfigSection(
            name = "Autistic",
            description = "",
            position = 5,
            closedByDefault = true
    )
    String aSec = "autSect";
    @ConfigSection(
            name = "Other",
            description = "",
            position = 6,
            closedByDefault = true
    )
    String oSec = "otherSect";
    int maiden_pos = 10;
    int nylo_pos = 100;
    int sote_pos = 200;
    int xarp_pos = 300;
    int verz_pos = 400;
    int aut_pos = 500;
    int meta_pos = 600;

    @ConfigItem(
            position = 11,
            keyName = "nylobadtype1",
            name = "Maiden melfrz wheelchair",
            description = "Shows when s frz needs to frz 3s in group size > 3.",
            section = "maidenSect"
    )
    default net.runelite.client.plugins.aztobextra.config.MaidenRedWCType getType() {
        return net.runelite.client.plugins.aztobextra.config.MaidenRedWCType.OFF;
    }

    @ConfigItem(
            position = 12,
            keyName = "nylobadtypecolor",
            name = "Melfrz WC tile Col",
            description = "color to highlight tile for ^",
            section = "maidenSect"
    )
    default Color melfrzTileColor() {
        return Color.RED;
    }

    @ConfigItem(
            position = 13,
            keyName = "nyloredshpmenus",
            name = "Maiden reds wheelchair",
            description = "Highest hp nylo in stack always top. Shows hp/distance(if ancients)/frz time",
            section = "maidenSect"
    )
    default boolean maidenRedsMenuWC() {
        return false;
    }

    @ConfigItem(
            position = 604,
            keyName = "groupmaidenred",
            name = "Group maiden reds data",
            description = "hp | distance | frzTime layout instead of one on top one on bottom",
            section = "maidenSect"
    )
    default boolean groupedMaiden() {
        return true;
    }

    @ConfigItem(
            position = 101,
            keyName = "eznylos",
            name = "<html><font color=#00ff00>Easy nylos",
            description = "Prioritises small nylos as left click if under a big one, prio fresh smalls over old smalls, prio barrage on mage smalls, hides spot anims",
            section = "nyloSect"
    )
    default boolean easyNylo() {
        return false;
    }

    @ConfigItem(
            position = 102,
            keyName = "eznylosmenucols",
            name = "Clean nylo menus",
            description = "Turn off the other plugins changing nylo menu shit first nigga...gets rid of examine and cmb lvl from menu",
            section = "nyloSect"
    )
    default boolean crispNylo() {
        return false;
    }

    @ConfigItem(
            position = 103,
            keyName = "hotkeyhiderboolean",
            name = "Hotkey hide old nylos",
            description = "Unset hotkey to turn off.",
            section = "nyloSect",
            hidden = true
    )
    default Keybind hotkey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            position = 104,
            keyName = "hotkeyhiderint",
            name = "Hotkey hider threshold",
            description = "Hotkey hide nylos when alive tick over this.",
            section = "nyloSect",
            hidden = true
    )
    default int thresholdHotkey() {
        return 40;
    }

    @ConfigItem(
            position = 201,
            keyName = "sotebombscountdown",
            name = "Sote bomb warning",
            description = "Shows sote attacks till bomb.",
            section = "soteSect"
    )
    default boolean soteBombCountdown() {
        return false;
    }

    @ConfigItem(
            position = 202,
            keyName = "sotestepoffcountdown",
            name = "Sote maze step off warning",
            description = "Shows when to step off maze to insta end.",
            hidden = true,
            section = "soteSect"
    )
    default boolean soteStepCountdown() {
        return false;
    }

    @ConfigItem(
            position = 203,
            keyName = "soteprojtypeenum",
            name = "Sote projectile",
            description = "bomb and personal orbs hats or hat+tick (supported by mcneill's api)",
            section = "soteSect"
    )
    default net.runelite.client.plugins.aztobextra.config.SoteProjectileType soteProjectiles() {
        return net.runelite.client.plugins.aztobextra.config.SoteProjectileType.OFF;
    }

    @ConfigItem(
            position = 301,
            keyName = "xaroexhume",
            name = "P1 helper",
            description = "Better exhumes and countdown till xarpus wakes<br>step off on 1 for both reg and hm",
            section = "xarpSect"
    )
    default boolean cleanXarpExhumes() {
        return false;
    }

    @ConfigItem(
            position = 401,
            keyName = "verzbombscountdown",
            name = "Verz bomb warning",
            description = "Shows verz attacks till lightning and green ball ticks",
            section = "verzSect"
    )
    default boolean verzBombCountdown() {
        return false;
    }

    @ConfigItem(
            position = 402,
            keyName = "verzpurpticks",
            name = "Verzik purp attacks",
            description = "Shows how many verz attacks since last crabs spawned as infobox.",
            section = "verzSect"
    )
    default boolean verzPurpleTicks() {
        return false;
    }

    @ConfigItem(
            position = 403,
            keyName = "p2redhps",
            name = "Clean reds hp",
            description = "red nylo hps",
            section = "verzSect"
    )
    default boolean p2redsHp() {
        return false;
    }

    @ConfigItem(
            position = 404,
            keyName = "smartnado",
            name = "Smart tornado tiles",
            description = "Highlights only your p3 nado and hides every other nado completely.",
            section = "verzSect"
    )
    default boolean smartNado() {
        return false;
    }

    @ConfigItem(
            position = 405,
            keyName = "verzyellowhide",
            name = "Hide verz during yellow",
            description = "hides verz during yellows, unhides 2t before verz attackable again",
            section = "verzSect"
    )
    default boolean hideVerzYellow() {
        return false;
    }

    @ConfigItem(
            position = 406,
            keyName = "verzyellowticksmode",
            name = "<html><font color=#00ff00>Easy yellows (hm/reg)",
            description = "Accurate ticks for reg and hm, colored by groups in hm, step on yellow at tick '1'",
            section = "verzSect"
    )
    default net.runelite.client.plugins.aztobextra.config.VerzYellowsMode yellowticks() {
        return net.runelite.client.plugins.aztobextra.config.VerzYellowsMode.off;
    }

    @ConfigItem(
            position = 407,
            keyName = "greenballticker",
            name = "Greenball Indicator",
            description = "better greenball ticker with nuke icon and mini tile",
            section = "verzSect"
    )
    default boolean gbTicker() {
        return false;
    }

    @ConfigItem(
            position = 408,
            keyName = "p3nadoheals",
            name = "P3 tornado heal count",
            description = "infobox showing how much nados have healed + who healed with game messages for how much hp",
            section = "verzSect"
    )
    default boolean nadohealcount() {
        return false;
    }

    @ConfigItem(
            position = 409,
            keyName = "hidefpills",
            name = "Hide p1 pillar",
            description = "hides specific p1 pillars cx",
            section = "verzSect"
    )
    default net.runelite.client.plugins.aztobextra.config.PillarHide hidePillar() {
        return net.runelite.client.plugins.aztobextra.config.PillarHide.No;
    }

    @ConfigItem(
            position = 410,
            keyName = "p1duotick",
            name = "P1 Fixed pillar tick",
            description = "ticks on ground behind pillar for autists when to flinch only works if duo scale, step behind pillar on tick 2",
            section = "verzSect"
    )
    default net.runelite.client.plugins.aztobextra.config.P1PillarTicks p1ticks() {
        return net.runelite.client.plugins.aztobextra.config.P1PillarTicks.Off;
    }

    @ConfigItem(
            position = 501,
            keyName = "situticks",
            name = "Situational player ticks",
            description = "your att ticks on bloat, every1 att ticks on xarp",
            section = "autSect"
    )
    default boolean situationalAttTicks() {
        return false;
    }

    @ConfigItem(
            position = 506,
            keyName = "leechwarn",
            name = "Unpotted warn box",
            description = "Red box flash under you if <118str, no piety, no rigour bow, no salve in bloat(shared), scy not agg, scy uncharged etc",
            section = "autSect"
    )
    default boolean leechWarn() {
        return false;
    }

    @ConfigItem(
            position = 507,
            keyName = "leechwarnmess",
            name = "Unpotted warn message",
            description = "Send message in accordance with unpotted warn.",
            section = "autSect"
    )
    default boolean leechWarnMes() {
        return false;
    }

    @ConfigItem(
            position = 508,
            keyName = "nylopillar",
            name = "Hide nylo pillars",
            description = "",
            section = "autSect"
    )
    default boolean nyloPillarHide() {
        return false;
    }

    @ConfigItem(
            position = 509,
            keyName = "stompline",
            name = "Bloat stomp line",
            description = "somewhat cleaner stomp line since mcneill aint adding it and u nigs need it",
            section = "autSect"
    )
    default boolean stompLine() {
        return false;
    }

    @ConfigItem(
            position = 510,
            keyName = "rlplusoverlaybload",
            name = "RL+ bloat overlay",
            description = "Uses old rl+ overlays for bloat, fat ticks with seconds, pink/red/green colors, true tile",
            section = "autSect"
    )
    default boolean rLPLUSBloat() {
        return false;
    }

    @ConfigItem(
            position = 511,
            keyName = "rewchest",
            name = "Reward chest warning",
            description = "",
            section = "autSect"
    )
    default boolean chestRew() {
        return false;
    }

    @ConfigItem(
            position = 601,
            keyName = "bombcol",
            name = "Bomb countdown color",
            description = "color of some ticks",
            section = "otherSect"
    )
    default Color getBombCol() {
        return Color.YELLOW;
    }

    @ConfigItem(
            position = 601,
            keyName = "hpdeci",
            name = "Crab hp to 1 decimal?",
            description = "100.0 instead of 100 hp",
            section = "otherSect"
    )
    default boolean crab1deci() {
        return true;
    }

    @ConfigItem(
            position = 602,
            keyName = "oldroomMessages",
            name = "Old room messages",
            description = "Remove the (Normal mode)/(Hard mode) from room messages.",
            section = "otherSect"
    )
    default boolean oldRoomMessages() {
        return false;
    }

    @ConfigItem(
            position = 603,
            keyName = "webhookname",
            name = "Wos Webhook",
            description = "webhook of discord bot to publish death screenshots.",
            section = "otherSect"
    )
    default String webhookId() {
        return "";
    }

    @ConfigItem(
            position = 604,
            keyName = "shadetext",
            name = "Shade text",
            description = "Add shade to text overlay.",
            section = "otherSect"
    )
    default boolean shadeText() {
        return true;
    }

    @ConfigItem(
            position = 605,
            keyName = "typetext",
            name = "Font metrics",
            description = "",
            section = "otherSect"
    )
    default net.runelite.client.plugins.aztobextra.config.FontMets fontType() {
        return net.runelite.client.plugins.aztobextra.config.FontMets.Small;
    }

    @ConfigItem(
            position = 606,
            keyName = "debug",
            name = "Debug",
            hidden = true,
            description = "used for testing-dont turn on unless u want aids to happen",
            section = "otherSect"
    )
    default boolean debug() {
        return false;
    }
}
