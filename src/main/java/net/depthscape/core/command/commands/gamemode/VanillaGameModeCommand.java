package net.depthscape.core.command.commands.gamemode;

import net.depthscape.core.command.RankBaseCommand;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.TabUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class VanillaGameModeCommand extends RankBaseCommand {
    public VanillaGameModeCommand() {
        super("gamemode", "Builder");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {
        switch (args.length) {
            case 1 -> {
                try {
                    GameMode gameMode = GameMode.valueOf(args[0].toUpperCase());
                    player.setGameMode(gameMode);
                    user.sendMessage(ChatUtils.getInfoMessage("You changed your gamemode to " + gameMode.name().toLowerCase() + " mode"));
                } catch (IllegalArgumentException e) {
                    user.sendMessage(getError("Invalid gamemode"));
                }
            }
            case 2 -> {
                if (user.getRank().getWeight() >= Rank.getRank("Admin").getWeight()) {
                    User target = User.getUser(args[0]);
                    if (target == null) {
                        user.sendMessage(getError("Player not found"));
                        return;
                    }
                    try {
                        GameMode gameMode = GameMode.valueOf(args[1].toUpperCase());
                        target.getPlayer().setGameMode(gameMode);
                        user.sendMessage(ChatUtils.getInfoMessage("You changed " + target.getName() + "'s gamemode to " + gameMode.name().toLowerCase() + " mode"));
                    } catch (IllegalArgumentException e) {
                        user.sendMessage(getError("Invalid gamemode"));
                    }
                } else {
                    user.sendMessage(getError("You don't have permission to change the gamemode of other players"));
                }
            }
            default -> user.sendMessage(getError("Usage: /gamemode <gamemode>"));
        }
    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        if (args.length == 1) {
            return TabUtil.complete(args[0], Arrays.stream(GameMode.values()).map(n -> n.name().toLowerCase()).toList());
        } else if (args.length == 2) {
            if (user.getRank().getWeight() >= Rank.getRank("Admin").getWeight()) {
                return TabUtil.complete(args[1], Arrays.stream(GameMode.values()).map(n -> n.name().toLowerCase()).toList());
            }
        }
        return null;
    }
}
