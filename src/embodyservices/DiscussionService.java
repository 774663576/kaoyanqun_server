package embodyservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import db.DBConnector;
import beans.Personinfo;
import beans.Response;
import beans.Tips;
import beans.Topic;
import services.DiscussisonDao;
import services.ToolDao;

public class DiscussionService implements DiscussisonDao {

	@Override
	public int addDiscussion(Topic topic) {
		int tid = 0;
		String sql = "insert into topic(title,host,sendtime,sid,ceid,mid) "
				+ " values(?,?,?,?,?,?)";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String title = new String(topic.getTitle().getBytes("ISO8859-1"),
					"UTF-8");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, topic.getLouzhu().getEmail());
			String datetime = formatdata("yy-MM-dd HH:mm");
			topic.setTimestamp(datetime);
			pstmt.setString(3, datetime);
			pstmt.setInt(4, topic.getSid());
			pstmt.setInt(5, topic.getCeid());
			pstmt.setInt(6, topic.getMid());
			pstmt.execute();
			String query = "select last_insert_id()";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				tid = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return tid;
	}

	public String formatdata(String format) {
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(date);
	}

	@Override
	public int getResponseTotal(int tid) {
		String sql = "select count(*) from responses where tid=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, tid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return 0;
	}

	@Override
	public int getTopicTotal(int sid, int ceid, int mid) {
		String sql = null;
		int count = 0;
		Object[] params;
		int types[];
		if (ceid == 0) {
			sql = "select count(*) from topic where sid=?";
			params = new Object[1];
			params[0] = sid;
			types = new int[1];
			types[0] = 1;
		} else if (mid == 0) {
			sql = "select count(*) from topic where sid=? and ceid=? ";
			// sql =
			// "select count(*) from topic where sid=? and ceid=? union select count(*) from topic where sid=? and ownbyschool=1";
			// sql="select count(*) from (select *  from topic  t1 where (t1.sid=? and t1.ceid=?  )  union select *  from topic t2 where (t2.sid=? and t2.ownbyschool=1)) t";
			params = new Object[2];
			params[0] = sid;
			params[1] = ceid;
			// params[2]=sid;
			types = new int[2];
			types[0] = 1;
			types[1] = 1;
			// types[2]=1;
		} else {
			sql = "select count(*) from topic where sid=? and ceid=? and mid=? ";
			// sql="select count(*) from (select *  from topic  t1 where (t1.sid=? and t1.ceid=? and t1.mid=? )  union select *  from topic t2 where (t2.sid=? and t2.ownbyschool=1)) t";
			params = new Object[3];
			params[0] = sid;
			params[1] = ceid;
			params[2] = mid;
			// params[3]=sid;
			types = new int[3];
			types[0] = 1;
			types[1] = 1;
			types[2] = 1;
			// types[3]=1;
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
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}

		return 0;
	}

	@Override
	public List<Topic> getTopiclist(int sid, int ceid, int mid, int page, int rp) {
		List<Topic> list = new ArrayList<Topic>();
		String sql = null;
		int start = (page - 1) * rp;
		Object[] params;
		int[] types;
		if (ceid == 0) {
			sql = "select topic.tid,topic.host,title,topic.sid,topic.ceid,topic.mid,a.sname as snamea,cename,mname ,nickname,"
					+ " topic.sendtime,count(responses.tid) as count ,"
					+ "gender,image,b.sname as snameb  from topic "
					+ " left join student on student.email=topic.host"
					+ " left join kaoyanqun.university a  on a.sid=topic.sid "
					+ " left join kaoyanqun.college on (kaoyanqun.college.sid=topic.sid and kaoyanqun.college.ceid=topic.ceid) "
					+ " left join kaoyanqun.major on (kaoyanqun.major.sid =topic.sid and kaoyanqun.major.ceid=topic.ceid and kaoyanqun.major.mid=topic.mid) "
					+ " left join batchelorschool b on b.sid = student.bachelorschoolid "
					+ " left join responses on(responses.tid=topic.tid) "
					+ " where topic.sid =  ?  "
					+ " group by topic.tid  order by priority desc,topic.tid desc limit ?,? ";
			Object sparam[] = new Object[] { sid, start, rp };
			params = sparam;
			int[] stype = new int[] { 1, 1, 1 };
			types = stype;
		} else if (mid == 0) {
			sql = "select topic.tid,topic.host,title,topic.sid,topic.ceid,topic.mid,a.sname as snamea,cename,mname ,nickname,"
					+ " topic.sendtime,count(responses.tid) as count ,"
					+ "gender,image,b.sname as snameb  from topic  "
					+ " left join student on student.email=topic.host"
					+ " left join kaoyanqun.university a  on a.sid=topic.sid "
					+ " left join kaoyanqun.college on (kaoyanqun.college.sid=topic.sid and kaoyanqun.college.ceid=topic.ceid) "
					+ " left join kaoyanqun.major on (kaoyanqun.major.sid =topic.sid and kaoyanqun.major.ceid=topic.ceid and kaoyanqun.major.mid=topic.mid) "
					+ " left join batchelorschool b on b.sid = student.bachelorschoolid "
					+ " left join responses on(responses.tid=topic.tid) "
					+ " where topic.sid =  ? and (topic.ceid=? )  "
					+ " group by topic.tid  order by priority desc,topic.tid desc limit ?,? ";
			Object sparam[] = new Object[] { sid, ceid, start, rp };
			params = sparam;
			int[] stype = new int[] { 1, 1, 1, 1 };
			types = stype;
		} else {
			sql = "select topic.tid,topic.host,title,topic.sid,topic.ceid,topic.mid,a.sname as snamea,cename,mname ,nickname,"
					+ " topic.sendtime,count(responses.tid) as count ,"
					+ "gender,image,b.sname as snameb  from topic  "
					+ " left join student on student.email=topic.host"
					+ " left join kaoyanqun.university a  on a.sid=topic.sid "
					+ " left join kaoyanqun.college on (kaoyanqun.college.sid=topic.sid and kaoyanqun.college.ceid=topic.ceid) "
					+ " left join kaoyanqun.major on (kaoyanqun.major.sid =topic.sid and kaoyanqun.major.ceid=topic.ceid and kaoyanqun.major.mid=topic.mid) "
					+ " left join batchelorschool b on b.sid = student.bachelorschoolid "
					+ " left join responses on(responses.tid=topic.tid) "
					+ " where topic.sid =  ? and ((topic.ceid=?  and topic.mid=?) ) "
					+ " group by topic.tid  order by priority desc,topic.tid desc limit ?,? ";
			Object sparam[] = new Object[] { sid, ceid, mid, start, rp };
			params = sparam;
			int[] stype = new int[] { 1, 1, 1, 1, 1 };
			types = stype;
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
			Personinfo info = null;
			Topic topic = null;
			while (rs.next()) {
				topic = new Topic();
				topic.setSid(sid);
				topic.setCeid(rs.getInt("topic.ceid"));
				topic.setMid(rs.getInt("topic.mid"));
				info = new Personinfo();
				info.setEmail(rs.getString("host"));
				info.setGender(rs.getString("gender"));
				info.setImage(rs.getString("image"));
				info.setNickname(rs.getString("nickname"));
				info.setBatchelorschool(rs.getString("snameb"));
				topic.setLouzhu(info);
				topic.setTimestamp(rs.getString("sendtime"));
				topic.setTitle(rs.getString("title"));
				topic.setTid(rs.getInt("tid"));
				topic.setTotal(rs.getInt("count"));
				if (info.getEmail().equals("admin@qq.com")) {
					info.setBatchelorschool("小研");
				}
				list.add(topic);

			}
			for (Topic one : list) {
				List<String> pics = getTopicPic(one.getTid());

				one.setUrls(pics);

			}
			DBConnector.closeAll(rs, pstmt, null, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list;
	}

	public List<String> getTopicPic(int tid) {
		List<String> list = new ArrayList<String>();
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select path from topic_picture where tid=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, tid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("path"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list;
	}

	public Personinfo fillPersonInfo(String email) {
		Personinfo info = null;

		int sid = 0;
		String sql = "select nickname,gender,image,bachelorschoolid from student where email=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				info = new Personinfo();
				info.setNickname(rs.getString("nickname"));
				info.setGender(rs.getString("gender"));
				info.setImage(rs.getString("image"));
				info.setEmail(email);
				sid = rs.getInt("bachelorschoolid");
				DBConnector.closeAll(rs, pstmt, null, conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		if (sid != 0) {
			ToolDao dao = new ToolServices();
			info.setBatchelorschool(dao.getSchoolname(1, sid));

		}
		return info;
	}

	@Override
	public int doresponse(Response response, int prid) {
		int rid = 0;
		String sql = "insert into responses(tid,host,guest,sendtime,content,prid) values(?,?,?,?,?,?)";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String content = new String(response.getContent().getBytes(
					"ISO8859-1"), "UTF-8");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, response.getTid());
			pstmt.setString(2, response.getHost().getEmail());
			pstmt.setString(3, response.getGuest().getEmail());
			String sendtime = formatdata("yy-MM-dd HH:mm");
			pstmt.setString(4, sendtime);
			pstmt.setString(5, content);
			pstmt.setInt(6, prid);
			response.setTimestamp(sendtime);
			pstmt.execute();
			String query = "select last_insert_id()";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rid = rs.getInt(1);
			}
			DBConnector.closeAll(null, pstmt, null, conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(null, pstmt, null, conn);
		}
		return rid;
	}

	public List<Response> getResponselist(int tid, int page, int rp) {
		List<Response> list = new ArrayList<Response>();
		String sql = "select e.host as host,e.guest as guest,a.nickname as hostname,b.nickname as guestname,e.sendtime as sendtime,e.content as content,f.content as contentb,e.rid as rid,"
				+ "a.image as imagea ,b.image as imageb,a.gender as gendera,b.gender as genderb,c.sname as snamea,"
				+ "d.sname as nameb  from responses e "
				+ "inner join student a on (e.host=a.email) inner join student b on (e.guest=b.email) "
				+ "left join batchelorschool c on a.bachelorschoolid=c.sid "
				+ " left join responses f on f.rid=e.prid "
				+ " left join batchelorschool d on b.bachelorschoolid=d.sid  where e.tid=? limit ?,? ";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, tid);
			pstmt.setInt(2, (page - 1) * rp);
			pstmt.setInt(3, rp);
			rs = pstmt.executeQuery();
			Response res = null;
			while (rs.next()) {
				res = new Response();
				res.setTid(tid);
				res.setRid(rs.getInt("rid"));
				if (rs.getString("contentb") != null
						&& !rs.getString("contentb").equals("")) {
					res.setResponse(rs.getString("contentb"));
				}
				res.setContent(rs.getString("content"));
				res.setTimestamp(rs.getString("sendtime"));
				Personinfo host = new Personinfo();
				host.setEmail(rs.getString("host"));
				host.setNickname(rs.getString("hostname"));
				host.setGender(rs.getString("gendera"));
				host.setImage(rs.getString("imagea"));
				host.setBatchelorschool(rs.getString("snamea"));
				Personinfo guest = new Personinfo();
				guest.setEmail(rs.getString("guest"));
				guest.setNickname(rs.getString("guestname"));
				guest.setGender(rs.getString("genderb"));
				guest.setImage(rs.getString("imageb"));
				guest.setBatchelorschool(rs.getString("imageb"));
				System.out.println(host + "-----------");
				String url = getResponsePic(res.getRid());
				System.out.println("-----------" + url);
				res.setUrl(url);
				res.setHost(host);
				res.setGuest(guest);
				list.add(res);
			}
			DBConnector.closeAll(rs, pstmt, null, conn);
		} catch (SQLException e) {
			System.out.println("error :" + e.getMessage());
			return null;
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list;
	}

	@Override
	public List<Topic> getMyTopics(String email, int page, int rp) {

		List<Topic> topics = new ArrayList<Topic>();
		String sql = "select topic.tid,title,topic.sid,topic.ceid,topic.mid,sname,cename,mname ,topic.sendtime,count(responses.tid) as count "
				+ "  from topic left join kaoyanqun.university on kaoyanqun.university.sid=topic.sid  "
				+ "left join kaoyanqun.college on (kaoyanqun.college.sid=topic.sid and kaoyanqun.college.ceid=topic.ceid) "
				+ "left join kaoyanqun.major on (kaoyanqun.major.sid=topic.sid and kaoyanqun.major.ceid=topic.ceid and kaoyanqun.major.mid=topic.mid)"
				+ " left join responses on((responses.tid=topic.tid)) "
				+ "where topic.host =? "
				+ " group by topic.tid  order by topic.tid desc limit ?,?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setInt(2, (page - 1) * rp);
			pstmt.setInt(3, rp);
			rs = pstmt.executeQuery();
			Topic topic = null;
			Personinfo louzhu = null;
			Map<Integer, Topic> map = new HashMap<Integer, Topic>();
			while (rs.next()) {
				topic = new Topic();
				topic.setSid(rs.getInt("topic.sid"));
				topic.setCeid(rs.getInt("topic.ceid"));
				topic.setMid(rs.getInt("topic.mid"));
				topic.setTid(rs.getInt("topic.tid"));
				topic.setSname(rs.getString("sname"));
				topic.setCename(rs.getString("cename"));
				topic.setMname(rs.getString("mname"));
				topic.setTitle(rs.getString("title"));
				topic.setTimestamp(rs.getString("topic.sendtime"));
				topic.setTotal(rs.getInt("count"));
				louzhu = new Personinfo();
				louzhu.setEmail(email);
				topic.setLouzhu(louzhu);
				map.put(topic.getTid(), topic);
				topics.add(topic);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}

		return topics;
	}

	@Override
	public int getMyTopicTotal(String email) {
		int count = 0;
		String sql = "select count(*) from topic where host=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return count;
	}

	@Override
	public void saveTopPic(int tid, String path) {
		System.out.println("tid:::::::::::" + tid + "   " + path);
		String sql = "insert into topic_picture(tid,path) values(?,?)";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, tid);
			pstmt.setString(2, path);
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(null, pstmt, null, conn);
		}

	}

	@Override
	public void saveResponsePic(int tid, String path) {
		String sql = "insert into response_pic(tid,path) values(?,?)";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, tid);
			pstmt.setString(2, path);
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closeAll(null, pstmt, null, conn);
		}
	}

	@Override
	public String getResponsePic(int tid) {
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select path from response_pic where tid=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, tid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getString("path");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();

		} finally {
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return "";
	}

}
