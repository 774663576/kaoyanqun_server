package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import services.ToolDao;
import utils.RESULT;
import beans.City;
import beans.College;
import beans.Major;
import cache.EncacheUtil;

import com.opensymphony.xwork2.ActionSupport;
import db.Utilities;
import embodyservices.ToolServices;

public class ToolAction extends ActionSupport {

	final String college = "_COLLEGE"; // college in cache _COLLEGE_sid_kind
	final String major = "_MAJOR"; // major in cache _MAJOR_SID_CEID
	final String city = "_CITY";// city in cache _CITY_1(pid)
	private static final long serialVersionUID = 1L;
	private Map<String, Object> container = new HashMap<String, Object>();
	ToolDao dao = new ToolServices();
	// private int sid;
	private String sid;
	private int ceid;
	private int index;
	private int pid;
	private int kind;
	private int majorid;
	private final Logger log = Logger.getLogger("action");

	public int getIndex() {
		return index;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getCeid() {
		return ceid;
	}

	public void setCeid(int ceid) {
		this.ceid = ceid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Map<String, Object> getContainer() {
		return container;
	}

	public void setContainer(Map<String, Object> container) {
		this.container = container;
	}
    
	public int getMajorid() {
		return majorid;
	}

	public void setMajorid(int majorid) {
		this.majorid = majorid;
	}

	@SuppressWarnings("unchecked")
	public String cities() {
		container.clear();
		String key = city + "_" + pid;
		CacheManager cm = EncacheUtil.getCacheManager();
		Cache cache = cm.getCache("base");
		Element em = cache.get(key);
		List<City> cities = null;
		if (null == em) {
			cities = Utilities.getCityFromProv(pid);
			cache.put(new Element(key, cities));
		} else {
			cities = (ArrayList<City>) em.getValue();
		}
		cm = null;
		container.put("cities", cities);
		if (cities.size() > 0) {
			log.debug("Select cities in province " + pid);
			container.put("result", RESULT.SUCCESS);
		} else {
			log.warn("Cannot find cities in province " + pid);
			container.put("result", RESULT.FAIL);
		}
		container.put("timestamp", System.currentTimeMillis());

		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String getcollege() {
		int ssid = 0;
		try {
			ssid = Integer.parseInt(sid);
		} catch (Exception e) {
			container.put("result", RESULT.FAIL);
			log.error("getcollege of school "+sid+" error");
			return SUCCESS;
		}
		List<College> list = null;
		String key = college + "_" + ssid + "_" + kind;
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("base");
		Element elem = cache.get(key);
		long start = System.currentTimeMillis();
		if (elem == null) {
			list = dao.getcollege(kind, ssid);
			cache.put(new Element(key, list));
		} else {
			list = (ArrayList<College>) elem.getValue();
		}
		log.info("get colleges using "+(System.currentTimeMillis()-start)+"ms");
		container.clear();
		container.put("colleges", list);
		if (list.size() > 0) {
			log.info("Select college in ungraduate  " + (0 == kind) + " id="
					+ sid);
			container.put("result", RESULT.SUCCESS);
		} else {
			log.info("Cannot find college in ungraduate  " + (0 == kind)
					+ " id=" + sid);
			container.put("result", RESULT.FAIL);
		}
		container.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String getmajor() {
		int ssid = 0;
		try {
			ssid = Integer.parseInt(sid);
		} catch (Exception e) {
			container.put("result", RESULT.FAIL);
			log.error("get majors with sid="+sid+" ,"+e.getMessage());
			return SUCCESS;
		}
		String key = major + "_" + ssid + "_" + ceid;
		List<Major> list = null;
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("base");
		Element elem = cache.get(key);
		long start = System.currentTimeMillis();
		if (elem == null) {
			list = dao.getmajor(ssid, ceid);
			if(list!= null && list.size()>0){
			cache.put(new Element(key, list));
			}
		} else {
			list = (List<Major>) elem.getValue();
		}
		log.info("get major of "+ssid+","+ceid+" using "+(System.currentTimeMillis()-start)+"ms");
		container.clear();
		container.put("major", list);
		if (list.size() > 0) {
			container.put("result", RESULT.SUCCESS);
			log.debug("select majors in school " + sid + ",college=" + ceid);
		} else {
			container.put("result", RESULT.FAIL);
			log.debug("Cannot find majors in school " + sid + ",college="
					+ ceid);
		}

		container.put("timestamp", System.currentTimeMillis());
		return "success";
	}
	
	public String getByClassify(){
		container.clear();
		String key = major+"_classify_"+ majorid;
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("base");
		Element elem = cache.get(key);
		long start = System.currentTimeMillis();
		List<Major> list = null;
		if (elem == null) {
			list = dao.getSchoolsByMajor(majorid);
			if(list != null && list.size()>0){
			 cache.put(new Element(key, list));
			}
		}else{
			list = (List<Major>) elem.getValue();
		}
		log.info("get info by classify "+majorid+" using "+(System.currentTimeMillis()-start)+" ms");
		container.put("infos", list);
		container.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

}
