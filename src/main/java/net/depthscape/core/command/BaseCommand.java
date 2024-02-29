/*
 * BaseCommand
 * Core
 *
 * Created by leobaehre on 9/9/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */
package net.depthscape.core.command;

import lombok.Getter;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public abstract class BaseCommand implements CommandExecutor, TabCompleter {

    private final String label;

    public BaseCommand(String label) {
        this.label = label;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.getUser(player);

            onCommand(player, user, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.getUser(player);
            return onTabComplete(player, user, args);
        }
        return null;
    }

    protected abstract void onCommand(Player player, User user, String[] args);
    protected abstract List<String> onTabComplete(Player player, User user, String[] args);

    public void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(label);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    protected String getError(String error) {
        return ChatUtils.getWarningMessage(error);
    }

    protected static String joinRemainingArgs(final int startIndex, final String[] array) {
        final StringBuilder joined = new StringBuilder();

        for (int i = startIndex; i < array.length; i++)
            joined.append((joined.isEmpty()) ? "" : " ").append(ChatUtils.format(array[i]));

        return joined.toString();
    }

}
