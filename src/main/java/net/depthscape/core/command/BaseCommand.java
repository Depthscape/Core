/*
 * BaseCommand
 * Core
 *
 * Created by leobaehre on 8/31/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */
package net.depthscape.core.command;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.user.User;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {

    private final String label;
    private final Rank minimumRank;


    public BaseCommand(String label, String minimumRank) {
        this.label = label;
        if (minimumRank != null) {
            this.minimumRank = Rank.getRank(minimumRank);
            register();
            return;
        }
        this.minimumRank = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.getUser(player);
            if (minimumRank != null) {
                if (user.getRank().getWeight() > minimumRank.getWeight()) {
                    user.sendMessage("&cYou do not have permission to use this command.");
                    return true;
                }
            }
            onCommand(player, user, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.getUser(player);
            if (minimumRank != null) {
                if (user.getRank().getWeight() > minimumRank.getWeight()) {
                    return null;
                }
            }
            return onTabComplete(player, user, args);
        }
        return null;
    }

    protected abstract void onCommand(Player player, User user, String[] args);
    protected abstract List<String> onTabComplete(Player player, User user, String[] args);

    public void register() {
        PluginCommand command = CorePlugin.getInstance().getCommand(label);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
