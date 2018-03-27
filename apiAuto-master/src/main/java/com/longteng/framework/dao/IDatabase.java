package com.longteng.framework.dao;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public interface IDatabase {

    /**
     * 连接sid数据库
     * @param sid
     */
    public void connect(String sid);

    /**
     * 根据sql和字段field查询单条记录
     * @param sql
     * @param field
     * @return
     */
    public HashMap<String,String> selectOneRecord(String sql, String field);

    /**
     * 根据sql更新数据库
     * @param sql
     * @return
     */
    public int update(String sql);

    /**
     * 根据sql和字段field查询多条记录
     * @param sql
     * @param field
     * @return
     */
    public List<HashMap<String,String>> selectMutiRecord(String sql, String field);

    /**
     * 执行sql语句，检查field字段的值与result是否一致
     * @param sql
     * @param field
     * @param result
     * @return
     */
    public boolean valueIsExist(String sql, String field, String result);

    /**
     * 执行sql语句，获取field字段的列表
     * @param sql
     * @param field
     * @return
     */
    public List select(String sql, String field);
}


