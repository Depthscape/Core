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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Rank {

    private String name, chatPrefix, tabPrefix;
    @Nullable String vanishPrefix;
    private int weight;
    private long discordRoleId;
    private List<String> permissions;
    private Rank inheritance = null;
    private boolean isStaff;

    public Rank(final String name, String chatPrefix, String tabPrefix, String vanishPrefix, final int weight, boolean isStaff, long discordRoleId) {
        this.name = ChatUtils.format(name);
        this.chatPrefix = ChatUtils.format(chatPrefix);
        this.tabPrefix = ChatUtils.format(tabPrefix);
        this.vanishPrefix = ChatUtils.format(vanishPrefix);
        this.weight = weight;
        this.permissions = new ArrayList<>();
        this.isStaff = isStaff;
        this.discordRoleId = discordRoleId;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    public static Rank getRank(String name) {
        return RankManager.getRank(name);
    }
}