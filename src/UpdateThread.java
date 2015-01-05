/**
 * Created by  Eugene Berlizov on 10.11.2014.
 */
interface UpdateInterface {
    public void update();
}

public class UpdateThread extends Thread {
    private final UpdateInterface sender;

    public UpdateThread(UpdateInterface sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        super.run();
        sender.update();
    }
}