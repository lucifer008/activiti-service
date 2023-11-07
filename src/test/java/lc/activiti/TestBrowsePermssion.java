package lc.activiti;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import lc.activiti.lcenum.LCExceptionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestBrowsePermssion {
	@Test
	public void testGuid() {
		String guid=UUID.randomUUID().toString();
		System.out.println(guid.replace("-", "").length());
	}
	@Test
	public void testExpetion() {
		try {
			Map<String, Object> maps=null;
//			if (null==maps) {
//				throw new RuntimeException("ssss");
//			}
			maps.get("1");
			
		}catch (Exception e) {
			String exMessage=LCExceptionUtils.getExpetionMessage(e);
			System.out.println(exMessage);
		}
		
	}
	@Test
	public void testArray() {
		List<String> eList=new ArrayList<>();
		eList.add("1");
		eList.add("2");
		eList.add("3");
		//List<String> tList=new ArrayList<>();
		log.info("List--->{}",eList);
		//System.arraycopy(eList, 1, tList, 0, 3);
		
	}
}
