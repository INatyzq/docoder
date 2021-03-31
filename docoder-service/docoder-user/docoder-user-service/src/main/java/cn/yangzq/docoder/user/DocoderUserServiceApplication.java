package cn.yangzq.docoder.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
*@author yangzq
*@description USER服务模块
**/
@EnableTransactionManagement
@EnableDiscoveryClient
@MapperScan(value = {"cn.yangzq.docoder.user.mapper"})
@SpringBootApplication
public class DocoderUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocoderUserServiceApplication.class, args);
	}

}
