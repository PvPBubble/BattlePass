// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.tier;

import io.fazal.cloudpass.data.player.DataPlayer;
import org.bukkit.command.CommandSender;
import io.fazal.cloudpass.data.player.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

import io.fazal.cloudpass.challenges.ChallengeRegistry;
import io.fazal.cloudpass.challenges.types.VoteChallenge;
import io.fazal.cloudpass.challenges.types.TypeChallenge;
import io.fazal.cloudpass.challenges.types.PlaceSpawnerChallengePvPing;
import io.fazal.cloudpass.challenges.types.PlaceSpawnerChallenge;
import io.fazal.cloudpass.challenges.types.PlaceChallenge;
import io.fazal.cloudpass.challenges.types.OpenSupplyCratesChallenge;
import io.fazal.cloudpass.challenges.types.OpenCrazyCrateKeyChallenge;
import io.fazal.cloudpass.challenges.types.OpenCoderCrateKeyChallenge;
import io.fazal.cloudpass.challenges.types.MineSpawnerChallenge;
import io.fazal.cloudpass.challenges.types.KillPlayerRegionChallenge;
import io.fazal.cloudpass.challenges.types.KillPlayerChallenge;
import io.fazal.cloudpass.challenges.types.KillMobChallenge;
import io.fazal.cloudpass.challenges.types.KillEnemyPlayerChallengeMC;
import io.fazal.cloudpass.challenges.types.KillEnemyPlayerChallenge;
import io.fazal.cloudpass.challenges.types.KillBossMobChallenge;
import io.fazal.cloudpass.challenges.types.FishChallenge;
import io.fazal.cloudpass.challenges.types.FactionCreateChallengeMC;
import io.fazal.cloudpass.challenges.types.FactionCreateChallenge;
import io.fazal.cloudpass.challenges.types.CraftChallenge;
import io.fazal.cloudpass.challenges.types.ConsumeChallenge;
import org.bukkit.Material;
import io.fazal.cloudpass.challenges.types.BreakChallenge;
import io.fazal.cloudpass.utils.Utils;
import io.fazal.cloudpass.challenges.ChallengeType;
import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.challenges.Challenge;

public class TierManager
{
    private static TierManager instance;
    private List<Tier> tiers;
    private Map<Tier, List<Challenge>> tierChallenges;
    private int maxTier;
    
    public TierManager() {
        this.load();
    }
    
    public static TierManager getInstance() {
        if (TierManager.instance == null) {
            synchronized (TierManager.class) {
                if (TierManager.instance == null) {
                    TierManager.instance = new TierManager();
                }
            }
        }
        return TierManager.instance;
    }
    
    public Map<Tier, List<Challenge>> getTierChallenges() {
        return this.tierChallenges;
    }
    
    public void setTierChallenges(final Map<Tier, List<Challenge>> tierChallenges) {
        this.tierChallenges = tierChallenges;
    }
    
    public List<Tier> getTiers() {
        return this.tiers;
    }
    
    public void setTiers(final List<Tier> tiers) {
        this.tiers = tiers;
    }

    public Optional<Tier> getByName(String name) {
        return tiers.stream().filter(tier -> tier.getName().equalsIgnoreCase(name)).findAny();
    }
    
    public int getMaxTier() {
        return this.maxTier;
    }
    
    public void setMaxTier(final int maxTier) {
        this.maxTier = maxTier;
    }
    
    public void addTier(final Tier tier) {
        this.tiers.add(tier);
        Main.getInstance().setTotalTiers(Main.getInstance().getTotalTiers() + 1);
    }
    
    public void load() {
        this.tiers = new ArrayList<>();
        this.tierChallenges = new HashMap<>();
        this.maxTier = 1;
        if (Main.getInstance().getConfig().getConfigurationSection("Tiers") != null) {
            int index = 1;
            for (final String tierKey : Main.getInstance().getConfig().getConfigurationSection("Tiers").getKeys(false)) {
                final Tier tier = new Tier(tierKey, Main.getInstance().getConfig().getBoolean("Tiers." + tierKey + ".Free", false), index);
                if (Main.getInstance().getConfig().getConfigurationSection("Tiers." + tierKey + ".Challenges") != null) {
                    final List<Challenge> challenges = new ArrayList<Challenge>();
                    for (final String challengeKey : Main.getInstance().getConfig().getConfigurationSection("Tiers." + tierKey + ".Challenges").getKeys(false)) {
                        final ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("Tiers." + tierKey + ".Challenges." + challengeKey);
                        Challenge challenge = null;
                        String world = section.getString("world");
                        String region = section.getString("region");
                        world = (world == null) ? "" : world;
                        region = (region == null) ? "" : region;
                        final ChallengeType challengeType = ChallengeType.getByName(section.getString("ChallengeType"));
                        if (challengeType == ChallengeType.BREAK) {
                            final Material material = Utils.getInstance().getMaterial(section.getString("Material", "STONE"));
                            final int max = section.getInt("Amount", 1);
                            final String description = section.getString("Description", "Default Description...");
                            challenge = new BreakChallenge(challengeKey, tier, max, description, material, world, region);
                        }
                        else if (challengeType == ChallengeType.CONSUME) {
                            final Material material = Material.getMaterial(section.getString("Material", "APPLE"));
                            final int data = section.getInt("Data", 0);
                            final int max2 = section.getInt("Amount", 1);
                            final String description2 = section.getString("Description", "Default Description...");
                            challenge = new ConsumeChallenge(challengeKey, tier, max2, description2, material, data, world, region);
                        }
                        else if (challengeType == ChallengeType.CRAFT) {
                            final Material material = Material.getMaterial(section.getString("Material", "DIAMOND_HELMET"));
                            final int data = section.getInt("Data", 0);
                            final int max2 = section.getInt("Amount", 1);
                            final String description2 = section.getString("Description", "Default Description...");
                            challenge = new CraftChallenge(challengeKey, tier, max2, description2, material, data, world, region);
                        }
                        else if (challengeType == ChallengeType.FACTION_CREATE) {
                            final String description3 = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isFactionsEnabled()) {
                                challenge = new FactionCreateChallenge(challengeKey, tier, description3, world, region);
                            }
                            else if (Main.getInstance().isFactionsMassiveCoreEnabled()) {
                                challenge = new FactionCreateChallengeMC(challengeKey, tier, description3, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to No Valid Factions Plugin found!");
                            }
                        }
                        else if (challengeType == ChallengeType.FISH) {
                            final int data2 = section.getInt("Data", 0);
                            final int max = section.getInt("Amount", 1);
                            final String description = section.getString("Description", "Default Description...");
                            challenge = new FishChallenge(challengeKey, tier, max, description, data2, world, region);
                        }
                        else if (challengeType == ChallengeType.KILL_BOSS_MOB) {
                            final int max3 = section.getInt("Amount", 1);
                            final String description4 = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isBossMobsICodeEnabled()) {
                                challenge = new KillBossMobChallenge(challengeKey, tier, max3, description4, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to No Valid Boss Mob Plugin found!");
                            }
                        }
                        else if (challengeType == ChallengeType.KILL_ENEMY_PLAYER) {
                            final int max3 = section.getInt("Amount", 1);
                            final String description4 = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isFactionsEnabled()) {
                                challenge = new KillEnemyPlayerChallenge(challengeKey, tier, max3, description4, world, region);
                            }
                            else if (Main.getInstance().isFactionsMassiveCoreEnabled()) {
                                challenge = new KillEnemyPlayerChallengeMC(challengeKey, tier, max3, description4, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to No Valid Factions Plugin found!");
                            }
                        }
                        else if (challengeType == ChallengeType.KILL_MOB) {
                            final EntityType type = Utils.getInstance().getEntity(section.getString("Mob", "BLAZE"));
                            final int max = section.getInt("Amount", 1);
                            final String description = section.getString("Description", "Default Description...");
                            challenge = new KillMobChallenge(challengeKey, tier, max, description, type, world, region);
                        }
                        else if (challengeType == ChallengeType.KILL_PLAYER) {
                            final int max3 = section.getInt("Amount", 1);
                            final String description4 = section.getString("Description", "Default Description...");
                            challenge = new KillPlayerChallenge(challengeKey, tier, max3, description4, world, region);
                        }
                        else if (challengeType == ChallengeType.KILL_PLAYER_REGION) {
                            final int max3 = section.getInt("Amount", 1);
                            final String description = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isWorldGuardEnabled()) {
                                challenge = new KillPlayerRegionChallenge(challengeKey, tier, max3, description, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to WorldGuard not being found!");
                            }
                        }
                        else if (challengeType == ChallengeType.MINE_SPAWNER) {
                            final EntityType type = Utils.getInstance().getEntity(section.getString("Mob", "BLAZE"));
                            final int max = section.getInt("Amount", 1);
                            final String description = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isSilkSpawnerEnabled()) {
                                challenge = new MineSpawnerChallenge(challengeKey, tier, max, description, type, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to Silk Spawners not being found!");
                            }
                        }
                        else if (challengeType == ChallengeType.OPEN_CRATE) {
                            final String crate = section.getString("Crate", "Vote");
                            final int max = section.getInt("Amount", 1);
                            final String description = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isCoderCratesEnabled()) {
                                challenge = new OpenCoderCrateKeyChallenge(challengeKey, tier, max, description, crate, world, region);
                            }
                            else if (Main.getInstance().isCrazyCratesEnabled()) {
                                challenge = new OpenCrazyCrateKeyChallenge(challengeKey, tier, max, description, crate, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to No Valid Crates Plugin found!");
                            }
                        }
                        else if (challengeType == ChallengeType.OPEN_SUPPLY_CRATE) {
                            final int max3 = section.getInt("Amount", 1);
                            final String description4 = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isSupplyCratesEnabled()) {
                                challenge = new OpenSupplyCratesChallenge(challengeKey, tier, max3, description4, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to SupplyCrates not being found!");
                            }
                        }
                        else if (challengeType == ChallengeType.PLACE) {
                            final Material material = Utils.getInstance().getMaterial(section.getString("Material", "STONE"));
                            final int max = section.getInt("Amount", 1);
                            final String description = section.getString("Description", "Default Description...");
                            challenge = new PlaceChallenge(challengeKey, tier, max, description, material, world, region);
                        }
                        else if (challengeType == ChallengeType.PLACE_SPAWNER) {
                            final EntityType type = Utils.getInstance().getEntity(section.getString("Mob", "BLAZE"));
                            final int max = section.getInt("Amount", 1);
                            final String description = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isSilkSpawnerEnabled()) {
                                challenge = new PlaceSpawnerChallenge(challengeKey, tier, max, description, type, world, region);
                            }
                            else if (Main.getInstance().isPvpingSpawnersEnabled()) {
                                challenge = new PlaceSpawnerChallengePvPing(challengeKey, tier, max, description, type, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to no compatible Spawners plugin being found!");
                            }
                        }
                        else if (challengeType == ChallengeType.TYPE) {
                            final String text = section.getString("Text", "#CloudPass");
                            final String description4 = section.getString("Description", "Default Description...");
                            challenge = new TypeChallenge(challengeKey, tier, description4, text, world, region);
                        }
                        else if (challengeType == ChallengeType.VOTE) {
                            final int max3 = section.getInt("Amount", 1);
                            final String description4 = section.getString("Description", "Default Description...");
                            if (Main.getInstance().isVotifierEnabled()) {
                                challenge = new VoteChallenge(challengeKey, tier, max3, description4, world, region);
                            }
                            else {
                                System.out.print("[CloudPass] Could not register challenge \"" + challengeKey + "\" for Tier \"" + tier.getName() + "\" due to Votifier not being found!");
                            }
                        }
                        if (challenge != null) {
                            ChallengeRegistry.getInstance().register(challenge);
                            challenges.add(challenge);
                            Main.getInstance().setTotalChallenges(Main.getInstance().getTotalChallenges() + 1);
                        }
                    }
                    this.tierChallenges.put(tier, challenges);
                }
                this.addTier(tier);
                ++index;
            }
            this.maxTier = index;
        }
    }
    
    public Tier getFromIndex(final int index) {
        if (this.tiers.get(index - 1).getIndex() == index) {
            return this.tiers.get(index - 1);
        }
        for (final Tier tier : this.tiers) {
            if (tier.getIndex() == index) {
                return tier;
            }
        }
        return null;
    }
    
    public void updateTier(final Player player, final Tier tier) {
        final DataPlayer data = DataManager.getInstance().getByPlayer(player);
        if (data.getTier() > this.getTiers().size() || data.getTier() > tier.getIndex()) {
            return;
        }
        boolean complete = tierChallenges.get(tier).stream().allMatch(c -> c.isDone(player));
        if (complete) {
            if (data.getTier() + 1 == this.maxTier) {
                Utils.getInstance().sendMessage((CommandSender)player, "TIER_COMPLETE", new String[] { "%newtier%", "%tier%" }, new String[] { "COMPLETE", tier.getName() });
            }
            else {
                Utils.getInstance().sendMessage((CommandSender)player, "TIER_COMPLETE", new String[] { "%newtier%", "%tier%" }, new String[] { this.getFromIndex(tier.getIndex() + 1).getName(), tier.getName() });
            }
            Utils.getInstance().playSound(player, "TIER_COMPLETE");
            data.setTier(data.getTier() + 1);
            data.setTiersCompelted(data.getTiersCompleted() + 1);
            data.getUnclaimedRewards().add(tier);
        }
    }
}
