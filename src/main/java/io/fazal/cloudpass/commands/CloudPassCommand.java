// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.commands;

import com.bgsoftware.superiorskyblock.i.D;
import io.fazal.cloudpass.challenges.Challenge;
import io.fazal.cloudpass.data.player.DataManager;
import io.fazal.cloudpass.data.player.DataPlayer;
import io.fazal.cloudpass.menu.ChallengesMenu;
import io.fazal.cloudpass.menu.MainMenu;
import io.fazal.cloudpass.menu.ProgressMenu;
import io.fazal.cloudpass.menu.RewardsMenu;
import io.fazal.cloudpass.tier.Tier;
import io.fazal.cloudpass.tier.TierManager;
import io.fazal.cloudpass.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

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
                } else if (args[0].equalsIgnoreCase("complete")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        Utils.getInstance().sendMessage(sender, "TARGET_OFFLINE", new String[]{"%player%"}, new String[] { args[1]});
                        return true;
                    }
                    DataPlayer dataPlayer = DataManager.getInstance().getByPlayer(player);
                    Tier tier;
                    try {
                        tier = TierManager.getInstance().getFromIndex(dataPlayer.getTier());
                    } catch (IndexOutOfBoundsException e) {
                        Utils.getInstance().sendMessage(sender, "NO_CHALLENGES_TO_COMPLETE", new String[] {"%player%"}, new String[] {player.getName()});
                        return true;
                    }
                    Optional<Challenge> challenge = TierManager.getInstance().getTierChallenges().get(tier).stream().filter(c -> !c.isDone(player)).findFirst();
                    if (!challenge.isPresent())
                        return true;
                    challenge.get().forceComplete(player);
                    Utils.getInstance().sendMessage(sender, "TIER_SKIPPED_EXECUTOR", new String[]{"%player%"}, new String[] {player.getName()});
                    Utils.getInstance().sendMessage(player, "TIER_SKIPPED_PLAYER");
                }
                else {
                    this.sendHelpMessage(sender, label);
                }
            } else if (args.length == 3) {
                if (!args[0].equalsIgnoreCase("settier")) {
                    this.sendHelpMessage(sender, label);
                    return true;
                }
                if (!sender.hasPermission("cloudpass.command.settier")) {
                    Utils.getInstance().sendMessage(sender, "NO_PERMISSION");
                    return true;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    Utils.getInstance().sendMessage(sender, "TARGET_OFFLINE", new String[]{"%player%"}, new String[] { args[1]});
                    return true;
                }
                Optional<Tier> optionalTier = TierManager.getInstance().getByName(args[2]);
                if (!optionalTier.isPresent()) {
                    Utils.getInstance().sendMessage(sender, "TIER_NOT_FOUND", new String[] {"%attempt%"}, new String[]{args[2]});
                    return true;
                }
                Tier tier = optionalTier.get();
                DataPlayer dataPlayer = DataManager.getInstance().getByPlayer(player);
                if (dataPlayer.getTier() >= tier.getIndex()) {
                    Utils.getInstance().sendMessage(sender, "USER_ALREADY_HIGHER_TIER", new String[] {"%attempt%"}, new String[] {tier.getName()});
                    return true;
                }
                while (dataPlayer.getTier() < tier.getIndex()) {
                    Tier currentTier = TierManager.getInstance().getFromIndex(dataPlayer.getTier());
                    TierManager.getInstance().getTierChallenges().get(currentTier).forEach(c -> {
                        if (!c.isDone(player))
                            c.forceComplete(player);
                    });
                }
                Utils.getInstance().sendMessage(sender, "TIER_SET", new String[] {"%player%", "%tier%"}, new String[] {player.getName(), tier.getName()});
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
