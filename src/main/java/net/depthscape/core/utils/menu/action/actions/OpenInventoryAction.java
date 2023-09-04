/*
 * OpenInventoryAction
 * Core
 *
 * Created by leobaehre on 9/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu.action.actions;

import net.depthscape.core.user.User;
import net.depthscape.core.utils.menu.action.ButtonAction;
import org.bukkit.inventory.Inventory;

public class OpenInventoryAction extends ButtonAction {

    Inventory inventory;
    @Override
    public void execute(User user) {
        user.getPlayer().openInventory(inventory);
    }
}
