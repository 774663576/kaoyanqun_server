package beans;

import java.io.Serializable;

public class Major implements Serializable{
  
  private static final long serialVersionUID = 1L;
  private int mid;
  private String mname;
  private int ceid;
  private int sid;
  private String cename;
  private String sname;
  private String year;
  private String ssid;
  private String sceid;
  private String groupid;//huanxin groupid
  private String timestamp;

public String getYear() {
	return year;
}
public void setYear(String year) {
	this.year = year;
}
public String getCename() {
	return cename;
}
public void setCename(String cename) {
	this.cename = cename;
}
public String getSname() {
	return sname;
}
public void setSname(String sname) {
	this.sname = sname;
}
public int getMid() {
	return mid;
}
public void setMid(int mid) {
	this.mid = mid;
}
public String getMname() {
	return mname;
}
public void setMname(String mname) {
	this.mname = mname;
}
public int getCeid() {
	return ceid;
}
public void setCeid(int ceid) {
	this.ceid = ceid;
}
public int getSid() {
	return sid;
}
public void setSid(int sid) {
	this.sid = sid;
}



public String getSsid() {
	return ssid;
}
public void setSsid(String ssid) {
	this.ssid = ssid;
}
public String getSceid() {
	return sceid;
}
public void setSceid(String sceid) {
	this.sceid = sceid;
}

public String getGroupid() {
	return groupid;
}
public void setGroupid(String groupid) {
	this.groupid = groupid;
}
public String getTimestamp() {
	return timestamp;
}
public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}


}
