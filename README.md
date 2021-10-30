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

# update log
- 2021-10-30 13h
    - add generator grammer define
        - define expression: #{[define,valueId],value=""}
        - it means that:
        - in current context define a value named valueId to give template use.
        - valueId: your define value ID ,it's collect into _def node,${_def.valueId} to get it
        - value: your value , it can be a template 
        - such:
        - define a value: #{[define,lowerNameBean],value="${data.name@toLowerCase}Bean"}
        - it equals:
        - _def.lowerNameBean=data.name.toLowerCase()
    - add generator grammer cmd
        - cmd expression: #{[cmd,env.data],command="",show="",charset=""}
        - it means that:
        - execute command "command",if show is true,get the execute result as return value according charset.
        - command: your command line
        - show: whether need echo return
        - charset: echo charset
        - such:
        - set cmd: #{[define,cmd],value="cmd /c "}
        - do ping: #{[cmd,env.data],command="${_root._def.cmd} ping 8.8.8.8",show="true",charset="GBK"}
        - it equlas:
        - Runtime.getRuntime().execute("cmd /c ping 8.8.8.8");
        - and command echo as result fill into template .
- 2021-10-28 16h 
    - add generator grammer trim
        - trim expression: #{[trim,ObjectRoutingExpression],prefix="",suffix="",sensible="",trimBefore="",trimAfter="",template="",ref=""}
        - it means that:
        - trim prefix and suffix from the result of ObjectRoutingExpression or form template/ref
        - prefix: point trim prefix(s) ,multi value split by '|'
        - suffix: point trim suffix(s), same as prefix
        - sensible: whether case sensible for prefix and suffix
        - trimBefore: whether do trim result before process
        - trimAfter: whether do trim result after process
        - template and ref arg,please refrence the introduce form for or if expression
        - such:
        - trim 'and,or' prefix and ',' (dot) suffix :#{[trim,env.data],prefix="and|or",suffix=",",sensible="false",trimBefore="true"}
- 2021-10-28 15h
    - add generator grammer fmt
        - fmt expression: #{[fmt,ObjectRoutingExpression],format="",values=""}
        - it means that:
        - use values formed objects to according to format string to format string
        - such:
        - format a simple: #{[fmt,env.args[2]],format="%02d,%.2f",values="_item.ival,_item.dval"}
        - it equals:
        - String.format("%02d,%.2f",env.args[2].ival,env.args[2].dval)
    - add generator grammer datefmt
        - datefmt expression: #{[datefmt,ObjectRoutingExpression],format=""}
        - it means that:
        - use format arg string to format a date object
        - such:
        - format date: #{[datefmt,args.date],format="yyyy-MM-dd HH:mm:ss"}
        - it equals:
        - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(args.date)  
- 2021-10-28 09h
    - add generator grammer include
        - include expression: #{[include,ObjectRoutingExpression],ref=""}
        - it means that:
        - include and render a template use the ref point template Id (from tpl expression defined or binding param)
        - such:
        - declare a template: #{[tpl,tplArgValue],template="value:${_item}"}
        - include the template: #{[include,arg.value],ref="_tpl.tplArgValue"}
    - add generator grammer val
        - val expression : #{[val,ObjectRoutingExpression],mapper=""}
        - it meas that:
        - use custom mapper which implements IMap<Object,String> interface class to mapping the value.
        - when mapper is not declared,the mapper will use from generate give.
        - so,it can instead of ${} expression used on mybatis mapper xml 
        - avoid conflict with other grammer.
        - such:
        - use custom mapper: #{[val,arg.value],mapper="com.i2f.mapper.StringMapper"}
        - use generate mapper: #{[val,arg.value]}
- 2021-10-26 17h
    - add generator grammer tpl
        - tpl expression: #{[tpl,tplId],template="",load="",key=""}
        - it mean's that
        - load a template name as tplId add into binding param obj's _tpl root
        - template: this template str
        - load: define the template loader which implements IMap<String,String> interface
        - key: define the template loader load from where ,the same as loader.map method's in param
        - when key is declared,template will be rewrite
        - when load not declared,use default file loader which support classpath path
        - what is it support?
            - to implement more complex template to render
            - to support multi-template file use
            - to simplify template embed
            - to subtract quote string and long template string direct write into template argument space
        - how to create myself template file?
            - you can see i2f-core/resources/tpl
            - and try to build yourself template   
    - add database table generate java files for spring+mybatis
        - it's base on generator and jdbc meta info
        - you can edit yourself template file
        - and give it the binding param
        - your code will auto bulding
        - how to generate code files
```java
        //get the jdbc connection
        JdbcDao dao=getDao();
        Connection conn=dao.getTransactionManager().getConnection();
        //get table info from connection
        TableMeta meta= DbResolver.getTableMeta(conn,"student_course");

        //setting binding param
        GenerateContext ctx=new GenerateContext();
        //which table info
        ctx.meta=meta;
        //which base package
        ctx.basePackage="com.i2f";
        //who is the author
        ctx.author="ltb";

        //result save as file
        ctx.save2File=true;
        //build all type
        ctx.genType= DbGenType.ALL.getCode();
        //file save root dir
        ctx.savePath="D:\\i2f-component\\src\\test\\java";

        //do the task
        DbTplGenerator.genCodeFiles(ctx);
```
- 2021-10-23 16h
    - add simple generator 
        - which base on Regex Patten and Reflect
        - none third dependencies
        - location: i2f-core > i2f.commons.core.utils.generator.RegexGenerator
        - simple usage: i2f-component > test.TestPatten
        - you just have an template string and an object of for binding the template
        - then the template will render for you,and return
        - key code has some chinese comments might help you use it
        - but,that's not important
        - you just know the thrid expression 
            - get value : ${ObjectRoutingExpression}
                - ObjectRoutingExpression: 
                - such ${data.args[2].str@Integer.parseInt}
                - mean's that:
                     - guess binding object name is obj
                     - this expression equals then:
                     - return Integer.parseInt(obj.data.args[2].str)
                     - say, it's simple to learn
                     - it's like js lang grammer
                - and other form:
                - ${_root}
                - ${_root.val}
                - ${vals[2]}
                - ${val@Math.abs}
                - ${val@java.lang.BigInteger.instanceof}
                - etc.
            - for expression :${[for,ObjectRoutingExpression],separator="",prefix="",suffix="",blank="true",jump="true",template="${_item}",ref=""}
                - in here,the ObjectRoutingExpression formed object need is iterable
                - such :
                        - implements Iterable interface as Collection<?>
                        - a Map
                        - an Array
                - and template mean's you can define the render template
                - at the time ,ref mean's the template from binding object's ObjectRoutingExpression value
                - when ref and template both exist,ref valid
            - if expression: ${[if,ObjectRoutingExpression],test="_item!=null && item>0",template="${_item}'s abs is ${_item@Math.abs}"}
                - test : you can do some boolean calc ,when the calc result is true will render it
                - template and ref same as for expression
            - cation:
                - if and for expression have some builtin object
                    - _item: it's ObjectRoutingExpression form value
                    - _root: it's binding object
                    - _ctx: when is for expression,have some value in:
                        - first: boolean value form whether is first render elem
                        - last:boolean value form whether is last render elem
                        - index: int value form current render index,not iterable index
            - when you need for and if embed
            - you cloud by template and builtin object implements
    - add easyexcel asyn export util
        - which base on alibaba easyexcel
        - support asyn export
        - self defined data provider design
        - default thread pool support
        - and , with an controller interface help you fast add into your project
        - auto page or none page interface support
        - direct list data adapter support
        - location: i2f-component > i2f.commons.component.excel.util.ExportUtil
        - usage: none
        - just invoke ExportUtil.writeExcelFile(...) method with a data provider is enough
        - and return a WebExcelRespData 
        - it's includes file relative Path, check export done url, download export excel url
        - on web side, just post export request and setInterval to check whether export done
        - when export done,you can download it.
        - every export excel file single sheet max rows is 65535
        - if more,which will be part into another sheet

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
