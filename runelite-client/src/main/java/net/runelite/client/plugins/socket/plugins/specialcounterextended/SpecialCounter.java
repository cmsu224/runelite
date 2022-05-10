/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.infobox.Counter
 */
package net.runelite.client.plugins.socket.plugins.specialcounterextended;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.specialcounterextended.SpecialCounterExtendedPlugin;
import net.runelite.client.plugins.socket.plugins.specialcounterextended.SpecialWeapon;
import net.runelite.client.ui.overlay.infobox.Counter;

class SpecialCounter
extends Counter {
    private final SpecialCounterExtendedPlugin plugin;
    private SpecialWeapon weapon;
    private final Map<String, Integer> partySpecs = new HashMap<String, Integer>();

    SpecialCounter(BufferedImage image, SpecialCounterExtendedPlugin plugin, int hitValue, SpecialWeapon weapon) {
        super(image, (Plugin)plugin, hitValue);
        this.plugin = plugin;
        this.weapon = weapon;
    }

    void addHits(double hit) {
        int count = this.getCount();
        this.setCount(count + (int)hit);
    }

    public String getTooltip() {
        int hitValue = this.getCount();
        if (this.partySpecs.isEmpty()) {
            return this.buildTooltip(hitValue);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.buildTooltip(hitValue));
        for (Map.Entry<String, Integer> entry : this.partySpecs.entrySet()) {
            stringBuilder.append("</br>").append(entry.getKey() == null ? "You" : entry.getKey()).append(": ").append(this.buildTooltip(entry.getValue()));
        }
        return stringBuilder.toString();
    }

    private String buildTooltip(int hitValue) {
        if (!this.weapon.isDamage()) {
            if (hitValue == 1) {
                return this.weapon.getName() + " special has hit " + hitValue + " time.";
            }
            return this.weapon.getName() + " special has hit " + hitValue + " times.";
        }
        return this.weapon.getName() + " special has hit " + hitValue + " total.";
    }

    Map<String, Integer> getPartySpecs() {
        return this.partySpecs;
    }
}

