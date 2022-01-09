// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import com.massivecraft.factions.event.FactionCreateEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class FactionCreateChallenge extends Challenge implements Listener
{
    public FactionCreateChallenge(final String name, final Tier tier, final String description, String world, String region) {
        super(name, tier, 1, description, world, region);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onFactionCreate(final FactionCreateEvent e) {
        final Player player = e.getFPlayer().getPlayer();
        if (this.canDo(player)) {
            this.addProgress(player);
        }
    }
}
