/*
 * Page
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

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Page {

    List<MenuItem> items;
    String title;

    public Page() {
        this.items = new ArrayList<>();
        this.title = "";
    }

    public void addItem(ItemStack itemStack, int slot) {
        items.add(new MenuItem(itemStack, slot));
    }
}
