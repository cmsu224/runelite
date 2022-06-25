/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Alpha
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 *  net.runelite.client.config.Range
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketba;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(value="socketBa")
public interface SocketBAConfig
extends Config {
    @ConfigItem(keyName="roleInfobox", name="Role Infobox", description="Displays an infobox with your role and the correct call.", position=1)
    default public boolean roleInfobox() {
        return true;
    }

    @ConfigItem(keyName="highlightEggs", name="Highlight Eggs", description="Highlights the correct eggs tiles.", position=2)
    default public boolean highlightEggs() {
        return false;
    }

    @ConfigItem(keyName="leftClickEggs", name="Remove Wrong Eggs", description="Removes pick up option on incorrect eggs.", position=3)
    default public boolean leftClickEggs() {
        return true;
    }

    @ConfigItem(keyName="leftClickHorn", name="Left Click Horn", description="Sets left click option on horn to the correct call.", position=4)
    default public boolean leftClickHorn() {
        return true;
    }

    @ConfigItem(keyName="hideAttack", name="Hide Attack", description="Hides attack on rangers and fighters if you are not attacker or using incorrect arrow/spell/attack style", position=5)
    default public boolean hideAttack() {
        return false;
    }

    @ConfigItem(keyName="meleeStyleHighlight", name="Melee Style Highlight", description="Outlines the correct attack option in the combat tab", position=6)
    default public boolean meleeStyleHighlight() {
        return false;
    }

    @Alpha
    @ConfigItem(keyName="meleeStyleHighlightColor", name="Melee Highlight Color", description="Sets the color of melee style highlight", position=7)
    default public Color meleeStyleHighlightColor() {
        return Color.GREEN;
    }

    @ConfigItem(keyName="meleeSpecHighlight", name="Melee Spec Highlight", description="Highlights melee spec weapons depending on current call.<br>Accurate = Dihns    Others = Crystal Halberd", position=8)
    default public boolean meleeSpecHighlight() {
        return false;
    }

    @ConfigItem(keyName="correctItemHighlight", name="Correct Item Highlight", description="Highlight the correct items in your inventory for your role", position=9)
    default public correctItemHighlightMode correctItemHighlight() {
        return correctItemHighlightMode.OFF;
    }

    @Alpha
    @ConfigItem(keyName="correctItemColor", name="Correct Item Color", description="Sets the color for Correct Item Highlight", position=10)
    default public Color correctItemColor() {
        return Color.GREEN;
    }

    @ConfigItem(keyName="hideHpOverlay", name="Hide HP Overlay", description="Hides that big ass HP overlay", position=11)
    default public boolean hideHpOverlay() {
        return false;
    }

    @ConfigItem(keyName="highlightRoleNpcs", name="Highlight Role NPCs", description="Highlights the tile of the NPCs for your role", position=12)
    default public boolean highlightRoleNpcs() {
        return false;
    }

    @Alpha
    @ConfigItem(keyName="highlightRoleNpcsColor", name="Role NPC Color", description="Sets the color for Highlight Role NPCs", position=13)
    default public Color highlightRoleNpcsColor() {
        return new Color(0, 255, 255, 20);
    }

    @ConfigItem(keyName="removeUseFood", name="Remove Use Food", description="Removes the use food option on anything other than healers", position=14)
    default public boolean removeUseFood() {
        return false;
    }

    @ConfigItem(keyName="highlightVendingMachine", name="Highlight Vending Machine", description="Highlights correct vending machine and removes click for incorrect ones", position=15)
    default public boolean highlightVendingMachine() {
        return false;
    }

    @ConfigItem(keyName="vendingMachineColor", name="Vending Machine Color", description="Sets the color for Highlight Vending Machine", position=16)
    default public Color vendingMachineColor() {
        return new Color(0, 255, 255);
    }

    @Range(min=0, max=255)
    @ConfigItem(keyName="vendingMachineOpacity", name="Vending Machine Opacity", description="Sets the fill opacity for Highlight Vending Machine", position=16)
    default public int vendingMachineOpacity() {
        return 0;
    }

    @ConfigItem(keyName="cannonHelper", name="Cannon Helper", description="Helps see cannon clickbox better", position=17)
    default public boolean cannonHelper() {
        return false;
    }

    @ConfigItem(keyName="discoQueen", name="Disco Queen", description="Had to do it to em", position=18)
    default public boolean discoQueen() {
        return false;
    }

    @ConfigItem(keyName="bmMessages", name="Messages", description="Letting you know whats up.... definitely not toxic...", position=99)
    default public boolean bmMessages() {
        return false;
    }

    public static enum correctItemHighlightMode {
        OFF,
        BOX,
        OUTLINE,
        UNDERLINE;

    }
}

