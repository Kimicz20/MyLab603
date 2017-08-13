package chenzuo.Controller;

import chenzuo.Bean.IPNode;
import chenzuo.Bean.IPDeploy;
import chenzuo.Bean.Pair;
import chenzuo.Service.HandelThread;
import chenzuo.Service.PreConnThread;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MutliThread with Socket and Scp
 *
 * @author geek
 */
public class Controller {

    private static Logger logger = Logger.getLogger(Controller.class);

    private static long MAX_FILE_SIZE = 20 * 1024 * 1024;
    // deploy
    private static IPDeploy IP_TYPE_DEPLOY = new IPDeploy();

    // thread pool
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    // deploy and handle
    private static void handleMapping(Pair<String, File> data) {

        if (data == null) {
            logger.debug("please choose file to send!");
        }
        String type = data.getFirst();
        File[] files = {data.getSecond()};
        //big testcase deply to 2 servers
        if (files[0].length() > MAX_FILE_SIZE) {
            //1.spilt file
            files = fileSpilt(data);
            //2.choose server
            execute(type, 2, files);
        } else {
            execute(type, 1, files);
        }
    }

    /////to do
    private static File[] fileSpilt(Pair<String, File> data) {
        return null;
    }

    public static void execute(String type, int num, File[] file) {

        //pre start
        executorService.submit(new PreConnThread(new IPNode("192.168.0.131")));
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<IPNode> nodes;
        int i = 0;
        if ((nodes = IP_TYPE_DEPLOY.findNodeFree(num)) != null) {
            for (IPNode node : nodes) {
                node.setType(type);
                executorService.submit(new HandelThread(node, file[i]));
                i++;
            }
        }
    }


    public static void Close() {
        // close
        executorService.shutdown();
    }

    public static void Run(Pair<String, File> data) {
        try {
            // deploy, distribute and accept
            handleMapping(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            Close();
        }
    }
}
