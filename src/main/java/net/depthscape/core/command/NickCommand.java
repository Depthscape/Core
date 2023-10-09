package net.depthscape.core.command;

import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class NickCommand extends BaseCommand {
    public NickCommand() {
        super("nick", "Admin");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {



        if (args.length > 0) {
            user.setCoolBar(joinRemainingArgs(0, args, true));
        }
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
