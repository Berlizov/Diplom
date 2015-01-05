import javax.xml.bind.JAXBException;
import java.io.IOException;
import Structs.*;
/**
 * Created by Eugene Berlizov on 16.10.2014.
 */
interface SenderInterface extends UpdateInterface {
    Packet sendMessage(Packet pack) throws IOException, JAXBException;
}
