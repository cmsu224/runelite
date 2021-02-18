//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.specialcounterextended;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.runelite.client.ui.overlay.infobox.Counter;

class SpecialCounter extends Counter {
    private final SpecialCounterExtendedPlugin plugin;
    private SpecialWeapon weapon;
    private final Map<String, Integer> partySpecs = new HashMap();

    SpecialCounter(BufferedImage image, SpecialCounterExtendedPlugin plugin, int hitValue, SpecialWeapon weapon) {
        super(image, plugin, hitValue);
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
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.buildTooltip(hitValue));
            Iterator var3 = this.partySpecs.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, Integer> entry = (Entry)var3.next();
                stringBuilder.append("</br>").append(entry.getKey() == null ? "You" : (String)entry.getKey()).append(": ").append(this.buildTooltip((Integer)entry.getValue()));
            }

            return stringBuilder.toString();
        }
    }

    private String buildTooltip(int hitValue) {
        if (!this.weapon.isDamage()) {
            return hitValue == 1 ? this.weapon.getName() + " special has hit " + hitValue + " time." : this.weapon.getName() + " special has hit " + hitValue + " times.";
        } else {
            return this.weapon.getName() + " special has hit " + hitValue + " total.";
        }
    }

    Map<String, Integer> getPartySpecs() {
        return this.partySpecs;
    }
}
