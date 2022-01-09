// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class ConsumeChallenge extends Challenge implements Listener
{
    private final Material TYPE;
    private final int DATA;
    
    public ConsumeChallenge(final String name, final Tier tier, final int max, final String description, final Material type, final int data, String world, String region) {
        super(name, tier, max, description, world, region);
        this.TYPE = type;
        this.DATA = data;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEat(final PlayerItemConsumeEvent e) {
        if (this.TYPE == null || e.isCancelled() || this.DATA < 0 || e.getItem() == null) {
            return;
        }
        final Player player = e.getPlayer();
        if (this.canDo(player) && e.getItem().getType().equals((Object)this.TYPE) && e.getItem().getDurability() == (short)this.DATA) {
            this.addProgress(player);
        }
    }
}
