import Components.NotifyPanel;
import Structs.Packet;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Eugene Berlizov on 16.10.2014.
 */
abstract class Panel extends JLayeredPane implements SenderInterface {
    private final NotifyPanel notifyPanel = new NotifyPanel();
    private SenderInterface parentSender = null;
    private SenderInterface childSender = null;

    public Panel(SenderInterface parentSender) {
        super();
        this.parentSender = parentSender;
        setBackground(Color.WHITE);
        setOpaque(true);
    }


    protected SenderInterface getParentSender() {
        return parentSender;
    }

    public void setParentSender(SenderInterface parentSender) {
        this.parentSender = parentSender;
    }

    public void setChildSender(SenderInterface childSender) {
        this.childSender = childSender;
    }

    protected void showConnectionError() {
        showError("Не удается подключиться к серверу! Проверьте настройки.");
    }

    protected void showSuccess() {
        showNotify("Успешное выполнение операции",Constants.SUCCESS_COLOR);
    }
    protected void showError(String error) {
        showNotify(error,Constants.ERROR_COLOR);
    }
    protected void showNotify(String text,Color color) {
        remove(notifyPanel);
        add(notifyPanel, JLayeredPane.POPUP_LAYER);
        notifyPanel.setX(getWidth());
        notifyPanel.setText(text);
        notifyPanel.setBackground(color);
        notifyPanel.open();
    }
    @Override
    public Packet sendMessage(Packet pack) throws IOException, JAXBException {
        return parentSender.sendMessage(pack);
    }

    @Override
    public synchronized void update() {
        if (childSender != null) {
            childSender.update();
        }
    }
}
