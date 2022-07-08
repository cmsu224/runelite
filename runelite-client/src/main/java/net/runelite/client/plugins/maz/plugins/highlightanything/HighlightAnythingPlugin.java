//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.highlightanything;

import com.google.inject.Provides;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
        name = "Highlight All",
        description = "Highlights anything by ID in-game"
)
public class HighlightAnythingPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private HighlightAnythingOverlay overlay;
    @Inject
    private HighlightAnythingConfig config;
    @Inject
    private KeyManager keyManager;
    @Inject
    private ChatMessageManager chatMessageManager;
    Set<String> graphicsObjectWhitelist;
    Set<String> groundDecorWhitelist;
    Set<String> gameObjectsWhitelist;
    Set<String> gameNPCsWhitelist;
    Set<String> gameProjectileWhitelist;
    Set<String> gameTickWhitelist;
    Set<String> locationWhitelist;
    Set<String> decorWhiteList;

    private int lastTick = 0;

    public HighlightAnythingPlugin() {
    }

    @Provides
    HighlightAnythingConfig provideConfig(ConfigManager configManager) {
        return (HighlightAnythingConfig)configManager.getConfig(HighlightAnythingConfig.class);
    }

    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
        this.graphicsObjectWhitelist = new HashSet();
        this.groundDecorWhitelist = new HashSet();
        this.gameObjectsWhitelist = new HashSet();
        this.gameNPCsWhitelist = new HashSet();
        this.gameProjectileWhitelist = new HashSet();
        this.gameTickWhitelist = new HashSet<>();
        this.locationWhitelist = new HashSet<>();
        this.decorWhiteList = new HashSet<>();
        this.parse_list(this.graphicsObjectWhitelist, this.config.graphicsObjectsToHighlight());
        this.parse_list(this.groundDecorWhitelist, this.config.groundDecorToHighlight());
        this.parse_list(this.gameObjectsWhitelist, this.config.gameObjectsToHighlight());
        this.parse_list(this.gameNPCsWhitelist, this.config.npcHighlight());
        this.parse_list(this.gameProjectileWhitelist, this.config.projectileHighlight());
        this.parse_list(this.gameTickWhitelist, this.config.gameTickOverlay());
        this.parse_list(this.locationWhitelist, this.config.mapLocToHighlight());
        this.parse_list(this.decorWhiteList, this.config.decorLocToHighlight());
        keyManager.registerKeyListener(hotkeyListener);
    }

    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.overlay);
        keyManager.unregisterKeyListener(hotkeyListener);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e) {
        if ("decorhighlight".equals(e.getGroup())) {
            this.graphicsObjectWhitelist.clear();
            this.groundDecorWhitelist.clear();
            this.gameObjectsWhitelist.clear();
            this.gameNPCsWhitelist.clear();
            this.gameProjectileWhitelist.clear();
            this.gameTickWhitelist.clear();
            this.locationWhitelist.clear();
            this.decorWhiteList.clear();
            this.parse_list(this.graphicsObjectWhitelist, this.config.graphicsObjectsToHighlight());
            this.parse_list(this.groundDecorWhitelist, this.config.groundDecorToHighlight());
            this.parse_list(this.gameObjectsWhitelist, this.config.gameObjectsToHighlight());
            this.parse_list(this.gameNPCsWhitelist, this.config.npcHighlight());
            this.parse_list(this.gameProjectileWhitelist, this.config.projectileHighlight());
            this.parse_list(this.gameTickWhitelist, this.config.gameTickOverlay());
            this.parse_list(this.locationWhitelist, this.config.mapLocToHighlight());
            this.parse_list(this.decorWhiteList, this.config.decorLocToHighlight());
        }
    }

    private void parse_list(Set<String> list, String src) {
        String[] split = src.split(",");

        for(int i = 0; i < split.length; ++i) {
            String s = split[i].trim();

            try {
                //int n = Integer.parseInt(n);
                list.add(s);
            } catch (NumberFormatException var7) {
            }
        }

    }

    @Subscribe
    private void onGameTick(GameTick event) {
        lastTick = lastTick - 1;
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggleKeybind())
    {
        @Override
        public void hotkeyPressed()
        {
            overlay.toggle();
        }
    };

    int getLastTick() {
        return this.lastTick;
    }

    void setLastTick(int i){
        lastTick = i;
    }
}
