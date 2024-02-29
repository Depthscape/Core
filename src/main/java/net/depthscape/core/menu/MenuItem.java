package net.depthscape.core.menu;

import lombok.Getter;
import net.depthscape.core.user.User;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Class for a menu item
 * Override the onClick method to handle a click
 */
@Getter
public abstract class MenuItem {

    ItemStack itemStack;

    public abstract ItemStack getItemStack();

    public void onClick(User user, Menu menu, InventoryClickEvent event) {}


    /**
     * Creates a menu item from an itemstack
     * @param itemStack0 the itemstack to create the menu item from
     * @return the menu item
     */
    public static MenuItem of(ItemStack itemStack0) {
        return new MenuItem() {
            @Override
            public ItemStack getItemStack() {
                return itemStack0;
            }
        };
    }
}
