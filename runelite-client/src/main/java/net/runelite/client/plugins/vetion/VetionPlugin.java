//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.vetion;

import com.google.inject.Provides;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
        name = "Vetion Helper",
        enabledByDefault = false,
        description = "Tracks Vet'ion's special attacks",
        tags = {"bosses", "combat", "pve", "overlay"},
        type = PluginType.PVM
)
public class VetionPlugin extends Plugin {
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private VetionOverlay overlay;
    private VetionConfig config;
    private Map<Actor, Instant> vetions;

    public VetionPlugin() {
    }

    @Provides
    VetionConfig provideConfig(ConfigManager configManager) {
        return (VetionConfig)configManager.getConfig(VetionConfig.class);
    }

    protected void startUp() {
        this.vetions = new HashMap();
        this.overlayManager.add(this.overlay);
    }

    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
        this.vetions = null;
    }

    @Subscribe
    private void onAnimationChanged(AnimationChanged event) {
        if (event.getActor().getAnimation() == 5507) {
            Actor vet = event.getActor();
            this.vetions.remove(vet, Instant.now());
            this.vetions.put(vet, Instant.now());
        }

    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("vetion")) {
            if (event.getKey().equals("mirrorMode")) {
                this.overlay.determineLayer();
                this.overlayManager.remove(this.overlay);
                this.overlayManager.add(this.overlay);
            }

        }
    }

    Map<Actor, Instant> getVetions() {
        return this.vetions;
    }
}
