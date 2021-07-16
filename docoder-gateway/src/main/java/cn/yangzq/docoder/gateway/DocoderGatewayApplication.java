package cn.yangzq.docoder.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DocoderGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocoderGatewayApplication.class, args);
	}

}
