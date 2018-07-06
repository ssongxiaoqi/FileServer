package edu.csuft.sxq.fileclient;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
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
	String address="127.0.0.1";
	
	//核心类
	InetAddress address2;
	
	/**
	 * 端口号
	 */
	int port=9000;
	
    /**
     * 启动客户端
     */
	public void start() {
		//通信协议（契约）
		//TCP/IP
		//--------------
		//应用层     HTTP、FTP、POP/SMTP、XMPP、MQTT
		//传输层     TCP、UDP
		//网络层     IP
		//物理层      电气接口的相关规范
		//--------------
		//创建一个TCP的套接字
		
		try {
			socket=new Socket(address, port);
			System.out.println("客户端建立连接");
			
			//选择一个文件
			//GUI、CLI
			Scanner sc=new Scanner(System.in);
			System.out.println("输入要上传的文件：");
			String file=sc.next();
			sc.close();
			
			//发送文件
			OutputStream out=socket.getOutputStream();//从套接字获得输出流
			
//			FileInputStream in=new FileInputStream(file);
			BufferedInputStream in=new BufferedInputStream(new FileInputStream(file));
			byte[] buf=new byte[1024 * 4];//一次读取4K数据
			int size;
			
			while(-1!=(size=in.read(buf))) {
				//使用从套接字获得输出流【发送】数据
				out.write(buf, 0, size);
				//刷新缓冲区
				out.flush();
			}
			System.out.println("上传完成");
		
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(socket!=null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket=null;
				}
			}				
		}
	}
}
