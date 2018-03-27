package com.longteng.framework.listener;

import com.longteng.framework.domain.Report;
import com.longteng.framework.domain.ReportCase;
import com.longteng.framework.domain.ReportSuite;
import com.longteng.framework.util.DataUtil;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class MyTestListener implements ITestListener {

    private static DataUtil data = DataUtil.getInstance();
    private static Logger logger = Logger.getLogger(MyTestListener.class);
    private static Report report = Report.getInstance();

    /**
     * 所有测试执行完成和对应的构造函数调用完成之后执行
     *
     * @param arg0
     */
    @Override
    public void onFinish(ITestContext arg0) {
        // TODO Auto-generated method stub
        logger.info("测试场景执行结束:" + arg0.getSuite().getName());
        logger.info("");
    }

    /**
     * 在测试类实例化之后，构造函数调用之前执行
     * 打印log信息，并注入场景名
     *
     * @param arg0
     */
    @Override
    public void onStart(ITestContext arg0) {
        String suiteName = arg0.getSuite().getName();
        ReportSuite reportSuite = report.getCurrentReportSuite();
        if (reportSuite == null) {
            logger.info("开始执行测试场景:" + suiteName);
            reportSuite = new ReportSuite();
            reportSuite.setSuiteName(suiteName);
            report.addReportSuite(reportSuite);
        } else {
            if (!suiteName.equalsIgnoreCase(reportSuite.getSuiteName())) {
                logger.info("开始执行测试场景:" + suiteName);
                reportSuite = new ReportSuite();
                reportSuite.setSuiteName(suiteName);
                report.addReportSuite(reportSuite);
            }
        }
        String suiteType = (String) arg0.getSuite().getParameter("seniorProtocol");
        arg0.setAttribute("seniorProtocol", suiteType);
        reportSuite.setSuiteType(suiteType);
        logger.info("当前执行场景类型:" + suiteType);
        data.setSenario(suiteName);
        report.setCurrentReportSuite(reportSuite);
        // TODO Auto-generated method stub
    }

    /**
     * 每次一个方法失败但已经用successPercentage注解并且这个失败仍然保持在要求的成功率内时执行
     *
     * @param arg0
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 每次测试失败时执行
     *
     * @param arg0
     */
    @Override
    public void onTestFailure(ITestResult arg0) {
        //获取到异常信息，如果你的测试代码里面没有catch异常的时候
        arg0.getThrowable().toString();
        // TODO Auto-generated method stub

    }

    /**
     * 每次跳过测试时执行
     *
     * @param arg0
     */
    @Override
    public void onTestSkipped(ITestResult arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 每次调用测试之前执行
     * 打印log信息，并注入当前测试用例名称
     * 打印检查点：初始化用例信息成功
     *
     * @param arg0
     */
    @Override
    public void onTestStart(ITestResult arg0) {
        // TODO Auto-generated method stub
        String suiteName =arg0.getTestContext().getSuite().getName();
        String caseName=arg0.getTestContext().getName();
        data.setCurrentTestCase(caseName);
        ReportCase reportCase = new ReportCase();
        report.getCurrentReportSuite().addReportCase(reportCase);
        report.setCurrentReportCase(reportCase);
        String seniorProtocol = (String) arg0.getTestContext().getAttribute("seniorProtocol");

        Object[] o = arg0.getParameters();
        Map<String, String> caseMap = (Map) o[0];
        //如果是接口测试的情况下读取EXCEL 里面的用例名称
        String interfaceCaseName = caseMap.get("用例名称");
        reportCase.setCaseName(interfaceCaseName);
        reportCase.setCaseType("interface");
        arg0.getTestContext().getCurrentXmlTest().setName(interfaceCaseName);
        arg0.getTestContext().setAttribute("caseName", interfaceCaseName);
        logger.info("开始执行测试用例:" +  interfaceCaseName + "--测试方法:" + arg0.getName());
//        if (!seniorProtocol.equalsIgnoreCase("webdriver")) {
//            Object[] o = arg0.getParameters();
//            Map<String, String> caseMap = (Map) o[0];
//            //如果是接口测试的情况下读取EXCEL 里面的用例名称
//            String interfaceCaseName = caseMap.get("用例名称");
//            reportCase.setCaseName(interfaceCaseName);
//            reportCase.setCaseType("interface");
//            arg0.getTestContext().getCurrentXmlTest().setName(interfaceCaseName);
//            arg0.getTestContext().setAttribute("caseName", interfaceCaseName);
//            logger.info("开始执行测试用例:" +  interfaceCaseName + "--测试方法:" + arg0.getName());
//        } else {
//            reportCase.setCaseType("webDriver");
//            reportCase.setCaseName(caseName);
//            logger.info("开始执行测试用例:" +  caseName + "--测试方法:" + arg0.getName());
//        }
    }

    /**
     * 每次测试成功时执行
     *
     * @param arg0
     */
    @Override
    public void onTestSuccess(ITestResult arg0) {
        // TODO Auto-generated method stub
//        MyTestResult myTestResult=(MyTestResult)arg0;
//        myTestResult.setName((String)arg0.getTestContext().getAttribute("caseName"));

    }
}

