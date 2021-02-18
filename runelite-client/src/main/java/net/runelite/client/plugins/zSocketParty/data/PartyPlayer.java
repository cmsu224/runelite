//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.ws.PartyMember;

import java.util.Arrays;

@Data
@EqualsAndHashCode()
public class PartyPlayer
{
    private transient PartyMember member;
    private String username;
    private Stats stats;
    private GameItem[] inventory;
    private GameItem[] equipment;
    private Prayers prayers;

    public PartyPlayer(final PartyMember member, final Client client, final ItemManager itemManager)
    {
        this.member = member;
        this.username = null;
        this.stats = null;
        this.inventory = new GameItem[28];
        this.equipment = new GameItem[EquipmentInventorySlot.AMMO.getSlotIdx() + 1];
        this.prayers = null;

        updatePlayerInfo(client, itemManager);
    }

    public PartyPlayer(JSONObject partyplayer)
    {
        this.member = member;
        this.username = null;
        this.stats = null;
        this.inventory = new GameItem[28];
        this.equipment = new GameItem[EquipmentInventorySlot.AMMO.getSlotIdx() + 1];
        this.prayers = null;

        parseJSON(partyplayer);
    }

    public void updatePlayerInfo(final Client client, final ItemManager itemManager)
    {
        // Player is logged in
        if (client.getLocalPlayer() != null)
        {
            this.username = client.getLocalPlayer().getName();
            this.stats = new Stats(client);

            final ItemContainer invi = client.getItemContainer(InventoryID.INVENTORY);
            if (invi != null)
            {
                this.inventory = GameItem.convertItemsToGameItems(invi.getItems(), itemManager);
            }

            final ItemContainer equip = client.getItemContainer(InventoryID.EQUIPMENT);
            if (equip != null)
            {
                this.equipment = GameItem.convertItemsToGameItems(equip.getItems(), itemManager);
            }

            if (this.prayers == null)
            {
                prayers = new Prayers(client);
            }
        }
    }

    public int getSkillBoostedLevel(final Skill skill)
    {
        if (stats == null)
        {
            return 0;
        }

        return stats.getBoostedLevels().get(skill);
    }

    public int getSkillRealLevel(final Skill skill)
    {
        if (stats == null)
        {
            return 0;
        }

        return stats.getBaseLevels().get(skill);
    }

    public void setSkillsBoostedLevel(final Skill skill, final int level)
    {
        if (stats == null)
        {
            return;
        }

        stats.getBoostedLevels().put(skill, level);
    }

    public void setSkillsRealLevel(final Skill skill, final int level)
    {
        if (stats == null)
        {
            return;
        }

        stats.getBaseLevels().put(skill, level);
    }

    public PartyMember getMember() {
        return this.member;
    }

    public String getUsername() {
        return this.username;
    }

    public Stats getStats() {
        return this.stats;
    }

    public GameItem[] getInventory() {
        return this.inventory;
    }

    public GameItem[] getEquipment() {
        return this.equipment;
    }

    public Prayers getPrayers() {
        return this.prayers;
    }

    public void setMember(PartyMember member) {
        this.member = member;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public void setInventory(GameItem[] inventory) {
        this.inventory = inventory;
    }

    public void setEquipment(GameItem[] equipment) {
        this.equipment = equipment;
    }

    public void setPrayers(Prayers prayers) {
        this.prayers = prayers;
    }

    public String toString() {
        return "PartyPlayer(member=" + this.getMember() + ", username=" + this.getUsername() + ", stats=" + this.getStats() + ", inventory=" + Arrays.deepToString(this.getInventory()) + ", equipment=" + Arrays.deepToString(this.getEquipment()) + ", prayers=" + this.getPrayers() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PartyPlayer)) {
            return false;
        } else {
            PartyPlayer other = (PartyPlayer)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label57: {
                    Object this$username = this.getUsername();
                    Object other$username = other.getUsername();
                    if (this$username == null) {
                        if (other$username == null) {
                            break label57;
                        }
                    } else if (this$username.equals(other$username)) {
                        break label57;
                    }

                    return false;
                }

                Object this$stats = this.getStats();
                Object other$stats = other.getStats();
                if (this$stats == null) {
                    if (other$stats != null) {
                        return false;
                    }
                } else if (!this$stats.equals(other$stats)) {
                    return false;
                }

                if (!Arrays.deepEquals(this.getInventory(), other.getInventory())) {
                    return false;
                } else if (!Arrays.deepEquals(this.getEquipment(), other.getEquipment())) {
                    return false;
                } else {
                    Object this$prayers = this.getPrayers();
                    Object other$prayers = other.getPrayers();
                    if (this$prayers == null) {
                        if (other$prayers != null) {
                            return false;
                        }
                    } else if (!this$prayers.equals(other$prayers)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof PartyPlayer;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = super.hashCode();
        Object $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        Object $stats = this.getStats();
        result = result * 59 + ($stats == null ? 43 : $stats.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getInventory());
        result = result * 59 + Arrays.deepHashCode(this.getEquipment());
        Object $prayers = this.getPrayers();
        result = result * 59 + ($prayers == null ? 43 : $prayers.hashCode());
        return result;
    }

    public JSONObject toJSON()
    {
        JSONObject json = new JSONObject();

        if (this.getUsername() != null)
        {
            json.put("username", this.getUsername());
        }
        if (this.getStats() != null) {
            json.put("stats", this.getStats().toJSON());
        }

        JSONArray inven = new JSONArray();
        for (int i=0; i<this.getInventory().length; i++){
            if(this.getInventory()[i] != null) {
                JSONObject invenItem = new JSONObject();
                invenItem.put("placement", i);
                invenItem.put("id", this.inventory[i].getId());
                invenItem.put("name", this.inventory[i].getName());
                invenItem.put("price", this.inventory[i].getPrice());
                invenItem.put("qty", this.inventory[i].getQty());
                invenItem.put("stack", this.inventory[i].isStackable());
                inven.put(i, invenItem);
            }
        }
        json.put("iventory", inven);

        JSONArray equip = new JSONArray();
        for (int i=0; i<this.getEquipment().length; i++){
            if(this.getEquipment()[i] != null) {
                JSONObject invenItem = new JSONObject();
                invenItem.put("placement", i);
                invenItem.put("id", this.equipment[i].getId());
                invenItem.put("name", this.equipment[i].getName());
                invenItem.put("price", this.equipment[i].getPrice());
                invenItem.put("qty", this.equipment[i].getQty());
                invenItem.put("stack", this.equipment[i].isStackable());
                equip.put(i, invenItem);
            }
        }
        json.put("equipment", equip);

        if (this.getPrayers() != null)
        {
            json.put("prayers", this.getPrayers().toJSON());
        }
        return json;
    }

    public void parseJSON(JSONObject json)
    {
        //Username
        this.username = json.getString("username");

        //Inventory
        GameItem[] inventry = new GameItem[28];
        JSONArray inven = json.getJSONArray("iventory");
        for(Object o: inven){
            if ( o instanceof JSONObject ) {
                JSONObject invenItem = (JSONObject)o;

                int invPlacement = invenItem.getInt("placement");
                int ID = invenItem.getInt("id");
                String nm = invenItem.getString("name");
                int prce = invenItem.getInt("price");
                int qty = invenItem.getInt("qty");

                GameItem ivnItem = new GameItem(ID, qty, nm, false, prce);
                inventry[invPlacement] = ivnItem;
            }
        }
        this.inventory = inventry;

        //Equipment
        GameItem[] equip = new GameItem[28];
        JSONArray equips = json.getJSONArray("equipment");
        for(Object o: equips){
            if ( o instanceof JSONObject ) {
                JSONObject invenItem = (JSONObject)o;

                int invPlacement = invenItem.getInt("placement");
                int ID = invenItem.getInt("id");
                String nm = invenItem.getString("name");
                int prce = invenItem.getInt("price");
                int qty = invenItem.getInt("qty");

                GameItem ivnItem = new GameItem(ID, qty, nm, false, prce);
                equip[invPlacement] = ivnItem;
            }
        }
        this.equipment = equip;

        this.stats = new Stats(json.getJSONObject("stats"));
        this.prayers = new Prayers(json.getJSONObject("prayers"));
    }
}
