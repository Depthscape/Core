package net.depthscape.core.menu.menus;

import net.depthscape.core.menu.Menu;
import net.depthscape.core.menu.MenuItem;
import net.depthscape.core.model.Callback;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.UnicodeSpace;
import net.depthscape.core.utils.UnicodeTranslator;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConfirmationMenu extends Menu {


    Callback<Boolean> callback;


    public ConfirmationMenu(String title, int wordLength, Callback<Boolean> callback) {
        super("", 2);

        setTitle(getTitleName("\u8F66", title, wordLength));
//        setTitle(ChatUtils.format(UnicodeSpace.find(-9) + "&f" + "\u8F66" + getTitleName(title, wordLength)));
        this.callback = callback;
    }

    @Override
    public MenuItem getItemAt(int slot) {
        switch (slot) {
            case 9, 10, 11, 12 -> { // button 1
                return new MenuItem() {
                    @Override
                    public ItemStack getItemStack() {
                        return getEmptyItem("&cCancel", List.of("Click to cancel"));
                    }

                    @Override
                    public void onClick(User user, Menu menu, InventoryClickEvent event) {
                        if (event.getClick() != ClickType.LEFT) return;
                        callback.call(false);
                    }
                };

            }
            case 14, 15, 16, 17 -> { // button 2
                return new MenuItem() {
                    @Override
                    public ItemStack getItemStack() {
                        return getEmptyItem("&aConfirm", List.of("Click to confirm"));
                    }

                    @Override
                    public void onClick(User user, Menu menu, InventoryClickEvent event) {
                        if (event.getClick() != ClickType.LEFT) return;
                        callback.call(true);
                    }
                };
            }
        }
        return null;
    }
}
