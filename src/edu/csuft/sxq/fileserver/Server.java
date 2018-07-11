
package edu.csuft.sxq.fileserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网盘服务器
 * 
 * @author 宋晓琪
 *
 */
public class Server {

	/**
	 * 服务器套接字
	 */
	ServerSocket serverSocket;

	HashMap<String, String> map = new HashMap<>();

	/**
	 * 服务器端口号
	 */
	int port = 9001;

	/**
	 * 线程池
	 */
	ExecutorService pool;

	public void start() {
		// 线程池
		pool = Executors.newFixedThreadPool(9);
		pool = Executors.newSingleThreadExecutor();
		pool = Executors.newCachedThreadPool();

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("服务器启动了......");

			while (true) {
				Socket socket = serverSocket.accept();
				// 接收数据
				pool.execute(new FileUploadTask(socket, map));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
