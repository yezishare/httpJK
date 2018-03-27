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
public class SqlServerDB implements IDatabase {

    private static String url = "";//characterEncoding=GBK
    private static String username = "";
    private static String password = "";
    public static Connection con;
    public static Statement stmt;
    public static ResultSet rs;
    public static Config config = new Config(Constants.config);
    public static Logger logger = Log.getInstance();
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
        SqlServerDB.username = username;
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
    public static void setPassword(String password) {
        SqlServerDB.password = password;
    }

    public void initBySid(String sid) {

        if (sid.equalsIgnoreCase(sid)) {
            this.setUsername(config.get(sid+"UID"));
            this.setPassword(config.get(sid+"PWD"));
        }

    }

    /**
     * 执行sql语句，返回结果集
     * @param sql
     * @return
     */
    public ResultSet select(String sql) {
        try {
            Log.getInstance().info("开始查询数据库:" + sql);

            return stmt.executeQuery(sql);
        } catch (Exception e) {
            Log.getInstance().error("查询失败", e);
            return null;
        }
    }

    /**
     * 连接sid数据库
     * @param sid
     */
    @Override
    public void connect(String sid) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            initBySid(sid);
        } catch (ClassNotFoundException e) {
            Log.getInstance().error(e);
        }
        try {
            String url = "jdbc:sqlserver://" + config.get(sid+"IP") + ":" + config.get(sid+"PORT") + "; DatabaseName=" + config.get(sid+"Server");
            con = DriverManager.getConnection(url, getUsername(), getPassword());
            stmt = con.createStatement();
        } catch (SQLException e) {
            Log.getInstance().error("连接数据库失败", e);
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
        Log.getInstance().info("开始查询数据库:" + sql);
        HashMap<String, String> tempMap = new HashMap<String, String>();
        try {
            ResultSet set = stmt.executeQuery(sql);
            set.next();
            String[] arr = field.split(Constants.Comma);
            Log.getInstance().info("开始查询数据库:" + sql);
            for (int i = 0; i < arr.length; i++) {
                tempMap.put(arr[i], set.getString(arr[i]));
            }

        } catch (Exception e) {
            Log.getInstance().error("查询失败", e);
            return null;
        }
        return tempMap;
    }

    /**
     * 根据sql更新数据库
     * @param sql
     * @return
     */
    @Override
    public int update(String sql) {
        try {
            Log.getInstance().info("开始更新数据库:" + sql);
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Log.getInstance().error("更新失败", e);
            return -1;
        }
    }

    /**
     * 根据sql和字段field查询多条记录
     * @param sql
     * @param field
     * @return
     */
    @Override
    public List<HashMap<String, String>> selectMutiRecord(String sql,
                                                          String field) {
        Log.getInstance().info("开始查询数据库:" + sql);
        List<HashMap<String, String>> list = new ArrayList();
        HashMap<String, String> tempMap = new HashMap<String, String>();
        try {
            ResultSet set = stmt.executeQuery(sql);

            String[] arr = field.split(Constants.Comma);
            Log.getInstance().info("开始查询数据库:" + sql);
            while (set.next()) {
                for (int i = 0; i < arr.length; i++) {
                    tempMap.put(arr[i], set.getString(arr[i]));
                }
                list.add(tempMap);
            }
        } catch (Exception e) {
            Log.getInstance().error("查询失败", e);
            return list;
        }
        return list;

    }

    public static String getRs(String sql, String field) {
        ConnectSqlServer();
        String result = null;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result = rs.getString(field);

            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return result;

    }


    public static void Close() {

        try {
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void ConnectSqlServer() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        } catch (ClassNotFoundException e) {
            Log.getInstance().error(e);
        }
        try {
            String url = "jdbc:sqlserver://" + config.get("SqlServerIP") + ":1433;databasename=" + config.get("SqlServerDB"); //orcl为数据库的SID
            con = DriverManager.getConnection(url, config.get("afsUID"), config.get("afsPWD"));
            stmt = con.createStatement();
        } catch (SQLException e) {
            Log.getInstance().error("连接数据库失败", e);
        }
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
        Log.getInstance().info("开始查询数据库:" + sql);
        boolean flag = false;
        try {
            ResultSet set = stmt.executeQuery(sql);

            Log.getInstance().info("开始查询数据库:" + sql);
            while (set.next()) {
                if (set.getString(field).equals(result)) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            Log.getInstance().error("查询失败", e);
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

        return list;
    }
}

