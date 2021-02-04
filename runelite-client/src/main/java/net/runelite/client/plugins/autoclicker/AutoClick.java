//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.externals.autoclicker;

import com.google.inject.Provides;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Extension
@PluginDescriptor(
        name = "Auto Clicker",
        enabledByDefault = false,
        type = PluginType.UTILITY
)
public class AutoClick extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(AutoClick.class);
    @Inject
    private Client client;
    @Inject
    private AutoClickConfig config;
    @Inject
    private AutoClickOverlay overlay;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private KeyManager keyManager;
    @Inject
    private ExtUtils extUtils;
    private ExecutorService executorService;
    private Point point;
    private Random random;
    private boolean run;
    private boolean flash;
    private HotkeyListener hotkeyListener = new HotkeyListener(() -> {
        return this.config.toggle();
    }) {
        public void hotkeyPressed() {
            AutoClick.this.run = !AutoClick.this.run;
            if (AutoClick.this.run) {
                AutoClick.this.point = AutoClick.this.client.getMouseCanvasPosition();
                AutoClick.this.executorService.submit(() -> {
                    while(true) {
                        if (AutoClick.this.run) {
                            if (AutoClick.this.client.getGameState() != GameState.LOGGED_IN) {
                                AutoClick.this.run = false;
                            } else {
                                if (!AutoClick.this.checkHitpoints() && !AutoClick.this.checkInventory()) {
                                    AutoClick.this.extUtils.click(AutoClick.this.point);

                                    try {
                                        Thread.sleep(AutoClick.this.randomDelay());
                                    } catch (InterruptedException var2) {
                                        var2.printStackTrace();
                                    }
                                    continue;
                                }

                                AutoClick.this.run = false;
                                if (AutoClick.this.config.flash()) {
                                    AutoClick.this.setFlash(true);
                                }
                            }
                        }

                        return;
                    }
                });
            }
        }
    };

    public AutoClick() {
    }

    @Provides
    AutoClickConfig getConfig(ConfigManager configManager) {
        return (AutoClickConfig)configManager.getConfig(AutoClickConfig.class);
    }

    protected void startUp() {
        this.overlayManager.add(this.overlay);
        this.keyManager.registerKeyListener(this.hotkeyListener);
        this.executorService = Executors.newSingleThreadExecutor();
        this.random = new Random();
    }

    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
        this.keyManager.unregisterKeyListener(this.hotkeyListener);
        this.executorService.shutdown();
        this.random = null;
    }

    private long randomDelay() {
        return this.config.weightedDistribution() ? (long)this.clamp(-Math.log(Math.abs(this.random.nextGaussian())) * (double)this.config.deviation() + (double)this.config.target()) : (long)this.clamp((double)Math.round(this.random.nextGaussian() * (double)this.config.deviation() + (double)this.config.target()));
    }

    private double clamp(double val) {
        return Math.max((double)this.config.min(), Math.min((double)this.config.max(), val));
    }

    private boolean checkHitpoints() {
        if (!this.config.autoDisableHp()) {
            return false;
        } else {
            return this.client.getBoostedSkillLevel(Skill.HITPOINTS) <= this.config.hpThreshold();
        }
    }

    private boolean checkInventory() {
        if (!this.config.autoDisableInv()) {
            return false;
        } else {
            Widget inventoryWidget = this.client.getWidget(WidgetInfo.INVENTORY);
            return inventoryWidget.getWidgetItems().size() == 28;
        }
    }

    boolean isFlash() {
        return this.flash;
    }

    void setFlash(boolean flash) {
        this.flash = flash;
    }
}
