package edu.csuft.sxq.fileclient;

import java.io.Serializable;

/**
 * 用户文件
 * 
 * @author 宋晓琪
 *
 */
public class UserFile implements Serializable {

	public String fileName;
	public String size;
	public String time;

	/**
	 * equals方法
	 * 
	 * @param arg0
	 * @return
	 */
	public boolean equals(String arg0) {
		return this.fileName.equals(arg0);
	}

	/**
	 * 返回用户文件的名字，大小，时间
	 * 
	 * @return
	 */
	public String[] getUserFile() {

		String str[] = { fileName, size, time };
		return str;
	}
}
