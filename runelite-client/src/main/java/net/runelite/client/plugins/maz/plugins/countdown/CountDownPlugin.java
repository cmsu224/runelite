package net.runelite.client.plugins.maz.plugins.countdown;


import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "CountDown Maz"
)
public class CountDownPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(CountDownPlugin.class);
    @Inject
    private Client client;
    @Inject
    private CountDownConfig config;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private CountDownOverlay overlay;

    public CountDownPlugin() {
    }

    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
    }

    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.overlay);
    }

    @Provides
    CountDownConfig provideConfig(ConfigManager configManager) {
        return (CountDownConfig)configManager.getConfig(CountDownConfig.class);
    }
}
