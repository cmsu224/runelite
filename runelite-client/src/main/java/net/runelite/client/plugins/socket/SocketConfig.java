//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket;

import java.util.UUID;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Socket Plugin v2.0.5")
public interface SocketConfig extends Config {
    @ConfigItem(
            position = 0,
            keyName = "getHost",
            name = "Server Host Address",
            description = "The host address of the server to connect to."
    )
    default String getServerAddress() {
        return "socket.kthisiscvpv.com";
    }

    @ConfigItem(
            position = 1,
            keyName = "getPort",
            name = "Server Port Number",
            description = "The port number of the server to connect to."
    )
    default int getServerPort() {
        return 26388;
    }

    @ConfigItem(
            position = 2,
            keyName = "getPassword",
            name = "Shared Password",
            description = "Used to encrypt and decrypt data sent to the server."
    )
    default String getPassword() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
