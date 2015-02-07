package beans;

import java.io.Serializable;

public class College implements Serializable {
  private  String cename;
  private int sid;
  private int ceid;
  private String sname;
public String getSname() {
	return sname;
}
public void setSname(String sname) {
	this.sname = sname;
}
public String getCename() {
	return cename;
}
public void setCename(String cename) {
	this.cename = cename;
}
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
  
}
