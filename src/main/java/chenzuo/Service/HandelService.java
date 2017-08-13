package chenzuo.Service;

import chenzuo.Bean.IPNode;
import chenzuo.Util.ScpClientUtil;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HandelService implements Callable {

    private Logger logger = Logger.getLogger(this.getClass());

    // server's IP and Port
    protected IPNode node;
    protected int PORT = 5555;

    private Socket socket = null;
    private ScpClientUtil scpclient;

    //executor to deal with receive
    private ExecutorService receiveService = Executors.newCachedThreadPool();

    // Stream based on socket
    DataOutputStream dos = null;
    DataInputStream dis = null;

    // testcases' Files
    File[] files = null;

    public HandelService(IPNode node, File files) {
        this(node, new File[]{files});
    }

    public HandelService(IPNode node, File[] files) {
        this.node = node;
        this.files = files;
        scpclient = new ScpClientUtil(node.getIp());
    }

    // connect socket
    public boolean connection() {
        //connect socket
        try {
            socket = new Socket(node.getIp(), PORT);
            if (socket != null) {
                logger.debug("connection " + node.getIp() + " success");
                return true;
            }
        } catch (Exception e) {
            logger.error("fail to connect server");
        }
        return false;
    }

    // close socket
    public void close() {
        try {
            node.setBusy(false);
            dos.close();
            dis.close();
            socket.close();
            scpclient.close();
            logger.debug("socket close");
        } catch (IOException e) {
            logger.error("close socket error ,cause by " + e.getMessage());
        }
    }

    // send xml files
    public void send() {

        String remoteTargetDirectory = "/home/8_13_Finall/Test/testcase/";
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            for (File f : files) {
                //send file
                scpclient.putFile(f.getAbsolutePath(), f.getName(), remoteTargetDirectory, null);
                //send filename
                dos.write(f.getName().getBytes());
                dos.flush();
                logger.debug("success send");
//                logger.debug("success send file:"+f.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // receive result
    public void recv() {
        int bufferSize = 500;
        boolean isContinue=true;
        byte[] buf = new byte[bufferSize];
        String data = "";
        try {
            dis = new DataInputStream(socket.getInputStream());
            while ( dis.read(buf) != -1) {
                data = new String(buf, "UTF-8").trim();
                Arrays.fill(buf, (byte) 0);
//                logger.debug("receive data:" + data);
                //get index of result file and convert
                if (data.contains("index")) {
                    receiveService.submit(new RecvTransService(node,data.split("#")[1]));
                } else if ("exit".equals(data)) {
                    logger.debug("success receive all files");
                    break;
                }
            }
        } catch (Exception e) {
            logger.debug("failed receive , cause by " + e.getCause());
        }
    }

    public Object call() throws Exception {
        // 1.create connection
        boolean isCon = connection();
        if (isCon) {
            // 2.send data
            send();
            // 3.receive result
            recv();
            // 4.close socket
            close();
        }
        return null;
    }
}
