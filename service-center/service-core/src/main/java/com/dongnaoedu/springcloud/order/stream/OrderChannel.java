package com.dongnaoedu.springcloud.order.stream;

import org.springframework.stereotype.Component;

@Component
public interface OrderChannel extends OrderInputChannel,OrderOutputChannel{
	String CHANNEL = "order-event";
}
