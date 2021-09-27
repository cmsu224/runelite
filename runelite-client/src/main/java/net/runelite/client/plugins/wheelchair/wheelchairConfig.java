package net.runelite.client.plugins.wheelchair;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("wheelchair")
public interface wheelchairConfig extends Config
{
	@ConfigSection(
			name = "Weapon List",
			description = "Weapon List",
			position = 0
	)
	String wepSection = "Event Passphrase";

	@ConfigSection(
			name = "NPC Names",
			description = "NPC Names",
			position = 1
	)
	String NPCSection = "Google Sheets API";

	@ConfigSection(
			name = "Region",
			description = "Exclude region",
			position = 2
	)
	String RegionSection = "Region";

	@ConfigItem(
			keyName = "rangeNPC",
			name = "NPC to Range",
			description = "ranged nylo",
			section = NPCSection
	)
	default String rangeNPC()
	{
		return "nylocas toxobolos";
	}

	@ConfigItem(
			keyName = "mageNPC",
			name = "NPC to Mage",
			description = "mage nylo",
			section = NPCSection
	)
	default String mageNPC()
	{
		return "nylocas hagios";
	}

	@ConfigItem(
			keyName = "meleeNPC",
			name = "NPC to Melee",
			description = "melee nylo",
			section = NPCSection
	)
	default String meleeNPC()
	{
		return "nylocas ischyros";
	}

	@ConfigItem(
			keyName = "rangeWep",
			name = "Range wep ID",
			description = "ID's of weapons used (12926, 20997, 11959)",
			section = wepSection
	)
	default String rangeWep()
	{
		return "12926, 20997, 11959";
	}

	@ConfigItem(
			keyName = "mageWep",
			name = "Mage wep ID",
			description = "ID's of weapons used (343656, 234523, 345435)",
			section = wepSection
	)
	default String mageWep()
	{
		return "22323, 21006, 12899, 11907";
	}

	@ConfigItem(
			keyName = "meleeWep",
			name = "Melee wep ID",
			description = "ID's of weapons used (343656, 234523, 345435)",
			section = wepSection
	)
	default String meleeWep()
	{
		return "22325, 13652, 24219, 23360, 22324, 4151, 12006";
	}

	@ConfigItem(
			keyName = "regionID",
			name = "Exclude Region ID",
			description = "Region to exclude wheelchair",
			section = NPCSection
	)
	default int regionID()
	{
		return 12611;
	}

}
