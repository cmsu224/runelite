/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 */
package net.runelite.client.plugins.socket.plugins.socketvangpots;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="socketvangpots")
public interface SocketVangPotsConfig
extends Config {
    @ConfigItem(keyName="showPanel", name="Show Panel", description="Displays pots panel")
    default public boolean showPanel() {
        return true;
    }

    @ConfigItem(keyName="showChatMessage", name="Chat Message", description="Displays split timer")
    default public boolean showChatMessage() {
        return true;
    }
}

