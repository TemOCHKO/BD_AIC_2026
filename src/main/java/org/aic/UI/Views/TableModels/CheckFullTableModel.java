package org.aic.UI.Views.TableModels;

import org.aic.DBModels.CheckDBModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CheckFullTableModel extends AbstractTableModel {
    private final List<CheckDBModel> checks;

    private final String[] columnNames = {
            "Номер чека", "Дата", "ID касира", "Сума", "ПДВ", "Карта клієнта"
    };

    public CheckFullTableModel(List<CheckDBModel> checks) {
        this.checks = checks;
    }

    @Override
    public int getRowCount() {
        return checks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CheckDBModel check = checks.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> check.getCheck_number();
            case 1 -> check.getPrint_date();
            case 2 -> check.getId_employee();

            case 3 -> String.format("%.2f ₴", check.getSum_total());
            case 4 -> String.format("%.2f ₴", check.getVat());

            case 5 -> (check.getCard_number() != null && !check.getCard_number().isEmpty())
                    ? check.getCard_number()
                    : "Немає карти";
            default -> null;
        };
    }

    /**
     * Grabs the full Check object instantly when a row is clicked.
     */
    public CheckDBModel getCheckAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < checks.size()) {
            return checks.get(rowIndex);
        }
        return null;
    }


    public void setChecks(List<CheckDBModel> newChecks) {
        this.checks.clear();
        if (newChecks != null) {
            this.checks.addAll(newChecks);
        }
        fireTableDataChanged();
    }
}
