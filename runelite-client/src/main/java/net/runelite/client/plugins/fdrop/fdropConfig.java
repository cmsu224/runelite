package net.runelite.client.plugins.fdrop;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("rat")
public interface fdropConfig extends Config
{
	@ConfigItem(
			position = 1,
			keyName = "msg",
			name = "Message",
			description = "The message to show to the user when they login"
	)
	default String msg()
	{
		return "";
	}

	@ConfigItem(
			position = 2,
			keyName = "Kill",
			name = "Killed Monster",
			description = "Display the monster killed"
	)
	default String Kill()
	{
		return "";
	}

	@ConfigItem(
			position = 3,
			keyName = "kc",
			name = "KC",
			description = "Show how much kc you have"
	)
	default String kc()
	{
		return "3027";
	}

	@ConfigItem(
			position = 4,
			keyName = "FightDuration",
			name = "FightDuration",
			description = "How much time it took to kill"
	)
	default String FightDuration()
	{
		return "";
	}

	@ConfigItem(
			position = 5,
			keyName = "pb",
			name = "PB",
			description = "Your current PB"
	)
	default String pb()
	{
		return "0:45";
	}

	@ConfigItem(
			position = 6,
			keyName = "drop1",
			name = "Drop 1",
			description = "Valuable drop 1"
	)
	default String drop1()
	{
		return "";
	}

	@ConfigItem(
			position = 7,
			keyName = "drop2",
			name = "Drop 2",
			description = "Valuable drop 2"
	)
	default String drop2()
	{
		return "";
	}

	@ConfigItem(
			position = 8,
			keyName = "drop3",
			name = "Drop 3",
			description = "Valuable drop 3"
	)
	default String drop3()
	{
		return "";
	}

	@ConfigItem(
			position = 9,
			keyName = "drop4",
			name = "Drop 4",
			description = "Valuable drop 4"
	)
	default String drop4()
	{
		return "";
	}

	@ConfigItem(
			position = 10,
			keyName = "custom",
			name = "Custom Message <col=ff0000></col>",
			description = "Valuable drop 4"
	)
	default String custom()
	{
		return "";
	}
}
