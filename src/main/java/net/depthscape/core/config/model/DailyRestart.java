package net.depthscape.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class DailyRestart {

    boolean enabled;
    boolean popups;
    LocalTime time;
    Map<Integer, String> messages;
}
