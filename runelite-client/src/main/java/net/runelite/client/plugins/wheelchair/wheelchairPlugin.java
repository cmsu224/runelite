package net.runelite.client.plugins.wheelchair;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
import net.runelite.api.kit.KitType;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;
import java.util.ArrayList;

@Slf4j
@PluginDescriptor(
	name = "[Maz] Menu-Entry",
	description = "Hide attack option on NPCs",
	tags = {"wheelchair", "maz", "hide"},
	enabledByDefault = false
)

public class wheelchairPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private net.runelite.client.plugins.wheelchair.wheelchairConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private Notifier notifier;

	@Inject
	private ClientThread clientThread;

	private int lastWeaponID = -1;
	private String testMessage = "";

	@Override
	protected void startUp() throws Exception
	{
		log.info("Wheelchair plugin started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Wheelchair plugin stopped!");
	}

	@Provides
    net.runelite.client.plugins.wheelchair.wheelchairConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(wheelchairConfig.class);
	}

	@Subscribe
	private void onClientTick(ClientTick event) {
		if (this.client.getGameState() == GameState.LOGGED_IN && !this.client.isMenuOpen() && this.client.getMapRegions()[0] != config.regionID()) {
			//get weapon
			Player player = this.client.getLocalPlayer();
			PlayerComposition playerComp = player != null ? player.getPlayerComposition() : null;
			if (playerComp == null) {
				return;
			}

			int weaponID = this.lastWeaponID;
			if (weaponID == -1) {
				weaponID = this.client.getLocalPlayer().getPlayerComposition().getEquipmentId(KitType.WEAPON);
			}

			//determine weapon type
			String wepType = "";
			Integer[] rangeWepList = processLine(config.rangeWep().trim().split(",", 0));
			Integer[] mageWepList = processLine(config.mageWep().trim().split(",", 0));
			Integer[] meleeWepList = processLine(config.meleeWep().trim().split(",", 0));

			if( ArrayUtils.contains(rangeWepList, weaponID) ){
				wepType = "range";
			}else if(ArrayUtils.contains(mageWepList, weaponID)){
				wepType = "mage";
			}else if(ArrayUtils.contains(meleeWepList, weaponID)){
				wepType = "melee";
			}

			String[] rangeNPCList = config.rangeNPC().trim().split(",", 0);
			String[] mageNPCList = config.mageNPC().trim().split(",", 0);
			String[] meleeNPCList = config.meleeNPC().trim().split(",", 0);
			//remove entries
			MenuEntry[] menuEntries = this.client.getMenuEntries();
			switch (wepType) {
				case "range":
					menuEntries = this.removeEntries(mageNPCList, meleeNPCList);
					this.client.setMenuEntries(menuEntries);
					break;
				case "mage":
					menuEntries = this.removeEntries(rangeNPCList, meleeNPCList);
					this.client.setMenuEntries(menuEntries);
					break;
				case "melee":
					menuEntries = this.removeEntries(rangeNPCList, mageNPCList);
					this.client.setMenuEntries(menuEntries);
					break;
			}
		}
	}

	private MenuEntry[] removeEntries(String[] NPCNames1, String[] NPCNames2){
		ArrayList<MenuEntry> filtered = new ArrayList();
		MenuEntry[] menuEntries = this.client.getMenuEntries();

		//combine array into 1
		int aLen = NPCNames1.length;
		int bLen = NPCNames2.length;
		String[] npcs = new String[aLen + bLen];

		System.arraycopy(NPCNames1, 0, npcs, 0, aLen);
		System.arraycopy(NPCNames2, 0, npcs, aLen, bLen);


		MenuEntry[] var3 = menuEntries;
		int var4 = menuEntries.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			MenuEntry entry = var3[var5];
			String target = Text.standardize(Text.removeTags(entry.getTarget()));
			String option = Text.standardize(Text.removeTags(entry.getOption()));
			//exclude option and target
			//sendDistinctChatMsg(target + " " + option);
			boolean chk = false;
			for (String s: npcs){
				if(target.contains(s.trim())){
					chk = true;
				}
			}
			if (!(chk && option.equalsIgnoreCase("attack"))) {
				filtered.add(entry);
			}
		}

		return filtered.toArray(new MenuEntry[0]);
	}

	private MenuEntry[] filterEntries(MenuEntry[] menuEntries) {
		ArrayList<MenuEntry> filtered = new ArrayList();
		MenuEntry[] var3 = menuEntries;
		int var4 = menuEntries.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			MenuEntry entry = var3[var5];
			String target = Text.standardize(Text.removeTags(entry.getTarget()));
			String option = Text.standardize(Text.removeTags(entry.getOption()));
			//net.runelite.client.plugins.zcustomswapper.zMenuEntryPlugin.EntryFromConfig entryFromConfig = new net.runelite.client.plugins.zcustomswapper.zMenuEntryPlugin.EntryFromConfig(option, target);
			//if (indexOfEntry(this.removeOptions, entryFromConfig, menuEntries) == -1) {
			filtered.add(entry);
			//}
		}

		return (MenuEntry[])filtered.toArray(new MenuEntry[0]);
	}

	private Integer[] processLine(String[] strings) {
		Integer[] intarray=new Integer[strings.length];
		int i=0;
		for(String str:strings){
			intarray[i]=Integer.parseInt(str.trim());
			i++;
		}
		return intarray;
	}

	public void sendDistinctChatMsg(String msg){
		if(!msg.equals(testMessage)){
			sendChatMessage(msg);
			testMessage = msg;
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
