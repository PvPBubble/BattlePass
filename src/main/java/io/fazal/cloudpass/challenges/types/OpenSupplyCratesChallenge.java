// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import me.icodetits.supplyCrates.events.SupplyCrateOpenEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class OpenSupplyCratesChallenge extends Challenge implements Listener
{
    public OpenSupplyCratesChallenge(final String name, final Tier tier, final int max, final String description, String world, String region) {
        super(name, tier, max, description, world, region);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onOpen(final SupplyCrateOpenEvent e) {
        final Player player = e.getPlayer();
        if (!isApplicableToPlayer(player))
            return;
        if (this.canDo(player)) {
            this.addProgress(player);
        }
    }
}
