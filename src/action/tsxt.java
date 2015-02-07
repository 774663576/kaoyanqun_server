package action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class tsxt {
	public static void main(String args[]) throws IOException  
    {  
//		String name = "D:/a.txt";
//		String name1 = "D:/b.txt";
//		File file = new File(name);
//		File file1 = new File(name1);
//		FileOutputStream fos = new FileOutputStream(file);
//		FileInputStream fis = new FileInputStream(file1);
//		byte[] buffer = new byte[1024];
//		int len = 0;
//		while ((len = fis.read(buffer))  != -1) {
//			fos.write(buffer);
//			fos.flush();
//		}
//		fis.close();
//		fos.close();
//           System.out.println("name");
		
		String sfile = "d:/sssssss.jpg";
	//  String sfile = "c:\\ask.jpg";
	  String dfile ="d:/b.jpg";
	  File srcFile = new File(sfile);
	  File destFile = new File(dfile);
	  
	  try {
	   if(!destFile.exists()){
	    destFile.createNewFile();
	   }
	   FileInputStream fin = new FileInputStream(srcFile);
	   FileOutputStream fout = new FileOutputStream(destFile);
	//   在末尾追加字符
	
	        //  一次读取1024字节 
	   byte[] bytes = new byte[1024];
	   while(fin.read(bytes)!=-1){
	    fout.write(bytes);
	    fout.flush();
	   }
	   
	   fin.close();
	   fout.close();
	   
	  } catch (FileNotFoundException e) {
	   e.printStackTrace();
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
		
		
	
		
		
		
		
		
		
		
		
		
    }  
}

