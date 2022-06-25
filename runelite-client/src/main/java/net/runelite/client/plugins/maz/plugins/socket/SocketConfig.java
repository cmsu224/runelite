/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 */
package net.runelite.client.plugins.maz.plugins.socket;

import java.util.UUID;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="Socket Plugin v2.1.0")
public interface SocketConfig
extends Config {
    @ConfigItem(position=0, keyName="getHost", name="Server Host Address", description="The host address of the server to connect to.")
    default public Server getServerAddress() {
        return Server.FOREIGNER;
    }

    @ConfigItem(position=1, keyName="customServerAddress", name="Custom Host Address", description="The host address of the server to connect to.")
    default public String customServerAddress() {
        return "socket.kthisiscvpv.com";
    }

    @ConfigItem(position=2, keyName="getPort", name="Server Port Number", description="The port number of the server to connect to.")
    default public int getServerPort() {
        return 26388;
    }

    @ConfigItem(position=3, keyName="getPassword", name="Shared Password", description="Used to encrypt and decrypt data sent to the server.")
    default public String getPassword() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @ConfigItem(position=99, keyName="disableChatMessages", name="Disable Chat Messages", description="Disable chat messages.")
    default public boolean disableChatMessages() {
        return false;
    }

    @ConfigItem(position=100, keyName="infobox", name="Show Connection Infobox", description="Displays an infobox to show if you are connected or disconnected.")
    default public boolean infobox() {
        return false;
    }

    @ConfigItem(keyName="dontsend", name="Dont send my info", description="Turn on to stop sending your data out")
    default public boolean dontsend() {
        return true;
    }

    @ConfigItem(keyName="printMyInfo", name="Print my info", description="Turn on to print your leech")
    default public boolean printMyLeach() {
        return true;
    }

    public static enum Server {
        FOREIGNER("American"),
        AUS("AUS"),
        CUSTOM("Custom");

        private final String name;

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        private Server(String name) {
            this.name = name;
        }
    }
}

