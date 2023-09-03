/*
 * Rank
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.rank;

import lombok.Getter;
import lombok.Setter;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Rank {

    private String name, chatPrefix, tabPrefix;
    private int weight;
    private List<String> permissions;
    private Rank inheritance = null;
    private boolean isStaff;

    public Rank(final String name, String chatPrefix, String tabPrefix, final int weight, boolean isStaff) {
        this.name = ChatUtils.format(name);
        this.chatPrefix = ChatUtils.format(chatPrefix);
        Bukkit.getLogger().info("Chat prefix: " + this.chatPrefix);
        this.tabPrefix = ChatUtils.format(tabPrefix);
        this.weight = weight;
        this.permissions = new ArrayList<>();
        this.isStaff = isStaff;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    public static Rank getRank(String name) {
        return RankManager.getRank(name);
    }
}