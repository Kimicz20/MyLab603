package chenzuo;

import chenzuo.Bean.Pair;
import chenzuo.Controller.Controller;
import chenzuo.Service.ResultService;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class demo extends JFrame {

    private Logger logger = Logger.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        PropertyConfigurator.configure("src/log4j.properties");


        File file = new File("F:\\陈佐\\3.项目\\虚拟仿真平台进度\\MyLab603\\src\\xx#1.xml");
        Controller.Run(new Pair<String, File>("Function", file));
        while(true){
            try {
                System.out.println(ResultService.list.size());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    demo frame = new demo();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    /**
     * Create the frame.
     */
    public demo() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        JTextArea jta;
        jta = new JTextArea(10, 15);
        jta.setTabSize(4);
        jta.setFont(new Font("标楷体", Font.BOLD, 16));
        jta.setLineWrap(true);// 激活自动换行功能
        jta.setWrapStyleWord(true);// 激活断行不断字功能

        final JButton btnNewButton = new JButton("connect1");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.debug("connect now");

                JFileChooser jfc = new JFileChooser();
                jfc.showDialog(new JLabel(), "select");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    Controller.Run(new Pair<String, File>("Function", file));
                }
            }
        });
        btnNewButton.setBounds(29, 50, 93, 23);

        contentPane.add(jta, BorderLayout.CENTER);
        contentPane.add(btnNewButton, BorderLayout.SOUTH);


//        final JButton btnNewButton2 = new JButton("connect2");
//        btnNewButton2.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                logger.debug("connect now");
//
//                JFileChooser jfc = new JFileChooser();
//                jfc.showDialog(new JLabel(), "select");
//                File file = jfc.getSelectedFile();
//
//                if (file != null) {
//                    Controller.Run(new Pair<String, File>("performance", file));
//                    try {
//                        System.out.println(Controller.getResult("performance"));
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });
//        btnNewButton2.setBounds(29, 100, 93, 23);
//        contentPane.add(btnNewButton2);
//
//        final JButton btnNewButton3 = new JButton("connect3");
//        btnNewButton3.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                logger.debug("connect now");
//
//                JFileChooser jfc = new JFileChooser();
//                jfc.showDialog(new JLabel(), "select");
//                File file = jfc.getSelectedFile();
//
//                if (file != null) {
//                    Controller.Run(new Pair<String, File>("time", file));
//                    try {
//                        System.out.println(Controller.getResult("time"));
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    } catch (ExecutionException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });
//        btnNewButton3.setBounds(29, 150, 93, 23);
//        contentPane.add(btnNewButton3);
    }
}
