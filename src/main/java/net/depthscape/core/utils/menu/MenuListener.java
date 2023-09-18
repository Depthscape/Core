/*
 * MenuListener
 * Core
 *
 * Created by leobaehre on 9/9/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu;

import net.depthscape.core.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Menu menu = checkOpenMenu(event.getWhoClicked(), event.getView());
        if (menu != null) {
            User user = User.getUser(event.getWhoClicked() instanceof Player ? (Player) event.getWhoClicked() : null);
            if (user == null) return;
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            int slot = event.getSlot();
            if (menu.getItem(slot) != null) {
                if (menu.getItem(slot) instanceof MenuButton button) {
                    button.executeActions(user);
                }
            }
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        Menu menu = checkOpenMenu(event.getPlayer(), event.getView());
        if (menu != null) {
            User user = User.getUser(event.getPlayer() instanceof Player ? (Player) event.getPlayer(): null);
            if (user != null) {
                user.setOpenMenu(null);
            }
        }
    }

    private Menu checkOpenMenu(HumanEntity humanEntity, InventoryView inventoryView) {
        User user = User.getUser(humanEntity instanceof Player ? (Player) humanEntity : null);
        if (user == null) return null;
        if (user.getOpenMenu() != null) {
            Menu menu = user.getOpenMenu();
            if (menu.getTitle().equals(inventoryView.getTitle())) {
                return menu;
            }
        }
        return null;
    }
}
