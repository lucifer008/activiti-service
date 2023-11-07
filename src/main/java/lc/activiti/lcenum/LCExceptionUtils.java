package lc.activiti.lcenum;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;



public class LCExceptionUtils {
	public static String getExpetionMessage(Exception ex) {
		if (null==ex.fillInStackTrace()) {
			return ex.getMessage();
		}
		String message = ExceptionUtils.getMessage(ex.fillInStackTrace());
//		if (StringUtils.isBlank(message) && null != ex.fillInStackTrace()) {
//			Throwable[] errTrace = ExceptionUtils.getThrowables(ex.fillInStackTrace());
//		}
		//List<String> errList=ExceptionUtils.getStackFrameList(ex.fillInStackTrace());
		return message;
	}

}
