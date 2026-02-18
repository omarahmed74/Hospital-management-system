import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DoctorPanel extends JPanel {

    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, specField;
    private JSpinner ageSpinner;
    private JComboBox<String> genderCombo;

    public DoctorPanel() {
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
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(255, 255, 255));

        panel.add(new JLabel("Doctor ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageSpinner = new JSpinner(new SpinnerNumberModel(30, 20, 80, 1));
        panel.add(ageSpinner);

        panel.add(new JLabel("Gender:"));
        genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        panel.add(genderCombo);

        panel.add(new JLabel("Specialization:"));
        specField = new JTextField();
        panel.add(specField);

        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.setBackground(new Color(240, 240, 240));

        String[] columnNames = { "ID", "Name", "Age", "Gender", "Specialization" };
        tableModel = new DefaultTableModel(columnNames, 0);
        doctorTable = new JTable(tableModel);
        doctorTable.setFont(new Font("Arial", Font.PLAIN, 11));
        doctorTable.setRowHeight(25);
        doctorTable.getTableHeader().setBackground(new Color(255, 152, 0));
        doctorTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addBtn = new JButton("Add Doctor");
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setBackground(new Color(76, 175, 80));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> addDoctor());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 12));
        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> refreshTable());

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        deleteBtn.setBackground(new Color(244, 67, 54));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteDoctor());

        JButton saveBtn = new JButton("Save to File");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 12));
        saveBtn.setBackground(new Color(156, 39, 176));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> FileIOUtils.saveDoctors());

        JButton loadBtn = new JButton("Load from File");
        loadBtn.setFont(new Font("Arial", Font.BOLD, 12));
        loadBtn.setBackground(new Color(156, 39, 176));
        loadBtn.setForeground(Color.WHITE);
        loadBtn.addActionListener(e -> {
            FileIOUtils.loadDoctors();
            refreshTable();
        });

        panel.add(addBtn);
        panel.add(refreshBtn);
        panel.add(deleteBtn);
        panel.add(saveBtn);
        panel.add(loadBtn);

        return panel;
    }

    private void addDoctor() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int age = (Integer) ageSpinner.getValue();
            String gender = (String) genderCombo.getSelectedItem();
            String specialization = specField.getText().trim();
            if (!ValidationUtils.isValidId(id)) {
                ValidationUtils.showError(this, "Doctor ID must be a positive integer");
                return;
            }
            if (HospitalSystem.doctors.stream().anyMatch(d -> d.getId().equals(id))) {
                ValidationUtils.showError(this, "Doctor ID already exists");
                return;
            }
            if (!ValidationUtils.isValidName(name)) {
                ValidationUtils.showError(this, "Doctor name must contain only letters and spaces");
                return;
            }
            if (!ValidationUtils.isNonEmpty(specialization)) {
                ValidationUtils.showError(this, "Specialization is required");
                return;
            }
            Doctor doctor = new Doctor(id, name, age, gender, specialization);
            HospitalSystem.doctors.add(doctor);

            clearFields();
            refreshTable();

            JOptionPane.showMessageDialog(this, "Doctor added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        HospitalSystem.doctors.removeIf(d -> d.getId().equals(id));

        refreshTable();
        JOptionPane.showMessageDialog(this, "Doctor deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Doctor doctor : HospitalSystem.doctors) {
            tableModel.addRow(new Object[] {
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getAge(),
                    doctor.getGender(),
                    doctor.getSpecialization()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        specField.setText("");
        ageSpinner.setValue(30);
        genderCombo.setSelectedIndex(0);
    }
}
