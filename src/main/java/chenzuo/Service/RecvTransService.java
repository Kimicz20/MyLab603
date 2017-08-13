package chenzuo.Service;

import chenzuo.Bean.IPNode;
import chenzuo.Bean.TestCase;
import chenzuo.Controller.Controller;
import chenzuo.Util.ScpClientUtil;
import chenzuo.Util.TcConvertUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by geek on 2017/8/13.
 */
public class RecvTransService implements Callable {

    private static Logger logger = Logger.getLogger(Controller.class);

    private static ScpClientUtil scpclient;
    private IPNode node;
    private String id;

    public RecvTransService(IPNode node, String id) {
        scpclient = new ScpClientUtil(node.getIp());
        this.node = node;
        this.id = id;
    }

    private void recvTSFileAndConvert() {
        String remotePath = "/home/8_13_Finall/Test/result/";
        String fileName = "result_" + node.getType() + "_" + id + ".txt";
        String localTargetDirectory = "F:\\陈佐\\3.项目\\虚拟仿真平台进度\\MyLab603\\src\\main\\java\\chenzuo\\File\\";

        long l = System.currentTimeMillis();
        scpclient.getFile(remotePath + fileName, localTargetDirectory);
        logger.debug("file " + id + " get ok,cost time is:" + (System.currentTimeMillis() - l) + " ms");

        //delete all files
        scpclient.execute("rm -rf "+remotePath+"*");
        // str2model store in map with fileIndex
        List<TestCase> testCaseList = new ArrayList<>();
        TcConvertUtil.buildTestCaseList(node.getType(),
                testCaseList,
                localTargetDirectory + fileName);
        Controller.dataQueue.add(testCaseList);
    }

    public static void close(){
        scpclient.close();
    }

    @Override
    public Object call() throws Exception {
        recvTSFileAndConvert();
        return null;
    }
}
