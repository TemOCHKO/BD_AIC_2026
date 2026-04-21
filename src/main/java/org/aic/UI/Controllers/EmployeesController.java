package org.aic.UI.Controllers;

import com.github.lgooddatepicker.components.DatePicker;
import org.aic.DBModels.EmployeeDBModel;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.UI.Views.AddEmpl;
import org.aic.UI.Views.ManagerFrame;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Random;

public class EmployeesController {

    private final ManagerController manager;
    private final AddEmpl addEmployeeView;
    private final AddEmpl editEmployeeView;
    private final IEmployeeService employeeService;
    public EmployeesController(ManagerController manager, IEmployeeService employeeService) {
        this.manager = manager;
        this.employeeService = employeeService;
        addEmployeeView = new AddEmpl(null, true);
        editEmployeeView = new AddEmpl(null, false);
        editEmployeeView.getSaveButton().setText("Update");
        editEmployeeView.getTitleLabel().setText("Редагувати працівника");
        initControllers();
    }

    public void showAddEmployeeDialog() {
        addEmployeeView.setLocationRelativeTo(null);
        addEmployeeView.setVisible(true);
    }

    public void showEditEmployeeDialog() {
        fetchData();
        editEmployeeView.setLocationRelativeTo(null);
        editEmployeeView.setVisible(true);
    }

    public void initControllers() {
        addEmployeeView.getSaveButton().addActionListener(e -> saveEmployee());
        addEmployeeView.getCancelButton().addActionListener(e -> addEmployeeView.dispose());
        editEmployeeView.getSaveButton().addActionListener(e -> updateEmployee());
        editEmployeeView.getCancelButton().addActionListener(e -> editEmployeeView.dispose());
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

        // ── Додатково валідуємо пароль ────────────────────────────
        if (!addEmployeeView.validatePassword()) return;

        // ── Хешуємо пароль через BCrypt ───────────────────────────
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(
                addEmployeeView.getPassword(),
                org.mindrot.jbcrypt.BCrypt.gensalt(12)
        );

        employeeService.saveNewEmployee(new EmployeeDBModel(generateEmployeeId(), addEmployeeView.getSurnameField().getText(),
                addEmployeeView.getNameField().getText().trim(),
                addEmployeeView.getPatronymicField().getText().trim(),
                addEmployeeView.getRoleField().getSelectedItem().toString().trim(),
                salary,
                convertToDBDateString(addEmployeeView.getDobField()),
                convertToDBDateString(addEmployeeView.getDosField()),
                addEmployeeView.getPhoneField().getText().trim(),
                addEmployeeView.getCityField().getText().trim(),
                addEmployeeView.getStreetField().getText().trim(),
                addEmployeeView.getZipField().getText().trim(),
                hashedPassword
        ));

        showMessage("Successfully added new employee");
        setEverytingToDefaultAndExit();
        //loadData();
        //goBack(addEmployeeView);
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
        } else if (!editEmployeeView.getDosField().getDate().isAfter(editEmployeeView.getDobField().getDate())) {
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
        // ... (початок методу: перевірки на порожні поля Прізвища, Імені тощо) ...

        int selectedRow = manager.getManagerView().getTable().getSelectedRow();
        if (selectedRow == -1) return;

        String employeeId = manager.getManagerView().getTable().getModel().getValueAt(selectedRow, 0).toString().trim();

        // 1. Отримуємо існуючого працівника з бази
        EmployeeDBModel existingEmployee = employeeService.getEmployeeById(employeeId);
        String finalPasswordHash = existingEmployee.getEmpl_password();

        // 2. Зчитуємо новий пароль
        String newPassword = editEmployeeView.getPassword().trim();

        // 🔴 ЛОГІКА: Якщо пароля в базі НЕМАЄ, а користувач нічого не ввів — видаємо помилку
        if ((finalPasswordHash == null || finalPasswordHash.isEmpty()) && newPassword.isEmpty()) {
            showInputErrorMessage("Цьому працівнику обов'язково потрібно встановити пароль!");
            return; // Зупиняємо збереження!
        }

        // 3. Якщо введено новий пароль — валідуємо та хешуємо його
        if (!newPassword.isEmpty()) {
            // Тепер викликаємо валідацію (з підтвердженням)
            if (!editEmployeeView.validatePassword()) {
                return;
            }

            finalPasswordHash = org.mindrot.jbcrypt.BCrypt.hashpw(
                    newPassword,
                    org.mindrot.jbcrypt.BCrypt.gensalt(12)
            );
        }

        employeeService.updateEmployee(new EmployeeDBModel(employeeId, editEmployeeView.getSurnameField().getText(),
                editEmployeeView.getNameField().getText().trim(),
                editEmployeeView.getPatronymicField().getText().trim(),
                editEmployeeView.getRoleField().getSelectedItem().toString().trim(),
                salary,
                convertToDBDateString(editEmployeeView.getDobField()),
                convertToDBDateString(editEmployeeView.getDosField()),
                editEmployeeView.getPhoneField().getText().trim(),
                editEmployeeView.getCityField().getText().trim(),
                editEmployeeView.getStreetField().getText().trim(),
                editEmployeeView.getZipField().getText().trim(),
                finalPasswordHash
        ));

        manager.handleTabSwitch(ManagerFrame.TAB_EMPLOYEES);
        setEverytingToDefaultAndExit();
    }

    private void setEverytingToDefaultAndExit() {
        addEmployeeView.dispose();
        editEmployeeView.dispose();
    }

    public AddEmpl getAddEmployeeView() {
        return addEmployeeView;
    }

    private void fetchData() {
        int selectedRow = manager.getManagerView().getTable().getSelectedRow();
        if (selectedRow == -1) {
            showInputErrorMessage("Please select an employee");
            return;
        }

        EmployeeDBModel dbModel = employeeService.getEmployeeById(manager.getManagerView().getTable().getModel().getValueAt(selectedRow, 0).toString().trim());
        LocalDate dob;
        try {
            dob = convertFromDBDateString(dbModel.getDate_of_birth());
        } catch (Exception e) {
            dob = LocalDate.now();
        }

        LocalDate dos;
        try {
            dos = convertFromDBDateString(dbModel.getDate_of_start());
        } catch (Exception e) {
            dos = LocalDate.now();
        }

        editEmployeeView.setEmployeeData(dbModel.getEmpl_surname(), dbModel.getEmpl_name(),
                dbModel.getEmpl_patronymic(), dbModel.getEmpl_role(),
                dbModel.getSalary() + "", dob, dos,
                dbModel.getPhone_number(), dbModel.getCity(), dbModel.getStreet(), dbModel.getZip_code());
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
                "Success",
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

    private LocalDate convertFromDBDateString(String dbDateString) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        String[] dates = dbDateString.split("-");
        return LocalDate.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
    }


    public String generateEmployeeId() {
        // The characters we want to pull from
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("EMP-");
        Random random = new Random();

        // Loop 4 times to add 4 random characters (change the 4 if you want longer IDs!)
        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
