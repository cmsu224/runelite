package net.runelite.client.plugins.fdrop;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.FontManager;
import javax.inject.Inject;
import java.awt.*;

import net.runelite.client.Notifier;
import static net.runelite.api.ChatMessageType.GAMEMESSAGE;

@Slf4j
@PluginDescriptor(
	name = "[Maz] Message",
	description = "Highlight and notify you of chat messages",
	tags = {"drop", "messages", "notifications"},
	enabledByDefault = false
)

public class fdropPlugin extends Plugin
{
	private static final Font FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 16);
	private static final Color RED = new Color(221, 44, 0);
	private static final Color GREEN = new Color(0, 200, 83);
	private static final Color ORANGE = new Color(255, 109, 0);
	private static final Color YELLOW = new Color(255, 214, 0);
	private static final Color CYAN = new Color(0, 184, 212);
	private static final Color BLUE = new Color(41, 98, 255);
	private static final Color DEEP_PURPLE = new Color(98, 0, 234);
	private static final Color PURPLE = new Color(170, 0, 255);
	private static final Color GRAY = new Color(158, 158, 158);

	@Inject
	private Client client;

	@Inject
	private fdropConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private Notifier notifier;

	@Inject
	private ClientThread clientThread;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Drop plugin started!");
		//List<NPC> npcList = client.getNpcs();
		//log.info(npcList.toString());
		//sendChatMessage(config.msg());

		if (client.getGameState() == GameState.LOGGED_IN)
		{
			clientThread.invokeLater(() ->
			{
				compileMessage();
			});
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Drop plugin stopped!");
	}

	@Provides
	fdropConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(fdropConfig.class);
	}

	public void compileMessage()
	{
		//g kill
		if(!config.gPB().equals(""))
		{
			client.addChatMessage(GAMEMESSAGE, "", "Corrupted challenge duration: <col=ff0000>" + config.gTot() + "</col>. Personal best: " + config.gPB()+".", null);
			client.addChatMessage(GAMEMESSAGE, "", "Preparation time: <col=ff0000>" + config.gPrep() + "</col>. Hunllef kill time: <col=ff0000>" + config.gKill()+"</col>.", null);
			client.addChatMessage(GAMEMESSAGE, "", "Your Corrupted Gauntlet completion count is: <col=ff0000>" + config.gkc() + "</col>.", null);
			client.addChatMessage(GAMEMESSAGE, "", "Your reward awaits you in the nearby chest.", null);
		}

		//Kill count message
		if(!config.Kill().equals(""))
		{
			client.addChatMessage(GAMEMESSAGE, "", "Your " + config.Kill() + " kill count is: <col=ff0000>" + config.kc() + "</col>.", null);
		}

		//Kill time message
		if(!config.FightDuration().equals(""))
		{
			client.addChatMessage(GAMEMESSAGE, "", "Fight duration: <col=ff0000>" + config.FightDuration() + " </col>. Personal best: " + config.pb(), null);
		}

		//Drop notification
		if(!config.drop1().equals(""))
		{
			sendChatMessage("Valuable drop: " + config.drop1());
		}
		if(!config.drop2().equals(""))
		{
			sendChatMessage("Valuable drop: " + config.drop2());
		}
		if(!config.drop3().equals(""))
		{
			sendChatMessage("Valuable drop: " + config.drop3());
		}
		if(!config.drop4().equals(""))
		{
			sendChatMessage("Valuable drop: " + config.drop4());
		}

		//other message
		if(!config.msg().equals(""))
		{
			sendChatMessage(config.msg());
		}
		if(!config.custom().equals(""))
		{
			client.addChatMessage(GAMEMESSAGE, "", config.custom(), null);
		}
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
}
