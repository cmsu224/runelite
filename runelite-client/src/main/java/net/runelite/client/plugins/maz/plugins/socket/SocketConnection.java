/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.client.callback.ClientThread
 *  net.runelite.client.eventbus.EventBus
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.maz.plugins.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.maz.plugins.socket.hash.AES256;
import net.runelite.client.plugins.maz.plugins.socket.hash.SHA256;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONException;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketMembersUpdate;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketPlayerJoin;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketPlayerLeave;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketReceivePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketConnection
implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SocketConnection.class);
    private SocketPlugin plugin;
    private SocketConfig config;
    private Client client;
    private ClientThread clientThread;
    private EventBus eventBus;
    private String playerName;
    private SocketState state;
    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private long lastHeartbeat;

    public SocketConnection(SocketPlugin plugin, String playerName) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.client = this.plugin.getClient();
        this.clientThread = this.plugin.getClientThread();
        this.eventBus = this.plugin.getEventBus();
        this.playerName = playerName;
        this.lastHeartbeat = 0L;
        this.state = SocketState.DISCONNECTED;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        if (this.state != SocketState.DISCONNECTED) {
            throw new IllegalStateException("Socket connection is already in state " + this.state.name() + ".");
        }
        this.state = SocketState.CONNECTING;
        log.info("Attempting to establish socket connection to {}:{}", (Object)this.config.getServerAddress(), (Object)this.config.getServerPort());
        String server = "";
        if (this.config.getServerAddress() == SocketConfig.Server.FOREIGNER) {
            server = "socket.kthisiscvpv.com";
        }
        if (this.config.getServerAddress() == SocketConfig.Server.AUS) {
            server = "socket-aus.kthisiscvpv.com";
        }
        if (this.config.getServerAddress() == SocketConfig.Server.CUSTOM) {
            server = this.config.customServerAddress();
        }
        String secret = new String(this.config.getPassword() + "$P@_/gKR`y:mv)6K");
        try {
            InetSocketAddress address = new InetSocketAddress(server, this.config.getServerPort());
            this.socket = new Socket();
            this.socket.connect(address, 10000);
            this.inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.outputStream = new PrintWriter(this.socket.getOutputStream(), true);
            JSONObject joinPacket = new JSONObject();
            joinPacket.put("header", "JOIN");
            joinPacket.put("room", SHA256.encrypt(secret));
            joinPacket.put("name", AES256.encrypt(secret, this.playerName));
            this.outputStream.println(joinPacket.toString());
            while (this.state != SocketState.DISCONNECTED && this.state != SocketState.TERMINATED && this.socket.isConnected() && !this.socket.isClosed()) {
                JSONObject data;
                if (this.outputStream.checkError()) {
                    throw new IOException("Broken transmission stream");
                }
                if (!this.inputStream.ready()) {
                    long elapsedTime = System.currentTimeMillis() - this.lastHeartbeat;
                    if (elapsedTime >= 30000L) {
                        this.lastHeartbeat = System.currentTimeMillis();
                        PrintWriter printWriter = this.outputStream;
                        synchronized (printWriter) {
                            this.outputStream.println();
                        }
                    }
                    Thread.sleep(20L);
                    continue;
                }
                String packet = this.inputStream.readLine();
                if (packet == null || packet.isEmpty()) continue;
                log.debug("Received packet from server: {}", (Object)packet);
                try {
                    data = new JSONObject(packet);
                    log.debug("Decoded packet as JSON.");
                }
                catch (JSONException e) {
                    log.error("Bad packet. Unable to decode: {}", (Object)packet);
                    continue;
                }
                if (!data.has("header")) {
                    throw new NullPointerException("Packet missing header");
                }
                String header = data.getString("header");
                try {
                    JSONArray membersArray;
                    String targetName;
                    String message;
                    if (header.equals("BROADCAST")) {
                        message = AES256.decrypt(secret, data.getString("payload"));
                        JSONObject payload = new JSONObject(message);
                        this.clientThread.invoke(() -> this.eventBus.post((Object)new SocketReceivePacket(payload)));
                        continue;
                    }
                    if (header.equals("JOIN")) {
                        targetName = AES256.decrypt(secret, data.getString("player"));
                        this.logMessage(SocketLog.INFO, targetName + " has joined the party.");
                        if (targetName.equals(this.playerName)) {
                            this.state = SocketState.CONNECTED;
                            log.info("You have successfully joined the socket party.");
                        }
                        membersArray = data.getJSONArray("party");
                        this.logMessage(SocketLog.INFO, this.mergeMembers(membersArray, secret));
                        try {
                            this.eventBus.post((Object)new SocketPlayerJoin(targetName));
                            this.eventBus.post((Object)new SocketMembersUpdate(this.mergeMembersAsList(membersArray, secret)));
                        }
                        catch (Exception exception) {}
                        continue;
                    }
                    if (header.equals("LEAVE")) {
                        targetName = AES256.decrypt(secret, data.getString("player"));
                        this.logMessage(SocketLog.ERROR, targetName + " has left the party.");
                        membersArray = data.getJSONArray("party");
                        this.logMessage(SocketLog.ERROR, this.mergeMembers(membersArray, secret));
                        try {
                            this.eventBus.post((Object)new SocketPlayerLeave(targetName));
                            this.eventBus.post((Object)new SocketMembersUpdate(this.mergeMembersAsList(membersArray, secret)));
                        }
                        catch (Exception exception) {}
                        continue;
                    }
                    if (!header.equals("MESSAGE")) continue;
                    message = data.getString("message");
                    this.clientThread.invoke(() -> this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null));
                }
                catch (JSONException e) {
                    log.warn("Bad packet contents. Unable to decode.");
                }
            }
        }
        catch (Exception ex) {
            log.error("Unable to establish connection with the server.", (Throwable)ex);
            this.terminate(false);
            this.logMessage(SocketLog.ERROR, "Socket terminated. " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            this.plugin.setNextConnection(System.currentTimeMillis() + 30000L);
            this.logMessage(SocketLog.ERROR, "Reconnecting in 30 seconds...");
            return;
        }
    }

    public void terminate(boolean verbose) {
        if (this.state == SocketState.TERMINATED) {
            return;
        }
        this.state = SocketState.TERMINATED;
        try {
            if (this.outputStream != null) {
                this.outputStream.close();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            if (this.socket != null) {
                this.socket.close();
                this.socket.shutdownOutput();
                this.socket.shutdownInput();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        log.info("Terminated connections with the socket server.");
        if (verbose) {
            this.logMessage(SocketLog.INFO, "Any active socket server connections were closed.");
        }
    }

    private String mergeMembers(JSONArray membersArray, String secret) {
        int count = membersArray.length();
        String members = String.format("Member%s (%d): ", count != 1 ? "s" : "", count);
        for (int i = 0; i < count; ++i) {
            if (i > 0) {
                members = (String)members + ", ";
            }
            members = (String)members + AES256.decrypt(secret, membersArray.getString(i));
        }
        return members;
    }

    private List<String> mergeMembersAsList(JSONArray membersArray, String secret) {
        int count = membersArray.length();
        ArrayList<String> members = new ArrayList<String>();
        for (int i = 0; i < count; ++i) {
            members.add(AES256.decrypt(secret, membersArray.getString(i)));
        }
        return members;
    }

    private void logMessage(SocketLog level, String message) {
        if (this.config.disableChatMessages()) {
            return;
        }
        this.clientThread.invoke(() -> this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", level.getPrefix() + message, null));
    }

    public SocketState getState() {
        return this.state;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public BufferedReader getInputStream() {
        return this.inputStream;
    }

    public PrintWriter getOutputStream() {
        return this.outputStream;
    }
}

