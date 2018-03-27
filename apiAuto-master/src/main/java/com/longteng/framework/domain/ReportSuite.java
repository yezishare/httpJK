package com.longteng.framework.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class ReportSuite {

    private String suiteName;

    public String getSuiteName() {
        return suiteName;
    }

    private String suiteType;

    public String getSuiteType() {
        return suiteType;
    }

    public void setSuiteType(String suiteType) {
        this.suiteType = suiteType;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public List<ReportCase> reportCaseList = new ArrayList<ReportCase>();

    public void addReportCase(ReportCase reportCase) {
        reportCaseList.add(reportCase);
    }

    public List<ReportCase> getReportCaseList() {
        return reportCaseList;
    }

    public void setReportCaseList(List<ReportCase> reportCaseList) {
        this.reportCaseList = reportCaseList;
    }
}

