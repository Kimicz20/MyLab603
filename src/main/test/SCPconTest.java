import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by geek on 2017/8/12.
 */
public class SCPconTest {

    private Logger logger = Logger.getLogger(this.getClass());

    @Test
    public void scpCon(){
        Connection conn = new Connection("192.168.0.131");
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword("root",
                    "1");
            if (isAuthenticated == false) {
                logger.error("authentication failed");
            }
            String cmd = "sh /home/8_11_Finall/start.sh";
            Session session = conn.openSession();
            session.execCommand(cmd);

            conn.close();
            session.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }
}
