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
 * 定义文件上传具体操作，交给线程池执行的一个任务
 * 
 * @author 宋晓琪
 *
 */
public class FileUploadTask implements Runnable {

	/**
	 * 服务器与客户端通信的连接(管道)
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
				// 存储文件的路径
				String path = "d:/files";
				// 根据文件内容的散列值生成
				String filename = "";

				ByteArrayOutputStream ram = new ByteArrayOutputStream(); // 负责读文件内容
				ByteArrayOutputStream ram2 = new ByteArrayOutputStream(); // 负责读文件名
				byte[] buf = new byte[1024 * 4];
				byte[] buf2 = new byte[1024 * 4];
				int size;

				try {
					size = in.read(buf2);
					ram2.write(buf2, 0, size);
					filename = new String(ram2.toByteArray()); // 获得文件名

					outputStream.write('0'); // 向客户端写一个0
					outputStream.flush(); // 刷新

					while (-1 != (size = in.read(buf))) {
						ram.write(buf, 0, size);
					}
				} catch (Exception e) {
				}
				// 获得所有的数据
				byte[] data = ram.toByteArray();

				try (FileOutputStream out = new FileOutputStream(new File(path, filename))) {
					out.write(data); // 写入到文件中
					out.flush(); // 刷新
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
				String filename2 = new String(by).trim(); // 得到文件名, 删除空白占位符
				 
				FileInputStream fileInputStream = new FileInputStream(new File(path2, filename2));// 获得文件绝对路径
				while ((size = fileInputStream.read(by)) != -1) {
					outputStream.write(by, 0, size); // 写文件内容
					outputStream.flush(); // 刷新
				}
				socket.shutdownOutput();// 关闭套接字的输出流
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
