package utils;

import org.apache.commons.codec.digest.DigestUtils;

public class StringUtils {
  public static String getMD5Result(String str) {
	  return DigestUtils.md5Hex(str);
  }
  public static boolean isEmpty(String str){
	  if(str == null || str.trim().equals("")){
		  return true;
	  }
	  return false;
  }
}
