/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.ui.overlay.components.PanelComponent
 */
package net.runelite.client.plugins.socket.plugins.playerstatus;

import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class PlayerStatus {
    private final PanelComponent panel = new PanelComponent();
    private int health;
    private int maxHealth;
    private int prayer;
    private int maxPrayer;
    private int run;
    private int special;

    private PlayerStatus() {
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("health", this.health);
        json.put("max-health", this.maxHealth);
        json.put("prayer", this.prayer);
        json.put("max-prayer", this.maxPrayer);
        json.put("run", this.run);
        json.put("special", this.special);
        return json;
    }

    public void parseJSON(JSONObject json) {
        this.health = json.getInt("health");
        this.maxHealth = json.getInt("max-health");
        this.prayer = json.getInt("prayer");
        this.maxPrayer = json.getInt("max-prayer");
        this.run = json.getInt("run");
        this.special = json.getInt("special");
    }

    public static PlayerStatus fromJSON(JSONObject json) {
        PlayerStatus ps = new PlayerStatus();
        ps.parseJSON(json);
        return ps;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setPrayer(int prayer) {
        this.prayer = prayer;
    }

    public void setMaxPrayer(int maxPrayer) {
        this.maxPrayer = maxPrayer;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public PanelComponent getPanel() {
        return this.panel;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getPrayer() {
        return this.prayer;
    }

    public int getMaxPrayer() {
        return this.maxPrayer;
    }

    public int getRun() {
        return this.run;
    }

    public int getSpecial() {
        return this.special;
    }

    public PlayerStatus(int health, int maxHealth, int prayer, int maxPrayer, int run, int special) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.prayer = prayer;
        this.maxPrayer = maxPrayer;
        this.run = run;
        this.special = special;
    }
}

