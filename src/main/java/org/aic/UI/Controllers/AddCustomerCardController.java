package org.aic.UI.Controllers;

import org.aic.DBModels.CustomerCardDBModel;
import org.aic.Services.CustomerCard.ICustomerCardService;
import org.aic.UI.Views.AddCustCardView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddCustomerCardController {

    public enum Mode { ADD, EDIT }

    private final AddCustCardView      view;
    private final ICustomerCardService service;
    private final Mode                 mode;
    private       CustomerCardDBModel  original; // для редагування

    // ── Додавання ─────────────────────────────────────────────────
    public AddCustomerCardController(Frame owner, ICustomerCardService service) {
        this(owner, service, Mode.ADD, null);
    }

    // ── Редагування ───────────────────────────────────────────────
    public AddCustomerCardController(Frame owner, ICustomerCardService service,
                                 CustomerCardDBModel card) {
        this(owner, service, Mode.EDIT, card);
    }

    private AddCustomerCardController(Frame owner, ICustomerCardService service,
                                  Mode mode, CustomerCardDBModel card) {
        this.service  = service;
        this.mode     = mode;
        this.original = card;
        this.view     = new AddCustCardView(owner);

        if (mode == Mode.EDIT) {
            view.setTitle("Редагувати карту клієнта");
            view.fillFields(card);
            view.getSaveButton().setText("Оновити");
        }

        view.getSaveButton().addActionListener(e -> handleSave());
        view.getCancelButton().addActionListener(e -> view.dispose());
    }

    private void handleSave() {
        // ── Валідація ─────────────────────────────────────────────
        String surname    = view.getSurname();
        String name       = view.getName();
        String patronymic = view.getPatronymic();
        String cardNumber = view.getCardNumber();
        String phone      = view.getPhone();
        String city =       view.getCity();
        String street =   view.getStreet();
        String zip =       view.getZip();
        String discountStr = view.getDiscount();

        if (surname.isEmpty() || name.isEmpty()) {
            error("Прізвище та ім'я є обов'язковими");
            return;
        }
        if (patronymic.isEmpty()) {
            error("По батькові обов'язкове");
        }
        if (city.isEmpty() || street.isEmpty() || zip.isEmpty()) {
            error("Адресна інформація обов'язкова");
        }

        if (!phone.matches("\\+?[0-9\\-\\s]{7,13}")) {
            error("Введіть коректний номер телефону");
            return;
        }

        int discount;
        try {
            discount = Integer.parseInt(discountStr);
            if (discount < 0 || discount > 100) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error("Знижка має бути числом від 0 до 100"); return;
        }

        if (mode == Mode.EDIT) {
            cardNumber = original.getCard_number();
        } else {
            cardNumber = service.generateCardNumber();
        }

        /*
        (String card_number, String cust_name, String cust_surname,
                         String cust_patronymic, String phone_number, String city,
                         String street, String zip_code, int percent
         */

       CustomerCardDBModel card = new CustomerCardDBModel(
                cardNumber, name, surname, patronymic,phone, city, street, zip, discount
        );


        try {
            if (mode == Mode.ADD) {
                service.addCustomerCard(card);
                success("Карту клієнта збережено!");
            } else {
                service.updateCustomerCard(card);
                success("Карту клієнта оновлено!");
            }
            view.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Помилка БД: " + ex.getMessage());
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Помилка", JOptionPane.ERROR_MESSAGE);
    }

    private void success(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Успіх", JOptionPane.INFORMATION_MESSAGE);
    }

    public void show() { view.setVisible(true); }
}