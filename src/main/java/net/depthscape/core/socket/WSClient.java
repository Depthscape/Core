package net.depthscape.core.socket;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.event.WebSocketClientRecieveDataEvent;
import net.depthscape.core.tasks.RestartTask;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;


public class WSClient extends WebSocketClient {

    private BukkitRunnable reconnectTask;
    private boolean serverRestarting = false;

    public WSClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        if (reconnectTask != null) {
            reconnectTask.cancel();
            reconnectTask = null;
        }

        JSONObject data = new JSONObject();
        data.put("address", Bukkit.getIp() + ":" + Bukkit.getPort());
        data.put("server_name", CorePlugin.getInstance().getMainConfig().getServerName());
        send(DataType.HANDSHAKE, data);
        System.out.println("new connection opened");

        System.out.println("server restarting? " + serverRestarting);
        if (serverRestarting) {
            RestartTask.runRestart(true);
        }
    }

    @Override
    public void onMessage(String s) {
        DataType type = DataType.valueOf(new JSONObject(s).getString("type"));
        JSONObject data = new JSONObject(s);
        switch (type) {
            case CHAT_MESSAGE -> {
                System.out.println("Data recieved: " + data);
                ChatUtils.sendDiscordMessage(data.getJSONObject("content"));
            }
            case RESTART -> {
                CorePlugin.setJoiningAllowed(false);
                System.out.println("Server restarting");
                this.serverRestarting = true;
            }
        }

        WebSocketClientRecieveDataEvent event = new WebSocketClientRecieveDataEvent(type, data);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(event);
            }
        }.runTask(CorePlugin.getInstance());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + ", additional info: " + reason);
        if ((code == 1006 || code == -1)) { // Abnormal closure or connection refused
            System.out.println("Attempting to reconnect in 5 seconds...");
            scheduleReconnect();
        }
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

    private void scheduleReconnect() {
        if (reconnectTask == null) {
            reconnectTask = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Reconnecting...");
                        connectBlocking(); // Attempt to reconnect
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            reconnectTask.runTaskLaterAsynchronously(CorePlugin.getInstance(), 20 * 5); // 5 seconds
        }
    }
}
