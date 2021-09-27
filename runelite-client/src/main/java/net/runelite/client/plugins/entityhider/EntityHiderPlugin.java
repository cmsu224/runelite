//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.entityhider;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.rs.ClientLoader;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
		name = "[S] Entity Hider",
		description = "Hide players, NPCs, and/or projectiles",
		tags = {"npcs", "players", "projectiles"},
		enabledByDefault = false
)
public class EntityHiderPlugin extends Plugin {
	@Inject
	private Client client;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private EntityHiderOverlay entityHiderOverlay;
	@Inject
	private Notifier notifier;
	@Inject
	private EntityHiderConfig config;
	public static boolean isHidingEntities;
	public static boolean isHidingOthers;
	public static boolean isHidingLocal;

	public EntityHiderPlugin() {
	}

	@Provides
	EntityHiderConfig provideConfig(ConfigManager configManager) {
		return (EntityHiderConfig)configManager.getConfig(EntityHiderConfig.class);
	}

	protected void startUp() {


		this.overlayManager.add(this.entityHiderOverlay);
		this.updateConfig();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e) {
		if (e.getGroup().equals("entityhider")) {
			this.updateConfig();
		}

	}

	private void updateConfig() {
		this.client.setIsHidingEntities(true);
		isHidingEntities = true;
		this.client.setOthersHidden(this.config.hideOthers());
		if (this.config.hideOthers()) {
			isHidingOthers = true;
		}

		this.client.setOthersHidden2D(this.config.hideOthers2D());
		this.client.setFriendsHidden(this.config.hideFriends());
		this.client.setFriendsChatMembersHidden(this.config.hideFriendsChatMembers());
		this.client.setIgnoresHidden(this.config.hideIgnores());
		this.client.setLocalPlayerHidden(this.config.hideLocalPlayer());
		if (this.config.hideLocalPlayer()) {
			isHidingLocal = true;
		}

		this.client.setLocalPlayerHidden2D(this.config.hideLocalPlayer2D());
		this.client.setNPCsHidden(this.config.hideNPCs());
		this.client.setNPCsHidden2D(this.config.hideNPCs2D());
		this.client.setPetsHidden(this.config.hidePets());
		this.client.setAttackersHidden(this.config.hideAttackers());
		this.client.setProjectilesHidden(this.config.hideProjectiles());
	}

	protected void shutDown() throws Exception {


		this.overlayManager.remove(this.entityHiderOverlay);
		this.client.setIsHidingEntities(false);
		this.client.setOthersHidden(false);
		this.client.setOthersHidden2D(false);
		this.client.setFriendsHidden(false);
		this.client.setFriendsChatMembersHidden(false);
		this.client.setIgnoresHidden(false);
		this.client.setLocalPlayerHidden(false);
		this.client.setLocalPlayerHidden2D(false);
		this.client.setNPCsHidden(false);
		this.client.setNPCsHidden2D(false);
		this.client.setPetsHidden(false);
		this.client.setAttackersHidden(false);
		this.client.setProjectilesHidden(false);
	}

	protected boolean isNpcAllowed(NPC npc) {
		Player localPlayer = this.client.getLocalPlayer();
		if (localPlayer == null) {
			return true;
		} else {
			return npc == null || npc.getName() == null || !npc.getName().toLowerCase().contains("ice demon") && !npc.getName().toLowerCase().contains("great olm") && !npc.getName().toLowerCase().contains("respiratory system") && !npc.getName().toLowerCase().contains("kalphite queen");
		}
	}
}
