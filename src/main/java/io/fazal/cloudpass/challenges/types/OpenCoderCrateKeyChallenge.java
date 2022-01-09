// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.c0d3r.crates.api.events.CrateOpenEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class OpenCoderCrateKeyChallenge extends Challenge implements Listener
{
    private final String CRATE;
    
    public OpenCoderCrateKeyChallenge(final String name, final Tier tier, final int max, final String description, final String crate, String world, String region) {
        super(name, tier, max, description, world, region);
        this.CRATE = crate;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onOpenCrate(final CrateOpenEvent e) {
        if (this.CRATE == null || e.getCrate() == null) {
            return;
        }
        final Player player = e.getPlayer();
        if (this.canDo(player) && e.getCrate().equals(this.CRATE)) {
            this.addProgress(player);
        }
    }
}
