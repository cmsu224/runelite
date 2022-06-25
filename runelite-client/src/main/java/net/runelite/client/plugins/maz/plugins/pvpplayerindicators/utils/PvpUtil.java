//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators.utils;

import java.awt.Polygon;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeMap;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.WorldType;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.QuantityFormatter;
import org.apache.commons.lang3.ArrayUtils;

public class PvpUtil {
    private static final Polygon NOT_WILDERNESS_BLACK_KNIGHTS = new Polygon(new int[]{2994, 2995, 2996, 2996, 2994, 2994, 2997, 2998, 2998, 2999, 3000, 3001, 3002, 3003, 3004, 3005, 3005, 3005, 3019, 3020, 3022, 3023, 3024, 3025, 3026, 3026, 3027, 3027, 3028, 3028, 3029, 3029, 3030, 3030, 3031, 3031, 3032, 3033, 3034, 3035, 3036, 3037, 3037}, new int[]{3525, 3526, 3527, 3529, 3529, 3534, 3534, 3535, 3536, 3537, 3538, 3539, 3540, 3541, 3542, 3543, 3544, 3545, 3545, 3546, 3546, 3545, 3544, 3543, 3543, 3542, 3541, 3540, 3539, 3537, 3536, 3535, 3534, 3533, 3532, 3531, 3530, 3529, 3528, 3527, 3526, 3526, 3525}, 43);
    private static final Cuboid MAIN_WILDERNESS_CUBOID = new Cuboid(2944, 3525, 0, 3391, 4351, 3);
    private static final Cuboid GOD_WARS_WILDERNESS_CUBOID = new Cuboid(3008, 10112, 0, 3071, 10175, 3);
    private static final Cuboid WILDERNESS_UNDERGROUND_CUBOID = new Cuboid(2944, 9920, 0, 3455, 10879, 3);

    public PvpUtil() {
    }

    public static int getWildernessLevelFrom(WorldPoint point) {
        int regionID = point.getRegionID();
        if (regionID != 12700 && regionID != 12187) {
            if (MAIN_WILDERNESS_CUBOID.contains(point)) {
                return NOT_WILDERNESS_BLACK_KNIGHTS.contains(point.getX(), point.getY()) ? 0 : (point.getY() - 3520) / 8 + 1;
            } else if (GOD_WARS_WILDERNESS_CUBOID.contains(point)) {
                return (point.getY() - 9920) / 8 - 1;
            } else {
                return WILDERNESS_UNDERGROUND_CUBOID.contains(point) ? (point.getY() - 9920) / 8 + 1 : 0;
            }
        } else {
            return 0;
        }
    }

    public static boolean isAttackable(Client client, Player player) {
        int wildernessLevel = 0;

            if (WorldType.isPvpWorld(client.getWorldType())) {
                wildernessLevel += 15;
            }

            if (client.getVar(Varbits.IN_WILDERNESS) == 1) {
                wildernessLevel += getWildernessLevelFrom(client.getLocalPlayer().getWorldLocation());
            }

            return wildernessLevel != 0 && Math.abs(client.getLocalPlayer().getCombatLevel() - player.getCombatLevel()) <= wildernessLevel;

    }

    public static int calculateRisk(Client client, ItemManager itemManager) {
        if (client.getItemContainer(InventoryID.EQUIPMENT) == null) {
            return 0;
        } else if (client.getItemContainer(InventoryID.INVENTORY).getItems() == null) {
            return 0;
        } else {
            Item[] items = (Item[])ArrayUtils.addAll(((ItemContainer)Objects.requireNonNull(client.getItemContainer(InventoryID.EQUIPMENT))).getItems(), ((ItemContainer)Objects.requireNonNull(client.getItemContainer(InventoryID.INVENTORY))).getItems());
            TreeMap<Integer, Item> priceMap = new TreeMap(Comparator.comparingInt(Integer::intValue));
            int wealth = 0;
            Item[] var5 = items;
            int var6 = items.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Item i = var5[var7];
                int value = itemManager.getItemPrice(i.getId()) * i.getQuantity();
                ItemComposition itemComposition = itemManager.getItemComposition(i.getId());
                if (!itemComposition.isTradeable() && value == 0) {
                    value = itemComposition.getPrice() * i.getQuantity();
                    priceMap.put(value, i);
                } else {
                    value = itemManager.getItemPrice(i.getId()) * i.getQuantity();
                    if (i.getId() > 0 && value > 0) {
                        priceMap.put(value, i);
                    }
                }

                wealth += value;
            }

            return Integer.parseInt(QuantityFormatter.quantityToRSDecimalStack(priceMap.keySet().stream().mapToInt(Integer::intValue).sum()));
        }
    }
}
