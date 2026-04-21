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

    public AddCustomerCardController(Frame owner, ICustomerCardService service) {
        this(owner, service, Mode.ADD, null);
    }

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
        // Рекомендую додавати .trim() щоб випадково не зберегти пробіли замість тексту
        String surname    = view.getSurname().trim();
        String name       = view.getName().trim();
        String patronymic = view.getPatronymic().trim();
        String cardNumber = view.getCardNumber(); // Це поле ми перезапишемо нижче
        String phone      = view.getPhone().trim();
        String city       = view.getCity().trim();
        String street     = view.getStreet().trim();
        String zip        = view.getZip().trim();
        String discountStr = view.getDiscount().trim();

        // ── 1. Перевірки на обов'язкові поля ────────────────────────────────
        if (surname.isEmpty() || name.isEmpty()) {
            error("Прізвище та ім'я є обов'язковими");
            return;
        }
        if (patronymic.isEmpty()) {
            error("По батькові обов'язкове");
            return; // ВИПРАВЛЕНО: Додано return
        }
        if (city.isEmpty() || street.isEmpty() || zip.isEmpty()) {
            error("Адресна інформація обов'язкова");
            return; // ВИПРАВЛЕНО: Додано return
        }

        // ── 2. Перевірки на довжину (згідно з БД) ───────────────────────────
        if (surname.length() > 50) {
            error("Прізвище не може перевищувати 50 символів");
            return;
        }
        if (name.length() > 50) {
            error("Ім'я не може перевищувати 50 символів");
            return;
        }
        if (patronymic.length() > 50) {
            error("По батькові не може перевищувати 50 символів");
            return;
        }
        if (city.length() > 50) {
            error("Місто не може перевищувати 50 символів");
            return;
        }
        if (street.length() > 50) {
            error("Вулиця не може перевищувати 50 символів");
            return;
        }
        if (zip.length() > 9) {
            error("Індекс не може перевищувати 9 символів");
            return;
        }
        if (phone.length() > 13) {
            error("Телефон не може перевищувати 13 символів");
            return;
        }

        // ── 3. Перевірка формату телефону та знижки ─────────────────────────
        if (!phone.matches("\\+?[0-9\\-\\s]{7,13}")) {
            error("Введіть коректний номер телефону");
            return;
        }

        int discount;
        try {
            discount = Integer.parseInt(discountStr);
            if (discount < 0 || discount > 100) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error("Знижка має бути числом від 0 до 100");
            return;
        }

        // ── 4. Формування номера картки та збереження ───────────────────────
        if (mode == Mode.EDIT) {
            cardNumber = original.getCard_number();
        } else {
            // Якщо генератор видає більше ніж 13 символів, БД видасть помилку.
            // Переконайтеся, що service.generateCardNumber() генерує <= 13 символів.
            cardNumber = service.generateCardNumber();
        }

        CustomerCardDBModel card = new CustomerCardDBModel(
                cardNumber, name, surname, patronymic, phone, city, street, zip, discount
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