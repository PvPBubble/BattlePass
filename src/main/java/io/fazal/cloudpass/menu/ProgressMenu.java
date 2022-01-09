// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.menu;

import java.util.Iterator;
import io.fazal.cloudpass.data.player.DataPlayer;
import io.fazal.cloudpass.data.player.DataManager;
import io.fazal.cloudpass.menus.Menu;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import io.fazal.cloudpass.menus.InventoryClickType;
import io.fazal.cloudpass.tier.Tier;
import io.fazal.cloudpass.tier.TierManager;
import io.fazal.cloudpass.menus.MenuItem;
import java.util.ArrayList;
import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.utils.Utils;
import io.fazal.cloudpass.menus.MenuAPI;
import org.bukkit.entity.Player;

public class ProgressMenu
{
    public ProgressMenu(final Player player) {
        final Menu menu = MenuAPI.getInstance().createMenu(Utils.getInstance().toColor(Main.getInstance().getConfig().getString("Menus.Progress.Title", "&8&lCLOUD PASS - &c&l[Progress]")), Main.getInstance().getConfig().getInt("Menus.Progress.Size", 27) / 9);
        final List<MenuItem> pageItems = new ArrayList<MenuItem>();
        for (final Tier tier : TierManager.getInstance().getTiers()) {
            pageItems.add(new MenuItem() {
                @Override
                public void onClick(final Player p0, final InventoryClickType p1) {
                }
                
                @Override
                public ItemStack getItemStack() {
                    return Utils.getInstance().getProgresItem(player, tier);
                }
            });
        }
        menu.setupPages(pageItems, Main.getInstance().getConfig().getIntegerList("Menus.Progress.Settings.ScrollSlots"));
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(final Player player, final InventoryClickType clickType) {
                if (!this.isClickable()) {
                    return;
                }
                if (!menu.previousPage(player)) {
                    this.setTemporaryIcon(Utils.getInstance().loadItem("Menus.Challenges.Items.MinPage"), 20L);
                    Utils.getInstance().playSound(player, "PAGE_SWITCH_FAIL");
                }
            }
            
            @Override
            public ItemStack getItemStack() {
                return Utils.getInstance().loadItem("Menus.Progress.Items.PreviousPage");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Progress.Items.PreviousPage.Slot", 21));
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(final Player player, final InventoryClickType clickType) {
                menu.setBypassMenuCloseBehaviour(true);
                menu.closeMenu(player);
                new MainMenu(player);
                Utils.getInstance().playSound(player, "BACK_BUTTON");
            }
            
            @Override
            public ItemStack getItemStack() {
                return Utils.getInstance().loadItem("Menus.Progress.Items.Back");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Progress.Items.Back.Slot", 22));
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(final Player player, final InventoryClickType clickType) {
                if (!this.isClickable()) {
                    return;
                }
                if (!menu.nextPage(player)) {
                    this.setTemporaryIcon(Utils.getInstance().loadItem("Menus.Challenges.Items.MaxPage"), 20L);
                    Utils.getInstance().playSound(player, "PAGE_SWITCH_FAIL");
                }
            }
            
            @Override
            public ItemStack getItemStack() {
                return Utils.getInstance().loadItem("Menus.Progress.Items.NextPage");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Progress.Items.NextPage.Slot", 23));
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(final Player player, final InventoryClickType clickType) {
            }
            
            @Override
            public ItemStack getItemStack() {
                final ItemStack item = Utils.getInstance().loadItem("Menus.Progress.Items.Info", player);
                final DataPlayer data = DataManager.getInstance().getByPlayer(player);
                Utils.getInstance().parsePlaceholders(item, new String[] { "%player%", "%challenges-completed%", "%challenges-total%", "%tiers-completed%", "%tiers-total%", "%rewards-claimed%" }, new String[] { player.getName(), data.getChallengesCompleted() + "", Main.getInstance().getTotalChallenges() + "", data.getTiersCompleted() + "", Main.getInstance().getTotalTiers() + "", data.getRewardsRedeemed() + "" });
                return item;
            }
        }, Main.getInstance().getConfig().getInt("Menus.Progress.Items.Info.Slot", 9));
        for (int free = 0; free < menu.getInventory().getSize(); ++free) {
            if (menu.getInventory().getItem(free) == null) {
                if (menu.getPageSlots() == null || !menu.getPageSlots().contains(free)) {
                    final MenuItem.UnclickableMenuItem item = new MenuItem.UnclickableMenuItem() {
                        @Override
                        public void onClick(final Player p0, final InventoryClickType p1) {
                        }
                        
                        @Override
                        public ItemStack getItemStack() {
                            return Utils.getInstance().loadItem("Menus.Progress.Items.Border");
                        }
                    };
                    menu.addMenuItem(item, free);
                }
            }
        }
        menu.openMenu(player);
        Utils.getInstance().playSound(player, "SUB_MENU_OPEN");
    }
}
