package beans;

import java.util.List;

public class NewMajor extends Major{
  private int sid;
  private int ceid;
  private int mid;
  private String mname;
  private String plan;
  private String groupid;
  private String cename;
  private String sname;
  private List<Direction> directions;
public int getSid() {
	return sid;
}
public void setSid(int sid) {
	this.sid = sid;
}
public int getCeid() {
	return ceid;
}
public void setCeid(int ceid) {
	this.ceid = ceid;
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
public String getPlan() {
	return plan;
}
public void setPlan(String plan) {
	this.plan = plan;
}
public String getGroupid() {
	return groupid;
}
public void setGroupid(String groupid) {
	this.groupid = groupid;
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
public List<Direction> getDirections() {
	return directions;
}
public void setDirections(List<Direction> directions) {
	this.directions = directions;
}
  
  
}
