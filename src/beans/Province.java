package beans;

public class Province {
  private int pid;
  private String pname;
  
  public Province(){}
  public Province(int pid,String pname){
	  this.pid =pid;
	  this.pname = pname;
  }
public int getPid() {
	return pid;
}
public void setPid(int pid) {
	this.pid = pid;
}
public String getPname() {
	return pname;
}
public void setPname(String pname) {
	this.pname = pname;
}
  
  
}
