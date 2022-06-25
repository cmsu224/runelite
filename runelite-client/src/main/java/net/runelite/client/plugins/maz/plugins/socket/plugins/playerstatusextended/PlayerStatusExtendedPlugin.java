/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.EventBus
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.util.ColorUtil
 *  org.pf4j.Extension
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.playerstatusextended;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.maz.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.maz.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.util.ColorUtil;

@PluginDescriptor(name="Socket - Player Status Extended", description="Socket extension for displaying player status to members in your party.", tags={"socket"}, enabledByDefault=false)
@PluginDependency(value=SocketPlugin.class)
public class PlayerStatusExtendedPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private PlayerStatusExtendedConfig config;
    @Inject
    private EventBus eventBus;
    private ArrayList<String> exemptPlayer = new ArrayList();

    @Provides
    PlayerStatusExtendedConfig getConfig(ConfigManager configManager) {
        return (PlayerStatusExtendedConfig)configManager.getConfig(PlayerStatusExtendedConfig.class);
    }

    protected void startUp() throws Exception {
        this.exemptPlayer.clear();
        this.exemptPlayer = new ArrayList();
        if (this.config.ePlayers() != null && this.config.ePlayers().length() > 0) {
            String[] sp0;
            for (String sp1 : sp0 = this.config.ePlayers().split(",")) {
                String sp2;
                if (sp1 == null || (sp2 = sp1.trim()).length() == 0) continue;
                this.exemptPlayer.add(sp2.toLowerCase());
            }
        }
    }

    protected void shutDown() throws Exception {
        this.exemptPlayer.clear();
        this.exemptPlayer = new ArrayList();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("playerstatusextendedconfig") && event.getKey().equals("exemptPl")) {
            this.exemptPlayer.clear();
            if (this.config.ePlayers() != null && this.config.ePlayers().length() > 0) {
                String[] sp0;
                for (String sp1 : sp0 = this.config.ePlayers().split(",")) {
                    if (sp1 == null) continue;
                    String sp2 = sp1.trim();
                    if (sp2.length() != 0) {
                        this.exemptPlayer.add(sp2.toLowerCase());
                    }
                    System.out.println("Config Changed: " + this.exemptPlayer);
                }
            }
        }
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            boolean inCox;
            JSONObject payload = event.getPayload();
            if (!payload.has("sLeech")) {
                return;
            }
            JSONArray data = payload.getJSONArray("sLeech");
            JSONObject jsonmsg = data.getJSONObject(0);
            String sender = jsonmsg.getString("sender");
            if (this.exemptPlayer.contains(sender.toLowerCase())) {
                return;
            }
            String mapRegion = jsonmsg.getString("mapregion");
            int[] mapRegions = this.regionsFromString(mapRegion);
            boolean inTob = this.inRegion(mapRegions, 12613, 13125, 13123, 12612, 12611, 13122);
            boolean bl = inCox = jsonmsg.getInt("raidbit") == 1;
            if (this.config.where() == PlayerStatusExtendedConfig.Where.TOB && !inTob) {
                return;
            }
            if (this.config.where() == PlayerStatusExtendedConfig.Where.COX && !inCox) {
                return;
            }
            if (this.config.where() == PlayerStatusExtendedConfig.Where.TOB_AND_COX && !inCox && !inTob) {
                return;
            }
            String msg = jsonmsg.getString("print");
            String finalS = ColorUtil.prependColorTag((String)msg, (Color)this.config.col());
            if (this.config.lvlOnly() && !finalS.contains("str")) {
                return;
            }
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", finalS, "");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] regionsFromString(String s) {
        String s1 = s.substring(1).replaceAll("]", "");
        String[] s2 = s1.split(",");
        ArrayList<Integer> o = new ArrayList<Integer>();
        for (String s3 : s2) {
            o.add(Integer.valueOf(s3.trim()));
        }
        return o.stream().mapToInt(i -> i).toArray();
    }

    private boolean inRegion(int[] realR, int ... regions) {
        if (realR != null) {
            for (int i : realR) {
                for (int j : regions) {
                    if (i != j) continue;
                    return true;
                }
            }
        }
        return false;
    }
}

