/**   
* @Title: JSONTypeHandler.java 
* @Package cn.net.hylink.mybatis.handlers 
* @Description: TODO(postgresql 数据库查询时 jsob对象返回对象) 
* @author Wang Yingnan    
* @date 2018年2月7日 下午7:19:07 
* @version V1.0   
*/ 
package cn.net.hlk.mybatis.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import com.alibaba.fastjson.JSON;


 
@MappedJdbcTypes(JdbcType.OTHER) // 可有可无
@MappedTypes(Object.class)
public class JSONTypeHandler extends BaseTypeHandler<Object> {
    private static final PGobject jsonObject = new PGobject();
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        jsonObject.setType("jsonb");
        jsonObject.setValue(parameter.toString());
        ps.setObject(i, jsonObject);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    	String rsStr = rs.getString(columnIndex);
    	try {
    		if(rsStr!=null&&!"".equals(rsStr)){
        		Object obj = JSON.parse(rsStr);
        		 return obj;
        	}
		} catch (Exception e) {
			  return rsStr;
		}
    	
        return rsStr;
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    	String rsStr =cs.getString(columnIndex);
    	try {
    		if(rsStr!=null&&!"".equals(rsStr)){
        		Object obj = JSON.parse(rsStr);
        		 return obj;
        	}
		} catch (Exception e) {
			 return rsStr;
		}
    	
    	 return rsStr;
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
    	
    		String rsStr = rs.getString(columnName);
		try {
        	if(rsStr!=null&&!"".equals(rsStr)){
        		Object obj = JSON.parse(rsStr);
        		 return obj;
        	}
		} catch (Exception e) {
			   return rsStr;
		}  
    	
        return rsStr;
    }

}