package net.depthscape.core.socket;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.event.OfflineUserAsyncChatEvent;
import net.depthscape.core.event.WebSocketClientRecieveDataEvent;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.util.UUID;


public class WSClient extends WebSocketClient {
    public WSClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        JSONObject data = new JSONObject();
        data.put("address", Bukkit.getIp() + ":" + Bukkit.getPort());
        send(DataType.HANDSHAKE, data);
        System.out.println("new connection opened");
    }

    @Override
    public void onMessage(String s) {
        DataType type = DataType.valueOf(new JSONObject(s).getString("type"));
        JSONObject data = new JSONObject(s);
        switch (type) {
            case CHAT_MESSAGE -> ChatUtils.sendDiscordMessage(data);
            case SHUTDOWN -> Bukkit.getLogger().info("Lost connection to websocket server");
        }

        WebSocketClientRecieveDataEvent event = new WebSocketClientRecieveDataEvent(type, data);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {
        System.err.println("an error occurred:" + e);
    }

    public void send(DataType type, JSONObject content) {
        JSONObject data = new JSONObject();
        data.put("type", type);
        data.put("content", content);
        send(data.toString());
    }
}
