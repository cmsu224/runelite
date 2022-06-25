//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators;

import java.awt.Color;
import java.util.Iterator;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.plugins.maz.plugins.pvpplayerindicators.utils.PvpUtil;

@Singleton
public class PvPPlayerIndicatorsTargetService {
    private final Client client;
    private final PvPPlayerIndicatorsConfig config;
    private final PvPPlayerIndicatorsPlugin plugin;

    @Inject
    private PvPPlayerIndicatorsTargetService(Client client, PvPPlayerIndicatorsPlugin plugin, PvPPlayerIndicatorsConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    public void forEachPlayer(BiConsumer<Player, Color> consumer) {
        if (this.config.highlightTargets() != TargetHighlightMode.OFF) {
            Player localPlayer = this.client.getLocalPlayer();
            Iterator var3 = this.client.getPlayers().iterator();

            while(var3.hasNext()) {
                Player player = (Player)var3.next();
                if (player != null && player.getName() != null && PvpUtil.isAttackable(this.client, player) && !this.client.isFriended(player.getName(), false) && !player.isFriendsChatMember() && !player.getName().equals(localPlayer.getName())) {
                    consumer.accept(player, this.config.getTargetColor());
                }
            }

        }
    }
}
