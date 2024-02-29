package net.depthscape.core.menu;

import net.depthscape.core.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/***
 * Listener for all events that is used for the menus
 */
public class MenuListener implements Listener {

    public final static List<User> transferringUsers = new ArrayList<>();

    /** Handles the click event for a menu */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        User user = User.getUser((Player) event.getWhoClicked());
        if (user == null) return;

        Menu menu = checkMenu(event, user);
        if (menu == null) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        int slot = event.getSlot();

        MenuItem menuItem = menu.getItemAt(slot);
        if (menuItem == null) return;

        menuItem.onClick(user, menu, event);
    }


    /** Removes the open menu from the user when an inventory is closed */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        User user = User.getUser(player);
        if (user == null) return;
        if (transferringUsers.contains(user)) return;
        user.setOpenMenu(null);
    }

    /***
     * Checks if the inventory in a inventoryEvent is a menu
     * @param event given from the event method
     * @param user the user that has the menu open
     * @return the menu if the inventory is a menu, otherwise null
     */
    private Menu checkMenu(InventoryEvent event, User user) {
        InventoryView view = event.getView();

        Menu menu = user.getOpenMenu();
        if (menu == null) return null;
        if (!menu.getTitle().equals(view.getTitle())) return null;

        return menu;
    }
}
