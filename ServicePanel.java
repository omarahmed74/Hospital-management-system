import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServicePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField;
    private JSpinner priceSpinner;

    public ServicePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel p = new JPanel(new GridLayout(1, 4, 10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        p.add(new JLabel("Service Name:"));
        nameField = new JTextField();
        p.add(nameField);

        p.add(new JLabel("Price:"));
        priceSpinner = new JSpinner(new SpinnerNumberModel(50.0, 0.0, 100000.0, 1.0));
        p.add(priceSpinner);

        return p;
    }

    private JScrollPane createTablePanel() {
        String[] cols = { "Name", "Price" };
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(24);
        return new JScrollPane(table);
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p.setBackground(new Color(240, 240, 240));

        JButton add = new JButton("Add Service");
        add.addActionListener(e -> addService());

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refresh());

        JButton delete = new JButton("Delete Selected");
        delete.addActionListener(e -> deleteSelected());

        p.add(add);
        p.add(refresh);
        p.add(delete);
        return p;
    }

    private void addService() {
        String name = nameField.getText().trim();
        double price = ((Number) priceSpinner.getValue()).doubleValue();
        if (!ValidationUtils.isValidName(name)) {
            ValidationUtils.showError(this, "Service name must contain only letters and spaces");
            return;
        }
        if (price < 0.0) {
            ValidationUtils.showError(this, "Service price cannot be negative");
            return;
        }
        if (HospitalSystem.services.stream().anyMatch(s -> s.getName().equalsIgnoreCase(name))) {
            ValidationUtils.showError(this, "Service with this name already exists");
            return;
        }
        Service s = new Service(name, price);
        HospitalSystem.services.add(s);
        nameField.setText("");
        priceSpinner.setValue(50.0);
        refresh();
    }

    private void refresh() {
        model.setRowCount(0);
        for (Service s : HospitalSystem.services) {
            model.addRow(new Object[] { s.getName(), String.format("$%.2f", s.getPrice()) });
        }
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select a service");
            return;
        }
        String name = (String) model.getValueAt(r, 0);
        HospitalSystem.services.removeIf(s -> s.getName().equals(name));
        refresh();
    }
}
