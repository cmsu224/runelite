package net.runelite.client.plugins.maz.plugins.cerberus.domain;

import java.awt.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Skill;

@Getter
@RequiredArgsConstructor
public enum Phase
{
    SPAWNING(null, 4, null),
    AUTO(Skill.ATTACK, 6, null),
    TRIPLE(Skill.FLETCHING, 6, new Color(153, 214, 255)),
    GHOSTS(Skill.PRAYER, 8, new Color(255, 255, 255)),
    LAVA(Skill.FIREMAKING, 8, new Color(255, 153, 153));

    private final Skill type;
    private final int tickDelay;
    private final Color textColor;
}