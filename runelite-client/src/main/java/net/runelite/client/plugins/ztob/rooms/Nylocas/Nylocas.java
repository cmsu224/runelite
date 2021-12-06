package net.runelite.client.plugins.ztob.rooms.Nylocas;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.ztob.NyloSelectionBox;
import net.runelite.client.plugins.ztob.NyloSelectionManager;
import net.runelite.client.plugins.ztob.NylocasAliveCounterOverlay;
import net.runelite.client.plugins.ztob.NylocasNotesOverlay;
import net.runelite.client.plugins.ztob.Room;
import net.runelite.client.plugins.ztob.TheatreConfig;
import net.runelite.client.plugins.ztob.TheatreInputListener;
import net.runelite.client.plugins.ztob.TheatrePlugin;
import net.runelite.client.ui.overlay.components.InfoBoxComponent;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import java.awt.Color;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Nylocas extends Room
{
	private static final Set<Integer> NPCID_NYLOCAS_PILLAR = ImmutableSet.of(8358, 10810, 10811);
	private static final Set<Integer> NYLOCAS_ID = ImmutableSet.of(
		8342, 8343, 8344, 8345, 8346, 8347, 8348, 8349, 8350, 8351, 8352, 8353,
		10774, 10775, 10776, 10777, 10778, 10779, 10780, 10781, 10782, 10783, 10784, 10785,
		10791, 10792, 10793, 10794, 10795, 10796, 10797, 10798, 10799, 10800, 10801, 10802);
	private static final Set<Integer> NYLOCAS_BOSS_ID = ImmutableSet.of(
		8354, 8355, 8356, 8357,
		10786, 10787, 10788, 10789,
		10807, 10808, 10809, 10810
	);
	private static final int NYLO_MAP_REGION = 13122;
	private static final int BLOAT_MAP_REGION = 13125;

	@Setter
	@Getter
	private static Runnable wave31Callback = null;
	@Setter
	@Getter
	private static Runnable endOfWavesCallback = null;

	@Inject
	private SkillIconManager skillIconManager;
	@Inject
	private MouseManager mouseManager;
	@Inject
	private TheatreInputListener theatreInputListener;
	@Inject
	private Client client;
	@Inject
	private NylocasOverlay nylocasOverlay;
	@Inject
	private NylocasAliveCounterOverlay nylocasAliveCounterOverlay;
	@Inject
	private NylocasNotesOverlay NylocasNotesOverlay;

	@Getter
	private boolean nyloActive;
	private boolean nyloBossAlive;
	private int nyloWave = 0;
	private int varbit6447 = -1;
	@Getter
	private Instant nyloWaveStart;
	@Getter
	private NyloSelectionManager nyloSelectionManager;
	@Getter
	private final HashMap<NPC, Integer> nylocasPillars = new HashMap<>();
	@Getter
	private final HashMap<NPC, Integer> nylocasNpcs = new HashMap<>();
	@Getter
	private final HashSet<NPC> aggressiveNylocas = new HashSet<>();
	//DUO
	@Getter
	private final HashSet<NPC> prefireDuoMageNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefireDuoRangeNylos = new HashSet<>();
	//TRIO
	@Getter
	private final HashSet<NPC> prefireTrioMageNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefireTrioRangeNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefireTrioMeleeNylos = new HashSet<>();
	//4MAN
	@Getter
	private final HashSet<NPC> prefire4manMageNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefire4manRangeNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefire4manMeleeWestNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefire4manMeleeEastNylos = new HashSet<>();
	//5MAN
	@Getter
	private final HashSet<NPC> prefire5manMageWestNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefire5manMageEastNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefire5manRangeNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefire5manMeleeWestNylos = new HashSet<>();
	@Getter
	private final HashSet<NPC> prefire5manMeleeEastNylos = new HashSet<>();

	private final HashMap<NyloNPC, NPC> currentWave = new HashMap<>();
	private int ticksSinceLastWave = 0;
	@Getter
	private String notes = "";
	@Getter
	private int instanceTimer = 0;
	@Getter
	private boolean isInstanceTimerRunning = false;
	private boolean nextInstance = true;

	@Inject
	protected Nylocas(TheatrePlugin plugin, TheatreConfig config)
	{
		super(plugin, config);
	}

	@Override
	public void init()
	{
		InfoBoxComponent box = new InfoBoxComponent();
		box.setImage(skillIconManager.getSkillImage(Skill.ATTACK));
		NyloSelectionBox nyloMeleeOverlay = new NyloSelectionBox(box);
		nyloMeleeOverlay.setSelected(config.getHighlightMeleeNylo());

		box = new InfoBoxComponent();
		box.setImage(skillIconManager.getSkillImage(Skill.MAGIC));
		NyloSelectionBox nyloMageOverlay = new NyloSelectionBox(box);
		nyloMageOverlay.setSelected(config.getHighlightMageNylo());

		box = new InfoBoxComponent();
		box.setImage(skillIconManager.getSkillImage(Skill.RANGED));
		NyloSelectionBox nyloRangeOverlay = new NyloSelectionBox(box);
		nyloRangeOverlay.setSelected(config.getHighlightRangeNylo());

		nyloSelectionManager = new NyloSelectionManager(nyloMeleeOverlay, nyloMageOverlay, nyloRangeOverlay);
		nyloSelectionManager.setHidden(!config.nyloOverlay());
		nylocasAliveCounterOverlay.setHidden(!config.nyloAlivePanel());
		nylocasAliveCounterOverlay.setNyloAlive(0);
		nylocasAliveCounterOverlay.setMaxNyloAlive(12);

		nyloBossAlive = false;
	}

	private void startupNyloOverlay()
	{
		mouseManager.registerMouseListener(theatreInputListener);

		if (nyloSelectionManager != null)
		{
			overlayManager.add(nyloSelectionManager);
			nyloSelectionManager.setHidden(!config.nyloOverlay());
		}

		if (nylocasAliveCounterOverlay != null)
		{
			overlayManager.add(nylocasAliveCounterOverlay);
			nylocasAliveCounterOverlay.setHidden(!config.nyloAlivePanel());
		}
		if (NylocasNotesOverlay != null)
		{
			overlayManager.add(NylocasNotesOverlay);
		}
	}

	private void shutdownNyloOverlay()
	{
		mouseManager.unregisterMouseListener(theatreInputListener);

		if (nyloSelectionManager != null)
		{
			overlayManager.remove(nyloSelectionManager);
			nyloSelectionManager.setHidden(true);
		}

		if (nylocasAliveCounterOverlay != null)
		{
			overlayManager.remove(nylocasAliveCounterOverlay);
			nylocasAliveCounterOverlay.setHidden(true);
		}
		if (NylocasNotesOverlay != null)
		{
			overlayManager.remove(NylocasNotesOverlay);
		}
	}

	public void load()
	{
		overlayManager.add(nylocasOverlay);
	}

	public void unload()
	{
		overlayManager.remove(nylocasOverlay);

		shutdownNyloOverlay();
		nyloBossAlive = false;
		nyloWaveStart = null;
	}

	private void resetNylo()
	{
		nyloBossAlive = false;
		nylocasPillars.clear();
		nylocasNpcs.clear();
		aggressiveNylocas.clear();

		prefireDuoMageNylos.clear();
		prefireDuoRangeNylos.clear();

		prefireTrioMageNylos.clear();
		prefireTrioMeleeNylos.clear();
		prefireTrioRangeNylos.clear();

		prefire4manMeleeEastNylos.clear();
		prefire4manMeleeWestNylos.clear();
		prefire4manMageNylos.clear();
		prefire4manRangeNylos.clear();

		prefire5manMageWestNylos.clear();
		prefire5manMeleeEastNylos.clear();
		prefire5manMageEastNylos.clear();
		prefire5manMeleeWestNylos.clear();
		prefire5manRangeNylos.clear();

		try{
			setNyloWave(0);
		}
		catch(Exception e){
			throw new RuntimeException("Unable to set Nylo Wave", e);
		}

		currentWave.clear();
	}

	private void setWavesForOverlay(int wave){
		nylocasAliveCounterOverlay.setWave(wave);
		NylocasNotesOverlay.setWave(wave);
	}

	private void setNyloWave(int wave)
	{
		nyloWave = wave;

		ImmutableMap<Integer, NylocasWave> waveData = NylocasWave.waves;

		//DUO
		if(config.nyloPrefiresDuoMage()){ waveData = NylocasWave.wavesDuoMage; setWavesForOverlay(wave);}
		if(config.nyloPrefiresDuoRange()){ waveData = NylocasWave.wavesDuoRange; setWavesForOverlay(wave);}

		//TRIO
		if(config.nyloPrefiresTrioMage()){ waveData = NylocasWave.wavesTrioMage; setWavesForOverlay(wave);}
		if(config.nyloPrefiresTrioRange()){ waveData = NylocasWave.wavesTrioRange; setWavesForOverlay(wave);}
		if(config.nyloPrefiresTrioMelee()){ waveData = NylocasWave.wavesTrioMelee; setWavesForOverlay(wave);}

		//4Man
		if(config.nyloPrefires4manMage()){ waveData = NylocasWave.waves4manMage; setWavesForOverlay(wave);}
		if(config.nyloPrefires4manRange()){ waveData = NylocasWave.waves4manRange; setWavesForOverlay(wave);}
		if(config.nyloPrefires4manMeleeEast()){ waveData = NylocasWave.waves4manMeleeEast; setWavesForOverlay(wave);}
		if(config.nyloPrefires4manMeleeWest()){ waveData = NylocasWave.waves4manMeleeWest; setWavesForOverlay(wave);}

		//5Man
		if(config.nyloPrefires5manMageWest()){ waveData = NylocasWave.waves5manMageWest; setWavesForOverlay(wave);}
		if(config.nyloPrefires5manMageEast()){ waveData = NylocasWave.waves5manMageEast; setWavesForOverlay(wave);}
		if(config.nyloPrefires5manMeleeEast()){ waveData = NylocasWave.waves5manMeleeEast; setWavesForOverlay(wave);}
		if(config.nyloPrefires5manMeleeWest()){ waveData = NylocasWave.waves5manMeleeWest; setWavesForOverlay(wave);}
		if(config.nyloPrefires5manRange()){ waveData = NylocasWave.waves5manRange; setWavesForOverlay(wave);}

		if (wave != 0)
		{
			nylocasAliveCounterOverlay.setNotes(waveData.get(wave).getNotes());
			nylocasAliveCounterOverlay.setStartLoc(waveData.get(wave).getStartPosition());
			NylocasNotesOverlay.setNotes(waveData.get(wave).getNotes());
			NylocasNotesOverlay.setStartLoc(waveData.get(wave).getStartPosition());
			NylocasNotesOverlay.setNextLoc(waveData.get(wave+1).getStartPosition());
			NylocasNotesOverlay.setWeapon(waveData.get(wave).getWeapon());
			NylocasNotesOverlay.setNextWeapon(waveData.get(wave+1).getWeapon());
		}


		if (wave >= 3)
		{
			isInstanceTimerRunning = false;
		}

		if (wave != 0)
		{
			ticksSinceLastWave = NylocasWave.waves.get(wave).getWaveDelay();
		}

		if (wave >= 20)
		{
			if (nylocasAliveCounterOverlay.getMaxNyloAlive() != 24)
			{
				nylocasAliveCounterOverlay.setMaxNyloAlive(24);
			}
		}
		if (wave < 20)
		{
			if (nylocasAliveCounterOverlay.getMaxNyloAlive() != 12)
			{
				nylocasAliveCounterOverlay.setMaxNyloAlive(12);
			}
		}

		if (wave == NylocasWave.MAX_WAVE && wave31Callback != null)
		{
			wave31Callback.run();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged change)
	{
		if (change.getKey().equals("nyloOverlay"))
		{
			nyloSelectionManager.setHidden(!config.nyloOverlay());
		}
		if (change.getKey().equals("nyloAliveCounter"))
		{
			nylocasAliveCounterOverlay.setHidden(!config.nyloAlivePanel());
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		if (NPCID_NYLOCAS_PILLAR.contains(npc.getId()))
		{
			nyloActive = true;
			if (nylocasPillars.size() > 3)
			{
				nylocasPillars.clear();
			}
			if (!nylocasPillars.containsKey(npc))
			{
				nylocasPillars.put(npc, 100);
			}
		}
		else if (NYLOCAS_ID.contains(npc.getId()))
		{
			if (nyloActive)
			{
				nylocasNpcs.put(npc, 52);
				nylocasAliveCounterOverlay.setNyloAlive(nylocasNpcs.size());

				NyloNPC nyloNPC = matchNpc(npc);
				if (nyloNPC != null)
				{
					currentWave.put(nyloNPC, npc);
					if (currentWave.size() > 2)
					{
						matchWave();
					}
				}
			}
		}
		else if (NYLOCAS_BOSS_ID.contains(npc.getId()))
		{
			nyloBossAlive = true;
			isInstanceTimerRunning = false;
			nylocasAliveCounterOverlay.setWave(0);
			NylocasNotesOverlay.setWave(0);
		}
	}

	private void matchWave()
	{
		HashSet<NyloNPC> potentialWave;
		Set<NyloNPC> currentWaveKeySet = currentWave.keySet();
		for (int wave = nyloWave + 1; wave <= NylocasWave.MAX_WAVE; wave++)
		{
			boolean matched = true;
			if(config.nyloPrefiresDuoMage()){
				potentialWave = NylocasWave.wavesDuoMage.get(wave).getWaveData();
				notes = NylocasWave.wavesDuoMage.get(wave).getNotes();
			}else{
				potentialWave = NylocasWave.waves.get(wave).getWaveData();
			}

			for (NyloNPC nyloNpc : potentialWave)
			{
				if (!currentWaveKeySet.contains(nyloNpc))
				{
					matched = false;
					break;
				}
			}

			if (matched)
			{
				setNyloWave(wave);
				for (NyloNPC nyloNPC : potentialWave)
				{
					if (nyloNPC.isAggressive())
					{
						aggressiveNylocas.add(currentWave.get(nyloNPC));
					}
					if(nyloNPC.getPrefireType().equals("Mager")){
						prefireDuoMageNylos.add(currentWave.get(nyloNPC));
						prefireTrioMageNylos.add(currentWave.get(nyloNPC));
						prefire4manMageNylos.add(currentWave.get(nyloNPC));
					}
					if(nyloNPC.getPrefireType().equals("Ranger")){
						prefireDuoRangeNylos.add(currentWave.get(nyloNPC));
						prefireTrioRangeNylos.add(currentWave.get(nyloNPC));
						prefire4manRangeNylos.add(currentWave.get(nyloNPC));
						prefire5manRangeNylos.add(currentWave.get(nyloNPC));
					}
					if(nyloNPC.getPrefireType().equals("Melee")){
						prefireTrioMeleeNylos.add(currentWave.get(nyloNPC));
					}
					if(nyloNPC.getPrefireType().equals("MeleeWest")){
						prefire4manMeleeWestNylos.add(currentWave.get(nyloNPC));
						prefire5manMeleeWestNylos.add(currentWave.get(nyloNPC));
					}
					if(nyloNPC.getPrefireType().equals("MeleeEast")){
						prefire4manMeleeEastNylos.add(currentWave.get(nyloNPC));
						prefire5manMeleeEastNylos.add(currentWave.get(nyloNPC));
					}
					if(nyloNPC.getPrefireType().equals("MagerEast")){
						prefire5manMageEastNylos.add(currentWave.get(nyloNPC));
					}
					if(nyloNPC.getPrefireType().equals("MagerWest")){
						prefire5manMageWestNylos.add(currentWave.get(nyloNPC));
					}
				}
				currentWave.clear();
				return;
			}
		}
	}

	private NyloNPC matchNpc(NPC npc)
	{
		WorldPoint p = WorldPoint.fromLocalInstance(client, npc.getLocalLocation());
		Point point = new Point(p.getRegionX(), p.getRegionY());
		NylocasSpawnPoint spawnPoint = NylocasSpawnPoint.getLookupMap().get(point);
		if (spawnPoint == null)
		{
			return null;
		}
		NylocasType nylocasType = NylocasType.getLookupMap().get(npc.getId());
		if (nylocasType == null)
		{
			return null;
		}
		return new NyloNPC(nylocasType, spawnPoint);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		NPC npc = npcDespawned.getNpc();
		if (NPCID_NYLOCAS_PILLAR.contains(npc.getId()))
		{
			if (nylocasPillars.containsKey(npc))
			{
				nylocasPillars.remove(npc);
			}
			if (nylocasPillars.size() < 1)
			{
				nyloWaveStart = null;
				nyloActive = false;
			}
		}
		else if (NYLOCAS_ID.contains(npc.getId()))
		{
			if (nylocasNpcs.remove(npc) != null)
			{
				nylocasAliveCounterOverlay.setNyloAlive(nylocasNpcs.size());
			}
			aggressiveNylocas.remove(npc);
			prefireDuoMageNylos.remove(npc);
			prefireDuoRangeNylos.remove(npc);

			prefireTrioMageNylos.remove(npc);
			prefireTrioMeleeNylos.remove(npc);
			prefireTrioRangeNylos.remove(npc);

			prefire4manMeleeEastNylos.remove(npc);
			prefire4manMeleeWestNylos.remove(npc);
			prefire4manMageNylos.remove(npc);
			prefire4manRangeNylos.remove(npc);

			prefire5manMageWestNylos.remove(npc);
			prefire5manMeleeEastNylos.remove(npc);
			prefire5manMageEastNylos.remove(npc);
			prefire5manMeleeWestNylos.remove(npc);
			prefire5manRangeNylos.remove(npc);

			if (nyloWave == NylocasWave.MAX_WAVE && nylocasNpcs.size() == 0 && endOfWavesCallback != null)
			{
				endOfWavesCallback.run();
			}
		}
		else if (NYLOCAS_BOSS_ID.contains(npc.getId()))
		{
			nyloBossAlive = false;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		int[] varps = client.getVarps();
		int newVarbit6447 = client.getVarbitValue(varps, 6447);
		if (isInNyloRegion() && newVarbit6447 != 0 && newVarbit6447 != varbit6447)
		{
			nyloWaveStart = Instant.now();
			if (nylocasAliveCounterOverlay != null)
			{
				nylocasAliveCounterOverlay.setNyloWaveStart(nyloWaveStart);
			}
		}

		varbit6447 = newVarbit6447;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (isInNyloRegion())
		{
			startupNyloOverlay();
		}
		else
		{
			if (!nyloSelectionManager.isHidden() || !nylocasAliveCounterOverlay.isHidden())
			{
				shutdownNyloOverlay();
			}
			resetNylo();

			isInstanceTimerRunning = false;
		}

		nextInstance = true;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (nyloActive)
		{
			for (Iterator<NPC> it = nylocasNpcs.keySet().iterator(); it.hasNext();)
			{
				NPC npc = it.next();
				int ticksLeft = nylocasNpcs.get(npc);

				if (ticksLeft < 0)
				{
					it.remove();
					continue;
				}
				nylocasNpcs.replace(npc, ticksLeft - 1);
			}

			for (NPC pillar : nylocasPillars.keySet())
			{
				int healthPercent = pillar.getHealthRatio();
				if (healthPercent > -1)
				{
					nylocasPillars.replace(pillar, healthPercent);
				}
			}

			if (config.nyloStallMessage() && (instanceTimer + 1) % 4 == 1 && nyloWave < NylocasWave.MAX_WAVE && ticksSinceLastWave < 2)
			{
				if (nylocasAliveCounterOverlay.getNyloAlive() >= nylocasAliveCounterOverlay.getMaxNyloAlive())
				{
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Stalled wave <col=EF1020>" +
							nyloWave + "/" + NylocasWave.MAX_WAVE + " <col=00>Time:<col=EF1020> " + nylocasAliveCounterOverlay.getFormattedTime() +
							" <col=00>Nylos alive<col=EF1020> " + nylocasAliveCounterOverlay.getNyloAlive() + "/" + nylocasAliveCounterOverlay.getMaxNyloAlive(), "", false);
				}
			}

			ticksSinceLastWave = Math.max(0, ticksSinceLastWave - 1);
		}
		instanceTimer = (instanceTimer + 1) % 4;
	}

	@Subscribe
	protected void onClientTick(ClientTick event)
	{
		List<Player> players = client.getPlayers();
		for (Player player : players)
		{
			if (player.getWorldLocation() != null)
			{
				LocalPoint lp = player.getLocalLocation();

				WorldPoint wp = WorldPoint.fromRegion(player.getWorldLocation().getRegionID(),5,33,0);
				LocalPoint lp1 = LocalPoint.fromWorld(client, wp.getX(), wp.getY());
				if (lp1 != null)
				{
					Point base = new Point(lp1.getSceneX(), lp1.getSceneY());
					Point point = new Point(lp.getSceneX() - base.getX(), lp.getSceneY() - base.getY());

					if (isInBloatRegion() && point.getX() == -1 && (point.getY() == -1 || point.getY() == -2 || point.getY() == -3) && nextInstance)
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Nylo instance timer started", "", false);
						instanceTimer = 3;
						isInstanceTimerRunning = true;
						nextInstance = false;
					}
				}
			}
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded entry)
	{
		if (!nyloActive)
		{
			return;
		}

		if (config.nyloRecolorMenu() && entry.getOption().equals("Attack"))
		{
			MenuEntry[] entries = client.getMenuEntries();
			MenuEntry toEdit = entries[entries.length - 1];

			String target = entry.getTarget();
			String strippedTarget = stripColor(target);

			boolean isBig = false;
			if (config.nyloRecolorBigDifferent() && toEdit.getType() == MenuAction.NPC_SECOND_OPTION.getId())
			{
				int eventId = toEdit.getIdentifier();
				NPC npc = client.getCachedNPCs()[eventId];
				if (npc != null && npc.getComposition() != null)
				{
					isBig = npc.getComposition().getSize() > 1;
				}
			}

			if (strippedTarget.startsWith("Nylocas Hagios"))
			{
				if (isBig)
				{
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, new Color(0, 190, 190)));
				}
				else
				{
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, new Color(0, 255, 255)));
				}
			}
			else if (strippedTarget.startsWith("Nylocas Ischyros"))
			{
				if (isBig)
				{
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, new Color(190, 150, 150)));
				}
				else
				{
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, new Color(255, 188, 188)));
				}
			}
			else if (strippedTarget.startsWith("Nylocas Toxobolos"))
			{
				if (isBig)
				{
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, new Color(0, 190, 0)));
				}
				else
				{
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, new Color(0, 255, 0)));
				}
			}
			client.setMenuEntries(entries);
		}
	}

	static String stripColor(String str)
	{
		return str.replaceAll("(<col=[0-9a-f]+>|</col>)", "");
	}

	@Subscribe
	public void onMenuOpened(MenuOpened menu)
	{
		if (!config.nyloRecolorMenu() || !nyloActive || nyloBossAlive)
		{
			return;
		}

		// filter all entries with examine
		client.setMenuEntries(Arrays.stream(menu.getMenuEntries()).filter(s -> !s.getOption().equals("Examine")).toArray(MenuEntry[]::new));
	}

	boolean isInNyloRegion()
	{
		return client.isInInstancedRegion() && client.getMapRegions().length > 0 && client.getMapRegions()[0] == NYLO_MAP_REGION;
	}

	private boolean isInBloatRegion()
	{
		return client.isInInstancedRegion() && client.getMapRegions().length > 0 && client.getMapRegions()[0] == BLOAT_MAP_REGION;
	}
}
