package net.depthscape.core.command;

import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class NickCommand extends BaseCommand {
    public NickCommand() {
        super("nick", "Admin");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        user.nick(ChatUtils.format(args[0]));
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
