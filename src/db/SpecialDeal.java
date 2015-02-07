package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import cache.EncacheUtil;
import utils.RESULT;

import beans.Student;

public class SpecialDeal {
	private Connection conn=null;
	private PreparedStatement pstmt=null;
	private ResultSet rs=null;
 
  public boolean IfExistsEmail(String email){
	   Set<String>emails = new HashSet<String>();
	    boolean flag = false;
	    String key="emails";
		CacheManager cacheManager = EncacheUtil.getCacheManager();
		Cache cache=cacheManager.getCache("base");
		Element elem = cache.get(key);
		if(null==elem){
	    String sql="select email from student";
	    conn=DBConnector.Getconnect();
	   try {
		 pstmt=conn.prepareStatement(sql);
		 rs=pstmt.executeQuery();
		
		 while(rs.next()){
			emails.add(rs.getString("email"));
		 }
		 cache.put(new Element(key,emails));
		
	 } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	 }finally{
		DBConnector.closeResult(rs);
		DBConnector.closePreparedStatement(pstmt);
		DBConnector.closeConn(conn);
		cacheManager = null;
	}
		}else{
			emails=(Set<String>) elem.getValue();
		}
		 if(emails.contains(email)){
			 flag = true;
		 }
		 cacheManager = null;
	  return flag;
  }
 
  public void changePassword(String email,String newpassword){
	  conn = DBConnector.Getconnect();
	  String sql="update student set password = ? where email=?";
	  PreparedStatement pstmt=null;
	  try {
		pstmt=conn.prepareStatement(sql);
		pstmt.setString(1, newpassword);
		pstmt.setString(2, email);
		pstmt.executeUpdate();
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		DBConnector.closePreparedStatement(pstmt);
		DBConnector.closeConn(conn);
	}
	 
  }
  public String getpassword(Student stu){
	  conn=DBConnector.Getconnect();
	  String sql="select password from student where email=?";
	  try {
		pstmt=conn.prepareStatement(sql);
		pstmt.setString(1, stu.getEmail());
		rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getString("password");
		}
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		DBConnector.closeResult(rs);
		DBConnector.closePreparedStatement(pstmt);
		DBConnector.closeConn(conn);
	}
	  
	  return null;
  }
  public int login(Student stu){
	  //conn=DBConnector.Getconnect();
	  conn =DBConnector.Getconnect();
	  int result=1;
	  String sql="select password,role from student where email=?";
	  try {
		pstmt=conn.prepareStatement(sql);
		pstmt.setString(1, stu.getEmail());
		rs=pstmt.executeQuery();
		if(rs.next()){
			String password=rs.getString("password");
			if(password.equals(stu.getPassword())){
				result=RESULT.SUCCESS;
				stu.setRole(rs.getInt("role"));
			}else{
				result=RESULT.PASSWORD_ERROR;
			}
		}else{
			result=RESULT.USER_INVALID;
		}
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		DBConnector.closeResult(rs);
		DBConnector.closePreparedStatement(pstmt);
		DBConnector.closeConn(conn);
	}
	  
	  return result;
  }
  
}
