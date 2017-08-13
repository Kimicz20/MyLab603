package chenzuo.Service;

import chenzuo.Bean.IPNode;
import chenzuo.Util.ScpClientUtil;

import java.util.concurrent.Callable;

/**
 * Created by geek on 2017/8/13.
 */
public class PreConnThread implements Callable<String>{

    private ScpClientUtil scpclient ;

    public PreConnThread(IPNode node){
        scpclient = new ScpClientUtil(node.getIp());
    }
    @Override
    public String call() throws Exception {
        return scpclient.preCon();
    }
}
