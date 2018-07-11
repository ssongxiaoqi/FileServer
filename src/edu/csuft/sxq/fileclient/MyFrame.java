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
 * ���
 * 
 * @author ������
 *
 */
public class MyFrame extends JFrame {
	private JTable ta;
	private JButton up, down, delete;
	private DefaultTableModel model;
	String str[] = { "�ļ���", "�ļ���С", "�ϴ�ʱ��" };
	public static ArrayList<UserFile> arrayList = new ArrayList();

	Client client = new Client();// ����һ��Client����

	/**
	 * ���췽�������崰��
	 */
	public MyFrame() {

		// ������Ŀ����С������
		setTitle("�ļ��б�");
		setSize(600, 500);
		setLayout(new BorderLayout());

		model = new DefaultTableModel(str, 0);
		ta = new JTable(model);
		ta.setShowGrid(true);
		this.add(new JScrollPane(ta)); // ��ӹ�����

		// �½�һ�����
		JPanel p1 = new JPanel();
		this.add(p1, BorderLayout.SOUTH);

		// ��ʼ���ϴ������أ�ɾ����ť
		up = new JButton("�ϴ�");
		down = new JButton("����");
		delete = new JButton("ɾ��");
		// ��ӵ������
		p1.add(up);
		p1.add(down);
		p1.add(delete);

		setVisible(true); // ��ʾ

		init(); // ����init��������

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					FileOutputStream fileOutput = new FileOutputStream("userfile");
					ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);
					for (int i = 0; i < arrayList.size(); i++) {
						// ���ļ�д��һ����������л�
						outputStream.writeObject((UserFile) arrayList.get(i));
					}
					outputStream.writeObject(null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);// �˳�
			}
		});

	}

	/**
	 * ������ť��Ӧ���¼�
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
		// Ϊ�ϴ���ť����¼�������
		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();// ����һ���ļ�ѡ����
				fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);// ����Ϊֻѡ���ļ�
				fileChooser.showOpenDialog(null);// ����������壬��ֱ�ӵ����Ի���
				File file = fileChooser.getSelectedFile();// ���صõ����ļ�����

				for (int i = 0; i < arrayList.size(); i++) {
					if (((UserFile) arrayList.get(i)).equals(file.getName())) {
						JOptionPane.showMessageDialog(MyFrame.this, "�ļ�����");
						return;
					} // �ж��ļ��Ƿ��Ѿ�����
				}
				if (file != null) {
					client.start(file);// �ϴ��ļ�
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// ����ʱ���ʽ
					String s[] = { file.getName(), FormetFileSize(file.length()), simpleDateFormat.format(new Date()) };// ����ϴ��ļ����ļ������ļ���С���ϴ�ʱ��

					UserFile userFile = new UserFile();
					userFile.fileName = file.getName();
					userFile.size = FormetFileSize(file.length());
					userFile.time = simpleDateFormat.format(new Date());
					arrayList.add(userFile);// ���ļ���ӵ��б�
					model.addRow(s);// ����¼��ӵ��ļ��б����
				}

			}
		});

		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();// ����һ���ļ�ѡ����
				fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);// ����Ϊֻѡ��·��
				fileChooser.showOpenDialog(null);// ����������壬��ֱ�ӵ����Ի���

				File file = fileChooser.getSelectedFile();// ���صõ����ļ�����
				int i = ta.getSelectedRow();// �õ�ѡ���е����

				if (file != null) {
					client.downFile(file, (String) model.getValueAt(i, 0));// �����ļ�
					JOptionPane.showMessageDialog(MyFrame.this, "�Ѿ������������");
				}
				
			}
		});

		// Ϊdelete��ť����¼�������
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int file[] = ta.getSelectedRows();// ����ѡ�е��е����
				// ��Ϊ����ִ����������
				if (file.length > 0) {
					for (int n = 0; n <file.length; n++) {
						// ��arrayList���ҵ�ѡ�е��ļ���ɾ����
						for (int i = 0; i < arrayList.size(); i++) {

							if (((UserFile) arrayList.get(i)).equals((String) model.getValueAt(file[n], 0))) {
								arrayList.remove(i); // �Ƴ��ļ�
								model.removeRow(i); // �Ƴ���
							}
							
						}
					}
				} else
					JOptionPane.showMessageDialog(MyFrame.this, "δѡ��!!!");// ���ûѡ���ļ��͵����Ի�����ʾ
			}
		});
	}

	/**
	 * �ļ���Сת����
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {// ת���ļ���С
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