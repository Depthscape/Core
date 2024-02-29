package net.depthscape.core.command.commands;

import net.depthscape.core.command.RankBaseCommand;
import net.depthscape.core.tasks.RestartTask;
import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class DailyRestartCommand extends RankBaseCommand {

    public DailyRestartCommand() {
        super("dailyrestart", "Admin");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        RestartTask.runRestart(false);
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
