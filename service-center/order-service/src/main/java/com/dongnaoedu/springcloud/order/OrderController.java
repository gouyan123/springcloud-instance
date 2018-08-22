package com.dongnaoedu.springcloud.order;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.springcloud.order.db.OrderRepository;
import com.dongnaoedu.springcloud.order.domains.OrderDomain;

// 订单服务
@RestController
@RequestMapping("/order")
public class OrderController {
	static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@RequestMapping("/new/{goodsId}")// 商品id为1，因为商品表里就一个商品
	/**@PathVariable("goodsId") long goodsId 获取http请求中的 路径变量 赋值给 long goodsId
	 * @RequestHeader(name = "phone") String phone 获取请求头中的 phone的值，并赋值给String phone
	 * */
	public String add(@PathVariable("goodsId") long goodsId, @RequestHeader(name = "phone") String phone,
			@RequestHeader(name = "email") String email) throws Exception {
		// 新增订单
		OrderDomain order = new OrderDomain();
		order.setOrderId(UUID.randomUUID().toString());
		order.setCreateTime(new Date());
		order.setEmail(email);
		order.setPhone(phone);
		order.setGoodsId(goodsId);
		orderService.save(order);
		
		return "下单成功，这个响应来自(新版)订单服务器：" + applicationContext.getEnvironment().getProperty("HOSTNAME");
	}

	// 取消订单
	// 这里没做校验
	@RequestMapping("/cancel/{orderId}")
	public String cancel(@PathVariable("orderId") String orderId) throws Exception {
		orderService.delete(orderId);
		return "取消订单成功，这个响应来自(新版)订单服务器：" + applicationContext.getEnvironment().getProperty("HOSTNAME");
	}

	// 根据手机号码查订单
	@RequestMapping("/orders")
	public List<OrderDomain> orders(@RequestHeader(name = "phone") String phone) {
		return orderRepository.findByPhone(phone);
	}
}
