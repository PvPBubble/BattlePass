// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import io.fazal.cloudpass.challenges.Challenge;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillEnemyPlayerChallengeMC extends Challenge implements Listener {
	public KillEnemyPlayerChallengeMC(final String name, final Tier tier, final int max, final String description, String world, String region) {
		super(name, tier, max, description, world, region);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onKill(final PlayerDeathEvent e) {

		if (!(e.getEntity().getKiller() instanceof Player)) {
			return;
		}

		Player player = e.getEntity().getKiller();
		SuperiorPlayer sPlayer = SuperiorSkyblockAPI.getPlayer(player);
		SuperiorPlayer deadPlayer = SuperiorSkyblockAPI.getPlayer(e.getEntity());

		if (sPlayer.getIsland().getUniqueId() != deadPlayer.getIsland().getUniqueId()) {
			this.addProgress(player);
		}
	}
}
