package chenzuo.Util;

import chenzuo.Bean.Pair;
import chenzuo.Bean.TestCase;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

/**
 * MutliThread with Socket
 * 
 * @author geek
 *
 */
public class Controller {

	private static Logger logger = Logger.getLogger(Controller.class);

	// deploy
	private static IpDeploy IP_TYPE_DEPLOY = new IpDeploy();

	// result data
//	public static Map<String, List<TestCase>> testCaseMap = Collections
//			.synchronizedMap(new HashMap<String, List<TestCase>>());

	// thread pool
	private static ExecutorService executorService = Executors.newCachedThreadPool();

	// result
	private static Future<Pair<String,List<TestCase>>> funFuture = null;

	private static Future<Pair<String,List<TestCase>>> perFuture = null;

	private static Future<Pair<String,List<TestCase>>> timeFuture = null;

	// deploy and handle
	private static void handleMapping(Pair<String, File> data) {

		if (data == null) {
			logger.debug("please choose file to send!");
		}
		String IP = IP_TYPE_DEPLOY.getIPbyType(data.getFirst());
		String Type = data.getFirst();
		File f = data.getSecond();
		logger.debug("connect ip is:" + IP + " ,type is:" + Type);

		if(Type.equals("function")){
			funFuture = executorService.submit(new HandelThread(IP, f, Type));
		}
		if(Type.equals("performance")){
			perFuture = executorService.submit(new HandelThread(IP, f, Type));
		}
		if(Type.equals("time")){
			timeFuture = executorService.submit(new HandelThread(IP, f, Type));
		}
	}

	// handle result
//	private static void handleResult() {
//		for (Future<Pair<String,List<TestCase>>> fs : resultList) {
//			try {
//				testCaseMap.put(fs.get().getFirst(), fs.get().getSecond());
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	//return result
	public static List<TestCase> getResult(String Type) {
		try {
			if(Type.equals("function")){
                return funFuture.get().getSecond();
            }
			if(Type.equals("performance")){
                return perFuture.get().getSecond();
            }
			if(Type.equals("time")){
                return timeFuture.get().getSecond();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//close ThreadPoolExecutor
//			if(funFuture.isDone() && perFuture.isDone() &&  timeFuture.isDone()){
//				Close();
//			}
		}
		return null;
	}

	public static void Run(Pair<String, File> data) {

		// deploy, distribute and accept
		handleMapping(data);

		// handle result
//		handleResult();
	}

	public static void Close() {
		// close
		executorService.shutdown();
	}

}