package net.runelite.client.plugins.maz.plugins.itemdropper.Queries;

import net.runelite.api.Client;

import java.util.function.Predicate;

/**
 * A query to search the game for objects that match.
 *
 * @param <EntityType> the returned object type
 * @param <QueryType>  the query type
 */
public abstract class Query<EntityType, QueryType, QR extends QueryResults>
{
    protected Predicate<EntityType> predicate = x -> true;

    protected Query()
    {
    }

    /**
     * Executes the query and filters through possible objects, returning only
     * those who evaluate true using {@link #predicate}.
     *
     * @param client the game client
     * @return the matching objects
     */
    public abstract QR result(Client client);

    /**
     * Constructs and returns a predicate that will evaluate {@link #predicate}
     * and the passed value.
     *
     * @param other the passed predicate
     * @return the combined predicate
     */
    protected Predicate<EntityType> and(Predicate<EntityType> other)
    {
        if (predicate == null)
        {
            return other;
        }
        return predicate.and(other);
    }
}
