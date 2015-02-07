package action;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.*;

public class Upload extends HttpServlet {

    private String uploadPath = "E:\\tomcat6\\webapps\\picture"; // 用于存放上传文件的目录
    private String tempPath = "E:\\tomcat6\\webapps\\picture\\tmp"; // 用于存放临时文件的目录

public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
{
    try {
    	DiskFileUpload fu = new DiskFileUpload();
        // 设置最大文件尺寸，这里是4MB
        fu.setSizeMax(4194304);
        // 设置缓冲区大小，这里是4kb
        fu.setSizeThreshold(4096);
        // 设置临时目录：
        fu.setRepositoryPath(tempPath);

        // 得到所有的文件：
        List fileItems = fu.parseRequest(request);
        Iterator i = fileItems.iterator();
        // 依次处理每一个文件：
        while(i.hasNext()) {
            FileItem fi = (FileItem)i.next();
            // 获得文件名，这个文件名包括路径：
            String fileName = fi.getName();
            if(fileName!=null) {
                // 在这里可以记录用户和文件信息
                // ...
                // 写入文件a.txt，你也可以从fileName中提取文件名：
                fi.write(new File(uploadPath + "a.txt"));
            }
        }
        // 跳转到上传成功提示页面
    }
    catch(Exception e) {
        // 可以跳转出错页面
    }
}

	public void init() throws ServletException {
	    uploadPath = "E:\\tomcat6\\webapps\\picture";
	    tempPath = "E:\\tomcat6\\webapps\\picture\\tmp";
	    // 文件夹不存在就自动创建：
	    if(!new File(uploadPath).isDirectory())
	        new File(uploadPath).mkdirs();
	    if(!new File(tempPath).isDirectory())
	        new File(tempPath).mkdirs();
	}
}
