package org.aic.Services.Check;

import org.aic.DBModels.CheckDBModel;
import org.aic.Repositories.Check.ICheckRepository;

import java.sql.SQLException;
import java.util.List;

public class CheckService implements ICheckService {

    private final ICheckRepository repository;
    public CheckService(ICheckRepository repository) {
        this.repository = repository;
    }
    @Override
    public List<CheckDBModel> getAllChecks() {
        try {
            return repository.getAllChecks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteCheck(String checkNumber) {
        try {
            return repository.deleteCheck(checkNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
