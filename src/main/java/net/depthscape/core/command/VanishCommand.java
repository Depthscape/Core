package net.depthscape.core.command;

import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class VanishCommand extends BaseCommand{
    public VanishCommand( ) {
        super("vanish", "Helper");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        user.toggleVanish();
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
