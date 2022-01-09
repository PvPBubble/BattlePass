// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges.types;

import io.fazal.cloudpass.challenges.Challenge;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.event.Listener;

public class OpenCrazyCrateKeyChallenge extends Challenge implements Listener {
	private final String CRATE;

	public OpenCrazyCrateKeyChallenge(final String name, final Tier tier, final int max, final String description, final String crate, String world, String region) {
		super(name, tier, max, description, world, region);
		this.CRATE = crate;
	}


}
