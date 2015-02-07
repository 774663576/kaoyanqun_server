package db;

import java.util.List;

import redis.clients.jedis.Jedis;
import utils.RESULT;

public class RedisOP {
  public static void set(String key,String value){
	Jedis jedis = null;
	try {
		jedis = RedisPool.getInstance().getConnection();
		jedis.set(key, value);
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
	  RedisPool.getInstance().releaseConnetion(jedis);
	}
  }
  public static String getValue(String key){
	  Jedis jedis = null;
	try {
		jedis = RedisPool.getInstance().getConnection();
		String res=jedis.get(key);
		return res;
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
	  RedisPool.getInstance().releaseConnetion(jedis);
	}
	return "";
	  
  }
  public static void saveUsers(List<String> userIds){
	  Jedis jedis = null;
	  try{
		  jedis = RedisPool.getInstance().getConnection();
		  for(String str:userIds){
			  jedis.sadd("user_ids", str);
		  }
	  }catch(Exception e){
		  
	  }finally{
		  RedisPool.getInstance().releaseConnetion(jedis);
	  }
  }
  public static void saveAUser(String id){
	  Jedis jedis = null;
	  try{
		  jedis = RedisPool.getInstance().getConnection();
			jedis.sadd("user_ids", id);
	  }catch(Exception e){
		  
	  }finally{
		  RedisPool.getInstance().releaseConnetion(jedis);
	  }
  }
  public static boolean hasValue(String key){
	  Jedis jedis = null;
	  try {
		jedis = RedisPool.getInstance().getConnection();
		return jedis.exists(key);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		RedisPool.getInstance().releaseConnetion(jedis);
	}
	  return false;
  }
  
  public static boolean userExists(String userId){
	  Jedis jedis = null;
	  try {
		jedis = RedisPool.getInstance().getConnection();
		return jedis.sismember("user_ids", userId);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		RedisPool.getInstance().releaseConnetion(jedis);
	}
	  return false;
  }
  public static void setRandom(String phone,String num){
	  Jedis jedis = null;
	  try {
		jedis = RedisPool.getInstance().getConnection();
		jedis.select(5);
			jedis.set(phone, num);
			jedis.expire(phone, 60*30);
		
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		RedisPool.getInstance().releaseConnetion(jedis);
	}
	  
  }
  public static int getRandom(String phone,String num){
	  Jedis jedis = null;
	  int res = 0;
	  try {
		jedis = RedisPool.getInstance().getConnection();
		jedis.select(5);
		if(jedis.exists(phone)){
			if(jedis.get(phone).equals(num)){
				res = RESULT.SUCCESS;
			}else{
				res = RESULT.FAIL;
			}
		}else{
			res = RESULT.EMAIL_INVALID;
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		RedisPool.getInstance().releaseConnetion(jedis);
	}
	  return res;
  }
}
