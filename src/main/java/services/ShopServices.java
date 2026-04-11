package services;

import DAO.EmplDao;
import models.Employee;

import java.sql.SQLException;

public class ShopServices {
    private EmplDao emplDao;

    public ShopServices(EmplDao emplDao) {
        this.emplDao = emplDao;
    }

    /**
     * ПРАЦІВНИКИ
     */
    public void addEmployee(Employee emp) throws SQLException {

    }
}
