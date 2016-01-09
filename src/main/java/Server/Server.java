package Server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by vereena on 1/6/16.
 */
public class Server {

    private static String echo(String s) {
        System.out.println(toZip("Server/MyFile"));
        return s;
    }

    private static String toZip(String path) {
        try {
            byte[] buf = new byte[2048];
            FileOutputStream fileOutputStream = new FileOutputStream(path + ".zip");
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
            return path + ".zip";
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void sendFile(String path, DataOutputStream outToClient) throws IOException {
        int count;
        byte[] buffer = new byte[1024];

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File((path))));
        while((count = in.read(buffer)) >= 0) {
            outToClient.write(buffer, 0, count);
            outToClient.flush();
        }
    }

    public static void main(String[] args) {
        try {
            String clientSentence;
            ServerSocket welcomeSocket = new ServerSocket(4567);
            System.out.println("Server is ready");

            while(true) {
                Socket connectionSocket = welcomeSocket.accept();

                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);
                outToClient.writeBytes(echo("MyFile.zip\n"));
                sendFile("Server/MyFile.zip", outToClient);
                connectionSocket.close();
            }

        } catch(IOException p_e) {
            p_e.printStackTrace();
        }
    }
}

