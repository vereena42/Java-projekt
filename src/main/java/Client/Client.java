package Client;

import Generated.ObjectFactory;
import Generated.PersonList;
import Generated.PersonType;
import Generated.Vehicle;
import Utils.SHAGenerator;
import Utils.XMLHandling;
import Utils.ZIPHandling;
import com.google.common.collect.Iterables;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import static Utils.ProtocolMethod.*;

/**
 * Created by Dominika Salawa & Pawel Polit
 */
public class Client {

    public enum VehicleGroup {
        REGISTERED,
        UNREGISTERED
    }

    private String host;
    private int port;

    private ObjectFactory objectFactory;
    private XMLHandling xmlHandling;

    private PersonList personList = null;
    private String personListName = null;

    public Client(String p_host, int p_port) throws IOException, JAXBException {
        host = p_host;
        port = p_port;

        objectFactory = new ObjectFactory();
        xmlHandling = new XMLHandling(objectFactory);
    }

    public void getFile(String p_fileName) throws IOException, JAXBException, NoSuchAlgorithmException {
        String xmlPath = "Client/" + p_fileName + ".xml";

        if(!new File(xmlPath).exists() || hasOldVersion(p_fileName)) {
            Socket socket = new Socket(host, port);
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            InputStream inFromServer = socket.getInputStream();

            outToServer.writeBytes(GET + p_fileName + "\n");

            String zipPath = "Client/" + p_fileName + ".zip";

            FileOutputStream fos = new FileOutputStream(zipPath);
            byte[] buffer = new byte[1024];
            int count;

            while((count = inFromServer.read(buffer)) >= 0) {
                fos.write(buffer, 0, count);
            }

            fos.close();
            socket.close();

            ZIPHandling.unZipFile(zipPath);
        }

        personList = xmlHandling.unmarshal(xmlPath);
        personListName = p_fileName;

        System.out.println("File loaded");
    }

    public void sendFile() throws IOException, JAXBException {
        Socket socket = new Socket(host, port);
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

        String xmlPath = "Client/" + personListName + ".xml";

        xmlHandling.marshal(personList, xmlPath);
        String zipPath = ZIPHandling.toZip(xmlPath);

        outToServer.writeBytes(POST + personListName + "\n");

        int count;
        byte[] buffer = new byte[1024];

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(zipPath)));
        while((count = in.read(buffer)) >= 0) {
            outToServer.write(buffer, 0, count);
            outToServer.flush();
        }

        socket.close();
    }

    public List<FileInfo> getFilesInfo() throws IOException {
        Socket socket = new Socket(host, port);
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        outToServer.writeBytes(GET + "\n");

        int numberOfFiles = Integer.parseInt(inFromServer.readLine());

        List<String> serverResponses = new ArrayList<>();

        while(serverResponses.size() != numberOfFiles) {
            serverResponses.add(inFromServer.readLine());
        }

        socket.close();

        return serverResponses.stream()
                .map(FileInfo::create)
                .collect(Collectors.toList());
    }

    public void createNewPersonList(String p_personListName) {
        personList = objectFactory.createPersonList();
        personListName = p_personListName;
    }

    public void removePersonList() throws IOException {
        Socket socket = new Socket(host, port);
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

        outToServer.writeBytes(DELETE + personListName + "\n");

        socket.close();

        personList = null;
        personListName = null;
    }

    public void addPerson(String p_name, String p_surname, String p_email, String p_pesel) {
        PersonType person = objectFactory.createPersonType();
        person.setName(p_name);
        person.setSurname(p_surname);
        person.setEmail(p_email);
        person.setPesel(p_pesel);

        personList.getPerson().add(person);
    }

    public void changePersonName(int p_personIndex, String p_name) {
        personList.getPerson().get(p_personIndex).setName(p_name);
    }

    public void changePersonSurname(int p_personIndex, String p_surname) {
        personList.getPerson().get(p_personIndex).setSurname(p_surname);
    }

    public void changePersonEmail(int p_personIndex, String p_email) {
        personList.getPerson().get(p_personIndex).setEmail(p_email);
    }

    public void changePersonPesel(int p_personIndex, String p_pesel) {
        personList.getPerson().get(p_personIndex).setPesel(p_pesel);
    }

    public void removePerson(int p_personIndex) {
        personList.getPerson().remove(p_personIndex);
    }

    public String getPersonName(int p_personIndex) {
        return personList.getPerson().get(p_personIndex).getName();
    }

    public String getPersonSurname(int p_personIndex) {
        return personList.getPerson().get(p_personIndex).getSurname();
    }

    public String getPersonPesel(int p_personIndex) {
        return personList.getPerson().get(p_personIndex).getPesel();
    }

    public String getPersonEmail(int p_personIndex) {
        return personList.getPerson().get(p_personIndex).getEmail();
    }

    public List<Vehicle> getVehicles(int p_personIndex, VehicleGroup p_vehicleGroup) {
        return getAppropriateVehicleList(personList.getPerson().get(p_personIndex), p_vehicleGroup);
    }

    public List<String> getPeopleInfo() {
        return personList.getPerson().stream()
                .map(person -> person.getName() + " " + person.getSurname() + " " + person.getPesel() + " " + person.getEmail())
                .collect(Collectors.toList());
    }

    public void addVehicle(int p_personIndex, VehicleGroup p_vehicleGroup, String p_type, String p_chassisNumber,
                           String p_documentNumber, String p_registrationNumber, Date p_registrationDate,
                           Date p_documentExpirationDate) throws DatatypeConfigurationException {

        Vehicle vehicle = prepareVehicle(p_type, p_chassisNumber, p_documentNumber, p_registrationNumber,
                                         p_registrationDate, p_documentExpirationDate);

        PersonType person = personList.getPerson().get(p_personIndex);
        getAppropriateVehicleList(person, p_vehicleGroup).add(vehicle);
    }

    public void changeVehicleType(int p_personIndex, VehicleGroup p_vehicleGroup, int p_vehicleIndex, String p_type) {
        PersonType person = personList.getPerson().get(p_personIndex);
        getAppropriateVehicleList(person, p_vehicleGroup)
                .get(p_vehicleIndex)
                .setType(p_type);
    }

    public void changeVehicleChassisNumber(int p_personIndex, VehicleGroup p_vehicleGroup, int p_vehicleIndex,
                                           String p_chassisNumber) {

        PersonType person = personList.getPerson().get(p_personIndex);
        getAppropriateVehicleList(person, p_vehicleGroup)
                .get(p_vehicleIndex)
                .setChassisNumber(p_chassisNumber);
    }

    public void changeVehicleDocumentNumber(int p_personIndex, VehicleGroup p_vehicleGroup, int p_vehicleIndex,
                                            String p_documentNumber) {

        PersonType person = personList.getPerson().get(p_personIndex);
        getAppropriateVehicleList(person, p_vehicleGroup)
                .get(p_vehicleIndex)
                .setDocumentNumber(p_documentNumber);
    }

    public void changeVehicleRegistrationNumber(int p_personIndex, VehicleGroup p_vehicleGroup, int p_vehicleIndex,
                                                String p_registrationNumber) {

        PersonType person = personList.getPerson().get(p_personIndex);
        getAppropriateVehicleList(person, p_vehicleGroup)
                .get(p_vehicleIndex)
                .setRegistrationNumber(p_registrationNumber);
    }

    public void changeVehicleRegistrationDate(int p_personIndex, VehicleGroup p_vehicleGroup, int p_vehicleIndex,
                                              Date p_registrationDate) throws DatatypeConfigurationException {

        PersonType person = personList.getPerson().get(p_personIndex);
        getAppropriateVehicleList(person, p_vehicleGroup)
                .get(p_vehicleIndex)
                .setRegistrationDate(prepareXmlDate(p_registrationDate));
    }

    public void changeVehicleDocumentExpirationDate(int p_personIndex, VehicleGroup p_vehicleGroup, int p_vehicleIndex,
                                                    Date p_documentExpirationDate) throws DatatypeConfigurationException {

        PersonType person = personList.getPerson().get(p_personIndex);
        getAppropriateVehicleList(person, p_vehicleGroup)
                .get(p_vehicleIndex)
                .setRegistrationDate(prepareXmlDate(p_documentExpirationDate));
    }

    public void removeVehicle(int p_personIndex, VehicleGroup p_vehicleGroup, int p_vehicleIndex) {
        PersonType person = personList.getPerson().get(p_personIndex);
        getAppropriateVehicleList(person, p_vehicleGroup).remove(p_vehicleIndex);
    }

    private boolean hasOldVersion(String p_fileName) throws IOException, NoSuchAlgorithmException {
        String shaOfOwnedFile = SHAGenerator.getSHA("Client/" + p_fileName + ".xml");
        List<FileInfo> serverFilesInfo = getFilesInfo();

        List<FileInfo> listWithOneFileInfo = serverFilesInfo.stream()
                .filter(fileInfo -> fileInfo.getName().equals(p_fileName))
                .collect(Collectors.toList());
        FileInfo correspondingFileInfo = Iterables.getOnlyElement(listWithOneFileInfo);

        return !correspondingFileInfo.getSha().equals(shaOfOwnedFile);
    }

    private List<Vehicle> getAppropriateVehicleList(PersonType p_person, VehicleGroup p_vehicleGroup) {
        if(p_vehicleGroup == VehicleGroup.REGISTERED) {
            return p_person.getRegisteredCars();

        } else if(p_vehicleGroup == VehicleGroup.UNREGISTERED) {
            return p_person.getUnregisteredCars();
        }

        throw new IllegalArgumentException("Not recognized vehicle group value");
    }

    private Vehicle prepareVehicle(String p_type, String p_chassisNumber, String p_documentNumber,
                                   String p_registrationNumber, Date p_registrationDate, Date p_documentExpirationDate)
            throws DatatypeConfigurationException {

        Vehicle vehicle = objectFactory.createVehicle();
        vehicle.setType(p_type);
        vehicle.setChassisNumber(p_chassisNumber);
        vehicle.setDocumentNumber(p_documentNumber);
        vehicle.setRegistrationNumber(p_registrationNumber);
        vehicle.setRegistrationDate(prepareXmlDate(p_registrationDate));
        vehicle.setDocumentExpirationDate(prepareXmlDate(p_documentExpirationDate));

        return vehicle;
    }

    private XMLGregorianCalendar prepareXmlDate(Date p_date) throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(p_date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }

    public static void main(String[] args) {
        try {
            new FilesListView(new Client("localhost", 4567));
        } catch(IOException | JAXBException p_e) {
            p_e.printStackTrace();
        }
    }
}
