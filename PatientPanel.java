import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PatientPanel extends JPanel {

    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, phoneField, addressField;
    private JSpinner ageSpinner;
    private JComboBox<String> genderCombo;

    public PatientPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(255, 255, 255));

        panel.add(new JLabel("Patient ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageSpinner = new JSpinner(new SpinnerNumberModel(25, 1, 120, 1));
        panel.add(ageSpinner);

        panel.add(new JLabel("Gender:"));
        genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        panel.add(genderCombo);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

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

        String[] columnNames = { "ID", "Name", "Age", "Gender", "Phone", "Address" };
        tableModel = new DefaultTableModel(columnNames, 0);
        patientTable = new JTable(tableModel);
        patientTable.setFont(new Font("Arial", Font.PLAIN, 11));
        patientTable.setRowHeight(25);
        patientTable.getTableHeader().setBackground(new Color(33, 150, 243));
        patientTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addBtn = new JButton("Add Patient");
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setBackground(new Color(76, 175, 80));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> addPatient());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 12));
        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> refreshTable());

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        deleteBtn.setBackground(new Color(244, 67, 54));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deletePatient());

        panel.add(addBtn);
        panel.add(refreshBtn);
        panel.add(deleteBtn);

        return panel;
    }

    private void addPatient() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int age = (Integer) ageSpinner.getValue();
            String gender = (String) genderCombo.getSelectedItem();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (!ValidationUtils.isValidId(id)) {
                ValidationUtils.showError(this, "Patient ID must be a positive integer");
                return;
            }
            if (HospitalSystem.patients.stream().anyMatch(p -> p.getId().equals(id))) {
                ValidationUtils.showError(this, "Patient ID already exists");
                return;
            }
            if (!ValidationUtils.isValidName(name)) {
                ValidationUtils.showError(this, "Patient name must contain only letters and spaces");
                return;
            }
            if (!ValidationUtils.isValidPhone(phone)) {
                ValidationUtils.showError(this, "Phone number must be exactly 11 digits");
                return;
            }
            if (!ValidationUtils.isNonEmpty(address)) {
                ValidationUtils.showError(this, "Address is required");
                return;
            }

            Patient patient = new Patient(id, name, age, gender, phone, address);
            HospitalSystem.patients.add(patient);

            clearFields();
            refreshTable();

            JOptionPane.showMessageDialog(this, "Patient added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        HospitalSystem.patients.removeIf(p -> p.getId().equals(id));

        refreshTable();
        JOptionPane.showMessageDialog(this, "Patient deleted successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Patient patient : HospitalSystem.patients) {
            tableModel.addRow(new Object[] {
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getPhone(),
                    patient.getAddress()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        addressField.setText("");
        ageSpinner.setValue(25);
        genderCombo.setSelectedIndex(0);
    }
}
