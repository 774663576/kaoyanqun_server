package beans;

import java.io.Serializable;


public class Student implements Serializable{
 
  private static final long serialVersionUID = 359602961462861885L;
  private String nickname;
  private String email;
  private String password;
  private String gender;//F -->female,and M-->true
  
  private String image;//the path of his/her image
  private int enterTime;//the year of entertime
  private int session;//the year of examination
  private Major major;//bachelor info
  private String status;//success or nothing
  private String city;
  private String pid;
  private String pname;
  private String cname;
  private Major aim;
  private int relation;
  private String declaration;
  private String senter;
  
  private String howgoing;
  private double longitude;
  private double latitude;
  private String location;
  private double distance;
  private int confirm;//0-->haven't upload any material
                      //1-->bing dealt with
                      //2-->confirm
                      //refuse
  
  private int role;//0-->want to enter for the undergraduation examination
                   //1--> undergraduate
                   //5-->administrator
  private String scores;
 
public String getNickname() {
	return nickname;
}
public void setNickname(String nickname) {
	this.nickname = nickname;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public int getEnterTime() {
	return enterTime;
}
public void setEnterTime(int enterTime) {
	this.enterTime = enterTime;
}
public int getSession() {
	return session;
}
public void setSession(int session) {
	this.session = session;
}
public Major getMajor() {
	return major;
}
public void setMajor(Major major) {
	this.major = major;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getPid() {
	return pid;
}
public void setPid(String pid) {
	this.pid = pid;
}
public String getPname() {
	return pname;
}
public void setPname(String pname) {
	this.pname = pname;
}
public String getCname() {
	return cname;
}
public void setCname(String cname) {
	this.cname = cname;
}
public Major getAim() {
	return aim;
}
public void setAim(Major aim) {
	this.aim = aim;
}
public int getRelation() {
	return relation;
}
public void setRelation(int relation) {
	this.relation = relation;
}
public String getDeclaration() {
	return declaration;
}
public void setDeclaration(String declaration) {
	this.declaration = declaration;
}
public String getHowgoing() {
	return howgoing;
}
public void setHowgoing(String howgoing) {
	this.howgoing = howgoing;
}
public double getLongitude() {
	return longitude;
}
public void setLongitude(double longitude) {
	this.longitude = longitude;
}
public double getLatitude() {
	return latitude;
}
public void setLatitude(double latitude) {
	this.latitude = latitude;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public double getDistance() {
	return distance;
}
public void setDistance(double distance) {
	this.distance = distance;
}
public int getRole() {
	return role;
}
public void setRole(int role) {
	this.role = role;
}
public String getScores() {
	return scores;
}
public void setScores(String scores) {
	this.scores = scores;
}
public int getConfirm() {
	return confirm;
}
public void setConfirm(int confirm) {
	this.confirm = confirm;
}
public String getSenter() {
	return senter;
}
public void setSenter(String senter) {
	this.senter = senter;
}

}
