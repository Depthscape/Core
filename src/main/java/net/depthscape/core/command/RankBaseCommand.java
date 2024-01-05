package net.depthscape.core.command;

import net.depthscape.core.rank.Rank;
import net.depthscape.core.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class RankBaseCommand extends BaseCommand {

    private final Rank minimumRank;

    public RankBaseCommand(String label, String minimumRank) {
        super(label);
        if (minimumRank != null) {
            this.minimumRank = Rank.getRank(minimumRank);
            return;
        }
        this.minimumRank = null;
    }

    //if (minimumRank != null) {
    //            if (user.getRank().getWeight() > minimumRank.getWeight()) {
    //                user.sendMessage(getError("The command you seek is lost in the server's pixelated dimension."));
    //                return true;
    //            }
    //        }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (minimumRank != null) {
            if (sender instanceof Player player) {
                User user = User.getUser(player);
                if (user.getRank().getWeight() > minimumRank.getWeight()) {
                    user.sendMessage(getError("The command you seek is lost in the server's pixelated dimension."));
                    return true;
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}
