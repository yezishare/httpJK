package com.longteng.framework.dao;

import com.longteng.framework.config.Config;
import com.longteng.framework.config.Constants;
import com.longteng.framework.util.Log;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class MysqlDB implements IDatabase {
    public static Logger logger = Log.getInstance();
    private static String url = "";//characterEncoding=GBK
    private static String username = "";
    private static String password = "";
    public static Connection con;
    public static Statement stmt;
    public static ResultSet rs;
    public static Config config = new Config(Constants.config);

    /**
     * 获取用户名
     * @return
     */
    public static String getUsername() {
        return username;
    }
    /**
     * 设置用户名
     * @return
     */
    public static void setUsername(String username) {
        MysqlDB.username = username;
    }
    /**
     * 获取密码
     * @return
     */
    public static String getPassword() {
        return password;
    }
    /**
     * 设置密码
     * @return
     */
    public void setPassword(String password) {
        MysqlDB.password = password;
    }

    /**
     * 执行sql语句，返回结果集
     * @param sql
     * @return
     */
    public ResultSet select(String sql) {
        try {
            logger.info("开始查询数据库:" + sql);

            return stmt.executeQuery(sql);
        } catch (Exception e) {
            logger.error("查询失败", e);
            return null;
        }
    }

    /**
     * 根据sql更新数据库
     * @param sql
     * @return
     */
    @Override
    public int update(String sql) {

        try {
            logger.info("开始更新数据库:" + sql);
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error("更新失败", e);
            return -1;
        }

    }

    /**
     * 连接sid数据库
     * @param sid
     */
    @Override
    public void connect(String sid) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            initBySid(sid);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        try {
//	        	 String url= "jdbc:mysql://"+config.get("AFSUIP")+":3306/"+config.get("AFSDBTYP"); //mysql为数据库的SID
            String url = "jdbc:mysql://" + config.get(sid+"IP") + ":"+config.get(sid+"PORT")+"/" +config.get(sid+"Server");
            con = DriverManager.getConnection(url, this.getUsername(), this.getPassword());
            stmt = con.createStatement();
        } catch (SQLException e) {
            logger.error("连接数据库失败", e);
        }
    }
    public void initBySid(String sid) {

        if (sid.equalsIgnoreCase(sid)) {
            this.setUsername(config.get(sid+"UID"));
            this.setPassword(config.get(sid+"PWD"));
        }


    }

    /**
     * 根据sql和字段field查询单条记录，以HashMap<String, String>形式返回
     * @param sql
     * @param field
     * @return
     */
    @Override
    public HashMap<String, String> selectOneRecord(String sql, String field) {
        logger.info("开始查询数据库:" + sql);
        HashMap<String, String> tempMap = new HashMap<String, String>();
        try {
            ResultSet set = stmt.executeQuery(sql);
            if (set.next()) {
                String[] arr = field.split(Constants.Comma);
                logger.info("开始查询数据库:" + sql);
                for (int i = 0; i < arr.length; i++) {
                    tempMap.put(arr[i], set.getString(arr[i]));
                }
            }
        } catch (Exception e) {
            logger.error("查询失败", e);
        }
        return tempMap;
    }

    /**
     * 根据sql和字段field查询多条记录，以List<HashMap<String, String>>形式返回
     * @param sql
     * @param field
     * @return
     */
    @Override
    public List<HashMap<String, String>> selectMutiRecord(String sql,
                                                          String field) {
        logger.info("开始查询数据库:" + sql);
        List<HashMap<String, String>> list = new ArrayList();
        HashMap<String, String> tempMap = new HashMap<String, String>();
        try {
            ResultSet set = stmt.executeQuery(sql);

            String[] arr = field.split(Constants.Comma);
            logger.info("开始查询数据库:" + sql);
            while (set.next()) {
                for (int i = 0; i < arr.length; i++) {
                    tempMap.put(arr[i], set.getString(arr[i]));
                }
                list.add(tempMap);
            }
        } catch (Exception e) {
            logger.error("查询失败", e);
        }
        return list;
    }

    /**
     * 执行sql语句，检查field字段的值与result是否一致
     * @param sql
     * @param field
     * @param result
     * @return
     */
    @Override
    public boolean valueIsExist(String sql, String field, String result) {
        logger.info("开始查询数据库:" + sql);
        boolean flag = false;
        try {
            ResultSet set = stmt.executeQuery(sql);

            logger.info("开始查询数据库:" + sql);
            while (set.next()) {
                if (set.getString(field).equals(result)) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("查询失败", e);
        }
        return flag;
    }

    /**
     * 执行sql语句，获取field字段的列表
     * @param sql
     * @param field
     * @return
     */
    @Override
    public List select(String sql, String field) {
        List list = new ArrayList();
        try {
            ResultSet set = stmt.executeQuery(sql);
            while(set.next()){
                list.add(set.getString(field));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

