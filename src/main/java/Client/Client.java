package Client;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by vereena on 1/6/16.
 */
public class Client {

    private static void receiveFile(InputStream inFromServer) throws IOException {
        FileOutputStream fos = new FileOutputStream("Client/MyFile.zip");

        byte[] buffer = new byte[1024];
        int count;

        while((count = inFromServer.read(buffer)) >= 0) {
            fos.write(buffer, 0, count);
        }
        fos.close();
        System.out.println("File received");
    }

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 4567);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            InputStream inFromServer = clientSocket.getInputStream();

            outToServer.writeBytes("Give me file\n");
            receiveFile(inFromServer);

            clientSocket.close();
        } catch(IOException p_e) {
            p_e.printStackTrace();
        }
    }
}
