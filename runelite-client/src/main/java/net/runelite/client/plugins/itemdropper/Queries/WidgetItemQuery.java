package net.runelite.client.plugins.itemdropper.Queries;

import java.util.Collection;
import java.util.function.Predicate;
import net.runelite.client.plugins.itemdropper.Queries.Query;
import net.runelite.client.plugins.itemdropper.Queries.QueryResults;
import net.runelite.api.widgets.WidgetItem;

public abstract class WidgetItemQuery extends Query<WidgetItem, WidgetItemQuery, QueryResults<WidgetItem>>
{
    public WidgetItemQuery idEquals(int... ids)
    {
        predicate = and(item ->
        {
            for (int id : ids)
            {
                if (item.getId() == id)
                {
                    return true;
                }
            }
            return false;
        });
        return this;
    }

    public WidgetItemQuery idEquals(Collection<Integer> ids)
    {
        predicate = and((object) -> ids.contains(object.getId()));
        return this;
    }

    public WidgetItemQuery indexEquals(int... indexes)
    {
        predicate = and(item ->
        {
            for (int index : indexes)
            {
                if (item.getIndex() == index)
                {
                    return true;
                }
            }
            return false;
        });
        return this;
    }

    public WidgetItemQuery quantityEquals(int quantity)
    {
        predicate = and(item -> item.getQuantity() == quantity);
        return this;
    }

    public WidgetItemQuery filter(Predicate<WidgetItem> other)
    {
        predicate = and(other);
        return this;
    }
}