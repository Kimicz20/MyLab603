package chenzuo.Util.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scpclient {
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    private static String  DEFAULTCHART="UTF-8";
    private Connection conn;
    private String ip;
    private String username = "root";
    private String password = "1";
    boolean flg=false;

    public Scpclient(String IP) {
        this.ip = IP;
    }

    public Boolean login(){
        if(flg)
            return flg;
        try {
            conn = new Connection(ip);
            conn.connect();//连接
            flg=conn.authenticateWithPassword(username, password);//认证
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flg;
    }

    public void preCon(){
       execute("sh /home/8_11_Finall/start.sh");
    }

    public void close(){
        conn.close();
    }

    public String execute(String cmd){
        String result="";
        try {
            if(login()){
                Session session= conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result=processStdout(session.getStdout(),DEFAULTCHART);
                //如果为得到标准输出为空，说明脚本执行出错了
                if(StringUtils.isBlank(result)){
                    result=processStdout(session.getStderr(),DEFAULTCHART);
                }
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String processStdout(InputStream in, String charset){
        InputStream    stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout,charset));
            String line=null;
            while((line=br.readLine()) != null){
                buffer.append(line+"\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    public void getFile(String remoteFile, String localTargetDirectory) {
        try {
            if(login()){
                SCPClient client = new SCPClient(conn);
                client.get(remoteFile, localTargetDirectory);
            }
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }

    public void putFile(String localFile, String remoteFileName,String remoteTargetDirectory,String mode) {
        try {
            if(login()) {
                SCPClient client = new SCPClient(conn);
                if ((mode == null) || (mode.length() == 0)) {
                    mode = "0600";
                }
                client.put(localFile, remoteFileName, remoteTargetDirectory, mode);
            }
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }

    public static void main(String[] args) {
        Scpclient rec = new Scpclient("192.168.0.131");
        //执行命令
//        System.out.println(rec.execute("sh /home/8_11_Finall/start.sh"));

        String remoteFile = "/home/8_11_Finall/Test/result/testaa.txt";
        String localTargetDirectory = "E:\\项目\\虚拟仿真平台进度\\MyLab603\\src\\main\\java\\chenzuo\\Util\\ssh";
//        rec.getFile(remoteFile, localTargetDirectory);
        String remoteTargetDirectory= "/home/8_11_Finall/Test/testcase/";
        File f = new File("E:\\项目\\虚拟仿真平台进度\\MyLab603\\src\\main\\java\\chenzuo\\Util\\ssh\\testaa.txt");
        rec.putFile(f.getAbsolutePath(),"testaa.txt",remoteTargetDirectory,null);
    }
}