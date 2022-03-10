package i2f.commons.component.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;

/**
 * @author ltb
 * @date 2022/3/9 19:56
 * @desc 用于在Blob与String之间自动转换
 * 插入时：
 * #{val,javaType=java.lang.String,jdbcType=BLOB,typeHandler=com.i2f...StringBlobTypeHandler}
 * 查询时：
 * <result javaType="java.lang.String" jdbcType="BLOB" typeHandler="com.i2f...StringBlobTypeHandler"/>
 */
public class StringBlobTypeHandler extends BaseTypeHandler<String> {

    // 字符集
    private static final String DEFAULT_CHARSET = "utf-8";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int index, String parameter, JdbcType jdbcType)
            throws SQLException {
        ByteArrayInputStream bis;
        byte[] bytes = null;
        try {
            bytes = parameter.getBytes(DEFAULT_CHARSET);
            // 把String转化成byte流
            bis = new ByteArrayInputStream(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
        ps.setBinaryStream(index, bis, bytes.length);
    }


    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Blob blob = (Blob) rs.getBlob(columnName);
        byte[] bts = null;
        if (null != blob) {
                bts = blob.getBytes(1, (int) blob.length());
        }
        try {
            if (bts == null) {
                return null;
            }
            // 把byte转化成string
            return new String(bts, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }


    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Blob blob = (Blob) cs.getBlob(columnIndex);
        byte[] bts = null;
        if (null != blob) {
                bts = blob.getBytes(1, (int) blob.length());
        }
        try {
            return new String(bts, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }


    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Blob blob = (Blob) rs.getBlob(columnIndex);
        byte[] bts = null;
        if (null != blob) {
            bts = blob.getBytes(1, (int) blob.length());
        }
        try {
            return new String(bts, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }
}
