// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import io.fazal.cloudpass.challenges.Challenge;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;

public class FactionCreateChallengeMC extends Challenge implements Listener {
	public FactionCreateChallengeMC(final String name, final Tier tier, final String description, String world, String region) {
		super(name, tier, 1, description, world, region);
	}

}
