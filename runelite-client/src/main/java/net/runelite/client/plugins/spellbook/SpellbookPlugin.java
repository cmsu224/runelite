//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.spellbook;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.inject.Provides;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Point;
import net.runelite.api.VarClientInt;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.VarClientIntChanged;
import net.runelite.api.events.WidgetMenuOptionClicked;
import net.runelite.api.util.Text;
import net.runelite.api.vars.InterfaceTab;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.MouseManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.menus.WidgetMenuOption;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.spellbook.SpellbookPlugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.MiscUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "Spellbook",
        enabledByDefault = false,
        description = "Modifications to the spellbook",
        tags = {"resize", "spell", "mobile", "lowers", "pvp", "skill", "level"}
)
public class SpellbookPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SpellbookPlugin.class);
    private static final int FULL_WIDTH = 184;
    private static final int FULL_HEIGHT = 240;
    private static final Gson GSON = new Gson();
    private static final String LOCK = "Disable";
    private static final String UNLOCK = "Enable";
    private static final String MENU_TARGET = "Reordering";
    private static final WidgetMenuOption FIXED_MAGIC_TAB_LOCK;
    private static final WidgetMenuOption FIXED_MAGIC_TAB_UNLOCK;
    private static final WidgetMenuOption RESIZABLE_MAGIC_TAB_LOCK;
    private static final WidgetMenuOption RESIZABLE_MAGIC_TAB_UNLOCK;
    private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_MAGIC_TAB_LOCK;
    private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_MAGIC_TAB_UNLOCK;
    private final Map<Integer, Spell> spells = new HashMap();
    private final SpellbookMouseListener mouseListener = new SpellbookMouseListener(this);
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ConfigManager configManager;
    @Inject
    private SpellbookConfig config;
    @Inject
    private MenuManager menuManager;
    @Inject
    private MouseManager mouseManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SpellbookDragOverlay overlay;
    private boolean dragging;
    private Widget draggingWidget;
    private Point draggingLocation;
    private ImmutableSet<String> notFilteredSpells;
    private Spellbook spellbook;
    private boolean mageTabOpen;

    public SpellbookPlugin() {
    }

    @Provides
    SpellbookConfig getConfig(ConfigManager configManager) {
        return (SpellbookConfig)configManager.getConfig(SpellbookConfig.class);
    }

    protected void startUp() {
        this.loadFilter();
        this.refreshMagicTabOption();
    }

    protected void shutDown() {
        this.clearMagicTabMenus();
        this.saveSpells();
        this.config.canDrag(false);
        this.mouseManager.unregisterMouseListener(this.mouseListener);
        this.mouseManager.unregisterMouseWheelListener(this.mouseListener);
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if ("spellbook".equals(event.getGroup())) {
            if (event.getKey().equals("filter")) {
                this.loadFilter();
            }

            this.runRebuild();
            this.refreshMagicTabOption();
        }
    }

    private void loadFilter() {
        this.notFilteredSpells = ImmutableSet.copyOf(Text.fromCSV(this.config.filter().toLowerCase()));
        this.saveSpells();
        this.loadSpells();
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            this.mageTabOpen = this.client.getVar(VarClientInt.INTERFACE_TAB) == InterfaceTab.SPELLBOOK.getId();
            this.refreshMagicTabOption();
        }

    }

    @Subscribe
    private void onVarCIntChanged(VarClientIntChanged event) {
        if (event.getIndex() == VarClientInt.INTERFACE_TAB.getIndex()) {
            boolean intfTab = this.client.getVar(VarClientInt.INTERFACE_TAB) == InterfaceTab.SPELLBOOK.getId();
            if (intfTab != this.mageTabOpen) {
                this.mageTabOpen = intfTab;
                this.refreshMagicTabOption();
            }

            if (this.config.canDrag() && this.client.getGameState() == GameState.LOGGED_IN) {
                boolean shouldBeAbleToDrag = this.mageTabOpen && this.client.getVar(Varbits.FILTER_SPELLBOOK) == 0;
                if (!shouldBeAbleToDrag) {
                    this.mouseManager.unregisterMouseListener(this.mouseListener);
                    this.mouseManager.unregisterMouseWheelListener(this.mouseListener);
                    this.config.canDrag(false);
                }
            }
        }
    }

    @Subscribe
    private void onWidgetMenuOptionClicked(WidgetMenuOptionClicked event) {
        if (event.getWidget() == WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB || event.getWidget() == WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB || event.getWidget() == WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_MAGIC_TAB) {
            this.saveSpells();
            this.loadSpells();
            if (event.getMenuOption().equals("Enable")) {
                this.config.canDrag(true);
                this.overlayManager.add(this.overlay);
                this.mouseManager.registerMouseListener(this.mouseListener);
                if (this.config.scroll()) {
                    this.mouseManager.registerMouseWheelListener(this.mouseListener);
                }
            } else if (event.getMenuOption().equals("Disable")) {
                this.config.canDrag(false);
                this.overlayManager.remove(this.overlay);
                this.mouseManager.unregisterMouseListener(this.mouseListener);
                this.mouseManager.unregisterMouseWheelListener(this.mouseListener);
            }

            this.refreshMagicTabOption();
        }
    }

    private void clearMagicTabMenus() {
        this.menuManager.removeManagedCustomMenu(FIXED_MAGIC_TAB_LOCK);
        this.menuManager.removeManagedCustomMenu(RESIZABLE_MAGIC_TAB_LOCK);
        this.menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_MAGIC_TAB_LOCK);
        this.menuManager.removeManagedCustomMenu(FIXED_MAGIC_TAB_UNLOCK);
        this.menuManager.removeManagedCustomMenu(RESIZABLE_MAGIC_TAB_UNLOCK);
        this.menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_MAGIC_TAB_UNLOCK);
    }

    private void refreshMagicTabOption() {
        this.clearMagicTabMenus();
        if (this.config.dragSpells() && this.mageTabOpen) {
            if (this.config.canDrag()) {
                this.menuManager.addManagedCustomMenu(FIXED_MAGIC_TAB_LOCK);
                this.menuManager.addManagedCustomMenu(RESIZABLE_MAGIC_TAB_LOCK);
                this.menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_MAGIC_TAB_LOCK);
            } else {
                this.menuManager.addManagedCustomMenu(FIXED_MAGIC_TAB_UNLOCK);
                this.menuManager.addManagedCustomMenu(RESIZABLE_MAGIC_TAB_UNLOCK);
                this.menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_MAGIC_TAB_UNLOCK);
            }

        }
    }

    @Subscribe
    private void onScriptCallbackEvent(ScriptCallbackEvent event) {
        if (this.client.getVar(Varbits.FILTER_SPELLBOOK) == 0 && this.config.enableMobile() && event.getEventName().toLowerCase().contains("spell")) {
            int[] iStack = this.client.getIntStack();
            int iStackSize = this.client.getIntStackSize();
            String[] sStack = this.client.getStringStack();
            int sStackSize = this.client.getStringStackSize();
            String var6 = event.getEventName();
            byte var7 = -1;
            switch(var6.hashCode()) {
                case -1360589955:
                    if (var6.equals("startSpellRedraw")) {
                        var7 = 0;
                    }
                    break;
                case -876460844:
                    if (var6.equals("resizeSpell")) {
                        var7 = 3;
                    }
                    break;
                case -848693708:
                    if (var6.equals("setSpellAreaSize")) {
                        var7 = 4;
                    }
                    break;
                case -715418344:
                    if (var6.equals("resizeIndividualSpells")) {
                        var7 = 5;
                    }
                    break;
                case -563100451:
                    if (var6.equals("shouldFilterSpell")) {
                        var7 = 1;
                    }
                    break;
                case 1565292847:
                    if (var6.equals("setSpellPosition")) {
                        var7 = 6;
                    }
                    break;
                case 1968579708:
                    if (var6.equals("isMobileSpellbookEnabled")) {
                        var7 = 2;
                    }
            }

            int columns;
            int widget;
            int y;
            switch(var7) {
                case 0:
                    Spellbook pook = Spellbook.getByID(this.client.getVar(Varbits.SPELLBOOK));
                    if (pook != this.spellbook) {
                        this.saveSpells();
                        this.spellbook = pook;
                        this.loadSpells();
                    }
                    break;
                case 1:
                    String spell = sStack[sStackSize - 1].toLowerCase();
                    columns = iStack[iStackSize - 1];
                    if (!this.spells.containsKey(columns)) {
                        Spell s = new Spell();
                        s.setWidget(columns);
                        s.setX(-1);
                        s.setY(-1);
                        s.setSize(0);
                        s.setName(spell);
                        this.spells.put(columns, s);
                    }

                    if (this.notFilteredSpells.isEmpty()) {
                        return;
                    }

                    iStack[iStackSize - 2] = isUnfiltered(spell, this.notFilteredSpells) ? 1 : 0;
                    break;
                case 2:
                    iStack[iStackSize - 1] = 1;
                    break;
                case 3:
                    int size = this.config.size();
                    if (size == 0) {
                        return;
                    }

                    columns = MiscUtils.clamp(184 / size, 2, 3);
                    iStack[iStackSize - 2] = size;
                    iStack[iStackSize - 1] = columns;
                    break;
                case 4:
                    if (!this.config.dragSpells()) {
                        return;
                    }

                    iStack[iStackSize - 2] = 184;
                    iStack[iStackSize - 1] = 240;
                    break;
                case 5:
                    widget = iStack[iStackSize - 1];
                    int visibleCount = 0;
                    Iterator var19 = this.spells.values().iterator();

                    while(var19.hasNext()) {
                        Spell spell = (Spell)var19.next();
                        String s = spell.getName();
                        if (isUnfiltered(s, this.notFilteredSpells)) {
                            ++visibleCount;
                        }
                    }

                    if (visibleCount > 20 || visibleCount == 0) {
                        return;
                    }

                    Spell spell = (Spell)this.spells.get(widget);
                    y = MiscUtils.clamp(this.trueSize(spell), 0, 184);
                    iStack[iStackSize - 3] = y;
                    iStack[iStackSize - 2] = y;
                    break;
                case 6:
                    if (!this.config.dragSpells()) {
                        return;
                    }

                    widget = iStack[iStackSize - 1];
                    Spell s = (Spell)this.spells.get(widget);
                    int x = s.getX();
                    y = s.getY();
                    if (x == -1 || y == -1) {
                        return;
                    }

                    iStack[iStackSize - 5] = x;
                    iStack[iStackSize - 4] = y;
            }

        }
    }

    private void loadSpells() {
        this.spells.clear();
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            if (this.spellbook == null) {
                log.debug("Spellbook is null?");
            } else {
                String cfg = this.configManager.getConfiguration("spellbook", this.spellbook.getConfigKey());
                if (!Strings.isNullOrEmpty(cfg)) {
                    Collection<Spell> gson = (Collection)GSON.fromJson(cfg, (new 1(this)).getType());
                    Iterator var3 = gson.iterator();

                    while(var3.hasNext()) {
                        Spell s = (Spell)var3.next();
                        this.spells.put(s.getWidget(), s);
                    }

                }
            }
        }
    }

    private void saveSpells() {
        if (!this.spells.isEmpty()) {
            this.configManager.setConfiguration("spellbook", this.spellbook.getConfigKey(), GSON.toJson(this.spells.values()));
        }
    }

    private void runRebuild() {
        if (this.client.getGameState() == GameState.LOGGED_IN && this.mageTabOpen) {
            this.clientThread.invoke(() -> {
                Widget spellWidget = this.client.getWidget(WidgetInfo.SPELLBOOK);
                if (spellWidget != null) {
                    Object[] args = spellWidget.getOnInvTransmit();
                    if (args != null) {
                        this.client.runScript(args);
                    }
                }

            });
        }
    }

    boolean isNotOnSpellWidget() {
        if (!this.client.isMenuOpen() && this.mageTabOpen) {
            return this.currentWidget() == null;
        } else {
            return true;
        }
    }

    private Widget currentWidget() {
        Widget parent = this.client.getWidget(WidgetInfo.SPELLBOOK_FILTERED_BOUNDS);
        if (parent == null) {
            return null;
        } else {
            Widget[] var2 = parent.getStaticChildren();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Widget w = var2[var4];
                if (w.containsMouse()) {
                    return w;
                }
            }

            return null;
        }
    }

    void startDragging(java.awt.Point point) {
        this.draggingWidget = this.currentWidget();
        if (this.draggingWidget != null) {
            Point widgetPos = this.draggingWidget.getCanvasLocation();
            int x = point.x - widgetPos.getX();
            int y = point.y - widgetPos.getY();
            this.draggingLocation = new Point(x, y);
            this.draggingWidget.setHidden(true);
            this.dragging = true;
        }
    }

    void completeDragging(java.awt.Point point) {
        Point parentPos = this.client.getWidget(WidgetInfo.SPELLBOOK_FILTERED_BOUNDS).getCanvasLocation();
        int x = point.x - this.draggingLocation.getX() - parentPos.getX();
        int y = point.y - this.draggingLocation.getY() - parentPos.getY();
        int size = this.draggingWidget.getWidth();
        x = MiscUtils.clamp(x, 0, 184 - size);
        y = MiscUtils.clamp(y, 0, 240 - size);
        int draggedID = this.draggingWidget.getId();
        Spell n = (Spell)this.spells.get(draggedID);
        n.setX(x);
        n.setY(y);
        this.draggingWidget.setHidden(false);
        this.dragging = false;
        this.runRebuild();
    }

    void increaseSize() {
        Widget scrolledWidget = this.currentWidget();
        if (scrolledWidget != null && !this.dragging) {
            int scrolledWidgetId = scrolledWidget.getId();
            Spell scrolledSpell = (Spell)this.spells.get(scrolledWidgetId);
            if (scrolledSpell.getX() == -1 || scrolledSpell.getY() == -1) {
                scrolledSpell.setX(scrolledWidget.getRelativeX());
                scrolledSpell.setY(scrolledWidget.getRelativeY());
            }

            if (this.trueSize(scrolledSpell) > 182) {
                scrolledSpell.setX(0);
                scrolledSpell.setY(MiscUtils.clamp(scrolledSpell.getY(), 0, 56));
            } else {
                scrolledSpell.setSize(scrolledSpell.getSize() + 1);
                scrolledSpell.setX(MiscUtils.clamp(scrolledSpell.getX() - 1, 0, 184 - this.trueSize(scrolledSpell)));
                scrolledSpell.setY(MiscUtils.clamp(scrolledSpell.getY() - 1, 0, 240 - this.trueSize(scrolledSpell)));
                this.runRebuild();
            }
        }
    }

    void decreaseSize() {
        Widget scrolledWidget = this.currentWidget();
        if (scrolledWidget != null && !this.dragging) {
            int scrolledWidgetId = scrolledWidget.getId();
            Spell scrolledSpell = (Spell)this.spells.get(scrolledWidgetId);
            if (this.trueSize(scrolledSpell) > 5) {
                scrolledSpell.setSize(scrolledSpell.getSize() - 1);
                if (scrolledSpell.getX() == -1 || scrolledSpell.getY() == -1) {
                    scrolledSpell.setX(scrolledWidget.getRelativeX());
                    scrolledSpell.setY(scrolledWidget.getRelativeY());
                }

                scrolledSpell.setX(scrolledSpell.getX() + 1);
                scrolledSpell.setY(scrolledSpell.getY() + 1);
                this.runRebuild();
            }
        }
    }

    void resetSize() {
        Widget clickedWidget = this.currentWidget();
        if (clickedWidget != null && !this.dragging && this.config.scroll()) {
            int clickedWidgetId = clickedWidget.getId();
            Spell clickedSpell = (Spell)this.spells.get(clickedWidgetId);
            int oldSize = clickedSpell.getSize();
            if (oldSize != 0) {
                if (clickedSpell.getX() == -1 || clickedSpell.getY() == -1) {
                    clickedSpell.setX(clickedWidget.getRelativeX());
                    clickedSpell.setY(clickedWidget.getRelativeY());
                }

                clickedSpell.setX(clickedSpell.getX() + oldSize);
                clickedSpell.setY(clickedSpell.getY() + oldSize);
                clickedSpell.setSize(0);
                this.runRebuild();
            }
        }
    }

    void resetLocation() {
        Widget clickedWidget = this.currentWidget();
        if (clickedWidget != null && !this.dragging) {
            int clickedWidgetId = clickedWidget.getId();
            Spell clickedSpell = (Spell)this.spells.get(clickedWidgetId);
            clickedSpell.setX(-1);
            clickedSpell.setY(-1);
            this.runRebuild();
        }
    }

    private int trueSize(Spell s) {
        return s.getSize() * 2 + this.config.size();
    }

    private static boolean isUnfiltered(String spell, Set<String> unfiltereds) {
        Iterator var2 = unfiltereds.iterator();

        while(var2.hasNext()) {
            String str = (String)var2.next();
            if (str.length() != 0) {
                boolean b;
                if (str.charAt(0) == '"') {
                    if (str.charAt(str.length() - 1) == '"') {
                        b = spell.equalsIgnoreCase(str.substring(1, str.length() - 1));
                    } else {
                        b = StringUtils.startsWithIgnoreCase(spell, str.substring(1));
                    }
                } else if (str.charAt(str.length() - 1) == '"') {
                    b = StringUtils.endsWithIgnoreCase(spell, StringUtils.chop(str));
                } else {
                    b = StringUtils.containsIgnoreCase(spell, str);
                }

                if (b) {
                    return true;
                }
            }
        }

        return false;
    }

    boolean isDragging() {
        return this.dragging;
    }

    Widget getDraggingWidget() {
        return this.draggingWidget;
    }

    Point getDraggingLocation() {
        return this.draggingLocation;
    }

    static {
        FIXED_MAGIC_TAB_LOCK = new WidgetMenuOption("Disable", "Reordering", WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB);
        FIXED_MAGIC_TAB_UNLOCK = new WidgetMenuOption("Enable", "Reordering", WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB);
        RESIZABLE_MAGIC_TAB_LOCK = new WidgetMenuOption("Disable", "Reordering", WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB);
        RESIZABLE_MAGIC_TAB_UNLOCK = new WidgetMenuOption("Enable", "Reordering", WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB);
        RESIZABLE_BOTTOM_LINE_MAGIC_TAB_LOCK = new WidgetMenuOption("Disable", "Reordering", WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_MAGIC_TAB);
        RESIZABLE_BOTTOM_LINE_MAGIC_TAB_UNLOCK = new WidgetMenuOption("Enable", "Reordering", WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_MAGIC_TAB);
    }
}
