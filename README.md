# DEPTHSCAPE CORE
Core is the main plugin of the Depthscape servers.
It provides the basic functionality for the other plugins to build upon.
Also, it has all the backend functionality for the servers.

## Network Channel System
The network channel system is a way to send data between all the servers.
1 server is the websocket server and other are the clients. 

### Websocket server
The websocket server is started at the start of the plugin that is the websocket server. It runs on `localhost:8080`.
The server is used to send and receive data from the other servers.

### Data Protocols
The protocol is a way to send data between the servers. It is a JSON object with the following structure:
```json
{
  "type": "string",
  "data": "object"
}
```
`data` is the data that is being sent and `type` is the type of the data. The type is used to determine what to do with the data.

### Data Types

#### Handshake
The handshake is the first thing that is sent when a client connects to the server. 
It is used to identify the client and to send the server information about the client. 
The data has the following structure:
```json
{
  "type": "HANDSHAKE",
  "data": {
    "address": "string",
    "server": "string"
  }
}
```
`address` is the address of the client and `server_name` is the name of the server.
`server_name` is used to identify the server and `address` is used to identify the client.

#### Chat Message
The chat message is used to send a chat_message to all the other servers. 
This is sent when a players send a message in the chat. 
The data has the following structure:
```json
{
  "type": "CHAT_MESSAGE",
  "data": {
    "server": "string",
    "player": "string",
    "message": "string"
  }
}
```
`server` is the server that send the message.
`player` is the player's uuid that send the message.
`message` is the message that is sent.

#### Player Join
The player join is used to send a message to all the other servers when a player joins the server. 
Its commonly send at PlayerJoinEvent. 
The data has the following structure:
```json
{
  "type": "JOIN",
  "data": {
    "server": "string",
    "player": "string"
  }
}
```
`server` is the server that the player joined.
`player` is the player's uuid that joined.

#### Player Quit
The player quit is used to send a message to all the other servers when a player quits the server.
Its commonly send at PlayerQuitEvent. 
The data has the following structure:
```json
{
  "type": "QUIT",
  "data": {
    "server": "string",
    "player": "string"
  }
}
```
`server` is the server that the player quit.
`player` is the player's uuid that quit.



