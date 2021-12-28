//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra;

import net.runelite.api.Projectile;

import java.lang.reflect.Field;

public class ClientInterface {
    private static Field interactingIndex = null;

    public ClientInterface() {
    }

    public static int getInteractingIndex(Projectile p) {
        try {
            interactingIndex = p.getClass().getDeclaredFields()[10];
            interactingIndex.setAccessible(true);
            return (Integer)interactingIndex.get(p) * 1305386011;
        } catch (IllegalAccessException var2) {
            var2.printStackTrace();
            return -1;
        }
    }
}
