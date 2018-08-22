package com.dongnaoedu.springcloud.sms.db;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dongnaoedu.springcloud.sms.domains.SmsDomain;

public interface SmsRepository extends PagingAndSortingRepository<SmsDomain, Long> {
}