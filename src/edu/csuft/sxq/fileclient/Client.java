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
 * 网盘客户端
 * 
 * @author 宋晓琪(s_xiaoqi@qq.com)
 *
 */
public class Client {

	/**
	 * 套接字：封装了网络通信的底层细节（插座） 
	 * 输出流：发送数据 
	 * 输入流：接收数据
	 */
	Socket socket;

	/**
	 * 服务器的地址
	 */
	String address = "127.0.0.1";

	/**
	 * 端口号
	 */
	int port = 9001;

	/**
	 * 启动客户端
	 * 
	 * @param file
	 */
	public void start(File file) {
		// 通信协议（契约）
		// TCP/IP
		// --------------
		// 应用层 HTTP、FTP、POP/SMTP、XMPP、MQTT
		// 传输层 TCP、UDP
		// 网络层 IP
		// 物理层 电气接口的相关规范
		// --------------
		// 创建一个TCP的套接字

		try {
			socket = new Socket(address, port);// 传入地址和端口号
			System.out.println("客户端建立连接");
		
			OutputStream outputStream = socket.getOutputStream();// 从套接字获得输出流
			outputStream.write('1');// 向服务端写一个1，代表上传文件
			outputStream.flush();// 刷新
			
			InputStream inputStream = socket.getInputStream();// 从套接字获得输入流

			if (inputStream.read() == '0') {
				outputStream.write(file.getName().getBytes());// 向服务端写入一个文件名

				if (inputStream.read() == '0') {
					// 创建一个输入流，得到选中的上传文件的文件的绝对路径的字符串
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));

					byte[] buf = new byte[1024 * 4];// 一次读取4K数据
					ByteArrayOutputStream ram = new ByteArrayOutputStream();
					int size;
					while (-1 != (size = in.read(buf))) {
						ram.write(buf, 0, size); // 使用从套接字获得输出流【发送】数据
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
					socket.close(); // 关闭套接字
				} catch (IOException e) {
					socket = null;
				}
			}
		}

	}

	/**
	 * 下载文件
	 * 
	 * @param file
	 * @param fileName
	 */
	public void downFile(File file, String fileName) {
		try {
			socket = new Socket(address, port);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write('2');// 向服务端写入一个2，代表下载文件
			outputStream.flush();// 刷新

			if (inputStream.read() == '1') {
				outputStream.write(fileName.getBytes()); // 像服务端写入文件名
				outputStream.flush();
				int size;
				byte[] by = new byte[1024 * 4];
				FileOutputStream fileOutput = new FileOutputStream(new File(file.getAbsolutePath(), fileName));// 得到选中的上传文件的文件的绝对路径的字符串
				while ((size = inputStream.read(by)) != -1) {
					fileOutput.write(by, 0, size); // 把读取的文件内容写入到文件里
				}
				fileOutput.close(); // 关闭文件输出流
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
