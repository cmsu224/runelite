/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.ui.FontManager
 *  net.runelite.client.util.ImageUtil
 *  net.runelite.http.api.worlds.World
 *  net.runelite.http.api.worlds.WorldRegion
 *  net.runelite.http.api.worlds.WorldType
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketworldhopper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldRegion;
import net.runelite.http.api.worlds.WorldType;

class SocketWorldTableRow
extends JPanel {
    private static final ImageIcon FLAG_AUS;
    private static final ImageIcon FLAG_UK;
    private static final ImageIcon FLAG_US;
    private static final ImageIcon FLAG_GER;
    private static final int WORLD_COLUMN_WIDTH = 60;
    private static final int PLAYERS_COLUMN_WIDTH = 40;
    private static final int PING_COLUMN_WIDTH = 35;
    private static final Color CURRENT_WORLD;
    private static final Color DANGEROUS_WORLD;
    private static final Color TOURNAMENT_WORLD;
    private static final Color MEMBERS_WORLD;
    private static final Color FREE_WORLD;
    private static final Color SEASONAL_WORLD;
    private final JMenuItem favoriteMenuOption = new JMenuItem();
    private JLabel worldField;
    private JLabel playerCountField;
    private JLabel activityField;
    private JLabel pingField;
    private final BiConsumer<World, Boolean> onFavorite;
    private final World world;
    private int updatedPlayerCount;
    private int ping;
    private Color lastBackground;

    SocketWorldTableRow(final World world, boolean current, boolean favorite, Integer ping, final Consumer<World> onSelect, BiConsumer<World, Boolean> onFavorite) {
        this.world = world;
        this.onFavorite = onFavorite;
        this.updatedPlayerCount = world.getPlayers();
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(2, 0, 2, 0));
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && onSelect != null) {
                    onSelect.accept(world);
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    SocketWorldTableRow.this.setBackground(SocketWorldTableRow.this.getBackground().brighter());
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    SocketWorldTableRow.this.setBackground(SocketWorldTableRow.this.getBackground().darker());
                }
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                SocketWorldTableRow.this.lastBackground = SocketWorldTableRow.this.getBackground();
                SocketWorldTableRow.this.setBackground(SocketWorldTableRow.this.getBackground().brighter());
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                SocketWorldTableRow.this.setBackground(SocketWorldTableRow.this.lastBackground);
            }
        });
        this.setFavoriteMenu(favorite);
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
        popupMenu.add(this.favoriteMenuOption);
        this.setComponentPopupMenu(popupMenu);
        JPanel leftSide = new JPanel(new BorderLayout());
        JPanel rightSide = new JPanel(new BorderLayout());
        leftSide.setOpaque(false);
        rightSide.setOpaque(false);
        JPanel worldField = this.buildWorldField();
        worldField.setPreferredSize(new Dimension(60, 0));
        worldField.setOpaque(false);
        JPanel pingField = this.buildPingField(ping);
        pingField.setPreferredSize(new Dimension(35, 0));
        pingField.setOpaque(false);
        JPanel playersField = this.buildPlayersField();
        playersField.setPreferredSize(new Dimension(40, 0));
        playersField.setOpaque(false);
        JPanel activityField = this.buildActivityField();
        activityField.setBorder(new EmptyBorder(5, 5, 5, 5));
        activityField.setOpaque(false);
        this.recolour(current);
        leftSide.add((Component)worldField, "West");
        leftSide.add((Component)playersField, "Center");
        rightSide.add((Component)activityField, "Center");
        rightSide.add((Component)pingField, "East");
        this.add((Component)leftSide, "West");
        this.add((Component)rightSide, "Center");
    }

    void setFavoriteMenu(boolean favorite) {
        String favoriteAction = favorite ? "Remove " + this.world.getId() + " from favorites" : "Add " + this.world.getId() + " to favorites";
        this.favoriteMenuOption.setText(favoriteAction);
        for (ActionListener listener : this.favoriteMenuOption.getActionListeners()) {
            this.favoriteMenuOption.removeActionListener(listener);
        }
        this.favoriteMenuOption.addActionListener(e -> this.onFavorite.accept(this.world, !favorite));
    }

    void updatePlayerCount(int playerCount) {
        this.updatedPlayerCount = playerCount;
        this.playerCountField.setText(SocketWorldTableRow.playerCountString(playerCount));
    }

    private static String playerCountString(int playerCount) {
        return playerCount < 0 ? "OFF" : Integer.toString(playerCount);
    }

    void setPing(int ping) {
        this.ping = ping;
        this.pingField.setText(ping <= 0 ? "-" : Integer.toString(ping));
    }

    void hidePing() {
        this.pingField.setText("-");
    }

    void showPing() {
        this.setPing(this.ping);
    }

    int getPing() {
        return this.ping;
    }

    public void recolour(boolean current) {
        this.playerCountField.setForeground(current ? CURRENT_WORLD : Color.WHITE);
        this.pingField.setForeground(current ? CURRENT_WORLD : Color.WHITE);
        if (current) {
            this.activityField.setForeground(CURRENT_WORLD);
            this.worldField.setForeground(CURRENT_WORLD);
            return;
        }
        if (this.world.getTypes().contains((Object)WorldType.PVP) || this.world.getTypes().contains((Object)WorldType.HIGH_RISK) || this.world.getTypes().contains((Object)WorldType.DEADMAN)) {
            this.activityField.setForeground(DANGEROUS_WORLD);
        } else if (this.world.getTypes().contains((Object)WorldType.SEASONAL)) {
            this.activityField.setForeground(SEASONAL_WORLD);
        } else if (this.world.getTypes().contains((Object)WorldType.NOSAVE_MODE)) {
            this.activityField.setForeground(TOURNAMENT_WORLD);
        } else {
            this.activityField.setForeground(Color.WHITE);
        }
        this.worldField.setForeground(this.world.getTypes().contains((Object)WorldType.MEMBERS) ? MEMBERS_WORLD : FREE_WORLD);
    }

    private JPanel buildPlayersField() {
        JPanel column = new JPanel(new BorderLayout());
        column.setBorder(new EmptyBorder(0, 5, 0, 5));
        this.playerCountField = new JLabel(SocketWorldTableRow.playerCountString(this.world.getPlayers()));
        this.playerCountField.setFont(FontManager.getRunescapeSmallFont());
        column.add((Component)this.playerCountField, "West");
        return column;
    }

    private JPanel buildPingField(Integer ping) {
        JPanel column = new JPanel(new BorderLayout());
        column.setBorder(new EmptyBorder(0, 5, 0, 5));
        this.pingField = new JLabel("-");
        this.pingField.setFont(FontManager.getRunescapeSmallFont());
        column.add((Component)this.pingField, "East");
        if (ping != null) {
            this.setPing(ping);
        }
        return column;
    }

    private JPanel buildActivityField() {
        JPanel column = new JPanel(new BorderLayout());
        column.setBorder(new EmptyBorder(0, 5, 0, 5));
        String activity = this.world.getActivity();
        this.activityField = new JLabel(activity);
        this.activityField.setFont(FontManager.getRunescapeSmallFont());
        if (activity != null && activity.length() > 16) {
            this.activityField.setToolTipText(activity);
            this.activityField.addMouseListener(new MouseAdapter(){

                @Override
                public void mouseClicked(MouseEvent e) {
                    SocketWorldTableRow.this.dispatchEvent(e);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    SocketWorldTableRow.this.dispatchEvent(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    SocketWorldTableRow.this.dispatchEvent(e);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    SocketWorldTableRow.this.dispatchEvent(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    SocketWorldTableRow.this.dispatchEvent(e);
                }
            });
        }
        column.add((Component)this.activityField, "West");
        return column;
    }

    private JPanel buildWorldField() {
        JPanel column = new JPanel(new BorderLayout(7, 0));
        column.setBorder(new EmptyBorder(0, 5, 0, 5));
        this.worldField = new JLabel("" + this.world.getId());
        ImageIcon flagIcon = SocketWorldTableRow.getFlag(this.world.getRegion());
        if (flagIcon != null) {
            JLabel flag = new JLabel(flagIcon);
            column.add((Component)flag, "West");
        }
        column.add((Component)this.worldField, "Center");
        return column;
    }

    private static ImageIcon getFlag(WorldRegion region) {
        if (region == null) {
            return null;
        }
        switch (region) {
            case UNITED_STATES_OF_AMERICA: {
                return FLAG_US;
            }
            case UNITED_KINGDOM: {
                return FLAG_UK;
            }
            case AUSTRALIA: {
                return FLAG_AUS;
            }
            case GERMANY: {
                return FLAG_GER;
            }
        }
        return null;
    }

    public World getWorld() {
        return this.world;
    }

    int getUpdatedPlayerCount() {
        return this.updatedPlayerCount;
    }

    static {
        CURRENT_WORLD = new Color(66, 227, 17);
        DANGEROUS_WORLD = new Color(251, 62, 62);
        TOURNAMENT_WORLD = new Color(79, 145, 255);
        MEMBERS_WORLD = new Color(210, 193, 53);
        FREE_WORLD = new Color(200, 200, 200);
        SEASONAL_WORLD = new Color(133, 177, 178);
        FLAG_AUS = new ImageIcon(ImageUtil.loadImageResource(SocketWorldHopperPlugin.class, (String)"flag_aus.png"));
        FLAG_UK = new ImageIcon(ImageUtil.loadImageResource(SocketWorldHopperPlugin.class, (String)"flag_uk.png"));
        FLAG_US = new ImageIcon(ImageUtil.loadImageResource(SocketWorldHopperPlugin.class, (String)"flag_us.png"));
        FLAG_GER = new ImageIcon(ImageUtil.loadImageResource(SocketWorldHopperPlugin.class, (String)"flag_ger.png"));
    }
}

