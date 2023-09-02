package net.depthscape.core.model;

import lombok.Getter;
import lombok.Setter;
import net.depthscape.core.utils.ChatUtils;

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
        this.name = ChatUtils.colorize(name);
        this.chatPrefix = ChatUtils.colorize(chatPrefix);
        this.tabPrefix = ChatUtils.colorize(tabPrefix);
        this.weight = weight;
        this.permissions = new ArrayList<>();
        this.isStaff = isStaff;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }
}