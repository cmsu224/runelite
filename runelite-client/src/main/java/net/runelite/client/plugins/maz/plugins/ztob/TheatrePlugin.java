/*
 * THIS SOFTWARE WRITTEN BY A KEYBOARD-WIELDING MONKEY BOI
 * No rights reserved. Use, redistribute, and modify at your own discretion,
 * and in accordance with Yagex and RuneLite guidelines.
 * However, aforementioned monkey would prefer if you don't sell this plugin for profit.
 * Good luck on your raids!
 */

package net.runelite.client.plugins.maz.plugins.ztob;

import com.google.inject.Binder;
import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.maz.plugins.ztob.rooms.Nylocas.Nylocas;

@PluginDescriptor(
        name = "Nylo Prefires Maz",
        description = "Nylo Pre-fire markers",
        tags = {"ToB"},
        enabledByDefault = false
)

public class TheatrePlugin extends Plugin
{
    private Room[] rooms = null;

    @Inject
    private EventBus eventBus;

    @Inject
    private Nylocas nylocas;
    @Inject
    private Client client;

    @Override
    public void configure(Binder binder)
    {
        binder.bind(TheatreInputListener.class);
    }

    @Provides
    TheatreConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(TheatreConfig.class);
    }

    @Override
    protected void startUp()
    {
        if (rooms == null)
        {
            rooms = new Room[]{nylocas};
            for (Room room : rooms)
            {
                room.init();
            }
        }

        for(Room room : rooms)
        {
            room.load();
            eventBus.register(room);
        }
    }

    @Override
    protected void shutDown()
    {
        for(Room room : rooms)
        {
            eventBus.unregister(room);
            room.unload();
        }
    }
}
