package chenzuo.Service;

import chenzuo.Bean.TestCase;
import chenzuo.Util.TcConvertUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by geek on 2017/8/14.
 */
public class ResultService {

    private Logger logger = Logger.getLogger(this.getClass());

    private ScheduledThreadPoolExecutor scheduledService = new ScheduledThreadPoolExecutor(1);

    private List<String> nameList = new ArrayList();
    static List<TestCase> list = Collections.synchronizedList(new ArrayList());

    public ResultService(String type) {
        scheduledService.scheduleAtFixedRate(
                new GetResult(type),
                0,
                30,
                TimeUnit.SECONDS);
    }

    class GetResult implements Runnable {

        private String type;
        int index =0;

        @Override
        public void run() {
            readfile();
        }

        public GetResult(String type) {
            this.type = type;
        }

        public void readfile() {
            ++index;
//            System.out.println("index :"+index);
            String filepath = "F:\\陈佐\\3.项目\\虚拟仿真平台进度\\MyLab603\\src\\main\\java\\chenzuo\\File\\";
            File file = new File(filepath);
            if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    if (!nameList.contains(filelist[i])) {
                        nameList.add(filelist[i]);
                        try {
                            list.addAll(TcConvertUtil.buildTestCaseList(type, filepath + filelist[i]));
                            logger.debug("list size:"+list.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(nameList.size() == 7){
                scheduledService.shutdown();
            }
        }

    }

    public static List<TestCase> getResult() {
        return list;
    }

    public static void main(String[] args) {
        ResultService s = new ResultService("Function");
    }

}
