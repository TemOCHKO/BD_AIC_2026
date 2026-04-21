package org.aic.Services.Check;

import org.aic.DBModels.CheckDBModel;

import java.sql.SQLException;
import java.util.List;

public interface ICheckService {
    List<CheckDBModel> getAllChecks();
    boolean deleteCheck(String checkNumber);
    void saveCheck(CheckDBModel check);
}
