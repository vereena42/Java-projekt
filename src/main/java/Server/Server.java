package Server;

import Generated.ObjectFactory;
import Generated.PersonList;
import Utils.SHAGenerator;
import Utils.XMLHandling;
import Utils.ZIPHandling;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dominika Salawa & Pawel Polit
 */
public class Server {

    private static String echo(String s) throws IOException {
        System.out.println(ZIPHandling.toZip("Server/personList.xml"));
        return s;
    }

    private static void changeInfo(String filename) throws IOException, NoSuchAlgorithmException {
        File prevInfo = new File("Server/ServerInfo.txt");
        File tempFile = new File("Server/TempFiles/temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(prevInfo));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line;

        while((line = reader.readLine()) != null) {
            String[] arr=line.split(" ");
            if(arr[0].equals(filename)){
                DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                writer.write(filename+" "+ SHAGenerator.getSHA("Server/"+filename+".xml")+" "+dateFormat.format(date)+"\n");
                continue;
            }
            writer.write(line + "\n");
        }
        writer.close();
        reader.close();
        Files.copy(tempFile.toPath(), prevInfo.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void deleteInfo(String filename) throws IOException {
        File prevInfo = new File("Server/ServerInfo.txt");
        File tempFile = new File("Server/TempFiles/temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(prevInfo));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line;
        boolean k=false;

        while((line = reader.readLine()) != null) {
            if(k==false){
                writer.write(new Integer(Integer.parseInt(line) - 1).toString() + "\n");
                k=true;
                continue;
            }
            String[] arr=line.split(" ");
            if(arr[0].equals(filename))
                continue;
            writer.write(line + "\n");
        }
        writer.close();
        reader.close();
        Files.copy(tempFile.toPath(), prevInfo.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void addInfo(String filename) throws IOException, NoSuchAlgorithmException {
        File prevInfo = new File("Server/ServerInfo.txt");
        File tempFile = new File("Server/TempFiles/temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(prevInfo));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line;
        boolean k=false;

        while((line = reader.readLine()) != null) {
            if(k==false){
                writer.write(new Integer(Integer.parseInt(line)+1).toString()+"\n");
                DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                writer.write(filename+" "+ SHAGenerator.getSHA("Server/"+filename+".xml")+" "+dateFormat.format(date)+"\n");
                k=true;
                continue;
            }
            writer.write(line + "\n");
        }
        writer.close();
        reader.close();
        Files.copy(tempFile.toPath(), prevInfo.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void sendFile(String path, DataOutputStream outToClient) throws IOException {
        if(!path.equals("Server/ServerInfo.txt"))
            path = ZIPHandling.toZip(path);
        int count;
        byte[] buffer = new byte[1024];

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        while((count = in.read(buffer)) >= 0) {
            outToClient.write(buffer, 0, count);
            outToClient.flush();
        }
    }

    private static void getFile(String filename, DataInputStream inFromClient) throws IOException, JAXBException, NoSuchAlgorithmException {

        //writing to temp
        String zipPath = "Server/TempFiles/temp.zip";
        FileOutputStream fos = new FileOutputStream(zipPath);
        XMLHandling xmlHandling = new XMLHandling(new ObjectFactory());
        byte[] buffer = new byte[1024];
        int count;

        while((count = inFromClient.read(buffer)) >= 0) {
            fos.write(buffer, 0, count);
        }

        fos.close();

        //unzipping and checking if it's personList
        ZIPHandling.unZipFile(zipPath);
        String xmlPath = "Server/TempFiles/"+filename+".xml";
        String properXmlPath = "Server/"+filename+".xml";
        PersonList personList= xmlHandling.unmarshal(xmlPath);

        //copying file to proper path
        File source = new File(xmlPath);
        File dest = new File(properXmlPath);
        if(dest.isFile()) {
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //changing info file
            changeInfo(filename);
        }
        else{
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //changing info file
            addInfo(filename);
        }
    }

    private static void deleteFile(String filename) throws IOException {
        File f=new File("Server/"+filename+".xml");
        if(f.isFile()) {
            Files.delete(f.toPath());
            deleteInfo(filename);
        }
    }

    public static void main(String[] args) {
        try {
            File info=new File("Server/ServerInfo.txt");
            BufferedWriter output = null;
            if(!info.isFile())
            {
                try {
                    output=new BufferedWriter(new FileWriter(info));
                    output.write("0\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String clientSentence;
            ServerSocket welcomeSocket = new ServerSocket(4567);
            System.out.println("Server is ready");

            while(true) {
                Socket connectionSocket = welcomeSocket.accept();

                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(connectionSocket.getInputStream());

                clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);
                String[] arr=clientSentence.split(" ");
                if(arr[0].equals("GET"))
                {
                    if(arr.length==1)
                        sendFile("Server/ServerInfo.txt", outToClient);
                    else
                        sendFile("Server/"+arr[1]+".xml",outToClient);
                }
                else if(arr[0].equals("POST"))
                    getFile(arr[1],inputStream);
                else if(arr[0].equals("DELETE"))
                    deleteFile(arr[1]);
                //echo("personList.zip\n");
                //sendFile("Server/personList.zip", outToClient);
                connectionSocket.close();
            }

        } catch(IOException p_e) {
            p_e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}

