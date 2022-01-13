// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges;

import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.data.player.DataPlayer;
import io.fazal.cloudpass.tier.TierManager;
import io.fazal.cloudpass.data.player.DataManager;
import org.bukkit.command.CommandSender;
import io.fazal.cloudpass.utils.Utils;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import io.fazal.cloudpass.tier.Tier;

public abstract class Challenge
{
    private final String NAME;
    private final Tier TIER;
    private final int MAX;
    private final String DESCRIPTION;
    private final String world;
    private final String region;
    private Map<UUID, Integer> players;
    
    public Challenge(final String name, final Tier tier, final int max, final String description, String world, String region) {
        this.NAME = name;
        this.TIER = tier;
        this.MAX = max;
        this.DESCRIPTION = description;
        this.world = world;
        this.region = region;
        this.players = new HashMap<UUID, Integer>();
    }
    
    public String getName() {
        return this.NAME;
    }
    
    public Tier getTier() {
        return this.TIER;
    }
    
    public int getMax() {
        return this.MAX;
    }
    
    public boolean isFree() {
        return this.getTier().isFree();
    }
    
    public boolean isDone(final Player player) {
        return this.getProgress(player) >= this.getMax();
    }
    
    public int getProgress(final Player player) {
        if (!this.players.containsKey(player.getUniqueId())) {
            this.players.put(player.getUniqueId(), 0);
        }
        return this.players.getOrDefault(player.getUniqueId(), 0);
    }
    
    public Map<UUID, Integer> getPlayers() {
        return this.players;
    }

    public void forceComplete(Player player) {
        players.put(player.getUniqueId(), getMax());
        Utils.getInstance().sendMessage(player, "CHALLENGE_COMPLETE", new String[] { "%tier%" }, new String[] { this.getTier().getName() });
        Utils.getInstance().playSound(player, "CHALLENGE_COMPLETE");
        final DataPlayer data = DataManager.getInstance().getByPlayer(player);
        data.setChallengesCompleted(data.getChallengesCompleted() + 1);
        TierManager.getInstance().updateTier(player, this.getTier());
    }
    
    public void setPlayers(final Map<UUID, Integer> players) {
        this.players = players;
    }
    
    public void addProgress(final Player player) {
        if (this.getProgress(player) == this.getMax()) {
            return;
        }
        final int progress = this.players.remove(player.getUniqueId()) + 1;
        this.players.put(player.getUniqueId(), progress);
        if (this.getMax() / 10 >= 1) {
            if (this.getProgress(player) % (this.getMax() / 10) == 0) {
                Utils.getInstance().sendMessage((CommandSender)player, "CHALLENGE_UPDATE", new String[] { "%percentage%", "%challenge%", "%tier%" }, new String[] { Utils.getInstance().getPercentage(this.getProgress(player), this.getMax()) + "%", this.getName(), this.getTier().getName() });
            }
        }
        else {
            Utils.getInstance().sendMessage((CommandSender)player, "CHALLENGE_UPDATE", new String[] { "%percentage%", "%challenge%", "%tier%" }, new String[] { Utils.getInstance().getPercentage(this.getProgress(player), this.getMax()) + "%", this.getName(), this.getTier().getName() });
        }
        if (this.isDone(player)) {
            Utils.getInstance().sendMessage((CommandSender)player, "CHALLENGE_COMPLETE", new String[] { "%tier%" }, new String[] { this.getTier().getName() });
            Utils.getInstance().playSound(player, "CHALLENGE_COMPLETE");
            final DataPlayer data = DataManager.getInstance().getByPlayer(player);
            data.setChallengesCompleted(data.getChallengesCompleted() + 1);
            TierManager.getInstance().updateTier(player, this.getTier());
        }
    }
    
    public boolean canDo(final Player player) {
        if (!this.getTier().isReleased()) {
            return false;
        }
        final DataPlayer data = DataManager.getInstance().getByPlayer(player);
        if (data != null) {
            if (data.isMaxTier()) {
                return false;
            }
            if (this.getTier().getIndex() <= data.getTier()) {
                if (this.getTier().isFree()) {
                    return !this.isDone(player);
                }
                if (data.hasPurchaed()) {
                    return !this.isDone(player);
                }
            }
        }
        return false;
    }
    
    public String getDescription() {
        return this.DESCRIPTION;
    }
    
    public void dumpToFile() {
        for (final Map.Entry<UUID, Integer> entry : this.players.entrySet()) {
            final String uuid = entry.getKey().toString();
            final int progress = entry.getValue();
            Main.getInstance().getData().set("data." + uuid + ".challenges." + this.getTier().getName() + "." + this.getName(), (Object)progress);
        }
    }
    
    public void dumpToRam() {
        if (Main.getInstance().getData().getConfigurationSection("data") != null) {
            for (final String uuidString : Main.getInstance().getData().getConfigurationSection("data").getKeys(false)) {
                try {
                    final UUID uuid = UUID.fromString(uuidString);
                    final int progress = Main.getInstance().getData().getInt("data." + uuidString + ".challenges." + this.getTier().getName() + "." + this.getName());
                    this.players.put(uuid, progress);
                }
                catch (Exception ignored) {}
            }
        }
    }

    protected boolean isApplicableToPlayer(Player player) {
        if (!world.equals("") && !player.getWorld().getName().equalsIgnoreCase(world))
            return false;
        if (!region.equals("") && !Utils.getApplicableRegions(player.getLocation()).contains(region.toUpperCase()))
            return false;
        return true;
    }

}
