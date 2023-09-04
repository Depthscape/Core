/*
 * PagedMenu
 * Core
 *
 * Created by leobaehre on 9/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu;

import net.depthscape.core.user.User;
import net.depthscape.core.utils.ItemCreator;
import net.depthscape.core.utils.menu.action.ButtonAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedMenu<T> extends Menu {

    private static final ItemStack nextItem = new ItemCreator(Material.RABBIT_FOOT).setCustomModelData(1).setDisplayName("&aNext Page").create();
    private static final int nextSlot = 53;

    private static final ItemStack previousItem = new ItemCreator(Material.RABBIT_FOOT).setCustomModelData(1).setDisplayName("&cPrevious Page").create();
    private static final int previousSlot = 45;

    private static final String canOnlyNextTitle = "\\u+00A7f\\u+F807\\u+F801\\u+5C0F\\u+F80C\\u+F80A\\u+F809\\u+4E0A";
    private static final String canOnlyPreviousTitle = "\\u+00A7f\\u+F807\\u+F801\\u+53E3\\u+F80C\\u+F80A\\u+F809\\u+4E0A";
    private static final String canBothTitle = "\\u+00A7f\\u+F807\\u+F801\\u+5C0F\\u+F80C\\u+F80A\\u+F809\\u+4E0A";
    private static final String canNeitherTitle = "\\u+00A7f\\u+F807\\u+F801\\u+5DFE\\u+F80C\\u+F80A\\u+F809\\u+4E0A";

    private final List<Page> pages;
    private int currentPageIndex;

    public PagedMenu(List<T> items) {
        super("", 6);
        pages = new ArrayList<>();
        currentPageIndex = 0;

        int slot = 0;
        Page p = new Page();
        for (T item : items) {
            if (slot == 45) {
                slot = 0;
                pages.add(p);
                p = new Page();
            }
            p.addItem(toItemStack(item), slot);
            slot++;
        }
        pages.add(p);

        openPage(currentPageIndex);
    }


    private void openPage(int pageIndex) {
        Page page = pages.get(pageIndex);
        currentPageIndex = pageIndex;
        clear();

        for (MenuItem item : page.getItems()) {
            addItem(item.getItemStack(), item.getSlot());
        }

        boolean canNext = false;
        boolean canPrevious = false;

        try {
            if (pages.get(currentPageIndex + 1) != null) {
                canNext = true;
            }
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            if (pages.get(currentPageIndex - 1) != null) {
                canPrevious = true;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        String title;
        if (canNext && canPrevious) {
            title = canBothTitle;
        } else if (canNext) {
            title = canOnlyNextTitle;
        } else if (canPrevious) {
            title = canOnlyPreviousTitle;
        } else {
            title = canNeitherTitle;
        }
        setTitle(title);

        if (pages.size()  > 1) {
            if (canNext) {
                addButton(nextItem, nextSlot, new ButtonAction() {
                    @Override
                    public void execute(User user) {
                        openPage(currentPageIndex + 1);
                    }
                });
            }
            if (canPrevious) {
                addButton(previousItem, previousSlot, new ButtonAction() {
                    @Override
                    public void execute(User user) {
                        openPage(currentPageIndex - 1);
                    }
                });
            }
        }
    }

    @Override
    public void open(Player player) {
        if (!pages.isEmpty()) {
            openPage(currentPageIndex);
        }
        super.open(player);
    }

    abstract protected ItemStack toItemStack(T item);
}