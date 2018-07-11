package edu.csuft.sxq.fileserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * �����ļ��ϴ���������������̳߳�ִ�е�һ������
 * 
 * @author ������
 *
 */
public class FileUploadTask implements Runnable {

	/**
	 * ��������ͻ���ͨ�ŵ�����(�ܵ�)
	 */
	Socket socket;

	Map<String, String> map;

	public FileUploadTask(Socket socket, Map<String, String> map) {
		this.socket = socket;
		this.map = map;
	}

	@Override
	public void run() {
		InputStream in = null;
		OutputStream outputStream = null;
		try {
			in = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			switch (in.read()) {
			case '1':

				outputStream.write('0');
				outputStream.flush();
				// �洢�ļ���·��
				String path = "d:/files";
				// �����ļ����ݵ�ɢ��ֵ����
				String filename = "";

				ByteArrayOutputStream ram = new ByteArrayOutputStream(); // ������ļ�����
				ByteArrayOutputStream ram2 = new ByteArrayOutputStream(); // ������ļ���
				byte[] buf = new byte[1024 * 4];
				byte[] buf2 = new byte[1024 * 4];
				int size;

				try {
					size = in.read(buf2);
					ram2.write(buf2, 0, size);
					filename = new String(ram2.toByteArray()); // ����ļ���

					outputStream.write('0'); // ��ͻ���дһ��0
					outputStream.flush(); // ˢ��

					while (-1 != (size = in.read(buf))) {
						ram.write(buf, 0, size);
					}
				} catch (Exception e) {
				}
				// ������е�����
				byte[] data = ram.toByteArray();

				try (FileOutputStream out = new FileOutputStream(new File(path, filename))) {
					out.write(data); // д�뵽�ļ���
					out.flush(); // ˢ��
				} catch (Exception e) {
				}
//				System.out.println("ok");
				break;


			case '2':
				outputStream.write('1');
				outputStream.flush();
				String path2 = "d:/files";
				byte by[] = new byte[1024 * 4];
				in.read(by);
				int size2;
				String filename2 = new String(by).trim(); // �õ��ļ���, ɾ���հ�ռλ��
				 
				FileInputStream fileInputStream = new FileInputStream(new File(path2, filename2));// ����ļ�����·��
				while ((size = fileInputStream.read(by)) != -1) {
					outputStream.write(by, 0, size); // д�ļ�����
					outputStream.flush(); // ˢ��
				}
				socket.shutdownOutput();// �ر��׽��ֵ������
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
