package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Dominika Salawa & Pawel Polit
 */

public enum ZIPHandling {
    ;

    public static String toZip(String path) throws IOException {
        byte[] buf = new byte[2048];
        String pathWithoutExtension = path.substring(0, path.lastIndexOf('.'));

        FileOutputStream fileOutputStream = new FileOutputStream(pathWithoutExtension + ".zip");
        FileInputStream fileInputStream = new FileInputStream(path);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        ZipEntry zipEntry = new ZipEntry(path.substring(path.lastIndexOf('/')));
        zipOutputStream.putNextEntry(zipEntry);
        int length = fileInputStream.read(buf);
        while(length > 0) {
            zipOutputStream.write(buf, 0, length);
            length = fileInputStream.read(buf);
        }
        zipOutputStream.closeEntry();
        zipOutputStream.close();
        fileInputStream.close();
        fileOutputStream.close();
        return pathWithoutExtension + ".zip";
    }

    public static void unZipFile(String p_filePath) throws IOException {
        byte[] buffer = new byte[1024];

        String outputFolderPath = p_filePath.substring(0, p_filePath.lastIndexOf('/'));
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(p_filePath));

        ZipEntry zipEntry = zipInputStream.getNextEntry();

        while(zipEntry != null) {
            String fileName = zipEntry.getName();
            File newFile = new File(outputFolderPath + '/' + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int length;

            while((length = zipInputStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, length);
            }

            fileOutputStream.close();
            zipEntry = zipInputStream.getNextEntry();
        }

        zipInputStream.closeEntry();
        zipInputStream.close();

    }
}
