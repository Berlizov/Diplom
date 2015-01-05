import Structs.API;
import Structs.Packet;

import java.io.BufferedReader;

/**
 * Created by Eugene Berlizov on 08.11.2014.
 */
class UpdateProxy extends Thread {
    private final ClientConnector cc;
    private final BufferedReader in;

    public UpdateProxy(ClientConnector cc, BufferedReader in) {
        this.cc = cc;
        this.in = in;

    }

    @Override
    public void run() {
        Packet pack;
        super.run();
        try {
            do {
                pack = Packet.xmlParse(in.readLine());
                if (pack.func == API.UPDATE) {
                    System.err.println("UPDATE");
                    if (cc != null) {
                        UpdateThread t = new UpdateThread(cc);
                        t.start();
                    }
                } else {
                    if (cc != null) {
                        synchronized (cc.packetArrayList){
                            cc.packetArrayList.add(pack);
                            cc.packetArrayList.notifyAll();
                            System.out.println();
                        }
                    }
                }
            } while (true);
        } catch (Exception e) {
            return;
        }
    }

}
