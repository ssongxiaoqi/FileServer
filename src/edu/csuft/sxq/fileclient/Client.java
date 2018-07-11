package edu.csuft.sxq.fileclient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * ���̿ͻ���
 * 
 * @author ������(s_xiaoqi@qq.com)
 *
 */
public class Client {

	/**
	 * �׽��֣���װ������ͨ�ŵĵײ�ϸ�ڣ������� 
	 * ��������������� 
	 * ����������������
	 */
	Socket socket;

	/**
	 * �������ĵ�ַ
	 */
	String address = "127.0.0.1";

	/**
	 * �˿ں�
	 */
	int port = 9001;

	/**
	 * �����ͻ���
	 * 
	 * @param file
	 */
	public void start(File file) {
		// ͨ��Э�飨��Լ��
		// TCP/IP
		// --------------
		// Ӧ�ò� HTTP��FTP��POP/SMTP��XMPP��MQTT
		// ����� TCP��UDP
		// ����� IP
		// ����� �����ӿڵ���ع淶
		// --------------
		// ����һ��TCP���׽���

		try {
			socket = new Socket(address, port);// �����ַ�Ͷ˿ں�
			System.out.println("�ͻ��˽�������");
		
			OutputStream outputStream = socket.getOutputStream();// ���׽��ֻ�������
			outputStream.write('1');// ������дһ��1�������ϴ��ļ�
			outputStream.flush();// ˢ��
			
			InputStream inputStream = socket.getInputStream();// ���׽��ֻ��������

			if (inputStream.read() == '0') {
				outputStream.write(file.getName().getBytes());// ������д��һ���ļ���

				if (inputStream.read() == '0') {
					// ����һ�����������õ�ѡ�е��ϴ��ļ����ļ��ľ���·�����ַ���
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));

					byte[] buf = new byte[1024 * 4];// һ�ζ�ȡ4K����
					ByteArrayOutputStream ram = new ByteArrayOutputStream();
					int size;
					while (-1 != (size = in.read(buf))) {
						ram.write(buf, 0, size); // ʹ�ô��׽��ֻ������������͡�����
					}
					outputStream.write(ram.toByteArray());
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close(); // �ر��׽���
				} catch (IOException e) {
					socket = null;
				}
			}
		}

	}

	/**
	 * �����ļ�
	 * 
	 * @param file
	 * @param fileName
	 */
	public void downFile(File file, String fileName) {
		try {
			socket = new Socket(address, port);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write('2');// ������д��һ��2�����������ļ�
			outputStream.flush();// ˢ��

			if (inputStream.read() == '1') {
				outputStream.write(fileName.getBytes()); // ������д���ļ���
				outputStream.flush();
				int size;
				byte[] by = new byte[1024 * 4];
				FileOutputStream fileOutput = new FileOutputStream(new File(file.getAbsolutePath(), fileName));// �õ�ѡ�е��ϴ��ļ����ļ��ľ���·�����ַ���
				while ((size = inputStream.read(by)) != -1) {
					fileOutput.write(by, 0, size); // �Ѷ�ȡ���ļ�����д�뵽�ļ���
				}
				fileOutput.close(); // �ر��ļ������
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
				}
			}
		}
	}
}
