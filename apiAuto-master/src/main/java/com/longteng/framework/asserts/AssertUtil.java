package com.longteng.framework.asserts;

import com.longteng.framework.domain.Report;
import com.longteng.framework.domain.ReportCase;
import com.longteng.framework.domain.ReportStep;
import com.longteng.framework.util.Log;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class AssertUtil {
    public static Report report = Report.getInstance();
    public static Logger logger = Log.getInstance();

    /**
     * @param:title,actual,expected,message
     * @return:
     * @Description:断言判断值是否相等
     * @Description:参数描述 title校验点的简单描述, 描述主要检查什么、actual实际值、expected预期结果、详细描述
     */
    public static void assertEquals(int actual, int expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Error e) {
        }
    }


    /**
     * @param:actual expected message
     * @return:
     * @Description:断言判断值是否相等
     * @Description:参数描述 actual实际值、expected预期结果、详细描述
     */
    public static void assertEquals(String actual, String expected, String message) {
        ReportCase reportCase = report.getCurrentReportCase();
        ReportStep reportStep = new ReportStep();
        reportStep.setStepName(reportCase.getCaseName());
        try {
            Assert.assertEquals(actual, expected, message);
            reportStep.setStatus("Pass");
        } catch (Error e) {
            try {
                reportCase.setCaseStatus(false);
            } catch (NullPointerException n) {
            }
            reportStep.setStatus("Fail");
        }
        if (null == reportCase) {
            logger.info("获取当前用例为空,不打印检查点!!!!");
        } else {
            reportStep.setActual(actual);
            reportStep.setExpected(expected);
            reportStep.setMessage(message);
            reportCase.addReportStep(reportStep);
        }
    }


    /**
     * @param:title,actual,expected,message
     * @return:
     * @Description:断言判断值是否相等
     * @Description:参数描述 title校验点的简单描述, 描述主要检查什么、actual实际值、expected预期结果、详细描述
     */
    public static void assertContains(String actual, String expected, String message) {
        ReportStep reportStep = new ReportStep();
        if (actual.contains(expected)) {
            reportStep.setStatus("Pass");
        } else {
            report.getCurrentReportCase().setCaseStatus(false);
            reportStep.setStatus("Fail");
        }
        reportStep.setActual(actual);
        reportStep.setExpected(expected);
        reportStep.setMessage(message);
        ReportCase reportCase = report.getCurrentReportCase();
        if (null == reportCase) {
            logger.debug("获取当前用例为空!!!!");
        } else {
            reportCase.addReportStep(reportStep);
        }
    }


    /**
     * @return
     * @Description:断言判断值是否相等
     */
    public static void assertEquals(Object actual, Object expected, String message) {
        ReportStep reportStep = new ReportStep();
        try {
            Assert.assertEquals(actual, expected, message);
            reportStep.setStatus("Pass");
        } catch (Error e) {
            report.getCurrentReportCase().setCaseStatus(false);
            reportStep.setStatus("Fail");
        }
        reportStep.setActual((String) actual);
        reportStep.setExpected((String) expected);
        reportStep.setMessage(message);
        report.getCurrentReportCase().addReportStep(reportStep);
        takeScreenShot();
    }

    private static void takeScreenShot() {
        String suiteType = report.getCurrentReportSuite().getSuiteType();
        if (suiteType.equalsIgnoreCase("webdriver")) {
            File path = new File("report");
            String picPath = "";
            WebDriver driver = null;
            if (driver != null)
                picPath = takeScreenShot(driver, path.getAbsolutePath());
        }
    }

    /**
     * 截图操作，图片默认后缀为png
     *
     * @param driver
     * @param savePath 图片存放路径
     * @return
     */
    private static String takeScreenShot(WebDriver driver, String savePath) {
        //        try {
//
//            File screenShotFile = ((TakesScreenshot) driver)
//                    .getScreenshotAs(OutputType.FILE);
//            String path = savePath + "\\" + screenShotFile.getName();
//            FileUtils.copyFile(screenShotFile, new File(path));
//            return path;
//        } catch (IOException ex) {
//            logger.error(ex);
//            return null;
//        }
//由于webderiver自带截图功能在有alert的情况下无法截图，所以用java自带截图功能
        String path = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
            path = savePath + "\\" + sdf.format(new Date()) + ".png";
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            // 拷贝屏幕BufferedImage对象screenshot
            BufferedImage screenshot = (new Robot())
                    .createScreenCapture(new Rectangle(0, 0,
                            (int) d.getWidth(), (int) d.getHeight()));

            // 根据文件前缀变量和文件格式变量自动生成文件名

            File f = new File(path);
//            FileUtils.copyFile(new File(name));
            // screenshot对象写入图像文件
            ImageIO.write(screenshot, "png", f);
        } catch (Exception ex) {
            logger.error(ex);
            System.out.println(ex);
        }
        return path;
    }
}

