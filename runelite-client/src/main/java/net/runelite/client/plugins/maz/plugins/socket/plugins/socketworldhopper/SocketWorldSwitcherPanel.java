/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Ordering
 *  net.runelite.client.ui.ColorScheme
 *  net.runelite.client.ui.DynamicGridLayout
 *  net.runelite.client.ui.PluginPanel
 *  net.runelite.http.api.worlds.World
 *  net.runelite.http.api.worlds.WorldType
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketworldhopper;

import com.google.common.collect.Ordering;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldType;

class SocketWorldSwitcherPanel
extends PluginPanel {
    private static final Color ODD_ROW = new Color(44, 44, 44);
    private static final int WORLD_COLUMN_WIDTH = 60;
    private static final int PLAYERS_COLUMN_WIDTH = 40;
    private static final int PING_COLUMN_WIDTH = 47;
    private final JPanel listContainer = new JPanel();
    private SocketWorldTableHeader worldHeader;
    private SocketWorldTableHeader playersHeader;
    private SocketWorldTableHeader activityHeader;
    private SocketWorldTableHeader pingHeader;
    private WorldOrder orderIndex = WorldOrder.WORLD;
    private boolean ascendingOrder = true;
    private final ArrayList<SocketWorldTableRow> rows = new ArrayList();
    private final SocketWorldHopperPlugin plugin;
    private SubscriptionFilterMode subscriptionFilterMode;
    private Set<RegionFilterMode> regionFilterMode;

    SocketWorldSwitcherPanel(SocketWorldHopperPlugin plugin) {
        this.plugin = plugin;
        this.setBorder(null);
        this.setLayout((LayoutManager)new DynamicGridLayout(0, 1));
        JPanel headerContainer = this.buildHeader();
        this.listContainer.setLayout(new GridLayout(0, 1));
        this.add(headerContainer);
        this.add(this.listContainer);
    }

    void switchCurrentHighlight(int newWorld, int lastWorld) {
        for (SocketWorldTableRow row : this.rows) {
            if (row.getWorld().getId() == newWorld) {
                row.recolour(true);
                continue;
            }
            if (row.getWorld().getId() != lastWorld) continue;
            row.recolour(false);
        }
    }

    void updateListData(Map<Integer, Integer> worldData) {
        for (SocketWorldTableRow worldTableRow : this.rows) {
            World world = worldTableRow.getWorld();
            Integer playerCount = worldData.get(world.getId());
            if (playerCount == null) continue;
            worldTableRow.updatePlayerCount(playerCount);
        }
        if (this.orderIndex == WorldOrder.PLAYERS) {
            this.updateList();
        }
    }

    void updatePing(int world, int ping) {
        for (SocketWorldTableRow worldTableRow : this.rows) {
            if (worldTableRow.getWorld().getId() != world) continue;
            worldTableRow.setPing(ping);
            if (this.orderIndex != WorldOrder.PING) break;
            this.updateList();
            break;
        }
    }

    void hidePing() {
        for (SocketWorldTableRow worldTableRow : this.rows) {
            worldTableRow.hidePing();
        }
    }

    void showPing() {
        for (SocketWorldTableRow worldTableRow : this.rows) {
            worldTableRow.showPing();
        }
    }

    void updateList() {
        this.rows.sort((r1, r2) -> {
            switch (this.orderIndex) {
                case PING: {
                    return this.getCompareValue((SocketWorldTableRow)r1, (SocketWorldTableRow)r2, row -> {
                        int ping = row.getPing();
                        return ping > 0 ? Integer.valueOf(ping) : null;
                    });
                }
                case WORLD: {
                    return this.getCompareValue((SocketWorldTableRow)r1, (SocketWorldTableRow)r2, row -> Integer.valueOf(row.getWorld().getId()));
                }
                case PLAYERS: {
                    return this.getCompareValue((SocketWorldTableRow)r1, (SocketWorldTableRow)r2, SocketWorldTableRow::getUpdatedPlayerCount);
                }
                case ACTIVITY: {
                    return this.getCompareValue((SocketWorldTableRow)r1, (SocketWorldTableRow)r2, row -> {
                        String activity = row.getWorld().getActivity();
                        return !activity.equals("-") ? activity : null;
                    });
                }
            }
            return 0;
        });
        this.rows.sort((r1, r2) -> {
            boolean b1 = this.plugin.isFavorite(r1.getWorld());
            boolean b2 = this.plugin.isFavorite(r2.getWorld());
            return Boolean.compare(b2, b1);
        });
        this.listContainer.removeAll();
        for (int i = 0; i < this.rows.size(); ++i) {
            SocketWorldTableRow row = this.rows.get(i);
            row.setBackground(i % 2 == 0 ? ODD_ROW : ColorScheme.DARK_GRAY_COLOR);
            this.listContainer.add(row);
        }
        this.listContainer.revalidate();
        this.listContainer.repaint();
    }

    private int getCompareValue(SocketWorldTableRow row1, SocketWorldTableRow row2, Function<SocketWorldTableRow, Comparable> compareByFn) {
        Ordering ordering = Ordering.natural();
        if (!this.ascendingOrder) {
            ordering = ordering.reverse();
        }
        ordering = ordering.nullsLast();
        return ordering.compare((Object)compareByFn.apply(row1), (Object)compareByFn.apply(row2));
    }

    void updateFavoriteMenu(int world, boolean favorite) {
        for (SocketWorldTableRow row : this.rows) {
            if (row.getWorld().getId() != world) continue;
            row.setFavoriteMenu(favorite);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    void populate(List<World> worlds) {
        this.rows.clear();
        int i = 0;
        while (true) {
            block7: {
                if (i >= worlds.size()) {
                    this.updateList();
                    return;
                }
                World world = worlds.get(i);
                switch (this.subscriptionFilterMode) {
                    case FREE: {
                        if (!world.getTypes().contains((Object)WorldType.MEMBERS)) break;
                        break block7;
                    }
                    case MEMBERS: {
                        if (!world.getTypes().contains((Object)WorldType.MEMBERS)) break block7;
                    }
                }
                if (this.regionFilterMode.isEmpty() || this.regionFilterMode.contains((Object)RegionFilterMode.of(world.getRegion()))) {
                    this.rows.add(this.buildRow(world, i % 2 == 0, world.getId() == this.plugin.getCurrentWorld() && this.plugin.getLastWorld() != 0, this.plugin.isFavorite(world)));
                }
            }
            ++i;
        }
    }

    private void orderBy(WorldOrder order) {
        this.pingHeader.highlight(false, this.ascendingOrder);
        this.worldHeader.highlight(false, this.ascendingOrder);
        this.playersHeader.highlight(false, this.ascendingOrder);
        this.activityHeader.highlight(false, this.ascendingOrder);
        switch (order) {
            case PING: {
                this.pingHeader.highlight(true, this.ascendingOrder);
                break;
            }
            case WORLD: {
                this.worldHeader.highlight(true, this.ascendingOrder);
                break;
            }
            case PLAYERS: {
                this.playersHeader.highlight(true, this.ascendingOrder);
                break;
            }
            case ACTIVITY: {
                this.activityHeader.highlight(true, this.ascendingOrder);
            }
        }
        this.orderIndex = order;
        this.updateList();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JPanel leftSide = new JPanel(new BorderLayout());
        JPanel rightSide = new JPanel(new BorderLayout());
        this.pingHeader = new SocketWorldTableHeader("Ping", this.orderIndex == WorldOrder.PING, this.ascendingOrder, this.plugin::refresh);
        this.pingHeader.setPreferredSize(new Dimension(47, 0));
        this.pingHeader.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    return;
                }
                SocketWorldSwitcherPanel.this.ascendingOrder = SocketWorldSwitcherPanel.this.orderIndex != WorldOrder.PING || !SocketWorldSwitcherPanel.this.ascendingOrder;
                SocketWorldSwitcherPanel.this.orderBy(WorldOrder.PING);
            }
        });
        this.worldHeader = new SocketWorldTableHeader("World", this.orderIndex == WorldOrder.WORLD, this.ascendingOrder, this.plugin::refresh);
        this.worldHeader.setPreferredSize(new Dimension(60, 0));
        this.worldHeader.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    return;
                }
                SocketWorldSwitcherPanel.this.ascendingOrder = SocketWorldSwitcherPanel.this.orderIndex != WorldOrder.WORLD || !SocketWorldSwitcherPanel.this.ascendingOrder;
                SocketWorldSwitcherPanel.this.orderBy(WorldOrder.WORLD);
            }
        });
        this.playersHeader = new SocketWorldTableHeader("#", this.orderIndex == WorldOrder.PLAYERS, this.ascendingOrder, this.plugin::refresh);
        this.playersHeader.setPreferredSize(new Dimension(40, 0));
        this.playersHeader.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    return;
                }
                SocketWorldSwitcherPanel.this.ascendingOrder = SocketWorldSwitcherPanel.this.orderIndex != WorldOrder.PLAYERS || !SocketWorldSwitcherPanel.this.ascendingOrder;
                SocketWorldSwitcherPanel.this.orderBy(WorldOrder.PLAYERS);
            }
        });
        this.activityHeader = new SocketWorldTableHeader("Activity", this.orderIndex == WorldOrder.ACTIVITY, this.ascendingOrder, this.plugin::refresh);
        this.activityHeader.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    return;
                }
                SocketWorldSwitcherPanel.this.ascendingOrder = SocketWorldSwitcherPanel.this.orderIndex != WorldOrder.ACTIVITY || !SocketWorldSwitcherPanel.this.ascendingOrder;
                SocketWorldSwitcherPanel.this.orderBy(WorldOrder.ACTIVITY);
            }
        });
        leftSide.add((Component)this.worldHeader, "West");
        leftSide.add((Component)this.playersHeader, "Center");
        rightSide.add((Component)this.activityHeader, "Center");
        rightSide.add((Component)this.pingHeader, "East");
        header.add((Component)leftSide, "West");
        header.add((Component)rightSide, "Center");
        return header;
    }

    private SocketWorldTableRow buildRow(World world, boolean stripe, boolean current, boolean favorite) {
        SocketWorldTableRow row = new SocketWorldTableRow(world, current, favorite, this.plugin.getStoredPing(world), this.plugin::hopTo, (world12, add) -> {
            if (add.booleanValue()) {
                this.plugin.addToFavorites((World)world12);
            } else {
                this.plugin.removeFromFavorites((World)world12);
            }
            this.updateList();
        });
        row.setBackground(stripe ? ODD_ROW : ColorScheme.DARK_GRAY_COLOR);
        return row;
    }

    void setSubscriptionFilterMode(SubscriptionFilterMode subscriptionFilterMode) {
        this.subscriptionFilterMode = subscriptionFilterMode;
    }

    void setRegionFilterMode(Set<RegionFilterMode> regionFilterMode) {
        this.regionFilterMode = regionFilterMode;
    }

    private static enum WorldOrder {
        WORLD,
        PLAYERS,
        ACTIVITY,
        PING;

    }
}

