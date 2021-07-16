package cn.yangzq.docoder.content;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
*@author yangzq
*@description 内容服务模块
**/
@EnableTransactionManagement
@EnableDiscoveryClient
@MapperScan(value = {"cn.yangzq.docoder.content.mapper"})
@SpringBootApplication
public class DocoderContentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocoderContentServiceApplication.class, args);
	}

}
