package com.dongnaoedu.springcloud.wms.db;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dongnaoedu.springcloud.wms.domains.GoodsDomain;

public interface GoodsRepository extends PagingAndSortingRepository<GoodsDomain, Long> {
}