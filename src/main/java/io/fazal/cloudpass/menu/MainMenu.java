// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.menu;

import org.bukkit.inventory.ItemStack;
import io.fazal.cloudpass.menus.InventoryClickType;
import io.fazal.cloudpass.menus.Menu;
import io.fazal.cloudpass.menus.MenuItem;
import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.utils.Utils;
import io.fazal.cloudpass.menus.MenuAPI;
import org.bukkit.entity.Player;

public class MainMenu
{
    public MainMenu(final Player player) {
        final Menu menu = MenuAPI.getInstance().createMenu(Utils.getInstance().toColor(Main.getInstance().getConfig().getString("Menus.Main.Title", "&8&lCLOUD PASS - &a&l[MENU]")), Main.getInstance().getConfig().getInt("Menus.Main.Size", 27) / 9);
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(final Player player, final InventoryClickType clickType) {
                menu.setBypassMenuCloseBehaviour(true);
                menu.closeMenu(player);
                new ChallengesMenu(player);
            }
            
            @Override
            public ItemStack getItemStack() {
                return Utils.getInstance().loadItem("Menus.Main.Items.Challenges");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Main.Items.Challenges.Slot", 10));
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(final Player player, final InventoryClickType clickType) {
                menu.setBypassMenuCloseBehaviour(true);
                menu.closeMenu(player);
                new ProgressMenu(player);
            }
            
            @Override
            public ItemStack getItemStack() {
                return Utils.getInstance().loadItem("Menus.Main.Items.Progress");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Main.Items.Progress.Slot", 13));
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(final Player player, final InventoryClickType clickType) {
                menu.setBypassMenuCloseBehaviour(true);
                menu.closeMenu(player);
                new RewardsMenu(player);
            }
            
            @Override
            public ItemStack getItemStack() {
                return Utils.getInstance().loadItem("Menus.Main.Items.Rewards");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Main.Items.Rewards.Slot", 16));
        for (int free = 0; free < menu.getInventory().getSize(); ++free) {
            if (menu.getInventory().getItem(free) == null) {
                if (menu.getPageSlots() == null || !menu.getPageSlots().contains(free)) {
                    final MenuItem.UnclickableMenuItem item = new MenuItem.UnclickableMenuItem() {
                        @Override
                        public void onClick(final Player p0, final InventoryClickType p1) {
                        }
                        
                        @Override
                        public ItemStack getItemStack() {
                            return Utils.getInstance().loadItem("Menus.Main.Items.Border");
                        }
                    };
                    menu.addMenuItem(item, free);
                }
            }
        }
        menu.openMenu(player);
    }
}
