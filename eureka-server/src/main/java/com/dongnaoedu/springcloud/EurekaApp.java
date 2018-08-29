package com.dongnaoedu.springcloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApp{
	final static Logger logger = LoggerFactory.getLogger(EurekaApp.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(EurekaApp.class).web(true)
				.run(args);
		logger.debug("eureka已经启动,当前host：{}", applicationContext.getEnvironment().getProperty("HOSTNAME"));
	}
}
