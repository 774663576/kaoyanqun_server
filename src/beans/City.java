package beans;

import java.io.Serializable;

public class City implements Serializable{
	
	private static final long serialVersionUID = 2707547683020004152L;
	private int pid;
	private String cname;
	private int cid;
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public City(){}
	public City(int pid,String cname,int cid){
		this.pid = pid;
		this.cname = cname;
		this.cid = cid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
	
}