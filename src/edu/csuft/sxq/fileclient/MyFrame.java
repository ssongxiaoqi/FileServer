package edu.csuft.sxq.fileclient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * 框架
 * 
 * @author 宋晓琪
 *
 */
public class MyFrame extends JFrame {
	private JTable ta;
	private JButton up, down, delete;
	private DefaultTableModel model;
	String str[] = { "文件名", "文件大小", "上传时间" };
	public static ArrayList<UserFile> arrayList = new ArrayList();

	Client client = new Client();// 创建一个Client对象

	/**
	 * 构造方法：定义窗口
	 */
	public MyFrame() {

		// 设置题目，大小，布局
		setTitle("文件列表");
		setSize(600, 500);
		setLayout(new BorderLayout());

		model = new DefaultTableModel(str, 0);
		ta = new JTable(model);
		ta.setShowGrid(true);
		this.add(new JScrollPane(ta)); // 添加滚动条

		// 新建一个面板
		JPanel p1 = new JPanel();
		this.add(p1, BorderLayout.SOUTH);

		// 初始化上传，下载，删除按钮
		up = new JButton("上传");
		down = new JButton("下载");
		delete = new JButton("删除");
		// 添加到面板上
		p1.add(up);
		p1.add(down);
		p1.add(delete);

		setVisible(true); // 显示

		init(); // 调用init（）方法

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					FileOutputStream fileOutput = new FileOutputStream("userfile");
					ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);
					for (int i = 0; i < arrayList.size(); i++) {
						// 向文件写入一个对象的序列化
						outputStream.writeObject((UserFile) arrayList.get(i));
					}
					outputStream.writeObject(null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);// 退出
			}
		});

	}

	/**
	 * 监听按钮响应的事件
	 */
	private void init() {
		try {
			FileInputStream fileInput = new FileInputStream("userfile");
			ObjectInputStream inputStream = new ObjectInputStream(fileInput);

			Object ob = null;
			while ((ob = inputStream.readObject()) != null) {
				UserFile userFile = (UserFile) ob;
				arrayList.add(userFile);
				model.addRow(userFile.getUserFile());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 为上传按钮添加事件监听器
		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();// 创建一个文件选择器
				fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);// 设置为只选择文件
				fileChooser.showOpenDialog(null);// 不依附于面板，可直接弹出对话框
				File file = fileChooser.getSelectedFile();// 返回得到的文件对象

				for (int i = 0; i < arrayList.size(); i++) {
					if (((UserFile) arrayList.get(i)).equals(file.getName())) {
						JOptionPane.showMessageDialog(MyFrame.this, "文件存在");
						return;
					} // 判断文件是否已经存在
				}
				if (file != null) {
					client.start(file);// 上传文件
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置时间格式
					String s[] = { file.getName(), FormetFileSize(file.length()), simpleDateFormat.format(new Date()) };// 存放上传文件的文件名，文件大小，上传时间

					UserFile userFile = new UserFile();
					userFile.fileName = file.getName();
					userFile.size = FormetFileSize(file.length());
					userFile.time = simpleDateFormat.format(new Date());
					arrayList.add(userFile);// 将文件添加到列表
					model.addRow(s);// 将记录添加到文件列表界面
				}

			}
		});

		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();// 创建一个文件选择器
				fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);// 设置为只选择路径
				fileChooser.showOpenDialog(null);// 不依附于面板，可直接弹出对话框

				File file = fileChooser.getSelectedFile();// 返回得到的文件对象
				int i = ta.getSelectedRow();// 得到选中行的序号

				if (file != null) {
					client.downFile(file, (String) model.getValueAt(i, 0));// 下载文件
					JOptionPane.showMessageDialog(MyFrame.this, "已经完成下载任务");
				}
				
			}
		});

		// 为delete按钮添加事件监听器
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int file[] = ta.getSelectedRows();// 返回选中的行的序号
				// 不为空则执行以下内容
				if (file.length > 0) {
					for (int n = 0; n <file.length; n++) {
						// 在arrayList中找到选中的文件，删除他
						for (int i = 0; i < arrayList.size(); i++) {

							if (((UserFile) arrayList.get(i)).equals((String) model.getValueAt(file[n], 0))) {
								arrayList.remove(i); // 移除文件
								model.removeRow(i); // 移除行
							}
							
						}
					}
				} else
					JOptionPane.showMessageDialog(MyFrame.this, "未选中!!!");// 如果没选中文件就弹出对话框提示
			}
		});
	}

	/**
	 * 文件大小转换器
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
}