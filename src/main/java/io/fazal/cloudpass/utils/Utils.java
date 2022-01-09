// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.utils;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import io.fazal.cloudpass.utils.sound.Sounds;
import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.challenges.Challenge;
import io.fazal.cloudpass.data.player.DataPlayer;
import io.fazal.cloudpass.data.player.DataManager;
import io.fazal.cloudpass.tier.Tier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.EntityType;

public class Utils
{
    private static Utils instance;
    private Map<UUID, Long> cooldowns;
    private Map<String, EntityType> stringToEntity;
    
    public Utils() {
        this.cooldowns = new HashMap<UUID, Long>();
        this.stringToEntity = new HashMap<String, EntityType>();
        for (final EntityType entityType : EntityType.values()) {
            this.stringToEntity.put(entityType.name().toLowerCase(), entityType);
            this.stringToEntity.put(entityType.name().replace("_", " ").toLowerCase(), entityType);
            this.stringToEntity.put(entityType.name().replace("_", "").toLowerCase(), entityType);
            this.stringToEntity.put(String.valueOf(entityType.getTypeId()), entityType);
            if (entityType.getName() != null) {
                this.stringToEntity.put(entityType.getName().toLowerCase(), entityType);
            }
        }
        this.addAllOneValue(this.stringToEntity, EntityType.CREEPER, "c", "creep", "cataclysm");
        this.addAllOneValue(this.stringToEntity, EntityType.SKELETON, "s", "sk", "skelly", "skellington");
        this.addAllOneValue(this.stringToEntity, EntityType.SPIDER, "sp", "bug");
        this.addAllOneValue(this.stringToEntity, EntityType.GIANT, "giantzombie");
        this.addAllOneValue(this.stringToEntity, EntityType.ZOMBIE, "z", "zed");
        this.addAllOneValue(this.stringToEntity, EntityType.SLIME, "sl");
        this.addAllOneValue(this.stringToEntity, EntityType.GHAST, "g", "ghost");
        this.addAllOneValue(this.stringToEntity, EntityType.PIG_ZOMBIE, "pigman", "zombiepigman", "pg", "zp");
        this.addAllOneValue(this.stringToEntity, EntityType.ENDERMAN, "e", "ender", "endermen", "slenderman", "slender");
        this.addAllOneValue(this.stringToEntity, EntityType.CAVE_SPIDER, "cspider", "cs", "bluespider");
        this.addAllOneValue(this.stringToEntity, EntityType.SILVERFISH, "sf", "sfish");
        this.addAllOneValue(this.stringToEntity, EntityType.BLAZE, "bl", "b");
        this.addAllOneValue(this.stringToEntity, EntityType.MAGMA_CUBE, "magmacube", "mcube", "magma", "m", "mc");
        this.addAllOneValue(this.stringToEntity, EntityType.ENDER_DRAGON, "dragon", "raqreqentba");
        this.addAllOneValue(this.stringToEntity, EntityType.PIG, "p");
        this.addAllOneValue(this.stringToEntity, EntityType.SHEEP, "sh");
        this.addAllOneValue(this.stringToEntity, EntityType.COW, "bovine");
        this.addAllOneValue(this.stringToEntity, EntityType.CHICKEN, "ch", "chick", "bird", "kfc");
        this.addAllOneValue(this.stringToEntity, EntityType.SQUID, "sq");
        this.addAllOneValue(this.stringToEntity, EntityType.WOLF, "w", "dog");
        this.addAllOneValue(this.stringToEntity, EntityType.MUSHROOM_COW, "mc", "mcow");
        this.addAllOneValue(this.stringToEntity, EntityType.SNOWMAN, "golem", "sgolem", "sg", "sm", "snowmen");
        this.addAllOneValue(this.stringToEntity, EntityType.OCELOT, "ocelot", "oce", "o", "cat", "kitty");
        this.addAllOneValue(this.stringToEntity, EntityType.IRON_GOLEM, "igolem", "ironman", "iron", "ig");
        this.addAllOneValue(this.stringToEntity, EntityType.VILLAGER, "v", "npc");
        this.addAllOneValue(this.stringToEntity, EntityType.WITCH, "hag", "sibly", "sorceress");
        this.addAllOneValue(this.stringToEntity, EntityType.BAT, "batman");
        this.addAllOneValue(this.stringToEntity, EntityType.WITHER, "wboss");
        this.addAllOneValue(this.stringToEntity, EntityType.HORSE, "horse", "h", "bronco", "pony");
        this.addAllOneValue(this.stringToEntity, EntityType.RABBIT, "r", "rab", "bunny", "hare", "cony", "coney");
        this.addAllOneValue(this.stringToEntity, EntityType.ENDERMAN, "mite", "acarid", "acarian", "acarine");
        this.addAllOneValue(this.stringToEntity, EntityType.GUARDIAN, "keeper", "guard", "watcher");
    }
    
    private void addAllOneValue(final Map<String, EntityType> map, final EntityType value, final String... keys) {
        for (final String key : keys) {
            map.put(key, value);
        }
    }
    
    public static Utils getInstance() {
        if (Utils.instance == null) {
            synchronized (Utils.class) {
                if (Utils.instance == null) {
                    Utils.instance = new Utils();
                }
            }
        }
        return Utils.instance;
    }
    
    public static String formatTime(final Long timeLeft) {
        final int days = (int)TimeUnit.MILLISECONDS.toDays(timeLeft);
        final int hours = (int)(TimeUnit.MILLISECONDS.toHours(timeLeft) - TimeUnit.DAYS.toHours(days));
        final int minutes = (int)(TimeUnit.MILLISECONDS.toMinutes(timeLeft) - (TimeUnit.DAYS.toMinutes(days) + TimeUnit.HOURS.toMinutes(hours)));
        return String.format("%02d days %02d hours %02d minutes", days, hours, minutes);
    }
    
    public String getProgressBar(final int current, final int max, final int totalBars, final char symbol, final ChatColor completedColor, final ChatColor incompleteColor) {
        final float percent = current / (float)max;
        final int completeBars = (int)(totalBars * percent);
        return completedColor + StringUtils.repeat(symbol, completeBars) + incompleteColor + StringUtils.repeat(symbol, totalBars - completeBars);
    }
    
    public String toColor(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    public void parsePlaceholders(final ItemStack item, final String[] replace, final String[] replacement) {
        if (replace.length == replacement.length) {
            final ItemMeta meta = item.getItemMeta();
            String displayname = meta.getDisplayName();
            final List<String> lore = (List<String>)meta.getLore();
            for (int i = 0; i < replace.length; ++i) {
                displayname = displayname.replace(replace[i], replacement[i]);
            }
            for (int i = 0; i < lore.size(); ++i) {
                String line = lore.get(i);
                for (int i2 = 0; i2 < replace.length; ++i2) {
                    line = line.replaceAll(replace[i2], replacement[i2]);
                }
                line = this.toColor(line);
                lore.set(i, line);
            }
            displayname = this.toColor(displayname);
            meta.setDisplayName(displayname);
            meta.setLore((List)lore);
            item.setItemMeta(meta);
        }
    }
    
    public ItemStack getProgresItem(final Player player, final Tier tier) {
        final DataPlayer data = DataManager.getInstance().getByPlayer(player);
        final String status = tier.isReleased() ? ((tier.getIndex() == data.getTier()) ? "InProgress" : ((data.getTier() < tier.getIndex()) ? "Locked" : "Unlocked")) : "Locked";
        return this.loadItem("Tiers." + tier.getName() + ".Items.Progress." + status);
    }
    
    public ItemStack getChallengeItem(final Player player, final Challenge challenge) {
        final String status = challenge.getTier().isReleased() ? (challenge.canDo(player) ? ((challenge.getProgress(player) > 0) ? "InProgress" : "NotStarted") : (challenge.isDone(player) ? "Completed" : "Locked")) : "Unreleased";
        final ItemStack item = this.loadItem("Menus.Challenges.Items." + status);
        this.parsePlaceholders(item, new String[] { "%tier%", "%description%", "%progressbar%", "%percent%", "%current%", "%max%" }, new String[] { challenge.getTier().getName() + "", challenge.getDescription(), this.getProgressBar(challenge.getProgress(player), challenge.getMax(), 36, '|', ChatColor.GREEN, ChatColor.RED), this.getPercentage(challenge.getProgress(player), challenge.getMax()) + "", challenge.getProgress(player) + "", challenge.getMax() + "" });
        if (status.equalsIgnoreCase("Unreleased")) {
            this.parsePlaceholders(item, new String[] { "%time%" }, new String[] { formatTime(challenge.getTier().releaseInMillis(challenge.getTier().getReleaseTime()) - System.currentTimeMillis()) });
        }
        return item;
    }
    
    public double getPercentage(final int current, final int max) {
        return Math.round(current * 100 / max * 100.0) / 100.0;
    }
    
    public void playSound(final Player player, final String config) {
        final String path = "Sounds." + config;
        if (Main.getInstance().getConfig().getBoolean(path + ".enabled", true)) {
            final float volume = (float)Main.getInstance().getConfig().getDouble(path + ".volume");
            final float pitch = (float)Main.getInstance().getConfig().getDouble(path + ".pitch");
            player.playSound(player.getLocation(), Sounds.valueOf(Main.getInstance().getConfig().getString(path + ".sound").toUpperCase()).bukkitSound(), volume, pitch);
        }
    }
    
    public void sendMessage(final CommandSender sender, final String config) {
        this.sendMessage(sender, config, null, null);
    }
    
    public void sendMessage(final CommandSender sender, final String config, final String[] replace, final String[] replacement) {
        for (String message : Main.getInstance().getConfig().getStringList("Messages." + config)) {
            if (replace != null && replacement != null && replace.length == replacement.length && replace.length > 0) {
                for (int i = 0; i < replace.length; ++i) {
                    message = message.replace(replace[i], replacement[i]);
                }
            }
            message = this.toColor(message);
            sender.sendMessage(message);
        }
    }
    
    public ItemStack loadItem(final String path) {
        return this.loadItem(path, null);
    }
    
    public ItemStack loadItem(final String path, final Player player) {
        final ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection(path);
        if (section == null) {
            System.out.print("[CloudPass] \"" + path + "\" path does not exist");
            return null;
        }
        final String materialName = section.getString("Material", "STONE");
        Material material = Material.getMaterial(materialName);
        int data = 0;
        String name = section.getString("Name");
        name = this.toColor(name);
        final List<String> lore = new ArrayList<String>();
        section.getStringList("Lore").stream().map(this::toColor).forEach(lore::add);
        if (material == null) {
            if (this.isInteger(materialName)) {
                material = Material.getMaterial(Integer.parseInt(materialName));
            }
            else if (materialName.contains(";")) {
                final String[] materialArgs = materialName.trim().split(";");
                if (this.isInteger(materialArgs[0])) {
                    material = Material.getMaterial(Integer.parseInt(materialArgs[0]));
                    data = Integer.parseInt(materialArgs[1]);
                }
            }
            else if (materialName.contains(":")) {
                final String[] materialArgs = materialName.trim().split(":");
                if (this.isInteger(materialArgs[0])) {
                    material = Material.getMaterial(Integer.parseInt(materialArgs[0]));
                    data = Integer.parseInt(materialArgs[1]);
                }
            }
            else if (materialName.contains(",")) {
                final String[] materialArgs = materialName.trim().split(",");
                if (this.isInteger(materialArgs[0])) {
                    material = Material.getMaterial(Integer.parseInt(materialArgs[0]));
                    data = Integer.parseInt(materialArgs[1]);
                }
            }
            else {
                try {
                    material = Main.getInstance().getEss().getItemDb().get(materialName, 1).getType();
                }
                catch (Exception ex) {}
            }
            if (material == null) {
                material = Material.STONE;
                System.out.print("[CloudPass] Unable to load material \"" + materialName + "\" for path \"" + path + "\", so it has been set to STONE.");
            }
        }
        if (data == 0) {
            data = section.getInt("Data", 0);
        }
        final ItemStack itemStack = new ItemStack(material, 1);
        itemStack.setDurability((short)data);
        final ItemMeta meta = itemStack.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (!lore.isEmpty()) {
            meta.setLore((List)lore);
        }
        if (itemStack.getType() == Material.SKULL_ITEM && itemStack.getDurability() == 3 && section.getString("Owner") != null) {
            if (player != null && player.getName() != null && section.getString("Owner").equalsIgnoreCase("%player%")) {
                ((SkullMeta)meta).setOwner(player.getName());
            }
            else {
                ((SkullMeta)meta).setOwner(section.getString("Owner", "Fazal"));
            }
        }
        itemStack.setAmount(section.getInt("Amount", 1));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    public boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException | NullPointerException ex2) {
            ex2.printStackTrace();
            return false;
        }
        return true;
    }
    
    public EntityType getEntity(final String name) {
        return this.stringToEntity.getOrDefault(name.toLowerCase(), null);
    }
    
    public Material getMaterial(final String materialName) {
        Material material = Material.getMaterial(materialName.toUpperCase().replace(" ", "_"));
        if (material == null) {
            if (this.isInteger(materialName)) {
                material = Material.getMaterial(Integer.parseInt(materialName));
            }
            else if (materialName.contains(";")) {
                final String[] materialArgs = materialName.trim().split(";");
                if (this.isInteger(materialArgs[0])) {
                    material = Material.getMaterial(Integer.parseInt(materialArgs[0]));
                }
            }
            else if (materialName.contains(":")) {
                final String[] materialArgs = materialName.trim().split(":");
                if (this.isInteger(materialArgs[0])) {
                    material = Material.getMaterial(Integer.parseInt(materialArgs[0]));
                }
            }
            else if (materialName.contains(",")) {
                final String[] materialArgs = materialName.trim().split(",");
                if (this.isInteger(materialArgs[0])) {
                    material = Material.getMaterial(Integer.parseInt(materialArgs[0]));
                }
            }
            else {
                try {
                    material = Main.getInstance().getEss().getItemDb().get(materialName, 1).getType();
                }
                catch (Exception ex) {}
            }
        }
        return material;
    }
    
    public void addCooldown(final Player player) {
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 5000L);
    }
    
    public boolean hasCooldown(final Player player) {
        if (this.cooldowns.containsKey(player.getUniqueId())) {
            final Long cooldown = this.cooldowns.get(player.getUniqueId());
            if (cooldown > System.currentTimeMillis()) {
                this.playSound(player, "COOLDOWN");
                this.sendMessage(player, "COOLDOWN");
                return true;
            }
            this.cooldowns.remove(player.getUniqueId());
        }
        return false;
    }

    public static List<String> getApplicableRegions(final Location l) {
        if (Main.getInstance().getWorldGuardPlugin() == null)
            throw new UnsupportedOperationException("WorldGuard is not loaded.");
        final ApplicableRegionSet regions = Main.getInstance().getWorldGuardPlugin().getRegionManager(l.getWorld()).getApplicableRegions(l);
        List<String> applicationsRegions = new ArrayList<>();
        regions.getRegions().forEach(region -> applicationsRegions.add(region.getId().toUpperCase()));
        return applicationsRegions;
    }

}
