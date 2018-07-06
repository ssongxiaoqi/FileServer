package edu.csuft.sxq.fileclient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
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
public class MyFrame extends JFrame{
	private JTable ta;
	private JButton up,down,delete;
	private DefaultTableModel model;
	String str[]= {"文件名","文件大小","上传时间"};
	ArrayList<UserFile> arrayList=new ArrayList();
	
	/**
	 * 构造方法：定义窗口
	 */
	public MyFrame() {
		
		//设置题目，大小，布局
		setTitle("文件列表");
		setSize(600, 500);
		setLayout(new BorderLayout());
		
		model=new DefaultTableModel(str, 0);
		ta=new JTable(model);
		ta.setShowGrid(true);
		this.add(new JScrollPane(ta));
		
		//新建一个面板
		JPanel p1=new JPanel();
		this.add(p1,BorderLayout.SOUTH);
		
		//初始化上传，下载，删除按钮
		up=new JButton("上传");
		down=new JButton("下载");
		delete=new JButton("删除");
		//添加到面板上
		p1.add(up);
		p1.add(down);
		p1.add(delete);
		
		setVisible(true);//显示
		
		init();
	
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					FileOutputStream fileOutput=new FileOutputStream("userfile");
					ObjectOutputStream outputStream =new ObjectOutputStream(fileOutput);
					for(int i=0;i<arrayList.size();i++) {
						//向文件写入一个对象的序列化
						outputStream.writeObject((UserFile)arrayList.get(i));
					}
					outputStream.writeObject(null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);//退出
			}
		});

	}
	
	
	 private void init() {
		try {
			FileInputStream fileInput=new FileInputStream("userfile");
			ObjectInputStream inputStream=new ObjectInputStream(fileInput);
	
			Object ob=null;
			while((ob=inputStream.readObject())!=null){
				UserFile userFile=(UserFile)ob;
				arrayList.add(userFile);
				model.addRow(userFile.getUserFile());
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int file[]=ta.getSelectedRows();//返回选中的行的序号
				if(file.length>0) {
					
				}
				else
					JOptionPane.showMessageDialog(MyFrame.this,"未选中!!!");

					
					
				
			}
		});
		
	}

	 
	 
	public boolean isCellEditable(int row, int column) {
         return false;//表格不允许被编辑
     }
	public static void main(String[] args) {
		new MyFrame();
	}

}
