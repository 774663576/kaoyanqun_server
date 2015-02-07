package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.City;
import beans.College;
import beans.Major;

public class Utilities {
   

	public static List<City>getCityFromProv(int pid){
		
		List<City> cities = new ArrayList<City>();
		String sql="select cid,cname from city where pid=?";
		Object[]params=new Object[1];
		params[0]=pid;
		int[]types=new int[1];
		types[0]= 1;
		ResultSet rs=DBConnector.getResultWitParams(sql, params, types);
		try {
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
		}
		return cities;
	}
	
	
	
	public static List<Major> getmajor(int sid,int ceid){
		List<Major> list=new ArrayList<Major>();
		String sql="select mid,sid,mname from major where sid=? and ceid=?";
		Object[]params=new Object[2];
		params[0]=sid;
		params[1]=ceid;
		int[]types=new int[2];
		types[0]= 1;
		types[1]=1;
		ResultSet rs=DBConnector.getResultWitParams(sql, params, types);
		try {
			while(rs.next()){
			Major uy = new Major();
                uy.setCeid(ceid);
                uy.setMid(rs.getInt("mid"));
                uy.setSid(sid);
                uy.setMname(rs.getString("mname"));
				list.add(uy);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<College> getcollege(int sid){
		
		List<College> list=new ArrayList<College>();
		String sql="select ceid,cename from college where sid=?";
		Object[]params=new Object[1];
		params[0]=sid;
		int[]types=new int[1];
		types[0]= 1;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		Connection conn =DBConnector.Getconnect();
		try {
			pstmt = conn.prepareStatement(sql);
			setParamForPstmt(pstmt, params, types);
			rs = pstmt.executeQuery();
			while(rs.next()){
			College uy = new College();
                uy.setCeid(rs.getInt("ceid"));
				uy.setCename(rs.getString("cename"));
				uy.setSid(sid);
				list.add(uy);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeResult(rs);
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeConn(conn);
		}
		return list;
	}
	public static void setParamForPstmt(PreparedStatement pstmt,Object[]params,int []types){
		try{
		for(int i=0;i<types.length;i++){
			
			if(types[i]==0){
				pstmt.setString(i+1, String.valueOf(params[i]));
			}else if (types[i]==1){
				pstmt.setInt(i+1, (Integer)params[i]);
			}else{
				pstmt.setDouble(i+1, (Double)params[i]);
			}
		}
		}catch(Exception e){
			System.out.println("set param error");
		}
	}
	public static String setSchoolname(int sid){
		String sql="select sname from university where sid =?";
		Object params[]=new Object[]{sid};
		int[]types = new int[]{1};
		return (String) getAnResult(sql, params, types, 0);
	}

	public static String setCollegeName(int sid,int ceid){
		String sql="select cename from college where sid=? and ceid=?";
		Object params[]=new Object[]{sid,ceid};
		int[]types=new int[]{1,1};
		return (String) getAnResult(sql, params, types, 0);
	}
	public static String setMajorName(int sid,int ceid,int mid){
		String sql = "select mname from major where sid =? and ceid=? and mid=?";
		Object params[]=new Object[]{sid,ceid,mid};
		int[]types=new int[]{1,1,1};
		return (String) getAnResult(sql, params, types, 0);
		
	}
	public static String getProvinceName(int pid){
		String sql="select pname from province where pid=?";
		Object params[]=new Object[]{pid};
		int[]types=new int[]{1};
		return (String) getAnResult(sql, params, types, 0);
	}
	public static String getCityName(int pid,int cid){
		String sql="select cname from city where pid=? and cid=?";
		Object params[]=new Object[]{pid,cid};
		int[]types=new int[]{1,1};
		return (String) getAnResult(sql, params, types, 0);
	}
	public static Object getAnResult(String sql,Object[]params,int[]types,int rtype){
		Object result = null;
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<types.length;i++){
				if(types[i]==0){
					pstmt.setString(i+1, String.valueOf(params[i]));
				}else if (types[i]==1){
					pstmt.setInt(i+1, (Integer)params[i]);
				}else{
					pstmt.setDouble(i+1, (Double)params[i]);
				}
			}
			rs=pstmt.executeQuery();
			if(rs.next()){
				switch (rtype){
				case 1:
					result = rs.getInt(1);
					break;
				case 0:
					result = rs.getString(1);
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnector.closeResult(rs);
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeResult(rs);
		}
		return result;
	}
	
}
