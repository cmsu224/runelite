//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.runelite.api.Prayer;
import net.runelite.client.plugins.socket.org.json.JSONObject;

@Data
@AllArgsConstructor
public class PrayerData
{
    private Prayer prayer;
    private boolean available;
    private boolean activated;

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();

        json.put("prayer", prayer.getVarbit().toString());
        json.put("available", available);
        json.put("activated", activated);

        return json;
    }

    public PrayerData(JSONObject json)
    {
        parseJSON(json);
    }

    public void parseJSON(JSONObject json)
    {
        String pray = json.getString("prayer");
        this.available = json.getBoolean("available");
        this.activated = json.getBoolean("activated");

        if (pray.equals("PRAYER_THICK_SKIN"))
        {
            this.prayer = Prayer.THICK_SKIN;
        }
        if (pray.equals("PRAYER_BURST_OF_STRENGTH"))
        {
            this.prayer = Prayer.BURST_OF_STRENGTH;
        }
        if (pray.equals("PRAYER_CLARITY_OF_THOUGHT"))
        {
            this.prayer = Prayer.CLARITY_OF_THOUGHT;
        }
        if (pray.equals("PRAYER_SHARP_EYE"))
        {
            this.prayer = Prayer.SHARP_EYE;
        }
        if (pray.equals("PRAYER_MYSTIC_WILL"))
        {
            this.prayer = Prayer.MYSTIC_WILL;
        }
        if (pray.equals("PRAYER_ROCK_SKIN"))
        {
            this.prayer = Prayer.ROCK_SKIN;
        }
        if (pray.equals("PRAYER_SUPERHUMAN_STRENGTH"))
        {
            this.prayer = Prayer.SUPERHUMAN_STRENGTH;
        }
        if (pray.equals("PRAYER_IMPROVED_REFLEXES"))
        {
            this.prayer = Prayer.IMPROVED_REFLEXES;
        }
        if (pray.equals("PRAYER_RAPID_RESTORE"))
        {
            this.prayer = Prayer.RAPID_RESTORE;
        }
        if (pray.equals("PRAYER_RAPID_HEAL"))
        {
            this.prayer = Prayer.RAPID_HEAL;
        }
        if (pray.equals("PRAYER_PROTECT_ITEM"))
        {
            this.prayer = Prayer.PROTECT_ITEM;
        }
        if (pray.equals("PRAYER_HAWK_EYE"))
        {
            this.prayer = Prayer.HAWK_EYE;
        }
        if (pray.equals("PRAYER_MYSTIC_LORE"))
        {
            this.prayer = Prayer.MYSTIC_LORE;
        }
        if (pray.equals("PRAYER_STEEL_SKIN"))
        {
            this.prayer = Prayer.STEEL_SKIN;
        }
        if (pray.equals("PRAYER_ULTIMATE_STRENGTH"))
        {
            this.prayer = Prayer.ULTIMATE_STRENGTH;
        }
        if (pray.equals("PRAYER_INCREDIBLE_REFLEXES"))
        {
            this.prayer = Prayer.INCREDIBLE_REFLEXES;
        }
        if (pray.equals("PRAYER_PROTECT_FROM_MAGIC"))
        {
            this.prayer = Prayer.PROTECT_FROM_MAGIC;
        }
        if (pray.equals("PRAYER_PROTECT_FROM_MISSILES"))
        {
            this.prayer = Prayer.PROTECT_FROM_MISSILES;
        }
        if (pray.equals("PRAYER_PROTECT_FROM_MELEE"))
        {
            this.prayer = Prayer.PROTECT_FROM_MELEE;
        }
        if (pray.equals("PRAYER_EAGLE_EYE"))
        {
            this.prayer = Prayer.EAGLE_EYE;
        }
        if (pray.equals("PRAYER_MYSTIC_MIGHT"))
        {
            this.prayer = Prayer.MYSTIC_MIGHT;
        }
        if (pray.equals("PRAYER_RETRIBUTION"))
        {
            this.prayer = Prayer.RETRIBUTION;
        }
        if (pray.equals("PRAYER_REDEMPTION"))
        {
            this.prayer = Prayer.REDEMPTION;
        }
        if (pray.equals("PRAYER_SMITE"))
        {
            this.prayer = Prayer.SMITE;
        }
        if (pray.equals("PRAYER_CHIVALRY"))
        {
            this.prayer = Prayer.CHIVALRY;
        }
        if (pray.equals("PRAYER_PIETY"))
        {
            this.prayer = Prayer.PIETY;
        }
        if (pray.equals("PRAYER_PRESERVE"))
        {
            this.prayer = Prayer.PRESERVE;
        }
        if (pray.equals("PRAYER_RIGOUR"))
        {
            this.prayer = Prayer.RIGOUR;
        }
        if (pray.equals("PRAYER_AUGURY"))
        {
            this.prayer = Prayer.AUGURY;
        }

    }
}