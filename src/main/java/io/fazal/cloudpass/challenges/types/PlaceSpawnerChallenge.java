// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import de.dustplanet.silkspawners.events.SilkSpawnersSpawnerPlaceEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class PlaceSpawnerChallenge extends Challenge implements Listener
{
    private final EntityType TYPE;
    
    public PlaceSpawnerChallenge(final String name, final Tier tier, final int max, final String description, final EntityType type, String world, String region) {
        super(name, tier, max, description, world, region);
        this.TYPE = type;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(final SilkSpawnersSpawnerPlaceEvent e) {
        if (this.TYPE == null || e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (this.canDo(player) && this.TYPE.getName() == e.getEntityID()) {
            this.addProgress(player);
        }
    }
}
