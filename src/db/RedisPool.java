package db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
	    private static RedisPool instance = new RedisPool();
	    private static JedisPool jedisPool = null;
	    private static final String REDIS_HOST ="localhost";
	    private static final int REDIS_PORT = 6379; //redis默认端口
	    
	    // 单例
	    private RedisPool() {
	    	
	    }
	    
	    public static final RedisPool getInstance() {
	    	return instance;
	    }
	    
	    // 初始化
	    public void Init() {
	    	if (jedisPool == null) {
	    		JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxActive(50);
				config.setMaxIdle(50);
				config.setMaxWait(1000);
				config.setTestOnBorrow(false);
				config.setTestWhileIdle(false);
				
				/**如果为true，表示有一个idle object evitor线程对idle object进行扫描，
				 * 如果validate失败，此object会被从pool中drop掉；
				 * 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；**/
				//config.setTestWhileIdle(true);
				/**表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；
				 * 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；**/
				//config.setMinEvictableIdleTimeMillis(60000);
				/**表示idle object evitor两次扫描之间要sleep的毫秒数**/
				//config.setTimeBetweenEvictionRunsMillis(30000);
				/**表示idle object evitor每次扫描的最多的对象数**/
				//config.setNumTestsPerEvictionRun(-1);
				
				jedisPool = new JedisPool(config, REDIS_HOST, REDIS_PORT);
	    	}
	    }
	    
	    // 取出redis连接
	    public synchronized Jedis getConnection() throws Exception  {
	    	Jedis jedis = null;
	    	try {
	    		JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxActive(50);
				config.setMaxIdle(50);
				config.setMaxWait(1000);
				config.setTestOnBorrow(false);
				config.setTestWhileIdle(false);
				jedisPool = new JedisPool(config, REDIS_HOST, REDIS_PORT);
	    		jedis = jedisPool.getResource();
	    		jedis.getClient().setTimeoutInfinite();
	    	} catch (Exception e) {
	    		System.out.println("[getConnection]" + " error:" + e.getMessage());
	    		if (jedis != null) {
	    			jedisPool.returnBrokenResource(jedis);
	    		}
	    		e.printStackTrace();
	    		throw new Exception(e);
	    	}
	    	return jedis;
	    }
	    
	    // 回收redis连接
	    public synchronized void releaseConnetion(Jedis jedis) {
	    	if (jedis != null) {
	    		jedisPool.returnResource(jedis);
	    	}
	    }
	

}
