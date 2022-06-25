/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketba;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

enum WeaponType {
    TYPE_0(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
    TYPE_1(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
    TYPE_2(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
    TYPE_3(AttackStyle.RANGING, AttackStyle.RANGING, null, AttackStyle.LONGRANGE),
    TYPE_4(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
    TYPE_5(AttackStyle.RANGING, AttackStyle.RANGING, null, AttackStyle.LONGRANGE),
    TYPE_6(AttackStyle.AGGRESSIVE, AttackStyle.RANGING, AttackStyle.CASTING, null),
    TYPE_7(AttackStyle.RANGING, AttackStyle.RANGING, null, AttackStyle.LONGRANGE),
    TYPE_8(AttackStyle.OTHER, AttackStyle.AGGRESSIVE, null, null),
    TYPE_9(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
    TYPE_10(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
    TYPE_11(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
    TYPE_12(AttackStyle.CONTROLLED, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
    TYPE_13(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
    TYPE_14(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
    TYPE_15(AttackStyle.CONTROLLED, AttackStyle.CONTROLLED, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
    TYPE_16(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
    TYPE_17(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
    TYPE_18(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE, AttackStyle.CASTING, AttackStyle.DEFENSIVE_CASTING),
    TYPE_19(AttackStyle.RANGING, AttackStyle.RANGING, null, AttackStyle.LONGRANGE),
    TYPE_20(AttackStyle.ACCURATE, AttackStyle.CONTROLLED, null, AttackStyle.DEFENSIVE),
    TYPE_21(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE, AttackStyle.CASTING, AttackStyle.DEFENSIVE_CASTING),
    TYPE_22(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
    TYPE_23(AttackStyle.CASTING, AttackStyle.CASTING, null, AttackStyle.DEFENSIVE_CASTING),
    TYPE_24(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
    TYPE_25(AttackStyle.CONTROLLED, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
    TYPE_26(AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, null, AttackStyle.AGGRESSIVE),
    TYPE_27(AttackStyle.ACCURATE, null, null, AttackStyle.OTHER);

    private final AttackStyle[] attackStyles;
    private static final Map<Integer, WeaponType> weaponTypes;

    private WeaponType(AttackStyle ... attackStyles) {
        this.attackStyles = attackStyles;
    }

    public AttackStyle[] getAttackStyles() {
        return this.attackStyles;
    }

    public static WeaponType getWeaponType(int id) {
        return weaponTypes.get(id);
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (WeaponType weaponType : WeaponType.values()) {
            builder.put((Object)weaponType.ordinal(), (Object)weaponType);
        }
        weaponTypes = builder.build();
    }
}

