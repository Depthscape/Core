package net.depthscape.core.socket;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.event.OfflineUserAsyncChatEvent;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.java_websocket.WebSocket;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WSServer extends WebSocketServer {

    List<String> connectedServers;

    public WSServer(InetSocketAddress address) {
        super(address);
        this.connectedServers = new ArrayList<>();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        Bukkit.getLogger().info("New connection from " + webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        // handle messages from clients
        JSONObject data = new JSONObject(s);
        DataType type = DataType.valueOf(data.getString("type"));
        JSONObject content = data.getJSONObject("content");
        switch (type) {
            case HANDSHAKE -> {
                // handle handshake
                String serverName = content.getString("server_name");
                String address = content.getString("address");
                this.connectedServers.add(serverName);
                Bukkit.getLogger().info("Handshake from " + address + " with server name " + serverName);
                Bukkit.getLogger().info("Status: " + (webSocket.getReadyState() == ReadyState.OPEN ? "OK" : "ERROR"));
            }
            case CHAT_MESSAGE -> {
                // handle chat message
                String server = content.getString("server");
                OfflineUser user = UserManager.getOfflineUserSync(UUID.fromString(content.getString("player")));
                String message = content.getString("message");
                OfflineUserAsyncChatEvent event = new OfflineUserAsyncChatEvent(user, message, server);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getPluginManager().callEvent(event);
                    }
                }.runTask(CorePlugin.getInstance());
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.err.println("an error occurred on connection " + webSocket.getRemoteSocketAddress()  + ":" + e);
    }

    @Override
    public void onStart() {

    }

    public void broadcast(DataType type, JSONObject content) {
        JSONObject data = new JSONObject();
        data.put("type", type);
        data.put("content", content);
        broadcast(data.toString());
    }

    public void handleChatMessage(OfflineUser user, String message, String server) {
        JSONObject data = new JSONObject();
        data.put("server", server);
        data.put("prefix", user.getRank().getChatPrefix());
        data.put("player", user.getName());
        data.put("message", message);
        broadcast(DataType.CHAT_MESSAGE, data);
        System.out.println("Data to send: " + data);
        ChatUtils.sendDiscordMessage(data);
    }

    public void broadcastRestart() {
        broadcast(DataType.RESTART, new JSONObject());
    }
}
