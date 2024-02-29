package net.depthscape.core;

import net.depthscape.core.model.Callback;
import net.depthscape.core.utils.ZipUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BackupManager {

    public static void backup(List<File> files, Callback<File> callback) {
        long start = System.currentTimeMillis();
        Bukkit.getLogger().info("Creating backup...");
        String currentDate = getCurrentDate();
        File dailyBackupFolder = createDailyBackupFolder(currentDate);

        int newIndex = getNextIndex(dailyBackupFolder, currentDate);
        File backupFolder = createBackupFolder(dailyBackupFolder, currentDate, newIndex);

        File destination = new File(backupFolder, "backup.zip");
        ZipUtils.zipFiles(files, destination, file -> {
            if (file == null) {
                Bukkit.getLogger().severe("Backup failed");
                return;
            }
            long end = System.currentTimeMillis();
            Bukkit.getLogger().info("Backup created: " + file.getName() + " in " + (end - start) + "ms");
            callback.call(file);
        });

        weeklyBackup();
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    private static File createDailyBackupFolder(String currentDate) {
        File dailyBackupFolder = new File("backups/daily");
        dailyBackupFolder.mkdirs();

        File dailySubFolder = new File(dailyBackupFolder, currentDate);
        dailySubFolder.mkdirs();

        return dailyBackupFolder;
    }

    private static int getNextIndex(File dailyBackupFolder, String currentDate) {
        int index = 0;
        while (backupFolderExists(dailyBackupFolder, currentDate, index)) {
            index++;
        }
        return index;
    }

    private static boolean backupFolderExists(File dailyBackupFolder, String currentDate, int index) {
        String folderName = "backup" + index;
        File potentialBackupFolder = new File(new File(dailyBackupFolder, currentDate), folderName);
        return potentialBackupFolder.exists();
    }

    private static File createBackupFolder(File dailyBackupFolder, String currentDate, int index) {
        String folderName = "backup" + index;
        File backupFolder = new File(new File(dailyBackupFolder, currentDate), folderName);
        backupFolder.mkdirs();

        return backupFolder;
    }

    public static void weeklyBackup() {
        String currentWeek = getCurrentWeek();
        File weeklyBackupFolder = createWeeklyBackupFolder(currentWeek);

        File dailyBackupFolder = new File("backups/daily");
        File[] dailyBackups = dailyBackupFolder.listFiles();

        if (dailyBackups != null) {
            for (File dailyBackup : dailyBackups) {
                if (isOlderThanOneWeek(dailyBackup)) {
                    moveBackupToWeekly(dailyBackup, weeklyBackupFolder);
                }
            }
        }
    }

    private static String getCurrentWeek() {
        SimpleDateFormat weekFormat = new SimpleDateFormat("yyyy-ww");
        return weekFormat.format(new Date());
    }

    private static File createWeeklyBackupFolder(String currentWeek) {
        File weeklyBackupFolder = new File("backups/weekly");
        weeklyBackupFolder.mkdirs();

        File weeklySubFolder = new File(weeklyBackupFolder, currentWeek);
        weeklySubFolder.mkdirs();

        return weeklySubFolder;
    }

    private static void moveBackupToWeekly(File dailyBackup, File weeklySubFolder) {
        try {
            Files.move(dailyBackup.toPath(), weeklySubFolder.toPath().resolve(dailyBackup.getName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isOlderThanOneWeek(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            long creationTime = attr.creationTime().toMillis();
            long oneWeekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000); // One week in milliseconds
            return creationTime < oneWeekAgo;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

