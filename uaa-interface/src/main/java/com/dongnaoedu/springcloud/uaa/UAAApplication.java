package com.dongnaoedu.springcloud.uaa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableEurekaClient
public class UAAApplication {
	final static Logger logger = LoggerFactory.getLogger(UAAApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(UAAApplication.class).web(true)
				.run(args);
		logger.debug(applicationContext.getId() + "已经启动,当前host：{}",
				applicationContext.getEnvironment().getProperty("HOSTNAME"));
	}
}
