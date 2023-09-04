/*
 * InventoryTestCommand
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.command;

import net.depthscape.core.user.User;
import net.depthscape.core.utils.menu.*;
import net.depthscape.core.utils.menu.action.actions.GiveItemAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InventoryTestCommand extends BaseCommand {
    public InventoryTestCommand() {
        super("openinv", null);
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add(new ItemStack(Material.values()[new Random().nextInt(Material.values().length)]));
        }
        PagedMenu<ItemStack> menu = new PagedMenu<>(items) {
            @Override
            public ItemStack toItemStack(ItemStack item) {
                return item;
            }
        };

        user.openMenu(menu);

    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
