// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.menus;

import io.fazal.cloudpass.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class MenuItem {
	private Menu menu;
	private int slot;
	private ItemStack itemStack;
	private boolean clickable;

	public MenuItem() {
		this.clickable = true;
	}

	void addToMenu(final Menu menu) {
		this.menu = menu;
	}

	public abstract void onClick(final Player p0, final InventoryClickType p1);

	void removeFromMenu(final Menu menu) {
		if (this.menu == menu) {
			this.menu = null;
		}
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public void setItemStack(final ItemStack item, final boolean update) {
		this.itemStack = item;
		if (update && this.getMenu() != null) {
			this.getMenu().addMenuItem(this, this.getSlot());
			this.getMenu().updateMenu();
		}
	}

	private Menu getMenu() {
		return this.menu;
	}

	private int getSlot() {
		return this.slot;
	}

	public void setSlot(final int slot) {
		this.slot = slot;
	}

	public boolean isClickable() {
		return this.clickable;
	}

	private void setClickable(final boolean clickable) {
		this.clickable = clickable;
	}

	public void setTemporaryIcon(final ItemStack item, final long time) {
		final ItemStack currentItemStack = this.getItemStack();
		this.menu.getInventory().setItem(this.getSlot(), item);
		this.menu.updateMenu();
		this.setClickable(false);
		final ItemStack itemStack = item;
		Bukkit.getScheduler().runTaskLater((Plugin) Main.getInstance(), () -> {
			this.setClickable(true);
			this.menu.getInventory().setItem(this.getSlot(), itemStack);
			this.menu.updateMenu();
		}, time);
	}

	@Override
	public String toString() {
		return "MenuItem{menu=" + this.menu.toString() + ", item=" + this.getItemStack().toString() + ", slot=" + this.slot + ", clickable=" + this.clickable + "}";
	}

	public abstract static class UnclickableMenuItem extends MenuItem {
		@Override
		public void onClick(final Player player, final InventoryClickType clickType) {
		}
	}
}
