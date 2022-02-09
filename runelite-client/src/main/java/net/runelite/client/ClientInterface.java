package net.runelite.client;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.BLClient;
import net.runelite.api.BLEntity;
import net.runelite.api.BLProjectile;
import net.runelite.api.Client;
import net.runelite.api.Projectile;
import net.runelite.api.Renderable;
import net.runelite.api.SpritePixels;

import java.util.HashSet;

// THE CLASS BELOW IS NO LONGER NEEDED AND SHOULD NOT BE USED
// The methods below, which used to be used to access the old api,
// have been turned into examples of how to use the new api directly.

@Slf4j
@Deprecated
public class ClientInterface
{
    // Get the rotation of the actor object. This is different from the orientation which is the world space rotation.
    public static int getRotation(Actor actor)
    {
        return actor.getCurrentOrientation();
    }

    // Set if the given renderable object will be hidden or not
    public static void setHidden(Renderable renderable, boolean hidden)
    {
        if (renderable instanceof BLEntity)
        {
            ((BLEntity) renderable).setHidden(hidden);
        }
    }

    // Get if the given renderable object will be hidden or not
    public static boolean getHidden(Renderable renderable)
    {
        if (renderable instanceof BLEntity)
        {
            return ((BLEntity) renderable).getHidden();
        }
        return false;
    }

    // Get the Actor object for which the given projectile is interacting with
    public static Actor getInteracting(Projectile p)
    {
        if (p instanceof BLProjectile)
        {
            return ((BLProjectile) p).getInteracting();
        }
        return null;
    }

    // Set the login background sprite
    public static void setLoginScreenBackgroundPixels(Client client, SpritePixels spritePixels)
    {
        if (client instanceof BLClient)
        {
            ((BLClient) client).setLoginScreenBackgroundPixels(spritePixels);
        }
    }

    // Force the left side of the login screen sprite to be refreshed
    public static void leftSpriteOverwrite(Client client)
    {
        if (client instanceof BLClient)
        {
            ((BLClient) client).leftSpriteOverwrite();
        }
    }

    // Force the right side of the login screen sprite to be refreshed
    public static void rightSpriteOverwrite(Client client)
    {
        if (client instanceof BLClient)
        {
            ((BLClient) client).rightSpriteOverwrite();
        }
    }

    // Set if any npc which dies should be hidden immediately
    public static void setDeadNPCsHidden(Client client, boolean hidden)
    {
        if (client instanceof BLClient)
        {
            ((BLClient) client).setDeadNPCsHidden(hidden);
        }
    }

    // Get the set of npc names which will be hidden at all times
    public static HashSet<String> getNpcsToHide(Client client)
    {
        if (client instanceof BLClient)
        {
            return ((BLClient) client).getNpcsToHide();
        }
        return null;
    }

    // Get the set of npc names which will be hidden when they die
    public static HashSet<String> getNpcsToHideOnDeath(Client client)
    {
        if (client instanceof BLClient)
        {
            return ((BLClient) client).getNpcsToHideOnDeath();
        }
        return null;
    }

    // Get the set of npc animation ids which will be hidden when the animation occurs
    public static HashSet<Integer> getNpcsByAnimationToHideOnDeath(Client client)
    {
        if (client instanceof BLClient)
        {
            return ((BLClient) client).getNpcsByAnimationToHideOnDeath();
        }
        return null;
    }

    // Get the set of npc ids which will be hidden when they die
    public static HashSet<Integer> getNpcsByIdToHideOnDeath(Client client)
    {
        if (client instanceof BLClient)
        {
            return ((BLClient) client).getNpcsByIdToHideOnDeath();
        }
        return null;
    }
}