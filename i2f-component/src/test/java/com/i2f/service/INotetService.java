package com.i2f.service;

import java.lang.*;
import java.util.*;
import java.sql.*;
import com.i2f.model.NotetBean;

/**
 * @author Ugex.Savelar
 * @date 2021-09-29 14:50:40 572
 */
public interface INotetService {
	List<NotetBean> queryList(NotetBean bean);
	boolean insert( List<NotetBean> list);
	boolean update( NotetBean bean);
	boolean delete( NotetBean bean);

}
