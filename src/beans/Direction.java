package beans;

import java.io.Serializable;

public class Direction implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int sid;
 private int ceid;
 private int mid;
 private int did;
 private String dname;
 private String teacher;
 private String note;
 private String politics;
 private String foreignLanguage;
 private String major1;
 private String major2;
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
public int getDid() {
	return did;
}
public void setDid(int did) {
	this.did = did;
}
public String getDname() {
	return dname;
}
public void setDname(String dname) {
	this.dname = dname;
}

public String getNote() {
	return note;
}
public void setNote(String note) {
	this.note = note;
}
public String getPolitics() {
	return politics;
}
public void setPolitics(String politics) {
	this.politics = politics;
}
public String getForeignLanguage() {
	return foreignLanguage;
}
public void setForeignLanguage(String foreignLanguage) {
	this.foreignLanguage = foreignLanguage;
}
public String getMajor1() {
	return major1;
}
public void setMajor1(String major1) {
	this.major1 = major1;
}
public String getMajor2() {
	return major2;
}
public void setMajor2(String major2) {
	this.major2 = major2;
}
 public Direction(int sid,int ceid,int mid,int did,String dname){
	 this.sid = sid;
	 this.ceid = ceid;
	 this.mid = mid;
	 this.did = did;
	 this.dname = dname;
 }
public String getTeacher() {
	return teacher;
}
public void setTeacher(String teacher) {
	this.teacher = teacher;
}
 
}
