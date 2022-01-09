// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.data.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import io.fazal.cloudpass.challenges.Challenge;
import io.fazal.cloudpass.tier.Tier;
import io.fazal.cloudpass.utils.pair.Pair;
import io.fazal.cloudpass.challenges.ChallengeRegistry;
import io.fazal.cloudpass.Main;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.event.Listener;

public class DataManager implements Listener
{
    private static DataManager instance;
    private CopyOnWriteArrayList<DataPlayer> players;
    
    public static DataManager getInstance() {
        if (DataManager.instance == null) {
            synchronized (DataManager.class) {
                if (DataManager.instance == null) {
                    DataManager.instance = new DataManager();
                }
            }
        }
        return DataManager.instance;
    }
    
    public void register() {
        if (this.players != null) {
            this.players.clear();
        }
        this.players = new CopyOnWriteArrayList<DataPlayer>();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            DataPlayer data = this.getByPlayer(p);
            if (data == null) {
                data = new DataPlayer(p.getUniqueId().toString(), Main.getInstance().getData().getBoolean("data." + p.getUniqueId() + ".owned", false), Main.getInstance().getData().getInt("data." + p.getUniqueId() + ".tier", 1), Main.getInstance().getData().getInt("data." + p.getUniqueId() + ".challengesCompleted", 0), Main.getInstance().getData().getInt("data." + p.getUniqueId() + ".tiersCompleted", 0), Main.getInstance().getData().getInt("data." + p.getUniqueId() + ".rewardsRedeemed", 0));
            }
            this.players.add(data);
        }
        for (final Pair<Tier, Challenge> pair : ChallengeRegistry.getInstance().getChallenges()) {
            pair.getValue().dumpToRam();
        }
    }
    
    public void unregister() {
        for (final Pair<Tier, Challenge> pair : ChallengeRegistry.getInstance().getChallenges()) {
            pair.getValue().dumpToFile();
        }
        for (final DataPlayer dataPlayer : this.players) {
            Main.getInstance().getData().set("data." + dataPlayer.getUuid() + ".owned", (Object)dataPlayer.hasPurchaed());
            Main.getInstance().getData().set("data." + dataPlayer.getUuid() + ".tier", (Object)dataPlayer.getTier());
            Main.getInstance().getData().set("data." + dataPlayer.getUuid() + ".challengesCompleted", (Object)dataPlayer.getChallengesCompleted());
            Main.getInstance().getData().set("data." + dataPlayer.getUuid() + ".tiersCompleted", (Object)dataPlayer.getTiersCompleted());
            Main.getInstance().getData().set("data." + dataPlayer.getUuid() + ".rewardsRedeemed", (Object)dataPlayer.getRewardsRedeemed());
            final List<Integer> unclaimedRewards = new ArrayList<Integer>();
            dataPlayer.getUnclaimedRewards().forEach(tier -> unclaimedRewards.add(tier.getIndex()));
            Main.getInstance().getData().set("data." + dataPlayer.getUuid() + ".unclaimed", (Object)unclaimedRewards);
        }
        Main.getInstance().saveData();
        Main.getInstance().reloadData();
        this.players.clear();
        this.players = null;
    }
    
    public DataPlayer getByPlayer(final Player p) {
        return this.getByUUID(p.getUniqueId());
    }
    
    public DataPlayer getByUUID(final UUID uuid) {
        for (final DataPlayer player : this.players) {
            if (player.getUuid().equals(uuid.toString())) {
                return player;
            }
        }
        return null;
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        DataPlayer data = this.getByPlayer(e.getPlayer());
        if (data == null) {
            data = new DataPlayer(e.getPlayer().getUniqueId().toString(), Main.getInstance().getData().getBoolean("data." + e.getPlayer().getUniqueId() + ".owned", false), Main.getInstance().getData().getInt("data." + e.getPlayer().getUniqueId() + ".tier", 1), Main.getInstance().getData().getInt("data." + e.getPlayer().getUniqueId() + ".challengesCompleted", 0), Main.getInstance().getData().getInt("data." + e.getPlayer().getUniqueId() + ".tiersCompleted", 0), Main.getInstance().getData().getInt("data." + e.getPlayer().getUniqueId() + ".rewardsRedeemed", 0));
            this.players.add(data);
        }
    }
    
    public CopyOnWriteArrayList<DataPlayer> getPlayers() {
        return this.players;
    }
    
    public void setPlayers(final CopyOnWriteArrayList<DataPlayer> players) {
        this.players = players;
    }
}
