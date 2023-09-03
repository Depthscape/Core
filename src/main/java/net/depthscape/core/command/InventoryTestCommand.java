/*
 * InventoryTestCommand
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.command;

import net.depthscape.core.user.User;
import net.depthscape.core.utils.menu.Menu;
import net.depthscape.core.utils.menu.MenuButton;
import net.depthscape.core.utils.menu.MenuItem;
import net.depthscape.core.utils.menu.action.actions.GiveItemAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryTestCommand extends BaseCommand {
    public InventoryTestCommand() {
        super("openinv", null);
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        Menu menu = new Menu("Test", 3);
        menu.addItem(new MenuItem(new ItemStack(Material.GRASS), 0));
        menu.addItem(new MenuButton(new ItemStack(Material.DIAMOND), 1, List.of(new GiveItemAction(new ItemStack(Material.DIAMOND)))));
        user.openMenu(menu);
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
