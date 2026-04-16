package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeeDashboard extends JFrame {

    public EmployeeDashboard() {
        setTitle("Employee Management Dashboard");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Create the Top Header (Logo + User Profile)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Create the Left Sidebar (Navigation Icons)
        add(createSidebarPanel(), BorderLayout.WEST);

        // 3. Create the Main Content Area (Table + Controls)
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 60));

        // Logo
        JLabel logo = new JLabel("LOGO");
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(logo, BorderLayout.WEST);

        // Right side icons (Notifications, Help, Profile)
        JPanel rightIcons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightIcons.setBackground(Color.WHITE);
        rightIcons.add(new JLabel("🔔"));
        rightIcons.add(new JLabel("❓"));
        rightIcons.add(new JLabel("👤"));
        header.add(rightIcons, BorderLayout.EAST);

        // Add a bottom border line
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        return header;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(248, 249, 250)); // Very light gray
        sidebar.setPreferredSize(new Dimension(60, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        // Add dummy navigation icons with spacing
        String[] icons = {"=", "👥", "📈", "🗂", "⚙", "🚪"};

        sidebar.add(Box.createRigidArea(new Dimension(0, 20))); // Top padding

        for (String icon : icons) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(iconLabel);
            sidebar.add(Box.createRigidArea(new Dimension(0, 30))); // Space between icons
        }

        return sidebar;
    }

    private JPanel createMainContentPanel() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 245, 245)); // Slightly darker gray background
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Inner Container with White Background ---
        JPanel innerContainer = new JPanel(new BorderLayout());
        innerContainer.setBackground(Color.WHITE);
        innerContainer.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // A. Top Action Bar (Search + Add Button)
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBackground(Color.WHITE);
        actionBar.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField searchField = new JTextField("🔍 Search", 20);
        searchField.setForeground(Color.GRAY);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(searchField);
        actionBar.add(searchPanel, BorderLayout.WEST);

        JButton addButton = new JButton("Add employee");
        addButton.setBackground(Color.WHITE);
        actionBar.add(addButton, BorderLayout.EAST);

        // Header Title (Employees) placed just below the action bar
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(0, 10, 10, 0));
        JLabel titleLabel = new JLabel("Employees");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBackground(Color.WHITE);
        topArea.add(actionBar, BorderLayout.NORTH);
        topArea.add(titlePanel, BorderLayout.SOUTH);

        innerContainer.add(topArea, BorderLayout.NORTH);

        // B. The Data Table
        String[] columns = {"☐", "Name", "Position", "Location", "Permission", "Report to", "Actions"};
        Object[][] data = {
                {false, "Daniel Beau", "Senior Dev", "Los Angeles", "Partial", "Chris Batista", "✏ 🗑"},
                {true, "Jo Martin", "Front-end Dev", "Los Angeles", "Partial", "Chris Batista", "✏ 🗑"},
                {false, "Vic Al", "Front-end Dev", "Los Angeles", "Partial", "Chris Batista", "✏ 🗑"}
        };

        // Custom Table Model to allow the first column to render as Checkboxes
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));

        // Add a bottom border to the table header to match the design
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default scrollpane border
        scrollPane.getViewport().setBackground(Color.WHITE);

        innerContainer.add(scrollPane, BorderLayout.CENTER);

        // C. Bottom Pagination Area
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        paginationPanel.setBackground(Color.WHITE);
        paginationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paginationPanel.add(new JLabel("Items per page: 10   |   1-10 of 100   |   <   >"));

        innerContainer.add(paginationPanel, BorderLayout.SOUTH);

        mainContent.add(innerContainer, BorderLayout.CENTER);
        return mainContent;
    }

    public static void main(String[] args) {
        // Ensure the UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for a more modern appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EmployeeDashboard().setVisible(true);
        });
    }
}
