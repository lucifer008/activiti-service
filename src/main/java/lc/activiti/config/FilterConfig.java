package lc.activiti.config;

import javax.servlet.Filter;

import lc.activiti.filter.ServiceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	
	@Bean(name = "requestFilter")
	public Filter sessionFilter() {
		return new ServiceFilter();
	}
}
