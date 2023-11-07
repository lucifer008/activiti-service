package lc.activiti.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = { "lc.activiti.dao*" }, sqlSessionTemplateRef = "masterSqlSessionTemplate")
public class MasterSourceConfig {
	@Bean(name = "masterDataSource")
	@ConfigurationProperties("spring.datasource")
	@Primary
	public DataSource masterDataSource() {
		//return DataSourceBuilder.create().build();
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "masterSqlSessionFactory")
	@Primary // 主数据源
	public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);

		List<Resource> resourceList = new ArrayList<>();
		// 合同相关
		Resource[] baseResources = new PathMatchingResourcePatternResolver()
				.getResources("classpath*:lc/activiti/mapper/base/*.xml");
		Resource[] resources = new PathMatchingResourcePatternResolver()
				.getResources("classpath*:lc/activiti/mapper/*.xml");


		resourceList.addAll(Arrays.asList(baseResources));
		resourceList.addAll(Arrays.asList(resources));


		sessionFactoryBean.setMapperLocations(resourceList.toArray(new Resource[resourceList.size()]));
		return sessionFactoryBean.getObject();
	}

	@Bean(name = "masterSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate masterSqlSessionTemplate(
			@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
