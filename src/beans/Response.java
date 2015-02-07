package beans;

import java.io.Serializable;

public class Response implements Serializable {
	private int tid;// ���ӱ��
	private String content;// ��������
	private String timestamp;// ����ʱ��
	private Personinfo host;// ��������Ϣ
	private Personinfo guest;// ���ظ�����Ϣ
	private int rid; // �ظ���id
	private String response;// �ظ�����һ���ľ�������
	private String url = "";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Personinfo getHost() {
		return host;
	}

	public void setHost(Personinfo host) {
		this.host = host;
	}

	public Personinfo getGuest() {
		return guest;
	}

	public void setGuest(Personinfo guest) {
		this.guest = guest;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

}
