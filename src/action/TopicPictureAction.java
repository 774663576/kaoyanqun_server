package action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import services.DiscussisonDao;
import utils.RESULT;

import com.opensymphony.xwork2.ActionSupport;

import db.DBConnector;
import embodyservices.DiscussionService;

public class TopicPictureAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private int tid;
	private List<File> uploads;
	private List<String> uploadsFileName;
	private List<String> uploadsContentType;
	private Map<String, Object> map = new HashMap<String, Object>();

	public String execute() throws Exception {
		List<String> pics = new ArrayList<String>();
		List<File> files = getUploads();
		String path = DBConnector.savepath + "pics/";
		DiscussisonDao dao = new DiscussionService();
		for (int i = 0; i < files.size(); i++) {
			String last = this
					.getUploadsFileName()
					.get(i)
					.substring(
							this.getUploadsFileName().get(i).lastIndexOf("."));
			String realName = System.currentTimeMillis() + last;
			String name = path + "\\" + realName;
			FileOutputStream fos = new FileOutputStream(name);
			FileInputStream fis = new FileInputStream(files.get(i));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			fis.close();
			String preurl = DBConnector.picurl + "pics/";
			String url = preurl +realName;
			dao.saveTopPic(tid, url);
			pics.add(url);
		}
		map.put("result", RESULT.SUCCESS);
		map.put("urls", pics);
		map.put("timestamp", System.currentTimeMillis());
		return SUCCESS;
	}

	public List<File> getUploads() {
		return uploads;
	}

	public void setUploads(List<File> uploads) {
		this.uploads = uploads;
	}

	public List<String> getUploadsFileName() {
		return uploadsFileName;
	}

	public void setUploadsFileName(List<String> uploadsFileName) {
		this.uploadsFileName = uploadsFileName;
	}

	public List<String> getUploadsContentType() {
		return uploadsContentType;
	}

	public void setUploadsContentType(List<String> uploadsContentType) {
		this.uploadsContentType = uploadsContentType;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

}
