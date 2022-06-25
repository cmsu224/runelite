package net.runelite.client.plugins.maz.plugins.mawsennostalgia;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(MawsenNostalgiaConfig.GROUP)
public interface MawsenNostalgiaConfig extends Config {

    String GROUP = "MawsenNostalgia";

    @ConfigItem(
            keyName = "volumeGain",
            name = "Volume Gain",
            description = "The volume gain used for the voice over audios."
    )
    @Range(min = -25, max = 6)
    default int volumeGain() {
        return 0;
    }
}