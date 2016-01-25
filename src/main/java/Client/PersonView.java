package Client;

import Client.Client.VehicleGroup;
import Generated.Vehicle;

import javax.swing.*;
import javax.xml.datatype.DatatypeConfigurationException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static Client.Client.VehicleGroup.REGISTERED;
import static Client.Client.VehicleGroup.UNREGISTERED;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Created by Dominika Salawa & Pawel Polit
 */
public class PersonView {
    private Client client;
    private PersonListView personListView;
    private int personIndex;
    private DefaultListModel<String> listModel1 =new DefaultListModel<>();
    private DefaultListModel<String> listModel2 =new DefaultListModel<>();
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int LINK_FIELD_HEIGHT = 40;
    private static final int BACK_BUTTON_WIDTH = 100;
    private static final int LINKS_LIST_WIDTH = 1000;
    private static final int LINKS_LIST_HEIGHT = 190;
    private static final int MESSAGE_WIDTH = 1000;
    private static final int MESSAGE_HEIGHT = 50;

    public PersonView(Client client,PersonListView personListView,int personIndex) throws IOException {
        this.client=client;
        this.personListView=personListView;
        this.personIndex=personIndex;
        List<String> temp=client.getPeopleInfo();
        temp.forEach(listModel1::addElement);
        JFrame window = new JFrame("Person");
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.pack();
        centreWidth(window);
        centreHeight(window);
        window.setResizable(false);
        window.setLayout(new FlowLayout());
        window.getContentPane().setBackground(Color.GRAY);

        refresh();

        JLabel jLabel1=new JLabel("Name");
        window.add(jLabel1);

        jTextField1=new JTextField(client.getPersonName(personIndex));
        jTextField1.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField1.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField1);

        JLabel jLabel2=new JLabel("Surname");
        window.add(jLabel2);

        jTextField2=new JTextField(client.getPersonSurname(personIndex));
        jTextField2.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField2.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField2);

        JLabel jLabel3=new JLabel("Pesel");
        window.add(jLabel3);

        jTextField3=new JTextField(client.getPersonPesel(personIndex));
        jTextField3.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField3.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField3);

        JLabel jLabel4=new JLabel("Email");
        window.add(jLabel4);

        jTextField4=new JTextField(client.getPersonEmail(personIndex));
        jTextField4.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField4.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField4);

        JLabel jLabel5=new JLabel("Registered vehicles");
        window.add(jLabel5);

        JButton addButton = new JButton();
        addButton.setPreferredSize(new Dimension(BACK_BUTTON_WIDTH, LINK_FIELD_HEIGHT));
        addButton.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        addButton.setText("Add");
        addButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent p_event) {
                try {
                    client.addVehicle(personIndex, REGISTERED, null, null, null, null, new Date(), new Date());
                    new VehicleView(client,PersonView.this,personIndex,listModel1.size(),REGISTERED);
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent p_event) {
            }

            @Override
            public void mouseReleased(MouseEvent p_event) {
            }

            @Override
            public void mouseEntered(MouseEvent p_event) {
            }

            @Override
            public void mouseExited(MouseEvent p_event) {
            }
        });
        window.add(addButton);

        window.add(vehiclesList(listModel1));


        JLabel jLabel6=new JLabel("Unregistered vehicles");
        window.add(jLabel6);
        JButton addButton1 = new JButton();
        addButton1.setPreferredSize(new Dimension(BACK_BUTTON_WIDTH, LINK_FIELD_HEIGHT));
        addButton1.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        addButton1.setText("Add");
        addButton1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent p_event) {
                try {
                    client.addVehicle(personIndex, UNREGISTERED, null, null, null, null, null, null);
                    new VehicleView(client,PersonView.this,personIndex,listModel2.size(),UNREGISTERED);
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent p_event) {
            }

            @Override
            public void mouseReleased(MouseEvent p_event) {
            }

            @Override
            public void mouseEntered(MouseEvent p_event) {
            }

            @Override
            public void mouseExited(MouseEvent p_event) {
            }
        });
        window.add(addButton1);

        window.add(vehiclesList(listModel2));


        JButton saveButton = new JButton();
        saveButton.setPreferredSize(new Dimension(BACK_BUTTON_WIDTH, LINK_FIELD_HEIGHT));
        saveButton.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        saveButton.setText("Save");
        saveButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent p_event) {
                client.changePersonName(personIndex,jTextField1.getText());
                client.changePersonSurname(personIndex, jTextField2.getText());
                client.changePersonPesel(personIndex, jTextField3.getText());
                client.changePersonEmail(personIndex,jTextField4.getText());
                personListView.refresh();
                window.dispose();
            }

            @Override
            public void mousePressed(MouseEvent p_event) {
            }

            @Override
            public void mouseReleased(MouseEvent p_event) {
            }

            @Override
            public void mouseEntered(MouseEvent p_event) {
            }

            @Override
            public void mouseExited(MouseEvent p_event) {
            }
        });
        window.add(saveButton);

        window.setVisible(true);
    }

    private JScrollPane vehiclesList(DefaultListModel<String> listModel) {

        JList<String> linksList = new JList<>(listModel);
        linksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        linksList.setLayoutOrientation(JList.VERTICAL);
        linksList.setFont(new Font(Font.DIALOG, Font.PLAIN, 22));
        linksList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent p_event) {
                JList links = (JList)p_event.getSource();
                int selectedIndex = links.getSelectedIndex();
                if(selectedIndex >= 0) {
                    if (listModel == listModel1) {
                        chooseFileOption(selectedIndex, REGISTERED);
                    } else {
                        chooseFileOption(selectedIndex, UNREGISTERED);
                    }

                }
            }

            @Override
            public void mousePressed(MouseEvent p_event) {
            }

            @Override
            public void mouseReleased(MouseEvent p_event) {
            }

            @Override
            public void mouseEntered(MouseEvent p_event) {
            }

            @Override
            public void mouseExited(MouseEvent p_event) {
            }
        });

        JScrollPane result = new JScrollPane(linksList);
        result.setPreferredSize(new Dimension(LINKS_LIST_WIDTH, LINKS_LIST_HEIGHT));

        return result;
    }

    private void centreWidth(Component p_component) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)(dimension.getWidth() - p_component.getWidth()) / 2;
        int y = p_component.getY();
        p_component.setLocation(x, y);
    }

    private void centreHeight(Component p_component) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = p_component.getX();
        int y = (int)(dimension.getHeight() - p_component.getHeight()) / 2;
        p_component.setLocation(x, y);
    }

    public void chooseFileOption(int vehicleIndex, VehicleGroup vehicleGroup){
        JFrame message = new JFrame("Options");
        message.getContentPane().setBackground(Color.LIGHT_GRAY);
        message.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        message.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT/2));
        message.setResizable(false);
        message.pack();
        centreWidth(message);
        centreHeight(message);
        message.setAlwaysOnTop(true);
        message.setLayout(new FlowLayout());

        JButton button1 = new JButton("Delete");
        button1.setPreferredSize(new Dimension(MESSAGE_WIDTH / 4, MESSAGE_HEIGHT / 3));
        button1.addActionListener(event -> {
            client.removeVehicle(personIndex, vehicleGroup, vehicleIndex);
            if (vehicleGroup == REGISTERED) {
                listModel1.remove(vehicleIndex);
            } else {
                listModel2.remove(vehicleIndex);
            }

            message.dispose();
        });

        message.add(button1);

        JButton button2 = new JButton("Edit");
        button2.setPreferredSize(new Dimension(MESSAGE_WIDTH / 4, MESSAGE_HEIGHT / 3));
        button2.addActionListener(event -> {
            if (vehicleGroup == REGISTERED) {
                new VehicleView(client,PersonView.this,personIndex,listModel1.size(),REGISTERED);
            } else {
                new VehicleView(client,PersonView.this,personIndex,listModel2.size(),UNREGISTERED);
            }
            message.dispose();
        });

        message.add(button2);


        message.setVisible(true);
    }

    public void refresh()
    {
        listModel1.clear();
        List<Vehicle> temp=client.getVehicles(personIndex, REGISTERED);
        temp.stream().map(vehicle -> vehicle.getType() + " " + vehicle.getChassisNumber()).forEach(listModel1::addElement);
        listModel2.clear();
        temp=client.getVehicles(personIndex, UNREGISTERED);
        temp.stream().map(vehicle -> vehicle.getType() + " " + vehicle.getChassisNumber()).forEach(listModel2::addElement);
    }

}
