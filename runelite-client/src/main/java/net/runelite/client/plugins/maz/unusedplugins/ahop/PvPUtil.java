package net.runelite.client.plugins.maz.unusedplugins.ahop;


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
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import org.apache.commons.lang3.ArrayUtils;

public class PvPUtil {
    public PvPUtil() {
    }

    public static int getWildernessLevelFrom(WorldPoint point) {
        int y = point.getY();
        int underLevel = (y - 9920) / 8 + 1;
        int upperLevel = (y - 3520) / 8 + 1;
        return y > 6400 ? underLevel : upperLevel;
    }

    public static boolean isAttackable(Client client, Player player) {
        int wildernessLevel = 0;
        if (client.getVar(Varbits.IN_WILDERNESS) != 1 && !WorldType.isPvpWorld(client.getWorldType())) {
            return false;
        } else {
            if (WorldType.isPvpWorld(client.getWorldType())) {
                if (client.getWidget(WidgetInfo.PVP_WORLD_SAFE_ZONE) != null) {
                    Widget x = client.getWidget(WidgetInfo.PVP_WORLD_SAFE_ZONE);
                    if (!x.isHidden() || !x.isSelfHidden()) {
                        return false;
                    }
                }

                if (client.getVar(Varbits.IN_WILDERNESS) != 1) {
                    return Math.abs(client.getLocalPlayer().getCombatLevel() - player.getCombatLevel()) <= 15;
                }

                wildernessLevel = 15;
            }

            return Math.abs(client.getLocalPlayer().getCombatLevel() - player.getCombatLevel()) < getWildernessLevelFrom(client.getLocalPlayer().getWorldLocation()) + wildernessLevel;
        }
    }

    public static int calculateRisk(Client client, ItemManager itemManager) {
        if (client.getItemContainer(InventoryID.EQUIPMENT) == null) {
            return 0;
        } else if (client.getItemContainer(InventoryID.INVENTORY).getItems() == null) {
            return 0;
        } else {
            Item[] items = (Item[]) ArrayUtils.addAll(((ItemContainer) Objects.requireNonNull(client.getItemContainer(InventoryID.EQUIPMENT))).getItems(), ((ItemContainer)Objects.requireNonNull(client.getItemContainer(InventoryID.INVENTORY))).getItems());
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

            return Integer.parseInt(StackFormatter.quantityToRSDecimalStack(priceMap.keySet().stream().mapToInt(Integer::intValue).sum()));
        }
    }
}
