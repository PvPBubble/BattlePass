// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.commands;

import io.fazal.cloudpass.data.player.DataPlayer;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import io.fazal.cloudpass.tier.TierManager;
import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.tier.Tier;
import io.fazal.cloudpass.utils.pair.Pair;
import io.fazal.cloudpass.challenges.ChallengeRegistry;
import io.fazal.cloudpass.data.player.DataManager;
import io.fazal.cloudpass.challenges.Challenge;
import java.util.ArrayList;
import io.fazal.cloudpass.menu.RewardsMenu;
import io.fazal.cloudpass.menu.ProgressMenu;
import io.fazal.cloudpass.menu.ChallengesMenu;
import io.fazal.cloudpass.menu.MainMenu;
import io.fazal.cloudpass.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CloudPassCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

            if (args.length == 0) {
                if (sender instanceof Player) {
                    final Player player = (Player)sender;
                    if (!Utils.getInstance().hasCooldown(player)) {
                        new MainMenu(player);
                        Utils.getInstance().addCooldown(player);
                        Utils.getInstance().playSound(player, "MENU_OPEN");
                    }
                }
                else {
                    sender.sendMessage("§4You must be a player to open the CloudPass GUI!");
                }
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("challenges")) {
                    if (sender instanceof Player) {
                        final Player player = (Player)sender;
                        new ChallengesMenu(player);
                    }
                }
                else if (args[0].equalsIgnoreCase("progress")) {
                    if (sender instanceof Player) {
                        final Player player = (Player)sender;
                        new ProgressMenu(player);
                    }
                }
                else if (args[0].equalsIgnoreCase("rewards")) {
                    if (sender instanceof Player) {
                        final Player player = (Player)sender;
                        new RewardsMenu(player);
                    }
                }
                else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("cloudpass.command.reload")) {
                        final long startTime = System.currentTimeMillis();
                        final List<Challenge> listToRemove = new ArrayList<Challenge>();
                        DataManager.getInstance().unregister();
                        for (final Pair<Tier, Challenge> pair : ChallengeRegistry.getInstance().getChallenges()) {
                            listToRemove.add(pair.getValue());
                        }
                        for (final Challenge challenge : listToRemove) {
                            ChallengeRegistry.getInstance().unregister(challenge);
                        }
                        DataManager.getInstance().register();
                        Main.getInstance().reloadConfig();
                        TierManager.getInstance().load();
                        Utils.getInstance().sendMessage(sender, "CONFIG_RELOAD", new String[] { "%ms%" }, new String[] { System.currentTimeMillis() - startTime + "" });
                    }
                    else {
                        Utils.getInstance().sendMessage(sender, "NO_PERMISSION");
                    }
                }
                else {
                    this.sendHelpMessage(sender, label);
                }
            }
            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (sender.hasPermission("cloudpass.command.give.pass")) {
                        final Player player = Bukkit.getPlayer(args[1]);
                        if (player != null) {
                            final DataPlayer dataPlayer = DataManager.getInstance().getByPlayer(player);
                            if (dataPlayer != null) {
                                if (!dataPlayer.hasPurchaed()) {
                                    dataPlayer.setPurchased(true);
                                    Utils.getInstance().sendMessage(sender, "GIVE_PASS", new String[] { "%player%" }, new String[] { args[1] });
                                    Utils.getInstance().sendMessage((CommandSender)player, "GIVEN_PASS");
                                }
                                else {
                                    Utils.getInstance().sendMessage(sender, "TARGET_ALREADY_OWNS", new String[] { "%player%" }, new String[] { args[1] });
                                }
                            }
                            else {
                                sender.sendMessage(ChatColor.DARK_RED + "Seems like some unexpected happened... (Error Code: 1A)");
                            }
                        }
                        else {
                            Utils.getInstance().sendMessage(sender, "TARGET_OFFLINE", new String[] { "%player%" }, new String[] { args[1] });
                        }
                    }
                }
                else {
                    this.sendHelpMessage(sender, label);
                }
            }
            else {
                this.sendHelpMessage(sender, label);
            }

        return false;
    }
    
    private void sendHelpMessage(final CommandSender player, final String command) {
        if (player.hasPermission("cloudpass.command.adminhelp")) {
            Utils.getInstance().sendMessage(player, "ADMIN_HELP", new String[] { "<command>" }, new String[] { command });
            return;
        }
        Utils.getInstance().sendMessage(player, "HELP", new String[] { "<command>" }, new String[] { command });
    }
}
