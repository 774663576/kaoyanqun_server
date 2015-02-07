package action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import services.ServiceInterface;
import utils.RESULT;

import com.opensymphony.xwork2.ActionSupport;

import db.DBConnector;

import embodyservices.UserDao;


public class UploadAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
    private String email;
    private File image;
    private String imageContentType;
    private String imageFileName;
    private Map<String, Object>map=new HashMap<String, Object>();
    
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String execute() {
		//System.out.println("execute filename="+this.imageFileName);
		ServiceInterface dao=new UserDao();
		String prefix=null;
		//String path=ServletActionContext.getServletContext().getRealPath("/images");
		String path = DBConnector.savepath+"images/";
	    String last="";
	    boolean has = false;
		String original =dao.getimage(email);
		if(null == original || "".equals(original)){
			prefix=email+"pc"+1;
		}else{
		
			String str=original.substring(original.lastIndexOf("pc")+2, original.lastIndexOf("."));
			int index = Integer.parseInt(str);
			prefix=email+"pc"+(index+1);
			has = true;
		}
		FileOutputStream fos = null;
		FileInputStream fis = null;
		boolean flag=false;
		try {
			//String str=new String(this.getImageFileName().getBytes("iso-8859-1"),"utf-8");
			 last+=this.getImageFileName().substring(this.getImageFileName().lastIndexOf("."));
			 System.out.println("path=["+path+prefix+last+"]");
			fos = new FileOutputStream(path+prefix+last);
			fis = new FileInputStream(this.getImage());
			byte[]buffer=new byte[1024];
			int len=0;
			while((len=fis.read(buffer))>0){
				fos.write(buffer,0,len);
			}
            flag=true;
		} catch (FileNotFoundException e) {
			map.clear();
			map.put("error", "can not find file");
			map.put("result", RESULT.FAIL);
			return INPUT;
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				fos.flush();
				fos.close();
				fis.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("error="+e.getMessage());
				e.printStackTrace();
			}
			
		}
		
		if(flag){
			map.clear();
			String preurl = DBConnector.picurl+"images/";
			  String url=preurl+prefix+last;
			if(has){
				File oo = new File(path+original.substring(original.lastIndexOf("/")+1));
				if(!oo.exists()){
					//System.out.println("file"+oo.getName()+" not exists");
				}else{
					if(oo.isFile()){
						oo.delete();
					}
				}
			}
			
			dao.insertPhoto(url, email);
			map.put("result", RESULT.SUCCESS);
			map.put("url", url);
			map.put("timestamp", System.currentTimeMillis());
		}
		
		return SUCCESS;
	}
	
	public String toConfirm() {
		//String path=ServletActionContext.getServletContext().getRealPath("/files");
		String path = DBConnector.savepath+"files/";
	    String last="";
		FileOutputStream fos = null;
		FileInputStream fis = null;
		boolean flag=false;
		try {
			 last+=this.getImageFileName().substring(this.getImageFileName().lastIndexOf("."));
			fos = new FileOutputStream(path+email+last);
			fis = new FileInputStream(this.getImage());
			byte[]buffer=new byte[1024];
			int len=0;
			while((len=fis.read(buffer))>0){
				fos.write(buffer,0,len);
			}
            flag=true;
		} catch (FileNotFoundException e) {
			map.clear();
			map.put("result", RESULT.FAIL);
			map.put("timestamp", System.currentTimeMillis());
			return INPUT;
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				fos.flush();
				fos.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(flag){
			map.clear();
			String preurl=DBConnector.picurl+"files/";
			String url=preurl+email+last;
			ServiceInterface dao=new UserDao();
			dao.toconfirm(email,url);
			map.put("result", RESULT.SUCCESS);
			map.put("timestamp", System.currentTimeMillis());
		
		}
		
		return SUCCESS;
	}
	public File getImage() {
		return image;
	}
	public void setImage(File image) {
		this.image = image;
	}
	public String getImageContentType() {
		return imageContentType;
	}
	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
    
}
