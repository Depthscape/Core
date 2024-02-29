package net.depthscape.core.tasks;

import lombok.AllArgsConstructor;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.BackupManager;
import net.depthscape.core.model.Callback;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BackupTask extends BukkitRunnable {

    Callback<File> callback;

    @Override
    public void run() {

        File root = new File(".");

        List<File> files = new ArrayList<>();
        // exclude logs and backups folder

        for (File file : root.listFiles()) {
            if (file.getName().equals("backups")) continue;
            if (file.getName().equals("libraries")) continue;
            files.add(file);
        }

        BackupManager.backup(files, callback);
    }

    public static void runBackup(Callback<File> callback) {
        new BackupTask(callback).runTaskAsynchronously(CorePlugin.getInstance());
    }
}
