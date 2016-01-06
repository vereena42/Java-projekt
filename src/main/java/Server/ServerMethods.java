import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by vereena on 1/6/16.
 */
public interface ServerMethods extends Remote {
    String echo(String s) throws RemoteException;
}

