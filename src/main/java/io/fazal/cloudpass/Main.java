// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass;

import com.earth2me.essentials.Essentials;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.dustplanet.util.SilkUtil;
import io.fazal.cloudpass.challenges.ChallengeRegistry;
import io.fazal.cloudpass.commands.CloudPassCommand;
import io.fazal.cloudpass.commands.tabcomplete.CloudPassTabCompleter;
import io.fazal.cloudpass.data.player.DataManager;
import io.fazal.cloudpass.hooks.MvDWPAPIHook;
import io.fazal.cloudpass.hooks.PAPIHook;
import io.fazal.cloudpass.menus.MenuAPI;
import io.fazal.cloudpass.task.SaveTask;
import io.fazal.cloudpass.tier.TierManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
	private static Main instance;
	private Essentials ess;
	private File dataf;
	private FileConfiguration data;
	private int totalChallenges;
	private int totalTiers;
	private SilkUtil silkutil;
	private boolean silkSpawnerEnabled;
	private WorldGuardPlugin wg;
	private boolean worldGuardEnabled;
	private boolean coderCratesEnabled;
	private boolean crazyCratesEnabled;
	private boolean supplyCratesEnabled;
	private boolean bossMobsICodeEnabled;
	private boolean factionsMassiveCoreEnabled;
	private boolean factionsEnabled;
	private boolean votifierEnabled;
	private boolean pvpingSpawnersEnabled;
	private boolean stackMob;

	public static Main getInstance() {
		return Main.instance;
	}

	public void onEnable() {
		this.saveDefaultConfig();
		this.setupData();
		instance = this;
		if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
			this.wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
			this.worldGuardEnabled = true;
			System.out.print("[CloudPass] Hooking into WorldGuard...");
		} else {
			this.worldGuardEnabled = false;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("SilkSpawners")) {
			this.silkutil = SilkUtil.hookIntoSilkSpanwers();
			this.silkSpawnerEnabled = true;
			System.out.print("[CloudPass] Hooking into SilkSpawners...");
		} else {
			this.silkSpawnerEnabled = false;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("CrazyCrates")) {
			this.crazyCratesEnabled = true;
			System.out.print("[CloudPass] Hooking into CrazyCrates...");
		} else {
			this.crazyCratesEnabled = false;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("C0d3rCrates")) {
			this.coderCratesEnabled = true;
			System.out.print("[CloudPass] Hooking into C0d3rCrates...");
		} else {
			this.coderCratesEnabled = false;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("SupplyCrates")) {
			this.supplyCratesEnabled = true;
			System.out.print("[CloudPass] Hooking into SupplyCrates...");
		} else {
			this.supplyCratesEnabled = false;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("BossMobs")) {
			this.bossMobsICodeEnabled = true;
			System.out.print("[CloudPass] Hooking into BossMobs...");
		} else {
			this.bossMobsICodeEnabled = false;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("Votifier")) {
			this.votifierEnabled = true;
			System.out.print("[CloudPass] Hooking into Votifier...");
		} else {
			this.votifierEnabled = false;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("xSpawners")) {
			this.pvpingSpawnersEnabled = true;
			System.out.print("[CloudPass] Hooking into xSpawners...");
		}

		if (Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) {
			System.out.print("[CloudPass] Found SuperiorSkyblock2...");
			this.factionsEnabled = true;
			System.out.print("[CloudPass] Hooking into SSuperiorSkyblock2..");

		}
		ChallengeRegistry.getInstance();
		TierManager.getInstance();
		this.ess = (Essentials) Bukkit.getPluginManager().

				getPlugin("Essentials");
		this.getCommand("chargepass").setExecutor(new CloudPassCommand());

		System.out.print("[CloudPass] Loading \"CloudPassCommand.class\" as /cloudpass command...");
		this.

				getCommand("chargepass").setTabCompleter(new CloudPassTabCompleter());
		System.out.print("[CloudPass] Loading \"CloudPassCommand.class\" as tab completer for /cloudpass command...");
		this.

				hookPlaceholders();
		this.

				loadListeners((Listener) MenuAPI.getInstance(), (Listener) DataManager.getInstance());
		DataManager.getInstance().

				register();
		Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin) getInstance(), (Runnable) new SaveTask(), this.getConfig().getInt("Database.Save") * 60L * 20L, this.getConfig().getInt("Database.Save") * 60L * 20L);
		System.out.print("[CloudPass] has been enabled");
	}


	public void onDisable() {
		DataManager.getInstance().unregister();
		System.out.print("[CloudPass] has been disabled");
	}

	private void hookPlaceholders() {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			PAPIHook.getInstance().hook();
			System.out.print("[CloudPass] Found PlaceholderAPI for Placeholders... Registering \"%cloudpass_tier%\"...");
		}
		if (Bukkit.getPluginManager().isPluginEnabled("MvDWPlaceholderAPI")) {
			MvDWPAPIHook.getInstance().hook();
			System.out.print("[CloudPass] Found MvDWPlaceholderAPI for Placeholders... Registering \"{cloudpass_tier}\"...");
		}
	}

	private boolean isClass(final String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public int getTotalChallenges() {
		return this.totalChallenges;
	}

	public void setTotalChallenges(final int totalChallenges) {
		this.totalChallenges = totalChallenges;
	}

	public int getTotalTiers() {
		return this.totalTiers;
	}

	public void setTotalTiers(final int totalTiers) {
		this.totalTiers = totalTiers;
	}

	public void loadListeners(final Listener... listeners) {
		for (final Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, (Plugin) this);
			System.out.print("[CloudPass] Loading \"" + listener.getClass().getSimpleName() + ".class\" as a listener...");
		}
	}

	private void setupData() {
		this.dataf = new File(this.getDataFolder(), "data.yml");
		if (!this.dataf.exists()) {
			this.dataf.getParentFile().mkdirs();
			this.saveResource("data.yml", false);
		}
		this.data = (FileConfiguration) new YamlConfiguration();
		try {
			this.data.load(this.dataf);
		} catch (IOException | InvalidConfigurationException ex2) {
			ex2.printStackTrace();
		}
	}

	public void saveData() {
		if (this.data == null || this.dataf == null) {
			return;
		}
		try {
			this.data.save(this.dataf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isStackMob() {
		return this.stackMob;
	}

	public boolean isFactionsEnabled() {
		return this.factionsEnabled;
	}

	public boolean isFactionsMassiveCoreEnabled() {
		return this.factionsMassiveCoreEnabled;
	}

	public boolean isCrazyCratesEnabled() {
		return this.crazyCratesEnabled;
	}

	public boolean isWorldGuardEnabled() {
		return this.worldGuardEnabled;
	}

	public boolean isSilkSpawnerEnabled() {
		return this.silkSpawnerEnabled;
	}

	public boolean isBossMobsICodeEnabled() {
		return this.bossMobsICodeEnabled;
	}

	public boolean isCoderCratesEnabled() {
		return this.coderCratesEnabled;
	}

	public boolean isSupplyCratesEnabled() {
		return this.supplyCratesEnabled;
	}

	public SilkUtil getSilkutil() {
		return this.silkutil;
	}

	public boolean isPvpingSpawnersEnabled() {
		return this.pvpingSpawnersEnabled;
	}

	public boolean isVotifierEnabled() {
		return this.votifierEnabled;
	}

	public WorldGuardPlugin getWorldGuardPlugin() {
		return this.wg;
	}

	public FileConfiguration getData() {
		return this.data;
	}

	public void reloadData() {
		YamlConfiguration.loadConfiguration(this.dataf);
	}

	public Essentials getEss() {
		return this.ess;
	}

	public void setEss(final Essentials ess) {
		this.ess = ess;
	}
}
