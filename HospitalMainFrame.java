import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HospitalMainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel dashboardPanel;

    public HospitalMainFrame() {
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        createMenuBar();

        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));

        dashboardPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Dashboard", dashboardPanel);
        tabbedPane.addTab("Patients", new PatientPanel());
        tabbedPane.addTab("Doctors", new DoctorPanel());
        tabbedPane.addTab("Appointments", new AppointmentPanel());
        tabbedPane.addTab("Medicines", new MedicinePanel());
        tabbedPane.addTab("Services", new ServicePanel());
        tabbedPane.addTab("Billing", new BillingPanel());

        // Add listener to refresh dashboard when tab is selected
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                refreshDashboard();
            }
        });

        add(tabbedPane);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(33, 150, 243));
        menuBar.setForeground(Color.WHITE);

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void refreshDashboard() {
        dashboardPanel.removeAll();
        dashboardPanel.add(createDashboardPanelContent(), BorderLayout.CENTER);
        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }

    private JPanel createDashboardPanelContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(0, 100));
        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        statsPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        statsPanel.setBackground(new Color(240, 240, 240));

        statsPanel.add(createStatCard("Total Patients", HospitalSystem.patients.size(), new Color(76, 175, 80)));
        statsPanel.add(createStatCard("Total Doctors", HospitalSystem.doctors.size(), new Color(255, 152, 0)));
        statsPanel
                .add(createStatCard("Total Appointments", HospitalSystem.appointments.size(), new Color(156, 39, 176)));
        statsPanel.add(createStatCard("Total Medicines", HospitalSystem.medicines.size(), new Color(244, 67, 54)));
        statsPanel.add(createStatCard("Total Services", HospitalSystem.services.size(), new Color(63, 81, 181)));

        // Calculate total billing amount from all bills
        double totalBilling = 0;
        for (Billing bill : HospitalSystem.bills) {
            totalBilling += bill.getTotal();
        }
        statsPanel.add(createMoneyStatCard("Total Billing", totalBilling, new Color(76, 175, 80)));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(0, 100));
        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        statsPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        statsPanel.setBackground(new Color(240, 240, 240));

        statsPanel.add(createStatCard("Total Patients", HospitalSystem.patients.size(), new Color(76, 175, 80)));
        statsPanel.add(createStatCard("Total Doctors", HospitalSystem.doctors.size(), new Color(255, 152, 0)));
        statsPanel
                .add(createStatCard("Total Appointments", HospitalSystem.appointments.size(), new Color(156, 39, 176)));
        statsPanel.add(createStatCard("Total Medicines", HospitalSystem.medicines.size(), new Color(244, 67, 54)));
        statsPanel.add(createStatCard("Total Services", HospitalSystem.services.size(), new Color(63, 81, 181)));

        // Calculate total billing amount from all bills
        double totalBilling = 0;
        for (Billing bill : HospitalSystem.bills) {
            totalBilling += bill.getTotal();
        }
        statsPanel.add(createMoneyStatCard("Total Billing", totalBilling, new Color(76, 175, 80)));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMoneyStatCard(String label, double value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(color, 2));

        JLabel valueLabel = new JLabel(String.format("$%.2f", value));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 40));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        labelLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(labelLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createStatCard(String label, int value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(color, 2));

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 48));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        labelLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(labelLabel, BorderLayout.SOUTH);

        return card;
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Hospital Management System v1.0\n\n" +
                        "A complete solution for managing hospital operations.\n\n" +
                        "© 2025 Hospital Management",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
