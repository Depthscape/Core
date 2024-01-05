package net.depthscape.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Websocket {

    private boolean isServer;
    private String host;
    private int port;
}
