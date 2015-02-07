package action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import mail.MainTest;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import services.ServiceInterface;
import services.ToolDao;
import utils.CONSTANT;
import utils.RESULT;
import cache.EncacheUtil;

import com.opensymphony.xwork2.ActionSupport;

import db.SpecialDeal;
import embodyservices.ToolServices;
import embodyservices.UserDao;

import beans.Direction;
import beans.NewMajor;
import beans.Student;

public class ServiceAction extends ActionSupport {
	final String msgkey="_msssage+";
	private static final long serialVersionUID = 1L;
	private Student user;
	private NewMajor major;
	private Student friend;
	private String to;
	private String message;
	private int did;
	private int mid;
	final static Logger log=Logger.getLogger("action");	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Student getFriend() {
		return friend;
	}
	public void setFriend(Student friend) {
		this.friend = friend;
	}
    
	private Map<String, Object>map=new HashMap<String, Object>();
	private ServiceInterface dao=new UserDao();
	private File image;
	
	
	public File getImage() {
		return image;
	}
	public void setImage(File image) {
		this.image = image;
	}
	public Student getUser() {
		return user;
	}
	public void setUser(Student user) {
		this.user = user;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	
	public ServiceInterface getDao() {
		return dao;
	}
	public void setDao(ServiceInterface dao) {
		this.dao = dao;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	
	public NewMajor getMajor() {
		return major;
	}
	public void setMajor(NewMajor major) {
		this.major = major;
	}
	public String addFollow(){
		return SUCCESS;
	}
	public String deleteFollow(){
		return SUCCESS;
	}
	
	public String addBachelorInfo(){
		map.clear();
		if(user==null || user.getMajor()==null ||user.getMajor().getSid()==0){
			map.put("result", RESULT.PARAMS_ERROR);
			return SUCCESS;
		}
		if(dao.addinfo(user)){
			map.put("result",RESULT.SUCCESS);
			log.info(user.getEmail()+" update bachelor info "+user.getMajor().getSid()+","+user.getMajor().getCeid());
		}else{
			map.put("result", RESULT.FAIL);
		}
		
		return SUCCESS;
	}
	
	public String addAim(){
		map.clear();
		map.put("timestamp", System.currentTimeMillis());
		dao.addAim(user, major);
		map.put("result", RESULT.SUCCESS);
		log.info("user "+user.getEmail()+" add aim "+major.getSid()+"|"+major.getCeid());
		return SUCCESS;
	}
	public String changeAim(){
		map.clear();
		ToolDao tool = new ToolServices();
		if(user==null ||user.getSession()==0||!dao.userExist(user.getEmail())||major==null || major.getSid()==0||!tool.validAim(major.getSid(), major.getCeid(), major.getMid())){
		  map.put("result", RESULT.PARAMS_ERROR);
		}else{
		  dao.updateAim(user, major);
		  map.put("result", RESULT.SUCCESS);
		}
		map.put("timestamp", System.currentTimeMillis());
		log.info("user "+user.getEmail()+" change aim "+major.getSid()+"|"+major.getCeid());
		return SUCCESS;
	}
	
	public String addFriends(){
		map.clear();
		dao.addFriends(user.getEmail(), friend.getEmail());
		map.put("timestamp", System.currentTimeMillis());
		map.put("result", RESULT.SUCCESS);
		return SUCCESS;
	}
	public String addBlackList(){
		map.clear();
		dao.addToBlackList(user.getEmail(), friend.getEmail());
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}
	public String deleteFromBlacklist(){
		map.clear();
		dao.deleteFromBlackList(user.getEmail(), friend.getEmail());
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}
	public String deleteFriends(){
		map.clear();
		dao.deleteFriends(user.getEmail(), friend.getEmail());
		map.put("timestamp", System.currentTimeMillis());
		map.put("result", RESULT.SUCCESS);
		return SUCCESS;
	}
	public String changePassword(){
		map.clear();
		SpecialDeal deal = new SpecialDeal();
		if(dao.userExist(user.getEmail())){
			deal.changePassword(user.getEmail(), user.getPassword());
			map.put("result", RESULT.SUCCESS);
		}else{
			map.put("result", RESULT.EMAIL_INVALID);
		}
		
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}
	public String getbackPassword(){
		SpecialDeal deal = new SpecialDeal();
		String password = deal.getpassword(user);
		if(password == null){
			map.put("result", RESULT.EMAIL_INVALID);
		}else{
			MainTest sendmail = new MainTest();
			boolean rrs =true;
			 try 
		        { 
				 sendmail.send(password, user.getEmail());				
		        } catch (Exception ex) 
		        { 
		        	rrs=false;
		        } 
			 if(rrs){
				 map.put("result", RESULT.SUCCESS); 
			 }else{
				 map.put("result", RESULT.EMAIL_INVALID); 
			 }		
		}
		log.info("user "+user.getEmail()+" want to change his password");
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}
	public String updatePersoninfo(){
		map.clear();
		map.put("timestamp", System.currentTimeMillis());
		dao.updatepersoninfo(user, major);
		map.put("result", RESULT.SUCCESS);
		return SUCCESS;
	}

	public String updateLocation(){
		map.clear();
		dao.updateLocation(user);
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}
	public String getmajordetail(){
		//String ip=ServletActionContext.getRequest().getRemoteAddr();
		map.clear();
		ToolDao tool = new ToolServices();
		if(major==null ||!tool.validAim(major.getSid(), major.getCeid(), major.getMid())){
			map.put("result", RESULT.PARAMS_ERROR);
		}else{
		String key = major.getSid() + "_" + major.getCeid() + "_" + major.getMid()+"major";
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("base");
		Element elem = cache.get(key);
		if(elem==null){
		 major = dao.getmajordetail(major);
		 cache.put(new Element(key,major));
		}else{
			major = (NewMajor)elem.getValue();
		}
		map.put("detail", major);
		map.put("result", RESULT.SUCCESS);
		}
		map.put("timestamp", System.currentTimeMillis());
		log.info("get major "+major.getSid()+","+major.getCeid()+","+major.getMid()+" detail");
		return SUCCESS;
	}
	public String getDirectionDetail(){
		map.clear();
		ToolDao tool = new ToolServices();
		if(major==null||!tool.validAim(major.getSid(), major.getCeid(), major.getMid())){
			map.put("result", RESULT.PARAMS_ERROR);
		}else{
			Direction dd = null;
			String key = major.getSid() + "_" + major.getCeid() + "_" + major.getMid()+"_"+did;
			CacheManager cacheManager = EncacheUtil.getCacheManager();
			Cache cache = cacheManager.getCache("base");
			Element elem = cache.get(key);
			if(elem==null){
		     dd = dao.getDirectionInfo(major.getSid(), major.getCeid(), major.getMid(), did);
			 cache.put(new Element(key,dd));
			}else{
				dd = (Direction)elem.getValue();
			}  
		  map.put("detail", dd);
		  map.put("timestamp", System.currentTimeMillis());
		 if(dd != null){
			map.put("result", RESULT.SUCCESS);
		  }
		 }
		return SUCCESS;
	}
    public String addComments(){
		map.clear();
		dao.addcomments(user.getEmail(), message);
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}
    public String isconfirmfinish(){
    	map.clear();
    	int result = dao.confirmFinish(user.getEmail());
    	map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		map.put("confirm", result);
    	return SUCCESS;
    }
    
    public String accusation(){
    	map.clear();
    	String msg= null;
		try {
			msg=new String(message.getBytes("ISO8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("add accusation error :"+e.getMessage());
			map.put("result", RESULT.FAIL);
			return SUCCESS;
		}
    	if(dao.addAccusation(user.getEmail(), friend.getEmail(), msg)){
    		map.put("result", RESULT.SUCCESS);
    	}else{
    		map.put("result", RESULT.FAIL);
    	}
    	map.put("timestamp", System.currentTimeMillis());
    	return SUCCESS;
    }
    public String addConcern(){
    	map.clear();
    	map.put("result", dao.addOrDelConcern(user.getEmail(), major,CONSTANT.ADD));
    	map.put("timestamp", System.currentTimeMillis());
    	log.info(user.getEmail()+" add concern"+major.getSid()+major.getCeid()+","+major.getMid()+" res="+map.get("result"));
    	return SUCCESS;
    }
    public String deleteConcern(){
    	map.clear();
    	map.put("result", dao.addOrDelConcern(user.getEmail(), major, CONSTANT.DEL));
    	map.put("timestamp", System.currentTimeMillis());
    	log.info(user.getEmail()+" delete concern res="+map.get("result"));
    	return SUCCESS;
    }
    public String getConcernList(){
    	map.clear();
    	map.put("timestamp", System.currentTimeMillis());
    	map.put("concerns", dao.getConcerns(user.getEmail()));
    	return SUCCESS;
    }
    public String searchByNickname(){
    	map.clear();
    	map.put("students", dao.searchStudent(user.getNickname()));
    	map.put("timestamp", System.currentTimeMillis());
    	return SUCCESS;
    }
    public String getTiaojiList(){
    	map.clear();
    	ToolDao tool = new ToolServices();
    	map.put("majors",tool.getTiaojiList() );
    	map.put("timestamp", System.currentTimeMillis());
    	return SUCCESS;
    }
    public String getOneTiaoji(){
    	map.clear();
    	ToolDao tool = new ToolServices();
    	map.put("infos", tool.getTiaojiInfo(mid));
    	map.put("timestamp", System.currentTimeMillis());
    	return SUCCESS;
    }
    public String getTiaojis(){
    	map.clear();
    	ToolDao tool = new ToolServices();
    	map.put("dizcuz",tool.getTiaojiTiezi(major.getSid()));
    	map.put("result", RESULT.SUCCESS);
    	return SUCCESS;
    }
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	
}
