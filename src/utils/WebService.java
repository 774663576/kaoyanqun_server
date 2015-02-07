package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class WebService {
	private static final Logger log = Logger.getLogger("huanxin_service");
    public  static boolean registerForHuanxin(String email){
	  try{
	  String token = getToken();
	 
	  String account = DigestUtils.md5Hex(email);
	  String hpasswd = DigestUtils.md5Hex("kaoyanqun");
	  Map<String,Object> params = new HashMap<String, Object>();
	  params.put("username",account);
	  params.put("password", hpasswd);
	  String reqURL="https://a1.easemob.com/jddl31c108/kaoyanqun/users";
	  String result = HttpsUtils.sendSSLRequest(reqURL, token, HttpsUtils.Map2Json(params),
				HttpsUtils.Method_POST);
	  log.info("register with huanxin success "+account+":"+hpasswd+",res="+result);
	   return true;
	  }catch(Exception e){
		  log.error("huanxin register error "+e.getMessage());
	  }
     return false;	  
  }
  
  public static String getToken(){
	  Map<String,Object> params = new HashMap<String,Object>();
	  params.put("grant_type", "client_credentials");
	  params.put("client_id", "YXA64mEn0ABMEeS20RfRmfDoVw");
	  params.put("client_secret", "YXA6TNt-9doin4rS8LzpPVOw7Qoi7dY");
	  String reqURL="https://a1.easemob.com/jddl31c108/kaoyanqun/token";
	  String result = HttpsUtils.sendSSLRequest(reqURL, null, HttpsUtils.Map2Json(params),
				HttpsUtils.Method_POST);
	  JSONObject obj = JSONObject.fromObject(result);
	
	 return obj.get("access_token").toString();
  }
  public static void addUserToGroup(String id,String groupid){
	 String reqURL = "https://a1.easemob.com/jddl31c108/kaoyanqun/chatgroups/"+groupid+"/users/"+id;
	 String result = HttpsUtils.sendSSLRequest(reqURL, getToken(), "",
				HttpsUtils.Method_POST);
	 log.info("addtogroup "+id+","+groupid+":"+result);
  }
  public static void deleteUserFromGroup(String id,String groupid){
	  String reqURL = "https://a1.easemob.com/jddl31c108/kaoyanqun/chatgroups/"+groupid+"/users/"+id;
		 String result = HttpsUtils.sendSSLRequest(reqURL, getToken(), "",
					HttpsUtils.Method_DELETE);
		 log.info("delfromgroup "+id+","+groupid+":"+result);

  }
  public static void sendMsgForNewAccount(String id,String name){
	  String reqURL = "https://a1.easemob.com/jddl31c108/kaoyanqun/messages/";
	  String body="{\"target_type\":\"users\",\"target\":[\""+id+"\"],\"msg\":{\"type\":\"txt\",\"msg\":\""+name+",你好，欢迎来到超级考研群，有问题可以向我咨询，遇到违规账户也可以向我举报。\"},\"from\":\"yan\"}";
		 String result = HttpsUtils.sendSSLRequest(reqURL, getToken(), body,
					HttpsUtils.Method_POST);
      log.info("send msg to "+id);
		 
  }
public static void sendRandom(String phone,String data){
	 try {
		  String PostData = "account=C108&password=q1q2w3e4r&mobile="+phone+"&content="+java.net.URLEncoder.encode("您的验证码是："+data+"。请不要把验证码泄露给其他人。如非本人操作，可不用理会！","utf-8");
		  URL url = new URL("http://sms.106jiekou.com/utf8/sms.aspx");
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("POST");
         conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         conn.setRequestProperty("Connection", "Keep-Alive");
         conn.setUseCaches(false);
         conn.setDoOutput(true);

         conn.setRequestProperty("Content-Length", "" + PostData.length());
         OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
         out.write(PostData);
         out.flush();
         out.close();

       
         if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
             System.out.println("connect failed!");
         }
         String line="";
         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
         while ((line = in.readLine()) != null) {
             log.info(line);
         }
         in.close();
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
 public static void main(String args[]){
	 //addUserToGroup("18560875421", "1413616586720814");
	// deleteUserFromGroup("18560875421", "1413616586720814");
	 System.out.println(DigestUtils.md5Hex("kaoyanqun"));
	  
 }
  
}
