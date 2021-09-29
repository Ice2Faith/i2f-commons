package com.i2f.service.impl;

import java.lang.*;
import java.util.*;
import java.sql.*;
import org.springframework.stereotype.*;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.i2f.service.INotetService;
import com.i2f.model.NotetBean;
import com.i2f.dao.NotetDao;

/**
 * @author Ugex.Savelar
 * @date 2021-09-29 14:50:40 575
 */
@Service
public class NotetServiceImpl  implements INotetService {
	private Logger logger= LoggerFactory.getLogger(NotetServiceImpl.class);
	@Resource
	private NotetDao beanDao;
	@Override
	public List<NotetBean> queryList(NotetBean bean) {
		List<NotetBean> list=beanDao.queryList(bean);
		return list;
	}
	public boolean insert( List<NotetBean> list) {
		int effecLine=beanDao.insertList(list);
		return effecLine>0;
	}
	public boolean update( NotetBean bean) {
		int effecLine=beanDao.updateOne(bean);
		return effecLine>0;
	}
	public boolean delete( NotetBean bean) {
		int effecLine=beanDao.deleteOne(bean);
		return effecLine>0;
	}

}
