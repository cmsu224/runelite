package net.runelite.client.plugins.autoclicker;


import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.autoclicker.PrayerMap;
import net.runelite.client.plugins.autoclicker.Tab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PluginDescriptor(
        name = "ExtUtils",
        hidden = true
)
@Singleton
public class ExtUtils extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(ExtUtils.class);
    @Inject
    private Client client;

    public ExtUtils() {
    }

    protected void startUp() {
    }

    protected void shutDown() {
    }

    public int[] stringToIntArray(String string) {
        return Arrays.stream(string.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
    }


    public List<Widget> getEquippedItems(int[] itemIds) {
        assert this.client.isClientThread();

        Widget equipmentWidget = this.client.getWidget(WidgetInfo.EQUIPMENT);
        List<Integer> equippedIds = new ArrayList();
        int[] var4 = itemIds;
        int var5 = itemIds.length;

        int var6;
        int i;
        for(var6 = 0; var6 < var5; ++var6) {
            i = var4[var6];
            equippedIds.add(i);
        }

        List<Widget> equipped = new ArrayList();
        if (equipmentWidget.getStaticChildren() != null) {
            Widget[] var14 = equipmentWidget.getStaticChildren();
            var6 = var14.length;

            for(i = 0; i < var6; ++i) {
                Widget widgets = var14[i];
                Widget[] var9 = widgets.getDynamicChildren();
                int var10 = var9.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    Widget items = var9[var11];
                    if (equippedIds.contains(items.getItemId())) {
                        equipped.add(items);
                    }
                }
            }
        } else {
            log.error("Children is Null!");
        }

        return equipped;
    }

    public int getTabHotkey(Tab tab) {
        assert this.client.isClientThread();

        int var = this.client.getVarbitValue(this.client.getVarps(), tab.getVarbit());
        boolean offset = true;
        switch(var) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return var + 111;
            case 13:
                return 27;
            default:
                return -1;
        }
    }


    public WidgetInfo getPrayerWidgetInfo(String spell) {
        assert this.client.isClientThread();

        return PrayerMap.getWidget(spell);
    }


    public Widget getPrayerWidget(String spell) {
        assert this.client.isClientThread();

        return this.client.getWidget(PrayerMap.getWidget(spell));
    }

    public void typeString(String string) {
        assert !this.client.isClientThread();

        char[] var2 = string.toCharArray();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            char c = var2[var4];
            this.pressKey(c);
        }

    }

    public void pressKey(char key) {
        this.keyEvent(401, key);
        this.keyEvent(402, key);
        this.keyEvent(400, key);
    }

    private void keyEvent(int id, char key) {
        KeyEvent e = new KeyEvent(this.client.getCanvas(), id, System.currentTimeMillis(), 0, 0, key);
        this.client.getCanvas().dispatchEvent(e);
    }

    public void click(Rectangle rectangle) {
        assert !this.client.isClientThread();

        Point point = this.getClickPoint(rectangle);
        this.click(point);
    }

    public void click(Point p) {
        assert !this.client.isClientThread();

        if (this.client.isStretchedEnabled()) {
            Dimension stretched = this.client.getStretchedDimensions();
            Dimension real = this.client.getRealDimensions();
            double width = (double)stretched.width / real.getWidth();
            double height = (double)stretched.height / real.getHeight();
            Point point = new Point((int)((double)p.getX() * width), (int)((double)p.getY() * height));
            this.mouseEvent(501, point);
            this.mouseEvent(502, point);
            this.mouseEvent(500, point);
        } else {
            this.mouseEvent(501, p);
            this.mouseEvent(502, p);
            this.mouseEvent(500, p);
        }
    }

    public Point getClickPoint(Rectangle rect) {
        int x = (int)(rect.getX() + (double)this.getRandomIntBetweenRange((int)rect.getWidth() / 6 * -1, (int)rect.getWidth() / 6) + rect.getWidth() / 2.0D);
        int y = (int)(rect.getY() + (double)this.getRandomIntBetweenRange((int)rect.getHeight() / 6 * -1, (int)rect.getHeight() / 6) + rect.getHeight() / 2.0D);
        return new Point(x, y);
    }

    public int getRandomIntBetweenRange(int min, int max) {
        return (int)(Math.random() * (double)(max - min + 1) + (double)min);
    }

    private void mouseEvent(int id, Point point) {
        MouseEvent e = new MouseEvent(this.client.getCanvas(), id, System.currentTimeMillis(), 0, point.getX(), point.getY(), 1, false, 1);
        this.client.getCanvas().dispatchEvent(e);
    }
}
