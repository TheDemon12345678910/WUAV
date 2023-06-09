package BLL;

import BE.*;
import DAL.ReportDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReportManager {
    private ReportDAO reportDAO;

    public ReportManager() {
        reportDAO = new ReportDAO();
    }

    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        reportDAO.createNewReport(reportName, reportDescription, caseID, userID);
    }


    public List<Report> getReports(int caseID) throws SQLException {
        return reportDAO.getReports(caseID);
    }

    public Report getChosenReport(int reportID) throws SQLException {
        return reportDAO.getChosenReport(reportID);
    }

    public List<ReportCaseAndCustomer> getAllReports() throws SQLException {
        return reportDAO.getAllReports();
    }


    public void SaveTextToReport(int position, int reportID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.SaveTextToReport(position, reportID, txt, userID, createdDate, createdTime);
    }


    public void SaveImageToReport(int position, int reportID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.SaveImageToReport(position, reportID, dataImage, comment, userID, createdDate, createdTime);
    }

    public List<TextsAndImagesOnReport> getImagesAndTextsForReport(int currentReportID) throws SQLException {
        return reportDAO.getImagesAndTextsForReport(currentReportID);
    }

    public void updateImageInReport(int imageID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.updateImageInReport(imageID, dataImage, comment, userID, createdDate, createdTime);
    }

    public void deletePartOfReport(TextsAndImagesOnReport textOrImage) throws SQLException {
        reportDAO.deletePartOfReport(textOrImage);
    }

    public void updateTextInReport(int textID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.updateTextInReport(textID, txt, userID, createdDate, createdTime);
    }

    public void moveItemUp(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException {
        reportDAO.moveItemUp(textOrImageID, positionOnReport);
    }

    public void moveItemDown(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException {
        reportDAO.moveItemDown(textOrImageID, positionOnReport);
    }

    public void submitReportForReview(int reportID) throws SQLException {
        reportDAO.submitReportForReview(reportID);
    }

    public void closeReport(int reportID) throws SQLException {
        reportDAO.closeReport(reportID);
    }

    public void updateReport(int reportID, String reportName, String reportDescription, int userID) throws SQLException {
        reportDAO.updateReport(reportID, reportName, reportDescription, userID);
    }

    public void deleteReport(int reportID) throws SQLException {
        reportDAO.deleteReport(reportID);
    }

    public void saveLoginDetails(int reportID, String component, String username, String password, String additionalInfo, LocalDate createdDate, LocalTime createdTime, int userID) throws SQLException {
        reportDAO.saveLoginDetails(reportID, component, username, password, additionalInfo, createdDate, createdTime, userID);
    }

    public void noLoginInfoForThisReport(int reportID, LocalDate createdDate, LocalTime createdTime, int userID) throws SQLException {
        reportDAO.noLoginInfoForThisReport(reportID,createdDate,createdTime,userID);
    }

    public List<LoginDetails> getLoginDetails(int reportID) throws SQLException {
        return reportDAO.getLoginDetails(reportID);
    }

    public void deleteLoginDetails(int loginDetailsID) throws SQLException {
        reportDAO.deleteLoginDetails(loginDetailsID);
    }

    public void updateLoginDetails(int loginDetailsID, String component, String username, String password, String additionalInfo, LocalDate createdDate, LocalTime createdTime, int userID) throws SQLException {
        reportDAO.updateLoginDetails(loginDetailsID, component, username, password, additionalInfo, createdDate, createdTime, userID);
    }

    public void updateToNoLogin(int loginDetailsID, LocalDate createdDate, LocalTime createdTime, int userID) throws SQLException {
        reportDAO.updateToNoLogin(loginDetailsID,createdDate,createdTime,userID);
    }
}
