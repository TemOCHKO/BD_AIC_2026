package org.aic.Services.CustomerCard;

import org.aic.DBModels.CustomerCardDBModel;
import org.aic.Repositories.CustomerCard.ICustomerCardRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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
        try {
            customerCardRepository.addCustomerCard(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public CustomerCardDBModel getCustomerByCardNumber(String cardNumber) {
        try {
            return customerCardRepository.getCustomerByCardNumber(cardNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateCardNumber() {
        return "CRD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 5);
    }
}
