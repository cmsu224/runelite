//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.hide;

import com.google.inject.Provides;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.hunter.HunterConfig;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import javax.inject.Inject;

@PluginDescriptor(
        name = "[Maz] Hide Panel",
        enabledByDefault = false,
        description = "Hide Panel",
        tags = {"hide", "panel"}
)
public class HidePlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(HidePlugin.class);
    private static final int INFERNO_REGION = 9043;
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    private NavigationButton navButton;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private PluginManager pluginManager;
    private HideConfig config;

    @Provides
    HideConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(HideConfig.class);
    }

    private final String hidePlugin = "BlueLite External Manager";

    protected void startUp() {
        showplugin();
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            hideplugin();
        } else if (event.getGameState() == GameState.LOGIN_SCREEN){
            showplugin();
        }
    }

    public void showplugin() {
        for (Plugin plugin : pluginManager.getPlugins())
        {
            try
            {
                if (plugin.getName().equals(hidePlugin))
                {
                    pluginManager.startPlugin(plugin);
                }
            }
            catch(NullPointerException | PluginInstantiationException ignore)
            {
            }
        }
    }

    public void hideplugin() {
        String test = "";
        for (Plugin plugin : pluginManager.getPlugins())
        {
            try
            {
                test = test + " \n " + plugin.getName();

                if (plugin.getName().equals(hidePlugin))
                {
                    pluginManager.stopPlugin(plugin);
                }
            }
            catch(NullPointerException | PluginInstantiationException ignore)
            {
            }
        }
        //writetofile(test);
    }

    public void sendChatMessage(String chatMessage)
    {
        final String message = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(chatMessage)
                .build();

        chatMessageManager.queue(
                QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(message)
                        .build());
    }

    public void writetofile(String str) {
        try {
            FileWriter myWriter = new FileWriter(System.getProperty("user.home") + "/.runelite/test_debug.txt");
            myWriter.write(str);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
