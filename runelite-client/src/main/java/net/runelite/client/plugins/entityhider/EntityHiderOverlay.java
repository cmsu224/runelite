//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.entityhider;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.client.rs.ClientLoader;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class EntityHiderOverlay extends Overlay {
    private final Client client;
    private final EntityHiderPlugin plugin;
    private final EntityHiderConfig config;
    public Set set = null;

    @Inject
    private EntityHiderOverlay(Client client, EntityHiderPlugin plugin, EntityHiderConfig config) {
        super(plugin);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    public Dimension render(Graphics2D graphics) {
        Set<Integer> hiddenIDs = new HashSet();
        Set<String> hiddenNames = new HashSet();
        Set<Integer> hiddenAnimations = new HashSet();
        Set<String> hiddenDeadNPCs = new HashSet();
        Set<String> excludedDeadNpcs = new HashSet();
        String configIDs = this.config.hideIDs().trim();
        int var10;
        String excludedNames;
        int id;
        if (configIDs.length() > 0) {
            String[] var8 = configIDs.split(",");
            int var9 = var8.length;

            for(var10 = 0; var10 < var9; ++var10) {
                excludedNames = var8[var10];

                try {
                    id = Integer.parseInt(excludedNames.trim());
                    hiddenIDs.add(id);
                } catch (NumberFormatException var19) {
                }
            }
        }

        String configNames = this.config.hideNames().trim();
        int var24;
        if (configNames.length() > 0) {
            String[] var21 = configNames.split(",");
            var10 = var21.length;

            for(var24 = 0; var24 < var10; ++var24) {
                String data = var21[var24];
                hiddenNames.add(data.toLowerCase());
            }
        }

        String configAnimations = this.config.hideAnimations().trim();

        if (configAnimations.length() > 0) {
            String[] var23 = configAnimations.split(",");
            var24 = var23.length;

            for(id = 0; id < var24; ++id) {
                String data = var23[id];

                try {
                    id = Integer.parseInt(data.trim());
                    hiddenAnimations.add(id);
                } catch (NumberFormatException var18) {
                }
            }
        }

        if (this.set == null) {
            return null;
        } else {
            this.set.clear();
            String configDeathListNames = this.config.hideDeadList().trim();
            int var28;
            if (configDeathListNames.length() > 0) {
                String[] var27 = configDeathListNames.split(",");
                id = var27.length;

                for(var28 = 0; var28 < id; ++var28) {
                    String data = var27[var28];
                    hiddenDeadNPCs.add(data.toLowerCase());
                }
            }

            excludedNames = this.config.excludedNPCS().trim();
            if (excludedNames.length() > 0) {
                String[] var29 = excludedNames.split(",");
                var28 = var29.length;

                for(id = 0; id < var28; ++id) {
                    String data = var29[id];
                    excludedDeadNpcs.add(data.toLowerCase());
                }
            }

            Iterator var30 = this.client.getNpcs().iterator();

            while(var30.hasNext()) {
                NPC npc = (NPC)var30.next();
                if (npc.getName() == null) {
                    break;
                }

                if (npc.getHealthRatio() == 0) {
                    if (this.config.hideDead() && !excludedDeadNpcs.contains(npc.getName().toLowerCase())) {
                        this.set.add(npc);
                    }

                    if (configDeathListNames.length() > 0) {
                        String[] var34 = configDeathListNames.split(",");
                        int var35 = var34.length;

                        for(int var16 = 0; var16 < var35; ++var16) {
                            String data = var34[var16];
                            if (data.toLowerCase().equals(npc.getName().toLowerCase())) {
                                this.set.add(npc);
                            }
                        }
                    }

                    if (configDeathListNames.contains(npc.getName().toLowerCase())) {
                        this.set.add(npc);
                    }
                } else if (hiddenIDs.contains(npc.getId()) || hiddenAnimations.contains(npc.getAnimation()) || hiddenNames.contains(npc.getName().toLowerCase())) {
                    this.set.add(npc);
                }
            }

            return null;
        }
    }
}
