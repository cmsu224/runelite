//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.data;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.zSocketParty.ui.prayer.PrayerSprites;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Prayers
{
    /**
     * Checks if the prayer is available to be used (checks all requirements)
     * <ul>
     * <li> int	Jagex prayer index to check (See {@link PrayerSprites} scriptIndex property) </li>
     * </ul>
     * Returns
     * <ul>
     * <li> int	(boolean) whether the prayer can be activated </li>
     * </ul>
     */
    public static final int PRAYER_IS_AVAILABLE = 464;

    @Getter
    private Map<Prayer, PrayerData> prayerData = new HashMap<>();

    public Prayers(final Client client)
    {
        // Initialize all prayers if created when logged in
        if (client.getLocalPlayer() != null)
        {
            for (final PrayerSprites p : PrayerSprites.values())
            {
                updatePrayerState(p, client);
            }
        }
    }

    public Prayers(JSONObject json)
    {
        this.parseJSON(json);
    }

    public boolean updatePrayerState(final PrayerSprites p, final Client client)
    {
        boolean changed = false;

        client.runScript(PRAYER_IS_AVAILABLE, p.getScriptIndex());
        final boolean available = client.getIntStack()[0] > 0;

        final boolean enabled = client.isPrayerActive(p.getPrayer());

        PrayerData data = prayerData.get(p.getPrayer());
        if (data == null)
        {
            data = new PrayerData(p.getPrayer(), available, enabled);
            changed = true;
        }
        else
        {
            changed = data.isAvailable() != available || data.isActivated() != enabled;
            data.setAvailable(available);
            data.setActivated(enabled);
        }

        prayerData.put(data.getPrayer(), data);
        return changed;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();

        for(Map.Entry<Prayer, PrayerData> entry : this.prayerData.entrySet())
        {
            Prayer prayer = entry.getKey();
            PrayerData pData = entry.getValue();

            JSONObject praData = pData.toJSON();
            json.put(prayer.getVarbit().toString(), praData);
        }
        return json;
    }

    public void parseJSON(JSONObject json)
    {
        this.prayerData = new HashMap<>();
        Iterator<String> keys = json.keys();
        while(keys.hasNext())
        {
            String key = keys.next();
            PrayerData pd = new PrayerData(json.getJSONObject(key));
            this.prayerData.put(pd.getPrayer(),pd);
        }
    }
}
