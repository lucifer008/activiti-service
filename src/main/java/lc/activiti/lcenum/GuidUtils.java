package lc.activiti.lcenum;

import java.util.UUID;

public class GuidUtils {
	public static String newGuid() {
		String guid=UUID.randomUUID().toString();
		return guid.replace("-", "");
	}
}
