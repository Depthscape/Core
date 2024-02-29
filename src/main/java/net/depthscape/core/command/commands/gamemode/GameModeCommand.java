package net.depthscape.core.command.commands.gamemode;

import net.depthscape.core.command.RankBaseCommand;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.TabUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

public class GameModeCommand extends RankBaseCommand {

    private final GameMode gameMode;

    public GameModeCommand(String label, GameMode gameMode) {
        super(label, "Builder");
        this.gameMode = gameMode;
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        switch (args.length) {
            case 0 -> {
                player.setGameMode(gameMode);
                user.sendMessage(ChatUtils.getInfoMessage("You changed your gamemode to " + gameMode.name().toLowerCase() + " mode"));
            }
            case 1 -> {
                if (user.getRank().getWeight() >= Rank.getRank("Admin").getWeight()) {
                    User target = User.getUser(args[0]);
                    if (target == null) {
                        user.sendMessage(getError("Player not found"));
                        return;
                    }
                    target.getPlayer().setGameMode(gameMode);
                    user.sendMessage(ChatUtils.getInfoMessage("You changed " + target.getName() + "'s gamemode to " + gameMode.name().toLowerCase() + " mode"));
                } else {
                    user.sendMessage(getError("You don't have permission to change the gamemode of other players"));
                }
            }
            default -> user.sendMessage(getError("Usage: /" + getLabel()));
        }
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        if (args.length == 1) {
            if (user.getRank().getWeight() >= Rank.getRank("Admin").getWeight()) {
                return null;
            }
            return TabUtil.complete(args[0], Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        }
        return null;
    }
}
