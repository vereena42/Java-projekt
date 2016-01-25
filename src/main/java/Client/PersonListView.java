package Client;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Created by Dominika Salawa & Pawel Polit
 */
public class PersonListView {
    private Client client;
    private FilesListView filesListView;
    private DefaultListModel<String> listModel=new DefaultListModel<>();
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int LINK_FIELD_HEIGHT = 40;
    private static final int BACK_BUTTON_WIDTH = 100;
    private static final int LINKS_LIST_WIDTH = 905;
    private static final int LINKS_LIST_HEIGHT = 600;
    private static final int MESSAGE_WIDTH = 400;
    private static final int MESSAGE_HEIGHT = 90;

    public PersonListView(Client client,FilesListView filesListView) throws IOException {
        this.client=client;
        this.filesListView=filesListView;
        List<String> temp=client.getPeopleInfo();
        temp.forEach(listModel::addElement);
        JFrame window = new JFrame("PersonList");
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.pack();
        centreWidth(window);
        centreHeight(window);
        window.setResizable(false);
        window.setLayout(new FlowLayout());
        window.getContentPane().setBackground(Color.GRAY);

        window.add(linksList());

        JButton addButton = new JButton();
        addButton.setPreferredSize(new Dimension(BACK_BUTTON_WIDTH, LINK_FIELD_HEIGHT));
        addButton.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        addButton.setText("Add");
        addButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent p_event) {
                client.addPerson(null, null, null, null);
                try {
                    new PersonView(client, PersonListView.this, listModel.size());
                } catch (IOException e) {
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

        JButton saveButton = new JButton();
        saveButton.setPreferredSize(new Dimension(BACK_BUTTON_WIDTH, LINK_FIELD_HEIGHT));
        saveButton.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        saveButton.setText("Save");
        saveButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent p_event) {
                try {
                    client.sendFile();
                    filesListView.refresh();
                    window.dispose();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JAXBException e) {
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
        window.add(saveButton);

        window.setVisible(true);
    }

    private JScrollPane linksList() {

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
                    chooseFileOption(selectedIndex);
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

    public void chooseFileOption(int personIndex){
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
            client.removePerson(personIndex);
            listModel.remove(personIndex);
            message.dispose();
        });

        message.add(button1);

        JButton button2 = new JButton("Edit");
        button2.setPreferredSize(new Dimension(MESSAGE_WIDTH / 4, MESSAGE_HEIGHT / 3));
        button2.addActionListener(event -> {
            try {
                new PersonView(client,this,personIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
            message.dispose();
        });

        message.add(button2);


        message.setVisible(true);
    }

    public void refresh()
    {
        listModel.clear();
        List<String> temp=client.getPeopleInfo();
        temp.forEach(listModel::addElement);
    }
}
