package net.runelite.client.plugins.maz.plugins.cerberus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.ui.overlay.components.ComponentOrientation;

@ConfigGroup("cerberus")
public interface CerberusConfig extends Config
{
    // Sections

    @ConfigSection(
            name = "General",
            description = "",
            position = 0
    )
    String generalSection = "Overlay";

    @ConfigSection(
            name = "Current Attack",
            description = "",
            position = 1
    )
    String currentAttackSection = "Current Attack";

    @ConfigSection(
            name = "Upcoming Attacks",
            description = "",
            position = 2
    )
    String upcomingAttacksSection = "Upcoming Attacks";

    @ConfigSection(
            name = "Guitar Hero Mode",
            description = "",
            position = 3
    )
    String guitarHeroSection = "Guitar Hero Mode";

    // General Section

    @ConfigItem(
            keyName = "drawGhostTiles",
            name = "Show ghost tiles",
            description = "Overlay ghost tiles with respective colors and attack timers.",
            position = 0,
            section = generalSection
    )
    default boolean drawGhostTiles()
    {
        return false;
    }

    @ConfigItem(
            keyName = "calculateAutoAttackPrayer",
            name = "Calculate auto attack prayer",
            description = "Calculate prayer for auto attacks based on your equipment defensive bonuses."
                    + "<br>Default is Protect from Magic.",
            position = 2,
            section = generalSection
    )
    default boolean calculateAutoAttackPrayer()
    {
        return false;
    }

    // Current Attack Section

    @ConfigItem(
            keyName = "showCurrentAttack",
            name = "Show current attack",
            description = "Overlay the current attack in a separate infobox.",
            position = 0,
            section = currentAttackSection
    )
    default boolean showCurrentAttack()
    {
        return false;
    }

    @ConfigItem(
            keyName = "showCurrentAttackTimer",
            name = "Show current attack timer",
            description = "Display a timer on the current attack infobox.",
            position = 1,
            section = currentAttackSection
    )
    default boolean showCurrentAttackTimer()
    {
        return false;
    }

    // Upcoming Attacks Section

    @ConfigItem(
            keyName = "showUpcomingAttacks",
            name = "Show upcoming attacks",
            description = "Overlay upcoming attacks in stacked info boxes.",
            position = 0,
            section = upcomingAttacksSection
    )
    default boolean showUpcomingAttacks()
    {
        return false;
    }

    @Range(
            min = 1,
            max = 10
    )
    @ConfigItem(
            keyName = "amountOfAttacksShown",
            name = "# of attacks",
            description = "Number of upcoming attacks to render.",
            position = 1,
            section = upcomingAttacksSection
    )
    default int amountOfAttacksShown()
    {
        return 4;
    }

    @ConfigItem(
            keyName = "reverseUpcomingAttacks",
            name = "Reverse order",
            description = "Reverse the order of the upcoming attacks.",
            position = 2,
            section = upcomingAttacksSection
    )
    default boolean reverseUpcomingAttacks()
    {
        return false;
    }

    @ConfigItem(
            keyName = "showUpcomingAttackNumber",
            name = "Show attack number",
            description = "Display the attack pattern number on each upcoming attack." +
                    "<br>See http://pastebin.com/hWCvantS",
            position = 3,
            section = upcomingAttacksSection
    )
    default boolean showUpcomingAttackNumber()
    {
        return false;
    }

    @ConfigItem(
            keyName = "upcomingAttacksOrientation",
            name = "Upcoming attacks orientation",
            description = "Display upcoming attacks vertically or horizontally.",
            position = 4,
            section = upcomingAttacksSection
    )
    default InfoBoxOrientation upcomingAttacksOrientation()
    {
        return InfoBoxOrientation.VERTICAL;
    }

    @ConfigItem(
            keyName = "infoBoxComponentSize",
            name = "Info box size",
            description = "Size of the upcoming attacks infoboxes.",
            position = 5,
            section = upcomingAttacksSection
    )
    default InfoBoxComponentSize infoBoxComponentSize()
    {
        return InfoBoxComponentSize.SMALL;
    }

    // Guitar Hero Mode Section

    @ConfigItem(
            keyName = "guitarHeroMode",
            name = "Guitar Hero mode",
            description = "Display descending boxes indicating the correct prayer for the current attack.",
            position = 0,
            section = guitarHeroSection
    )
    default boolean guitarHeroMode()
    {
        return false;
    }

    @Range(
            min = 1,
            max = 10
    )
    @ConfigItem(
            keyName = "guitarHeroTicks",
            name = "# of ticks",
            description = "The number of ticks, before the upcoming current attack, to render.",
            position = 1,
            section = guitarHeroSection
    )
    default int guitarHeroTicks()
    {
        return 4;
    }

    // Constants

    @Getter
    @RequiredArgsConstructor
    enum InfoBoxOrientation
    {
        HORIZONTAL("Horizontal layout", ComponentOrientation.HORIZONTAL),
        VERTICAL("Vertical layout", ComponentOrientation.VERTICAL);

        private final String name;
        private final ComponentOrientation orientation;

        @Override
        public String toString()
        {
            return name;
        }
    }

    @Getter
    @RequiredArgsConstructor
    enum InfoBoxComponentSize
    {
        SMALL("Small boxes", 40), MEDIUM("Medium boxes", 60), LARGE("Large boxes", 80);

        private final String name;
        private final int size;

        @Override
        public String toString()
        {
            return name;
        }
    }
}