package net.runelite.api;

import java.util.HashSet;

public interface BLClient
{
    void setDeadNPCsHidden(boolean state);
    HashSet<String> getNpcsToHide();
    HashSet<String> getNpcsToHideOnDeath();
    HashSet<Integer> getNpcsByAnimationToHideOnDeath();
    HashSet<Integer> getNpcsByIdToHideOnDeath();

    void setLoginScreenBackgroundPixels(SpritePixels pixels);
    void rightSpriteOverwrite();
    void leftSpriteOverwrite();
}