package com.dongnaoedu.springcloud.wms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.dongnaoedu.springcloud.wms.db.GoodsRepository;
import com.dongnaoedu.springcloud.wms.domains.GoodsDomain;

@Component
public class GoodsService {
	static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	GoodsRepository goodsRepository;
	/**锁定某商品库存*/
	public void lock(long goodsId) {
		int c = 0;
		// 乐观锁，如果版本不一致，则再次获取
		while (c == 0) {
			/**version = version + 1表示 每次update，版本号都会加1；*/
			GoodsDomain goodsDomain = goodsRepository.findOne(goodsId);
			/**这个语句，同一时刻只有一个线程执行成功*/
			c = jdbcTemplate.update("update tb_goods set stock_count = stock_count - 1, version = version + 1 where goods_id=" + goodsId
					+ " and version=" + goodsDomain.getVersion());
		}
		logger.debug("锁定商品，编号为：{}，库存-1", goodsId);
	}

	public void release(long goodsId) {
		int c = 0;
		// 乐观锁，如果版本不一致，则再次获取
		// update <table> set xx=xx where version = <version>
		while (c == 0) {
			// 获取版本号
			GoodsDomain goodsDomain = goodsRepository.findOne(goodsId);
			c = jdbcTemplate.update("update tb_goods set stock_count = stock_count + 1, version = version + 1 where goods_id=" + goodsId
					+ " and version=" + goodsDomain.getVersion());
		}

		logger.debug("释放库存锁定，商品编号为：{},，库存+1", goodsId);
	}

}
