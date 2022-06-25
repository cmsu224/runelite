package net.runelite.client.plugins.maz.plugins.itemdropper.Queries;

import java.util.Collection;
import java.util.Comparator;
import javax.annotation.Nullable;

public class LocatableQueryResults<EntityType extends Locatable> extends QueryResults<EntityType>
{

    public LocatableQueryResults(Collection<? extends EntityType> results)
    {
        super(results);
    }

    @Nullable
    public EntityType nearestTo(Locatable locatable)
    {
        return this.stream()
                .min(Comparator.comparing(entityType -> entityType.getLocalLocation().distanceTo(locatable.getLocalLocation())))
                .orElse(null);
    }

}