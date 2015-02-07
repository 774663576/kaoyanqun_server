package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import services.ServiceInterface;
import utils.CACHE;
import utils.RESULT;
import beans.Announce;
import beans.Student;
import cache.EncacheUtil;

import com.opensymphony.xwork2.ActionSupport;
import embodyservices.UserDao;

public class showAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	final String msgkey = "_msssage+";
	private List<Student> list;
	private int rp;
	private int page;
	private ServiceInterface dao = new UserDao();
	private Student user;
	private Student friend;
	private String timestamp;
	private Map<String, Object> map = new HashMap<String, Object>();
	final static Logger log = Logger.getLogger("action");
	private int target;

	/*
	 * 0-->all 1-->same major 2-->same college 3-->undergraduate
	 */
	public List<Student> getList() {
		return list;
	}

	public void setList(List<Student> list) {
		this.list = list;
	}

	public Student getFriend() {
		return friend;
	}

	public void setFriend(Student friend) {
		this.friend = friend;
	}

	public Student getUser() {
		return user;
	}

	public void setUser(Student user) {
		this.user = user;
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

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String listAll() {
		map.clear();
		String anemail = user.getEmail();
		user = dao.getStudent(user.getEmail());
		if (user != null) {
			int total = 60;
			List<Student> list = dao.lastLoginUsers(user.getEmail(), page, rp);
			for (Student student : list) {
				student.setDistance(distance(user.getLongitude(),
						user.getLatitude(), student.getLongitude(),
						student.getLatitude()));
			}
			map.put("friends", list);
			map.put("total", total);
			int size = total / rp + (total % rp == 0 ? 0 : 1);
			map.put("totalpage", size);
			map.put("result", RESULT.SUCCESS);
		} else {
			log.error("Invalid user " + anemail + " is using function listall");
			map.put("result", RESULT.EMAIL_INVALID);
		}
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public double distance(double lng1, double lat1, double lng2, double lat2) {
		if (lng1 == 0 || lat1 == 0 || lng2 == 0 || lat2 == 0) {
			return -1;
		}
		double EARTH_RADIUS = 6378137;
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1 - lng2);

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = (s * 10000) / 10000;
		return s;
	}

	public double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public String getFriends() {
		map.clear();
		int total = dao.countAll(1, user);
		map.put("total", total);
		if (total > 0) {
			user = dao.getStudent(user.getEmail());
			List<Student> list = dao.getFriends(user, page, rp);
			map.put("friends", list);
			int size = total / rp + (total % rp == 0 ? 0 : 1);
			map.put("totalpage", size);
		}
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		log.debug("user " + user.getEmail() + " find " + total
				+ "  friends here");
		return SUCCESS;
	}

	public String listFromSameSchoolSameCollege() {
		long start = System.currentTimeMillis();
		map.clear();
		String email = user.getEmail();
		user = dao.getStudent(email);
		if (user == null) {
			map.put("result", RESULT.EMAIL_INVALID);
			log.error("Invalid user " + email
					+ " is using function listfromsameschool");
		} else {
			if (user.getMajor().getSid() == 0) {
				map.put("total", 0);
			} else {
				int total = dao.countAll(3, user);
				map.put("total", total);
				if (total > 0) {
					List<Student> list = dao.fromSameSchool(user, page, rp);
					map.put("friends", list);
				}
				int size = total / rp + (total % rp == 0 ? 0 : 1);
				map.put("totalpage", size);
				long end = System.currentTimeMillis();
				log.debug("user " + email + " in school "
						+ user.getAim().getSid() + " and find " + total
						+ " schoolmates,using time " + (end - start));
			}
			map.put("result", RESULT.SUCCESS);

		}
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public String listAimAtSameSchool() {
		long start = System.currentTimeMillis();
		int total = 0;
		int op = 0;
		switch (target) {
		case 0:
			op = 4;
			break;
		case 1:
			op = 6;
			break;
		case 2:
			op = 7;
			break;
		case 3:
			op = 8;
			break;
		}
		total = dao.countAll(op, user);
		map.put("total", total);
		if (total > 0) {
			list = dao.aimatSameSchool(user, page, rp, target);
			map.put("friends", list);
		}
		long end = System.currentTimeMillis();
		log.debug("user " + user.getEmail() + " getsameaim in sid="
				+ user.getAim().getSid() + " total=" + total + " using time "
				+ (end - start));
		int size = total / rp + (total % rp == 0 ? 0 : 1);
		map.put("totalpage", size);
		map.put("timestamp", System.currentTimeMillis());
		map.put("result", RESULT.SUCCESS);
		return SUCCESS;
	}

	public String getinfo() {
		String email = user.getEmail();
		user = dao.getStudent(user.getEmail());
		map.clear();
		if (user == null) {
			map.put("result", RESULT.EMAIL_INVALID);
			log.error("Cannot get information for user " + email);
		} else {
			map.put("detail", user);
			map.put("result", RESULT.SUCCESS);
			log.debug("Get information of user " + email);
		}
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public String getfellow() {
		map.clear();
		long start = System.currentTimeMillis();
		String email = user.getEmail();
		user = dao.getStudent(user.getEmail());
		if (null != user) {
			List<Student> list = new ArrayList<Student>();
			int total = dao.countAll(2, user);
			map.put("total", total);
			if (total > 0) {
				list = dao.sameplace(user, page, rp);
				map.put("friends", list);
			}
			long end = System.currentTimeMillis();
			log.debug("user " + user.getEmail() + " get " + total
					+ " fellows using time " + (end - start));
			int size = total / rp + (total % rp == 0 ? 0 : 1);
			map.put("totalpage", size);
			map.put("result", RESULT.SUCCESS);
		} else {
			map.put("result", RESULT.EMAIL_INVALID);
			log.error("invaliduser " + email + " is using getfellow");
		}

		map.put("timestamp", System.currentTimeMillis());

		return SUCCESS;
	}

	public String getBlackList() {
		map.clear();
		int total = dao.countAll(5, user);
		map.put("total", total);
		if (total > 0) {
			List<Student> list = dao.getBlackList(user.getEmail(), page, rp);
			map.put("friends", list);
		}
		log.debug("user " + user.getEmail() + " get " + total + " in blacklist");
		int size = total / rp + (total % rp == 0 ? 0 : 1);
		map.put("totalpage", size);
		map.put("result", RESULT.SUCCESS);
		map.put("timstamp", System.currentTimeMillis());
		return SUCCESS;
	}

	/*
	 * public String getallmessage() { map.clear(); // List<Message>list =
	 * dao.getmessage(user.getEmail(), // friend.getEmail()); List<Message> list
	 * = dao.getMsgBetweenTwo(user.getEmail(), friend.getEmail());
	 * map.put("messages", list); map.put("timestamp",
	 * System.currentTimeMillis()); return SUCCESS; }
	 */
	/*
	 * @SuppressWarnings("unchecked") public String tapelist() { if (null ==
	 * user || null == user.getEmail()) { return SUCCESS; } map.clear();
	 * CacheManager cacheManager = EncacheUtil.getCacheManager(); Cache cache =
	 * cacheManager.getCache("tape"); Element elem = cache.get(msgkey +
	 * user.getEmail()); List<Support> list = null; if (null != elem) { list =
	 * (List<Support>) elem.getValue(); if (list.size() > 0) {
	 * map.put("records", list); } } else { list =
	 * dao.getTapFriends(user.getEmail(), timestamp); if (list.size() > 0) {
	 * map.put("records", list); }
	 * 
	 * } List<Support> sts = new ArrayList<Support>(); cache.put(new
	 * Element(msgkey + user.getEmail(), sts)); String guest[] = new
	 * String[list.size()]; int index = 0; StringBuffer bf = new StringBuffer();
	 * for (Support st : list) { guest[index++] = st.getEmail();
	 * bf.append(st.getEmail() + "\t"); }
	 * 
	 * dao.delFlagInTapeline(user.getEmail(), guest); cacheManager = null;
	 * 
	 * map.put("timestamp", System.currentTimeMillis()); return SUCCESS; }
	 */
	public String getAnnounce() {
		map.clear();
		List<Announce> list = dao.getAnnounces(timestamp);
		map.put("announces", list);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

}
