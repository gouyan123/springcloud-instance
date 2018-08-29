/**
 * 
 */
package com.dongnaoedu.springcloud.order;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongnaoedu.springcloud.order.client.SmsServiceClient;
import com.dongnaoedu.springcloud.order.client.WmsServiceClient;
import com.dongnaoedu.springcloud.order.db.OrderRepository;
import com.dongnaoedu.springcloud.order.domains.OrderDomain;
import com.dongnaoedu.springcloud.order.events.OrderCancelEvent;
import com.dongnaoedu.springcloud.order.events.OrderCreateEvent;
import com.dongnaoedu.springcloud.order.stream.OrderOutputChannel;

@Service
@Transactional(rollbackFor=Throwable.class)/**回滚*/
public class OrderService {
	static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	Tracer tracer;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	WmsServiceClient wmsServiceClient;

	@Autowired
	SmsServiceClient smsServiceClient;

	@Autowired
	ApplicationContext ctx;

	@Autowired
	OrderOutputChannel orderOutputChannel;

	public void save(OrderDomain order) throws Exception {
		// 保存订单，用来做数据库操作，spring jpa结合hibernate
		orderRepository.save(order);
		// 库存锁定，lock()方法请求wms-service服务的 /wms/lock/{goodsId}接口
		String lockResult = wmsServiceClient.lock(order.getGoodsId());
		if (lockResult == null) {
			throw new Exception("下单失败[请联系客服]");
		}
		/**MQ异步发送短信，spring cloud stream默认集成了rabbitmq和kafka*/
		smsServiceClient.sendNewOrderSms(order);

		// 发布一个取消订单的事件，订阅的人自行处理，发给邮件系统
		Object eventType = OrderCreateEvent.class.getSimpleName();
		orderOutputChannel.output().send(MessageBuilder.createMessage(new OrderCreateEvent(order),
				new MessageHeaders(Collections.singletonMap("event-type", eventType))));

	}

	public void delete(String orderId) throws Exception {
		OrderDomain orderDomain = orderRepository.findOne(orderId);
		orderRepository.delete(orderDomain);

		// 库存解锁
		String releaseResult = wmsServiceClient.release(orderDomain.getGoodsId());
		if (releaseResult == null) {
			throw new Exception("取消订单失败[请联系客服]");
		}
		// 异步发送短信
		smsServiceClient.sendCancelOrderSms(orderDomain);

		// 发布一个取消订单的事件
		Object eventType = OrderCancelEvent.class.getSimpleName();
		orderOutputChannel.output().send(MessageBuilder.createMessage(new OrderCancelEvent(orderDomain),
				new MessageHeaders(Collections.singletonMap("event-type", eventType))));
	}
}
