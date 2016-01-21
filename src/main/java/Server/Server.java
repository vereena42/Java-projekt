package Server;

import Utils.ZIPHandling;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dominika Salawa & Pawel Polit
 */
public class Server {

    private static String echo(String s) throws IOException {
        System.out.println(ZIPHandling.toZip("Server/personList.xml"));
        return s;
    }

    private static void sendFile(String path, DataOutputStream outToClient) throws IOException {
        int count;
        byte[] buffer = new byte[1024];

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
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
                echo("personList.zip\n");
                sendFile("Server/personList.zip", outToClient);
                connectionSocket.close();
            }

        } catch(IOException p_e) {
            p_e.printStackTrace();
        }
    }
}

