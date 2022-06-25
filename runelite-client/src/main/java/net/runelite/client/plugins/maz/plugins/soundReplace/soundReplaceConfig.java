package net.runelite.client.plugins.maz.plugins.soundReplace;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("soundreplace")
public interface soundReplaceConfig extends Config
{
    enum SoundMode
    {
        DISABLED,
        BEN,
        MITCH,
    }

    @ConfigItem(
            position = 0,
            keyName = "soundDebug",
            name = "Enable Sound Debug",
            description = "Enable this to find the id of sounds being played"
    )
    default boolean soundDebug()
    {
        return false;
    }

    @ConfigItem(
            position = 1,
            keyName = "soundMode",
            name = "Type of filter",
            description = ""
    )
    default SoundMode soundMode()
    {
        return SoundMode.MITCH;
    }

    @ConfigItem(
            position = 2,
            keyName = "replacelistEffect",
            name = "Sound Effect Replace list",
            description = "ONLY sound effects in this list will be replaced" +
                    "<br>Use the format 2665,2424,530"
    )
    default String replacelistEffect()
    {
        return "2911";
    }

    @ConfigItem(
            position = 3,
            keyName = "replacelistArea",
            name = "Sound Area Replace list",
            description = "ONLY sound effects in this list will be replaced" +
                    "<br>Use the format 2665,2424,530"
    )
    default String replacelistArea()
    {
        return "";
    }

}