package net.depthscape.core.command.commands;


import net.depthscape.core.command.BaseCommand;
import net.depthscape.core.command.RankBaseCommand;
import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCommand extends RankBaseCommand {
    public TestCommand() {
        super("test", "Moderator");
    }
    @Override
    protected void onCommand(Player player, User user, String[] args) {
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
