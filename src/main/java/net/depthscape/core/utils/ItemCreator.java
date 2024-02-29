/*
 * ItemCreator
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemCreator {

    ItemStack itemStack;
    ItemMeta meta;

    public ItemCreator(Material material) {
        this.itemStack = new ItemStack(material);
        this.meta = this.itemStack.getItemMeta();
    }

    public ItemCreator setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemCreator setDisplayName(String displayName) {
        this.meta.setDisplayName(ChatUtils.format(displayName));
        return this;
    }

    public ItemCreator setLore(List<String> lore) {
        List<String> formattedLore = new ArrayList<>();
        lore.forEach(line -> formattedLore.add(ChatUtils.format("&7" + line)));
        this.meta.setLore(formattedLore);
        return this;
    }

    public ItemCreator setCustomModelData(int modelData) {
        this.meta.setCustomModelData(modelData);
        return this;
    }

    public ItemStack create() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }
}