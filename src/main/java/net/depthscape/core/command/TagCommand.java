package net.depthscape.core.command;

import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class TagCommand extends BaseCommand {
    public TagCommand() {
        super("tag", "Admin");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {

    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
