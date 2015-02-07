package beans;

import java.io.Serializable;

public class Tips implements Serializable{
	private String url;
	private String title;
	private String createdtime;
	private String author;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(String createdtime) {
		this.createdtime = createdtime;
	}

	public Tips() {
	}
     
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Tips(String title, String url, String createdtime,String author) {
		this.title = title;
		this.url = url;
		this.createdtime = createdtime;
		this.author = author;
	}
	
}
