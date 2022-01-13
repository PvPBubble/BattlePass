// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import io.fazal.cloudpass.Main;
import org.bukkit.event.entity.EntityDeathEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class KillMobChallenge extends Challenge implements Listener
{
    private final EntityType TYPE;
    
    public KillMobChallenge(final String name, final Tier tier, final int max, final String description, final EntityType type, String world, String region) {
        super(name, tier, max, description, world, region);
        this.TYPE = type;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onKill(final EntityDeathEvent e) {
        if (this.TYPE == null || e.getEntity().getKiller() == null) {
            return;
        }
        final Player player = e.getEntity().getKiller();
        if (!isApplicableToPlayer(player))
            return;
        if (!this.canDo(player)) {
            return;
        }
        if (Main.getInstance().isStackMob()) {
            final LivingEntity entity = e.getEntity();
            if (!entity.getType().equals((Object)this.TYPE)) {
                return;
            }
            if (entity.hasMetadata("stackmob:stack-size") && entity.getMetadata("stackmob:stack-size").size() > 0 && entity.getMetadata("stackmob:stack-size").get(0).asInt() > 1) {
                for (int stackSize = entity.getMetadata("stackmob:stack-size").get(0).asInt(), i = 0; i < stackSize && this.canDo(player); ++i) {
                    this.addProgress(player);
                }
                return;
            }
        }
        if (e.getEntity().getType().equals((Object)this.TYPE)) {
            this.addProgress(player);
        }
    }
}
