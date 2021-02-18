package net.runelite.client.plugins.zSocketParty;

import java.awt.Color;
import net.runelite.client.config.*;

import java.awt.Color;
import net.runelite.client.config.*;

@ConfigGroup("zSocketParty")
public interface EasierPartyConfig extends Config
{
    @ConfigItem(
            keyName = "pingTileColor",
            name = "Ping Tile Color",
            description = "Configures the highlight color of pinged tile",
            position = 1
    )
    default Color pingTileColor()
    {
        return new Color((int)(Math.random() * 0x1000000));
    }

    @ConfigItem(
            position = 2,
            keyName = "PingKey",
            name = "Ping Tile Key (R = CTRL, Q = SHIFT)",
            description = "These keycodes are mapped differently in runelite, put R for CTRL and Q for SHIFT. Ask me if you want others."
    )
    default ModifierlessKeybind PingKey()
    {
        return new ModifierlessKeybind(82, 0);
    }

    @ConfigItem(
            keyName = "showpartypanel",
            name = "Show party panel",
            description = "Creates a new panel on the sidebar to display info of all party members",
            position = 3
    )
    default boolean showpartypanel()
    {
        return true;
    }

}