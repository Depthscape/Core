package net.depthscape.core.ability;

import lombok.Getter;
import net.depthscape.core.user.User;
import org.bukkit.event.Listener;

@Getter
public abstract class Ability implements Listener {

    String name;

    public Ability(String name) {
        this.name = name;
    }

    public abstract void enable(User user);
    public abstract void disable(User user);

}
