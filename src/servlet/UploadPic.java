package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import services.ServiceInterface;
import utils.RESULT;
import embodyservices.UserDao;

public class UploadPic extends HttpServlet {
	String imageSavePath;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public UploadPic() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		imageSavePath = this.getServletConfig().getServletContext()
				.getRealPath("/images")
				+ File.separator;
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println(imageSavePath);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("serlvet start!!-----------");
		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		String path = request.getContextPath();
		String preurl = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/images/";
		String serverPath = "";
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(imageSavePath));
		factory.setSizeThreshold(4096);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(10 * 1024 * 1024);
		int imgIndex = 1;
		ServiceInterface dao = new UserDao();
		try {
			List fileItems = upload.parseRequest(request);
			System.out.println("filesize::::::" + fileItems.size());
			Iterator iter = fileItems.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					request.setAttribute(name, value);
				} else {
					String value = item.getName();
					String fileName = System.currentTimeMillis()
							+ "-"
							+ imgIndex
							+ value.substring(value.length() - 4,
									value.length());
					imgIndex++;
					File file = new File(imageSavePath, fileName);
					item.write(file);
					serverPath = preurl + fileName;
				}
			}
		} catch (Exception e) {
			System.out.println("1" + e.toString());
			e.printStackTrace();
		}
		String email = request.getAttribute("email").toString();
		dao.insertPhoto(serverPath, email);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", RESULT.SUCCESS);
		map.put("url", serverPath);
		map.put("timestamp", System.currentTimeMillis());
		JSONObject jsonObjectFromMap = JSONObject.fromObject(map);
		PrintWriter out = response.getWriter();
		out.print(jsonObjectFromMap.toString());
		out.flush();
		out.close();
		System.out.println("serlvet end!!-----------");
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}