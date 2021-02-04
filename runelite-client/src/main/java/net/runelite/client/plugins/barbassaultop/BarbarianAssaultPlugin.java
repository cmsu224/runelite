//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.barbarianassault;

import com.google.inject.Provides;
import java.awt.Image;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
        name = "Barbarian Assault",
        description = "Show a timer to the next call change and game/wave duration in chat.",
        tags = {"minigame", "overlay", "timer"}
)
public class BarbarianAssaultPlugin extends Plugin {
    private static final int BA_WAVE_NUM_INDEX = 2;
    private static final String START_WAVE = "1";
    private static final String ENDGAME_REWARD_NEEDLE_TEXT = "<br>5";
    private Image clockImage;
    private int inGameBit = 0;
    private String currentWave = "1";
    private GameTimer gameTime;
    private Round currentRound;
    @Inject
    private Client client;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private BarbarianAssaultConfig config;
    @Inject
    private TimerOverlay timerOverlay;
    @Inject
    private HealerOverlay healerOverlay;

    public BarbarianAssaultPlugin() {
    }

    @Provides
    BarbarianAssaultConfig provideConfig(ConfigManager configManager) {
        return (BarbarianAssaultConfig)configManager.getConfig(BarbarianAssaultConfig.class);
    }

    protected void startUp() throws Exception {
        this.overlayManager.add(this.timerOverlay);
        this.overlayManager.add(this.healerOverlay);
        this.clockImage = ImageUtil.loadImageResource(this.getClass(), "clock.png");
    }

    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.timerOverlay);
        this.overlayManager.remove(this.healerOverlay);
        this.gameTime = null;
        this.currentWave = "1";
        this.inGameBit = 0;
        this.clockImage = null;
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        switch(event.getGroupId()) {
            case 485:
                this.setRound(Role.ATTACKER);
                break;
            case 486:
                this.setRound(Role.COLLECTOR);
                break;
            case 487:
                this.setRound(Role.DEFENDER);
                break;
            case 488:
                this.setRound(Role.HEALER);
            case 489:
            case 490:
            case 491:
            case 492:
            case 493:
            case 494:
            case 495:
            case 496:
            default:
                break;
            case 497:
                Widget rewardWidget = this.client.getWidget(WidgetInfo.BA_REWARD_TEXT);
                if (this.config.waveTimes() && rewardWidget != null && rewardWidget.getText().contains("<br>5") && this.gameTime != null) {
                    this.announceTime("Game finished, duration: ", this.gameTime.getTime(false));
                    this.gameTime = null;
                }
        }

    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() == ChatMessageType.GAMEMESSAGE && event.getMessage().startsWith("---- Wave:")) {
            String[] message = event.getMessage().split(" ");
            this.currentWave = message[2];
            if (this.currentWave.equals("1")) {
                this.gameTime = new GameTimer();
            } else if (this.gameTime != null) {
                this.gameTime.setWaveStartTime();
            }
        }

    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        int inGame = this.client.getVar(Varbits.IN_GAME_BA);
        if (this.inGameBit != inGame) {
            if (this.inGameBit == 1) {
                this.currentRound = null;
                if (this.config.waveTimes() && this.gameTime != null && this.client.isInInstancedRegion()) {
                    this.announceTime("Wave " + this.currentWave + " duration: ", this.gameTime.getTime(true));
                }
            }

            this.inGameBit = inGame;
        }

    }

    private void setRound(Role role) {
        if (this.currentRound == null) {
            this.currentRound = new Round(role);
        }

    }

    private void announceTime(String preText, String time) {
        String chatMessage = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append(preText).append(ChatColorType.HIGHLIGHT).append(time).build();
        this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
    }

    Image getClockImage() {
        return this.clockImage;
    }

    public Round getCurrentRound() {
        return this.currentRound;
    }
}
