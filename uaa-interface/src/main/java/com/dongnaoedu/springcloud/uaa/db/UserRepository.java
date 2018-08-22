package com.dongnaoedu.springcloud.uaa.db;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserDomain, Long> {
	// 根据用户名和密码查找用户
	public UserDomain findByPhoneAndPassword(String phone, String password);
}