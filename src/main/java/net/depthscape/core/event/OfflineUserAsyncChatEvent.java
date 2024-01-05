package net.depthscape.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.depthscape.core.user.OfflineUser;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class OfflineUserAsyncChatEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private OfflineUser user;
    private String message;
    private String server;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
