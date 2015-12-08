import generated.ObjectFactory;
import generated.PersonList;
import generated.PersonType;
import generated.Vehicle;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.File;

/**
 * @author Pawel Polit
 */
public class XMLHandling {

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;

    public XMLHandling(ObjectFactory p_objectFactory) throws JAXBException {
        objectFactory = p_objectFactory;
        jaxbContext = JAXBContext.newInstance(PersonList.class);
    }

    public void marshal(PersonList p_personList) throws JAXBException {
        File file = new File("src/main/resources/personList.xml");
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        JAXBElement<PersonList> listJAXBElement = objectFactory.createPersonListInstance(p_personList);

        marshaller.marshal(listJAXBElement, file);
    }

    public PersonList unmarshal() throws JAXBException {
        File file = new File("src/main/resources/personList.xml");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (PersonList) unmarshaller.unmarshal(file);
    }

    public static void main(String[] args) throws JAXBException, DatatypeConfigurationException {
        ObjectFactory objectFactory = new ObjectFactory();
        XMLHandling xmlHandling = new XMLHandling(objectFactory);

//        PersonList personList = objectFactory.createPersonList();
//        List<PersonType> persons = personList.getPerson();
//
//        PersonType person = objectFactory.createPersonType();
//        person.setName("Jan");
//        person.setSurname("Kowalski");
//        person.setEmail("abc@abc.com");
//        person.setPesel("12345678901");
//
//        List<Vehicle> registeredCars = person.getRegisteredCars();
//        List<Vehicle> unregisteredCars = person.getUnregisteredCars();
//
//        Vehicle registeredVehicle = objectFactory.createVehicle();
//        registeredVehicle.setType("Car");
//        registeredVehicle.setChassisNumber("123");
//        registeredVehicle.setDocumentNumber("456");
//        registeredVehicle.setRegistrationNumber("789");
//
//        Date date = new Date(System.currentTimeMillis());
//        GregorianCalendar gregorianCalendar = new GregorianCalendar();
//        gregorianCalendar.setTime(date);
//        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
//
//        registeredVehicle.setRegistrationDate(xmlGregorianCalendar);
//        registeredVehicle.setDocumentExpirationDate(xmlGregorianCalendar);
//
//        registeredCars.add(registeredVehicle);
//
//        Vehicle unregisteredVehicle = objectFactory.createVehicle();
//        unregisteredVehicle.setType("Plane");
//        unregisteredVehicle.setChassisNumber("987");
//        unregisteredVehicle.setDocumentNumber("654");
//        unregisteredVehicle.setRegistrationNumber("321");
//        unregisteredVehicle.setRegistrationDate(xmlGregorianCalendar);
//        unregisteredVehicle.setDocumentExpirationDate(xmlGregorianCalendar);
//
//        unregisteredCars.add(unregisteredVehicle);
//
//        persons.add(person);
//        persons.add(objectFactory.createPersonType());
//
//        xmlHandling.marshal(personList);

        PersonList personList = xmlHandling.unmarshal();

        for(PersonType person : personList.getPerson()) {
            System.out.println(person.getName());
            System.out.println(person.getSurname());
            System.out.println(person.getPesel());
            System.out.println(person.getEmail());
            System.out.println();

            person.getRegisteredCars().forEach(XMLHandling::printInfo);
            System.out.println();
            person.getUnregisteredCars().forEach(XMLHandling::printInfo);
        }
    }

    private static void printInfo(Vehicle p_vehicle) {
        System.out.println(p_vehicle.getType());
        System.out.println(p_vehicle.getChassisNumber());
        System.out.println(p_vehicle.getDocumentNumber());
        System.out.println(p_vehicle.getRegistrationNumber());
        System.out.println(p_vehicle.getRegistrationDate());
        System.out.println(p_vehicle.getDocumentExpirationDate());
        System.out.println();
    }
}
