package com.horstmann.violet.application.gui.util.chenzuo.Bean;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;


public class ConnectionPool {

	//list of ip
	private LinkedList<IPNode> pool = new LinkedList<IPNode>();
	
	public ConnectionPool(){
		buildFromProperties();
	}

	public void releaseConnection(IPNode connection){
		if (connection !=null){
			synchronized (pool){
				pool.addLast(connection);
				pool.notifyAll();
			}
		}
	}
	public IPNode fetchConnection(long mills) throws InterruptedException{
		synchronized (pool){
			if(mills <= 0){
				while(pool.isEmpty()){
					pool.wait();
				}
				return pool.removeFirst();
			}else{
				long future = System.currentTimeMillis() + mills;
				long remaining = mills;
				while(pool.isEmpty() && remaining >0){
					pool.wait(remaining);
					remaining = future - System.currentTimeMillis();
				}
				IPNode result =null;
				if(!pool.isEmpty()){
					result = pool.removeFirst();
				}
				return result;
			}
		}
	}

	//read properties
	private void buildFromProperties() {
		Properties prop = new Properties();
		Pair<String,String> rPair = new Pair<String,String>();
		try {
            // read .properties
			InputStream in = new BufferedInputStream(new FileInputStream("src/ip.properties"));
            // load list
			prop.load(in);
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = prop.getProperty(key);
				//read ips
				if(key.equals("ip")) {
					String[] tmpL = value.split(",");
					for(String r:tmpL) {
						IPNode node = new IPNode(r);
						pool.addLast(node);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	//test 
	public static void main(String[] args) {
		ConnectionPool p = new ConnectionPool();
	}
}
