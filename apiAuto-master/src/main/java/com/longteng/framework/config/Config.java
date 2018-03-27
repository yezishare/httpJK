package com.longteng.framework.config;

import com.longteng.framework.util.Log;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class Config
{
    private InputStream in;
    private Properties properties;
    private Logger logger;

    /**
     * 载入配置文件config.properties，从输入流中读取属性列表
     * @param path
     */
    public Config(String path)
    {
        logger = Log.getInstance();
        //	in = this.getClass().getResourceAsStream(path);
        //	File file = new File(path);
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e);
        }
        properties = new Properties();
        load();
    }

    /**
     * 将输入流加载到属性文件中
     */
    private void load()
    {
        try
        {
            properties.load(in);
        }
        catch(Exception e)
        {
            logger.error("载入配置文件异常", e);
        }
    }

    /**
     * 根据属性文件中的key，获取属性文件中相应的value
     * @param strName
     * @return
     */
    public String get(String strName)
    {
        String values= "";
        if(properties.getProperty(strName) != null)
        {
            values =  properties.getProperty(strName).trim();
        }
        return values;
    }
}

