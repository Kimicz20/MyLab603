package chenzuo.Util;

import chenzuo.Bean.Pair;
import chenzuo.Bean.TestCase;
import chenzuo.Util.ssh.Scpclient;
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


public class HandelThread implements Callable<Pair<String,List<TestCase>>> {

	private Logger logger = Logger.getLogger(this.getClass());

	// server's IP and Port
	protected String IP;
	protected int PORT = 5555;

	private Socket socket = null;

	// Stream based on socket
	DataOutputStream dos = null;
	DataInputStream dis = null;

	// testcases' Files
	File[] files = null;

	// type:0,function;1,performance;2,time
	String type = null;

	public List<TestCase> testCaseList = Collections.synchronizedList(new ArrayList<TestCase>());

	public HandelThread(String ip, File files, String type) {
		this(ip,new File[]{files},type);
	}

	public HandelThread(String ip, File[] files, String type) {
		this.IP = ip;
		this.files = files;
		this.type = type;
	}

	// connect socket
	public boolean connection() {
		try {
			socket = new Socket(IP, PORT);
			if (socket != null) {
				logger.debug("success connection");
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
			dos.close();
			dis.close();
			socket.close();
			logger.debug("socket close");
		} catch (IOException e) {
			logger.error("close socket error ,cause by " + e.getMessage());
		}
	}

	// send xml files
	public void send() {

		String remoteTargetDirectory= "/home/8_11_Finall/Test/testcase/";
		Scpclient scp = Scpclient.getInstance(IP, 22,"root","1");
		try {
			dos = new DataOutputStream(socket.getOutputStream());

			for(File f:files) {
				//send file
				scp.putFile(f.getAbsolutePath(), f.getName(), remoteTargetDirectory, null);

				//send filename
				dos.write(f.getName().getBytes());
				dos.flush();

			}
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
			Scpclient scp = Scpclient.getInstance(IP, 22,"root","1");
			String remoteFile = "/home/8_11_Finall/Test/result/result.txt";
			String localTargetDirectory = "F:\\陈佐\\3.项目\\虚拟仿真平台进度\\MyLab603\\src\\main\\java\\chenzuo\\Util\\ssh";

			//
			long l = System.currentTimeMillis();
			scp.getFile(remoteFile, localTargetDirectory);
			long s = System.currentTimeMillis();
			logger.debug("begin time is:"+(s-l));
			// str2model store in list
			TestCaseConvertUtil.buildTestCaseList(type, testCaseList, localTargetDirectory+"\\result.txt");

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
		return new Pair<String,List<TestCase>>(type,testCaseList);
	}

}
