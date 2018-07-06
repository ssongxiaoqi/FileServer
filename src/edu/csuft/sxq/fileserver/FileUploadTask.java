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
 * 定义文件上传具体操作，交给线程池执行的一个任务
 * 
 * @author 宋晓琪
 *
 */
public class FileUploadTask implements Runnable{
	
	/**
	 * 服务器与客户端通信的连接(管道)
	 */
	Socket socket;
	
	Map<String,String> map;
	
	public FileUploadTask(Socket socket,Map<String,String> map) {
		this.socket=socket;
		this.map=map;
	}

	@Override
	public void run() {
		//存储文件的路径
		String path="d:/files";
		//根据文件内容的散列值生成
		String fileName="";
		
		//接收数据
		//可变的内存数组
		ByteArrayOutputStream ram=new ByteArrayOutputStream();
		byte[] buf=new byte[1024 * 4];
		int size;
		try(InputStream in=socket.getInputStream()) {
			
			while(-1!=(size=in.read())) {
				ram.write(buf, 0, size);
			}		
		} catch (Exception e) {
		}
		//获得所有的数据
		byte[] data=ram.toByteArray();
		
		//生产文件的消息摘要(SHA-256),使用摘要信息做文件名
		try {
			byte[] hash=MessageDigest.getInstance("SHA-256").digest(data);
			
			fileName=new BigInteger(1,hash).toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		//写入新的文件
		try (FileOutputStream out=new FileOutputStream(new File(path,fileName))){
			out.write(data);
			out.flush();
			System.out.println("成功接收："+fileName);
			
		} catch (Exception e) {
			
		}
		
	}

	
	
}
