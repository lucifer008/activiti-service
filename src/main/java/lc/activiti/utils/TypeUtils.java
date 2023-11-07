package lc.activiti.utils;

public class TypeUtils {
	public static <T> T as(Class<T> clazz, Object o){
	    if(clazz.isInstance(o)){
	        return clazz.cast(o);
	    }
	    return null;
	}
}
