package com.microwise.msp.hardware.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.Map;

/**
 * 这类干了提供 ibatis 接口，内部使用 mybatis 实现的活。
 *
 * 将来某个时候，我们将完全消灭 ibatis 的使用
 *
 * @author gaohui
 * @date 13-7-30 11:04
 */
public class SqlMapClient2SqlSessionAdapter extends SqlSessionDaoSupport {

    @Autowired
    private void initSqlSessionFactory(SqlSessionFactory ssf){
        super.setSqlSessionFactory(ssf);
    }

    /**
     * ibatis 的 queryForMap 方法的遗留 @gaohui 2013-09-13
     *
     * @param statementName
     * @param parameterObject
     * @param keyProperty
     * @param valueProperty
     * @return
     * @throws DataAccessException
     */
    public Map queryForMap(String statementName, Object parameterObject, String keyProperty, String valueProperty) throws DataAccessException {
        Map<String, Map> map =  getSqlSession().selectMap(statementName, parameterObject, keyProperty);
        Map result = new HashMap();
        for(Map.Entry<String, Map> entry : map.entrySet()){
            result.put(entry.getKey(), entry.getValue().get(valueProperty));
        }
        return result;
    }

}
