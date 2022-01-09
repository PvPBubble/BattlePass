// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;
import io.fazal.cloudpass.challenges.Challenge;

public class TypeChallenge extends Challenge implements Listener
{
    private final String TEXT;
    
    public TypeChallenge(final String name, final Tier tier, final String description, final String text, String world, String region) {
        super(name, tier, 1, description, world, region);
        this.TEXT = text;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent e) {
        if (this.TEXT == null || e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (this.canDo(player) && e.getMessage().equals(this.TEXT)) {
            this.addProgress(player);
        }
    }
}
