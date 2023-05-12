package BLL;

import BE.*;
import DAL.ReportDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReportManager {
    private ReportDAO reportDAO;

    public ReportManager(){
        reportDAO = new ReportDAO();
    }

    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        reportDAO.createNewReport(reportName,reportDescription, caseID, userID);
    }


    public List<Report> getReports(int caseID) throws SQLException {
        return reportDAO.getReports(caseID);
    }
    public List<ReportCaseAndCustomer> getAllReports() throws SQLException {
        return reportDAO.getAllReports();
    }

    public void createNewAddendum(String addendumName, String addendumDescription, int caseID, int reportID, int userID) throws SQLException {
        reportDAO.createNewAddendum(addendumName, addendumDescription, caseID, reportID, userID);

    }

    public List<Addendum> getAddendums(int caseID, int reportID) throws SQLException {
        return reportDAO.getAddendums(caseID, reportID);
    }


    public void SaveTextToReport(int position, int reportID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.SaveTextToReport(position, reportID, txt, userID, createdDate, createdTime);
    }


    public void SaveImageToReport(int position, int reportID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.SaveImageToReport(position, reportID, dataImage, comment, userID, createdDate, createdTime);
    }

    public List<TextOnReport> getAllTextFieldsForReport(int currentReportID) throws SQLException {
        return reportDAO.getAllTextFieldsForReport(currentReportID);
    }

    public List<ImageOnReport> getAllImagesForReport(int currentReportID) throws SQLException {
        return reportDAO.getAllImagesForReport(currentReportID);
    }
}