package edu.csuft.sxq.fileclient;

import java.io.Serializable;

/**
 * �û��ļ�
 * 
 * @author ������
 *
 */
public class UserFile implements Serializable {

	public String fileName;
	public String size;
	public String time;

	/**
	 * equals����
	 * 
	 * @param arg0
	 * @return
	 */
	public boolean equals(String arg0) {
		return this.fileName.equals(arg0);
	}

	/**
	 * �����û��ļ������֣���С��ʱ��
	 * 
	 * @return
	 */
	public String[] getUserFile() {

		String str[] = { fileName, size, time };
		return str;
	}
}
