package myinterceptors;

import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class LoggerInterceptor implements Interceptor {

	final static Logger log=Logger.getLogger("interceptor");
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> params = invocation.getInvocationContext().getParameters();
		StringBuffer buffer = new StringBuffer();
		buffer.append(invocation.getAction().getClass().getName()+"\t");
		buffer.append(invocation.getProxy().getMethod()+"\t");
		for(String key:params.keySet()){
			Object obj = params.get(key);
			 if(obj instanceof String[]){  
				 String[] arr = (String[]) obj; 
				 buffer.append(key+":");
				 for(String str:arr){
					 buffer.append(str+",");
				 }
				 buffer.append("\t");
			 }
		}
		log.info(buffer.toString());
		return invocation.invoke();
	}

}
