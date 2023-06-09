package BE;

import java.time.LocalDate;

public class Report {
    private int reportID;
    private String reportName;
    private String reportDescription;
    private int caseID;
    private String assignedTechnician;
    private LocalDate createdDate;
    private String isActive;

    public Report(int reportID, String reportName, String reportDescription, int caseID, String assignedTechnician, LocalDate createdDate, String isActive) {
        this.reportID = reportID;
        this.reportName = reportName;
        this.reportDescription = reportDescription;
        this.caseID = caseID;
        this.assignedTechnician = assignedTechnician;
        this.createdDate = createdDate;
        this.isActive = isActive;
    }

    public Report(int reportID, String reportName, String reportDescription, String assignedTechnician, LocalDate createdDate, String isActive) {
        this.reportID = reportID;
        this.reportName = reportName;
        this.reportDescription = reportDescription;
        this.assignedTechnician = assignedTechnician;
        this.createdDate = createdDate;
        this.isActive = isActive;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(String assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
