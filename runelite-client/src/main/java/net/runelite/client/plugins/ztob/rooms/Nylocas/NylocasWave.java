package net.runelite.client.plugins.ztob.rooms.Nylocas;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.HashSet;
import lombok.Getter;

public class NylocasWave
{
	public static final int MAX_WAVE = 31;
	static final ImmutableMap < Integer, NylocasWave > waves = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();


	//DUO - MAGE
	static final ImmutableMap < Integer, NylocasWave > wavesDuoMage = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST, false, "Mager"),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}, "south", "3 Sang hits only"))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH, false, "Mager"),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}, "west", "3 Sang hits only"))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true, "Mager")
			}, "east", "3 Sang hits only"))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, false, "Mager"),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH, false, "Mager")
			}, "south", "Full kill big mage", "Dagger"))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, false, "Mager")
			}, "east", "Help clear smalls from mage/range big"))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH, false, "Mager"),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}, "west", "Help clear smalls from mage/range big"))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST, false, "Mager"),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}, "east", "Run west"))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true, "Mager"),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, false, "Mager"),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}, "west", "Kill big asap", "Dagger"))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, false, "Mager"),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}, "west", "Kill stuff around room, then go east", "Dagger"))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}, "middle", "Kill stuff around room, then go east"))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true, "Mager"),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST, false, "Mager"),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST, false, "Mager"),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true, "Mager"),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, false,"Mager")
			}, "east", "East > West > South", "Kodai"))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true, "Mager"),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH, false, "Mager")
			}, "east", "", "Scythe"))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, false, "Mager"),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}, "south", "", "Dagger"))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH, false, "Mager"),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true, "Mager"),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}, "west", "Scythe pillars", "Dagger"))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, false, "Mager"),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true, "Mager"),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}, "west", "Claw spec Big Melee aggro", "Dagger/BP"))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}, "middle", "Wait for wave 17 to spawn"))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}, "middle", "Kill splits from bigs"))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}, "middle", "Kill smalls"))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG,false,"Mager"),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG,false,"Mager"),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true,"Mager")
			}, "east", "Kill all 3 Big mages"))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true, "Mager"),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, false, "Mager")
			}, "south", "", "Scythe"))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH, false,"Mager"),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, false,"Mager"),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true, "Mager"),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST, false, "Mager"),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}, "west", "Look out for big value barrages", "Kodai/Scythe"))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}, "middle", "Look out for big value barrages"))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}, "middle", "Help ranger kill bigs"))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true, "Mager"),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true, "Mager")
			}, "south", "Run east, scythe big", "Scythe"))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true, "Mager"),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}, "west", "", "Scythe"))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true, "Mager"),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}, "west", ""))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}, "middle", "Only fire 5 sangs max"))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}, "middle", "Only fire 5 sangs max"))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, false, "Mager"),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}, "middle", "Only fire 5 sangs max", "Scythe"))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true, "Mager")
			}, "east", "Kill melee big"))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}, "east", "Kill new things"))
			.build();

	//DUO - RANGE
	static final ImmutableMap < Integer, NylocasWave > wavesDuoRange = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//TRIO - Mage
	static final ImmutableMap < Integer, NylocasWave > wavesTrioMage = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//TRIO - Melee
	static final ImmutableMap < Integer, NylocasWave > wavesTrioMelee = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//TRIO - Range
	static final ImmutableMap < Integer, NylocasWave > wavesTrioRange = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//4man - Mage
	static final ImmutableMap < Integer, NylocasWave > waves4manMage = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//4man - Melee east
	static final ImmutableMap < Integer, NylocasWave > waves4manMeleeEast = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//4man - Melee west
	static final ImmutableMap < Integer, NylocasWave > waves4manMeleeWest = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//4man - Range
	static final ImmutableMap < Integer, NylocasWave > waves4manRange = wavesTrioRange;

	//5man - Mage west
	static final ImmutableMap < Integer, NylocasWave > waves5manMageWest = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//5man - Mage east
	static final ImmutableMap < Integer, NylocasWave > waves5manMageEast = ImmutableMap. < Integer, NylocasWave > builder()
			.put(1, new NylocasWave(1, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(2, new NylocasWave(2, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(3, new NylocasWave(3, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(4, new NylocasWave(4, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(5, new NylocasWave(5, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(6, new NylocasWave(6, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(7, new NylocasWave(7, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(8, new NylocasWave(8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(9, new NylocasWave(9, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(10, new NylocasWave(10, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(11, new NylocasWave(11, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(12, new NylocasWave(12, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(13, new NylocasWave(13, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(14, new NylocasWave(14, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(15, new NylocasWave(15, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(16, new NylocasWave(16, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(17, new NylocasWave(17, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(18, new NylocasWave(18, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(19, new NylocasWave(19, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(20, new NylocasWave(20, 16, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(21, new NylocasWave(21, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_WEST, true),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(22, new NylocasWave(22, 12, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(23, new NylocasWave(23, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(24, new NylocasWave(24, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.EAST_NORTH, true)
			}))
			.put(25, new NylocasWave(25, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(26, new NylocasWave(26, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG, true),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(27, new NylocasWave(27, 8, new NyloNPC[] {
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG, true),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_NORTH)
			}))
			.put(28, new NylocasWave(28, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH, true),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.put(29, new NylocasWave(29, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH, true),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MELEE_BIG, NylocasSpawnPoint.SOUTH_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH, true)
			}))
			.put(30, new NylocasWave(30, new NyloNPC[] {
					new NyloNPC(NylocasType.RANGE_BIG, NylocasSpawnPoint.WEST_BIG),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_BIG, NylocasSpawnPoint.EAST_BIG, true)
			}))
			.put(31, new NylocasWave(31, new NyloNPC[] {
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.WEST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.WEST_SOUTH),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.SOUTH_WEST),
					new NyloNPC(NylocasType.MELEE_SMALL, NylocasSpawnPoint.SOUTH_EAST),
					new NyloNPC(NylocasType.MAGE_SMALL, NylocasSpawnPoint.EAST_NORTH),
					new NyloNPC(NylocasType.RANGE_SMALL, NylocasSpawnPoint.EAST_SOUTH)
			}))
			.build();

	//5man - MeleeEast
	static final ImmutableMap < Integer, NylocasWave > waves5manMeleeEast = waves4manMeleeEast;

	//5man - MeleeWest
	static final ImmutableMap < Integer, NylocasWave > waves5manMeleeWest = waves4manMeleeWest;

	//5man - Range
	static final ImmutableMap < Integer, NylocasWave > waves5manRange = waves4manRange;

	@Getter
	private final int wave;
	@Getter
	private final HashSet<NyloNPC> waveData;
	@Getter
	private final int waveDelay;
	@Getter
	public String startPosition = "";
	@Getter
	public String notes = "";
	@Getter
	public String weapon = "";

	private NylocasWave(int wave, NyloNPC[] waveData)
	{
		this(wave, 4 ,waveData);
	}

	private NylocasWave(int wave, int waveDelay, NyloNPC[] waveData)
	{
		this.wave = wave;
		this.waveData = new HashSet<>(Arrays.asList(waveData));
		this.waveDelay = waveDelay;
	}

	private NylocasWave(int wave, NyloNPC[] waveData, String startPosition)
	{
		this(wave, 4 ,waveData);
		this.startPosition = startPosition;
	}

	private NylocasWave(int wave, int waveDelay, NyloNPC[] waveData, String startPosition)
	{
		this.wave = wave;
		this.waveData = new HashSet<>(Arrays.asList(waveData));
		this.waveDelay = waveDelay;
		this.startPosition = startPosition;
	}

	private NylocasWave(int wave, NyloNPC[] waveData, String startPosition, String notes)
	{
		this(wave, 4 ,waveData);
		this.startPosition = startPosition;
		this.notes = notes;
	}

	private NylocasWave(int wave, int waveDelay, NyloNPC[] waveData, String startPosition, String notes)
	{
		this.wave = wave;
		this.waveData = new HashSet<>(Arrays.asList(waveData));
		this.waveDelay = waveDelay;
		this.startPosition = startPosition;
		this.notes = notes;
	}

	private NylocasWave(int wave, NyloNPC[] waveData, String startPosition, String notes, String weapon)
	{
		this(wave, 4 ,waveData);
		this.startPosition = startPosition;
		this.notes = notes;
		this.weapon = weapon;
	}

	private NylocasWave(int wave, int waveDelay, NyloNPC[] waveData, String startPosition, String notes, String weapon)
	{
		this.wave = wave;
		this.waveData = new HashSet<>(Arrays.asList(waveData));
		this.waveDelay = waveDelay;
		this.startPosition = startPosition;
		this.notes = notes;
		this.weapon = weapon;
	}

}
