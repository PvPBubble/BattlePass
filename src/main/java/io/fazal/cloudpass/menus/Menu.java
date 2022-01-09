// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.menus;

import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;
import io.fazal.cloudpass.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import io.fazal.cloudpass.utils.Utils;
import org.bukkit.entity.Player;
import com.google.common.collect.Maps;
import io.fazal.cloudpass.utils.pair.Pair;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.inventory.InventoryHolder;

public class Menu implements InventoryHolder
{
    private final ConcurrentMap<Integer, MenuItem> items;
    private final String title;
    private final int rows;
    private boolean exitOnClickOutside;
    private MenuAPI.MenuCloseBehaviour menuCloseBehaviour;
    private boolean bypassMenuCloseBehaviour;
    private Menu parentMenu;
    private Inventory inventory;
    private int currentPage;
    private int maxPage;
    private Map<Integer, List<Pair<Integer, MenuItem>>> pageItems;
    private List<Integer> pageSlots;
    
    public Menu(final String title, final int rows) {
        this(title, rows, null);
    }
    
    private Menu(final String title, final int rows, final Menu parentMenu) {
        this.items = Maps.newConcurrentMap();
        this.exitOnClickOutside = false;
        this.bypassMenuCloseBehaviour = false;
        this.title = title;
        this.rows = rows;
        this.parentMenu = parentMenu;
    }
    
    public MenuAPI.MenuCloseBehaviour getMenuCloseBehaviour() {
        return this.menuCloseBehaviour;
    }
    
    public void setMenuCloseBehaviour(final MenuAPI.MenuCloseBehaviour menuCloseBehaviour) {
        this.menuCloseBehaviour = menuCloseBehaviour;
    }
    
    public int getCurrentPage() {
        return this.currentPage;
    }
    
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }
    
    public int getMaxPage() {
        return this.maxPage;
    }
    
    public void setMaxPage(final int maxPage) {
        this.maxPage = maxPage;
    }
    
    public Map<Integer, List<Pair<Integer, MenuItem>>> getPageItems() {
        return this.pageItems;
    }
    
    public void setPageItems(final Map<Integer, List<Pair<Integer, MenuItem>>> pageItems) {
        this.pageItems = pageItems;
    }
    
    public List<Integer> getPageSlots() {
        return this.pageSlots;
    }
    
    public void setPageSlots(final List<Integer> pageSlots) {
        this.pageSlots = pageSlots;
    }
    
    public boolean nextPage(final Player player) {
        if (this.currentPage >= this.maxPage) {
            return false;
        }
        ++this.currentPage;
        this.clearPageSlots();
        Utils.getInstance().playSound(player, "PAGE_SWITCH");
        for (final Pair<Integer, MenuItem> pair : this.pageItems.get(this.currentPage)) {
            this.addMenuItem(pair.getValue(), pair.getKey());
        }
        this.updateMenu();
        return true;
    }
    
    public boolean previousPage(final Player player) {
        if (this.currentPage <= 1) {
            return false;
        }
        --this.currentPage;
        this.clearPageSlots();
        Utils.getInstance().playSound(player, "PAGE_SWITCH");
        for (final Pair<Integer, MenuItem> pair : this.pageItems.get(this.currentPage)) {
            this.addMenuItem(pair.getValue(), pair.getKey());
        }
        this.updateMenu();
        return true;
    }
    
    public void clearPageSlots() {
        for (final Integer slot : this.pageSlots) {
            this.removeMenuItem(slot);
        }
    }
    
    public void setupPages(final List<MenuItem> items, final List<Integer> pageSlots) {
        this.currentPage = 1;
        this.pageItems = new HashMap<Integer, List<Pair<Integer, MenuItem>>>();
        (this.pageSlots = new ArrayList<Integer>()).addAll(pageSlots);
        if (items.size() <= pageSlots.size()) {
            this.maxPage = 1;
            final List<Pair<Integer, MenuItem>> itemSlotList = new ArrayList<Pair<Integer, MenuItem>>();
            for (int i = 0; i < items.size(); ++i) {
                itemSlotList.add(new Pair<Integer, MenuItem>(pageSlots.get(i), items.get(i)));
            }
            this.pageItems.put(1, itemSlotList);
        }
        else {
            final int pagesWholeNumber = items.size() / pageSlots.size();
            if (pagesWholeNumber * pageSlots.size() == items.size()) {
                this.maxPage = pagesWholeNumber;
            }
            else {
                this.maxPage = pagesWholeNumber + 1;
            }
            int count = 0;
            int page = 1;
            this.pageItems.put(1, new ArrayList<Pair<Integer, MenuItem>>());
            for (final MenuItem item : items) {
                if (count == pageSlots.size()) {
                    count = 0;
                    ++page;
                    this.pageItems.put(page, new ArrayList<Pair<Integer, MenuItem>>());
                }
                this.pageItems.get(page).add(new Pair<Integer, MenuItem>(pageSlots.get(count), item));
                ++count;
            }
        }
        this.setupInitialPage();
    }
    
    private void setupInitialPage() {
        this.clearPageSlots();
        for (final Pair<Integer, MenuItem> pair : this.pageItems.get(this.currentPage)) {
            this.addMenuItem(pair.getValue(), pair.getKey());
        }
    }
    
    public void setBypassMenuCloseBehaviour(final boolean bypassMenuCloseBehaviour) {
        this.bypassMenuCloseBehaviour = bypassMenuCloseBehaviour;
    }
    
    public boolean bypassMenuCloseBehaviour() {
        return this.bypassMenuCloseBehaviour;
    }
    
    private void setExitOnClickOutside(final boolean exit) {
        this.exitOnClickOutside = exit;
    }
    
    public Map<Integer, MenuItem> getMenuItems() {
        return this.items;
    }
    
    public boolean addMenuItem(final MenuItem item, final int x, final int y) {
        return this.addMenuItem(item, y * 9 + x);
    }
    
    public MenuItem getMenuItem(final int index) {
        return this.items.get(index);
    }
    
    public boolean addMenuItem(final MenuItem item, final int index) {
        final ItemStack slot = this.getInventory().getItem(index);
        if (slot != null && slot.getType() != Material.AIR) {
            this.removeMenuItem(index);
        }
        item.setSlot(index);
        this.getInventory().setItem(index, item.getItemStack());
        this.items.put(index, item);
        item.addToMenu(this);
        return true;
    }
    
    public boolean removeMenuItem(final int x, final int y) {
        return this.removeMenuItem(y * 9 + x);
    }
    
    private boolean removeMenuItem(final int index) {
        final ItemStack slot = this.getInventory().getItem(index);
        if (slot == null || slot.getType().equals((Object)Material.AIR)) {
            return false;
        }
        this.getInventory().clear(index);
        this.items.remove(index).removeFromMenu(this);
        return true;
    }
    
    void selectMenuItem(final Player player, final int index, final InventoryClickType clickType) {
        if (this.items.containsKey(index)) {
            final MenuItem item = this.items.get(index);
            item.onClick(player, clickType);
        }
    }
    
    public void openMenu(final Player player) {
        if (!this.getInventory().getViewers().contains(player)) {
            player.openInventory(this.getInventory());
        }
    }
    
    public void closeMenu(final Player player) {
        if (this.getInventory().getViewers().contains(player)) {
            this.getInventory().getViewers().remove(player);
            player.closeInventory();
        }
    }
    
    public void scheduleUpdateTask(final Player player, final int ticks) {
        new BukkitRunnable() {
            public void run() {
                if (player == null || Bukkit.getPlayer(player.getName()) == null) {
                    this.cancel();
                    return;
                }
                if (player.getOpenInventory() == null || player.getOpenInventory().getTopInventory() == null || player.getOpenInventory().getTopInventory().getHolder() == null) {
                    this.cancel();
                    return;
                }
                if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof Menu)) {
                    this.cancel();
                    return;
                }
                final Menu menu = (Menu)player.getOpenInventory().getTopInventory().getHolder();
                if (!menu.inventory.equals(Menu.this.inventory)) {
                    this.cancel();
                    return;
                }
                for (final Map.Entry<Integer, MenuItem> entry : menu.items.entrySet()) {
                    Menu.this.getInventory().setItem((int)entry.getKey(), entry.getValue().getItemStack());
                }
            }
        }.runTaskTimer((Plugin)Main.getInstance(), (long)ticks, (long)ticks);
    }
    
    public Menu getParent() {
        return this.parentMenu;
    }
    
    public void setParent(final Menu menu) {
        this.parentMenu = menu;
    }
    
    public Inventory getInventory() {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory((InventoryHolder)this, this.rows * 9, this.title);
        }
        return this.inventory;
    }
    
    public boolean exitOnClickOutside() {
        return this.exitOnClickOutside;
    }
    
    @Override
    protected Menu clone() throws CloneNotSupportedException {
        final Menu menu = (Menu)super.clone();
        final Menu clone = new Menu(this.title, this.rows);
        clone.setExitOnClickOutside(this.exitOnClickOutside);
        clone.setMenuCloseBehaviour(this.menuCloseBehaviour);
        for (final Map.Entry<Integer, MenuItem> entry : this.items.entrySet()) {
            clone.addMenuItem(entry.getValue(), entry.getKey());
        }
        return clone;
    }
    
    public void updateMenu() {
        for (final HumanEntity entity : this.getInventory().getViewers()) {
            ((Player)entity).updateInventory();
        }
    }
    
    @Override
    public String toString() {
        return "Menu{title=" + this.title + ", rows=" + this.rows + ", exitOnClickOutside=" + this.exitOnClickOutside + ", bypassMenuCloseBehavior=" + this.bypassMenuCloseBehaviour + ", parentMenu=" + this.parentMenu.toString() + ", inventory=" + this.inventory.toString() + ", currentPage=" + this.currentPage + ", maxPage=" + this.maxPage + ", pageItems=" + this.pageItems + "pageSlots=" + this.pageSlots + "}";
    }
}
