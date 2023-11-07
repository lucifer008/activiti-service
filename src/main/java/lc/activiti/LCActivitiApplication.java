package lc.activiti;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@ComponentScan(basePackages= {"lc.activiti"})
//或者在Mapper映射接口上加@Mapper注解,可传多包
//@MapperScan("lc.activiti.contract.dao*","lc.activiti.xx.dao*")
@EnableScheduling
@MapperScan({"lc.activiti.contract.dao*","lc.activiti.common.dao*","lc.activiti.expense.dao*"})
public class LCActivitiApplication {
    public static  void main(String[] args){
        SpringApplication.run(LCActivitiApplication.class,args);
    }
}
