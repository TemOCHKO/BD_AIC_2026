package org.aic.UI.Controllers;
import com.github.lgooddatepicker.components.DatePicker;
import org.aic.DBModels.EmployeeDBModel;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.UI.Views.Employee.AddEmployeeView;
import org.aic.UI.Views.Employee.EditEmployeeView;
import org.aic.UI.Views.Employee.EmployeesListView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeController {

    private final EmployeesListView listView;
    private final AddEmployeeView addEmployeeView;
    private final EditEmployeeView editEmployeeView;
    private final IEmployeeService employeeService;
    private EmployeeDBModel currentEmployee;

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
        this.listView = new EmployeesListView();
        this.addEmployeeView = new AddEmployeeView();
        this.editEmployeeView = new EditEmployeeView();
        currentEmployee = null;

        initController();
        prepareToShow(listView);
    }

    private void initController() {
        listView.getLoadDataButton().addActionListener(e -> loadData());
        listView.getCreateNewEmployeeButton().addActionListener(e -> goToAddNewEmployeeView());
        listView.getEmployeeTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = listView.getEmployeeTable().getSelectedRow();

                if (selectedRow >= 0) {
                    if (e.getClickCount() >= 2) {
                        fetchData(selectedRow);
                        prepareToShow(editEmployeeView);
                    }
                }
            }
        });
        listView.getDeleteEmployee().addActionListener(e -> deleteEmployee());
        addEmployeeView.getCancelButton().addActionListener(e -> goBack(addEmployeeView));
        addEmployeeView.getSaveButton().addActionListener(e -> saveNewEmployee());
        editEmployeeView.getCancelButton().addActionListener(e -> goBack(editEmployeeView));
        editEmployeeView.getUpdateButton().addActionListener(e -> updateEmployee());
    }

    private void deleteEmployee() {
        int selectedRow = listView.getEmployeeTable().getSelectedRow();

        if (selectedRow < 0) {
            showInputErrorMessage("Please select which employee to delete");
            return;
        }

        employeeService.deleteEmployee(listView.getEmployeeTable().getValueAt(selectedRow, -1).toString().trim());
        showMessage("Successfully deleted " + listView.getEmployeeTable().getValueAt(selectedRow, 1));

        loadData();
    }

    private void updateEmployee() {
        if (editEmployeeView.getSurnameField().getText().isEmpty() || editEmployeeView.getSurnameField().getText() == null) {
            showInputErrorMessage("Please enter a surname");
            return;
        } else if (editEmployeeView.getNameField().getText().isEmpty() || editEmployeeView.getNameField().getText() == null) {
            showInputErrorMessage("Please enter a name");
            return;
        } else if (editEmployeeView.getPatronymicField().getText().isEmpty() || editEmployeeView.getPatronymicField().getText() == null) {
            showInputErrorMessage("Please enter a patronymic");
            return;
        } else if (editEmployeeView.getRoleField().getSelectedItem() == null || editEmployeeView.getRoleField().getSelectedItem().toString().trim().isEmpty()) {
            showInputErrorMessage("Please enter a role");
            return;
        } else if (editEmployeeView.getSalaryField().getText().isEmpty() || editEmployeeView.getSalaryField().getText() == null) {
            showInputErrorMessage("Please enter a salary");
            return;
        } else if (editEmployeeView.getDobField().getText().isEmpty() || editEmployeeView.getDobField().getText() == null) {
            showInputErrorMessage("Please enter a date of birth");
            return;
        } else if (editEmployeeView.getDosField().getText().isEmpty() || editEmployeeView.getDosField().getText() == null) {
            showInputErrorMessage("Please enter a date of start");
            return;
        } else if (editEmployeeView.getPhoneField().getText().isEmpty() || editEmployeeView.getPhoneField().getText() == null) {
            showInputErrorMessage("Please enter a phone number");
            return;
        } else if (editEmployeeView.getCityField().getText().isEmpty() || editEmployeeView.getCityField().getText() == null) {
            showInputErrorMessage("Please enter a city");
            return;
        } else if (editEmployeeView.getStreetField().getText().isEmpty() || editEmployeeView.getStreetField().getText() == null) {
            showInputErrorMessage("Please enter a street");
            return;
        } else if (editEmployeeView.getZipField().getText().isEmpty() || editEmployeeView.getZipField().getText() == null) {
            showInputErrorMessage("Please enter a zip code");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(editEmployeeView.getSalaryField().getText().trim());
        } catch (NumberFormatException e) {
            showInputErrorMessage("Please enter a valid salary");
            return;
        }

        if (isIllegalStringLength(editEmployeeView.getSurnameField().getText(), 50)) {
            showInputErrorMessage("Surname cant be bigger than " + 50 + " characters");
            return;
        } else if (isIllegalStringLength(editEmployeeView.getNameField().getText(), 50)) {
            showInputErrorMessage("Name cant be bigger than " + 50 + " characters");
            return;
        } else if (isIllegalStringLength(editEmployeeView.getPatronymicField().getText(), 50)) {
            showInputErrorMessage("Patronymic cant be bigger than " + 50 + " characters");
            return;
        } else if (isIllegalStringLength(editEmployeeView.getRoleField().getSelectedItem().toString(), 10)) {
            showInputErrorMessage("Name cant be bigger than " + 10 + " characters");
            return;
        } else if (editEmployeeView.getDobField().getDate().isAfter(LocalDate.now().minusYears(18))) {
            showInputErrorMessage("Employee cant be younger than " + 18 + " years old");
            return;
        } else if (!editEmployeeView.getDosField().getDate().isAfter(addEmployeeView.getDobField().getDate())) {
            showInputErrorMessage("Employee cant have started working before being born");
            return;
        } else if (isIllegalStringLength(editEmployeeView.getPhoneField().getText(), 13)) {
            showInputErrorMessage("Phone number cant be bigger than " + 13 + " characters");
            return;
        } else if (isIllegalStringLength(editEmployeeView.getCityField().getText(), 50)) {
            showInputErrorMessage("City cant be bigger than " + 50 + " characters");
            return;
        } else if (isIllegalStringLength(editEmployeeView.getStreetField().getText(), 50)) {
            showInputErrorMessage("Street cant be bigger than " + 50 + " characters");
            return;
        }  else if (isIllegalStringLength(editEmployeeView.getZipField().getText(), 9)) {
            showInputErrorMessage("Zip Code cant be bigger than " + 9 + " characters");
            return;
        }

        employeeService.updateEmployee(new EmployeeDBModel(currentEmployee.getId_employee(), editEmployeeView.getSurnameField().getText(),
                editEmployeeView.getNameField().getText().trim(),
                editEmployeeView.getPatronymicField().getText().trim(),
                editEmployeeView.getRoleField().getSelectedItem().toString().trim(),
                salary,
                convertToDBDateString(editEmployeeView.getDobField()),
                convertToDBDateString(editEmployeeView.getDosField()),
                editEmployeeView.getPhoneField().getText().trim(),
                editEmployeeView.getCityField().getText().trim(),
                editEmployeeView.getStreetField().getText().trim(),
                editEmployeeView.getZipField().getText().trim()
        ));

        currentEmployee = null;
        loadData();
        goBack(editEmployeeView);
    }

    private void goToAddNewEmployeeView() {
        //view.setVisible(false);
        prepareToShow(addEmployeeView);
    }

    private void prepareToShow(JFrame view) {
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    private void goBack(JFrame view) {
        cancelShowing(view);
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

        loadData();
        goBack(addEmployeeView);
    }

    private boolean isIllegalStringLength(String s, int maxLength) {
        s = s.trim();
        if (s.length() > maxLength) {
            return true;
        }
        return false;
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

    private String convertToDBDateString(DatePicker picker) {
        StringBuilder sb = new StringBuilder();
        sb.append(picker.getDate().getYear()).append("-");
        sb.append(picker.getDate().getMonthValue()).append("-");
        sb.append(picker.getDate().getDayOfMonth());

        return sb.toString().trim();
    }

    private LocalDate convertFromDBDateString(String dbDateString) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        String[] dates = dbDateString.split("-");
        return LocalDate.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
    }

    private void fetchData(int rowNumber) {
        currentEmployee = employeeService.getEmployeeBySurname(listView.getEmployeeTable().getValueAt(rowNumber, 1).toString().trim());

        LocalDate dob;
        try {
            dob = convertFromDBDateString(currentEmployee.getDate_of_birth());
        } catch (Exception e) {
            dob = LocalDate.now();
        }

        LocalDate dos;
        try {
            dos = convertFromDBDateString(currentEmployee.getDate_of_start());
        } catch (Exception e) {
            dos = LocalDate.now();
        }

        editEmployeeView.setEmployeeData(currentEmployee.getEmpl_surname(), currentEmployee.getEmpl_name(), currentEmployee.getEmpl_patronymic()
                , currentEmployee.getEmpl_role(), currentEmployee.getSalary() + "", dob, dos, currentEmployee.getPhone_number()
                , currentEmployee.getCity(), currentEmployee.getStreet(), currentEmployee.getZip_code());
    }

}
