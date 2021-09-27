package net.runelite.client.plugins.socketplayerstatusextended;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.kit.KitType;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;

@PluginDescriptor(
        name = "Socket Player Status Extended",
        description = "Socket extension for displaying extended player status to members in your party.",
        tags = {"socket"},
        enabledByDefault = false
)
@Slf4j
@PluginDependency(SocketPlugin.class)
public class SocketPlayerStatusExtendedPlugin extends Plugin
{
    @Inject
    private Client client;
    @Inject
    private EventBus eventBus;
    private boolean inTheatre = false;
    private boolean deferredCheck;
    private int deferredTick;

    protected void startUp() throws Exception
    {
        if (enforceRegion())
        {
            sendFlag(client.getLocalPlayer().getName() + " turned on extended player status");
        }

        deferredCheck = false;
        deferredTick = -1;
    }

    protected void shutDown() throws Exception
    {
        if (enforceRegion())
        {
            sendFlag(client.getLocalPlayer().getName() + " turned off extended player status");
        }

    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (!inTheatre)
        {
            if (enforceRegion())
            {
                inTheatre = true;
                sendFlag(client.getLocalPlayer().getName() + " is using extended player status v1");
            }
        }
        else if (!enforceRegion())
        {
            inTheatre = false;
        }

        if (deferredCheck && client.getTickCount() == deferredTick)
        {
            deferredTick = -1;
            deferredCheck = false;
            checkStats();
        }

    }

    private void sendFlag(String msg)
    {
        JSONArray data = new JSONArray();
        JSONObject jsonmsg = new JSONObject();
        jsonmsg.put("msg", " " + msg);
        data.put(jsonmsg);
        JSONObject send = new JSONObject();
        send.put("playerstatusextendedalt", data);
        //eventBus.post(new SocketBroadcastPacket(send));
    }

    private void sendFlag(int lvl)
    {
        JSONArray data = new JSONArray();
        JSONObject jsonmsg = new JSONObject();
        jsonmsg.put("level1", lvl);
        jsonmsg.put("sender", client.getLocalPlayer().getName());
        data.put(jsonmsg);
        JSONObject send = new JSONObject();
        send.put("playerstatusextended", data);
        //eventBus.post(new SocketBroadcastPacket(send));
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event)
    {
        try
        {
            JSONObject payload = event.getPayload();
            JSONArray data;
            JSONObject jsonObject;
            String msg;
            if (payload.has("playerstatusextended"))
            {
                data = payload.getJSONArray("playerstatusextended");
                jsonObject = data.getJSONObject(0);
                msg = jsonObject.getString("sender");
                int lvl = jsonObject.getInt("level1");
                msg = msg + " attacked at " + lvl + " strength.";
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null);
            }
            else if (payload.has("playerstatusextendedalt"))
            {
                data = payload.getJSONArray("playerstatusextendedalt");
                jsonObject = data.getJSONObject(0);
                msg = jsonObject.getString("msg");
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private boolean enforceRegion()
    {
        return inRegion(12613, 13125, 13123, 12612, 12611);
    }

    private void checkStats()
    {
        if (client.getBoostedSkillLevel(Skill.STRENGTH) != 118)
        {
            sendFlag(client.getBoostedSkillLevel(Skill.STRENGTH));
        }
    }

    private void deferStatCheck()
    {
        deferredCheck = true;
        deferredTick = client.getTickCount();
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        if (event.getActor() instanceof Player && enforceRegion())
        {
            Player p = (Player) event.getActor();
            if (p.equals(client.getLocalPlayer()))
            {
                int animation = event.getActor().getAnimation();
                if (animation == 8056 || animation == 1378 || animation == 7642)
                {
                    deferStatCheck();
                }
            }
        }

        if (event.getActor() instanceof Player && enforceRegion())
        {
            int weapon;
            Player actor = (Player) event.getActor();

            if (actor.getPlayerComposition() == null)
            {
                return;
            }

            weapon = actor.getPlayerComposition().getEquipmentId(KitType.WEAPON);

            if (actor.equals(client.getLocalPlayer()))
            {
                if (event.getActor().getAnimation() == 1378)
                {
                    if (!client.isPrayerActive(Prayer.PIETY))
                    {
                        sendFlag(client.getLocalPlayer().getName() + " DWH specced without piety.");
                    }
                }
                else if (event.getActor().getAnimation() == 8056)
                {
                    if (!client.isPrayerActive(Prayer.PIETY))
                    {
                        sendFlag(client.getLocalPlayer().getName() + " scythed without piety.");
                    }

                    if (client.getVar(VarPlayer.ATTACK_STYLE) != 1)
                    {
                        sendFlag(client.getLocalPlayer().getName() + " scythed on crush");
                    }
                }
                else if (event.getActor().getAnimation() == 7642)
                {
                    if (!client.isPrayerActive(Prayer.PIETY))
                    {
                        sendFlag(client.getLocalPlayer().getName() + " BGS specced without piety.");
                    }
                }
                else if (event.getActor().getAnimation() == 426 && weapon == 20997 && !client.isPrayerActive(Prayer.RIGOUR))
                {
                    sendFlag(client.getLocalPlayer().getName() + " bowed without rigour.");
                }
            }
        }

    }

    private boolean inRegion(int... regions)
    {
        if (client.getMapRegions() != null)
        {
            int[] mapRegions = client.getMapRegions();

            for (int mapRegion : mapRegions)
            {

                if (Arrays.stream(regions).anyMatch(j -> mapRegion == j))
                {
                    return true;
                }
            }
        }

        return false;
    }
}