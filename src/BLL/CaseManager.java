package BLL;

import BE.Case;
import BE.Technician;
import BE.User;
import DAL.CaseDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CaseManager {
    private CaseDAO caseDAO;

    public CaseManager() {
        caseDAO = new CaseDAO();
    }

    public List<Case> getCasesForThisCustomer(int customerID) throws SQLException {
        return caseDAO.getCasesForThisCustomer(customerID);
    }

    public Case getChosenCase(int chosenCase) throws SQLException {
        return caseDAO.getChosenCase(chosenCase);
    }

    public void createNewCase(String caseName, String caseContact, String caseDescription, int customerID) throws SQLException {
        caseDAO.createNewCase(caseName, caseContact, caseDescription, customerID);
    }

    public void addTechnicianToCase(int caseID, List<Technician> chosenTechnicians) throws SQLException {
        caseDAO.addTechnicianToCase(caseID, chosenTechnicians);
    }

    public List<Case> getAllCases() throws SQLException {
        return caseDAO.getAllCases();
    }

    public void updateCase(int caseID, String caseName, String contactPerson, String caseDescription) throws SQLException {
        caseDAO.updateCase(caseID, caseName, contactPerson, caseDescription);
    }

    public List<Technician> getAssignedTechnicians(int caseID) throws SQLException {
        return caseDAO.getAssignedTechnicians(caseID);
    }

    public void deleteCase(Case casen) throws SQLException {
        caseDAO.deleteCase(casen);
    }

    public void closeCase(Case chosenCase) throws SQLException {
        caseDAO.closeCase(chosenCase);
    }

    public void expandKeepingTime(Case casen, int daysToKeep) throws SQLException {
        caseDAO.expandKeepingTime(casen, daysToKeep);
    }

    public void storeUserCaseLink(int userID, int caseID) throws SQLException {
        caseDAO.storeUserCaseLink(userID, caseID);
    }

    public List<Case> getUsersActiveCases(int userID) throws SQLException {
        return caseDAO.getUsersActiveCases(userID);
    }
}