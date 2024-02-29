package net.depthscape.core.socket;

public enum DataType {

    /*
    Handshake from a client to server. Send to server when a client connects. Also, to test connection.
    Params:
    - server_name, server name (string)
    - address, server address (string)
    */
    HANDSHAKE,

    /*
    Shutdown or restart of the server. Send to all clients when about to restart or shutdown. So they can handle it.
     */
    SHUTDOWN,
    RESTART,

    /* Chat message from a player. Send to server when a player sends a message. So other servers can use the chat messages.
     Params:
     - server, server name (string)
     - player, player UUID (string)
     - message, message (string)
     */
    CHAT_MESSAGE,

    /* Join message from a player. Send to server when a player joins. So other servers can use the join messages.
     Params:
     - server, server name (string)
     - player, player UUID (string)
     */
    JOIN,

    /* Quit message from a player. Send to server when a player quits. So other servers can use the quit messages.
     Params:
     - server, server name (string)
     - player, player UUID (string)
     */
    QUIT,

    /* Idk yet
     Params:
     - server, server name (string)
     */
    PING_PONG,
}
