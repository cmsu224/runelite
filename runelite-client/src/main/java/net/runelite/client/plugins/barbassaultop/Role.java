//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.barbarianassault;

import net.runelite.api.widgets.WidgetInfo;

enum Role {
    ATTACKER(WidgetInfo.BA_ATK_ROLE_TEXT, WidgetInfo.BA_ATK_ROLE_SPRITE),
    DEFENDER(WidgetInfo.BA_DEF_ROLE_TEXT, WidgetInfo.BA_DEF_ROLE_SPRITE),
    COLLECTOR(WidgetInfo.BA_COLL_ROLE_TEXT, WidgetInfo.BA_COLL_ROLE_SPRITE),
    HEALER(WidgetInfo.BA_HEAL_ROLE_TEXT, WidgetInfo.BA_HEAL_ROLE_SPRITE);

    private final WidgetInfo roleText;
    private final WidgetInfo roleSprite;

    public String toString() {
        return this.name();
    }

    private Role(WidgetInfo roleText, WidgetInfo roleSprite) {
        this.roleText = roleText;
        this.roleSprite = roleSprite;
    }

    public WidgetInfo getRoleText() {
        return this.roleText;
    }

    public WidgetInfo getRoleSprite() {
        return this.roleSprite;
    }
}
