/*
 * User
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.user;

import lombok.Getter;
import lombok.Setter;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.menu.Menu;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class User extends OfflineUser {

    public User(UUID uniqueId) {
        super(uniqueId);
    }


    private UUID uuid;
    private String name;
    private Rank rank = RankManager.getDefaultRank();
    private int coins;

    private Player player;

    private Menu openMenu;

    public static User getUser(Player player) {
        return UserCache.getUser(player);
    }

    /*--- Nametag ---*/
    public void setNametag() {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), rank.getTabPrefix() + " ", rank.getWeight());
    }

    public void setNametag(boolean hidden) {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), rank.getTabPrefix() + " ", null, rank.getWeight(), false, hidden);
    }

    public void setNametag(String prefix, int weight) {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), prefix + " ", null, weight);
    }

    public void setNametag(String prefix, int weight, boolean hidden) {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), prefix + " ", null, weight, false, hidden);
    }

    public void sendNametags() {
        CorePlugin.getNametagManager().sendTeams(this.player);
    }

    public void openMenu(Menu menu) {
        menu.open(this.player);
        this.openMenu = menu;
    }

    public void sendMessage(String message) {
        this.player.sendMessage(ChatUtils.format(message));
    }
}
