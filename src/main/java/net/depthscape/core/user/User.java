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
import net.depthscape.core.tasks.NametagUpdateTask;
import net.depthscape.core.utils.*;
import net.depthscape.core.utils.hologram.Hologram;
import net.depthscape.core.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User extends OfflineUser {

    private Player player;

    private Menu openMenu;

    public User(UUID uniqueId) {
        super(uniqueId);
    }

    public User(OfflineUser user) {
        super(user.getUniqueId(), user.getName(), user.getRank(), user.getCoins(), user.isVanished(), user.getDiscordId());
    }

    /*--- Nametag ---*/

    private Hologram nametag;
    private NametagUpdateTask nametagUpdateTask;

    public void setNametag() {

        if (nametag != null) {
            Bukkit.getOnlinePlayers().stream()
                    //.filter(op -> !op.getUniqueId().equals(getUniqueId()))
                    .forEach(nametag::remove);
        }

        String prefix = ChatUtils.format(getRank().getTabPrefix() + " ");
        String fullNametag = prefix + this.player.getName();

        player.setPlayerListName(fullNametag);

        nametag = new Hologram("nametag-" + player.getUniqueId(), fullNametag, player.getLocation());
        Bukkit.getOnlinePlayers().stream()
                //.filter(op -> !op.getUniqueId().equals(getUniqueId()))
                .forEach(op -> {
                    nametag.spawn(op);
                    nametag.asPassenger(op, player);
                });


        //this.nametagUpdateTask = new NametagUpdateTask(this, nametag);
        //getNametagUpdateTask().start();

        new BukkitRunnable() {
            @Override
            public void run() {
                CorePlugin.getNametagManager().setNametag(player.getName(), prefix, null, getRank().getWeight(), false, false);
            }
        }.runTaskLater(CorePlugin.getInstance(), 1L);
    }

    public void setOldNametag() {

        String prefix = ChatUtils.format(getRank().getTabPrefix() + " ");
        new BukkitRunnable() {
            @Override
            public void run() {
                CorePlugin.getNametagManager().setNametag(player.getName(), prefix, getRank().getWeight());
            }
        }.runTaskLater(CorePlugin.getInstance(), 1L);
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

//        for (User user : Bukkit.getOnlinePlayers().stream().map(User::getUser).toArray(User[]::new)) {
//            if (user == this) continue;
//            user.getNametag().spawn(this.player);
//        }
//
    }

    /*--- Bossbar ---*/

    BossBar bossBar;

    public void setBossBar(String title) {


        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(title, BarColor.WHITE, BarStyle.SOLID);
        }
        bossBar.setTitle(ChatUtils.format(title));
        if (!bossBar.getPlayers().contains(this.player))
            bossBar.addPlayer(this.player);
    }

    public String getCoolBar(String text) {
        int padding = 3;

        int textWidth = DefaultFontInfo.getStringLength(text, false);
        List<BossBarCharacter> closestBossbarCenters = BossBarCharacter.getCenters(textWidth + padding * 2);
        // + padding * 2
        int centerWidth = closestBossbarCenters.stream().mapToInt(BossBarCharacter::getWidth).sum();
        // end + -1 + center + -1 + end + x + text
        //x = textWidth / 2 + centerWidth / 2

        int x = -(textWidth / 2 + centerWidth / 2) - 2;

        // (closestBossbarCenters.size() > 1 ? closestBossbarCenters.size() : 0) / 2)

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&#4e5c24").append(BossBarCharacter.BOSSBAR_END).append(UnicodeSpace.MINUS_1);
        for (BossBarCharacter customFontCharacter : closestBossbarCenters) {
            stringBuilder.append(customFontCharacter.getCharacter())
                    .append(UnicodeSpace.MINUS_1);

        }
        stringBuilder.append(BossBarCharacter.BOSSBAR_END)
                .append(UnicodeSpace.findBestCombination(x))
                .append("&r")
                .append(text);

        return stringBuilder.toString();
    }

    public void setCoolBar(String text) {
        setBossBar(getCoolBar(text));
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
    }


    public static User getUser(Player player) {
        return UserManager.getUser(player.getUniqueId());
    }
}
