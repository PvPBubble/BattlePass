// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.commands.tabcomplete;

import org.bukkit.entity.Player;
import java.util.Iterator;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.TabCompleter;

public class CloudPassTabCompleter implements TabCompleter
{
    public final List<String> subCommands;
    
    public CloudPassTabCompleter() {
        Collections.sort(this.subCommands = Arrays.asList("challenges", "progress", "rewards", "reload", "give"));
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("cloudpass")) {
            if (args.length == 1) {
                if (!args[0].isEmpty()) {
                    final String argument = args[0].toLowerCase();
                    final List<String> foundSubCommands = new ArrayList<String>();
                    for (final String command : this.subCommands) {
                        if (command.toLowerCase().startsWith(argument)) {
                            foundSubCommands.add(command);
                        }
                    }
                    Collections.sort(foundSubCommands);
                    return foundSubCommands;
                }
                return this.subCommands;
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
                final List<String> players = new ArrayList<String>();
                Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
                if (!args[1].isEmpty()) {
                    final String incompletePlayer = args[1].toLowerCase();
                    final List<String> foundPossiblePlayers = new ArrayList<String>();
                    for (final String playerName : players) {
                        if (playerName.toLowerCase().startsWith(incompletePlayer)) {
                            foundPossiblePlayers.add(playerName);
                        }
                    }
                    Collections.sort(foundPossiblePlayers);
                    return foundPossiblePlayers;
                }
                Collections.sort(players);
                return players;
            }
        }
        return null;
    }
}
