// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.hooks;

import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.data.player.DataManager;
import io.fazal.cloudpass.data.player.DataPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PAPIHook extends PlaceholderExpansion
{
    private static PAPIHook instance;
    
    public PAPIHook() {
    }

    public void hook() {
        register();
    }

    public String getIdentifier() {
        return "battlepass";
    }

    public String getAuthor() {
        return "Fazal";
    }

    public String getVersion() {
        return Main.getInstance().getDescription().getVersion();
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
