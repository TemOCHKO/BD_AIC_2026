package org.aic.UI.Controllers;

import org.aic.DBModels.CheckDBModel;
import org.aic.Services.Check.ICheckService;
import org.aic.UI.Views.CheckView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckController {

    private final CheckView    view;
    private final ICheckService service;

    // товари в поточному чеку: {upc, name, qty, price}
    private final List<Object[]> items = new ArrayList<>();

    private double discountPct = 0.0;

    public CheckController(Frame owner, ICheckService service) {
        this.service = service;
        this.view    = new CheckView(owner);

        view.getAddProductButton().addActionListener(e -> handleAddProduct());
        view.getIssueButton().addActionListener(e -> handleIssue());
    }

    // ── Додати товар у чек ────────────────────────────────────────
    private void handleAddProduct() {
        String upc = view.getUPC();
        if (upc.isEmpty()) {
            error("Введіть UPC товару"); return;
        }

        // Запитуємо кількість
        String qtyStr = JOptionPane.showInputDialog(view, "Кількість:", "Додати товар", JOptionPane.PLAIN_MESSAGE);
        if (qtyStr == null) return;

        int qty;
        try {
            qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error("Кількість має бути цілим додатнім числом"); return;
        }

        // TODO: отримати назву і ціну з ProductService за UPC
        // Поки що — заглушка, підстав свій ProductService
        String name  = "Товар " + upc; // замінити на реальне
        double price = 0.0;            // замінити на реальне

        items.add(new Object[]{upc, name, qty, price});
        view.addRow(upc, name, qty, price);
        recalculate();
    }

    // ── Перерахувати підсумок ─────────────────────────────────────
    private void recalculate() {
        double subtotal = items.stream()
                .mapToDouble(r -> (int) r[2] * (double) r[3])
                .sum();

        // знижка з карти клієнта (якщо є)
        // TODO: отримати знижку з CustomerCardService за номером карти
        discountPct = 0.0; // замінити на реальне

        double discount = subtotal * discountPct / 100.0;
        double total    = subtotal - discount;
        double vat      = total * 0.20; // ПДВ 20%

        view.setVAT(vat);
        view.setDiscount(discountPct);
        view.setTotal(total);
    }

    // ── Видати чек ────────────────────────────────────────────────
    private void handleIssue() {
        if (items.isEmpty()) {
            error("Додайте хоча б один товар"); return;
        }

        double subtotal = items.stream()
                .mapToDouble(r -> (int) r[2] * (double) r[3])
                .sum();
        double discount = subtotal * discountPct / 100.0;
        double total    = subtotal - discount;
        double vat      = total * 0.20;

        String checkNumber = "CHK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        CheckDBModel check = new CheckDBModel(
                checkNumber,
                null,          // id касира — передай через конструктор або сесію
                view.getCardNumber().isEmpty() ? null : view.getCardNumber(),
                now,
                total,
                vat
        );

        try {
            service.saveCheck(check);
            JOptionPane.showMessageDialog(view,
                    "Чек " + checkNumber + " видано!\nСума: " + String.format("%.2f грн", total),
                    "Успіх", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Помилка БД: " + ex.getMessage());
        }
    }

    // ── Видалення чека (статично, з таблиці переліку) ─────────────
    public static void deleteCheck(Component parent,
                                   ICheckService service,
                                   String checkNumber) {
        if (checkNumber == null || checkNumber.isBlank()) {
            JOptionPane.showMessageDialog(parent, "Оберіть чек для видалення",
                    "Помилка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(parent,
                "Видалити чек " + checkNumber + "?",
                "Підтвердження", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean deleted = service.deleteCheck(checkNumber);
        if (deleted) {
            JOptionPane.showMessageDialog(parent, "Чек видалено!");
        } else {
            JOptionPane.showMessageDialog(parent, "Чек не знайдено",
                    "Помилка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Помилка", JOptionPane.ERROR_MESSAGE);
    }

    public void show() { view.setVisible(true); }
}