package net.depthscape.core.command.commands;


import net.depthscape.core.CorePlugin;
import net.depthscape.core.command.RankBaseCommand;
import net.depthscape.core.menu.Menu;
import net.depthscape.core.menu.MenuItem;
import net.depthscape.core.menu.PagedMenu;
import net.depthscape.core.menu.menus.ProfileMenu;
import net.depthscape.core.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class TestCommand extends RankBaseCommand {
    public TestCommand() {
        super("test", "");
    }

    @Override
    protected void onCommand(Player player, User user, String[] args) {

        PagedMenu<Material> menu = new PagedMenu<>(Arrays.stream(Material.values()).filter(Material::isBlock).filter(material -> !material.isAir()).toList(), "Blocks") {
            @Override
            public MenuItem getItem(Material material) {
                return MenuItem.of(new ItemStack(material));
            }
        };

        menu.open(user);

//        ProfileMenu menu = new ProfileMenu(user);
//        menu.open(user);

    }

    @Override
    protected List<String> onTabComplete(Player player, User user, String[] args) {
        return null;
    }
}
