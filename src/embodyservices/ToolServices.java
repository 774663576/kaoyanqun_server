package embodyservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import db.DBConnector;
import db.Utilities;
import beans.City;
import beans.College;
import beans.Major;
import beans.Tiaoji;
import beans.Tips;
import services.ToolDao;

public class ToolServices implements ToolDao {
	private final Logger log = Logger.getLogger("db");
	@Override
	public List<College> getcollege(int kind,int sid) {
		List<College> list=new ArrayList<College>();
		String sql=null;
		if(kind ==0){
			sql="select ceid,cename from kaoyanqun.college where sid=?";
		}else{
			sql="select ceid,cename from batchelorcollege where sid=?";
		}
		
		Connection conn =DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			rs = pstmt.executeQuery();
			College uy = null;
			while(rs.next()){
			uy = new College();
                uy.setCeid(rs.getInt("ceid"));
				uy.setCename(rs.getString("cename"));
				uy.setSid(sid);
				list.add(uy);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		  log.error("get colleges error "+e.getMessage());
		}finally{
			DBConnector.closeResult(rs);
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeConn(conn);
		}
		return list;
	}

	@Override
	public List<City> getCityFromProv(int pid) {
		List<City> cities = new ArrayList<City>();
		String sql="select cid,cname from city where pid=?";
		Connection conn =DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, pid);
			rs = pstmt.executeQuery();
			while(rs.next()){
				City city=new City();
				city.setPid(rs.getInt("cid"));
				city.setCname(rs.getString("cname"));
				city.setPid(pid);
				cities.add(city);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeResult(rs);
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeConn(conn);
		}
		return cities;
	}

	@Override
	public List<Major> getmajor(int sid, int ceid) {
		List<Major> list=new ArrayList<Major>();
		String sql="select mid,mname from kaoyanqun.major where sid=? and ceid=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			pstmt.setInt(2, ceid);
			rs = pstmt.executeQuery();
			Major uy = null;
			while(rs.next()){
			uy = new Major();
                uy.setCeid(ceid);
                uy.setMid(rs.getInt("mid"));
                uy.setSid(sid);
                uy.setMname(rs.getString("mname"));
				list.add(uy);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("get major error "+e.getMessage());
		}finally{
			DBConnector.closeResult(rs);
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeConn(conn);
		}
		return list;
	}

	@Override
	public String getSchoolname(int kind, int sid) {
		 String sql=null;
		 if(kind==0){
		    sql="select sname from kaoyanqun.university where sid =?";
		  }else{
			 sql="select sname from batchelorschool where sid =?";
	      }
			Object params[]=new Object[]{sid};
			int[]types = new int[]{1};
			return (String) Utilities.getAnResult(sql, params, types, 0);
	}

	@Override
	public String getCollegeName(int kind, int sid, int ceid) {
		String sql = null;
		if(kind==0){
		  sql="select cename from kaoyanqun.college where sid=? and ceid=?";
		}else{
			 sql="select cename from batchelorcollege where sid=? and ceid=?";
		}
			Object params[]=new Object[]{sid,ceid};
			int[]types=new int[]{1,1};
			return (String) Utilities.getAnResult(sql, params, types, 0);
	}

	@Override
	public String getMajorName(int sid, int ceid, int mid) {
		String sql = "select mname from kaoyanqun.major where sid =? and ceid=? and mid=?";
		Object params[]=new Object[]{sid,ceid,mid};
		int[]types=new int[]{1,1,1};
		return (String) Utilities.getAnResult(sql, params, types, 0);
	}

	@Override
	public String getProvinceName(int pid) {
		String sql="select pname from province where pid=?";
		Object params[]=new Object[]{pid};
		int[]types=new int[]{1};
		return (String) Utilities.getAnResult(sql, params, types, 0);
	}

	@Override
	public String getCityName(int pid, int cid) {
		String sql="select cname from city where pid=? and cid=?";
		Object params[]=new Object[]{pid,cid};
		int[]types=new int[]{1,1};
		return (String) Utilities.getAnResult(sql, params, types, 0);
	}

	@Override
	public String[] getProvinceAndCity(int pid, int cid) {
		String sql="select pname,cname from province,city where province.pid=? and city.pid=province.pid and city.cid=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pid);
			pstmt.setInt(2, cid);
			rs = pstmt.executeQuery();
			if(rs.next()){
			  return new String[]{rs.getString("pname"),rs.getString("cname")};
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return null;
	}

	@Override
	public String[] getSchoolAndCollege(int sid, int ceid,int kind) {
		String sql= null;
		if(kind ==0){
			sql="select sname,cename from kaoyanqun.university,kaoyanqun.college where kaoyanqun.university.sid=? and kaoyanqun.college.sid=university.sid and kaoyanqun.college.ceid =?";
		}else{
			sql="select sname,cename from batchelorschool,batchelorcollege where batchelorschool.sid=? and batchelorcollege.sid=batchelorschool.sid and batchelorcollege.ceid =?";
		}
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			pstmt.setInt(2, ceid);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new String[]{rs.getString("sname"),rs.getString("cename")};
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return null;
	}

	@Override
	public List<Major> getSchoolsByMajor(int ssid) {
		List<Major> list = new ArrayList<Major>();
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select kaoyanqun.major.sid,kaoyanqun.major.ceid,sname,cename,mname from kaoyanqun.major "
				+ "left join kaoyanqun.university on kaoyanqun.university.sid=kaoyanqun.major.sid "
				+ "left join kaoyanqun.college on (kaoyanqun.college.sid=kaoyanqun.major.sid and kaoyanqun.college.ceid=major.ceid)"
				+ "where mid=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ssid);
			rs = pstmt.executeQuery();
			Major major = null;
			while(rs.next()){
				major = new Major();
				major.setSid(rs.getInt("major.sid"));
				major.setCeid(rs.getInt("major.ceid"));
				major.setMid(ssid);
				major.setSname(rs.getString("sname"));
				major.setCename(rs.getString("cename"));
				major.setMname(rs.getString("mname"));
				list.add(major);
			}
		} catch (SQLException e) {
				log.error("get by major classify error "+e.getMessage());
		}finally{
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list.size()>0?list:null;
	}

	@Override
	public Map<Integer, String> getTiaojiList() {
		String sql = "select distinct ssid,sename from tiaoji left join secondclass on fid=ssid";
		Connection conn=DBConnector.Getconnect();
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer,String> maps = new HashMap<Integer, String>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				maps.put(rs.getInt("ssid"), rs.getString("sename"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeAll(rs, null, stmt, conn);
		}
		return maps;
	}

	@Override
	public List<Tiaoji> getTiaojiInfo(int mid) {
		String sql="select sname,cename,tpid,mname from tiaoji " +
				" left join kaoyanqun.university on kaoyanqun.university.sid=tiaoji.sid " +
				" left join kaoyanqun.college on (kaoyanqun.college.sid=tiaoji.sid and kaoyanqun.college.ceid=tiaoji.ceid)" +
				" left join kaoyanqun.major on (kaoyanqun.major.sid=tiaoji.sid and kaoyanqun.major.ceid=tiaoji.ceid and kaoyanqun.major.mid=tiaoji.mid) " +
				" where tiaoji.mid=?";
		List<Tiaoji> list = new ArrayList<Tiaoji>();
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mid);
			rs = pstmt.executeQuery();
			Tiaoji tiaoji = null;
			while(rs.next()){
				tiaoji = new Tiaoji();
				tiaoji.setSname(rs.getString("sname")) ;
				tiaoji.setCename(rs.getString("cename")) ;
				tiaoji.setMname(rs.getString("mname"));
				tiaoji.setUrl("http://www.yifulou.cn:8180/Manage/getContent?tpid="+rs.getInt("tpid")) ;
				list.add(tiaoji);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list;
	}

	@Override
	public boolean validAim(int sid, int ceid, int mid) {
		String sql = "select count(*) from kaoyanqun.major where sid=? and ceid=? and mid=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			pstmt.setInt(2, ceid);
			pstmt.setInt(3, mid);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getInt(1)==1){
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return false;
	}
/*
	@Override
	public List<Tips> getTiaojiTiezi(int mid) {
		String sql="select title,createdtime,pushtip.tpid,author from pushtip " +
				" left join tiaoji on tiaoji.tpid=pushtip.tpid where mid=?";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Tips> list = new ArrayList<Tips>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mid);
			rs = pstmt.executeQuery();
		    Tips tip = new Tips();
			while(rs.next()){
			 tip = new Tips();
			 tip.setTitle(rs.getString("title"));
			 tip.setCreatedtime(rs.getString("createdtime").substring(0, 11));
			 tip.setAuthor(rs.getString("author"));
			 tip.setUrl("http://www.yifulou.cn:8180/Manage/getContent?tpid="+rs.getInt("pushtip.tpid"));
			 list.add(tip);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list;
	} */
	@Override
	public List<Tips> getTiaojiTiezi(int sid) {
		String sql="select title,createdtime,tpid,author from pushtip " +
				"  where sid=? and kind=4";
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Tips> list = new ArrayList<Tips>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			rs = pstmt.executeQuery();
		    Tips tip = new Tips();
			while(rs.next()){
			 tip = new Tips();
			 tip.setTitle(rs.getString("title"));
			 tip.setCreatedtime(rs.getString("createdtime").substring(0, 11));
			 tip.setAuthor(rs.getString("author"));
			 tip.setUrl("http://www.yifulou.cn:8180/Manage/getContent?tpid="+rs.getInt("pushtip.tpid"));
			 list.add(tip);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeAll(rs, pstmt, null, conn);
		}
		return list;
	}

}
