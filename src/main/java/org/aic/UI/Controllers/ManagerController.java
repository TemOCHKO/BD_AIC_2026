package org.aic.UI.Controllers;

import org.aic.UI.Views.ManagerFrame;

import javax.swing.*;
import java.awt.*;

public class ManagerController {

    private final ManagerFrame managerView;
    public ManagerController(ManagerFrame managerFrame) {
        this.managerView = managerFrame;
        initController();
    }

    private void switchTab(int idx) {
        managerView.setActiveTab(idx);
        managerView.getTabBar().repaint();

        // Swap filter panel
        managerView.getContentCenter().remove(managerView.getFilterPanel());
        managerView.setFilterPanel(managerView.buildFilterPanel(idx));
        managerView.getContentCenter().add(managerView.getFilterPanel(), BorderLayout.NORTH);
        managerView.getContentCenter().revalidate();
        managerView.getContentCenter().repaint();

        // Reset table columns
        managerView.getTableModel().setColumnIdentifiers(ManagerFrame.getColumns()[idx]);
        managerView.getTableModel().setRowCount(0);
        managerView.styleTable();

        // Hide "Додати" on Receipts tab (тільки касир створює чеки)
        managerView.getAddButton().setVisible(idx != ManagerFrame.TAB_RECEIPTS);
    }

    private void initController() {
        // Bind action listeners to the tab buttons
        JButton[] tabButtons = managerView.getTabButtons();
        for (int i = 0; i < tabButtons.length; i++) {
            final int tabIndex = i;
            tabButtons[i].addActionListener(e -> handleTabSwitch(tabIndex));
        }

        // Example of where you will bind other UI actions:
        // view.getAddButton().addActionListener(e -> handleAddAction());
        // view.getBtnSortBySurname().addActionListener(e -> handleSortEmployees());
    }

    /**
     * Handles the logic of switching tabs: updating UI state, changing columns,
     * and managing specific component visibility.
     */
    private void handleTabSwitch(int tabIndex) {
        // 1. Tell the view to redraw its specific panels for the new tab
        managerView.updateViewForTab(tabIndex);

        // 2. Update the table's data model columns based on the selected tab
        String[] columns = ManagerFrame.getColumns()[tabIndex];
        managerView.getTableModel().setColumnIdentifiers(columns);
        managerView.getTableModel().setRowCount(0);
        managerView.styleTable();

        // 3. Controller logic: "Add" button is hidden on the Receipts tab
        managerView.getAddButton().setVisible(tabIndex != ManagerFrame.TAB_RECEIPTS);

        // 4. TODO: Fetch data for this tab from your Service/Repository layers
        // Object[][] tabData = storageService.getDataForTab(tabIndex);
        // view.setTableData(columns, tabData);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            // 1. Create the View
            ManagerFrame view = new ManagerFrame();

            // 2. Create the Controller, passing the View as a dependency
            ManagerController controller = new ManagerController(view);

            // 3. Show the View
            view.setVisible(true);
        });
    }
}
