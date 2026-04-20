package org.aic.UI.Controllers;

import com.github.lgooddatepicker.components.DatePicker;
import org.aic.DBModels.EmployeeDBModel;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.UI.Views.AddEmpl;

import javax.swing.*;
import java.time.LocalDate;

public class EmployeesController {

    private final AddEmpl addEmployeeView;
    private final IEmployeeService employeeService;
    public EmployeesController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
        addEmployeeView = new AddEmpl(null);
        initControllers();
    }

    public void showAddEmployeeDialog() {
        addEmployeeView.setLocationRelativeTo(null);
        addEmployeeView.setVisible(true);
    }

    public void initControllers() {
        addEmployeeView.getSaveButton().addActionListener(e -> saveEmployee());
        addEmployeeView.getCancelButton().addActionListener(e -> addEmployeeView.dispose());
    }

    private void saveEmployee() {
        if (addEmployeeView.getSurnameField().getText().isEmpty() || addEmployeeView.getSurnameField().getText() == null) {
            showInputErrorMessage("Please enter a surname");
            return;
        } else if (addEmployeeView.getNameField().getText().isEmpty() || addEmployeeView.getNameField().getText() == null) {
            showInputErrorMessage("Please enter a name");
            return;
        } else if (addEmployeeView.getPatronymicField().getText().isEmpty() || addEmployeeView.getPatronymicField().getText() == null) {
            showInputErrorMessage("Please enter a patronymic");
            return;
        } else if (addEmployeeView.getRoleField().getSelectedItem() == null || addEmployeeView.getRoleField().getSelectedItem().toString().trim().isEmpty()) {
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
            salary = Double.parseDouble(addEmployeeView.getSalaryField().getText().trim());
        } catch (NumberFormatException e) {
            showInputErrorMessage("Please enter a valid salary");
            return;
        }

        if (isIllegalStringLength(addEmployeeView.getSurnameField().getText(), 50)) {
            showInputErrorMessage("Surname cant be bigger than " + 50 + " characters");
            return;
        } else if (isIllegalStringLength(addEmployeeView.getNameField().getText(), 50)) {
            showInputErrorMessage("Name cant be bigger than " + 50 + " characters");
            return;
        } else if (isIllegalStringLength(addEmployeeView.getPatronymicField().getText(), 50)) {
            showInputErrorMessage("Patronymic cant be bigger than " + 50 + " characters");
            return;
        } else if (isIllegalStringLength(addEmployeeView.getRoleField().getSelectedItem().toString(), 10)) {
            showInputErrorMessage("Name cant be bigger than " + 10 + " characters");
            return;
        } else if (addEmployeeView.getDobField().getDate().isAfter(LocalDate.now().minusYears(18))) {
            showInputErrorMessage("Employee cant be younger than " + 18 + " years old");
            return;
        } else if (!addEmployeeView.getDosField().getDate().isAfter(addEmployeeView.getDobField().getDate())) {
            showInputErrorMessage("Employee cant have started working before being born");
            return;
        } else if (isIllegalStringLength(addEmployeeView.getPhoneField().getText(), 13)) {
            showInputErrorMessage("Phone number cant be bigger than " + 13 + " characters");
            return;
        } else if (isIllegalStringLength(addEmployeeView.getCityField().getText(), 50)) {
            showInputErrorMessage("City cant be bigger than " + 50 + " characters");
            return;
        } else if (isIllegalStringLength(addEmployeeView.getStreetField().getText(), 50)) {
            showInputErrorMessage("Street cant be bigger than " + 50 + " characters");
            return;
        }  else if (isIllegalStringLength(addEmployeeView.getZipField().getText(), 9)) {
            showInputErrorMessage("Zip Code cant be bigger than " + 9 + " characters");
            return;
        }

        employeeService.saveNewEmployee(new EmployeeDBModel(addEmployeeView.getSurnameField().getText(),
                addEmployeeView.getNameField().getText().trim(),
                addEmployeeView.getPatronymicField().getText().trim(),
                addEmployeeView.getRoleField().getSelectedItem().toString().trim(),
                salary,
                convertToDBDateString(addEmployeeView.getDobField()),
                convertToDBDateString(addEmployeeView.getDosField()),
                addEmployeeView.getPhoneField().getText().trim(),
                addEmployeeView.getCityField().getText().trim(),
                addEmployeeView.getStreetField().getText().trim(),
                addEmployeeView.getZipField().getText().trim()
        ));

        setEverytingToDefaultAndExit();
        //loadData();
        //goBack(addEmployeeView);
    }

    private void setEverytingToDefaultAndExit() {
        addEmployeeView.dispose();
    }

    public AddEmpl getAddEmployeeView() {
        return addEmployeeView;
    }

    private void showInputErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(addEmployeeView,
                errorMessage,
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(addEmployeeView,
                message,
                "",
                JOptionPane.PLAIN_MESSAGE);
    }

    private boolean isIllegalStringLength(String s, int maxLength) {
        s = s.trim();
        if (s.length() > maxLength) {
            return true;
        }
        return false;
    }

    private String convertToDBDateString(DatePicker picker) {
        StringBuilder sb = new StringBuilder();
        sb.append(picker.getDate().getYear()).append("-");
        sb.append(picker.getDate().getMonthValue()).append("-");
        sb.append(picker.getDate().getDayOfMonth());

        return sb.toString().trim();
    }
}
