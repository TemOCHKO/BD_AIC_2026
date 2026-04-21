package org.aic.Services.CustomerCard;

import org.aic.DBModels.CustomerCardDBModel;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerCardService {
    List<CustomerCardDBModel> getAllCustomerCards();
    void addCustomerCard(CustomerCardDBModel c);
    boolean deleteCustomerCard(String cardNumber);
    List<CustomerCardDBModel> getCustomerBySurname(String surname);
    List<CustomerCardDBModel> getCustomersByPercent(int percent);
    boolean updateCustomerCard(CustomerCardDBModel c);
    CustomerCardDBModel getCustomerByCardNumber(String cardNumber);
    String generateCardNumber();

}
