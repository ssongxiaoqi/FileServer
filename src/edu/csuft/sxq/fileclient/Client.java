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
	String address="127.0.0.1";
	
	//������
	InetAddress address2;
	
	/**
	 * �˿ں�
	 */
	int port=9000;
	
    /**
     * �����ͻ���
     */
	public void start() {
		//ͨ��Э�飨��Լ��
		//TCP/IP
		//--------------
		//Ӧ�ò�     HTTP��FTP��POP/SMTP��XMPP��MQTT
		//�����     TCP��UDP
		//�����     IP
		//�����      �����ӿڵ���ع淶
		//--------------
		//����һ��TCP���׽���
		
		try {
			socket=new Socket(address, port);
			System.out.println("�ͻ��˽�������");
			
			//ѡ��һ���ļ�
			//GUI��CLI
			Scanner sc=new Scanner(System.in);
			System.out.println("����Ҫ�ϴ����ļ���");
			String file=sc.next();
			sc.close();
			
			//�����ļ�
			OutputStream out=socket.getOutputStream();//���׽��ֻ�������
			
//			FileInputStream in=new FileInputStream(file);
			BufferedInputStream in=new BufferedInputStream(new FileInputStream(file));
			byte[] buf=new byte[1024 * 4];//һ�ζ�ȡ4K����
			int size;
			
			while(-1!=(size=in.read(buf))) {
				//ʹ�ô��׽��ֻ������������͡�����
				out.write(buf, 0, size);
				//ˢ�»�����
				out.flush();
			}
			System.out.println("�ϴ����");
		
			
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
