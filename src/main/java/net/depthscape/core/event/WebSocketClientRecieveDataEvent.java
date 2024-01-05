package net.depthscape.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.depthscape.core.socket.DataType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
public class WebSocketClientRecieveDataEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private DataType type;
    private JSONObject content;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
