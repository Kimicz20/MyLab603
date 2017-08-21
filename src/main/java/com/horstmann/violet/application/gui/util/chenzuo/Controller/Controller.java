package com.horstmann.violet.application.gui.util.chenzuo.Controller;

import com.horstmann.violet.application.gui.util.chenzuo.Bean.ConnectionPool;
import com.horstmann.violet.application.gui.util.chenzuo.Bean.IPNode;
import com.horstmann.violet.application.gui.util.chenzuo.Bean.Pair;
import com.horstmann.violet.application.gui.util.chenzuo.Service.HandelService;
import com.horstmann.violet.application.gui.util.chenzuo.Service.PreConnService;
import com.horstmann.violet.application.gui.util.chenzuo.Util.FileUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * MutliThread with Socket and Scp
 *
 * @author geek
 */
public class Controller {

    private static Logger logger = Logger.getLogger(Controller.class);
    // deploy
    private static ConnectionPool pool = new ConnectionPool();
    // thread pool
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    //connect by scp
    private static boolean preCon = true;

    // deploy and handle
    private static void handleMapping(Pair<String, File> data) {
        if (data == null) {
            logger.debug("please choose file to send!");
        }
        String type = data.getFirst();
        execute(type,FileUtil.XMLSpilt(data));
    }

    public static void execute(String type, File[] file) {

        IPNode node = null;
        for (int i=0;i<file.length;i++){
            try {
                //
                node = pool.fetchConnection(TimeUnit.MICROSECONDS.toMicros(1000));
                if(node !=null){
                    try {
                        //pre start
                        if (preCon) {
                            executorService.submit(new PreConnService(node));
                            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
                        }
                        //execute handelService
                        executorService.submit(new HandelService(node,type,file[i]));
                    }finally {
                        pool.releaseConnection(node);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void Close() {
        // close
        executorService.shutdown();
    }

    public static void Run(Pair<String, File> data, boolean p) {
        preCon = p;
        Run(data);
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
