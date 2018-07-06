package edu.csuft.sxq.fileclient;

import java.io.Serializable;

public class UserFile implements Serializable{
	public String fileName;
	public String size;
	public String time;
	
	
	public boolean equals(UserFile arg0) {
		
		return this.fileName.equals(arg0.fileName);
	}
	
	public String[] getUserFile() {
		
		String str[]= {fileName,size,time};
		return str;
	}
}
