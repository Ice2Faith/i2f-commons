# i2f-commons
commons util package project for java devlop


# What's the project?
- java develop commons util package
    - include core package
        - which only rely jdk 1.8 or higher
        - with out any more dependency
    - and component package
        - which rely the core
        - and other third package when you need it
- core includes
    - list to tree
    - encode and decode
    - date time convert
    - database generate
    - java web source file generate
    - file util
    - stream util
    - net util
    - reflect util
    - package scanner
    - jpa base on jdbc
    - string util
    - thread util
    - etc.
- component includes
    - aspectj util
    - cglib util
    - email util
    - excel util
    - httpclient util
    - json util
    - mybatis util
    - netty util
    - qrcode util
    - quartz util
    - redis util
    - spring family util
    - kaptcha verifycode util
    - http file process util
    - etc.
# How to use it?
- just include the jar i2f-core.jar

# preview usage
## list to tree
```java
        List<TreeItem> list=new ArrayList<>();
        list.add(new TreeItem(-1,-999));
        list.add(new TreeItem(1,-1));
        list.add(new TreeItem(2,-1));
        list.add(new TreeItem(3,-1));
        list.add(new TreeItem(4,-1));
        list.add(new TreeItem(5,-1));
        list.add(new TreeItem(6,-1));
        list.add(new TreeItem(7,-1));
        list.add(new TreeItem(8,-1));
        list.add(new TreeItem(9,-1));
        list.add(new TreeItem(10,1));
        list.add(new TreeItem(11,1));
        list.add(new TreeItem(12,1));
        list.add(new TreeItem(13,1));
        list.add(new TreeItem(14,1));
        list.add(new TreeItem(20,2));
        list.add(new TreeItem(21,2));
        list.add(new TreeItem(22,2));
        list.add(new TreeItem(101,10));
        list.add(new TreeItem(102,10));
        list.add(new TreeItem(103,10));
        list.add(new TreeItem(104,10));
        list.add(new TreeItem(105,10));
        list.add(new TreeItem(1021,102));
        list.add(new TreeItem(1022,102));
        list.add(new TreeItem(1023,102));
        list.add(new TreeItem(1024,102));
        list.add(new TreeItem(1025,102));
        list.add(new TreeItem(1026,102));


        List<TreeItem> tree= ConvertUtil.list2Tree(list);
```
- the result
```bash
TreeItem(id=-1, parentId=-999, 
    children=[
        TreeItem(id=1,parentId=-1, 
            children=[
                TreeItem(id=10, parentId=1,
                    children=[
                        TreeItem(id=101, parentId=10, children=null), 
                        TreeItem(id=102, parentId=10, children=[
                            TreeItem(id=1021, parentId=102, children=null), 
                            TreeItem(id=1022, parentId=102, children=null), 
                            TreeItem(id=1023, parentId=102, children=null), 
                            TreeItem(id=1024, parentId=102, children=null), 
                            TreeItem(id=1025, parentId=102, children=null), 
                            TreeItem(id=1026, parentId=102, children=null)
                        ]),
                TreeItem(id=103, parentId=10, children=null), 
                TreeItem(id=104, parentId=10, children=null), 
                TreeItem(id=105, parentId=10, children=null)
            ]), 
        TreeItem(id=11, parentId=1, children=null), 
        TreeItem(id=12, parentId=1, children=null), 
        TreeItem(id=13, parentId=1, children=null), 
        TreeItem(id=14, parentId=1, children=null)
    ]), 
    TreeItem(id=2, parentId=-1, children=[
        TreeItem(id=20, parentId=2, children=null), 
        TreeItem(id=21, parentId=2, children=null), 
        TreeItem(id=22, parentId=2, children=null)
    ]), 
    TreeItem(id=3, parentId=-1, children=null), 
    TreeItem(id=4, parentId=-1, children=null), 
    TreeItem(id=5, parentId=-1, children=null), 
    TreeItem(id=6, parentId=-1, children=null), 
    TreeItem(id=7, parentId=-1, children=null), 
    TreeItem(id=8, parentId=-1, children=null), 
    TreeItem(id=9, parentId=-1, children=null)
])
```

## jdbc util
```java
    public static void main(String[] args) throws SQLException {
        UserBean bean=new UserBean();
        bean.setUserId(1);
        bean.setUserName("account");
        bean.setUserIntro("userName");
        bean.setUserAge(22);
        bean.setUserSex(1);

        IJdbcMeta meta=TestWrapperJdbc.getJdbcMeta();
        JdbcDao dao=new JdbcDao(meta);
        TransactionManager transactionManager=dao.getTransactionManager();

        JdbcDaoUtil daoUtil=new JdbcDaoUtil(dao);

        //testJdbcJpa(bean, dao, daoUtil);

        daoUtil.insertAutoPk(bean);

        transactionManager.openTrans(true)
                .isolation(TransactionManager.Isolation.READ_COMMITTED)
                .begin();

        daoUtil.insertAutoPk(bean);

        transactionManager.commit();

        dao.emptyTable(daoUtil.getTableName(UserBean.class));

        transactionManager.rollback();

        transactionManager.close();

    }

    private static void testJdbcJpa(UserBean bean, JdbcDao dao, JdbcDaoUtil daoUtil) throws SQLException {
        dao.dropTable(daoUtil.getTableName(UserBean.class));

        String sql="create table user(" +
                "user_id int(11) auto_increment primary key," +
                "user_name varchar(300)," +
                "user_intro varchar(300)," +
                "user_age int(11)," +
                "user_sex char(1)" +
                ")";
        dao.executeNative(sql);

        daoUtil.insert(bean);

        daoUtil.insertAutoPk(bean);

        bean = daoUtil.queryByPk(UserBean.class,"user_id",1,"*");

        bean = daoUtil.queryByPkAnn(UserBean.class,2,"*");

        bean.setUserIntro("your father");
        daoUtil.updateByPk(bean);

        QueryWrapper wrapper=QueryWrapper.build()
                .page(0,10)
                .cols("*")
                .done();
        DBPageData<UserBean> page= daoUtil.page(UserBean.class,wrapper);
        System.out.println("page:"+page);

        daoUtil.deleteByPk(UserBean.class, bean.getUserId());

        dao.emptyTable(daoUtil.getTableName(UserBean.class));

        dao.queryAll(daoUtil.getTableName(UserBean.class));


        dao.queryAll(daoUtil.getTableName(UserBean.class));

        dao.dropTable(daoUtil.getTableName(UserBean.class));
    }
```
- the result
```bash
JdbcLogOut -> time:2021-09-29 17:11:51 828
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: drop table if exists user
	sql : drop table if exists user
	params : []
JdbcLogOut -> time:2021-09-29 17:11:51 828 : update result : 0
JdbcLogOut -> time:2021-09-29 17:11:51 958
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: create table user(user_id int(11) auto_increment primary key,user_name varchar(300),user_intro varchar(300),user_age int(11),user_sex char(1))
	sql : create table user(user_id int(11) auto_increment primary key,user_name varchar(300),user_intro varchar(300),user_age int(11),user_sex char(1))
	params : []
JdbcLogOut -> time:2021-09-29 17:11:51 958 : update result : 0
JdbcLogOut -> time:2021-09-29 17:11:52 125
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: insert into user (  user_sex,user_id,user_name,user_age,user_intro  )  values  (  1 , 1 , 'account' , 22 , 'userName'  ) 
	sql : insert into user (  user_sex,user_id,user_name,user_age,user_intro  )  values  (  ? , ? , ? , ? , ?  ) 
	params : [1,1,account,22,userName]
JdbcLogOut -> time:2021-09-29 17:11:52 125 : update result : 1
JdbcLogOut -> time:2021-09-29 17:11:52 173
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: insert into user (  user_sex,user_name,user_age,user_intro  )  values  (  1 , 'account' , 22 , 'userName'  ) 
	sql : insert into user (  user_sex,user_name,user_age,user_intro  )  values  (  ? , ? , ? , ?  ) 
	params : [1,account,22,userName]
JdbcLogOut -> time:2021-09-29 17:11:52 173 : update result : 1
JdbcLogOut -> time:2021-09-29 17:11:52 213
	stat: com.mysql.cj.jdbc.ClientPreparedStatement:  select  * from user where  user_id =  1 
	sql :  select  * from user where  user_id =  ? 
	params : [1]
JdbcLogOut -> time:2021-09-29 17:11:52 213 : query result : 1
JdbcLogOut -> time:2021-09-29 17:11:52 245
	stat: com.mysql.cj.jdbc.ClientPreparedStatement:  select  * from user where  user_id =  2 
	sql :  select  * from user where  user_id =  ? 
	params : [2]
JdbcLogOut -> time:2021-09-29 17:11:52 245 : query result : 1
JdbcLogOut -> time:2021-09-29 17:11:52 262
	stat: com.mysql.cj.jdbc.ClientPreparedStatement:  update user set  user_id =  2 , user_name =  'account' , user_age =  22 , user_intro =  'your father'  where  user_id =  2 
	sql :  update user set  user_id =  ? , user_name =  ? , user_age =  ? , user_intro =  ?  where  user_id =  ? 
	params : [2,account,22,your father,2]
JdbcLogOut -> time:2021-09-29 17:11:52 262 : update result : 1
JdbcLogOut -> time:2021-09-29 17:11:52 366
	stat: com.mysql.cj.jdbc.ClientPreparedStatement:  select count(*) cnt  from (  select  * from user ) tmp 
	sql :  select count(*) cnt  from (  select  * from user ) tmp 
	params : []
JdbcLogOut -> time:2021-09-29 17:11:52 366 : query result : 1
JdbcLogOut -> time:2021-09-29 17:11:52 372
	stat: com.mysql.cj.jdbc.ClientPreparedStatement:  select  * from user limit 0,10
	sql :  select  * from user limit ?,?
	params : [0,10]
JdbcLogOut -> time:2021-09-29 17:11:52 372 : query result : 2
page:DBPageData(count=2, index=0, limit=10, data=[UserBean(userId=1, userName=account, userAge=22, userIntro=userName, userSex=null), UserBean(userId=2, userName=account, userAge=22, userIntro=your father, userSex=null)])
JdbcLogOut -> time:2021-09-29 17:11:52 388
	stat: com.mysql.cj.jdbc.ClientPreparedStatement:  delete from user where  user_id =  2 
	sql :  delete from user where  user_id =  ? 
	params : [2]
JdbcLogOut -> time:2021-09-29 17:11:52 388 : update result : 1
JdbcLogOut -> time:2021-09-29 17:11:52 418
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: delete from user
	sql : delete from user
	params : []
JdbcLogOut -> time:2021-09-29 17:11:52 418 : update result : 1
JdbcLogOut -> time:2021-09-29 17:11:52 443
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: select * from user
	sql : select * from user
	params : []
JdbcLogOut -> time:2021-09-29 17:11:52 443 : query result : 0
JdbcLogOut -> time:2021-09-29 17:11:52 469
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: select * from user
	sql : select * from user
	params : []
JdbcLogOut -> time:2021-09-29 17:11:52 469 : query result : 0
JdbcLogOut -> time:2021-09-29 17:11:52 480
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: drop table if exists user
	sql : drop table if exists user
	params : []
JdbcLogOut -> time:2021-09-29 17:11:52 480 : update result : 0
JdbcLogOut -> time:2021-09-29 17:11:52 543
	stat: com.mysql.cj.jdbc.ClientPreparedStatement: insert into user (  user_sex,user_name,user_age,user_intro  )  values  (  1 , 'account' , 22 , 'userName'  ) 
	sql : insert into user (  user_sex,user_name,user_age,user_intro  )  values  (  ? , ? , ? , ?  ) 
	params : [1,account,22,userName]
```

## auto recognize db type do page
```java
        QueryWrapper wrapper=QueryWrapper.build()
                        .table(tableName)
                        .cols("id","title","viceTitle","content","createDate","viewCount")
                        .and()
                        .gte("id",105)
                        .lt("id",150)
                        .like("title","aa")
                        .like("viceTitle","tes","est")
                        .in("viewCount",1,2,3,4,5,6,7,8)
                        .asc("id")
                        .desc("viewCount")
                        .page(0,10)
                        .done();
        
                PageContextData data=dao.queryPage(wrapper);
```

## read text file
```java
    File file=new File("D:\\IDEA_ROOT\\i2f-commons\\i2f-component\\pom.xml");
        List<String> lines= FileUtil.readTxtFile(file,0,-1,true,true,(val)->{
            return val.startsWith("<!");
        },(val)->{
            return val.replace("<!","#");
        });

        for(String item : lines){
            System.out.println(item);
        }
```
- the result
```html
#-- lombok   版本管理  -->
#-- i2f.web -->
#-- i2f.proxy.cglib -->
#-- i2f.file.excel.jexcel -->
#-- i2f.component.json.gson  -->
#-- i2f.component.json.fastjson  -->
#-- i2f.component.mybatis  -->
#-- MySQL的jdbc驱动包 -->
#-- pagehelper 分页插件 -->
#-- i2f.component.aspectj -->
#-- i2f.component.spring -->
```

## generate java web files from db
```java
        IJdbcMeta meta=TestWrapperJdbc.getJdbcMeta();
        JdbcDao dao=new JdbcDao(meta);
        TableMeta tm= DbResolver.getTableMeta(dao.getTransactionManager().getConnection(),"NoteT");

        GenerateContext ctx= GenerateContext.build()
                .basePackage("com.i2f")
                .author("Ugex.Savelar")
                .table(tm).types(DbGenType.ALL)
                .save2File(true)
                .savePath("D:\\IDEA_ROOT\\i2f-commons\\i2f-component\\src\\test\\java")
                .done();

        ctx= DbGenerator.genCodeFiles(ctx);
```

## build create table sql by bean class
```java
        String sql= DBClassUtil.genCreateTableByBean(UserBean.class);
        System.out.println(sql);

        sql=DBClassUtil.genDropTableByBean(UserBean.class,true);
        System.out.println(sql);

        System.out.println("-----------------------");
        sql=DBClassUtil.genTablesByBean(true,UserBean.class, BinTreeNode.class, Pair.class, Triple.class);
        System.out.println(sql);
```
- the result
```bash
CREATE TABLE singlenotedb.user
(
	user_id	INT  PRIMARY KEY AUTO_INCREMENT COMMENT 'ID'
	,user_name	VARCHAR(300)  UNIQUE NOT NULL COMMENT '用户名'
	,user_age	INT  NOT NULL DEFAULT 0 COMMENT '年龄'
	,user_sex	CHAR(1)  CHECK(user_sex in (1,2)) COMMENT '性别：1 男 2 女'
	,user_intro	TEXT  COMMENT '简介'
) AUTO_INCREMENT=100 COMMENT '用户表';

DROP TABLE IF EXISTS singlenotedb.user;
```

## and more powerful usually use module or function wait you to find.
