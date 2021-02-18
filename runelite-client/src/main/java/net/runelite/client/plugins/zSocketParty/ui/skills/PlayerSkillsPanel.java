//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.ui.skills;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import lombok.Getter;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.zSocketParty.data.PartyPlayer;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.runelite.api.Skill.*;

public class PlayerSkillsPanel extends JPanel
{
    /**
     * Skills ordered in the way they should be displayed in the panel.
     */
    private static final List<Skill> SKILLS = ImmutableList.of(
            ATTACK, HITPOINTS, MINING,
            STRENGTH, AGILITY, SMITHING,
            DEFENCE, HERBLORE, FISHING,
            RANGED, THIEVING, COOKING,
            PRAYER, CRAFTING, FIREMAKING,
            MAGIC, FLETCHING, WOODCUTTING,
            RUNECRAFT, SLAYER, FARMING,
            CONSTRUCTION, HUNTER
    );

    private static final ImmutableMap<Skill, Integer> SPRITE_MAP;
    static
    {
        final Builder<Skill, Integer> map = ImmutableMap.builder();
        map.put(Skill.ATTACK, SpriteID.SKILL_ATTACK);
        map.put(Skill.STRENGTH, SpriteID.SKILL_STRENGTH);
        map.put(Skill.DEFENCE, SpriteID.SKILL_DEFENCE);
        map.put(Skill.RANGED, SpriteID.SKILL_RANGED);
        map.put(Skill.PRAYER, SpriteID.SKILL_PRAYER);
        map.put(Skill.MAGIC, SpriteID.SKILL_MAGIC);
        map.put(Skill.HITPOINTS, SpriteID.SKILL_HITPOINTS);
        map.put(Skill.AGILITY, SpriteID.SKILL_AGILITY);
        map.put(Skill.HERBLORE, SpriteID.SKILL_HERBLORE);
        map.put(Skill.THIEVING, SpriteID.SKILL_THIEVING);
        map.put(Skill.CRAFTING, SpriteID.SKILL_CRAFTING);
        map.put(Skill.FLETCHING, SpriteID.SKILL_FLETCHING);
        map.put(Skill.MINING, SpriteID.SKILL_MINING);
        map.put(Skill.SMITHING, SpriteID.SKILL_SMITHING);
        map.put(Skill.FISHING, SpriteID.SKILL_FISHING);
        map.put(Skill.COOKING, SpriteID.SKILL_COOKING);
        map.put(Skill.FIREMAKING, SpriteID.SKILL_FIREMAKING);
        map.put(Skill.WOODCUTTING, SpriteID.SKILL_WOODCUTTING);
        map.put(Skill.RUNECRAFT, SpriteID.SKILL_RUNECRAFT);
        map.put(Skill.SLAYER, SpriteID.SKILL_SLAYER);
        map.put(Skill.FARMING, SpriteID.SKILL_FARMING);
        map.put(Skill.CONSTRUCTION, SpriteID.SKILL_CONSTRUCTION);
        map.put(Skill.HUNTER, SpriteID.SKILL_HUNTER);
        SPRITE_MAP = map.build();
    }

    private static final Dimension PANEL_SIZE = new Dimension(PluginPanel.PANEL_WIDTH - 10, 300);
    private static final Color PANEL_BORDER_COLOR = new Color(87, 80, 64);
    private static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(3, 3, 3, 3, PANEL_BORDER_COLOR),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
    );

    @Getter
    private final Map<Skill, SkillPanelSlot> panelMap = new HashMap<>();
    @Getter
    private final TotalPanelSlot totalLevelPanel;

    public PlayerSkillsPanel(final PartyPlayer player, final SpriteManager spriteManager)
    {
        super();

        this.setMinimumSize(PANEL_SIZE);
        this.setPreferredSize(PANEL_SIZE);
        this.setBorder(PANEL_BORDER);
        this.setBackground(new Color(62, 53, 41));
        this.setLayout(new DynamicGridLayout(8, 3, 0, 0));

        for (final Skill s : SKILLS)
        {
            final SkillPanelSlot slot = new SkillPanelSlot(player.getSkillBoostedLevel(s), player.getSkillRealLevel(s));
            slot.setToolTipText(s.getName());
            panelMap.put(s, slot);
            this.add(slot);
            spriteManager.getSpriteAsync(SPRITE_MAP.get(s), 0, img -> SwingUtilities.invokeLater(() -> slot.initImages(img, spriteManager)));
        }

        final int totalLevel = player.getStats() == null ? -1 : player.getStats().getTotalLevel();
        totalLevelPanel = new TotalPanelSlot(totalLevel, spriteManager);
        this.add(totalLevelPanel);
    }
}
