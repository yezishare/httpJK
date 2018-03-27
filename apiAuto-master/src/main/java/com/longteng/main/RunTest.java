package com.longteng.main;

import com.longteng.framework.config.Config;
import com.longteng.framework.config.Constants;
import com.longteng.framework.domain.Report;
import com.longteng.framework.domain.ReportSuite;
import com.longteng.framework.listener.MyTestListener;
import com.longteng.framework.mail.SendEmail;
import com.longteng.framework.report.template.ReportHtml;
import com.longteng.framework.suite.LoadSuite;
import com.longteng.framework.util.DataUtil;
import com.longteng.framework.util.FileUtil;
import com.longteng.framework.util.Log;
import org.apache.log4j.Logger;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class RunTest {
    private static Logger log = Log.getInstance();
    public static Config config = new Config(Constants.config);
    private static DataUtil data = DataUtil.getInstance();
    private static Report report = Report.getInstance();
    private static Logger logger = Logger.getLogger(RunTest.class);
    private static List<XmlSuite> xmlSuiteList = loadSuite();

    public void runTest() {

        TestNG testng = new TestNG();
        testng.addListener(new MyTestListener()); //加入监听
        boolean UseDefaultListeners = false;
        testng.setUseDefaultListeners(UseDefaultListeners); //设置报告信息
        //判断是否生成testNg自带报告
        if (config.get("REPORT_PRIORITY") == "1" || "1".equals(config.get("REPORT_PRIORITY"))) {
            List<Class> listenerClass = new ArrayList<Class>();
            listenerClass.add(org.uncommons.reportng.HTMLReporter.class);
            testng.setListenerClasses(listenerClass);
        }
        testng.setOutputDirectory(Constants.Out_Dir);    //设置报告输出路径
        testng.setXmlSuites(xmlSuiteList); //设置运行套件
        try {
            long startTime = System.currentTimeMillis();
            report.setStartTime(startTime);
            logger.info("开始所有运行测试!");
            testng.run();
            logger.info("所有测试运行结束!");
            long entTime = System.currentTimeMillis();
//            DriverFactory.closePage();
            report.setStartTime(startTime);
            long runTime = entTime - startTime;
            report.setRunTime((runTime / 1000) + "秒");
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        FileUtil.DeleteFolder("report", false);
        List<ReportSuite> reportSuiteList = report.getReportSuiteList();
        ReportHtml.writeReport(reportSuiteList);
        try {
            String sendTo = config.get("sendTo");
            if (!"".equalsIgnoreCase(sendTo)) {
                logger.info("开始发送邮件!");
                SendEmail.sendEmail();
                logger.info("发送邮件完毕!");
            }else {
                logger.info("没有收件人信息不发送邮件!");
            }
        } catch (Exception e) {
            logger.error("发送邮件异常:" + e);
            e.printStackTrace();
        }

        logger.info("工程执行结束!");
    }

    public static void main(String[] args) {
        RunTest runTest = new RunTest();
        runTest.runTest();
    }

    /**
     * 加载测试套件
     *
     * @return
     */
    private static List<XmlSuite> loadSuite() {
        logger.info("开始加载测试场景!");
        xmlSuiteList = LoadSuite.getXmlSuites();
        logger.info("加载测试场景结束!");
        return xmlSuiteList;
    }


    /**
     * 获取config.properties属性配置
     *
     * @return
     */
    public static Config getConfig() {
        return config;
    }
}

