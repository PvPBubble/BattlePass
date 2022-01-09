// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import com.vexsoftware.votifier.model.VotifierEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class VoteChallenge extends Challenge implements Listener
{
    public VoteChallenge(final String name, final Tier tier, final int amount, final String description, String world, String region) {
        super(name, tier, amount, description, world, region);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVote(final VotifierEvent e) {
        if (e.getVote().getUsername() == null) {
            return;
        }
        final Player player = Bukkit.getPlayer(e.getVote().getUsername());
        if (player != null && this.canDo(player)) {
            this.addProgress(player);
        }
    }
}
