package net.depthscape.core.menu;

import lombok.Getter;
import lombok.Setter;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.ItemCreator;
import net.depthscape.core.utils.UnicodeSpace;
import net.depthscape.core.utils.UnicodeTranslator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Menu class basics for all menus
 * stores the title, rows, items
 */
@Getter
@Setter
public abstract class Menu {


    private final static int MENU_WIDTH = 176;
    private final static int TEXT_BOX_WIDTH = 129;
    private final static int TEXT_BOX_OFFSET_RIGHT = 23;

    private final static Sound CLICK_SOUND = Sound.UI_BUTTON_CLICK;

    private String title;
    private int size;

    private final Map<Integer, MenuItem> items;

    public Menu(String title, int rows) {
        this.title = ChatUtils.format(title);
        this.size = rows * 9;

        this.items = new HashMap<>();
    }

    /**
     * Adds an item to the menu. Used to set items in the parent class
     * @param slot the slot to add the item to
     * @return the menu
     */
    public abstract MenuItem getItemAt(int slot);

    public void open(User user) {
        user.getPlayer().playSound(user.getPlayer().getLocation(), CLICK_SOUND, 1, 1);
        openWithoutSound(user);
    }

    /**
     * Opens the menu for the user
     * @param user the user to open the menu for
     */
    public void openWithoutSound(User user) {

        Inventory inventory = Bukkit.createInventory(null, size, title);

        for (int i = 0; i < size; i++) {
            MenuItem item = getItemAt(i);
            if (item != null) {
                inventory.setItem(i, item.getItemStack());
            }
        }

        if (user.getOpenMenu() != null) {
            MenuListener.transferringUsers.add(user);
        }

        user.setOpenMenu(this);
        user.getPlayer().openInventory(inventory);
        MenuListener.transferringUsers.remove(user);
    }

    /**
     * Get an empty item for a menu
     * @param displayName the display name of the item
    *  @param lore the lore of the item
     */
    protected ItemStack getEmptyItem(String displayName, List<String> lore) {
        return new ItemCreator(Material.RABBIT_FOOT)
                .setDisplayName("&f" + displayName)
                .setLore(lore)
                .setCustomModelData(1)
                .create();
    }

    /**
     * Gets the title of the menu
     * @param name the name of the player
     * @return the title of the menu
     */
    protected String getTitleName(String menuCode, String name, int wordLength) {

        StringBuilder builder = new StringBuilder();

        builder.append(UnicodeSpace.find(-9)).append("&f").append(menuCode);

        String translatedSuffix = UnicodeTranslator.translateToUnicode(name);

        int backSpaces = -(TEXT_BOX_WIDTH + TEXT_BOX_OFFSET_RIGHT + wordLength); // get the amount of spaces to remove to go to the left of the text box
        int spaces = wordLength + (TEXT_BOX_WIDTH - wordLength) / 2; // get the amount of spaces forward to center the word in the text box

        int targetSpaces = backSpaces + spaces; // go backspaces back and go spaces forward

        builder.append(UnicodeSpace.find(targetSpaces)).append("&#2B2B2B").append(translatedSuffix);

        return builder.toString();
    }
}
