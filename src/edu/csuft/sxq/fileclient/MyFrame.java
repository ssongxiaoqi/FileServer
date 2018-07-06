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
 * ���
 * 
 * @author ������
 *
 */
public class MyFrame extends JFrame{
	private JTable ta;
	private JButton up,down,delete;
	private DefaultTableModel model;
	String str[]= {"�ļ���","�ļ���С","�ϴ�ʱ��"};
	ArrayList<UserFile> arrayList=new ArrayList();
	
	/**
	 * ���췽�������崰��
	 */
	public MyFrame() {
		
		//������Ŀ����С������
		setTitle("�ļ��б�");
		setSize(600, 500);
		setLayout(new BorderLayout());
		
		model=new DefaultTableModel(str, 0);
		ta=new JTable(model);
		ta.setShowGrid(true);
		this.add(new JScrollPane(ta));
		
		//�½�һ�����
		JPanel p1=new JPanel();
		this.add(p1,BorderLayout.SOUTH);
		
		//��ʼ���ϴ������أ�ɾ����ť
		up=new JButton("�ϴ�");
		down=new JButton("����");
		delete=new JButton("ɾ��");
		//��ӵ������
		p1.add(up);
		p1.add(down);
		p1.add(delete);
		
		setVisible(true);//��ʾ
		
		init();
	
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					FileOutputStream fileOutput=new FileOutputStream("userfile");
					ObjectOutputStream outputStream =new ObjectOutputStream(fileOutput);
					for(int i=0;i<arrayList.size();i++) {
						//���ļ�д��һ����������л�
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
				System.exit(0);//�˳�
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
				int file[]=ta.getSelectedRows();//����ѡ�е��е����
				if(file.length>0) {
					
				}
				else
					JOptionPane.showMessageDialog(MyFrame.this,"δѡ��!!!");

					
					
				
			}
		});
		
	}

	 
	 
	public boolean isCellEditable(int row, int column) {
         return false;//��������༭
     }
	public static void main(String[] args) {
		new MyFrame();
	}

}
