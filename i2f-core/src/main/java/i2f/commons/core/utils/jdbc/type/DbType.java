package i2f.commons.core.utils.jdbc.type;

import i2f.commons.core.utils.safe.CheckUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * 来源：Mybatis-Plus
 */
public enum DbType {

    /**
     * MYSQL
     */
    MYSQL("mysql", "MySql数据库"),
    /**
     * MARIADB
     */
    MARIADB("mariadb", "MariaDB数据库"),
    /**
     * ORACLE
     */
    ORACLE("oracle", "Oracle11g及以下数据库(高版本推荐使用ORACLE_NEW)"),
    /**
     * oracle12c new pagination
     */
    ORACLE_12C("oracle12c", "Oracle12c+数据库"),
    /**
     * DB2
     */
    DB2("db2", "DB2数据库"),
    /**
     * H2
     */
    H2("h2", "H2数据库"),
    /**
     * HSQL
     */
    HSQL("hsql", "HSQL数据库"),
    /**
     * SQLITE
     */
    SQLITE("sqlite", "SQLite数据库"),
    /**
     * POSTGRE
     */
    POSTGRE_SQL("postgresql", "Postgre数据库"),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005", "SQLServer2005数据库"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver", "SQLServer数据库"),
    /**
     * DM
     */
    DM("dm", "达梦数据库"),
    /**
     * xugu
     */
    XU_GU("xugu", "虚谷数据库"),
    /**
     * Kingbase
     */
    KINGBASE_ES("kingbasees", "人大金仓数据库"),
    /**
     * Phoenix
     */
    PHOENIX("phoenix", "Phoenix HBase数据库"),
    /**
     * Gauss
     */
    GAUSS("zenith", "Gauss 数据库"),
    /**
     * ClickHouse
     */
    CLICK_HOUSE("clickhouse", "clickhouse 数据库"),
    /**
     * GBase
     */
    GBASE("gbase", "南大通用数据库"),
    /**
     * Oscar
     */
    OSCAR("oscar", "神通数据库"),
    /**
     * Sybase
     */
    SYBASE("sybase", "Sybase ASE 数据库"),
    /**
     * OceanBase
     */
    OCEAN_BASE("oceanbase", "OceanBase 数据库"),
    /**
     * Firebird
     */
    FIREBIRD("Firebird", "Firebird 数据库"),

    /**
     * HighGo
     */
    HighGo("highgo", "瀚高数据库"),
    /**
     * UNKONWN DB
     */
    OTHER("other", "其他数据库"),
    /**
     * URL参数无法读取或者错误
     */
    ERROR("error","URL参数读取错误");
    /**
     * 数据库名称
     */
    private final String db;
    /**
     * 描述
     */
    private final String desc;

    private DbType(String db, String desc){
        this.db=db;
        this.desc=desc;
    }

    /**
     * 获取数据库类型
     *
     * @param dbType 数据库类型字符串
     */
    public static DbType typeOfName(String dbType) {
        for (DbType type : DbType.values()) {
            if (type.db.equalsIgnoreCase(dbType)) {
                return type;
            }
        }
        return OTHER;
    }

    public static DbType typeOfConnection(Connection conn) throws SQLException {
        return typeOfJdbcUrl(conn.getMetaData().getURL());
    }

    /**
     * 根据连接地址判断数据库类型
     *
     * @param jdbcUrl 连接地址
     * @return ignore
     */
    public static DbType typeOfJdbcUrl(String jdbcUrl) {
        if(CheckUtil.isEmptyStr(jdbcUrl,true)){
            //"Error: The jdbcUrl is Null, Cannot read database type"
            return DbType.ERROR;
        }
        String url = jdbcUrl.toLowerCase();
        if (url.contains(":mysql:") || url.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (url.contains(":mariadb:")) {
            return DbType.MARIADB;
        } else if (url.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (url.contains(":sqlserver:") || url.contains(":microsoft:")) {
            return DbType.SQL_SERVER2005;
        } else if (url.contains(":sqlserver2012:")) {
            return DbType.SQL_SERVER;
        } else if (url.contains(":postgresql:")) {
            return DbType.POSTGRE_SQL;
        } else if (url.contains(":hsqldb:")) {
            return DbType.HSQL;
        } else if (url.contains(":db2:")) {
            return DbType.DB2;
        } else if (url.contains(":sqlite:")) {
            return DbType.SQLITE;
        } else if (url.contains(":h2:")) {
            return DbType.H2;
        } else if (regexFind(":dm\\d*:", url)) {
            return DbType.DM;
        } else if (url.contains(":xugu:")) {
            return DbType.XU_GU;
        } else if (regexFind(":kingbase\\d*:", url)) {
            return DbType.KINGBASE_ES;
        } else if (url.contains(":phoenix:")) {
            return DbType.PHOENIX;
        } else if (jdbcUrl.contains(":zenith:")) {
            return DbType.GAUSS;
        } else if (jdbcUrl.contains(":gbase:")) {
            return DbType.GBASE;
        } else if (jdbcUrl.contains(":clickhouse:")) {
            return DbType.CLICK_HOUSE;
        } else if (jdbcUrl.contains(":oscar:")) {
            return DbType.OSCAR;
        } else if (jdbcUrl.contains(":sybase:")) {
            return DbType.SYBASE;
        } else if (jdbcUrl.contains(":oceanbase:")) {
            return DbType.OCEAN_BASE;
        }else if (url.contains(":highgo:")) {
            return DbType.HighGo;
        }else {
            //logger.warn("The jdbcUrl is " + jdbcUrl + ", Mybatis Plus Cannot Read Database type or The Database's Not Supported!");
            return DbType.OTHER;
        }
    }

    /**
     * 正则匹配
     *
     * @param regex 正则
     * @param input 字符串
     * @return 验证成功返回 true，验证失败返回 false
     */
    public static boolean regexFind(String regex, CharSequence input) {
        if (null == input) {
            return false;
        }
        return Pattern.compile(regex).matcher(input).find();
    }
}
