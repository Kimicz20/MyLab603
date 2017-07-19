package chengzuo;

import chengzuo.Util.Controller;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class demo extends JFrame {

	private Logger logger = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			PropertyConfigurator.configure(Class.forName(demo.class.getName()).getResourceAsStream("/log4j.properties"));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					demo frame = new demo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
		contentPane.setLayout(null);
		
		final JButton btnNewButton = new JButton("����");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.debug("connect now");
				
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(true);
				jfc.showDialog(new JLabel(), "ѡ���������"); 
				File[] files = jfc.getSelectedFiles();
				logger.debug("select files ,size = "+files.length );
				
				Map<String, File[]> fs = new HashMap();
				File[] f1 = {files[0]};
				File[] f2 = {files[1]};
				File[] f3 = {files[2]};
				fs.put("function", f1);
				fs.put("performance", f2);
				fs.put("time", f3);
				Controller.Run(fs);
				
				System.out.println(Controller.testCaseMap.size());
			}
		});
		btnNewButton.setBounds(29, 192, 93, 23);
		contentPane.add(btnNewButton);
	}
}
