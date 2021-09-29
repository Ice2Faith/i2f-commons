package com.i2f.controller;

import java.lang.*;
import java.util.*;
import java.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.*;
import com.i2f.service.INotetService;
import com.i2f.model.NotetBean;

/**
 * @author Ugex.Savelar
 * @date 2021-09-29 14:50:40 578
 */
@RestController
@RequestMapping("Notet")
public class NotetController {
	@Autowired
	private INotetService beanService;
	@RequestMapping("queryList")
	public List<NotetBean> queryList(NotetBean bean) {
		List<NotetBean> list=beanService.queryList(bean);
		return list;
	}
	@RequestMapping("insert")
	public boolean insert( List<NotetBean> list) {
		boolean success=beanService.insert(list);
		return success;
	}
	@RequestMapping("update")
	public boolean update( NotetBean bean) {
		boolean success=beanService.update(bean);
		return success;
	}
	@RequestMapping("delete")
	public boolean delete( NotetBean bean) {
		boolean success=beanService.delete(bean);
		return success;
	}

}
