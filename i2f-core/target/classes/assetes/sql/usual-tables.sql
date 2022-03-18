-- 通用系统表设计汇总

-- 权限管理部分 -----BEGIN------------------------------------------------------------------------------------------

-- 菜单、权限表
create table sys_menu(
	id int(11) auto_increment primary key,
	name varchar(300), -- 菜单名称
	menu_type int(11) not null, -- 0.菜单 1.控件 2.后台接口
	url varchar(3000), -- 菜单URL
	permission varchar(1024), -- 权限
	parent_id int(11) not null default 0, -- 父菜单ID
	description varchar(300),
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 角色表
create table sys_role(
	id int(11) auto_increment primary key,
	name varchar(100) unique not null, -- 角色名称
	role_key varchar(300), -- 角色键，保证唯一性
	description varchar(300), -- 角色描述
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 角色菜单表
create table sys_role_menu(
	role_id int(11) not null,
	menu_id int(11) not null,
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 用户表
create table sys_user(
	id int(11) auto_increment primary key,
	account varchar(300) not null, -- 账号
	password varchar(300) not null, -- 密码
	valid int(11) not null default 1, -- 有效性
	online_status int(11) , -- 在线状态
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 用户角色表
create table sys_user_role(
	user_id int(11) not null,
	role_id int(11) not null,
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 权限管理部分 -----END------------------------------------------------------------------------------------------

-- 通用组件部分 -----BEGIN------------------------------------------------------------------------------------------

-- 字典表
create table sys_dict(
	id int(11) auto_increment primary key,
	dict_group varchar(300) not null,
	dict_type int(11) not null default 0, -- 字典值类型，0.数值 1.字符串
	dict_key_text varchar(300), -- 字符串KEY，dict_type=1时有效
	dict_key_num int(11) , --  数值key,dict=0时有效
	dict_decode varchar(3000), -- 字典解析值
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 通用组件部分 -----END------------------------------------------------------------------------------------------

-- 部门管理部分 -----BEGIN------------------------------------------------------------------------------------------

-- 部门表
create table sys_department(
	id int(11) auto_increment primary key,
	name varchar(300) not null, -- 部门名称
	parent_id int(11) not null default 0, -- 上级部门
	description varchar(300), -- 部门描述
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 用户部门表
create table sys_user_department(
	user_id int(11) not null,
	department_id int(11) not null,
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 部门管理部分 -----END------------------------------------------------------------------------------------------

-- 商城部分 -----BEGIN------------------------------------------------------------------------------------------

-- 店铺表
create table sys_market(
	id int(11) auto_increment primary key,
	user_id int(11) not null, -- 店主ID
	name varchar(300) not null, -- 店铺名称
	description varchar(300), -- 店铺描述
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 商品表
create table sys_goods(
	id int(11) auto_increment primary key,
	title varchar(300) not null, -- 标题
	detail text, -- 详情
	sale_money int(11) not null, -- 售价
	market_id int(11) not null, -- 店铺ID
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 媒体表
create table sys_media(
	id int(11) auto_increment primary key,
	name varchar(1024) , -- 媒体名称
	type_g int(11) not null default 0, -- 媒体类型，0.图片 1.视频 2.音频
	url varchar(3000) not null, -- 媒体URL
	valid int(11) not null default 1, -- 是否正常
	description varchar(300), -- 媒体描述
	create_operator varchar(100) not null,
	create_time datetime not null
);

-- 商品媒体表
create table sys_goods_media(
	goods_id int(11) not null,
	media_id int(11) not null,
	create_operator varchar(100) not null,
	create_time datetime not null
);

-- 订单表
create table sys_order(
	id int(11) auto_increment primary key,
	user_id int(11) not null, -- 买家ID
	status int(11) not null default 0, -- 订单状态 0.创建 1.支付 2.发货 3.物流 4.收货
	pay_money int(11), -- 支付金额
	user_address_id int(11) not null, -- 收货地址
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 订单明细表
create table sys_order_detail(
	id int(11) auto_increment primary key,
	order_id int(11) not null,
	goods_id int(11) not null,
	buy_count int(11) not null default 1 -- 购买数量
);

-- 购物车表
create table sys_shoppingcar(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	goods_id int(11) not null,
	in_sale_money int(11) not null, -- 加入时单价
	in_time datetime not null, -- 加入时间
	in_count int(11) not null, -- 数量
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 商城部分 -----END------------------------------------------------------------------------------------------


-- 用户地址表
create table sys_user_address(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	recv_name varchar(100) not null, -- 收货人
	recv_tel varchar(20) not null, -- 收货电话
	recv_address varchar(300) not null, -- 收货地址
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 分类表
create table sys_classification(
	id int(11) auto_increment primary key,
	name varchar(300) not null, -- 分类名称
	type_g int(11) not null default 0, -- 分类类型，可用作其他树形属性的分类
	parent_id int(11) not null default 0, -- 父类型
	description varchar(300), -- 分类描述
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 钱包表
create table sys_wallet(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	money int(11) not null default 0,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 用户基本信息表
create table sys_user_base_info(
	user_id int(11) not null,
	nick_name varchar(300), -- 昵称
	born_day datetime, -- 出生日期
	sex int(11) , -- 性别
	introduce varchar(300), -- 个人简介
	photo varchar(3000), -- 头像
	email varchar(300), -- 邮箱
	tel varchar(50) -- 电话
);

-- 操作日志表
create table sys_log(
	id int(11) auto_increment primary key,
	log_group varchar(300) not null, -- 组别
	log_level int(11) not null, -- 级别
	log_tag varchar(300), -- 标签
	log_content text, -- 内容
	create_time datetime default now()
);

-- 收藏表
create table sys_favorite(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	fav_type varchar(300) not null default 'fav', -- 收藏类型，这里可以复用用作，点赞，收藏，喜欢等
	ref_table varchar(300) not null, -- 收藏的表
	ref_id int(11) not null, -- 表里面的ID
	fav_time datetime not null default now() -- 收藏时间
);

-- 聊天表
create table sys_communication(
	id int(11) auto_increment primary key,
	send_user_id int(11) not null,
	recv_user_id int(11) not null,
	is_read int(11) not null default 0, -- 是否已读
	content text,
	send_time datetime not null default now()
);

-- 留言表
create table sys_leavemsg(
	id int(11) auto_increment primary key,
	send_name varchar(300),
	send_user_id int(11),
	type_g int(11) not null, -- 留言类型
	content text,
	send_time datetime not null default now()
);

-- 公告表
create table sys_notifacation(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	valid_time datetime not null default now(), -- 生效时间
	invalid_time datetime not null, -- 失效时间
	title varchar(300) not null, -- 标题
	content text not null, -- 内容
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 文章表
create table sys_blog(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	title varchar(300) not null, -- 标题
	content text not null, -- 内容
	key_words varchar(100), -- 关键字
	public_strategy int(11) not null, -- 公开策略，0.私有 1.公开
	create_operator varchar(100) not null,
	create_time datetime not null,
	modify_operator varchar(100) not null,
	modify_time datetime not null
);

-- 评论表
create table sys_comment(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	ref_table varchar(300) not null, -- 评论的表
	ref_id int(11) not null, -- 表里面的ID
	content varchar(3000) not null, -- 评论内容
	create_time datetime not null
);

-- 好友表
create table sys_friend(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	friend_user_id int(11) not null, -- 好友ID
	is_passed int(11) not null default 0, -- 是否通过好友，0.未通过 1.通过
	comments_name varchar(300) , -- 备注名
	create_time datetime not null
);

-- 人际关系字典表
create table sys_relation(
    id int(11) auto_increment primary key,
    relation varchar(50) not null,
    create_time datetime not null
);

-- 人际关系表
create table sys_user_relation(
    id int(11) auto_increment primary key,
    user_id int(11) not null,
    relation_id int(11) not null,
    relation_user_id int(11) not null
);

-- 动态表
create table sys_user_zone(
	id int(11) auto_increment primary key,
	user_id int(11) not null,
	content varchar(3000) not null, -- 内容
	location varchar(100), -- 定位
	create_time datetime not null
);

-- 动态媒体表
create table sys_user_zone_media(
	user_zone_id int(11) not null,
	media_id int(11) not null
);

-- 图表分块表
create table sys_charts_mapping(
    key_code int(11) auto_increment primary key,
    data_key varchar(300),
    key_desc varchar(1024)
);

-- 图表数据表
create table sys_charts_datasource(
    src_id int(11) auto_increment primary key,
    key_code int(11) not null,
    data_desc varchar(300) not null,
    data_value varchar(300),
    data_unit varchar(50),
    data_order int(11)
);

-- SQL处理表
create table sys_sql(
    sql_id int(11) auto_increment primary key,
    sql_group varchar(300),
    sql_type varchar(10),
    sql_content text
);

-- SQL处理参数表
create table sys_sql_params(
    id int(11) auto_increment primary key,
    sql_id int(11) not null,
    param_key varchar(300) not null,
    param_type varchar(10)
);

-- 历史记录表
create table sys_history(
    id int(11) auto_increment primary key,
    his_type varchar(10),
    user_id int(11),
    content text not null,
    create_time datetime not null
);

-- 初始化角色
insert into  sys_role(id,name,role_key,description,create_operator,create_time,modify_operator,modify_time)
values(100,'root','root','超级管理员','sys',NOW(),'sys',NOW());
insert into  sys_role(id,name,role_key,description,create_operator,create_time,modify_operator,modify_time)
values(110,'admin','admin','管理员','sys',NOW(),'sys',NOW());
insert into  sys_role(id,name,role_key,description,create_operator,create_time,modify_operator,modify_time)
values(900,'user','user','用户','sys',NOW(),'sys',NOW());

-- 初始化角色菜单
insert into sys_role_menu(role_id,menu_id,create_operator,create_time,modify_operator,modify_time)
select 100 role_id,
    id menu_id,
	'sys' create_operator,
	NOW() create_time,
	'sys' modify_operator,
	NOW() modify_time
    from sys_menu;
