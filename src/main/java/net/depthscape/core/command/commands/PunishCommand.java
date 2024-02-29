package net.depthscape.core.command.commands;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.command.RankBaseCommand;
import net.depthscape.core.menu.menus.PunishmentMenu;
import net.depthscape.core.punishment.PunishmentManager;
import net.depthscape.core.punishment.PunishmentReason;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.TabUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PunishCommand extends RankBaseCommand {
    public PunishCommand() {
        super("punish", "Helper");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        // /punish <player> [reason]
        if (args.length < 1) {
            user.sendMessage(getError("Usage: /punish <player> [reason]"));
            return;
        }
        OfflineUser.getOfflineUser(args[0], target -> {
            if (target == null) {
                user.sendMessage(getError("Player not found"));
                return;
            }

            if (args.length < 2) {
                Bukkit.getScheduler().runTask(CorePlugin.getInstance(), () -> {
                    PunishmentMenu menu = new PunishmentMenu(target, 1);
                    menu.open(user);
                });
                return;
            }

            PunishmentReason reason;
            try {
                reason = PunishmentReason.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                user.sendMessage(getError("Invalid punishment reason"));
                return;
            }

            PunishmentManager.punishPlayer(target, user, reason, punishment -> {
                user.sendMessage(ChatUtils.getInfoMessage("You have punished " + target.getName() + " for " + punishment.getReason().getReason()));
            });
        });
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        if (args.length == 2) {
            return TabUtil.complete(args[1], Arrays.stream(PunishmentReason.values()).map(Enum::name).toList());
        }
        return null;
    }
}
