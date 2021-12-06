//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.decorhighlight;

import com.google.inject.Provides;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;

import net.runelite.api.ChatMessageType;
import net.runelite.api.events.GameTick;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
        name = "[Maz] Decor Highlight",
        description = "Highlights ground decor \"graphics objects\" by ID in-game"
)
public class DecorHighlightPlugin extends Plugin {
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private DecorSceneOverlay overlay;
    @Inject
    private DecorHighlightConfig config;
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

    private int lastTick = 0;

    public DecorHighlightPlugin() {
    }

    @Provides
    DecorHighlightConfig provideConfig(ConfigManager configManager) {
        return (DecorHighlightConfig)configManager.getConfig(DecorHighlightConfig.class);
    }

    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
        this.graphicsObjectWhitelist = new HashSet();
        this.groundDecorWhitelist = new HashSet();
        this.gameObjectsWhitelist = new HashSet();
        this.gameNPCsWhitelist = new HashSet();
        this.gameProjectileWhitelist = new HashSet();
        this.gameTickWhitelist = new HashSet<>();
        this.parse_list(this.graphicsObjectWhitelist, this.config.graphicsObjectsToHighlight());
        this.parse_list(this.groundDecorWhitelist, this.config.groundDecorToHighlight());
        this.parse_list(this.gameObjectsWhitelist, this.config.gameObjectsToHighlight());
        this.parse_list(this.gameNPCsWhitelist, this.config.npcHighlight());
        this.parse_list(this.gameProjectileWhitelist, this.config.projectileHighlight());
        this.parse_list(this.gameTickWhitelist, this.config.gameTickOverlay());
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
            this.parse_list(this.graphicsObjectWhitelist, this.config.graphicsObjectsToHighlight());
            this.parse_list(this.groundDecorWhitelist, this.config.groundDecorToHighlight());
            this.parse_list(this.gameObjectsWhitelist, this.config.gameObjectsToHighlight());
            this.parse_list(this.gameNPCsWhitelist, this.config.npcHighlight());
            this.parse_list(this.gameProjectileWhitelist, this.config.projectileHighlight());
            this.parse_list(this.gameTickWhitelist, this.config.gameTickOverlay());
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
