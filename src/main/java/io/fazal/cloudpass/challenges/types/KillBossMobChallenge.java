// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;


import io.fazal.cloudpass.challenges.Challenge;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;

public class KillBossMobChallenge extends Challenge implements Listener
{
    public KillBossMobChallenge(final String name, final Tier tier, final int max, final String description, String world, String region) {
        super(name, tier, max, description, world, region);

    }

    /*
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBossDeath(final BossMobDeathEvent e) {
        final Player player = e.getPlayer();
        if (this.canDo(player)) {
            this.addProgress(player);
        }
    }
     */
}
