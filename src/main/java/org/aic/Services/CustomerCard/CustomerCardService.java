package org.aic.Services.CustomerCard;

import org.aic.DBModels.CustomerCardDBModel;
import org.aic.Repositories.CustomerCard.ICustomerCardRepository;

import java.sql.SQLException;
import java.util.List;

public class CustomerCardService implements ICustomerCardService{

    private final ICustomerCardRepository customerCardRepository;
    public CustomerCardService(ICustomerCardRepository customerCardRepository) {
        this.customerCardRepository = customerCardRepository;
    }
    @Override
    public List<CustomerCardDBModel> getAllCustomerCards() {
        try {
            return customerCardRepository.getAllCustomerCards();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addCustomerCard(CustomerCardDBModel c) {

    }

    @Override
    public boolean deleteCustomerCard(String cardNumber) {
        try {
            return customerCardRepository.deleteCustomerCard(cardNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerCardDBModel> getCustomerBySurname(String surname){
        try {
            return customerCardRepository.getCustomerBySurname(surname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerCardDBModel> getCustomersByPercent(int percent) {
        try {
            return customerCardRepository.getCustomersByPercent(percent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomerCard(CustomerCardDBModel c) {
        try {
            return customerCardRepository.updateCustomerCard(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
