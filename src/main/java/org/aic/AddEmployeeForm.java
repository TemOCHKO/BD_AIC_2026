package org.aic;

import DAO.EmplDao;
import models.Employee;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * приклад додавання працівників
 * (підключено до UI)
 */
public class AddEmployeeForm extends JDialog {

    private EmplDao emplDao;

    // Поля вводу
    private JTextField idField        = new JTextField();
    private JTextField surnameField   = new JTextField();
    private JTextField nameField      = new JTextField();
    private JTextField patronymicField= new JTextField();
    private JComboBox<String> roleBox = new JComboBox<>(new String[]{"Manager", "Cashier"});
    private JTextField salaryField    = new JTextField();
    private JTextField birthField     = new JTextField("рррр-мм-дд");
    private JTextField startField     = new JTextField("рррр-мм-дд");
    private JTextField phoneField     = new JTextField();
    private JTextField cityField      = new JTextField();
    private JTextField streetField    = new JTextField();
    private JTextField zipField       = new JTextField();

    public AddEmployeeForm(JFrame parent, EmplDao emplDao) {
        super(parent, "Додати працівника", true);
        this.emplDao = emplDao;

        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Панель з полями
        JPanel fieldsPanel = new JPanel(new GridLayout(12, 2, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fieldsPanel.add(new JLabel("ID працівника:"));
        fieldsPanel.add(idField);

        fieldsPanel.add(new JLabel("Прізвище:"));
        fieldsPanel.add(surnameField);

        fieldsPanel.add(new JLabel("Ім'я:"));
        fieldsPanel.add(nameField);

        fieldsPanel.add(new JLabel("По батькові:"));
        fieldsPanel.add(patronymicField);

        fieldsPanel.add(new JLabel("Посада:"));
        fieldsPanel.add(roleBox);

        fieldsPanel.add(new JLabel("Зарплата:"));
        fieldsPanel.add(salaryField);

        fieldsPanel.add(new JLabel("Дата народження:"));
        fieldsPanel.add(birthField);

        fieldsPanel.add(new JLabel("Дата початку роботи:"));
        fieldsPanel.add(startField);

        fieldsPanel.add(new JLabel("Телефон:"));
        fieldsPanel.add(phoneField);

        fieldsPanel.add(new JLabel("Місто:"));
        fieldsPanel.add(cityField);

        fieldsPanel.add(new JLabel("Вулиця:"));
        fieldsPanel.add(streetField);

        fieldsPanel.add(new JLabel("Індекс:"));
        fieldsPanel.add(zipField);

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveBtn   = new JButton("Зберегти");
        JButton cancelBtn = new JButton("Скасувати");

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Кнопка Зберегти
        saveBtn.addActionListener(e -> {
            try {
                // Перевірка що поля не пусті
                if (idField.getText().isEmpty() || surnameField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Заповніть всі обов'язкові поля!",
                            "Помилка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Перевірка віку (18+)
                // TODO: додати перевірку віку

                Employee emp = new Employee(
                        idField.getText(),
                        surnameField.getText(),
                        nameField.getText(),
                        patronymicField.getText(),
                        (String) roleBox.getSelectedItem(),
                        Double.parseDouble(salaryField.getText()),
                        birthField.getText(),
                        startField.getText(),
                        phoneField.getText(),
                        cityField.getText(),
                        streetField.getText(),
                        zipField.getText()
                );

                emplDao.addEmployee(emp);
                JOptionPane.showMessageDialog(this, "Працівника додано! ✅");
                dispose(); // закрити форму

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Зарплата має бути числом!",
                        "Помилка", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Помилка бази даних: " + ex.getMessage(),
                        "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Кнопка Скасувати
        cancelBtn.addActionListener(e -> dispose());
    }
}