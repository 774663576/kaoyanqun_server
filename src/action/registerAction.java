package action;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import services.ServiceInterface;
import services.ToolDao;
import utils.RESULT;
import utils.StringUtils;
import utils.WebService;
import beans.Major;
import beans.Student;
import com.opensymphony.xwork2.ActionSupport;

import embodyservices.ToolServices;
import embodyservices.UserDao;


public class registerAction extends ActionSupport {

	
	private static final long serialVersionUID = 1L;
	
	final static Logger log=Logger.getLogger("action");
    private Student user;
    private Map<String, Object>map=new HashMap<String, Object>();
    private ServiceInterface dao = new UserDao();
    private Major aim;
    private String valid;
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public Student getUser() {
		return user;
	}
	public void setUser(Student user) {
		this.user = user;
	}
	
    public Major getAim() {
		return aim;
	}
	public void setAim(Major aim) {
		this.aim = aim;
	}
	
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String reg(){
    	map.clear();
    	if(!paramsValid()){
    		log.error(user.getEmail() +" register with invalid params");
    		 map.put("result", RESULT.PARAMS_ERROR);
    	}else{
    	  int result = dao.registe(user, aim);
    	  map.put("result", result);
    	}
    	/**
    	 * 200--> success
    	 * -2--> email exists
    	 * -1--> email server is invalid
    	 */    	
    	
//    	String ip=ServletActionContext.getRequest().getRemoteAddr();
//    	log.debug("user  email="+user.getEmail()+" registered with ip:"+ip);
    	map.put("timestamp", System.currentTimeMillis());
    	return SUCCESS;
    }
    public String login(){
    	map.clear();
    	String email = user.getEmail();
    	int rs=dao.login(user);
    	if(rs==RESULT.SUCCESS && user.getRole()!=5){
    		user=dao.getStudent(user.getEmail());
    		if(user != null){
    		   log.info("user "+user.getEmail()+" login successfully");
    		  dao.updatelastlogin(user);   		
      		map.put("personinfo", user);
    		}else{
    			log.error("check password ok but cannot get info for "+email);
    		}
    		
    	}
    	map.put("result", rs);
    	map.put("timestamp", System.currentTimeMillis());
    	return SUCCESS;
    }
    public String randomData(){
    	String phone = user.getEmail();
    	map.clear();
    	
    	if(dao.userExist(phone)){
    		map.put("result", RESULT.EMAIL_EXISTS);
    	}else{
         String data =getData(phone);
    	WebService.sendRandom(user.getEmail(),data);
    	dao.saveRandomData(phone, data);
    	  map.put("result", RESULT.SUCCESS);
    	log.debug("send info to user"+user.getEmail());
    	}
    	map.put("timestamp", System.currentTimeMillis());
    	return SUCCESS;
    }
    
    public String accountExists(){
    	String phone = user.getEmail();
    	map.clear();
    	if(!StringUtils.isEmpty(phone)){
    		if(dao.userExist(phone)){
    			String data = getData(phone);
    			WebService.sendRandom(phone, data);
    			dao.saveRandomData(phone, data);
    			map.put("result", RESULT.SUCCESS);
    		}else{
    			map.put("result", RESULT.EMAIL_INVALID);
    		}
    	}else{
    		map.put("result", RESULT.SUCCESS);
    	}
    	return SUCCESS;
    }
    public String checkRandom(){
       map.clear();
       map.put("result", dao.checkRandomData(user.getEmail(), valid));
       map.put("timestamp", System.currentTimeMillis());
    	return SUCCESS;
    }
    //for example:13263143391
    private String getData(String phone){
    	int pre=Integer.parseInt(phone.substring(5));//3391
    	int now = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(7));
    	String res = (pre+now)+"";
    	if(res.length()==7){
    		res = res.substring(1);
    	}
    	return res;
    }
    private boolean paramsValid(){
    	if(aim != null & aim.getSid()!=0 && aim.getMid()!=0 ){
    		ToolDao tool = new ToolServices();
    		if(!tool.validAim(aim.getSid(), aim.getCeid(), aim.getMid())){
    			return false;
    		}
    		if(user != null && !utils.StringUtils.isEmpty(user.getEmail())){
    			if(user.getSession()!=0 && !utils.StringUtils.isEmpty(user.getPassword()) && !utils.StringUtils.isEmpty(user.getNickname()) && !utils.StringUtils.isEmpty(user.getGender())){
    				try{
    					Integer.parseInt(user.getPid());
    					Integer.parseInt(user.getCity());
    				}catch(Exception e){
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }
}
