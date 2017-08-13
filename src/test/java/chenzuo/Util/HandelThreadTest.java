package chenzuo.Util;

import org.junit.Test;

import java.net.Socket;

/**
 * Created by geek on 2017/8/7.
 */
public class HandelThreadTest {

    protected String IP = "10.1.16.91";
    protected int PORT = 5555;

    private Socket socket = null;

    @Test
    public void connection() throws Exception {
        try {
            socket = new Socket(IP, PORT);
            if (socket != null) {
                System.out.println("success connection");
            }
        } catch (Exception e) {
            System.err.println("success failed");
        }
    }

}