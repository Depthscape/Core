/*
 * ReloadCommand
 * Core
 *
 * Created by leobaehre on 8/31/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.command;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends BaseCommand{
    public ReloadCommand() {
        super("corereload", "Admin");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        CorePlugin plugin = CorePlugin.getInstance();
        plugin.getMainConfig().load();
        RankManager.loadRanks();
        user.sendMessage("&aReloaded config.");
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
