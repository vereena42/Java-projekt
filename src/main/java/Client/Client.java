package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by vereena on 1/6/16.
 */
public class Client {

    public static void main(String [] args)
    {
        try {
            String serverResponse;

            Socket clientSocket = new Socket("localhost", 5678);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer.writeBytes("Give me file\n");
            serverResponse = inFromServer.readLine();
            System.out.println("FROM SERVER: " + serverResponse);
            clientSocket.close();
        } catch(IOException p_e) {
            p_e.printStackTrace();
        }
    }
}
