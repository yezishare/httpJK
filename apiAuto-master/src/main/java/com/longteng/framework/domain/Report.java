package com.longteng.framework.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class Report {

    public static Report report = new Report();

    private Report() {
    }

    public static Report getInstance() {
        return report;
    }

    private ReportSuite currentReportSuite;
    private ReportCase currentReportCase;
    private ReportStep currentReportStep;
    private long startTime;
    private long endTime;
    private String suiteSum;
    private int caseSum;
    private int passCaseSum;
    private String runTime;
    private String passRate;
    private Map<String, ReportSuite> reportSuiteMap;

    private List<ReportSuite> reportSuiteList = new ArrayList<ReportSuite>();

    public List<ReportSuite> getReportSuiteList() {
        return reportSuiteList;
    }


    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public void addReportSuite(ReportSuite reportSuite) {
        reportSuiteList.add(reportSuite);
    }

    public ReportSuite getCurrentReportSuite() {
        return currentReportSuite;
    }

    public void setCurrentReportSuite(ReportSuite currentReportSuite) {
        this.currentReportSuite = currentReportSuite;
    }

    public ReportCase getCurrentReportCase() {
        return currentReportCase;
    }

    public void setCurrentReportCase(ReportCase currentReportCase) {
        this.currentReportCase = currentReportCase;
    }

    public ReportStep getCurrentReportStep() {
        return currentReportStep;
    }

    public void setCurrentReportStep(ReportStep currentReportStep) {
        this.currentReportStep = currentReportStep;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSuiteSum() {
        return suiteSum;
    }

    public void setSuiteSum(String suiteSum) {
        this.suiteSum = suiteSum;
    }

    public int getCaseSum() {
        return caseSum;
    }

    public void setCaseSum(int caseSum) {
        this.caseSum = caseSum;
    }

    public int getPassCaseSum() {
        return passCaseSum;
    }

    public void setPassCaseSum(int passCaseSum) {
        this.passCaseSum = passCaseSum;
    }

    public Map<String, ReportSuite> getReportSuiteMap() {
        return reportSuiteMap;
    }

    public void setReportSuiteMap(Map<String, ReportSuite> reportSuiteMap) {
        this.reportSuiteMap = reportSuiteMap;
    }
}

