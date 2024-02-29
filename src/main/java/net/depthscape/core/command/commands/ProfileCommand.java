package net.depthscape.core.command.commands;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.command.BaseCommand;
import net.depthscape.core.menu.menus.ProfileMenu;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.TabUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ProfileCommand extends BaseCommand {
    public ProfileCommand() {
        super("profile");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        if (args.length == 0) {
            new ProfileMenu(user, user).open(user);
            user.sendMessage(ChatUtils.getInfoMessage("You have opened your profile"));
        } else {
            OfflineUser.getOfflineUser(args[0], offlineUser -> {
                if (offlineUser == null) {
                    user.sendMessage(getError("Player not found"));
                    return;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new ProfileMenu(offlineUser, user).open(user);
                        user.sendMessage(ChatUtils.getInfoMessage("You have opened the profile of " + offlineUser.getName()));
                    }
                }.runTask(CorePlugin.getInstance());
            });
        }

    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        if (args.length == 1) {
            return TabUtil.complete(args[0], Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        }
        return null;
    }
}
