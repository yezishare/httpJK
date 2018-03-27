package com.longteng.framework.report.template;

import com.longteng.framework.domain.Report;
import com.longteng.framework.domain.ReportCase;
import com.longteng.framework.domain.ReportStep;
import com.longteng.framework.domain.ReportSuite;
import com.longteng.framework.util.DateUtil;
import com.longteng.framework.util.FileUtil;
import com.longteng.framework.util.OS;
import org.apache.log4j.Logger;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class ReportHtml {
    private static Report report = Report.getInstance();
    public static Logger logger = Logger.getLogger(ReportHtml.class);
    private static int allCase = 0;
    private static int passCase = 0;
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    public static void main(String[] args) {
        List<ReportSuite> suiteList = new ArrayList<ReportSuite>();
        ReportSuite reportSuite = new ReportSuite();
        reportSuite.setSuiteName("接口测试");
        reportSuite.setSuiteType("interface");

        List<ReportCase> reportCases = new ArrayList<ReportCase>();
        ReportCase reportCase = new ReportCase();
        reportCase.setCaseName("接口测试用例");

        List<ReportStep> reportStepList = new ArrayList<ReportStep>();
        ReportStep reportStep = new ReportStep();
        reportStep.setStatus("PASS");
        reportStep.setActual("实际结果的值");
        reportStep.setExpected("预期结果的值");
        reportStepList.add(reportStep);
        reportCase.setReportStepList(reportStepList);

        reportCases.add(reportCase);
        reportSuite.setReportCaseList(reportCases);

        suiteList.add(reportSuite);

        ReportSuite reportSuite22 = new ReportSuite();
        reportSuite22.setSuiteName("UI测试");
        reportSuite22.setSuiteType("webdriver");

        List<ReportCase> reportCases222 = new ArrayList<ReportCase>();
        ReportCase reportCase222 = new ReportCase();
        reportCase222.setCaseName("Tttt");

        List<ReportStep> reportStepList222 = new ArrayList<ReportStep>();
        ReportStep reportStep222 = new ReportStep();
        reportStep222.setStatus("FAIL");
        reportStep222.setActual("实际结果的值");
        reportStep222.setExpected("预期结果的值");
        reportStepList222.add(reportStep222);

        ReportStep reportStep2223 = new ReportStep();
        reportStep2223.setStatus("FAIL");
        reportStep2223.setActual("实际结果的值");
        reportStep2223.setExpected("预期结果的值");
        reportStepList222.add(reportStep2223);
        reportCase222.setReportStepList(reportStepList222);


        reportCases222.add(reportCase222);


        ReportCase reportCase333 = new ReportCase();
        reportCase333.setCaseName("Aaaaa");
        reportCases222.add(reportCase333);
        reportSuite22.setReportCaseList(reportCases222);
        suiteList.add(reportSuite22);


        String html = getTitle() + getBodyTitle() + getSummary() + getSuiteList(suiteList) + getCaseStep(suiteList) + htmlEnd();
        String path = "D://report/writeReport.html";
//        FileUtil.writeFile(path, html);
    }

    public static void writeReport(List<ReportSuite> suiteList) {
        logger.info("开始写入报告!!!");
        String title = getTitle() + getBodyTitle();
        String suiteBody = getSuiteList(suiteList) + getCaseStep(suiteList) + htmlEnd();
        String summary = getSummary();
        String mailHtml = title + summary;
        String html = title + summary + suiteBody;
        String path = "";
        File directory = new File("report");// 设定为当前文件夹
        try {
            path = directory.getAbsolutePath();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        String htmlPath = path + "\\report.html";
        FileUtil.writeFile(htmlPath, html);
        logger.info("报告写入结束!!!");

        logger.info("生成饼状图!!!");
        try {
            DefaultPieDataset dataset = new DefaultPieDataset();
            String casePassCount = passCase + "";
            String caseFailCount = (allCase - passCase) + "";
            dataset.setValue("PassCase", new Double(casePassCount));
            dataset.setValue("FailCase", new Double(caseFailCount));
            String pngPath = path + "\\report.png";
            PicReport picReport = new PicReport();
            picReport.save(dataset, pngPath, "通过率");  //写入生成饼状图
            logger.info("饼状图生成结束!!!");
        } catch (Exception e) {
            logger.error("饼状图生成失败:" + e);
        }
    }

    private static String getTitle() {
        return getHeadStart() + getStyle() + getJavaScript() + getHeadEnd();
    }

    private static String getBodyTitle() {
        String reportName = "自动化测试报告";
        String date = DateUtil.getCurrentDate();
        String bodyTitle = "<body><h1>" + reportName + "</h1>" +
                "<table width=\"100%\">" +
                "    <tr>" +
                "        <td align=\"left\">Date report: " + date + "</td>" +
                "        <td align=\"right\">Designed for use with <a href=\"#\">longteng</a> and <a href=\"#\">牛金亮</a>.</td>" +
                "    </tr>" +
                "</table>";
        return bodyTitle;
    }


    private static String getSummary() {
        int failCase = allCase - passCase;
        String passRate = numberFormat.format((float) passCase / (float) allCase * 100) + "%";  //通过率
        report.setPassRate(passRate);
        String runTime = report.getRunTime();
        String ip = OS.getLocalIP();
        String user = "yezi";
        String summary = "" +
                "<hr size=\"1\">" +
                "<h2>概要</h2>" +
                "<table align=\"center\" class=\"details\" border=\"0\" cellpadding=\"5\" cellspacing=\"2\" width=\"95%\">" +
                "    <tr valign=\"top\">" +
                "        <th>用例总数</th>" +
                "        <th>通过数</th>" +
                "        <th>失败数</th>" +
                "        <th>通过率</th>" +
                "        <th>运行时间</th>" +
                "        <th>执行机器IP</th>" +
                "        <th>执行人</th>" +
                "    </tr>" +
                "    <tr valign=\"top\" class=\"\">" +
                "        <td align=\"center\">" + allCase + "</td>" +
                "        <td align=\"center\">" +
                "           <span style=\"color: green\">" +
                "             <b>" + passCase + "</b>" +
                "           </span>" +
                "        </td>" +
                "        <td align=\"center\">" +
                "              <span style=\"color: red;width:120px;height:20px;\">" +
                "             <b>" + failCase + "</b>" +
                "           </span></td>" +
                "        <td align=\"center\">" + passRate + "</td>" +
                "        <td align=\"center\">" + runTime + "</td>" +
                "        <td align=\"center\">" + ip + "</td>" +
                "        <td align=\"center\">" + user + "</td>" +
                "    </tr>" +
                "</table>" +
                "</br>";
        return summary;
    }

    private static int caseNum = 0;

    private static String getSuiteList(List<ReportSuite> suiteList) {
        String s = "<hr size=\"1\" width=\"100%\" align=\"center\">" +
                "<div>" +
                "    <div id=\"div_left\" style=\"overflow:auto\">" +
                " <table id=\"suites\">";
        String suites = "";
        for (int i = 0; i < suiteList.size(); i++) {
            ReportSuite reportSuite = suiteList.get(i);
            String suiteName = reportSuite.getSuiteName();
            String tbodyId = "tests-" + i;
            String toggleId = "toggle-" + i;
            suites += "<thead>" +
                    "            <tr>" +
                    "                <th class=\"header suite\" onclick=\"toggleElement('" + tbodyId + "', 'table-row-group'); toggle('" + toggleId + "')\">" +
                    "                    <span id=\"" + toggleId + "\" class=\"toggle\">&#x25bc;</span>" +
                    "                    <span id=\"" + i + "\">" + suiteName + "</span>" +
                    "                </th>" +
                    "            </tr>" +
                    "            </thead>";
            String suiteCase = getCaseList(tbodyId, reportSuite.getReportCaseList());
            suites += suiteCase;
        }
        String center = "<div id=\"div_center\">" +
                "        <HR align=center width=1 style=\"height:100%;\" size=100 color=#00686b>" +
                "    </div>";
        s = s + suites + "  </table></div>" + center;
        report.setCaseSum(allCase);
        report.setPassCaseSum(passCase);
        return s;
    }

    private static String getCaseList(String tbodyId, List<ReportCase> caseList) {
        String c = "<tbody id=\"" + tbodyId + "\" class=\"tests\">";
        for (int i = 0; i < caseList.size(); i++) {
            ReportCase reportCase = caseList.get(i);
            String caseName = reportCase.getCaseName();
            boolean caseStatus = reportCase.isCaseStatus();
            c += "<tr>" +
                    " <td class=\"test\">";
            allCase++;
            if (caseStatus) {
                passCase++;
                c += " <span class=\"successIndicator\" title=\"全部通过\">&#x2714;</span>";
            } else {
                c += " <span class=\"failureIndicator\" title=\"部分失败\">&#x2718;</span>";
            }
            c += "   <a id=\"Case" + caseNum + "\" href=\"#\" onclick=\"showDetail(this)\">" + caseName + "</a>" +
                    "   </td>" +
                    "   </tr>";
            caseNum++;
        }
        c += "</tbody>";
        return c;
    }


    private static String htmlEnd() {
        return "</div></div></body></html>";
    }

    private static String getCaseStep(List<ReportSuite> suiteList) {
        String div = "<div id=\"div_right\" style=\"overflow:auto\">";

        caseNum = 0;
        String caseDiv = "";
        String firstCaseName = "";
        String firstSuiteType = "";
        String firstSuiteName = "";
        for (int i = 0; i < suiteList.size(); i++) {
            ReportSuite reportSuite = suiteList.get(i);
            List<ReportCase> reportCaseList = reportSuite.getReportCaseList();
            String step = "";
            String suiteType = reportSuite.getSuiteType();
            if (i == 0) {
                firstSuiteName = reportSuite.getSuiteName();
                firstSuiteType = suiteType;
            }
            for (int k = 0; k < reportCaseList.size(); k++) {
                ReportCase reportCase = reportCaseList.get(k);
                String caseName = reportCase.getCaseName();
                List<ReportStep> reportStepList = reportCase.getReportStepList();
                String display = "none";
                if (caseNum == 0) {
                    display = "";
                    firstCaseName = caseName;
                }
                step += "<div id =\"parentCase" + caseNum + "\" style=\"display: " + display + "\">";
                String resultTable = "<table  class=\"resultsTable\" >";
                for (int j = 0; j < reportStepList.size(); j++) {
                    ReportStep reportStep = reportStepList.get(j);
                    String stepNum = "step" + (j + 1);
                    String stepStatus = reportStep.getStatus();
                    String actual = reportStep.getActual();
                    String expected = reportStep.getExpected();
                    String message = reportStep.getMessage();

                    if (suiteType.equalsIgnoreCase("webDriver")) {
                        resultTable += "<tr>" +
                                "     <td class=\"method\" style=\"width:15%\">" +
                                "     " + stepNum + "" +
                                "      </td>";
                    } else {
                        resultTable += "<tr>" +
                                "       <td class=\"method\" style=\"width:15%\">" +
                                "       " + caseName + "" +
                                "       </td>";
                    }
                    if (stepStatus.equalsIgnoreCase("pass")) {
                        resultTable += "<td class=\"passed\" style=\"width:5%\">" +
                                " " + stepStatus + "" +
                                " </td>";
                    } else {
                        resultTable += "<td class=\"failed\" style=\"width:5%\">" +
                                " " + stepStatus + "" +
                                " </td>";
                    }
                    resultTable += " <td style=\"width:85%\">" +
                            "            <i>预期结果:</i> <span class=\"arguments\">" + expected + "</span><br/>" +
                            "            <i>实际结果:</i> <span class=\"arguments\">" + actual + "</span><br/>" +
                            "            <i>描述:</i> <span class=\"arguments\">" + message + "</span><br/>" +
                            "        </td>" +
                            "</tr>";
                }
                resultTable += "</table></div>";
                step += resultTable;
                caseNum++;
            }
            caseDiv = caseDiv + step;
        }
        div += "<div>" +
                "        <div>" +
                "            <table>" +
                "                <tr>" +
                "                    <td>";
        if (firstSuiteType.equalsIgnoreCase("webDriver")) {
            div += "<h1 id =\"caseName\">当前用例:" + firstCaseName + "</h1>";
        } else {
            div += "<h1 id =\"caseName\">当前场景:" + firstSuiteName + "</h1>";
        }
        div += "</td>" +
                "                    <td style=\"width: 30%\">" +
                "                    </td>";
        div += "                    <td style=\"float: right\">" +
                "                        <input type=\"radio\" name=\"caseStatus\" value=0 onchange=\"onchangeRadio(this)\" checked=\"checked\"/>所有步骤" +
                "                        <input type=\"radio\" name=\"caseStatus\" value=1 onchange=\"onchangeRadio(this)\"/>失败步骤" +
                "                        <input type=\"radio\" name=\"caseStatus\" value=2 onchange=\"onchangeRadio(this)\"/>通过步骤" +
                "                    </td>";
        div += "</tr></table></div></div>";
        String resultTitleTable = "<table  class=\"resultsTitleTable\" ><tr>" +
                "                <th id=\"headerStepColor\" colspan=\"3\" class=\"header skipped\">" +
                "                   <span id=\"headerStepColorSpan\">所有步骤</span></th> " +
                "                                   </tr> " +
                "                               <tr> " +
                "                                       <th align=\"center\" style=\"width:15%\"> " +
                "                                             用例步骤 " +
                "                                         </th>" +
                "                                         <th align=\"center\" style=\"width:5%\"> " +
                "                                             状态 " +
                "                                         </th> " +
                "                                           <th align=\"center\" style=\"width:80%\"> " +
                "                                             描述 " +
                "                                         </th> " +
                "                                   </tr></table>";
        div += resultTitleTable;
        div += caseDiv;
        div += "  <input id=\"allCaseNum\" type=\"hidden\" value=\"" + caseNum + "\">" +
                " <input id=\"currentCaseId\" type=\"hidden\" value=\"parentCase0\"></div>";
        return div;
    }

    private static String getStyle() {
        String style = "<style type=\"text/css\">" +
                "        body {" +
                "            font:normal 68% verdana,arial,helvetica;" +
                "            color:#000000;" +
                "        }" +
                "        table tr td, table tr th {" +
                "            font-size: 68%;" +
                "        }" +
                "        table.details tr th{" +
                "            color: #ffffff;" +
                "            font-weight: bold;" +
                "            text-align:center;" +
                "            background:#2674a6;" +
                "            white-space: nowrap;" +
                "        }" +
                "        table.details tr td{" +
                "            background:#eeeee0;" +
                "            white-space: nowrap;" +
                "        }" +
                "        h1 {" +
                "            margin: 0px 0px 5px; font: 165% verdana,arial,helvetica" +
                "        }" +
                "        h2 {" +
                "            margin-top: 1em; margin-bottom: 0.5em; font: bold 125% verdana,arial,helvetica" +
                "        }" +
                "        h3 {" +
                "            margin-bottom: 0.5em; font: bold 115% verdana,arial,helvetica" +
                "        }" +
                "        .Failure {" +
                "            font-weight:bold; color:red;" +
                "        }" +
                "" +
                "" +
                "        img" +
                "        {" +
                "            border-width: 0px;" +
                "        }" +
                "" +
                "" +
                "        #div_left {" +
                "            float: left;" +
                "            width: 15%;" +
                "            height: 100%;" +
                "        }" +
                "        #div_center {" +
                "            float: left;" +
                "            width: 2%;" +
                "            height: 100%;" +
                "        }" +
                "        #div_right {" +
                "            float: left;" +
                "            width: 83%;" +
                "            height: 100%;" +
                "        }" +
                "    </style>" +
                "    <style type=\"text/css\">" +
                "        #suites {" +
                "            line-height: 1.7em;" +
                "            border-spacing: 0.1em;" +
                "            width: 100%;" +
                "        }" +
                "        .suite {" +
                "            background-color: #999999;" +
                "            font-weight: bold;" +
                "        }" +
                "        .header {" +
                "            font-size: 1.0em;" +
                "            font-weight: bold;" +
                "            text-align: left;" +
                "        }" +
                "        .header.suite {" +
                "            cursor: pointer;" +
                "            clear: right;" +
                "            height: 1.214em;" +
                "            margin-top: 1px;" +
                "        }" +
                "        .toggle {" +
                "            font-family: monospace;" +
                "            font-weight: bold;" +
                "            padding-left: 2px;" +
                "            padding-right: 5px;" +
                "            color: #777777;" +
                "        }" +
                "        .test {" +
                "            background-color: #eeeeee;" +
                "            padding-left: 2em;" +
                "        }" +
                "        .successIndicator {" +
                "            float: right;" +
                "            font-family: monospace;" +
                "            font-weight: bold;" +
                "            padding-right: 2px;" +
                "            color: #44aa44;" +
                "        }" +
                "" +
                "        .skipIndicator {" +
                "            float: right;" +
                "            font-family: monospace;" +
                "            font-weight: bold;" +
                "            padding-right: 2px;" +
                "            color: #ffaa00;" +
                "        }" +
                "        .failureIndicator {" +
                "            float: right;" +
                "            font-family: monospace;" +
                "            font-weight: bold;" +
                "            padding-right: 2px;" +
                "            color: #ff4444;" +
                "        }" +
                " .resultsTitleTable {\n" +
                "        border: 0;\n" +
                "        width: 100%;\n" +
                "        margin-top: 1.8em;\n" +
                "        line-height: 1.7em;\n" +
                "        border-spacing: 0.1em;\n" +
                "    }" +
                "        .resultsTable {" +
                "            border: 0;" +
                "            width: 100%;" +
//                "            margin-top: 1.8em;" +
                "            line-height: 1.7em;" +
                "            border-spacing: 0.1em;" +
                "        }" +
                "" +
                "        .resultsTable .method {" +
                "            width: 1em;" +
                "        }" +
                "        .resultsTable.passed {\n" +
                "        background: #008000;" +
                "width: 1em;\n" +
                "    }\n" +
                "    .resultsTable.failure {\n" +
                "        background: red;" +
                "width: 1em;\n" +
                "    }" +
                "        .resultsTable .duration {" +
                "            width: 6em;" +
                "        }" +
                "" +
                "        .resultsTable td {" +
                "            vertical-align: top;" +
                "        }" +
                "" +
//                "        .resultsTable th {" +
//                "            padding: 0 .2em;" +
//                "               width:150px;" +
//                "        }" +
                "        .passed {" +
                "            background-color: #44aa44;" +
                "width: 1em;" +
                "        }" +
                "" +
                "        .skipped {" +
                "            background-color: #ffaa00;" +
                "width: 1em;" +
                "        }" +
                "" +
                "        .failed {" +
                "            background-color: #ff4444;" +
                "width: 1em;" +
                "        }" +
                "        .arguments {" +
                "            font-family: Lucida Console, Monaco, Courier New, monospace;" +
                "            font-weight: bold;" +
                "        }" +
                "    </style>\n";
        return style;
    }

    private static String getJavaScript() {
        String javaScript = "\n<script language=\"JavaScript\">\n" +
                "        function toggleElement(elementId, displayStyle)" +
                "        {" +
                "            var current = getStyle(elementId, 'display');" +
                "            document.getElementById(elementId).style.display = (current == 'none' ? displayStyle : 'none');" +
                "        }" +
                "        function getStyle(elementId, property)" +
                "        {" +
                "            var element = document.getElementById(elementId);" +
                "            return element.currentStyle ? element.currentStyle[property]" +
                "                    : document.defaultView.getComputedStyle(element, null).getPropertyValue(property);" +
                "        }" +
                "\n" +
                "        function toggle(toggleId)" +
                "        {" +
                "            var toggle;" +
                "            if (document.getElementById)" +
                "            {" +
                "                toggle = document.getElementById(toggleId);" +
                "            }" +
                "            else if (document.all)" +
                "            {" +
                "                toggle = document.all[toggleId];" +
                "            }" +
                "            toggle.textContent = toggle.innerHTML == '\\u25b6' ? '\\u25bc' : '\\u25b6';" +
                "        }\n" +
                "function onchangeRadio(obj) {\n" +
                "    var headerStepColorObj = document.getElementById(\"headerStepColor\");\n" +
                "    var headerStepColorSpanObj = document.getElementById(\"headerStepColorSpan\");" +
                "    switch (obj.value) {\n" +
                "        case \"0\":\n" +
                "           headerStepColorSpanObj.innerHTML=\"所有用例步骤\";\n" +
                "           headerStepColorObj.setAttribute(\"class\", \"header skipped\");\n" +
                "            break\n" +
                "        case \"1\":\n" +
                "            headerStepColorSpanObj.innerHTML=\"所有失败步骤\";\n" +
                "            headerStepColorObj.setAttribute(\"class\", \"header failed\");\n" +
                "            break\n" +
                "        case \"2\":\n" +
                "            headerStepColorSpanObj.innerHTML=\"所有通过步骤\";\n" +
                "            headerStepColorObj.setAttribute(\"class\", \"header passed\");\n" +
                "            break\n" +
                "    }\n" +
                "}\n" +
                "function showDetail(obj) {\n" +
                " var caseId = obj.id;\n " +
                " document.getElementById(\"currentCaseId\").value = caseId; \n" +
                " var caseName =obj.text;\n" +
                " document.getElementById(\"caseName\").innerHTML=\"当前用例:\"+caseName;\n" +
                "  var parentCaseId=\"parent\"+caseId;\n " +
                "reSetRadio();  \n " +
                " var allCaseNum = document.getElementById(\"allCaseNum\").value;\n" +
                "        for (var i = 0; i < allCaseNum; i++) {\n" +
                "            var div = \"parentCase\" + i;\n" +
                "            if (div == parentCaseId) {\n" +
                "               document.getElementById(div).style.display=\"inline\"\n" +
                "            } else {\n" +
                "               document.getElementById(div).style.display=\"none\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "function reSetRadio() {\n" +
                "        var chkObjs = document.getElementsByName(\"caseStatus\");\n" +
                "        chkObjs[0].checked  =\"checked\";\n" +
                "        document.getElementById(\"headerStepColorSpan\").innerHTML=\"所有用例步骤\";\n" +
                "        document.getElementById(\"headerStepColor\").setAttribute(\"class\", \"header skipped\");\n" +
                "    }\n" +
                "    </script>";

        return javaScript;
    }

    private static String getHeadStart() {
        String title = "<!DOCTYPE html private \"-//W3C//DTD HTML 4.01 Transitional//EN\">" +
                "<html>" +
                "<head>" +
                "    <META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "    <title>Load Test Results</title>";

        return title;
    }

    private static String getHeadEnd() {
        return "</head>";
    }

}

