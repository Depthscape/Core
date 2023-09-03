/*
 * GiveItemAction
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu.action.actions;

import lombok.AllArgsConstructor;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.menu.action.ButtonAction;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class GiveItemAction extends ButtonAction {

    private ItemStack item;


    @Override
    public void execute(User user) {
        user.getPlayer().getInventory().addItem(item);
    }
}
