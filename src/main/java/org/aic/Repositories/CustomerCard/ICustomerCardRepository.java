package org.aic.Repositories.CustomerCard;

import org.aic.DBModels.CustomerCardDBModel;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerCardRepository {
    void addCustomerCard(CustomerCardDBModel c) throws SQLException;
    List<CustomerCardDBModel> getAllCustomerCards() throws SQLException;
    boolean deleteCustomerCard(String cardNumber) throws SQLException;
    List<CustomerCardDBModel> getCustomerBySurname(String surname) throws SQLException;
    CustomerCardDBModel getCustomerByCardNumber(String cardNumber) throws SQLException;
    List<CustomerCardDBModel> getCustomersByPercent(int percent) throws SQLException;
    boolean updateCustomerCard(CustomerCardDBModel c) throws SQLException;
}
