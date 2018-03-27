package com.longteng.framework.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class ReportCase {
    private String caseName;
    private boolean caseStatus = true;
    private String caseType;
    private List<ReportStep> reportStepList = new ArrayList<ReportStep>();
    private ReportStep reportStep;

    public boolean isCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(boolean caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public ReportStep getReportStep() {
        return reportStep;
    }

    public void addReportStep(ReportStep reportStep) {
        this.getReportStepList().add(reportStep);
    }

    public void setReportStep(ReportStep reportStep) {
        this.reportStep = reportStep;
    }

    public List<ReportStep> getReportStepList() {
        return reportStepList;
    }

    public void setReportStepList(List<ReportStep> reportStepList) {
        this.reportStepList = reportStepList;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
}

