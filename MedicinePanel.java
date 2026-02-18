import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MedicinePanel extends JPanel {

    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, expiryField;
    private JSpinner quantitySpinner, priceSpinner;

    public MedicinePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            refreshTable();
        }
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 5, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(255, 255, 255));

        panel.add(new JLabel("Medicine ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
        panel.add(quantitySpinner);

        panel.add(new JLabel("Price/Unit:"));
        priceSpinner = new JSpinner(new SpinnerNumberModel(10.0, 0.1, 10000.0, 0.5));
        panel.add(priceSpinner);

        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        expiryField = new JTextField();
        panel.add(expiryField);

        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.setBackground(new Color(240, 240, 240));

        String[] columnNames = { "ID", "Name", "Quantity", "Price/Unit", "Total Price", "Expiry Date" };
        tableModel = new DefaultTableModel(columnNames, 0);
        medicineTable = new JTable(tableModel);
        medicineTable.setFont(new Font("Arial", Font.PLAIN, 11));
        medicineTable.setRowHeight(25);
        medicineTable.getTableHeader().setBackground(new Color(244, 67, 54));
        medicineTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(medicineTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addBtn = new JButton("Add Medicine");
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setBackground(new Color(76, 175, 80));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> addMedicine());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 12));
        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> refreshTable());

        JButton issueBtn = new JButton("Issue Medicine");
        issueBtn.setFont(new Font("Arial", Font.BOLD, 12));
        issueBtn.setBackground(new Color(255, 152, 0));
        issueBtn.setForeground(Color.WHITE);
        issueBtn.addActionListener(e -> issueMedicine());

        JButton restockBtn = new JButton("Restock");
        restockBtn.setFont(new Font("Arial", Font.BOLD, 12));
        restockBtn.setBackground(new Color(76, 175, 80));
        restockBtn.setForeground(Color.WHITE);
        restockBtn.addActionListener(e -> restockMedicine());

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        deleteBtn.setBackground(new Color(244, 67, 54));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteMedicine());

        JButton saveBtn = new JButton("Save to File");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 12));
        saveBtn.setBackground(new Color(156, 39, 176));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> FileIOUtils.saveMedicines());

        JButton loadBtn = new JButton("Load from File");
        loadBtn.setFont(new Font("Arial", Font.BOLD, 12));
        loadBtn.setBackground(new Color(156, 39, 176));
        loadBtn.setForeground(Color.WHITE);
        loadBtn.addActionListener(e -> {
            FileIOUtils.loadMedicines();
            refreshTable();
        });

        panel.add(addBtn);
        panel.add(refreshBtn);
        panel.add(issueBtn);
        panel.add(restockBtn);
        panel.add(deleteBtn);
        panel.add(saveBtn);
        panel.add(loadBtn);

        return panel;
    }

    private void addMedicine() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int quantity = (Integer) quantitySpinner.getValue();
            double price = (Double) priceSpinner.getValue();
            String expiry = expiryField.getText().trim();

            if (!ValidationUtils.isValidId(id)) {
                ValidationUtils.showError(this, "Medicine ID must be a positive integer");
                return;
            }
            if (HospitalSystem.medicines.stream().anyMatch(m -> m.getId().equals(id))) {
                ValidationUtils.showError(this, "Medicine ID already exists");
                return;
            }
            if (!ValidationUtils.isValidName(name)) {
                ValidationUtils.showError(this, "Medicine name must contain only letters and spaces");
                return;
            }
            if (quantity <= 0) {
                ValidationUtils.showError(this, "Quantity must be greater than zero");
                return;
            }
            if (price <= 0.0) {
                ValidationUtils.showError(this, "Price must be greater than zero");
                return;
            }
            if (!ValidationUtils.isValidDate(expiry)) {
                ValidationUtils.showError(this, "Expiry date must be in YYYY-MM-DD format");
                return;
            }

            Medicine medicine = new Medicine(id, name, quantity, price, expiry);
            HospitalSystem.medicines.add(medicine);

            clearFields();
            refreshTable();

            JOptionPane.showMessageDialog(this, "Medicine added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void issueMedicine() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Medicine medicine = HospitalSystem.medicines.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (medicine == null)
            return;

        String input = JOptionPane.showInputDialog(this, "Enter quantity to issue:", "Issue Medicine",
                JOptionPane.PLAIN_MESSAGE);
        if (input == null)
            return;

        try {
            int amount = Integer.parseInt(input);
            if (medicine.issue(amount)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Medicine issued successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Not enough stock!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restockMedicine() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Medicine medicine = HospitalSystem.medicines.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (medicine == null)
            return;

        String input = JOptionPane.showInputDialog(this, "Enter quantity to add:", "Restock Medicine",
                JOptionPane.PLAIN_MESSAGE);
        if (input == null)
            return;

        try {
            int amount = Integer.parseInt(input);
            medicine.restock(amount);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Medicine restocked successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMedicine() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to delete!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        HospitalSystem.medicines.removeIf(m -> m.getId().equals(id));

        refreshTable();
        JOptionPane.showMessageDialog(this, "Medicine deleted successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Medicine medicine : HospitalSystem.medicines) {
            tableModel.addRow(new Object[] {
                    medicine.getId(),
                    medicine.getName(),
                    medicine.getQuantity(),
                    String.format("$%.2f", medicine.getPrice()),
                    String.format("$%.2f", medicine.getTotalPrice()),
                    medicine.getExpiryDate()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        expiryField.setText("");
        quantitySpinner.setValue(10);
        priceSpinner.setValue(10.0);
    }
}
