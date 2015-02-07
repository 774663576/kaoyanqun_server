package services;

import java.util.List;
import java.util.Map;

import beans.City;
import beans.College;
import beans.Major;
import beans.Tiaoji;
import beans.Tips;

public interface ToolDao {
	public  List<College> getcollege(int kind,int sid);
	public  List<City>getCityFromProv(int pid);
	public List<Major> getmajor(int sid,int ceid);
	//get schoolname according to sid,kind==0,search undergraduateschoolname
	//kind==1 to search bachelor schoolname
	public  String getSchoolname(int kind,int sid);
	//the function of kind as same with the former
	public  String getCollegeName(int kind,int sid,int ceid);
	public  String getMajorName(int sid,int ceid,int mid);
	public  String getProvinceName(int pid);
	public  String getCityName(int pid,int cid);
	public String[] getProvinceAndCity(int pid,int cid);
	public String[] getSchoolAndCollege(int sid,int ceid,int kind);
	
	public List<Major> getSchoolsByMajor(int ssid);
	public Map<Integer, String>getTiaojiList();
	public List<Tiaoji> getTiaojiInfo(int mid);
	public List<Tips> getTiaojiTiezi(int sid);
	public boolean validAim(int sid,int ceid,int mid);
}
