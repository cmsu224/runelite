//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty;

import com.google.inject.Inject;
import net.runelite.client.plugins.zSocketParty.data.PartyPlayer;
import net.runelite.client.plugins.zSocketParty.ui.PlayerBanner;
import net.runelite.client.plugins.zSocketParty.ui.PlayerPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

class PartyPanel extends PluginPanel
{
    private static final Color BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;
    private static final Color BACKGROUND_HOVER_COLOR = ColorScheme.DARK_GRAY_HOVER_COLOR;

    private final EasierPartyPlugin plugin;
    private final Map<String, PlayerBanner> bannerMap = new HashMap<>();
    private final JPanel panel;
    private PlayerPanel playerPanel = null;
    private PartyPlayer selectedPlayer = null;

    @Inject
    PartyPanel(final EasierPartyPlugin plugin)
    {
        super(false);
        this.plugin = plugin;
        this.setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setBorder(new EmptyBorder(BORDER_OFFSET, BORDER_OFFSET, BORDER_OFFSET, BORDER_OFFSET));
        panel.setLayout(new DynamicGridLayout(0, 1, 0, 3));

        // Wrap content to anchor to top and prevent expansion
        final JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(panel, BorderLayout.NORTH);
        final JScrollPane scrollPane = new JScrollPane(northPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(createReturnButton(), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        //this.add(createLeaveButton(), BorderLayout.SOUTH);
    }

    void refreshUI()
    {
        panel.removeAll();
        if (selectedPlayer == null)
        {
            showBannerView();
        }
        else if (plugin.getPartyMembers().containsKey(selectedPlayer.getUsername()))
        {
            showPlayerView();
        }
        else
        {
            selectedPlayer = null;
            showBannerView();
        }
    }

    /**
     * Shows all members of the party, excluding the local player, in banner view. See {@link PlayerBanner)
     */
    void showBannerView()
    {
        selectedPlayer = null;
        panel.removeAll();

        final Collection<PartyPlayer> players = plugin.getPartyMembers().values()
                .stream()
                // Sort by username, if it doesn't exist use their discord name
                //.sorted(Comparator.comparing(o -> o.getUsername() == null ? o.getMember().getName() : o.getUsername()))
                .sorted(Comparator.comparing(o -> o.getUsername() == null ? "Null user" : o.getUsername()))
                .collect(Collectors.toList());

        for (final PartyPlayer player : players)
        {
            //plugin.sendChatMessage("attempt created banner");
            final PlayerBanner banner = new PlayerBanner(player, plugin.spriteManager);
            //plugin.sendChatMessage("created banner");
            banner.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    if (e.getButton() == MouseEvent.BUTTON1)
                    {
                        selectedPlayer = player;
                        showPlayerView();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e)
                {
                    banner.setBackground(BACKGROUND_HOVER_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e)
                {
                    banner.setBackground(BACKGROUND_COLOR);
                }
            });
            panel.add(banner);
            //bannerMap.put(player.getMember().getName(), banner);
            bannerMap.put(player.getUsername(), banner);
        }

        if (getComponentCount() == 0)
        {
            panel.add(new JLabel("There are no members in your party"));
        }

        panel.revalidate();
        panel.repaint();

    }

    void showPlayerView()
    {
        if (selectedPlayer == null)
        {
            showBannerView();
        }

        panel.removeAll();
        //panel.add(createReturnButton());

        if (playerPanel != null)
        {
            playerPanel.changePlayer(selectedPlayer);
        }
        else
        {
            playerPanel = new PlayerPanel(selectedPlayer, plugin.spriteManager, plugin.itemManager);
        }
        panel.add(playerPanel);

        panel.revalidate();
        panel.repaint();
    }

    private JButton createReturnButton()
    {
        final JButton label = new JButton("Refresh");
        label.setFocusable(false);
        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                label.setBackground(BACKGROUND_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                label.setBackground(BACKGROUND_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    showBannerView();
                }
            }
        });

        return label;
    }

    private JButton createLeaveButton()
    {
        final JButton label = new JButton("Leave Party");
        label.setFocusable(false);
        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                label.setBackground(BACKGROUND_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                label.setBackground(BACKGROUND_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    selectedPlayer = null;
                    bannerMap.clear();
                    playerPanel = null;
                    //plugin.leaveParty();
                }
            }
        });

        return label;
    }

    void updatePartyPlayer(final PartyPlayer player)
    {
        if (selectedPlayer == null)
        {
            final PlayerBanner panel = bannerMap.get(player.getUsername());
            if (panel == null)
            {
                // New member, recreate entire view
                showBannerView();
                return;
            }

            final String oldPlayerName = panel.getPlayer().getUsername();
            panel.setPlayer(player);
            if (!Objects.equals(player.getUsername(), oldPlayerName))
            {
                panel.recreatePanel();
            }
            else
            {
                panel.refreshStats();
            }
        }
        else
        {
            if (player.getUsername().equals(selectedPlayer.getUsername()))
            {
                this.selectedPlayer = player;
                showPlayerView();
            }
        }
    }

    void removePartyPlayer(final PartyPlayer player)
    {
        bannerMap.remove(player.getUsername());
        selectedPlayer = null;
        showBannerView();
        if (selectedPlayer != null && !selectedPlayer.getUsername().equals(player.getUsername()));
        {
            return;
        }

        //selectedPlayer = null;
        //showBannerView();
    }
}