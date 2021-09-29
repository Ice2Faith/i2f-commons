package com.i2f.dao;

import java.lang.*;
import java.util.*;
import java.sql.*;
import org.apache.ibatis.annotations.*;
import com.i2f.model.NotetBean;

/**
 * @author Ugex.Savelar
 * @date 2021-09-29 14:50:40 566
 */
public interface NotetDao {
	List<NotetBean> queryList(@Param("bean") NotetBean bean);
	int insertList(@Param("list") List<NotetBean> list);
	int updateOne(@Param("bean") NotetBean bean);
	int deleteOne(@Param("bean") NotetBean bean);

}
