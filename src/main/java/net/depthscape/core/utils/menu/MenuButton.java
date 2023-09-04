/*
 * MenuButton
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu;

import com.google.common.collect.Lists;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.menu.action.ButtonAction;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuButton extends MenuItem {

    private List<ButtonAction> actions;

    public MenuButton(ItemStack item, int slot, List<ButtonAction> actions) {
        super(item, slot);
        this.actions = actions;
    }

    public MenuButton(ItemStack item, int slot, ButtonAction action) {
        super(item, slot);
        this.actions = new ArrayList<>();
        actions.add(action);
    }

    public void executeActions(User user) {
        for (ButtonAction action : actions) {
            action.execute(user);
        }
    }
}
