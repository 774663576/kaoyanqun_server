package beans;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {
	private int tid;// ���ӱ��
	private String title; // �������
	private Personinfo louzhu;// ��������Ϣ
	private String timestamp;// ����ʱ��
	private List<Response> responses;// �ظ�����

	private int sid; // ѧУ���
	private int ceid;// ѧԺ���
	private int mid;// רҵ���
	private String sname;// ѧУ���
	private String cename;// ѧԺ���
	private String mname;// רҵ���
	private int total;
	private List<String> urls;// pictures in topics

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<Response> getResponses() {
		return responses;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
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

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

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

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public Personinfo getLouzhu() {
		return louzhu;
	}

	public void setLouzhu(Personinfo louzhu) {
		this.louzhu = louzhu;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

}
