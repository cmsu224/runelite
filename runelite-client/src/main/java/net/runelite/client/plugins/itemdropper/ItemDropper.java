//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.externals.itemdropper;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.queries.InventoryWidgetItemQuery;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.util.HotkeyListener;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Extension
@PluginDescriptor(
        name = "Item Dropper",
        description = "Drops selected items for you.",
        tags = {"item", "drop", "dropper", "bot"},
        type = PluginType.UTILITY
)
@PluginDependency(ExtUtils.class)
public class ItemDropper extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(ItemDropper.class);
    @Inject
    private Client client;
    @Inject
    private ConfigManager configManager;
    @Inject
    private ItemDropperConfig config;
    @Inject
    private KeyManager keyManager;
    @Inject
    private MenuManager menuManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private ExtUtils utils;
    private final List<WidgetItem> items = new ArrayList();
    private final Set<Integer> ids = new HashSet();
    private final Set<String> names = new HashSet();
    private boolean iterating;
    private int iterTicks;
    private Robot robot;
    private BlockingQueue<Runnable> queue = new ArrayBlockingQueue(1);
    private ThreadPoolExecutor executorService;
    private final HotkeyListener toggle;

    public ItemDropper() {
        this.executorService = new ThreadPoolExecutor(1, 1, 25L, TimeUnit.SECONDS, this.queue, new DiscardPolicy());
        this.toggle = new HotkeyListener(() -> {
            return this.config.toggle();
        }) {
            public void hotkeyPressed() {
                List<WidgetItem> list = (new InventoryWidgetItemQuery()).idEquals(ItemDropper.this.ids).result(ItemDropper.this.client).list;
                ItemDropper.this.items.addAll(list);
            }
        };
    }

    @Provides
    ItemDropperConfig provideConfig(ConfigManager configManager) {
        return (ItemDropperConfig)configManager.getConfig(ItemDropperConfig.class);
    }

    protected void startUp() throws AWTException {
        this.robot = new Robot();
        this.keyManager.registerKeyListener(this.toggle);
        this.updateConfig();
    }

    protected void shutDown() {
        this.keyManager.unregisterKeyListener(this.toggle);
        this.robot = null;
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("ItemDropperConfig")) {
            this.updateConfig();
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.items.isEmpty()) {
            if (this.iterating) {
                ++this.iterTicks;
                if (this.iterTicks > 10) {
                    this.iterating = false;
                    this.clearNames();
                }
            } else if (this.iterTicks > 0) {
                this.iterTicks = 0;
            }

        } else {
            this.dropItems(this.items);
            this.items.clear();
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getItemContainer() == this.client.getItemContainer(InventoryID.INVENTORY)) {
            int quant = 0;
            Item[] var3 = event.getItemContainer().getItems();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Item item = var3[var5];
                if (this.ids.contains(item.getId())) {
                    ++quant;
                }
            }

            if (this.iterating && quant == 0) {
                this.iterating = false;
                this.clearNames();
            }

        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            this.updateConfig();
        }

    }

    private void dropItems(List<WidgetItem> dropList) {
        this.iterating = true;
        Iterator var2 = this.names.iterator();

        while(var2.hasNext()) {
            String name = (String)var2.next();
            this.menuManager.addPriorityEntry("drop", name);
            this.menuManager.addPriorityEntry("release", name);
            this.menuManager.addPriorityEntry("destroy", name);
        }

        List<Rectangle> rects = new ArrayList();
        Iterator var6 = dropList.iterator();

        while(var6.hasNext()) {
            WidgetItem item = (WidgetItem)var6.next();
            rects.add(item.getCanvasBounds());
        }

        this.executorService.submit(() -> {
            Iterator var2 = rects.iterator();

            while(var2.hasNext()) {
                Rectangle rect = (Rectangle)var2.next();
                this.utils.click(rect);

                try {
                    Thread.sleep((long)((int)this.getMillis()));
                } catch (InterruptedException var5) {
                    var5.printStackTrace();
                }
            }

        });
    }

    private long getMillis() {
        return (long)(Math.random() * (double)this.config.randLow() + (double)this.config.randHigh());
    }

    private void updateConfig() {
        this.ids.clear();
        int[] var1 = this.utils.stringToIntArray(this.config.items());
        int i = var1.length;

        for(int var3 = 0; var3 < i; ++var3) {
            int i = var1[var3];
            this.ids.add(i);
        }

        this.clearNames();
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.names.clear();
            Iterator var5 = this.ids.iterator();

            while(var5.hasNext()) {
                i = (Integer)var5.next();
                String name = Text.standardize(this.itemManager.getItemDefinition(i).getName());
                this.names.add(name);
            }
        }

    }

    private void clearNames() {
        Iterator var1 = this.names.iterator();

        while(var1.hasNext()) {
            String name = (String)var1.next();
            this.menuManager.removePriorityEntry("drop", name);
            this.menuManager.removePriorityEntry("release", name);
            this.menuManager.removePriorityEntry("destroy", name);
        }

    }
}
