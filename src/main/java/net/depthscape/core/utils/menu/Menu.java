/*
 * Menu
 * Core
 *
 * Created by leobaehre on 9/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu;

import lombok.Getter;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.menu.action.ButtonAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class Menu {

    private String title;
    private int rows;

    private List<MenuItem> items;

    private Inventory inventory;

    public Menu(String title, int rows) {
        this.title = ChatUtils.format(title);
        this.rows = rows;
        this.items = new ArrayList<>();
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
    }

    public void addItem(ItemStack item, int slot) {
        this.items.add(new MenuItem(item, slot));
        inventory.setItem(slot, item);
    }

    public void addButton(ItemStack item, int slot, ButtonAction... actions) {
        this.items.add(new MenuButton(item, slot, Arrays.asList(actions)));
        inventory.setItem(slot, item);
    }

    public void addButton(MenuButton item) {
        this.items.add(item);
        inventory.setItem(item.getSlot(), item.getItemStack());
    }

    public void clear() {
        this.items.clear();
        this.inventory.clear();
    }

    public MenuItem getItem(int slot) {
        for (MenuItem item : items) {
            if (item.getSlot() == slot) {
                return item;
            }
        }
        return null;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    private List<Player> getViewers() {
        return inventory.getViewers().stream().map(viewer -> (Player) viewer).toList();
    }

    public void setTitle(String title) {
        this.title = ChatUtils.format(title);
        getViewers().forEach(player -> {
            InventoryView open = player.getOpenInventory();
            open.setTitle(this.title);
            Bukkit.getLogger().info("Set title to " + this.title + " for " + player.getName());
        });
    }
}