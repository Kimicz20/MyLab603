package com.horstmann.violet.application.gui.util.chenzuo.Util;

import com.horstmann.violet.application.gui.util.chenzuo.Bean.Constants;
import com.horstmann.violet.application.gui.util.chenzuo.Bean.Pair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by geek on 2017/8/15.
 */

public class FileUtil {

    public static String REMOTE_TC_PATH = "/home/8_13_Finall/Test/testcase/";
    public static String REMOTE_RS_PATH = "/home/8_13_Finall/Test/result/";
    public static String LOCAL_BASE_PATH = "F:\\陈佐\\3.项目\\虚拟仿真平台进度\\MyLab603\\src\\main\\java\\com.horstmann.violet.application.gui.util.chenzuo\\";
    public static String LOCAL_TARGET_PATH = LOCAL_BASE_PATH + "File\\";

    public void SetLocalPath(String path) {
        LOCAL_TARGET_PATH = path;
    }

    public static <T> List<List<T>> subList(List<T> list, int blockSize) {
        List<List<T>> lists = new ArrayList<List<T>>();
        if (list != null && blockSize > 0) {
            int listSize = list.size();
            if (listSize <= blockSize) {
                lists.add(list);
                return lists;
            }
            int batchSize = listSize / blockSize;
            int remain = listSize % blockSize;
            for (int i = 0; i < batchSize; i++) {
                int fromIndex = i * blockSize;
                int toIndex = fromIndex + blockSize;
                lists.add(list.subList(fromIndex, toIndex));
            }
            if (remain > 0) {
                lists.add(list.subList(listSize - remain, listSize));
            }
        }
        return lists;
    }

    public static File[] XMLSpilt(Pair<String, File> data) {
        File[] files = null;
        String filePath = "F:\\陈佐\\3.项目\\虚拟仿真平台进度\\MyLab603\\src\\main\\java\\TC\\";
        OutputFormat format = OutputFormat.createPrettyPrint();
        SAXReader reader = new SAXReader();
        XMLWriter writer = null;
        Document doc = null;
        try {
            //1. get all testcases
            doc = reader.read(new File(data.getSecond().getPath()));
            List elements = doc.getRootElement().elements();
            //if data is smaller ,juest return
            if(elements.size() < Constants.MAX_TC_SIZE){
                return new File[]{data.getSecond()};
            }
            List<List<Element>> lists = subList(elements, Constants.MAX_TC_SIZE);
            files = new File[lists.size()];
            int index = 0;
            for (List<Element> list : lists) {
                File f = CreateFile(filePath, "demo_" + index + ".xml");
                files[index++] = f;
                writer = new XMLWriter(
                        new FileWriter(f), format);
                Document docs = DocumentHelper.createDocument();
                Element root = docs.addElement("TCS");

                for (Element e : list) {
                    root.add((Element) e.clone());
                }
                writer.write(docs);
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static File CreateFile(String filePath, String fileName) {
//        File path = new File(filePath);
//        if (!path.exists()) {
//            path.mkdirs();
//        }
        File file = new File(filePath + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    @Test
    public void test() {
        XMLSpilt(new Pair<String, File>("function", new File("F:\\陈佐\\3.项目\\虚拟仿真平台进度\\MyLab603\\src\\1L#1.xml")));
    }
}
