package action;

import java.util.HashMap;
import java.util.Map;

import services.ServiceInterface;
import utils.RESULT;
import beans.Major;

import com.opensymphony.xwork2.ActionSupport;

import embodyservices.UserDao;

public class TipAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private int kind;
	private Major major;
	private ServiceInterface dao = new UserDao();
	private Map<String, Object> map = new HashMap<String, Object>();
    private int page;
    private int rp;
	public String getTips() {
		map.clear();
		int total = dao.getTipCount(major, kind);
		map.put("total", total);
		if(total>0){
			map.put("dizcuz", dao.getTips(major,  page, rp,kind));
			int size = total / rp + (total % rp == 0 ? 0 : 1);
			map.put("totalpage", size);
		}
		map.put("result", RESULT.SUCCESS);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRp() {
		return rp;
	}

	public void setRp(int rp) {
		this.rp = rp;
	}
    
}
