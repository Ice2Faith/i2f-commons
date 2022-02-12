/**
  即时做
  ---------------------------
  可以发起一个项目，仅注册后
  任意人可参与这个项目，不需要注册
*/

/**
  发起者表，需要提供用户名和一个能够提供验证的电话或者邮箱
 */
create table jot_owner
(
    owner_id int(11) auto_increment primary key, -- 拥有者ID
    user_name varchar(300) unique not null, -- 拥有者账号
    user_password varchar(300) not null, -- 拥有者密码
    telephone varchar(20), -- 拥有者电话
    email varchar(300) -- 拥有者邮箱
);

-- ---------------------------------------------
-- 在线投票 开始
-- ---------------------------------------------
/**
  在线投票话题表
 */
create table jot_vote_topic
(
    topic_id int(11) auto_increment primary key , -- 投票话题ID
    owner_id int(11) not null , -- 话题发起者
    topic_name varchar(1024) not null, -- 话题名称
    topic_desc varchar(4096), -- 话题描述
    start_time datetime not null, -- 话题开始时间
    end_time datetime -- 话题结束时间
);

/**
  投票话题媒体表
 */
create table jot_vote_topic_media
(
    media_id int(11) auto_increment primary key , -- 媒体ID
    topic_id int(11) not null, -- 话题ID
    media_url varchar(2048) not null, -- 媒体连接
    media_type varchar(10), -- 媒体类型
    media_order varchar(20) -- 媒体排序
);

/**
  在线投票话题选项表
 */
 create table jot_vote_topic_choice_item
(
    item_id int(11) auto_increment primary key , -- 选项ID
    topic_id int(11) not null, -- 话题ID
    item_text varchar(2048) not null, -- 话题文本
    item_media varchar(2048), -- 话题关联媒体
    media_type varchar(10) -- 媒体类型
);

/**
  投票话题选项媒体表
 */
create table jot_vote_topic_choice_item_media
(
    media_id int(11) auto_increment primary key , -- 媒体ID
    item_id int(11) not null, -- 选项ID
    media_url varchar(2048) not null, -- 媒体连接
    media_type varchar(10), -- 媒体类型
    media_order varchar(20) -- 媒体排序
);

/**
  投票结果表
 */
create table jot_vote_result
(
    result_id int(11) auto_increment primary key , -- 结果ID
    topic_id int(11) not null, -- 话题ID
    item_id int(11) not null, -- 选项ID
    join_user_name varchar(300), -- 投票人姓名
    join_user_connection varchar(2048), -- 投票人联系方式
    join_user_connection_type varchar(10), -- 投票人联系方式类型
    join_user_message varchar(300) -- 投票人留言
);

-- ---------------------------------------------
-- 在线投票 结束
-- ---------------------------------------------


-- ---------------------------------------------
-- 在线打分 开始
-- ---------------------------------------------
/**
  在线打分话题表
 */
create table jot_score_topic
(
    topic_id int(11) auto_increment primary key , -- 投票话题ID
    owner_id int(11) not null , -- 话题发起者
    topic_name varchar(1024) not null, -- 话题名称
    topic_desc varchar(4096), -- 话题描述
    low_limit_score double not null, -- 最低分
    high_limit_score double not null, -- 最高分
    start_time datetime not null, -- 话题开始时间
    end_time datetime -- 话题结束时间
);

/**
  打分话题媒体表
 */
create table jot_score_topic_media
(
    media_id int(11) auto_increment primary key , -- 媒体ID
    topic_id int(11) not null, -- 话题ID
    media_url varchar(2048) not null, -- 媒体连接
    media_type varchar(10), -- 媒体类型
    media_order varchar(20) -- 媒体排序
);


/**
  打分结果表
 */
create table jot_score_result
(
    result_id int(11) auto_increment primary key , -- 结果ID
    topic_id int(11) not null, -- 话题ID
    score double not null, -- 打分分数
    join_user_name varchar(300), -- 投票人姓名
    join_user_connection varchar(2048), -- 投票人联系方式
    join_user_connection_type varchar(10), -- 投票人联系方式类型
    join_user_message varchar(300) -- 投票人留言
);

-- ---------------------------------------------
-- 在线打分 结束
-- ---------------------------------------------