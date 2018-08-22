package com.dongnaoedu.springcloud.order.events;

import com.dongnaoedu.springcloud.order.domains.OrderDomain;

// 订单取消事件
public class OrderCancelEvent {

	public OrderDomain orderDomain;

	public OrderDomain getOrderDomain() {
		return orderDomain;
	}

	public void setOrderDomain(OrderDomain orderDomain) {
		this.orderDomain = orderDomain;
	}

	public OrderCancelEvent() {
	}

	public OrderCancelEvent(OrderDomain orderDomain) {
		super();
		this.orderDomain = orderDomain;
	}

}
