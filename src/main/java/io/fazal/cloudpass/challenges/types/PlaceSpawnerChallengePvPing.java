// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import io.fazal.cloudpass.challenges.Challenge;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.creepermc.spawners.events.SpawnerPlaceEvent;

public class PlaceSpawnerChallengePvPing extends Challenge implements Listener {
	private final EntityType TYPE;

	public PlaceSpawnerChallengePvPing(final String name, final Tier tier, final int max, final String description, final EntityType type, String world, String region) {
		super(name, tier, max, description, world, region);
		this.TYPE = type;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlace(final SpawnerPlaceEvent e) {
		if (this.TYPE == null || e.isCancelled()) {
			return;
		}
		final Player player = e.getPlayer();
		if (!isApplicableToPlayer(player))
			return;
		if (this.canDo(player) && e.getSpawner().getType() == this.TYPE) {
			this.addProgress(player);
		}
	}
}
