package net.depthscape.core.ability;

import lombok.Getter;
import net.depthscape.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AbilityManager {

    private static final List<Ability> registeredAbilities = new ArrayList<>();

    public static void registerAbility(Ability ability) {
        registeredAbilities.add(ability);
        Bukkit.getServer().getPluginManager().registerEvents(ability, CorePlugin.getInstance());
    }

    public static void unregisterAbility(Ability ability) {
        registeredAbilities.remove(ability);
        HandlerList.unregisterAll(ability);
    }

    public static Ability getAbility(String name) {
        for (Ability ability : registeredAbilities) {
            if (ability.getName().equalsIgnoreCase(name)) {
                return ability;
            }
        }
        return null;
    }
}
