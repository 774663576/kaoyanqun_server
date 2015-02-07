package embodyservices;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import cache.EncacheUtil;
import services.ServiceInterface;
import utils.CACHE;
import utils.CONSTANT;
import utils.RESULT;
import utils.StringUtils;
import utils.WebService;
import db.DBConnector;
import db.RedisOP;
import db.SpecialDeal;
import beans.Announce;
import beans.Direction;
import beans.Major;
import beans.NewMajor;
import beans.Student;
import beans.Tips;
import beans.VersionInfo;

public class UserDao implements ServiceInterface {
	final String msgkey = "_msssage+";
	SpecialDeal deal = new SpecialDeal();
	private final Logger log = Logger.getLogger("service");

	@Override
	public int login(Student stu) {
		return deal.login(stu);
	}

	@Override
	public void updatelastlogin(Student stu) {
		String sql = "update student set lastlogin=? where email=?";
		Object[] params = new Object[2];
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
		String dateString = formatter.format(currentTime);
		params[0] = dateString;
		params[1] = stu.getEmail();
		int[] types = { 0, 0 };
		DBConnector.getCount(sql, params, types, 0);
	}

	@Override
	public boolean addAim(Student stu, Major major) {
		String sql = "insert into choice(email,aimschoolid,aimcollegeid,aimmajorid,session) "
				+ " values(?,?,?,?,?)";
		Object[] params = new Object[5];
		params[0] = stu.getEmail();
		params[1] = major.getSid();
		params[2] = major.getCeid();
		params[3] = major.getMid();
		params[4] = stu.getSession();
		int[] types = new int[5];
		types[0] = 0;
		for (int i = 1; i < 5; i++)
			types[i] = 1;
		return DBConnector.getCount(sql, params, types, 0);
	}

	@Override
	public boolean updateAim(Student stu, Major major) {
		String sql = "update choice set aimschoolid=? ,aimcollegeid=?,aimmajorid=?,session=? where email=?";
		Object[] params = new Object[5];
		params[0] = major.getSid();
		params[1] = major.getCeid();
		params[2] = major.getMid();
		params[3] = stu.getSession();
		params[4] = stu.getEmail();
		int[] types = { 1, 1, 1, 1, 0 };
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("application");
		cache.remove(stu.getEmail());
		cacheManager = null;
		return DBConnector.getCount(sql, params, types, 0);
	}

	@Override
	public boolean addinfo(Student stu) {
		String sql = "update student set bachelorschoolid=?,bachelorcollegeid=? ,"
				+ " entertime=? where email=?";
		Object[] params = new Object[4];
		params[0] = stu.getMajor().getSid();
		params[1] = stu.getMajor().getCeid();
		params[2] = stu.getEnterTime();
		params[3] = stu.getEmail();
		int[] types = new int[] { 1, 1, 1, 0 };
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("application");
		cache.remove(stu.getEmail());
		cacheManager = null;
		return DBConnector.getCount(sql, params, types, 0);
	}

	@Override
	public boolean updatepersoninfo(Student stu, Major major) {
		boolean flag = false;
		String nickname = "";
		String status = "";
		String declaration = "";
		String howgoing = "";
		try {
			nickname += new String(stu.getNickname().getBytes("ISO8859-1"),
					"UTF-8");
			if (null != stu.getStatus()) {
				status += new String(stu.getStatus().getBytes("ISO8859-1"),
						"UTF-8");
			}

			// declaration += new String(stu.getDeclaration()
			// .getBytes("ISO8859-1"), "UTF-8");
			howgoing += new String(stu.getHowgoing().getBytes("ISO8859-1"),
					"UTF-8");
			if (null == stu.getScores()) {
				stu.setScores("");
			} else {
				String scores = new String(stu.getScores()
						.getBytes("ISO8859-1"), "UTF-8");
				stu.setScores(scores);
			}
		} catch (UnsupportedEncodingException e) {
			log.error("errmsg=" + e.getMessage());

		}

		if (null == stu.getStatus()) {
			stu.setStatus("");
		}
		int sid = 0;
		try {
			if (!"".equals(major.getSsid().trim())) {
				sid = Integer.parseInt(major.getSsid());
			}

		} catch (Exception e) {
			log.info("numberformat exception");
		}
		String sql = null;
		if (null != major && sid != 0) {
			sql = "update student set pid=?,cid=?,nickname=? ,"
					+ " bachelorschoolid=?,bachelorcollegeid=? , entertime=?,"
					+ " status=?,declaration=?,howgoing=?,scores=?  "
					+ " where email=?";
			Object[] params = new Object[11];
			params[0] = Integer.parseInt(stu.getPid());
			params[1] = Integer.parseInt(stu.getCity());
			params[2] = nickname;
			// params[3]= major.getSid();
			// params[4]= major.getCeid();
			params[3] = Integer.parseInt(major.getSsid());
			params[4] = Integer.parseInt(major.getSceid());
			params[5] = Integer.parseInt(stu.getSenter());
			params[6] = status;
			params[7] = declaration;
			params[8] = howgoing;
			String scores = stu.getScores();
			if (null == scores) {
				scores = new String("");
			}
			params[9] = scores;
			params[10] = stu.getEmail();

			int[] types = { 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0 };
			flag = DBConnector.getCount(sql, params, types, 0);
		} else {
			sql = "update student set pid=?,cid=?,nickname=? ,"
					+ " status=?,declaration=?,howgoing=?,scores=?  "
					+ " where email=?";
			Object[] params = new Object[8];
			params[0] = Integer.parseInt(stu.getPid());
			params[1] = Integer.parseInt(stu.getCity());
			params[2] = nickname;
			params[3] = status;
			params[4] = declaration;
			params[5] = howgoing;
			String scores = stu.getScores();
			if (null == scores) {
				scores = new String("");
			}
			params[6] = scores;
			params[7] = stu.getEmail();

			int[] types = { 1, 1, 0, 0, 0, 0, 0, 0 };
			flag = DBConnector.getCount(sql, params, types, 0);
		}
		if (flag) {
			CacheManager cacheManager = EncacheUtil.getCacheManager();
			Cache cache = cacheManager.getCache("application");
			cache.remove(stu.getEmail());
			cacheManager = null;
		}
		return flag;
	}

	@Override
	public int countAll(int op, Student stu) {
		int count = 0;
		String sql = null;
		Object[] params = null;
		int[] types = null;
		switch (op) {
		case 0:

			sql = "select count(*) from choice where email!=?";
			params = new Object[] { stu.getEmail() };
			types = new int[] { 0 };
			break;
		case 1:
			sql = "select count(*) from friends where ema=? ";
			params = new Object[1];
			params[0] = stu.getEmail();
			types = new int[1];
			types[0] = 0;
			break;
		case 2:
			sql = "select count(*) from student where pid=? and email!=?";
			params = new Object[] { Integer.parseInt(stu.getPid()),
					stu.getEmail() };
			types = new int[] { 1, 0 };
			break;
		case 3:
			sql = "select count(*) from student where bachelorschoolid =? and email!=?";
			params = new Object[] { stu.getMajor().getSid(), stu.getEmail() };
			types = new int[] { 1, 0 };
			break;
		case 4:
			sql = "select count(*) from choice where aimschoolid=? and email!=?";
			params = new Object[] { stu.getAim().getSid(), stu.getEmail() };
			types = new int[] { 1, 0 };
			break;
		case 5:
			sql = "select count(*) from blacklist where ema=? ";
			params = new Object[1];
			params[0] = stu.getEmail();
			types = new int[1];
			types[0] = 0;
			break;
		case 6:
			sql = "select count(*) from choice where aimschoolid=? and aimcollegeid=? and aimmajorid=? and email!=?";
			params = new Object[] { stu.getAim().getSid(),
					stu.getAim().getCeid(), stu.getAim().getMid(),
					stu.getEmail() };
			types = new int[] { 1, 1, 1, 0 };
			break;
		case 7:
			sql = "select count(*) from choice where aimschoolid=? and aimcollegeid=? and email!=?";
			params = new Object[] { stu.getAim().getSid(),
					stu.getAim().getCeid(), stu.getEmail() };
			types = new int[] { 1, 1, 0 };
			break;
		case 8:
			sql = "select count(*) from choice inner join student on choice.email=student.email where aimschoolid=? and (role=2 or role=1) and choice.email!=? ";
			params = new Object[] { stu.getAim().getSid(), stu.getEmail() };
			types = new int[] { 1, 0 };
			break;
		default:
			break;
		}
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < types.length; i++) {
				if (types[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(params[i]));
				} else if (types[i] == 1) {
					pstmt.setInt(i + 1, (Integer) params[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) params[i]);
				}
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return count;
	}

	@Override
	public List<Student> sameplace(Student stu, int page, int rp) {

		ResultSet rf = null;
		List<Student> list = new ArrayList<Student>();
		List<String> emailist = new ArrayList<String>();
		String sql = "select email from student "
				+ "where pid=?  order by abs(cid-?)  asc limit ?,?";
		Object[] params = new Object[4];
		params[0] = Integer.parseInt(stu.getPid());
		params[1] = Integer.parseInt(stu.getCity());
		params[2] = (page - 1) * rp;
		params[3] = rp;
		int[] types = { 1, 1, 1, 1 };
		Student ts = null;
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		Set<String> blacklist = getBlacks(stu.getEmail());
		Set<String> friends = getFriends(stu.getEmail());
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < types.length; i++) {
				if (types[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(params[i]));
				} else if (types[i] == 1) {
					pstmt.setInt(i + 1, (Integer) params[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) params[i]);
				}
			}
			rf = pstmt.executeQuery();
			String email = null;
			while (rf.next()) {
				email = rf.getString("email");
				if (email.equals(stu.getEmail())) {
					continue;
				}
				if (null != blacklist && blacklist.contains(email)) {
					continue;
				}
				emailist.add(email);
				log.debug("user " + stu.getEmail() + " find fellow " + email);
			}
			DBConnector.closeAll(rf, pstmt, null, conn);
			for (String ss : emailist) {
				ts = getStudent(ss);
				if (null == ts) {
					log.warn("cannot find student " + ss);
					continue;
				}
				if (null != friends && friends.contains(ss)) {
					ts.setRelation(1);
				}
				ts.setDistance(distance(ts.getLongitude(), ts.getLatitude(),
						stu.getLongitude(), stu.getLatitude()));
				list.add(ts);
			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeAll(rf, pstmt, null, conn);
		}
		return list;
	}

	@Override
	public List<Student> fromSameSchool(Student stu, int page, int rp) {
		List<Student> list = new ArrayList<Student>();
		List<String> emaillist = new ArrayList<String>();
		String sqlf = "select email" + " from student where "
				+ " bachelorschoolid= ?  "
				+ " order by abs(bachelorcollegeid - ?) asc limit ?,?";
		Object[] params = new Object[] { stu.getMajor().getSid(),
				stu.getMajor().getCeid(), (page - 1) * rp, rp };
		int[] types = { 1, 1, 1, 1 };
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rf = null;
		Set<String> blacklist = getBlacks(stu.getEmail());
		Set<String> friends = getFriends(stu.getEmail());
		Student ts = null;
		try {
			pstmt = conn.prepareStatement(sqlf);
			for (int i = 0; i < types.length; i++) {
				if (types[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(params[i]));
				} else if (types[i] == 1) {
					pstmt.setInt(i + 1, (Integer) params[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) params[i]);
				}
			}
			rf = pstmt.executeQuery();
			while (rf.next()) {
				String email = rf.getString("email");
				if (stu.getEmail().equals(email)) {
					continue;
				}
				if (null != blacklist && blacklist.contains(email)) {
					continue;
				}
				emaillist.add(email);
			}
			DBConnector.closeAll(rf, pstmt, null, conn);
			for (String ss : emaillist) {
				ts = getStudent(ss);
				if (null != friends && friends.contains(ss)) {
					ts.setRelation(1);
				}
				ts.setDistance(distance(ts.getLongitude(), ts.getLatitude(),
						stu.getLongitude(), stu.getLatitude()));
				list.add(ts);
			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + "" + e.getMessage());
		} finally {
			DBConnector.closeAll(rf, pstmt, null, conn);
		}

		return list;
	}

	@Override
	public List<Student> aimatSameSchool(Student stu, int page, int rp, int op) {
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rf = null;
		List<String> list = new ArrayList<String>();
		List<Student> res = new ArrayList<Student>();
		Set<String> blacklist = getBlacks(stu.getEmail());
		Set<String> friends = getFriends(stu.getEmail());
		String sql = null;
		Object[] params = null;
		int types[] = null;
		try {
			switch (op) {
			case 0:
				sql = "select email from "
						+ "choice where aimschoolid=?  "
						+ "  order by abs(aimmajorid-?)  asc,abs(aimcollegeid-?) asc limit ?,?";

				params = new Object[] { stu.getAim().getSid(),
						stu.getAim().getMid(), stu.getAim().getCeid(),
						(page - 1) * rp, rp };
				types = new int[] { 1, 1, 1, 1, 1 };
				break;
			case 1:
				sql = "select email from " + "choice where aimschoolid=?  "
						+ " and aimcollegeid=? and aimmajorid=? limit ?,?";

				params = new Object[] { stu.getAim().getSid(),
						stu.getAim().getCeid(), stu.getAim().getMid(),
						(page - 1) * rp, rp };
				types = new int[] { 1, 1, 1, 1, 1 };
				break;
			case 2:
				sql = "select email from " + "choice where aimschoolid=?  "
						+ " and aimcollegeid=? limit ?,?";

				params = new Object[] { stu.getAim().getSid(),
						stu.getAim().getCeid(), (page - 1) * rp, rp };
				types = new int[] { 1, 1, 1, 1 };
				break;
			case 3:
				sql = "select choice.email from choice inner join student on choice.email=student.email "
						+ " where aimschoolid=? and (role=2 or role=1) and choice.email!=?";
				params = new Object[] { stu.getAim().getSid(), stu.getEmail() };
				types = new int[] { 1, 0 };
				break;
			}

			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < types.length; i++) {
				if (types[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(params[i]));
				} else if (types[i] == 1) {
					pstmt.setInt(i + 1, (Integer) params[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) params[i]);
				}
			}
			rf = pstmt.executeQuery();
			while (rf.next()) {
				String email = rf.getString("email");
				if (stu.getEmail().equals(email)) {
					continue;
				}
				if (null != blacklist && blacklist.contains(email)) {
					continue;
				}
				list.add(email);
			}
			DBConnector.closeAll(rf, pstmt, null, conn);
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeAll(rf, pstmt, null, conn);
		}
		Student st = null;
		for (String email : list) {
			st = getStudent(email);
			if (null != friends && friends.contains(email)) {
				st.setRelation(1);
			}
			st.setDistance(distance(st.getLongitude(), st.getLatitude(),
					stu.getLongitude(), stu.getLatitude()));
			res.add(st);
		}
		return res.size() > 0 ? res : null;
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

	@Override
	public Student getStudent(String email) {
		Student stu = null;

		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("application");
		Element elem = cache.get(email);
		if (null != elem) {
			cacheManager = null;
			return (Student) elem.getValue();
		}

		String sqll = " select longitude,latitude,location,nickname,gender,image,"
				+ " status,howgoing,declaration,bachelorschoolid,bachelorcollegeid,"
				+ " entertime,aimschoolid,aimcollegeid,aimmajorid,deal,session,"
				+ " student.pid,student.cid,role,scores,pname,cname,kaoyanqun.university.sname,"
				+ " kaoyanqun.college.cename,mname,groupid,"
				+ " batchelorschool.sname,batchelorcollege.cename from student "
				+ " left join choice on  student.email=choice.email left join "
				+ " batchelorschool on batchelorschool.sid=student.bachelorschoolid "
				+ " left join batchelorcollege on (batchelorcollege.sid=student.bachelorschoolid and "
				+ " batchelorcollege.ceid = student.bachelorcollegeid) "
				+ " left join kaoyanqun.university on kaoyanqun.university.sid=aimschoolid "
				+ " left join kaoyanqun.college on (kaoyanqun.college.sid=aimschoolid and "
				+ " kaoyanqun.college.ceid=aimcollegeid) "
				+ " left join kaoyanqun.major on (kaoyanqun.major.sid=aimschoolid and kaoyanqun.major.ceid=aimcollegeid and kaoyanqun.major.mid=aimmajorid) "
				+ " left join province on province.pid=student.pid "
				+ " left join city on (city.pid=student.pid and city.cid=student.cid)"
				+ " where student.email=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rrs = null;
		try {
			pstmt = conn.prepareStatement(sqll);
			pstmt.setString(1, email);
			rrs = pstmt.executeQuery();
			if (rrs.next()) {
				stu = new Student();
				stu.setEmail(email);
				stu.setGender(rrs.getString("gender"));
				stu.setLongitude(rrs.getDouble("longitude"));
				stu.setLatitude(rrs.getDouble("latitude"));
				stu.setLocation(rrs.getString("location"));
				stu.setNickname(rrs.getString("nickname"));
				stu.setEnterTime(rrs.getInt("entertime"));
				stu.setImage(rrs.getString("image"));
				stu.setSession(rrs.getInt("session"));
				stu.setPid(String.valueOf(rrs.getInt("pid")));
				stu.setPname(rrs.getString("pname"));
				stu.setCname(rrs.getString("cname"));
				stu.setCity(String.valueOf(rrs.getInt("cid")));
				stu.setDeclaration(rrs.getString("declaration"));
				stu.setStatus(rrs.getString("status"));
				stu.setHowgoing(rrs.getString("howgoing"));
				stu.setRole(rrs.getInt("role"));
				stu.setScores(rrs.getString("scores"));
				stu.setConfirm(rrs.getInt("deal"));

				Major mm = new Major();
				mm.setSid(rrs.getInt("bachelorschoolid"));
				mm.setSname(rrs.getString("batchelorschool.sname"));
				mm.setCeid(rrs.getInt("bachelorcollegeid"));
				mm.setCename(rrs.getString("batchelorcollege.cename"));
				Major am = new Major();
				am.setSid(rrs.getInt("aimschoolid"));
				am.setSname(rrs.getString("university.sname"));
				am.setCeid(rrs.getInt("aimcollegeid"));
				am.setCename(rrs.getString("college.cename"));
				am.setMid(rrs.getInt("aimmajorid"));
				am.setMname(rrs.getString("mname"));
				am.setGroupid(rrs.getString("groupid"));
				stu.setMajor(mm);
				stu.setAim(am);
				stu.setEmail(email);
				DBConnector.closeAll(rrs, pstmt, null, conn);

			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeAll(rrs, pstmt, null, conn);
			cacheManager = null;
		}
		cache.put(new Element(email, stu));
		return stu;
	}

	@Override
	public void updateLocation(Student stu) {
		String location = null;
		try {
			location = new String(stu.getLocation().getBytes("ISO8859-1"),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}

		String sql = "update student set longitude=?,latitude=?,location=? where email=?";
		Object[] params = new Object[4];
		params[0] = stu.getLongitude();
		params[1] = stu.getLatitude();
		params[2] = location;
		params[3] = stu.getEmail();
		int types[] = { 2, 2, 0, 0 };
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("application");
		cache.remove(stu.getEmail());
		cacheManager = null;
		DBConnector.getCount(sql, params, types, 0);
	}

	private Set<String> getFriends(String email) {
		Set<String> set = new HashSet<String>();
		CacheManager cm = EncacheUtil.getCacheManager();
		Cache cache = cm.getCache("emails");
		Element em = cache.get(CACHE.FRIENDS + email);
		if (null != em) {
			set = (Set<String>) em.getValue();
			log.debug("get friends list in cache " + set.size());
		} else {
			String sql = "select emb from friends where ema = ?";
			Connection conn = DBConnector.Getconnect();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, email);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					set.add(rs.getString("emb"));
				}
				cache.put(new Element(CACHE.FRIENDS + email, set));
				log.debug("get friends in database ," + set.size());
			} catch (SQLException e) {
				log.error(e.getErrorCode() + ":" + e.getMessage());
			} finally {
				DBConnector.closeAll(rs, pstmt, null, conn);
				cm = null;
			}
		}
		return set.size() > 0 ? set : null;
	}

	private Set<String> getBlacks(String email) {
		Set<String> set = new HashSet<String>();
		CacheManager cm = EncacheUtil.getCacheManager();
		Cache cache = cm.getCache("emails");
		Element em = cache.get(CACHE.BLACKS + email);
		if (null != em) {
			set = (Set<String>) em.getValue();
		} else {
			String sql = "select emb from blacklist where ema = ?";
			Connection conn = DBConnector.Getconnect();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, email);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					set.add(rs.getString("emb"));
				}
				cache.put(new Element(CACHE.BLACKS + email, set));
			} catch (SQLException e) {
				log.error(e.getErrorCode() + ":" + e.getMessage());
			} finally {
				DBConnector.closeAll(rs, pstmt, null, conn);
				cm = null;
			}
		}

		return set.size() > 0 ? set : null;
	}

	@Override
	public List<Student> randinfo(Student stu, int page, int rp) {
		List<Student> list = new ArrayList<Student>();
		List<String> emaillist = new ArrayList<String>();
		Map<String, String> nicknames = new HashMap<String, String>();
		CacheManager cm = EncacheUtil.getCacheManager();
		Cache cache = cm.getCache("emails");
		Element em = cache.get(CACHE.EMAILS);
		List<String> allist = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String emaila = null;
		try {
			if (null != em) {
				allist = (List) em.getValue();
				log.debug("get stulist from cache :" + allist.toString());
			} else {
				String sql = "select choice.email,nickname from choice left join student on student.email=choice.email";
				conn = DBConnector.Getconnect();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					emaila = rs.getString(1);
					allist.add(emaila);
					nicknames.put(rs.getString(2), emaila);
				}
				cache.put(new Element(CACHE.EMAILS, allist));
				cache.put(new Element(CACHE.NICKNAMES, nicknames));
				log.debug("get stulist from sql,and put it into cache "
						+ allist.toString());
			}
			cm = null;
			int from = (page - 1) * rp;
			for (int i = 0; i < rp; i++) {
				if (from + i >= allist.size()) {
					break;
				}
				emaillist.add(allist.get(from + i));
			}
			String email = stu.getEmail();
			Student ts = null;
			Set<String> blacklist = getBlacks(email);
			Set<String> friends = getFriends(email);
			for (String ss : emaillist) {
				if (ss.equals(email)) {
					continue;
				}
				if (null != blacklist && blacklist.contains(ss)) {
					continue;
				}
				ts = getStudent(ss);
				if (null == ts) {
					log.error("cannot get info about " + ss);
					continue;
				}
				if (null != friends && friends.contains(ss)) {
					ts.setRelation(1);
				}
				ts.setDistance(distance(ts.getLongitude(), ts.getLatitude(),
						stu.getLongitude(), stu.getLatitude()));
				list.add(ts);
			}
		} catch (Exception e) {
			log.error("list all error :" + e.getMessage());
		} finally {
			cm = null;
		}
		return list;
	}

	@Override
	public List<Student> getBlackList(String email, int page, int rp) {
		List<Student> list = new ArrayList<Student>();
		String sql = "select emb from blacklist where ema=? limit ?,?";
		Object[] obj = new Object[] { email, (page - 1) * rp, rp };

		int[] tp = { 0, 1, 1 };
		Student stu = null;
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < tp.length; i++) {
				if (tp[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(obj[i]));
				} else if (tp[i] == 1) {
					pstmt.setInt(i + 1, (Integer) obj[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) obj[i]);
				}
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				stu = new Student();
				String rb = rs.getString("emb");
				stu.setEmail(rb);
				stu = this.getStudent(stu.getEmail());
				stu.setRelation(-1);
				list.add(stu);
			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeResult(rs);
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeConn(conn);
		}
		return list;
	}

	@Override
	public List<Student> getFriends(Student user, int page, int rp) {
		String email = user.getEmail();
		List<Student> list = new ArrayList<Student>();
		List<Student> res = new ArrayList<Student>();
		String sql = "select emb from friends where ema=?  limit ?,?";
		Object[] obj = new Object[3];
		obj[0] = email;
		obj[1] = (page - 1) * rp;
		obj[2] = rp;
		int[] tp = { 0, 1, 1 };
		Student stu = null;
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < tp.length; i++) {
				if (tp[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(obj[i]));
				} else if (tp[i] == 1) {
					pstmt.setInt(i + 1, (Integer) obj[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) obj[i]);
				}
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				stu = new Student();
				String rb = rs.getString("emb");
				stu.setEmail(rb);
				list.add(stu);
			}
			DBConnector.closeResult(rs);
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeConn(conn);

		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeResult(rs);
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeConn(conn);
		}
		for (Student student : list) {
			stu = getStudent(student.getEmail());
			stu.setDistance(distance(user.getLongitude(), user.getLatitude(),
					stu.getLongitude(), stu.getLatitude()));
			stu.setRelation(1);
			res.add(stu);
		}
		return res.size() > 0 ? res : null;
	}

	@Override
	public void addFriends(String from, String to) {
		log.info(from + " add friends with " + to);
		String sql = "insert into friends(ema,emb) values(?,?)";
		Object[] params = new Object[2];
		params[0] = from;
		params[1] = to;
		int[] types = { 0, 0 };

		CacheManager cm = null;

		try {
			DBConnector.getCount(sql, params, types, 0);
			cm = EncacheUtil.getCacheManager();
			Cache cache = cm.getCache("emails");
			Element em = cache.get(CACHE.FRIENDS + from);
			if (null != em) {
				Set<String> set = (Set<String>) em.getValue();
				set.add(to);
			}
		} catch (Exception e) {
			log.error("addfriends error:" + e.getMessage());
		} finally {
			cm = null;
		}
	}

	@Override
	public void addToBlackList(String from, String to) {
		if (isFriends(from, to)) {
			deleteFriends(from, to);
		}
		String sql = "insert into blacklist(ema,emb) values(?,?)";
		Object[] params = new Object[2];
		params[0] = from;
		params[1] = to;
		int types[] = { 0, 0 };
		DBConnector.getCount(sql, params, types, 0);
		CacheManager cm = EncacheUtil.getCacheManager();
		Cache cache = cm.getCache("emails");
		Element em = cache.get(CACHE.BLACKS + from);
		if (null != em) {
			Set<String> set = (Set<String>) em.getValue();
			set.add(to);
		}
		cm = null;
	}

	@Override
	public void deleteFromBlackList(String from, String to) {
		String sql = "delete from blacklist where ema=? and emb=?";
		Object[] params = new Object[2];
		params[0] = from;
		params[1] = to;
		int[] types = { 0, 0 };
		CacheManager cm = EncacheUtil.getCacheManager();
		Cache cache = cm.getCache("emails");
		Element em = cache.get(CACHE.BLACKS + from);
		if (null != em) {
			Set<String> set = (Set<String>) em.getValue();
			set.remove(to);
		}
		cm = null;
		DBConnector.getCount(sql, params, types, 0);
	}

	@Override
	public void deleteFriends(String from, String to) {
		String sql = "delete from friends where ema=? and emb=?";
		Object[] params = new Object[2];
		params[0] = from;
		params[1] = to;
		int[] types = { 0, 0 };
		CacheManager cm = EncacheUtil.getCacheManager();
		Cache cache = cm.getCache("emails");
		Element em = cache.get(CACHE.BLACKS + from);
		if (null != em) {
			Set<String> set = (Set<String>) em.getValue();
			set.remove(to);
		}
		cm = null;
		DBConnector.getCount(sql, params, types, 0);
	}

	public boolean isFriends(String host, String guest) {
		boolean flag = false;
		String friends = "select count(*) from friends where ema=? and emb=?";
		Object ff[] = new Object[2];
		ff[0] = host;
		ff[1] = guest;
		int[] ttf = { 0, 0 };
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet fs = null;
		try {
			pstmt = conn.prepareStatement(friends);
			for (int i = 0; i < ttf.length; i++) {
				if (ttf[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(ff[i]));
				} else if (ttf[i] == 1) {
					pstmt.setInt(i + 1, (Integer) ff[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) ff[i]);
				}
			}
			fs = pstmt.executeQuery();
			if (fs.next()) {
				int tc = fs.getInt(1);
				if (tc == 1) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeAll(fs, pstmt, null, conn);
		}
		return flag;
	}

	public boolean isBlack(String host, String guest) {
		boolean flag = false;
		String enemies = "select count(*) from blacklist where ema=? and emb=?";
		Object ff[] = new Object[2];
		ff[0] = host;
		ff[1] = guest;
		int[] ttf = { 0, 0 };
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet fs = null;
		try {
			pstmt = conn.prepareStatement(enemies);
			for (int i = 0; i < ttf.length; i++) {
				if (ttf[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(ff[i]));
				} else if (ttf[i] == 1) {
					pstmt.setInt(i + 1, (Integer) ff[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) ff[i]);
				}
			}
			fs = pstmt.executeQuery();
			if (fs.next()) {
				int tc = fs.getInt(1);
				if (tc == 1) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeAll(fs, pstmt, null, conn);
		}

		return flag;
	}

	@Override
	public void insertPhoto(String path, String email) {
		String sql = "update student set image=? where email=?";
		Object[] params = new Object[2];
		params[0] = path;
		params[1] = email;
		int[] types = { 0, 0 };
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache = cacheManager.getCache("application");
		Element em = cache.get(email);
		if (null != em) {
			Student stu = (Student) em.getValue();
			if (null != stu) {
				cache.remove(email);
				stu.setImage(path);
				cache.put(new Element(email, stu));
			}

		}
		cacheManager = null;
		DBConnector.getCount(sql, params, types, 0);
	}

	@Override
	public void toconfirm(String email, String path) {
		/*
		 * upload material,deal = 1 deal it,deal = 2 refuse it,deal = 3
		 */
		String sql = "update student set deal=1,dealpath=? where email=?";
		DBConnector.getCount(sql, new Object[] { path, email }, new int[] { 0,
				0 }, 0);
	}

	@Override
	public void addcomments(String email, String msg) {
		String message = null;
		try {
			message = new String(msg.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		String sql = "insert into comment(email,comments) values(?,?)";
		Object[] params = new Object[2];
		params[0] = email;
		params[1] = message;
		int types[] = { 0, 0 };
		DBConnector.getCount(sql, params, types, 0);
	}

	@Override
	public NewMajor getmajordetail(NewMajor major) {
		return getMajorInfo(major.getSid(), major.getCeid(), major.getMid());
	}

	public NewMajor getMajorInfo(int sid, int ceid, int mid) {
		NewMajor major = null;
		String sql = "select did, dname,plan ,groupid from kaoyanqun.major "
				+ " left join kaoyanqun.direction on (kaoyanqun.direction.sid=kaoyanqun.major.sid and kaoyanqun.direction.ceid=kaoyanqun.major.ceid "
				+ " and kaoyanqun.direction.mid=kaoyanqun.major.mid) where kaoyanqun.major.sid=? and kaoyanqun.major.ceid=? and kaoyanqun.major.mid=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			pstmt.setInt(2, ceid);
			pstmt.setInt(3, mid);
			int index = 0;
			rs = pstmt.executeQuery();
			List<Direction> list = new ArrayList<Direction>();
			while (rs.next()) {
				if (index == 0) {
					major = new NewMajor();
					major.setSid(sid);
					major.setCeid(ceid);
					major.setMid(mid);
					major.setPlan(rs.getString("plan"));
					major.setGroupid(rs.getString("groupid"));
				}
				list.add(new Direction(sid, ceid, mid, rs.getInt("did"), rs
						.getString("dname")));
			}
			if (major != null) {
				major.setDirections(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return major;
	}

	private List<String> setSplitValues(String data) {
		List<String> list = new ArrayList<String>();
		if (null != data && !"".equals(data.trim())) {
			String refers[] = data.trim().split("#");
			for (int i = 0; i < refers.length; i++) {
				if (!"".equals(refers[i].trim())) {
					list.add(refers[i].trim());
				}
			}
		}
		return list;
	}

	@Override
	public String getimage(String email) {
		String sql = "select image  from student where email=?";
		Object[] params = new Object[1];
		params[0] = email;
		int[] types = { 0 };
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < types.length; i++) {
				if (types[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(params[i]));
				} else if (types[i] == 1) {
					pstmt.setInt(i + 1, (Integer) params[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) params[i]);
				}
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("image");
			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return null;
	}

	@Override
	public int confirmFinish(String email) {
		int flag = 0;
		String sql = "select deal from student where email =?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = rs.getInt("deal");
			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}

		return flag;
	}

	@Override
	public VersionInfo getVersionInfo() {
		String sql = "select appversion,appsize,somedescription,flag,url from appinfo where aid=1";
		Connection conn = DBConnector.Getconnect();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				VersionInfo version = new VersionInfo();
				version.setDescription(rs.getString("somedescription"));
				version.setSize(rs.getString("appsize"));
				version.setVersion(rs.getString("appversion"));
				version.setFlag(rs.getString("flag"));
				version.setUrl(rs.getString("url"));
				return version;
			}
		} catch (SQLException e) {
			log.error(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			DBConnector.closeResult(rs);
			DBConnector.closeStatement(stmt);
			DBConnector.closeConn(conn);
		}
		return null;
	}

	/*
	 * register using transaction 1.insert info into student 2.insert info into
	 * choice
	 * 
	 * 3.register with huanxin
	 */
	@Override
	public int registe(Student stu, Major major) {
		// if(userExist(stu.getEmail())){
		// return RESULT.EMAIL_EXISTS;
		// }
		String nickname = null;
		try {
			nickname = new String(stu.getNickname().getBytes("ISO8859-1"),
					"UTF-8");
		} catch (UnsupportedEncodingException e2) {
			log.error(stu.getEmail() + " register error,nickname error "
					+ e2.getMessage());
			return RESULT.FAIL;
		}
		String account = DigestUtils.md5Hex(stu.getEmail());
		String sqlbase = "insert into student(email,password,role,regtime,pid,cid,gender,nickname,account) values(?,?,?,?,?,?,?,?,?)";
		String sqlchoice = "insert into choice(email,aimschoolid,aimcollegeid,aimmajorid,session) "
				+ " values(?,?,?,?,?)";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sqlbase);
			String email = stu.getEmail();
			pstmt.setString(1, account);
			pstmt.setString(2, stu.getPassword());
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String time = formatter.format(new Date());
			pstmt.setInt(3, stu.getRole());
			pstmt.setString(4, time);
			pstmt.setInt(5, Integer.parseInt(stu.getPid()));
			pstmt.setInt(6, Integer.parseInt(stu.getCity()));
			pstmt.setString(7, stu.getGender());
			pstmt.setString(8, nickname);
			pstmt.setString(9, email);
			pstmt.executeUpdate();

			log.info("save user info into database " + email);
			pstmt = conn.prepareStatement(sqlchoice);
			pstmt.setString(1, account);
			pstmt.setInt(2, major.getSid());
			pstmt.setInt(3, major.getCeid());
			pstmt.setInt(4, major.getMid());
			pstmt.setInt(5, stu.getSession());
			pstmt.executeUpdate();
			log.info("save choice into database");
			if (WebService.registerForHuanxin(stu.getEmail())) {
				conn.commit();
				WebService.addUserToGroup(
						account,
						getGroupid(major.getSid(), major.getCeid(),
								major.getMid()));
				RedisOP.saveAUser(email);
				WebService.sendMsgForNewAccount(account, nickname);
				return RESULT.SUCCESS;
			} else {
				conn.rollback();
				log.error(stu.getEmail()
						+ " fails to  register with huanxin,rollback ");
				return RESULT.FAIL;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
				log.error(stu.getEmail() + " register rollback,"
						+ e.getMessage());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} finally {
			DBConnector.closeAll(null, pstmt, null, conn);
		}

		return RESULT.FAIL;
	}

	// addaccusation contains two actions:add guest to blacklist,and send msg to
	// manager
	@Override
	public boolean addAccusation(String host, String guest, String msg) {
		boolean flag = false;
		String deleteFriendsql = "delete from friends where ema=? and emb=?";
		String addToBlacklistsql = "insert into blacklist(ema,emb) values(?,?)";
		String accusationsql = "insert into accusation(host,guest,msg,sendtime) values(?,?,?,?)";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(deleteFriendsql);
			pstmt.setString(1, host);
			pstmt.setString(2, guest);
			pstmt.execute();
			pstmt = conn.prepareStatement(addToBlacklistsql);
			pstmt.setString(1, host);
			pstmt.setString(2, guest);
			pstmt.execute();
			pstmt = conn.prepareStatement(accusationsql);
			pstmt.setString(1, host);
			pstmt.setString(2, guest);
			pstmt.setString(3, msg);
			SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
			String dateString = formatter.format(new Date());
			pstmt.setString(4, dateString);
			pstmt.execute();
			conn.commit();
			log.info(host + " accused " + guest + " successfully");
			flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				log.error(host + " accuse " + guest + " error :"
						+ e.getMessage());
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(null, pstmt, null, conn);
		}
		return flag;
	}

	@Override
	public List<Announce> getAnnounces(String sendtime) {
		long million = Long.parseLong(sendtime);
		String timestamp = null;
		if (million == 0) {
			timestamp = "0";
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = new Date(million);
			timestamp = format.format(date);
		}

		String sql = "select aid,msg,sendtime from announce where sendtime >? order  by aid desc limit 0,6";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Announce> list = new ArrayList<Announce>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, timestamp);
			rs = pstmt.executeQuery();
			Announce announce = null;
			while (rs.next()) {
				announce = new Announce();
				announce.setAid(rs.getInt("aid"));
				announce.setMsg(rs.getString("msg"));
				announce.setSendtime(rs.getString("sendtime"));
				list.add(announce);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("get announce error :" + e.getMessage());
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list;
	}

	private boolean hasConcern(String email, Major major) {
		boolean flag = false;
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from concern where email=? and aimschoolid=? and aimcollegeid=? and aimmajorid=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setInt(2, major.getSid());
			pstmt.setInt(3, major.getCeid());
			pstmt.setInt(4, major.getMid());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return flag;
	}

	@Override
	public int addOrDelConcern(String email, Major major, int op) {
		String sql = null;
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		switch (op) {
		case CONSTANT.ADD:
			if (hasConcern(email, major)) {
				log.warn(email + " has concern :" + major.getSid() + ","
						+ major.getCeid() + "," + major.getMid());
				return CONSTANT.DUPLICATE;
			}
			sql = "insert into concern(email,aimschoolid,aimcollegeid,aimmajorid,session,mytime) values(?,?,?,?,?,?)";
			log.warn(email + " want concern :" + major.getSid() + ","
					+ major.getCeid() + "," + major.getMid());
			break;
		case CONSTANT.DEL:
			sql = "delete from concern where email=? and aimschoolid=? and aimcollegeid=? and aimmajorid=?";
			log.warn(email + " delete concern :" + major.getSid() + ","
					+ major.getCeid() + "," + major.getMid());
			break;
		}
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setInt(2, major.getSid());
			pstmt.setInt(3, major.getCeid());
			pstmt.setInt(4, major.getMid());
			if (op == CONSTANT.ADD) {
				pstmt.setInt(5, Integer.parseInt(major.getYear()));
				pstmt.setString(6, major.getTimestamp());
			}
			if (pstmt.executeUpdate() == 1) {
				if (op == CONSTANT.ADD) {
					WebService.addUserToGroup(
							email,
							getGroupid(major.getSid(), major.getCeid(),
									major.getMid()));
				} else {
					WebService.deleteUserFromGroup(
							email,
							getGroupid(major.getSid(), major.getCeid(),
									major.getMid()));
				}
				return RESULT.SUCCESS;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("addordelConcern error:" + e.getMessage());
			return RESULT.FAIL;
		} catch (Exception e2) {
			log.error(e2.getMessage());
		} finally {
			DBConnector.closeAll(null, pstmt, null, conn);
		}
		return RESULT.SUCCESS;
	}

	@Override
	public List<Major> getConcerns(String email) {
		String sql = "select aimschoolid,aimcollegeid,aimmajorid,session,sname,cename,mname,mytime,groupid from concern "
				+ " left join kaoyanqun.university on kaoyanqun.university.sid=aimschoolid left join kaoyanqun.college on (kaoyanqun.college.sid=aimschoolid and kaoyanqun.college.ceid=aimcollegeid) "
				+ " left join kaoyanqun.major on (kaoyanqun.major.sid=aimschoolid and kaoyanqun.major.ceid=aimcollegeid and kaoyanqun.major.mid=aimmajorid) where email=?";
		List<Major> list = new ArrayList<Major>();
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			Major major = null;
			while (rs.next()) {
				major = new Major();
				major.setSid(rs.getInt("aimschoolid"));
				major.setCeid(rs.getInt("aimcollegeid"));
				major.setMid(rs.getInt("aimmajorid"));
				major.setSname(rs.getString("sname"));
				major.setCename(rs.getString("cename"));
				major.setMname(rs.getString("mname"));
				major.setYear(String.valueOf(rs.getInt("session")));
				major.setTimestamp(rs.getString("mytime"));
				major.setGroupid(rs.getString("groupid"));
				list.add(major);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list;
	}

	public List<Student> searchStudent(String key) {
		List<Student> list = new ArrayList<Student>();
		String tk = null;
		try {
			tk = new String(key.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return list;
		}
		if (null == tk || tk.equals("")) {
			return list;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("(.*)");
		char splitKey[] = tk.toCharArray();
		for (int i = 0; i < splitKey.length; i++) {
			buffer.append(splitKey[i] + "(.*)");
		}
		String matchKey = buffer.toString();
		CacheManager cm = EncacheUtil.getCacheManager();
		Cache cache = cm.getCache("emails");
		Element em = cache.get(CACHE.NICKNAMES);

		Student stu = null;
		if (null != em) {
			Map<String, String> all = (Map) em.getValue();
			Iterator iter = all.entrySet().iterator();
			Entry<String, String> entry = null;
			while (iter.hasNext()) {
				entry = (Entry<String, String>) iter.next();
				if (entry.getKey().matches(matchKey)) {
					stu = getStudent(entry.getValue());
					list.add(stu);
					if (list.size() > 10) {
						break;
					}
				}
			}
		}
		cm = null;
		return list;
	}

	@Override
	public List<Student> lastLoginUsers(String email, int page, int rp) {
		String sql = "select email from student where email!= ? order by lastlogin desc limit ?,?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Student> list = new ArrayList<Student>();
		List<String> emails = new ArrayList<String>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			// pstmt.setInt(2, (page - 1) * rp);
			// pstmt.setInt(3, rp);
			pstmt.setInt(2, 0);
			pstmt.setInt(3, 50);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String emil = rs.getString("email");
				if (!"yan".equals(emil)) {
					emails.add(emil);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		for (String id : emails) {
			list.add(getStudent(id));
		}
		return list.size() > 0 ? list : null;
	}

	@Override
	public List<Tips> getTips(Major major, int page, int rp, int kind) {
		List<Tips> list = new ArrayList<Tips>();
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// String sql =
			// "select push_res.tpid, title,createdtime,author from push_res "
			// + "inner join pushtip on pushtip.tpid=push_res.tpid "
			// +
			// "where push_res.kind=? and push_res.sid=? and ceid=? and mid=? order by rid desc "
			// + " limit ?,?";
			String sql = "select discuz_res.tpid,title,createdtime,author from discuz_res "
					+ " inner join pushtip on pushtip.tpid=discuz_res.tpid "
					+ " where discuz_res.kind=? and(push_target=? or push_target=? or push_target=? or push_target=?)"
					+ " limit ?,?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, kind);
			// pstmt.setInt(2, major.getSid());
			// pstmt.setInt(3, major.getCeid());
			// pstmt.setInt(4, major.getMid());
			pstmt.setString(2, major.getSid() + "_" + major.getCeid() + "_"
					+ major.getMid());
			pstmt.setString(3, major.getSid() + "_" + major.getCeid());
			pstmt.setString(4, major.getSid() + "");
			pstmt.setString(5, "all");
			// pstmt.setInt(6, (page - 1) * rp);
			// pstmt.setInt(7, rp);
			pstmt.setInt(6, 0);
			pstmt.setInt(7, 50);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String url = "http://www.yifulou.cn:8180/Manage/getContent?tpid="
						+ rs.getInt("discuz_res.tpid");
				list.add(new Tips(rs.getString("title"), url, rs.getString(
						"createdtime").substring(0, 11), rs.getString("author")));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list.size() > 0 ? list : null;
	}

	@Override
	public int getTipCount(Major major, int kind) {
		int count = 0;
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// String sql =
		// "select count(*) from push_res where kind = ? and sid=? and ceid=? and mid=?";
		String sql = "select count(*) from discuz_res where kind=? and (push_target=? or push_target=? or push_target=? or push_target=?) order by tpid desc";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, kind);
			// pstmt.setInt(2, major.getSid());
			// pstmt.setInt(3, major.getCeid());
			// pstmt.setInt(4, major.getMid());
			pstmt.setString(2, major.getSid() + "_" + major.getCeid() + "_"
					+ major.getMid());
			pstmt.setString(3, major.getSid() + "_" + major.getCeid());
			pstmt.setString(4, major.getSid() + "");
			pstmt.setString(5, "all");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return count;
	}

	/**
	 * 
	 * 妫�煡鐢ㄦ埛id鏄惁瀛樺湪锛岃繖涓彲浠ユ斁鍦╩emcache涓紝鍓嶆彁鏄紝闇�memcache涓啓鍏ョ敤鎴风殑淇℃伅
	 * 鍦ㄩ儴缃叉椂锛屽鍏ヤ竴浠斤紝浠ュ悗鏈夋柊鐢ㄦ埛娉ㄥ唽锛屽悓鏃跺鍔�
	 * 
	 */
	public boolean userExist(String id) {
		if (!RedisOP.hasValue("user_ids")) {
			Connection conn = DBConnector.Getconnect();
			String sql = "select account from student";
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				List<String> ids = new ArrayList<String>();
				while (rs.next()) {
					ids.add(rs.getString(1));
				}
				if (ids.size() > 0) {
					RedisOP.saveUsers(ids);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBConnector.closeAll(rs, null, stmt, conn);
			}
		}
		if (RedisOP.userExists(id)) {
			return true;
		}
		return false;
	}

	public String getGroupid(int sid, int ceid, int mid) {
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String groupid = "";
		String sql = "select groupid from kaoyanqun.major where sid=? and ceid = ? and mid=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			pstmt.setInt(2, ceid);
			pstmt.setInt(3, mid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				groupid = rs.getString("groupid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("getGroupid error :" + e.getMessage());
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return groupid;
	}

	@Override
	public Direction getDirectionInfo(int sid, int ceid, int mid, int did) {
		String sql = "select teacher,physics,language,major1,major2,note from kaoyanqun.direction where sid=? and ceid=? and mid=? and did=?";
		Direction dd = null;
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			pstmt.setInt(2, ceid);
			pstmt.setInt(3, mid);
			pstmt.setInt(4, did);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dd = new Direction(sid, ceid, mid, did, "");
				dd.setTeacher(rs.getString("teacher"));
				dd.setPolitics(concat(rs.getString("physics").split(",")));
				dd.setForeignLanguage(concat(rs.getString("language")
						.split(",")));
				dd.setMajor1(concat(rs.getString("major1").split(",")));
				dd.setMajor2(concat(rs.getString("major2").split(",")));
				dd.setNote(rs.getString("note"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return dd;
	}

	private String concat(String[] strs) {
		String res = "";
		int index = 0;
		for (String str : strs) {
			if (!StringUtils.isEmpty(str)) {
				if (index == 0) {
					res += str;
				} else {
					res += " 鎴�" + str;
				}
				index++;
			}
		}
		return res;
	}

	@Override
	public void saveRandomData(String phone, String num) {
		RedisOP.setRandom(phone, num);
	}

	@Override
	public int checkRandomData(String phone, String num) {

		return RedisOP.getRandom(phone, num);
	}

}
