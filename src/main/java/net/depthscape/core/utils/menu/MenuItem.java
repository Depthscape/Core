/*
 * MenuItem
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;


@Getter
@Setter
@AllArgsConstructor
public class MenuItem {

    ItemStack itemStack;
    int slot;
}
