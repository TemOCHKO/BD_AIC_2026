package org.aic.UI.Controllers;

import org.aic.DBModels.CheckDBModel;
import org.aic.Services.Check.ICheckService;
import org.aic.Services.CustomerCard.ICustomerCardService;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.UI.Views.CashierAddCheckDialog;
import org.aic.UI.Views.CashierAddCheckDialog;
import org.aic.UI.Controllers.ComboItem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddCheckController {

    private final CashierAddCheckDialog dialog;
    private final ICheckService checkService;
    private final IEmployeeService employeeService;
    private final ICustomerCardService customerCardService;

    public AddCheckController(JFrame parentFrame, ICheckService checkService,
                              IEmployeeService employeeService, ICustomerCardService customerCardService) {
        this.checkService = checkService;
        this.employeeService = employeeService;
        this.customerCardService = customerCardService;

        this.dialog = new CashierAddCheckDialog(parentFrame);
        initController();
    }

    public void show() {
        dialog.setVisible(true);
    }

    private void initController() {
        populateDropdowns();
        setupVatCalculator();

        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());
        dialog.getBtnSave().addActionListener(e -> handleSave());
    }

    private void populateDropdowns() {

        // get cashiers
        var employees = employeeService.getOnlyCashiers();
        for (var emp : employees) {
            String fullName = emp.getEmpl_surname() + " " + emp.getEmpl_name();
            dialog.getCbCashier().addItem(new ComboItem(emp.getId_employee(), fullName));
        }

        // download clients
        dialog.getCbClientCard().addItem(new ComboItem(null, "--- Без картки клієнта ---"));

        var clients = customerCardService.getAllCustomerCards();
        for (var client : clients) {
            String display = client.getCust_surname() + " (" + client.getPhone_number() + ")";
            dialog.getCbClientCard().addItem(new ComboItem(client.getCard_number(), display));
        }
    }

    private void setupVatCalculator() {

        // add automatic action listner for price
        dialog.getTxtSumTotal().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateVat(); }
            public void removeUpdate(DocumentEvent e) { calculateVat(); }
            public void changedUpdate(DocumentEvent e) { calculateVat(); }

            private void calculateVat() {
                try {
                    String text = dialog.getTxtSumTotal().getText().trim();
                    if (!text.isEmpty()) {
                        double sum = Double.parseDouble(text);
                        double vat = sum * 0.20; // 20% ПДВ
                        dialog.getTxtVat().setText(String.format("%.2f", vat)); // Форматуємо до 2 знаків після коми
                    } else {
                        dialog.getTxtVat().setText("");
                    }
                } catch (NumberFormatException ex) {
                    dialog.getTxtVat().setText("Помилка");
                }
            }
        });
    }

    private void handleSave() {
        try {
            //  get ID of cashier
            ComboItem selectedCashier = (ComboItem) dialog.getCbCashier().getSelectedItem();
            if (selectedCashier == null) {
                JOptionPane.showMessageDialog(dialog, "Оберіть касира!");
                return;
            }
            String idEmployee = selectedCashier.getId();

            // get id card of client
            ComboItem selectedCard = (ComboItem) dialog.getCbClientCard().getSelectedItem();
            String idCard = (selectedCard != null) ? selectedCard.getId() : null;

            double sumTotal = Double.parseDouble(dialog.getTxtSumTotal().getText().trim().replace(",", "."));
            double vat = sumTotal * 0.20;

            // generate receipt num
            String checkNumber = generateCheckNumber();

            // todays date
            String printDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            //
            CheckDBModel newCheck = new CheckDBModel(
                    checkNumber,
                    idEmployee,
                    idCard,
                    printDate,
                    sumTotal,
                    vat
            );

            // save to db
            checkService.saveCheck(newCheck);

            JOptionPane.showMessageDialog(dialog, "Чек успішно збережено!");
            dialog.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Введіть коректну числову суму!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Помилка при збереженні: " + ex.getMessage());
        }
    }

    private String generateCheckNumber() {
        StringBuilder sb = new StringBuilder("CH");
        for (int i = 0; i < 8; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}