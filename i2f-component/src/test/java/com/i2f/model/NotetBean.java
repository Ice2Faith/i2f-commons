package com.i2f.model;

import java.lang.*;
import java.util.*;
import java.io.Serializable;
import java.sql.*;
import lombok.*;

/**
 * @author Ugex.Savelar
 * @date 2021-09-29 14:50:40 537
 */
@Data
@NoArgsConstructor
public class NotetBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String viceTitle;
	private String headImg;
	private String attachFile;
	private String content;
	private Timestamp createDate;
	private Timestamp modifyDate;
	private Integer updateCount;
	private Integer viewCount;

}
