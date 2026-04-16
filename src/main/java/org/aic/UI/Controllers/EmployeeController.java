package org.aic.UI.Controllers;
import com.github.lgooddatepicker.components.DatePicker;
import org.aic.DBModels.EmployeeDBModel;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.UI.Views.Employee.AddEmployeeView;
import org.aic.UI.Views.Employee.EmployeesListView;

import javax.swing.*;

public class EmployeeController {

    private final EmployeesListView listView;
    private final AddEmployeeView addEmployeeView;
    private final IEmployeeService employeeService;

    public EmployeeController(IEmployeeService employeeService, EmployeesListView listView, AddEmployeeView addEmployeeView) {
        this.employeeService = employeeService;
        this.listView = listView;
        this.addEmployeeView = addEmployeeView;
        initController();
        prepareToShow(listView);
    }

    private void initController() {
        listView.getLoadDataButton().addActionListener(e -> loadData());
        listView.getCreateNewEmployeeButton().addActionListener(e -> goToAddNewEmployeeView());
        //listView.getTableModel().addTableModelListener(e -> );
        addEmployeeView.getCancelButton().addActionListener(e -> goBack());
        addEmployeeView.getSaveButton().addActionListener(e -> saveNewEmployee());
    }

    private void goToAddNewEmployeeView() {
        //view.setVisible(false);
        prepareToShow(addEmployeeView);
    }

    private void prepareToShow(JFrame view) {
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    private void goBack() {
        cancelShowing(addEmployeeView);
    }

    private void cancelShowing(JFrame view) {
        view.setVisible(false);
        view = null;
    }

    private void loadData() {
        listView.displayEmployees(employeeService.getAllListEmployeesDTO());
    }
    private void saveNewEmployee() {
        if (addEmployeeView.getSurnameField().getText().isEmpty() || addEmployeeView.getSurnameField().getText() == null) {
            showInputErrorMessage("Please enter a surname");
            return;
        } else if (addEmployeeView.getNameField().getText().isEmpty() || addEmployeeView.getNameField().getText() == null) {
            showInputErrorMessage("Please enter a name");
            return;
        } else if (addEmployeeView.getPatronymicField().getText().isEmpty() || addEmployeeView.getPatronymicField().getText() == null) {
            showInputErrorMessage("Please enter a patronymic");
            return;
        } else if (addEmployeeView.getRoleField().getText().isEmpty() || addEmployeeView.getRoleField().getText() == null) {
            showInputErrorMessage("Please enter a role");
            return;
        } else if (addEmployeeView.getSalaryField().getText().isEmpty() || addEmployeeView.getSalaryField().getText() == null) {
            showInputErrorMessage("Please enter a salary");
            return;
        } else if (addEmployeeView.getDobField().getText().isEmpty() || addEmployeeView.getDobField().getText() == null) {
            showInputErrorMessage("Please enter a date of birth");
            return;
        } else if (addEmployeeView.getDosField().getText().isEmpty() || addEmployeeView.getDosField().getText() == null) {
            showInputErrorMessage("Please enter a date of start");
            return;
        } else if (addEmployeeView.getPhoneField().getText().isEmpty() || addEmployeeView.getPhoneField().getText() == null) {
            showInputErrorMessage("Please enter a phone number");
            return;
        } else if (addEmployeeView.getCityField().getText().isEmpty() || addEmployeeView.getCityField().getText() == null) {
            showInputErrorMessage("Please enter a city");
            return;
        } else if (addEmployeeView.getStreetField().getText().isEmpty() || addEmployeeView.getStreetField().getText() == null) {
            showInputErrorMessage("Please enter a street");
            return;
        } else if (addEmployeeView.getZipField().getText().isEmpty() || addEmployeeView.getZipField().getText() == null) {
            showInputErrorMessage("Please enter a zip code");
            return;
        }

        double salary;
        try {
            salary = Integer.parseInt(addEmployeeView.getSalaryField().getText().trim());
        } catch (NumberFormatException e) {
            showInputErrorMessage("Please enter a valid salary");
            return;
        }


        employeeService.saveNewEmployee(new EmployeeDBModel(addEmployeeView.getSurnameField().getText(),
                addEmployeeView.getNameField().getText().trim(),
                addEmployeeView.getPatronymicField().getText().trim(),
                addEmployeeView.getRoleField().getText().trim(),
                salary,
                convertToDBDateString(addEmployeeView.getDobField()),
                convertToDBDateString(addEmployeeView.getDosField()),
                addEmployeeView.getPhoneField().getText().trim(),
                addEmployeeView.getCityField().getText().trim(),
                addEmployeeView.getStreetField().getText().trim(),
                addEmployeeView.getZipField().getText().trim()
                ));

        loadData();
        goBack();
    }

    private void showInputErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(addEmployeeView,
                 errorMessage,
                "Input Error",
                 JOptionPane.ERROR_MESSAGE);
    }

    private String convertToDBDateString(DatePicker picker) {
        StringBuilder sb = new StringBuilder();
        sb.append(picker.getDate().getYear()).append("-");
        sb.append(picker.getDate().getMonthValue()).append("-");
        sb.append(picker.getDate().getDayOfMonth());

        return sb.toString().trim();
    }

}
