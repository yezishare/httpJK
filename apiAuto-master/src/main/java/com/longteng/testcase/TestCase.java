package com.longteng.testcase;

import com.longteng.framework.config.Config;
import com.longteng.framework.domain.Report;
import com.longteng.framework.param.ExcelDataProvider;
import com.longteng.framework.util.DataUtil;
import com.longteng.framework.util.Log;
import com.longteng.main.RunTest;

import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class TestCase  {
    public static Logger log = Log.getInstance();
    public static Logger logger = Logger.getLogger(TestCase.class);
    public static DataUtil data = DataUtil.getInstance();
    public static Config config = RunTest.getConfig();
    private static Report report = Report.getInstance();

    /**
     * 在当前场景下，根据列名获取数据
     *
     * @param columnName
     * @return
     */
    public static String GetData(String columnName) {
        String senario = data.getSenario();
        return data.GetData(senario, columnName);
    }


    /**
     * 在当前场景下，设置列名和值
     *
     * @param columnName
     * @param values
     */
    public static void SetData(String columnName, String values) {
        String senario = data.getSenario();
        data.SetData(senario, columnName, values);
    }

    /**
     * 获得Config属性文件中ENABLE_DATABASE_CHECK的值
     * 为空或者true，返回true；反之，返回false
     *
     * @return
     */
    public boolean isEnabledDBCheck() {

        if (config.get("ENABLE_DATABASE_CHECK") == null || "true".equalsIgnoreCase(config.get("ENABLE_DATABASE_CHECK")))
            return true;
        else
            return false;

    }

    /**
     * 将符合Json格式的字符串returnStr转化为JSON对象，并将其封装到map对象中返回
     *
     * @param returnStr
     * @return
     */
    public static HashMap<String, String> SaxJsonStr(String returnStr) {
        HashMap<String, String> map = new LinkedHashMap<String, String>();
//        JSONObject obj = null;
//        try {
//            obj = JSONObject.fromObject(returnStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Set<String> set = obj.keySet();
//        for (String key : set) {
//            map.put(key, obj.getString(key));
//        }
        return map;
    }

    /**
     * 格式化日期，将";"替换成" "
     *
     * @param currentDate
     * @return
     */
    public static String dateFormat(String currentDate) {
        currentDate = currentDate.replace(";", " ");
        return currentDate;
    }

    /**
     * 获取和方法名一致的excel列的所有数据,参数化使用
     *
     * @param method
     * @return String
     */
    @DataProvider(name = "data")
    public Iterator<Object[]> dataForTestMethod(Method method)
            throws IOException {
//        String methodName = method.getName();
//        int index = methodName.lastIndexOf("_");
//        methodName = methodName.substring(index + 1);
        String senarioName = data.getSenario();
        List<String> filePath = new ArrayList<String>();
        new DataUtil().findFiles(".\\data", senarioName + ".xlsx", filePath);
        if (filePath.size() == 0) {
            logger.error(senarioName + "接口测试获取数据文件为空!");
            return null;
        }
        Iterator iterator = (Iterator<Object[]>) new ExcelDataProvider(filePath.get(0));
        return iterator;
    }
}

