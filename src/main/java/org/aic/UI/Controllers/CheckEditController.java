package org.aic.UI.Controllers;

import org.aic.DBModels.CheckDBModel;
import org.aic.Services.Check.ICheckService;
import org.aic.Services.CustomerCard.ICustomerCardService;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.UI.Views.CheckEditDialog;
import org.aic.UI.Controllers.ComboItem;
import org.aic.UI.Views.ManagerFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CheckEditController {

    private final ManagerController parentController;
    private final CheckEditDialog dialog;
    private final ICheckService checkService;
    private final IEmployeeService employeeService;
    private final ICustomerCardService customerCardService;

    private CheckDBModel currentCheck;

    public CheckEditController(ManagerController parentController,
                               ICheckService checkService,
                               IEmployeeService employeeService,
                               ICustomerCardService customerCardService) {
        this.parentController = parentController;
        this.checkService = checkService;
        this.employeeService = employeeService;
        this.customerCardService = customerCardService;

        this.dialog = new CheckEditDialog(parentController.getManagerView());
    }

    public void showEditDialog(CheckDBModel checkToEdit) {
        this.currentCheck = checkToEdit;

        populateDropdowns();

        dialog.getTxtCheckNumber().setText(currentCheck.getCheck_number());
        dialog.getTxtSumTotal().setText(String.valueOf(currentCheck.getSum_total()));
        dialog.getTxtVat().setText(String.valueOf(currentCheck.getVat()));

        selectComboItemById(dialog.getCbCashier(), currentCheck.getId_employee());

        selectComboItemById(dialog.getCbClientCard(), currentCheck.getCard_number());

        setupVatCalculator();
        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());
        dialog.getBtnSave().addActionListener(e -> handleSave());

        dialog.setVisible(true);
    }

    private void populateDropdowns() {
        dialog.getCbCashier().removeAllItems();
        dialog.getCbClientCard().removeAllItems();

        // get cashiers
        var cashiers = employeeService.getOnlyCashiers();
        for (var cashier : cashiers) {
            String fullName = cashier.getEmpl_surname() + " " + cashier.getEmpl_name();
            dialog.getCbCashier().addItem(new ComboItem(cashier.getId_employee(), fullName));
        }

        // gte clients
        dialog.getCbClientCard().addItem(new ComboItem(null, "--- Без картки клієнта ---"));
        var clients = customerCardService.getAllCustomerCards();
        for (var client : clients) {
            String display = client.getCust_surname() + " (" + client.getPhone_number() + ")";
            dialog.getCbClientCard().addItem(new ComboItem(client.getCard_number(), display));
        }
    }

    private void selectComboItemById(JComboBox<ComboItem> comboBox, String idToFind) {
        if (idToFind == null) {
            comboBox.setSelectedIndex(0);
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            ComboItem item = comboBox.getItemAt(i);
            if (item.getId() != null && item.getId().equals(idToFind)) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void setupVatCalculator() {
        dialog.getTxtSumTotal().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateVat(); }
            public void removeUpdate(DocumentEvent e) { calculateVat(); }
            public void changedUpdate(DocumentEvent e) { calculateVat(); }

            private void calculateVat() {
                try {
                    String text = dialog.getTxtSumTotal().getText().trim().replace(",", ".");
                    if (!text.isEmpty()) {
                        double sum = Double.parseDouble(text);
                        double vat = sum * 0.20;
                        dialog.getTxtVat().setText(String.format("%.2f", vat).replace(",", "."));
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
            // id of cashier
            ComboItem selectedCashier = (ComboItem) dialog.getCbCashier().getSelectedItem();
            if (selectedCashier == null) {
                JOptionPane.showMessageDialog(dialog, "Оберіть касира!");
                return;
            }

            // id of customer card
            ComboItem selectedCard = (ComboItem) dialog.getCbClientCard().getSelectedItem();
            String idCard = (selectedCard != null && selectedCard.getId() != null) ? selectedCard.getId() : null;

            double newSum = Double.parseDouble(dialog.getTxtSumTotal().getText().trim().replace(",", "."));
            double newVat = newSum * 0.20;

            // Оновлюємо поточний об'єкт
            currentCheck.setId_employee(selectedCashier.getId());
            currentCheck.setCard_number(idCard);
            currentCheck.setSum_total(newSum);
            currentCheck.setVat(newVat);

            // TODO: Збереження в базу даних!
            checkService.updateCheck(currentCheck);

            JOptionPane.showMessageDialog(dialog, "Чек успішно оновлено!");
            dialog.dispose();

            parentController.handleTabSwitch(ManagerFrame.TAB_RECEIPTS);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Введіть коректну числову суму!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Помилка при оновленні: " + ex.getMessage());
        }
    }
}