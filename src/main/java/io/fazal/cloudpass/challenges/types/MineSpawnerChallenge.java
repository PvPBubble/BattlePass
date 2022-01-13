// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import de.dustplanet.silkspawners.events.SilkSpawnersSpawnerBreakEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class MineSpawnerChallenge extends Challenge implements Listener
{
    private final EntityType TYPE;
    
    public MineSpawnerChallenge(final String name, final Tier tier, final int max, final String description, final EntityType type, String world, String region) {
        super(name, tier, max, description, world, region);
        this.TYPE = type;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(final SilkSpawnersSpawnerBreakEvent e) {
        if (this.TYPE == null || e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (!isApplicableToPlayer(player))
            return;
        if (this.canDo(player) && this.TYPE.getName().equals(e.getEntityID())) {
            this.addProgress(player);
        }
    }
}
