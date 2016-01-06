import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by vereena on 1/6/16.
 */
public class Client {
    public Client(){}

    public static void main(String [] args)
    {
        //String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            ServerMethods stub = (ServerMethods) registry.lookup("ServerMethods");
            String response = stub.echo("Hello World!");
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
