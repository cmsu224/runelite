package net.runelite.client.plugins.itemdropper.Queries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class QueryResults<EntityType> implements Collection<EntityType>
{

    public final ArrayList<EntityType> list;

    public QueryResults(Collection<? extends EntityType> results)
    {
        if (results == null)
        {
            list = new ArrayList<>();
        }
        else
        {
            if (results instanceof ArrayList)
            {
                list = (ArrayList<EntityType>) results;
            }
            else
            {
                list = new ArrayList<>(results);
            }
        }

    }

    @Override
    public int size()
    {
        return list.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    @Override
    public boolean contains(Object o)
    {
        return list.contains(o);
    }

    @Override
    public Iterator<EntityType> iterator()
    {
        return list.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return list.toArray();
    }

    @Override
    public <entityType> entityType[] toArray(entityType[] a)
    {
        return (entityType[]) this.list.toArray();
    }

    @Override
    public boolean add(EntityType entityType)
    {
        return list.add(entityType);
    }

    @Override
    public boolean remove(Object o)
    {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends EntityType> c)
    {
        return list.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return list.retainAll(c);
    }

    @Override
    public void clear()
    {
        list.clear();
    }

    @Nullable
    public final EntityType first()
    {
        return this.size() == 0 ? null : this.get(0);
    }

    @Nullable
    public final EntityType last()
    {
        int size;
        return (size = this.size()) == 0 ? null : this.get(size - 1);
    }

    public EntityType get(int index)
    {
        return list.get(index);
    }

    public final QueryResults limit(int entries)
    {
        return this.limit(0, entries);
    }

    public final QueryResults limit(int startIndex, int amount)
    {
        List<EntityType> limitedList = new ArrayList<>(amount);

        for (int i = startIndex; i < this.size() && i - startIndex < amount; i++)
        {
            limitedList.add(this.get(i));
        }

        this.list.retainAll(limitedList);
        return this;
    }
}