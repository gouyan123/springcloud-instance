package com.dongnaoedu.springcloud.order.db;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.dongnaoedu.springcloud.order.domains.OrderDomain;

public interface OrderRepository extends PagingAndSortingRepository<OrderDomain, String> {
	// 根据手机号查找
	List<OrderDomain> findByPhone(String phone);
}