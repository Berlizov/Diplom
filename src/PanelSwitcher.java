import Structs.API;
import Structs.Packet;
import Structs.UsersTypes;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Eugene Berlizov on 16.10.2014.
 */
@XmlRootElement(name = "SETTINGS")
class PanelSwitcher implements SenderInterface {
    private final ClientConnector connector;
    @XmlElement(name = "LOGIN")
    private String login;
    @XmlElement(name = "IP")
    private String ip;
    @XmlElement(name = "PORT")
    private int port;
    private JFrame frame;
    private SenderInterface childSender = null;


    private PanelSwitcher() {
        connector = new ClientConnector();
    }

    public static void main(String[] args) {
        PanelSwitcher m = new PanelSwitcher();
        try {
            DBConnector.connectDB();
            m.readSettings();
            m.sendMessage(new Packet(API.LOGIN,"admin","","",0));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(), "Невозможно прочитать файл настроек!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
       // m.openLoginPanel();
    }

    public void readSettings() throws IOException, JAXBException {
        File file = new File("settings.xml");
        login = "";
        ip = "127.0.0.1";
        port = 4567;
        if (!file.exists()) {
            writeSettings(ip, port, login);
        }
        JAXBContext jc = JAXBContext.newInstance(PanelSwitcher.class);
        Unmarshaller unm = jc.createUnmarshaller();
        PanelSwitcher ps = (PanelSwitcher) unm.unmarshal(file);
        login = ps.login;
        ip = ps.ip;
        port = ps.port;
    }

    public void writeSettings(String ip, int port, String login) throws IOException, JAXBException {
        this.ip = ip;
        this.port = port;
        this.login = login;
        PrintWriter writer = new PrintWriter("settings.xml", "UTF-8");
        JAXBContext jc = JAXBContext.newInstance(PanelSwitcher.class);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true );
        m.marshal(this, writer);
        writer.close();
    }

    public void openLoginPanel() {
        if(frame!=null) {
            frame.setVisible(false);
            frame.dispose();
        }
            connector.close();

        frame = new JFrame();
        LoginPanel panel = new LoginPanel(this, login, ip, port);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(300, 230);
        frame.setResizable(false);
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        panel.focusPassword();
        childSender = null;
    }

    public void openMainMenuPanel(UsersTypes type) {
        if(frame!=null) {
            frame.setVisible(false);
            frame.dispose();
        }
        frame = new JFrame();
        Panel panel = new MainMenuPanel(login, type, this);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setMinimumSize(new Dimension(670,290));
        frame.setSize(800, 600);
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        childSender = panel;
    }

    @Override
    public Packet sendMessage(Packet pack) throws IOException, JAXBException {
        if (pack.func == API.LOGIN) {
            Packet packT=connector.setup((String) pack.arguments[2], (Integer) pack.arguments[3], (String) pack.arguments[0], (String) pack.arguments[1], this);
            UsersTypes ut = (UsersTypes) packT.arguments[0];
            if (ut != UsersTypes.NO) {
                writeSettings((String) pack.arguments[2], (Integer) pack.arguments[3], (String) pack.arguments[0]);
                openMainMenuPanel(ut);
            }
            return packT;
        }
        return connector.sendMessage(pack);
    }

    @Override
    public void update() {
        if (childSender != null) {
            childSender.update();
        }
    }
}
