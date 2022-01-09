// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.menu;

import java.util.Iterator;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import io.fazal.cloudpass.menus.InventoryClickType;
import io.fazal.cloudpass.data.player.DataPlayer;
import io.fazal.cloudpass.menus.Menu;
import io.fazal.cloudpass.tier.Tier;
import io.fazal.cloudpass.data.player.DataManager;
import io.fazal.cloudpass.menus.MenuItem;
import java.util.ArrayList;
import io.fazal.cloudpass.Main;
import io.fazal.cloudpass.utils.Utils;
import io.fazal.cloudpass.menus.MenuAPI;
import org.bukkit.entity.Player;

public class RewardsMenu
{
    public RewardsMenu(final Player player) {
        final Menu menu = MenuAPI.getInstance().createMenu(Utils.getInstance().toColor(Main.getInstance().getConfig().getString("Menus.Rewards.Title", "&8&lCLOUD PASS - &b&l[Rewards]")), Main.getInstance().getConfig().getInt("Menus.Rewards.Size", 54) / 9);
        final List<MenuItem> pageItems = new ArrayList<MenuItem>();
        final DataPlayer dataPlayer = DataManager.getInstance().getByPlayer(player);
        for (final Tier unclaimedReward : dataPlayer.getUnclaimedRewards()) {
            pageItems.add(new MenuItem() {
                @Override
                public void onClick(final Player p0, final InventoryClickType p1) {
                    menu.setBypassMenuCloseBehaviour(true);
                    menu.closeMenu(player);
                    dataPlayer.claimReward(unclaimedReward);
                    Utils.getInstance().playSound(p0, "TIER_REDEEM");
                }
                
                @Override
                public ItemStack getItemStack() {
                    return Utils.getInstance().loadItem("Tiers." + unclaimedReward.getName() + ".Items.Progress.Claim");
                }
            });
        }
        menu.setupPages(pageItems, Main.getInstance().getConfig().getIntegerList("Menus.Rewards.Settings.ScrollSlots"));
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
                return Utils.getInstance().loadItem("Menus.Rewards.Items.PreviousPage");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Rewards.Items.PreviousPage.Slot", 48));
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
                return Utils.getInstance().loadItem("Menus.Rewards.Items.Back");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Rewards.Items.Back.Slot", 49));
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
                return Utils.getInstance().loadItem("Menus.Rewards.Items.NextPage");
            }
        }, Main.getInstance().getConfig().getInt("Menus.Rewards.Items.NextPage.Slot", 50));
        for (int free = 0; free < menu.getInventory().getSize(); ++free) {
            if (menu.getInventory().getItem(free) == null) {
                if (menu.getPageSlots() == null || !menu.getPageSlots().contains(free)) {
                    final MenuItem.UnclickableMenuItem item = new MenuItem.UnclickableMenuItem() {
                        @Override
                        public void onClick(final Player p0, final InventoryClickType p1) {
                        }
                        
                        @Override
                        public ItemStack getItemStack() {
                            return Utils.getInstance().loadItem("Menus.Rewards.Items.Border");
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
