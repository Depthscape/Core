package net.depthscape.core.menu.menus;

import net.depthscape.core.menu.MenuItem;
import net.depthscape.core.menu.PagedMenu;
import net.depthscape.core.punishment.Punishment;
import net.depthscape.core.user.OfflineUser;

import java.util.List;

public class PunishmentHistoryMenu extends PagedMenu<Punishment> {

    OfflineUser target;

    public PunishmentHistoryMenu(List<Punishment> punishments, OfflineUser target) {
        super(punishments, target.getName().substring(0, 13) + "'s History");
    }

    @Override
    public MenuItem getItem(Punishment punishment) {
        return null;
    }
}
