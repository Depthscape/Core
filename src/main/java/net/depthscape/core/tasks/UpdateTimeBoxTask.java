package net.depthscape.core.tasks;

import lombok.Getter;
import net.depthscape.core.model.Box;
import net.depthscape.core.utils.CustomFontCharacter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class UpdateTimeBoxTask extends BukkitRunnable {

    Box box;

    int hours = 0, minutes = 0;

    public UpdateTimeBoxTask() {
        box = new Box(getFormattedTime());
    }

    @Override
    public void run() {
        // translate mincraft ingame time 20 minutes to 1 day (24 hours)
        long ingameTime = Bukkit.getWorlds().get(0).getTime();

        hours = (int) (ingameTime / 1000 + 6) % 24;
        minutes = (int) ((ingameTime % 1000) / 1000.0 * 60);

        box.setText(getFormattedTime());
    }

    private String getFormattedTime() {
        String hoursString = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesString = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        return CustomFontCharacter.CLOCK + " " + hoursString + ":" + minutesString;
    }
}
