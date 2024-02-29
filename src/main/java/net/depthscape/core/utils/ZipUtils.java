package net.depthscape.core.utils;

import net.depthscape.core.model.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void zipFiles(List<File> files, File zipFile, Callback<File> callback) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file, file.getName(), zos);
                } else {
                    zipFile(file, "", zos);
                }
            }

            zos.close();
            fos.close();

            callback.call(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
            } else {
                zipFile(file, parentFolder, zos);
            }
        }
    }

    private static void zipFile(File file, String parentFolder, ZipOutputStream zos) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(file);
        zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

        int length;
        while ((length = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}
