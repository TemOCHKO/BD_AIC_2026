package org.aic.Repositories.Check;

import org.aic.DBModels.CheckDBModel;

import java.sql.SQLException;
import java.util.List;

public interface ICheckRepository {
    void addCheck(CheckDBModel check) throws SQLException;
    List<CheckDBModel> getAllChecks() throws SQLException;
    List<CheckDBModel> getChecksForLastThreeYears() throws SQLException;
    boolean deleteCheck(String checkNumber) throws SQLException;
    List<CheckDBModel> getChecksByEmployeeAndPeriod(String idEmployee, String dateFrom, String dateTo) throws SQLException;
    CheckDBModel getCheckByNumber(String checkNumber) throws SQLException;
    List<CheckDBModel> getChecksByEmployeeToday(String idEmployee) throws SQLException;
    List<CheckDBModel> getChecksByPeriod(String dateFrom, String dateTo) throws SQLException;
}
