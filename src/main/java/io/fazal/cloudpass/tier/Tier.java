// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.tier;

import java.util.Iterator;

import io.fazal.cloudpass.data.player.DataManager;
import io.fazal.cloudpass.data.player.DataPlayer;
import io.fazal.cloudpass.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;

import io.fazal.cloudpass.Main;
import org.apache.commons.lang.Validate;

public class Tier
{
    private final String NAME;
    private final boolean FREE;
    private final int INDEX;
    private final String RELEASE_TIME;
    private boolean released;
    
    public Tier(final String name, final boolean free, final int index) {
        Validate.notNull((Object)name);
        this.NAME = name;
        this.FREE = free;
        this.INDEX = index;
        this.RELEASE_TIME = Main.getInstance().getConfig().getString("Tiers." + this.NAME + ".ReleaseTime");
        if (this.getReleaseTime() != null) {
            final Long releaseInMillis = this.releaseInMillis(this.getReleaseTime());
            if (releaseInMillis > System.currentTimeMillis()) {
                this.scheduleRelease((releaseInMillis - System.currentTimeMillis()) / 50L);
                this.released = false;
            }
            else {
                this.released = true;
            }
        }
    }
    
    public Long releaseInMillis(final String time) {
        return this.getReleaseCalendar(time).getTimeInMillis();
    }
    
    public GregorianCalendar getReleaseCalendar(final String time) {
        final DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss z uuuu");
        final ZonedDateTime zdt = ZonedDateTime.parse(time, f);
        return GregorianCalendar.from(zdt);
    }
    
    public String getName() {
        return this.NAME;
    }
    
    public String getReleaseTime() {
        return this.RELEASE_TIME;
    }
    
    public boolean isFree() {
        return this.FREE;
    }
    
    public int getIndex() {
        return this.INDEX;
    }
    
    public boolean isReleased() {
        return this.released;
    }
    
    public void setReleased(final boolean released) {
        this.released = released;
    }
    
    public void scheduleRelease(final long ticks) {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Main.getInstance(), this::release, ticks);
    }
    
    public void executeCommands(final Player player) {
        FileConfiguration config = Main.getInstance().getConfig();
        String prepath = "Tiers." + this.getName() + ".";
        if (!this.isFree()) {
            executeCommands(player, config.getStringList(prepath + "Commands"));
            return;
        }
        DataPlayer dataPlayer = DataManager.getInstance().getByPlayer(player);
        if (dataPlayer.hasPurchaed())
            executeCommands(player, config.getStringList(prepath + "PremiumRewards"));
        else
            executeCommands(player, config.getStringList(prepath + "FreeRewards"));
    }
    
    public void release() {
        this.setReleased(true);
        for (final Player player : Bukkit.getOnlinePlayers()) {
            Utils.getInstance().sendMessage(player, "TIER_RELEASE", new String[] { "%tier%" }, new String[] { this.getName() });
            Utils.getInstance().playSound(player, "TIER_RELEASE");
        }
    }

    private void executeCommands(Player player, List<String> commands) {
        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
    }

}
