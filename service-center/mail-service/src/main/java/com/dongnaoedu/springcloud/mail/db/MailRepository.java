package com.dongnaoedu.springcloud.mail.db;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dongnaoedu.springcloud.mail.domains.MailDomain;

public interface MailRepository extends PagingAndSortingRepository<MailDomain, Long> {
}