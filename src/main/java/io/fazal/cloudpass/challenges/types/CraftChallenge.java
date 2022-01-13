// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class CraftChallenge extends Challenge implements Listener
{
    private final Material TYPE;
    private final int DATA;
    
    public CraftChallenge(final String name, final Tier tier, final int max, final String description, final Material type, final int data, String world, String region) {
        super(name, tier, max, description, world, region);
        this.TYPE = type;
        this.DATA = data;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCraft(final CraftItemEvent e) {
        if (this.TYPE == null || e.isCancelled() || !(e.getWhoClicked() instanceof Player) || e.getCurrentItem() == null) {
            return;
        }
        final Player player = (Player)e.getWhoClicked();
        if (!isApplicableToPlayer(player))
            return;
        if (this.canDo(player) && e.getCurrentItem().getType().equals((Object)this.TYPE) && e.getCurrentItem().getDurability() == (short)this.DATA) {
            this.addProgress(player);
        }
    }
}
