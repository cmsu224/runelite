package net.runelite.client.plugins.itemdropper.Queries;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

public class InventoryWidgetItemQuery extends WidgetItemQuery
{
    private static final WidgetInfo[] INVENTORY_WIDGET_INFOS =
            {
                    WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER,
                    WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER,
                    WidgetInfo.SHOP_INVENTORY_ITEMS_CONTAINER,
                    WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER,
                    WidgetInfo.GUIDE_PRICES_INVENTORY_ITEMS_CONTAINER,
                    WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER,
                    WidgetInfo.INVENTORY
            };

    @Override
    public QueryResults<WidgetItem> result(Client client)
    {
        Collection<WidgetItem> widgetItems = getInventoryItems(client);
        return new QueryResults<>(widgetItems.stream()
                .filter(Objects::nonNull)
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    private Collection<WidgetItem> getInventoryItems(Client client)
    {
        Collection<WidgetItem> widgetItems = new ArrayList<>();
        for (WidgetInfo widgetInfo : INVENTORY_WIDGET_INFOS)
        {
            Widget inventory = client.getWidget(widgetInfo);
            if (inventory == null || inventory.isHidden())
            {
                continue;
            }
            if (widgetInfo == WidgetInfo.INVENTORY)
            {
                widgetItems.addAll(inventory.getWidgetItems());
                break;
            }
            else
            {
                Widget[] children = inventory.getDynamicChildren();
                for (int i = 0; i < children.length; i++)
                {
                    Widget child = children[i];
                    //boolean isDragged = child.isWidgetItemDragged(child.getItemId());
                    boolean isDragged = false;
                    int dragOffsetX = 0;
                    int dragOffsetY = 0;

                    if (isDragged)
                    {
                        //Point p = child.getWidgetItemDragOffsets();
                        Point p = new Point(0,0);
                        dragOffsetX = p.getX();
                        dragOffsetY = p.getY();
                    }
                    // set bounds to same size as default inventory
                    Rectangle bounds = child.getBounds();
                    bounds.setBounds(bounds.x - 1, bounds.y - 1, 32, 32);
                    Rectangle dragBounds = child.getBounds();
                    dragBounds.setBounds(bounds.x + dragOffsetX, bounds.y + dragOffsetY, 32, 32);
                    widgetItems.add(new WidgetItem(child.getItemId(), child.getItemQuantity(), i, bounds, child, dragBounds));
                }
                break;
            }
        }
        return widgetItems;
    }
}