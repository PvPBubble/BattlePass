// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.data.player;

import org.bukkit.Bukkit;
import java.util.UUID;
import io.fazal.cloudpass.tier.TierManager;
import io.fazal.cloudpass.Main;
import java.util.ArrayList;
import io.fazal.cloudpass.tier.Tier;
import java.util.List;

public class DataPlayer
{
    private final String uuid;
    private boolean purchased;
    private int tier;
    private int challengesCompleted;
    private int tiersCompleted;
    private int rewardsRedeemed;
    private List<Tier> unclaimedRewards;
    
    public DataPlayer(final String uuid, final boolean purchased, final int tier, final int challengesCompleted, final int tiersCompleted, final int rewardsRedeemed) {
        this.uuid = uuid;
        this.purchased = purchased;
        this.tier = tier;
        this.challengesCompleted = challengesCompleted;
        this.tiersCompleted = tiersCompleted;
        this.rewardsRedeemed = rewardsRedeemed;
        this.unclaimedRewards = new ArrayList<Tier>();
        if (Main.getInstance().getData().getIntegerList("data." + uuid + ".unclaimed") == null) {
            return;
        }
        Main.getInstance().getData().getIntegerList("data." + uuid + ".unclaimed").forEach(tierIndex -> this.unclaimedRewards.add(TierManager.getInstance().getFromIndex(tierIndex)));
    }
    
    public int getTier() {
        return this.tier;
    }
    
    public void setTier(final int tier) {
        this.tier = tier;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public boolean hasPurchaed() {
        return this.purchased;
    }
    
    public void setPurchased(final boolean purchased) {
        this.purchased = purchased;
    }
    
    public int getChallengesCompleted() {
        return this.challengesCompleted;
    }
    
    public void setChallengesCompleted(final int challengesCompleted) {
        this.challengesCompleted = challengesCompleted;
    }
    
    public int getTiersCompleted() {
        return this.tiersCompleted;
    }
    
    public int getRewardsRedeemed() {
        return this.rewardsRedeemed;
    }
    
    public void setRewardsRedeemed(final int rewardsRedeemed) {
        this.rewardsRedeemed = rewardsRedeemed;
    }
    
    public void setTiersCompelted(final int tiersCompelted) {
        this.tiersCompleted = tiersCompelted;
    }
    
    public List<Tier> getUnclaimedRewards() {
        return this.unclaimedRewards;
    }
    
    public void setUnclaimedRewards(final List<Tier> unclaimedRewards) {
        this.unclaimedRewards = unclaimedRewards;
    }
    
    public void claimReward(final Tier tier) {
        if (this.unclaimedRewards.contains(tier)) {
            tier.executeCommands(Bukkit.getPlayer(UUID.fromString(this.uuid)));
            this.unclaimedRewards.remove(tier);
            ++this.rewardsRedeemed;
        }
    }
    
    public boolean isMaxTier() {
        return this.getTier() > TierManager.getInstance().getMaxTier();
    }
}
