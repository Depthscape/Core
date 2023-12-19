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
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.depthscape.core.tasks.NametagUpdateTask;
import net.depthscape.core.utils.BossBarCharacter;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.DefaultFontInfo;
import net.depthscape.core.utils.UnicodeSpace;
import net.depthscape.core.utils.hologram.Hologram;
import net.depthscape.core.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

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

        String prefix = ChatUtils.format(getRank().getTabPrefix() + " ");
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(getUniqueId());
        if (tabPlayer == null) {
            return;
        }
        TabAPI.getInstance().getNameTagManager().setPrefix(tabPlayer, prefix);
        TabAPI.getInstance().getTabListFormatManager().setPrefix(tabPlayer, prefix);

    }

    public void sendNametags() {
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
