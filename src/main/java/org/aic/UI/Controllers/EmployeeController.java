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

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
        this.listView = new EmployeesListView();
        this.addEmployeeView = new AddEmployeeView();
        this.editEmployeeView = new EditEmployeeView();

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
        addEmployeeView.getCancelButton().addActionListener(e -> goBack(addEmployeeView));
        addEmployeeView.getSaveButton().addActionListener(e -> saveNewEmployee());
        editEmployeeView.getCancelButton().addActionListener(e -> goBack(editEmployeeView));
        editEmployeeView.getUpdateButton().addActionListener(e -> updateEmployee());
    }

    private void updateEmployee() {
        employeeService.updateEmployee(new ArrayList<>());
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
        goBack(addEmployeeView);
    }

    private void validateDataInFields() throws IllegalArgumentException {
        
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

    private LocalDate convertFromDBDateString(String dbDateString) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        String[] dates = dbDateString.split("-");
        return LocalDate.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
    }

    private void fetchData(int rowNumber) {
        EmployeeDBModel employeeDBModel = employeeService.getEmployeeBySurname(listView.getEmployeeTable().getValueAt(rowNumber, 1).toString().trim());

        LocalDate dob;
        try {
            dob = convertFromDBDateString(employeeDBModel.getDate_of_birth());
        } catch (Exception e) {
            dob = LocalDate.now();
        }

        LocalDate dos;
        try {
            dos = convertFromDBDateString(employeeDBModel.getDate_of_start());
        } catch (Exception e) {
            dos = LocalDate.now();
        }

        editEmployeeView.setEmployeeData(employeeDBModel.getEmpl_surname(), employeeDBModel.getEmpl_name(), employeeDBModel.getEmpl_patronymic()
                , employeeDBModel.getEmpl_role(), employeeDBModel.getSalary() + "", dob, dos, employeeDBModel.getPhone_number()
                , employeeDBModel.getCity(), employeeDBModel.getStreet(), employeeDBModel.getZip_code());
    }

}
