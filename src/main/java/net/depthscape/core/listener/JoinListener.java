/*
 * JoinListener
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.listener;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.user.User;
import net.depthscape.core.user.UserCache;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        User user = User.getUser(player);

        user.setPlayer(player);
        user.setName(player.getName());
        UserCache.updatePlayerInfo(user);

        player.setPlayerListHeaderFooter(
                ChatUtils.format(CorePlugin.getInstance().getMainConfig().getTablist().getHeader()),
                ChatUtils.format(CorePlugin.getInstance().getMainConfig().getTablist().getFooter()));

        user.sendNametags();
        new BukkitRunnable() {
            @Override
            public void run() {
                user.setNametag();
            }
        }.runTaskLater(CorePlugin.getInstance(), 1);
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        User user = UserCache.checkOrCreateUserSync(event.getUniqueId());
        UserCache.refreshUserSync(user);
        List<String> whitelistedRanks = CorePlugin.getInstance().getMainConfig().getWhitelist().getWhitelistedRanks();
        if (!whitelistedRanks.isEmpty()) {
            if (!whitelistedRanks.contains(user.getRank().getName())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, ChatUtils.format(CorePlugin.getInstance().getMainConfig().getWhitelist().getMessage()));
            }
        }
    }
}
