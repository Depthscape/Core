package net.depthscape.core.command.commands;

import net.depthscape.core.command.RankBaseCommand;
import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class NoClipCommand extends RankBaseCommand {

    public NoClipCommand() {
        super("noclip", "Builder");
    }

    @Override
    protected void onCommand(Player player, User user, String[] strings) {
        user.toggleNoClip();
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] strings) {
        return null;
    }
}
