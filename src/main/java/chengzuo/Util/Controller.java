package chengzuo.Util;

import chengzuo.Bean.Pair;
import chengzuo.Bean.TestCase;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	public static Map<String, List<TestCase>> testCaseMap = Collections
			.synchronizedMap(new HashMap<String, List<TestCase>>());

	// thread pool
	private static ExecutorService executorService = Executors.newFixedThreadPool(3);

	// result
	private static List<Future<Pair<String,List<TestCase>>>> resultList =
			new ArrayList<Future<Pair<String,List<TestCase>>>>();

	// deploy and handle
	private static void handleMapping(Map<String, File[]> files) {

		if (files == null || files.size() == 0) {
			logger.debug("please choose file to send!");
		}
		List<Pair<String, String>> rPList = IP_TYPE_DEPLOY.getDeploy();

		for (Pair<String, String> p : rPList) {
			String ip = p.getFirst();
			String type = p.getSecond();

			logger.debug("connect ip is:" + ip + " ,type is:" + type);
			File[] fs = files.get(type);
			resultList.add(executorService.submit(new HandelThread(ip, fs, type)));
		}
	}

	// handle result
	private static void handleResult() {
		for (Future<Pair<String,List<TestCase>>> fs : resultList) {
			try {
				testCaseMap.put(fs.get().getFirst(), fs.get().getSecond());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	public static void Run(Map<String, File[]> files) {

		// deploy, distribute and accept
		handleMapping(files);

		// close
		executorService.shutdown();
		
		// handle result
		handleResult();
	}

}
