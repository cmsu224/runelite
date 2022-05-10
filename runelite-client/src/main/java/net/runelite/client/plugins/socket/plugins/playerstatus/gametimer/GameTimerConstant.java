/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.socket.plugins.playerstatus.gametimer;

import java.util.regex.Pattern;

public class GameTimerConstant {
    public static final String ANTIFIRE_DRINK_MESSAGE = "You drink some of your antifire potion.";
    public static final String ANTIFIRE_EXPIRED_MESSAGE = "<col=7f007f>Your antifire potion has expired.</col>";
    public static final String ANTIVENOM_DRINK_MESSAGE = "You drink some of your antivenom potion";
    public static final String CANNON_FURNACE_MESSAGE = "You add the furnace.";
    public static final String CANNON_PICKUP_MESSAGE = "You pick up the cannon. It's really heavy.";
    public static final String CANNON_REPAIR_MESSAGE = "You repair your cannon, restoring it to working order.";
    public static final String CHARGE_EXPIRED_MESSAGE = "<col=ef1020>Your magical charge fades away.</col>";
    public static final String CHARGE_MESSAGE = "<col=ef1020>You feel charged with magic power.</col>";
    public static final String DIVINE_DRINK_MESSAGE = "You drink some of your divine";
    public static final String DIVINE_EXPIRED_MESSAGE = "The effects of the divine potion have worn off.";
    public static final String EXTENDED_ANTIFIRE_DRINK_MESSAGE = "You drink some of your extended antifire potion.";
    public static final String EXTENDED_SUPER_ANTIFIRE_DRINK_MESSAGE = "You drink some of your extended super antifire potion.";
    public static final String FROZEN_MESSAGE = "<col=ef1020>You have been frozen!</col>";
    public static final String GOD_WARS_ALTAR_MESSAGE = "you recharge your prayer.";
    public static final String IMBUED_HEART_READY_MESSAGE = "<col=ef1020>Your imbued heart has regained its magical power.</col>";
    public static final String MAGIC_IMBUE_EXPIRED_MESSAGE = "Your Magic Imbue charge has ended.";
    public static final String MAGIC_IMBUE_MESSAGE = "You are charged to combine runes!";
    public static final String SANFEW_SERUM_DRINK_MESSAGE = "You drink some of your Sanfew Serum.";
    public static final String STAFF_OF_THE_DEAD_SPEC_EXPIRED_MESSAGE = "Your protection fades away";
    public static final String STAFF_OF_THE_DEAD_SPEC_MESSAGE = "Spirits of deceased evildoers offer you their protection";
    public static final String STAMINA_DRINK_MESSAGE = "You drink some of your stamina potion.";
    public static final String STAMINA_SHARED_DRINK_MESSAGE = "You have received a shared dose of stamina potion.";
    public static final String STAMINA_EXPIRED_MESSAGE = "<col=8f4808>Your stamina potion has expired.</col>";
    public static final String SUPER_ANTIFIRE_DRINK_MESSAGE = "You drink some of your super antifire potion";
    public static final String SUPER_ANTIFIRE_EXPIRED_MESSAGE = "<col=7f007f>Your super antifire potion has expired.</col>";
    public static final String SUPER_ANTIVENOM_DRINK_MESSAGE = "You drink some of your super antivenom potion";
    public static final Pattern DEADMAN_HALF_TELEBLOCK_PATTERN = Pattern.compile("<col=4f006f>A Tele Block spell has been cast on you by (.+). It will expire in 1 minute, 15 seconds.</col>");
    public static final Pattern FULL_TELEBLOCK_PATTERN = Pattern.compile("<col=4f006f>A Tele Block spell has been cast on you by (.+). It will expire in 5 minutes, 0 seconds.</col>");
    public static final Pattern HALF_TELEBLOCK_PATTERN = Pattern.compile("<col=4f006f>A Tele Block spell has been cast on you by (.+). It will expire in 2 minutes, 30 seconds.</col>");
}

