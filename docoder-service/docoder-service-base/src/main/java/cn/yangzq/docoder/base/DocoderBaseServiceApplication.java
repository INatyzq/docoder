package cn.yangzq.docoder.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableDiscoveryClient
@MapperScan(value = {"cn.yangzq.docoder.base.mapper"})
@SpringBootApplication
public class DocoderBaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocoderBaseServiceApplication.class, args);
	}

}
