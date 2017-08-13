package chenzuo.Service;

import chenzuo.Bean.IPNode;
import chenzuo.Bean.Pair;
import chenzuo.Bean.TestCase;
import chenzuo.Util.ScpClientUtil;
import chenzuo.Util.TestCaseConvertUtil;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


public class HandelThread implements Callable<Pair<String,List<TestCase>>> {

	private Logger logger = Logger.getLogger(this.getClass());

	// server's IP and Port
	protected IPNode node;
	protected int PORT = 5555;

	private Socket socket = null;
    private ScpClientUtil scpclient;

	// Stream based on socket
	DataOutputStream dos = null;
	DataInputStream dis = null;

	// testcases' Files
	File[] files = null;

	public List<TestCase> testCaseList = Collections.synchronizedList(new ArrayList<TestCase>());

	public HandelThread(IPNode node, File files) {
		this(node,new File[]{files});
	}

	public HandelThread(IPNode node, File[] files) {
		this.node = node;
		this.files = files;
		scpclient = new ScpClientUtil(node.getIp());
	}

	// connect socket
	public boolean connection() {
		//prehandle start
//		scpclient.preCon();
		//connect socket
		try {
			socket = new Socket(node.getIp(), PORT);
			if (socket != null) {
				logger.debug("connection "+node.getIp() +" success");
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

		String remoteTargetDirectory= "/home/8_11_Finall/Test/testcase/";
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			for(File f:files) {
				//send file
				scpclient.putFile(f.getAbsolutePath(), f.getName(), remoteTargetDirectory, null);
				logger.debug("success putFile");
				//send filename
				dos.write(f.getName().getBytes());
				dos.flush();
			}
			logger.debug(TimeUnit.SECONDS+": success send");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// receive result
	public void recv() {
		int bufferSize = 500;
		byte[] buf = new byte[bufferSize];
		String data = "";
		try {
			dis = new DataInputStream(socket.getInputStream());
			while (dis.read(buf) != -1) {
				data = new String(buf, "UTF-8").trim();
				logger.debug("recevice data:" + data);
				if("exit".equals(data))
					break;
			}

			String remoteFile = "/home/8_11_Finall/Test/result/result.txt";
			String localTargetDirectory = "F:\\陈佐\\3.项目\\虚拟仿真平台进度\\MyLab603\\src\\main\\java\\chenzuo\\File";
			//
			long l = System.currentTimeMillis();
			scpclient.getFile(remoteFile, localTargetDirectory);
			logger.debug("file get ok,cost time is:"+(System.currentTimeMillis()-l)+" s");
			// str2model store in list
			TestCaseConvertUtil.buildTestCaseList(node.getType(), testCaseList, localTargetDirectory+"\\result.txt");
			logger.debug("success recevice");
		} catch (Exception e) {
			logger.debug("failed recevice , cause by " + e.getCause());
		}
	}

	public Pair<String,List<TestCase>> call() throws Exception {
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
		return new Pair<String,List<TestCase>>(node.getType(),testCaseList);
	}
}
