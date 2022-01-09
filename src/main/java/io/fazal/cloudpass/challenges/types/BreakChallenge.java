// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class BreakChallenge extends Challenge implements Listener
{
    private final Material TYPE;
    
    public BreakChallenge(final String name, final Tier tier, final int max, final String description, final Material type, String world, String region) {
        super(name, tier, max, description, world, region);
        this.TYPE = type;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(final BlockBreakEvent e) {
        if (this.TYPE == null || e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (this.canDo(player) && e.getBlock().getType().equals((Object)this.TYPE)) {
            this.addProgress(player);
        }
    }
}
