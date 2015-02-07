package action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import services.DiscussisonDao;
import services.ServiceInterface;
import services.ToolDao;
import utils.RESULT;
import beans.Personinfo;
import beans.Response;
import beans.Topic;

import com.opensymphony.xwork2.ActionSupport;

import db.DBConnector;

import embodyservices.DiscussionService;
import embodyservices.ToolServices;
import embodyservices.UserDao;

public class Discussion extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private int rp;
	private int page;
	private int prid;
	private Topic topic;
	private String email;
	private String guest;
	private Response res;
    private File image;
    private String imageFileName;
	private Map<String, Object> map = new HashMap<String, Object>();
	DiscussisonDao dao = new DiscussionService();

	public String addDiscussion() {
		map.clear();
		ToolDao tool = new ToolServices();
		if(topic==null ||!tool.validAim(topic.getSid(), topic.getCeid(), topic.getMid())){
			map.put("result", RESULT.PARAMS_ERROR);
		}else{
		Personinfo info = new Personinfo();
		info.setEmail(email);
		topic.setLouzhu(info);
		int tid = dao.addDiscussion(topic);
		if (tid > 0) {
			map.put("sendtime", topic.getTimestamp());
		}
		map.put("topicid", tid);
		map.put("result", RESULT.SUCCESS);
		}
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public String getTopicList() {
		map.clear();
		int total = dao.getTopicTotal(topic.getSid(), topic.getCeid(),
				topic.getMid());
		map.put("total", total);
		if (total > 0) {
			int size = total / rp + (total % rp == 0 ? 0 : 1);
			map.put("topics",
					dao.getTopiclist(topic.getSid(), topic.getCeid(),
							topic.getMid(), page, rp));
			map.put("totalpage", size);
		}
		int flag = 0;
		if (topic.getCeid() == 0) {
			flag = 1;
		} else if (topic.getMid() == 0) {
			flag = 2;
		} else {
			flag = 3;
		}
		map.put("result", RESULT.SUCCESS);
		map.put("flag", flag);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public String doresponse() {
		map.clear();
		Personinfo host = new Personinfo();
		host.setEmail(email);
		Personinfo guestinfo = new Personinfo();
		guestinfo.setEmail(guest);
		res.setHost(host);
		res.setGuest(guestinfo);
		int rid = dao.doresponse(res,prid);
		File images=getImage();
		if(images!=null){
		String path = DBConnector.savepath+"images/";
		String prefix=null;
		String last="";		
		ServiceInterface userdao=new UserDao();
		//String path=ServletActionContext.getServletContext().getRealPath("/images");
	    boolean has = false;
		
		prefix=System.currentTimeMillis()+"";
		
		FileOutputStream fos = null;
		FileInputStream fis = null;
		boolean flag=false;
		try {
			//String str=new String(this.getImageFileName().getBytes("iso-8859-1"),"utf-8");
			 last+=this.getImageFileName().substring(this.getImageFileName().lastIndexOf("."));
			 System.out.println("path=["+path+prefix+last+"]");
			 String responsePic = path+prefix+last;
			 
			 fis = new FileInputStream(this.getImage());
			 fos = new FileOutputStream(responsePic);
			byte[]buffer=new byte[1024];
			int len=0;
			while((len=fis.read(buffer))>0){
				fos.write(buffer,0,len);
			}
            flag=true;
		} catch (Exception e){ 
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
			
			dao.saveResponsePic(rid,url);
			map.put("url", url);
		}
	}
		
		
		if (rid > 0) {
			map.put("sendtime", res.getTimestamp());
		}
		map.put("topicid", rid);
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public String getResponseList() {
		map.clear();
		
		int total = dao.getResponseTotal(res.getTid());

		map.put("total", total);
		if (total > 0) {
			map.put("responses", dao.getResponselist(res.getTid(), page, rp));
			int size = total / rp + (total % rp == 0 ? 0 : 1);
			map.put("totalpage", size);
		}
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public String getMyTopicList() {
		map.clear();
		int total = dao.getMyTopicTotal(email);
		
		map.put("total", total);

		if (total > 0) {
			map.put("topics", dao.getMyTopics(email, page, rp));
			int size = total / rp + (total % rp == 0 ? 0 : 1);
			map.put("totalpage", size);
		}
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public int getRp() {
		return rp;
	}

	public void setRp(int rp) {
		this.rp = rp;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public Response getRes() {
		return res;
	}

	public void setRes(Response res) {
		this.res = res;
	}

	public int getPrid() {
		return prid;
	}

	public void setPrid(int prid) {
		this.prid = prid;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	

}
