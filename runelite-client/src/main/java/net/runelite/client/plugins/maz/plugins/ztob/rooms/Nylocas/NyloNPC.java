package net.runelite.client.plugins.maz.plugins.ztob.rooms.Nylocas;

import java.util.Objects;
import lombok.Data;

@Data
class NyloNPC
{
	private NylocasType nyloType;
	private NylocasSpawnPoint spawnPoint;
	private boolean aggressive = false;
	private String prefireType = "";

	NyloNPC(NylocasType nyloType, NylocasSpawnPoint nylocasSpawnPoint)
	{
		this.nyloType = nyloType;
		this.spawnPoint = nylocasSpawnPoint;
	}

	NyloNPC(NylocasType nyloType, NylocasSpawnPoint nylocasSpawnPoint, boolean aggressive)
	{
		this(nyloType, nylocasSpawnPoint);
		this.aggressive = aggressive;
	}

	NyloNPC(NylocasType nyloType, NylocasSpawnPoint nylocasSpawnPoint, boolean aggressive, String prefireType)
	{
		this(nyloType, nylocasSpawnPoint, aggressive);
		this.prefireType = prefireType;
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof NyloNPC))
		{
			return false;
		}
		NyloNPC otherNpc = (NyloNPC)other;
		return this.nyloType.equals(otherNpc.getNyloType()) && this.spawnPoint.equals(otherNpc.getSpawnPoint());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.nyloType, this.spawnPoint);
	}
}
