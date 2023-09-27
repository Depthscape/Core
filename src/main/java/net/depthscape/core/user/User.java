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

    public User(OfflineUser user) {
        super(user.getUniqueId(), user.getName(), user.getRank(), user.getCoins(), user.isVanished());
    }

    private Player player;

    private Menu openMenu;

    public static User getUser(Player player) {
        return UserManager.getUser(player.getUniqueId());
    }

    /*--- Nametag ---*/
    public void setNametag() {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), getRank().getTabPrefix() + " ", getRank().getWeight());
    }

    public void setNametag(boolean hidden) {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), getRank().getTabPrefix() + " ", null, getRank().getWeight(), false, hidden);
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

    public void vanish() {

    }

    public void unvanish() {

    }

    public void openMenu(Menu menu) {
        menu.open(this.player);
        this.openMenu = menu;
    }

    public void sendMessage(String message) {
        this.player.sendMessage(ChatUtils.format(message));
    }

    public void nick(String nick) {

//        Disguise disguise = Disguise.builder()
//                .setName(nick, false).build();
//
//        DisguiseManager.getProvider().disguise(this.player, disguise);

    }
}
