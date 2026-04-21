package org.aic.UI.Controllers;

import org.aic.DBModels.CheckDBModel;
import org.aic.Services.Check.ICheckService;
import org.aic.UI.Views.CheckDialog;
import org.aic.UI.Views.ManagerFrame;

import javax.swing.*;

public class CheckController {

    private final ManagerController parent;
    private final ICheckService checkService;
    private CheckDialog dialog;

    public CheckController(ManagerController parent, ICheckService checkService) {
        this.parent = parent;
        this.checkService = checkService;
    }

    public void showEditDialog(CheckDBModel checkToEdit) {
        dialog = new CheckDialog(parent.getManagerView(), "Редагування чека");
        dialog.setData(checkToEdit);

        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());
        dialog.getBtnSave().addActionListener(e -> handleSave(checkToEdit));

        dialog.setVisible(true);
    }

    private void handleSave(CheckDBModel check) {
        try {
            String newCardNumber = dialog.getCardNumber();

            // update the model. if not empty then its new
            check.setCard_number(newCardNumber.isEmpty() ? null : newCardNumber);

            // TODO: Викличте метод вашого сервісу для оновлення в базі:
            checkService.updateCheck(check);

            dialog.dispose();
            parent.handleTabSwitch(ManagerFrame.TAB_RECEIPTS); // Оновлюємо таблицю
            JOptionPane.showMessageDialog(parent.getManagerView(), "Чек успішно оновлено!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Помилка збереження: " + ex.getMessage());
        }
    }
}