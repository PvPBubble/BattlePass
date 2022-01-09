// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.task;

import java.util.Iterator;
import io.fazal.cloudpass.data.player.DataManager;
import org.bukkit.command.CommandSender;
import io.fazal.cloudpass.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import io.fazal.cloudpass.Main;

public class SaveTask implements Runnable
{
    public SaveTask() {
        System.out.print("[CloudPass] Database save task registered! Save task will run every " + Main.getInstance().getConfig().getInt("Database.Save") + " minutes!");
    }
    
    @Override
    public void run() {
        final long startTime = System.currentTimeMillis();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() || player.hasPermission("cloudpass.admin")) {
                Utils.getInstance().sendMessage((CommandSender)player, "SAVE_TASK_START");
            }
        }
        DataManager.getInstance().unregister();
        DataManager.getInstance().register();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() || player.hasPermission("cloudpass.admin")) {
                Utils.getInstance().sendMessage((CommandSender)player, "SAVE_TASK_DONE", new String[] { "%ms%" }, new String[] { System.currentTimeMillis() - startTime + "" });
            }
        }
    }
}
