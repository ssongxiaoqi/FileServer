
package edu.csuft.sxq.fileserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ���̷�����
 * 
 * @author ������
 *
 */
public class Server {

	/**
	 * �������׽���
	 */
	ServerSocket serverSocket;

	HashMap<String, String> map = new HashMap<>();

	/**
	 * �������˿ں�
	 */
	int port = 9001;

	/**
	 * �̳߳�
	 */
	ExecutorService pool;

	public void start() {
		// �̳߳�
		pool = Executors.newFixedThreadPool(9);
		pool = Executors.newSingleThreadExecutor();
		pool = Executors.newCachedThreadPool();

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("������������......");

			while (true) {
				Socket socket = serverSocket.accept();
				// ��������
				pool.execute(new FileUploadTask(socket, map));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
