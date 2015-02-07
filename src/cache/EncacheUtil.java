package cache;

import net.sf.ehcache.CacheManager;

public class EncacheUtil {
	private static CacheManager  cacheManager = null; 
    private EncacheUtil(){}
   static{       
       cacheManager = CacheManager.create();   
	   //cacheManager = new CacheManager("ehcache.xml");
   }   
      
   public static CacheManager getCacheManager(){   
       return cacheManager;   
   }   
}
