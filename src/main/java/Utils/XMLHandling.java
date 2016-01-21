package Utils;

import Generated.ObjectFactory;
import Generated.PersonList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Dominika Salawa & Pawel Polit
 */
public class XMLHandling {

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;

    public XMLHandling(ObjectFactory p_objectFactory) throws JAXBException {
        objectFactory = p_objectFactory;
        jaxbContext = JAXBContext.newInstance(PersonList.class);
    }

    public void marshal(PersonList p_personList, String p_filePath) throws JAXBException {
        File file = new File(p_filePath);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        JAXBElement<PersonList> listJAXBElement = objectFactory.createPersonListInstance(p_personList);

        marshaller.marshal(listJAXBElement, file);
    }

    public PersonList unmarshal(String p_filePath) throws JAXBException {
        File file = new File(p_filePath);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (PersonList)unmarshaller.unmarshal(file);
    }
}
