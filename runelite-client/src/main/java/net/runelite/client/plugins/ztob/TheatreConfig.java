/*
 * THIS SOFTWARE WRITTEN BY A KEYBOARD-WIELDING MONKEY BOI
 * No rights reserved. Use, redistribute, and modify at your own discretion,
 * and in accordance with Yagex and RuneLite guidelines.
 * However, aforementioned monkey would prefer if you don't sell this plugin for profit.
 * Good luck on your raids!
 */

package net.runelite.client.plugins.ztob;

import java.awt.Color;

import net.runelite.client.config.*;

@ConfigGroup("Theatre")

public interface TheatreConfig extends Config
{
    @ConfigSection(
            name = "Duo",
            description = "Settings for DUO Prefires",
            position = 0
    )
    String duoSection = "duoSection";

    @ConfigSection(
            name = "Trio",
            description = "Settings for Trio Prefires",
            position = 1
    )
    String trioSection = "trioSection";

    @ConfigSection(
            name = "4-Man",
            description = "Settings for 4-Man Prefires",
            position = 2
    )
    String fourManSection = "fourManSection";

    @ConfigSection(
            name = "5-Man",
            description = "Settings for 5-Man Prefires",
            position = 3
    )
    String fiveManSection = "fiveManSection";

    @ConfigItem(
            position = 0,
            keyName = "nyloPrefiresMage",
            name = "Nylocas Duo Mage prefires",
            description = "",
            section = duoSection
    )
    default boolean nyloPrefiresDuoMage(){ return false; }

    @ConfigItem(
            position = 1,
            keyName = "nyloPrefiresDuoRange",
            name = "Nylocas Duo Range prefires",
            description = "",
            section = duoSection
    )
    default boolean nyloPrefiresDuoRange(){ return false; }

    @ConfigItem(
            position = 2,
            keyName = "nyloPrefiresTrioMage",
            name = "Nylocas Trio Mage prefires",
            description = "",
            section = trioSection
    )
    default boolean nyloPrefiresTrioMage(){ return false; }

    @ConfigItem(
            position = 3,
            keyName = "nyloPrefiresTrioRange",
            name = "Nylocas Trio Range prefires",
            description = "",
            section = trioSection
    )
    default boolean nyloPrefiresTrioRange(){ return false; }

    @ConfigItem(
            position = 4,
            keyName = "nyloPrefiresTrioMelee",
            name = "Nylocas Melee prefires",
            description = "",
            section = trioSection
    )
    default boolean nyloPrefiresTrioMelee(){ return false; }

    @ConfigItem(
            position = 5,
            keyName = "nyloPrefires4manMage",
            name = "Nylocas Mage prefires",
            description = "",
            section = fourManSection
    )
    default boolean nyloPrefires4manMage(){ return false; }

    @ConfigItem(
            position = 6,
            keyName = "nyloPrefires4manRange",
            name = "Nylocas Range prefires",
            description = "",
            section = fourManSection
    )
    default boolean nyloPrefires4manRange(){ return false; }

    @ConfigItem(
            position = 7,
            keyName = "nyloPrefires4manMeleeEast",
            name = "Nylocas Melee East prefires",
            description = "",
            section = fourManSection
    )
    default boolean nyloPrefires4manMeleeEast(){ return false; }

    @ConfigItem(
            position = 8,
            keyName = "nyloPrefires4manMeleeWest",
            name = "Nylocas Melee West prefires",
            description = "",
            section = fourManSection
    )
    default boolean nyloPrefires4manMeleeWest(){ return false; }

    @ConfigItem(
            position = 9,
            keyName = "nyloPrefires5manMageWest",
            name = "Nylocas Mage West prefires",
            description = "",
            section = fiveManSection
    )
    default boolean nyloPrefires5manMageWest(){ return false; }

    @ConfigItem(
            position = 10,
            keyName = "nyloPrefires5manMageEast",
            name = "Nylocas Mage East prefires",
            description = "",
            section = fiveManSection
    )
    default boolean nyloPrefires5manMageEast(){ return false; }

    @ConfigItem(
            position = 11,
            keyName = "nyloPrefires5manMeleeEast",
            name = "Nylocas Melee East prefires",
            description = "",
            section = fiveManSection
    )
    default boolean nyloPrefires5manMeleeEast(){ return false; }

    @ConfigItem(
            position = 12,
            keyName = "nyloPrefires5manMeleeWest",
            name = "Nylocas Melee West prefires",
            description = "",
            section = fiveManSection
    )
    default boolean nyloPrefires5manMeleeWest(){ return false; }

    @ConfigItem(
            position = 22,
            keyName = "nyloPrefires5manRange",
            name = "Nylocas Range prefires",
            description = "",
            section = fiveManSection
    )
    default boolean nyloPrefires5manRange(){ return false; }

    @ConfigItem(
            position = 13,
            keyName = "nyloPillars",
            name = "Nylocas pillar health",
            description = "",
            hidden = true
    )
    default boolean nyloPillars(){ return false; }

    @ConfigItem(
            position = 14,
            keyName = "nyloBlasts",
            name = "Nylocas explosions",
            description = "",
            hidden = true
    )
    default boolean nyloBlasts(){ return false; }

    @ConfigItem(
            position = 15,
            keyName = "nyloTimeAlive",
            name = "Nylocas time alive",
            description = "",
            hidden = true
    )
    default boolean nyloTimeAlive(){ return false; }

    @ConfigItem(
            position = 16,
            keyName = "nyloRecolorMenu",
            name = "Nylocas recolor menu",
            description = "",
            hidden = true
    )
    default boolean nyloRecolorMenu(){ return false; }

    @ConfigItem(
            position = 16,
            keyName = "nyloRecolorMenuBig",
            name = "Nylocas recolor big different",
            description = "",
            hidden = true
    )
    default boolean nyloRecolorBigDifferent(){ return false; }

    @ConfigItem(
            position = 17,
            keyName = "nyloOverlay",
            name = "Nylocas overlay",
            description = "",
            hidden = true
    )
    default boolean nyloOverlay(){ return false; }

    @ConfigItem(
        position = 18,
        keyName = "nyloAliveCounter",
        name = "Nylocas alive counter panel",
        description = "",
            hidden = true
    )
    default boolean nyloAlivePanel(){ return false; }

    @ConfigItem(
        position = 19,
        keyName = "nyloAggressiveOverlay",
        name = "Highlight aggressive nylocas",
        description = "",
            hidden = true
    )
    default boolean nyloAggressiveOverlay()
    {
        return false;
    }

    @ConfigItem(
        position = 20,
        keyName = "nyloInstanceTimer",
        name = "Nylo instance timer",
        description = "",
            hidden = true
    )
    default boolean nyloInstanceTimer()
    {
        return false;
    }

    @ConfigItem(
            position = 21,
            keyName = "nyloStallMessage",
            name = "Nylo stall chat message",
            description = "",
            hidden = true
    )
    default boolean nyloStallMessage()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMelee",
            name = "",
            description = "",
            hidden = true
    )
    default boolean getHighlightMeleeNylo()
    {
        return false;
    }

    @ConfigItem(
        keyName = "highlightMelee",
        name = "",
        description = "",
        hidden = true
    )
    void setHighlightMeleeNylo(boolean set);

    @ConfigItem(
            keyName = "highlightMage",
            name = "",
            description = "",
            hidden = true
    )
    default boolean getHighlightMageNylo()
    {
        return false;
    }

    @ConfigItem(
        keyName = "highlightMage",
        name = "",
        description = "",
        hidden = true
    )
    void setHighlightMageNylo(boolean set);

    @ConfigItem(
            keyName = "highlightRange",
            name = "",
            description = "",
            hidden = true
    )
    default boolean getHighlightRangeNylo()
    {
        return false;
    }

    @ConfigItem(
        keyName = "highlightRange",
        name = "",
        description = "",
        hidden = true
    )
    void setHighlightRangeNylo(boolean set);
}
