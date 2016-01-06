import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by vereena on 1/6/16.
 */
public class Server implements ServerMethods {

    public Server(){}
    @Override
    public String echo(String s)
    {
        System.out.println(toZip("MyFile"));
        return s;
    }

    private String toZip(String path)
    {
        try {
            byte[] buf = new byte[2048];
            FileOutputStream fileOutputStream=new FileOutputStream(path+".zip");
            FileInputStream fileInputStream=new FileInputStream(path);
            ZipOutputStream zipOutputStream=new ZipOutputStream(fileOutputStream);
            ZipEntry zipEntry=new ZipEntry(path);
            zipOutputStream.putNextEntry(zipEntry);
            int length=fileInputStream.read(buf);
            while(length>0)
            {
                zipOutputStream.write(buf,0,length);
                length=fileInputStream.read(buf);
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
            return path+".zip";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String [] args)
    {
        try{
            Server server=new Server();
            ServerMethods stub=(ServerMethods) UnicastRemoteObject.exportObject(server,0);
            Registry registry= LocateRegistry.getRegistry(1099);
            registry.bind("ServerMethods",stub);
            System.out.println("Server is ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}

