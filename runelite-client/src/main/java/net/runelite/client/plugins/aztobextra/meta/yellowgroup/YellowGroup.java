//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta.yellowgroup;

import java.util.ArrayList;
import net.runelite.api.Point;

public class YellowGroup {
    public Point a;
    public Point b;
    public Point c;

    public YellowGroup(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public YellowGroup(ArrayList<Point> points) {
        this.a = (Point)points.get(0);
        this.b = (Point)points.get(1);
        this.c = (Point)points.get(2);
    }
}
