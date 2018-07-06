package edu.csuft.sxq.fileserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * �����ļ��ϴ���������������̳߳�ִ�е�һ������
 * 
 * @author ������
 *
 */
public class FileUploadTask implements Runnable{
	
	/**
	 * ��������ͻ���ͨ�ŵ�����(�ܵ�)
	 */
	Socket socket;
	
	Map<String,String> map;
	
	public FileUploadTask(Socket socket,Map<String,String> map) {
		this.socket=socket;
		this.map=map;
	}

	@Override
	public void run() {
		//�洢�ļ���·��
		String path="d:/files";
		//�����ļ����ݵ�ɢ��ֵ����
		String fileName="";
		
		//��������
		//�ɱ���ڴ�����
		ByteArrayOutputStream ram=new ByteArrayOutputStream();
		byte[] buf=new byte[1024 * 4];
		int size;
		try(InputStream in=socket.getInputStream()) {
			
			while(-1!=(size=in.read())) {
				ram.write(buf, 0, size);
			}		
		} catch (Exception e) {
		}
		//������е�����
		byte[] data=ram.toByteArray();
		
		//�����ļ�����ϢժҪ(SHA-256),ʹ��ժҪ��Ϣ���ļ���
		try {
			byte[] hash=MessageDigest.getInstance("SHA-256").digest(data);
			
			fileName=new BigInteger(1,hash).toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		//д���µ��ļ�
		try (FileOutputStream out=new FileOutputStream(new File(path,fileName))){
			out.write(data);
			out.flush();
			System.out.println("�ɹ����գ�"+fileName);
			
		} catch (Exception e) {
			
		}
		
	}

	
	
}
