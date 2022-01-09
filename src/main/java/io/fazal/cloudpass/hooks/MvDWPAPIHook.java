// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.hooks;

import io.fazal.cloudpass.data.player.DataPlayer;
import io.fazal.cloudpass.data.player.DataManager;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import org.bukkit.plugin.Plugin;
import be.maximvdw.placeholderapi.PlaceholderAPI;
import io.fazal.cloudpass.Main;

public class MvDWPAPIHook
{
    private static MvDWPAPIHook instance;
    
    public static MvDWPAPIHook getInstance() {
        if (MvDWPAPIHook.instance == null) {
            synchronized (MvDWPAPIHook.class) {
                if (MvDWPAPIHook.instance == null) {
                    MvDWPAPIHook.instance = new MvDWPAPIHook();
                }
            }
        }
        return MvDWPAPIHook.instance;
    }
    
    public void hook() {
        PlaceholderAPI.registerPlaceholder((Plugin)Main.getInstance(), "cloudpass_tier", e -> {
            final DataPlayer dataPlayer = DataManager.getInstance().getByPlayer(e.getPlayer());
            if (dataPlayer == null) {
                return "Error Code: MVDW-A";
            }
            if (!dataPlayer.isMaxTier()) {
                return String.valueOf(dataPlayer.getTier());
            }
            return "Max Tier";
        });
    }
}
