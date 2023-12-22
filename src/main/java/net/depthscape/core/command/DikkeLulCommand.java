package net.depthscape.core.command;

import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class DikkeLulCommand extends BaseCommand{
    public DikkeLulCommand() {
        super("dikkelul", "Moderator");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {

        user.getInfoPanelRunnable().cancel();

    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
