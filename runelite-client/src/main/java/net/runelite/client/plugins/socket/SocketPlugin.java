//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket;

import com.google.inject.Provides;
import java.io.PrintWriter;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.hash.AES256;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketPlayerJoin;
import net.runelite.client.plugins.socket.packet.SocketPlayerLeave;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "Socket",
        description = "Socket connection for broadcasting messages across clients.",
        tags = {"socket", "server", "discord", "connection", "broadcast"},
        enabledByDefault = false
)
public class SocketPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SocketPlugin.class);
    public static final String CONFIG_VERSION = "Socket Plugin v2.0.5";
    public static final String PASSWORD_SALT = "$P@_/gKR`y:mv)6K";
    @Inject
    private Client client;
    @Inject
    private EventBus eventBus;
    @Inject
    private ClientThread clientThread;
    @Inject
    private SocketConfig config;
    private long nextConnection;
    private SocketConnection connection = null;

    public SocketPlugin() {
    }

    @Provides
    SocketConfig getConfig(ConfigManager configManager) {
        return (SocketConfig)configManager.getConfig(SocketConfig.class);
    }

    protected void startUp() {
        this.nextConnection = 0L;
        this.eventBus.register(SocketReceivePacket.class);
        this.eventBus.register(SocketBroadcastPacket.class);
        this.eventBus.register(SocketPlayerJoin.class);
        this.eventBus.register(SocketPlayerLeave.class);
    }

    protected void shutDown() {
        this.eventBus.unregister(SocketReceivePacket.class);
        this.eventBus.unregister(SocketBroadcastPacket.class);
        this.eventBus.unregister(SocketPlayerJoin.class);
        this.eventBus.unregister(SocketPlayerLeave.class);
        if (this.connection != null) {
            this.connection.terminate(true);
        }

    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            if (this.connection != null) {
                SocketState state = this.connection.getState();
                if (state == SocketState.CONNECTING || state == SocketState.CONNECTED) {
                    return;
                }
            }

            if (System.currentTimeMillis() >= this.nextConnection) {
                this.nextConnection = System.currentTimeMillis() + 30000L;
                this.connection = new SocketConnection(this, this.client.getLocalPlayer().getName());
                (new Thread(this.connection)).start();
            }
        }

    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("Socket Plugin v2.0.5")) {
            this.clientThread.invoke(() -> {
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=b4281e>Configuration changed. Please restart the plugin to see updates.", (String)null);
            });
        }

    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGIN_SCREEN && this.connection != null) {
            this.connection.terminate(false);
        }

    }

    @Subscribe
    public void onSocketBroadcastPacket(SocketBroadcastPacket packet) {
        try {
            if (this.connection == null || this.connection.getState() != SocketState.CONNECTED) {
                return;
            }

            String data = packet.getPayload().toString();
            log.debug("Deploying packet from client: {}", data);
            String secret = this.config.getPassword() + "$P@_/gKR`y:mv)6K";
            JSONObject payload = new JSONObject();
            payload.put("header", "BROADCAST");
            payload.put("payload", AES256.encrypt(secret, data));
            PrintWriter outputStream = this.connection.getOutputStream();
            synchronized(outputStream) {
                outputStream.println(payload.toString());
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            log.error("An error has occurred while trying to broadcast a packet.", var9);
        }

    }

    public Client getClient() {
        return this.client;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public ClientThread getClientThread() {
        return this.clientThread;
    }

    public SocketConfig getConfig() {
        return this.config;
    }

    public long getNextConnection() {
        return this.nextConnection;
    }

    public void setNextConnection(long nextConnection) {
        this.nextConnection = nextConnection;
    }
}
