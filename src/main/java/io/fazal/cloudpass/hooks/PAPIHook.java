// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.hooks;

import io.fazal.cloudpass.data.player.DataPlayer;
import io.fazal.cloudpass.data.player.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import io.fazal.cloudpass.Main;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class PAPIHook extends EZPlaceholderHook
{
    private static PAPIHook instance;
    
    public PAPIHook() {
        super((Plugin)Main.getInstance(), "cloudpass");
    }
    
    public static PAPIHook getInstance() {
        if (PAPIHook.instance == null) {
            synchronized (PAPIHook.class) {
                if (PAPIHook.instance == null) {
                    PAPIHook.instance = new PAPIHook();
                }
            }
        }
        return PAPIHook.instance;
    }
    
    public String onPlaceholderRequest(final Player player, final String subplaceholder) {
        if (!subplaceholder.equalsIgnoreCase("tier")) {
            return "Sublaceholder \"" + subplaceholder + "\" Not Found.";
        }
        final DataPlayer dataPlayer = DataManager.getInstance().getByPlayer(player);
        if (dataPlayer == null) {
            return "Error Code: PAPI-A";
        }
        if (!dataPlayer.isMaxTier()) {
            return String.valueOf(dataPlayer.getTier());
        }
        return "Max Tier";
    }
}
