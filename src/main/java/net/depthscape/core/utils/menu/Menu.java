/*
 * Menu
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu;

import lombok.Getter;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Menu {

    private String title;
    private int rows;

    private List<MenuItem> items;

    public Menu(String title, int rows) {
        this.title = ChatUtils.format(title);
        this.rows = rows;
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        this.items.add(item);
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
        Inventory inventory = player.getServer().createInventory(null, rows * 9, title);
        for (MenuItem item : items) {
            inventory.setItem(item.getSlot(), item.getItemStack());
        }
        player.openInventory(inventory);
    }


}
