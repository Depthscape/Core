package net.depthscape.core.command.commands;

import net.depthscape.core.command.BaseCommand;
import net.depthscape.core.command.RankBaseCommand;
import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class StaffChatCommand extends RankBaseCommand {
    public StaffChatCommand() {
        super("staffchat", "Builder");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        user.toggleStaffChat();
        String message = user.isStaffChatSendEnabled() ? "&aYou are now in staff chat" : "&aYou are now in public chat";
        user.sendNotification(message);
        user.sendMessage(message);
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
