package net.depthscape.core.command;

import net.depthscape.core.ability.Ability;
import net.depthscape.core.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class AbilityCommand extends BaseCommand {

    private final Ability ability;

    public AbilityCommand(String label, Ability ability) {
        super(label);
        this.ability = ability;
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
