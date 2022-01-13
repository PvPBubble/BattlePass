// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerFishEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class FishChallenge extends Challenge implements Listener
{
    private final int DATA;
    
    public FishChallenge(final String name, final Tier tier, final int max, final String description, final int data, String world, String region) {
        super(name, tier, max, description, world, region);
        this.DATA = data;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onFish(final PlayerFishEvent e) {
        if (this.DATA < 0 || e.isCancelled() || !e.getState().equals((Object)PlayerFishEvent.State.CAUGHT_FISH) || e.getCaught() == null) {
            return;
        }
        final Player player = e.getPlayer();
        if (!isApplicableToPlayer(player))
            return;
        if (this.canDo(player) && ((Item)e.getCaught()).getItemStack().getType() == Material.RAW_FISH && ((Item)e.getCaught()).getItemStack().getDurability() == (short)this.DATA) {
            this.addProgress(player);
        }
    }
}
