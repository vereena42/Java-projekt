package Client;

import Client.Client.VehicleGroup;
import Generated.Vehicle;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.datatype.DatatypeConfigurationException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Created by Dominika Salawa & Pawel Polit
 */
public class VehicleView {

    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private JTextField jTextField5;
    private JTextField jTextField6;

    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int LINK_FIELD_HEIGHT = 40;
    private static final int BACK_BUTTON_WIDTH = 100;
    private static final int MESSAGE_WIDTH = 1000;
    private static final int MESSAGE_HEIGHT = 50;


    public VehicleView(Client client, PersonView personView, int personIndex, int vehicleIndex, VehicleGroup vehicleGroup) {
        JFrame window = new JFrame("Vehicle");
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.pack();
        centreWidth(window);
        centreHeight(window);
        window.setResizable(false);
        window.setLayout(new FlowLayout());
        window.getContentPane().setBackground(Color.GRAY);

        JLabel jLabel1=new JLabel("Type");
        window.add(jLabel1);

        Vehicle vehicle = client.getVehicles(personIndex, vehicleGroup).get(vehicleIndex);

        jTextField1=new JTextField(vehicle.getType());
        jTextField1.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField1.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField1);

        JLabel jLabel2=new JLabel("Chassis Number");
        window.add(jLabel2);

        jTextField2=new JTextField(vehicle.getChassisNumber());
        jTextField2.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField2.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField2);

        JLabel jLabel3=new JLabel("Registration Date");
        window.add(jLabel3);

        jTextField3=new JTextField(vehicle.getRegistrationDate().toString());
        jTextField3.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField3.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField3);

        JLabel jLabel4=new JLabel("Document Number");
        window.add(jLabel4);

        jTextField4=new JTextField(vehicle.getDocumentNumber());
        jTextField4.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField4.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField4);

        JLabel jLabel5=new JLabel("Document Expiration Date");
        window.add(jLabel5);

        jTextField5=new JTextField(vehicle.getDocumentExpirationDate().toString());
        jTextField5.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField5.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField5);

        JLabel jLabel6=new JLabel("Registration Number");
        window.add(jLabel6);

        jTextField6=new JTextField(vehicle.getRegistrationNumber());
        jTextField6.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT / 2));
        jTextField6.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        window.add(jTextField6);

        JButton saveButton = new JButton();
        saveButton.setPreferredSize(new Dimension(BACK_BUTTON_WIDTH, LINK_FIELD_HEIGHT));
        saveButton.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        saveButton.setText("Save");
        saveButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent p_event) {
                try {
                    client.changeVehicleType(personIndex,vehicleGroup,vehicleIndex,jTextField1.getText());
                    client.changeVehicleChassisNumber(personIndex, vehicleGroup, vehicleIndex, jTextField2.getText());
                    client.changeVehicleRegistrationDate(personIndex, vehicleGroup, vehicleIndex,parseDate(jTextField3.getText()));
                    client.changeVehicleDocumentNumber(personIndex, vehicleGroup, vehicleIndex, jTextField4.getText());
                    client.changeVehicleDocumentExpirationDate(personIndex, vehicleGroup, vehicleIndex, parseDate(jTextField5.getText()));
                    client.changeVehicleRegistrationNumber(personIndex, vehicleGroup, vehicleIndex, jTextField6.getText());
                    personView.refresh();
                    window.dispose();
                } catch (DatatypeConfigurationException e) {
                    printMessage("Date format is wrong! It ought to be yyyy-MM-dd");
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
        window.add(saveButton);

        window.setVisible(true);
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

    private Date parseDate(String date) throws DatatypeConfigurationException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1=simpleDateFormat.parse(date, new ParsePosition(0));
        if(date1==null)
            throw new DatatypeConfigurationException();
        return date1;
    }

    public void printMessage(String p_message) {
        JFrame message = new JFrame("Message");
        message.setUndecorated(true);
        message.getContentPane().setBackground(Color.LIGHT_GRAY);
        message.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        message.setPreferredSize(new Dimension(400, 150));
        message.setResizable(false);
        message.pack();
        centreWidth(message);
        centreHeight(message);
        message.setAlwaysOnTop(true);
        message.setLayout(new FlowLayout());

        JTextPane messageText = new JTextPane();
        messageText.setPreferredSize(new Dimension(400, 90));
        messageText.setBackground(Color.LIGHT_GRAY);
        messageText.setText(p_message);
        messageText.setEditable(false);
        messageText.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        StyledDocument styledDocument = messageText.getStyledDocument();
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(simpleAttributeSet, StyleConstants.ALIGN_CENTER);
        styledDocument.setParagraphAttributes(0, styledDocument.getLength(), simpleAttributeSet, false);

        message.add(messageText);

        JButton button = new JButton("OK");
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(event -> message.dispose());

        message.add(button);
        message.setVisible(true);
    }
}
