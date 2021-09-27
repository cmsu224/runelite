package net.runelite.client.plugins.tobtimer;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

import java.awt.*;

@ConfigGroup(TobtimerPlugin.CONFIG_GROUP)
public interface TobtimerConfig extends Config
{
    @ConfigItem(
            position = 5,
            keyName = "timeColor",
            name = "Color",
            description = "Configures the color of the time"
    )
    default Color timeColor()
    {
        return Color.GREEN;
    }

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
            keyName = "getStartMsgs",
            name = "Timer Start Messages (separate by ';')",
            description = "List of messages to start the timer (Ex. 'You enter the Theatre of Blood; The nightmare has woken')"
    )
    default String getStartMsgs()
    {
        return "";
    }

    @ConfigItem(
            position = 2,
            keyName = "getStopMsgs",
            name = "Timer Stop Messages (separate by ';')",
            description = "List of messages to stop the timer  (Ex. 'Theatre of Blood total completion time:')"
    )
    default String getStopMsgs()
    {
        return "";
    }
}