package net.depthscape.core.menu;

import net.depthscape.core.user.User;
import net.depthscape.core.utils.CustomFontCharacter;
import net.depthscape.core.utils.ItemCreator;
import net.depthscape.core.utils.UnicodeSpace;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for a paged menu. This menu is scrollable and can contain a large amount of items
 * @param <T> the type of the items in the menu
 */
public abstract class PagedMenu<T> {

    private static final int ITEMS_PER_PAGE = 9 * 5; // 5 rows

    private final List<MenuItem> items;

    private final int maxPage;
    private int page;

    String title;

    private final MenuItem leftButton;
    private final MenuItem rightButton;

    /**
     * Constructor for a paged menu
     * @param list the list of objects to display in the menu
     * @param title the title of the menu
     */
    public PagedMenu(List<T> list, String title) {
        this.items = new ArrayList<>();
        list.stream().map(this::getItem).forEach(items::add);

        this.maxPage = (items.size() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE - 1;
        this.page = 0;
        this.title = title;

        this.leftButton = new MenuItem() {

            @Override
            public ItemStack getItemStack() {
                return new ItemCreator(Material.RABBIT_FOOT)
                        .setDisplayName("&fPrevious Page")
                        .setCustomModelData(1)
                        .create();
            }

            @Override
            public void onClick(User user, Menu menu, InventoryClickEvent event) {
                if (event.getClick() != ClickType.LEFT) return;
                if (hasPrevious()) {
                    page--;
                    open(user);
                }
            }
        };

        this.rightButton = new MenuItem() {

            @Override
            public ItemStack getItemStack() {
                return new ItemCreator(Material.RABBIT_FOOT)
                        .setDisplayName("&fNext Page")
                        .setCustomModelData(1)
                        .create();
            }

            @Override
            public void onClick(User user, Menu menu, InventoryClickEvent event) {
                if (event.getClick() != ClickType.LEFT) return;
                if (hasNext()) {
                    page++;
                    open(user);
                }
            }
        };
    }

    /**
     * Translates the object of the list to a MenuItem
     * @param t the object of the list
     * @return the MenuItem
     */
    public abstract MenuItem getItem(T t);

    /**
     * Opens the menu for the user
     * @param user the user to open the menu for
     */
    public void open(User user) {
        int start = page * ITEMS_PER_PAGE; // begin index of the page

        Menu page = new Menu(getTitle(title), 6) {
            @Override
            public MenuItem getItemAt(int slot) {
                if (slot < ITEMS_PER_PAGE) { // 0 - 44 should be filled with items
                    return items.get(start + slot);
                }
                if (hasPrevious()) {
                    if (slot == 45) { // left button
                        return leftButton;
                    }
                }
                if (hasNext()) {
                    if (slot == 53) { // right button
                        return rightButton;
                    }
                }
                return null;
            }
        };

        page.open(user);
    }

    /**
     * Gets the title for the page
     * @param titleText the title of the menu that is on top
     * @return the complete title of the menu
     */
    private String getTitle(String titleText) {

        String title = UnicodeSpace.find(-9) + "&f";

        if (hasPrevious() && hasNext()) {
            title = title + CustomFontCharacter.SCROLLABLE_MIDDLE;
        }else if (hasPrevious() && !hasNext()) {
            title = title + CustomFontCharacter.SCROLLABLE_RIGHT;
        } else if (hasNext() && !hasPrevious()) {
            title = title + CustomFontCharacter.SCROLLABLE_LEFT;
        } else {
            title = title + CustomFontCharacter.SCROLLABLE_LOCKED;
        }

        return title + UnicodeSpace.find(-170) + "&f" + titleText + " (" + (page + 1) + "/" + (maxPage + 1) + ")";
    }

    /**
     * Checks if the menu has a previous page
     * @return true if the menu has a previous page
     */
    private boolean hasPrevious() {
        return page > 0;
    }

    /**
     * Checks if the menu has a next page
     * @return true if the menu has a next page
     */
    private boolean hasNext() {
        return page < maxPage - 1;
    }
}
