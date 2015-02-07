package services;

import java.util.List;

import beans.Announce;
import beans.Direction;
import beans.Major;
import beans.NewMajor;
import beans.Student;
import beans.Tips;
import beans.VersionInfo;

public interface ServiceInterface {


	public abstract int login(Student stu);

	public abstract void updatelastlogin(Student stu);
	public int registe(Student stu,Major major);

	public abstract boolean addAim(Student stu, Major major);

	public abstract boolean updateAim(Student stu, Major major);

	public abstract boolean addinfo(Student stu);

	public abstract boolean updatepersoninfo(Student stu, Major major);
	public boolean userExist(String id);


	/**
	 * op=0:all
	 * op=1:friends
	 * op=2:from same place(city,province)
	 * op=3:from same school
	 * op=4:aim at same
	 * op=5:blacklist
	 * 
	 */
	public abstract int countAll(int op, Student stu);

	public abstract List<Student> sameplace(Student stu, int page, int rp);

	public abstract List<Student> fromSameSchool(Student stu,int page,int rp);

	public abstract List<Student> aimatSameSchool(Student stu, int page, int rp,int op);

	//	2 * 6378.137*ASIN(SQRT(POW(SIN(PI()*(latitude-?)/360),2)+ 
	//COS(PI()* latitude/180)*COS(?*PI()/180)*POW(SIN(PI()*(longitude-(?))/360),2)))
	public abstract double distance(double longt1, double lat1,
			double longt2, double lat2);

	public abstract Student getStudent(String email);

	public abstract void updateLocation(Student stu);

	public abstract List<Student> randinfo(Student stu, int page, int rp);

	public abstract List<Student> getBlackList(String email, int page, int rp);


	public abstract List<Student> getFriends(Student user, int page, int rp);

	public abstract void addFriends(String from, String to);

	public abstract void addToBlackList(String from, String to);

	public abstract void deleteFromBlackList(String from, String to);

	public abstract void deleteFriends(String from, String to);


	public abstract void insertPhoto(String path, String email);

	public abstract void toconfirm(String email, String path);

	public abstract void addcomments(String email, String msg);

	public abstract NewMajor getmajordetail(NewMajor major);

	public abstract String getimage(String email);

	public abstract int confirmFinish(String email);
	
	public VersionInfo getVersionInfo();
	public boolean addAccusation(String host,String guest,String msg);
	public List<Announce> getAnnounces(String sendtime);
	public int addOrDelConcern(String email,Major major,int op);
	public List<Major> getConcerns(String email);
	public List<Student>searchStudent(String key);
	
	public List<Student>lastLoginUsers(String email,int page,int rp);
	
	public List<Tips> getTips(Major major,int page,int rp,int kind);
	public int getTipCount(Major major,int kind);
	public Direction getDirectionInfo(int sid,int ceid,int mid,int did);
	
	public void saveRandomData(String phone,String num);
	public int checkRandomData(String phone,String num);
	

}