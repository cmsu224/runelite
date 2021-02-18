//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.data;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.client.plugins.socket.org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Getter
@Setter
public class Stats
{
    private Map<Skill, Integer> baseLevels = new HashMap<>();
    private Map<Skill, Integer> boostedLevels = new HashMap<>();
    private int specialPercent;
    private int runEnergy;
    private int combatLevel;
    private int totalLevel;

    public Stats(final Client client)
    {
        final int[] bases = client.getRealSkillLevels();
        final int[] boosts = client.getBoostedSkillLevels();
        for (final Skill s : Skill.values())
        {
            baseLevels.put(s, bases[s.ordinal()]);
            boostedLevels.put(s, boosts[s.ordinal()]);
        }

        combatLevel = Experience.getCombatLevel(
                baseLevels.get(Skill.ATTACK),
                baseLevels.get(Skill.STRENGTH),
                baseLevels.get(Skill.DEFENCE),
                baseLevels.get(Skill.HITPOINTS),
                baseLevels.get(Skill.MAGIC),
                baseLevels.get(Skill.RANGED),
                baseLevels.get(Skill.PRAYER)
        );

        specialPercent = client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10;
        totalLevel = client.getTotalLevel();
        runEnergy = client.getEnergy();
    }

    public Stats(JSONObject json){
        this.parseJSON(json);
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();

        JSONObject baselvls = new JSONObject();
        for(Map.Entry<Skill, Integer> entry : this.baseLevels.entrySet())
        {
            Skill skll = entry.getKey();
            Integer lvl = entry.getValue();
            baselvls.put(skll.getName(), lvl);
        }

        JSONObject boostlvls = new JSONObject();
        for(Map.Entry<Skill, Integer> entry : this.boostedLevels.entrySet())
        {
            Skill skll = entry.getKey();
            Integer lvl = entry.getValue();
            boostlvls.put(skll.getName(), lvl);
        }

        json.put("baseLevels", baselvls);
        json.put("boostedLevels", boostlvls);
        json.put("specialPercent", this.specialPercent);
        json.put("runEnergy", this.runEnergy);
        json.put("combatLevel", this.combatLevel);
        json.put("totalLevel", this.totalLevel);
        return json;
    }

    public void parseJSON(JSONObject json){
        this.specialPercent = 0;
        this.runEnergy = 0;
        this.combatLevel = 0;
        this.totalLevel = 0;

        JSONObject baselvls = json.getJSONObject("baseLevels");
        Iterator<String> keys = baselvls.keys();

        this.baseLevels = new HashMap<>();
        while(keys.hasNext())
        {
            String key = keys.next();
            if(key.equals("Attack"))
            {
                this.baseLevels.put(Skill.ATTACK, baselvls.getInt(key));
            }
            if(key.equals("Defence"))
            {
                this.baseLevels.put(Skill.DEFENCE, baselvls.getInt(key));
            }
            if(key.equals("Strength"))
            {
                this.baseLevels.put(Skill.STRENGTH, baselvls.getInt(key));
            }
            if(key.equals("Hitpoints"))
            {
                this.baseLevels.put(Skill.HITPOINTS, baselvls.getInt(key));
            }
            if(key.equals("Ranged"))
            {
                this.baseLevels.put(Skill.RANGED, baselvls.getInt(key));
            }
            if(key.equals("Prayer"))
            {
                this.baseLevels.put(Skill.PRAYER, baselvls.getInt(key));
            }
            if(key.equals("Magic"))
            {
                this.baseLevels.put(Skill.MAGIC, baselvls.getInt(key));
            }
            if(key.equals("Cooking"))
            {
                this.baseLevels.put(Skill.COOKING, baselvls.getInt(key));
            }
            if(key.equals("Woodcutting"))
            {
                this.baseLevels.put(Skill.WOODCUTTING, baselvls.getInt(key));
            }
            if(key.equals("Fletching"))
            {
                this.baseLevels.put(Skill.FLETCHING, baselvls.getInt(key));
            }
            if(key.equals("Fishing"))
            {
                this.baseLevels.put(Skill.FISHING, baselvls.getInt(key));
            }
            if(key.equals("Firemaking"))
            {
                this.baseLevels.put(Skill.FIREMAKING, baselvls.getInt(key));
            }
            if(key.equals("Crafting"))
            {
                this.baseLevels.put(Skill.CRAFTING, baselvls.getInt(key));
            }
            if(key.equals("Smithing"))
            {
                this.baseLevels.put(Skill.SMITHING, baselvls.getInt(key));
            }
            if(key.equals("Mining"))
            {
                this.baseLevels.put(Skill.MINING, baselvls.getInt(key));
            }
            if(key.equals("Herblore"))
            {
                this.baseLevels.put(Skill.HERBLORE, baselvls.getInt(key));
            }
            if(key.equals("Agility"))
            {
                this.baseLevels.put(Skill.AGILITY, baselvls.getInt(key));
            }
            if(key.equals("Thieving"))
            {
                this.baseLevels.put(Skill.THIEVING, baselvls.getInt(key));
            }
            if(key.equals("Slayer"))
            {
                this.baseLevels.put(Skill.SLAYER, baselvls.getInt(key));
            }
            if(key.equals("Farming"))
            {
                this.baseLevels.put(Skill.FARMING, baselvls.getInt(key));
            }
            if(key.equals("Runecraft"))
            {
                this.baseLevels.put(Skill.RUNECRAFT, baselvls.getInt(key));
            }
            if(key.equals("Hunter"))
            {
                this.baseLevels.put(Skill.HUNTER, baselvls.getInt(key));
            }
            if(key.equals("Construction"))
            {
                this.baseLevels.put(Skill.CONSTRUCTION, baselvls.getInt(key));
            }
            if(key.equals("Overall"))
            {
                this.baseLevels.put(Skill.OVERALL, baselvls.getInt(key));
            }

        }

        JSONObject boostlvls = json.getJSONObject("boostedLevels");
        keys = boostlvls.keys();

        this.boostedLevels = new HashMap<>();
        while(keys.hasNext())
        {
            String key = keys.next();
            if(key.equals("Attack"))
            {
                this.boostedLevels.put(Skill.ATTACK, boostlvls.getInt(key));
            }
            if(key.equals("Defence"))
            {
                this.boostedLevels.put(Skill.DEFENCE, boostlvls.getInt(key));
            }
            if(key.equals("Strength"))
            {
                this.boostedLevels.put(Skill.STRENGTH, boostlvls.getInt(key));
            }
            if(key.equals("Hitpoints"))
            {
                this.boostedLevels.put(Skill.HITPOINTS, boostlvls.getInt(key));
            }
            if(key.equals("Ranged"))
            {
                this.boostedLevels.put(Skill.RANGED, boostlvls.getInt(key));
            }
            if(key.equals("Prayer"))
            {
                this.boostedLevels.put(Skill.PRAYER, boostlvls.getInt(key));
            }
            if(key.equals("Magic"))
            {
                this.boostedLevels.put(Skill.MAGIC, boostlvls.getInt(key));
            }
            if(key.equals("Cooking"))
            {
                this.boostedLevels.put(Skill.COOKING, boostlvls.getInt(key));
            }
            if(key.equals("Woodcutting"))
            {
                this.boostedLevels.put(Skill.WOODCUTTING, boostlvls.getInt(key));
            }
            if(key.equals("Fletching"))
            {
                this.boostedLevels.put(Skill.FLETCHING, boostlvls.getInt(key));
            }
            if(key.equals("Fishing"))
            {
                this.boostedLevels.put(Skill.FISHING, boostlvls.getInt(key));
            }
            if(key.equals("Firemaking"))
            {
                this.boostedLevels.put(Skill.FIREMAKING, boostlvls.getInt(key));
            }
            if(key.equals("Crafting"))
            {
                this.boostedLevels.put(Skill.CRAFTING, boostlvls.getInt(key));
            }
            if(key.equals("Smithing"))
            {
                this.boostedLevels.put(Skill.SMITHING, boostlvls.getInt(key));
            }
            if(key.equals("Mining"))
            {
                this.boostedLevels.put(Skill.MINING, boostlvls.getInt(key));
            }
            if(key.equals("Herblore"))
            {
                this.boostedLevels.put(Skill.HERBLORE, boostlvls.getInt(key));
            }
            if(key.equals("Agility"))
            {
                this.boostedLevels.put(Skill.AGILITY, boostlvls.getInt(key));
            }
            if(key.equals("Thieving"))
            {
                this.boostedLevels.put(Skill.THIEVING, boostlvls.getInt(key));
            }
            if(key.equals("Slayer"))
            {
                this.boostedLevels.put(Skill.SLAYER, boostlvls.getInt(key));
            }
            if(key.equals("Farming"))
            {
                this.boostedLevels.put(Skill.FARMING, boostlvls.getInt(key));
            }
            if(key.equals("Runecraft"))
            {
                this.boostedLevels.put(Skill.RUNECRAFT, boostlvls.getInt(key));
            }
            if(key.equals("Hunter"))
            {
                this.boostedLevels.put(Skill.HUNTER, boostlvls.getInt(key));
            }
            if(key.equals("Construction"))
            {
                this.boostedLevels.put(Skill.CONSTRUCTION, boostlvls.getInt(key));
            }
            if(key.equals("Overall"))
            {
                this.boostedLevels.put(Skill.OVERALL, boostlvls.getInt(key));
            }

        }

        this.specialPercent = json.getInt("specialPercent");
        this.runEnergy = json.getInt("runEnergy");
        this.combatLevel = json.getInt("combatLevel");
        this.totalLevel = json.getInt("totalLevel");
    }
}
