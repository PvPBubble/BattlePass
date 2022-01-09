// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.apache.commons.lang.Validate;
import java.util.ArrayList;
import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.tier.Tier;
import io.fazal.cloudpass.utils.pair.Pair;
import java.util.List;

public final class ChallengeRegistry
{
    private static ChallengeRegistry instance;
    private final List<Pair<Tier, Challenge>> challenges;
    private final Main plugin;
    
    public ChallengeRegistry(final Main plugin) {
        this.challenges = new ArrayList<Pair<Tier, Challenge>>();
        Validate.notNull((Object)plugin);
        this.plugin = plugin;
    }

    public static ChallengeRegistry getInstance() {
        if (ChallengeRegistry.instance == null) {
            synchronized (ChallengeRegistry.class) {
                if (ChallengeRegistry.instance == null) {
                    ChallengeRegistry.instance = new ChallengeRegistry(Main.getInstance());
                }
            }
        }
        return ChallengeRegistry.instance;
    }
    
    public void registerAll(final Challenge... challenges) {
        for (final Challenge challenge : challenges) {
            this.register(challenge);
        }
    }
    
    public void register(final Challenge challenge) {
        Validate.notNull(challenge);
        this.challenges.add(new Pair<>(challenge.getTier(), challenge));
        System.out.print("[CloudPass] Registered Challenge \"" + challenge.getName() + "\" for Tier \"" + challenge.getTier().getName() + "\"");
        if (Listener.class.isAssignableFrom(challenge.getClass())) {
            this.plugin.loadListeners((Listener)challenge);
        }
    }
    
    public void unregisterAll(final Challenge... challenges) {
        for (final Challenge challenge : challenges) {
            this.unregister(challenge);
        }
    }
    
    public void unregister(final Challenge challenge) {
        Validate.notNull((Object)challenge);
        Pair<Tier, Challenge> pair = null;
        for (final Pair<Tier, Challenge> challengePair : this.challenges) {
            if (challengePair.getKey().getName().equalsIgnoreCase(challenge.getTier().getName()) && challengePair.getValue().getName().equals(challenge.getName())) {
                pair = challengePair;
                if (Listener.class.isAssignableFrom(challenge.getClass())) {
                    HandlerList.unregisterAll((Listener)challenge);
                    break;
                }
                break;
            }
        }
        if (pair != null) {
            this.challenges.remove(pair);
        }
    }
    
    public List<Pair<Tier, Challenge>> getChallenges() {
        return this.challenges;
    }
}
