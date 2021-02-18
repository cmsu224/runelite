//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.specialcounterextended;

enum SpecialWeapon {
    DRAGON_WARHAMMER("Dragon Warhammer", 13576, false),
    ARCLIGHT("Arclight", 19675, false),
    DARKLIGHT("Darklight", 6746, false),
    BANDOS_GODSWORD("Bandos Godsword", 11804, true),
    BANDOS_GODSWORD_OR("Bandos Godsword", 20370, true),
    DAWNBRINGER("Dawnbringer", 22516, true);

    private final String name;
    private final int itemID;
    private final boolean damage;

    private SpecialWeapon(String name, int itemID, boolean damage) {
        this.name = name;
        this.itemID = itemID;
        this.damage = damage;
    }

    public String getName() {
        return this.name;
    }

    public int getItemID() {
        return this.itemID;
    }

    public boolean isDamage() {
        return this.damage;
    }
}
