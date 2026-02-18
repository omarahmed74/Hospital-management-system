import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppointmentPanel extends JPanel {

    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> patientCombo, doctorCombo;
    private JTextField dateTimeField;

    public AppointmentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(255, 255, 255));

        panel.add(new JLabel("Select Patient:"));
        patientCombo = new JComboBox<>();
        panel.add(patientCombo);

        panel.add(new JLabel("Select Doctor:"));
        doctorCombo = new JComboBox<>();
        panel.add(doctorCombo);

        panel.add(new JLabel("Date & Time (YYYY-MM-DD HH:MM):"));
        dateTimeField = new JTextField();
        panel.add(dateTimeField);

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

        String[] columnNames = { "Appointment ID", "Patient", "Doctor", "Date & Time", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentTable = new JTable(tableModel);
        appointmentTable.setFont(new Font("Arial", Font.PLAIN, 11));
        appointmentTable.setRowHeight(25);
        appointmentTable.getTableHeader().setBackground(new Color(156, 39, 176));
        appointmentTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addBtn = new JButton("Create Appointment");
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setBackground(new Color(76, 175, 80));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> createAppointment());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 12));
        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> refreshTable());

        JButton cancelBtn = new JButton("Cancel Appointment");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setBackground(new Color(244, 67, 54));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> cancelAppointment());

        panel.add(addBtn);
        panel.add(refreshBtn);
        panel.add(cancelBtn);

        return panel;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            updateCombos();
            refreshTable();
        }
    }

    private void updateCombos() {
        patientCombo.removeAllItems();
        for (Patient p : HospitalSystem.patients) {
            patientCombo.addItem(p.getId() + " - " + p.getName());
        }

        doctorCombo.removeAllItems();
        for (Doctor d : HospitalSystem.doctors) {
            doctorCombo.addItem(d.getId() + " - " + d.getName());
        }
    }

    private void createAppointment() {
        try {
            if (patientCombo.getItemCount() == 0 || doctorCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please add patients and doctors first!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String patientSelection = (String) patientCombo.getSelectedItem();
            String doctorSelection = (String) doctorCombo.getSelectedItem();
            String dateTime = dateTimeField.getText().trim();

            if (!ValidationUtils.isNonEmpty(dateTime)) {
                ValidationUtils.showError(this, "Please enter date and time");
                return;
            }
            if (!ValidationUtils.isValidDateTime(dateTime)) {
                ValidationUtils.showError(this, "Date & Time must be in YYYY-MM-DD HH:MM format");
                return;
            }

            String patientId = patientSelection.split(" - ")[0];
            String doctorId = doctorSelection.split(" - ")[0];

            Patient patient = HospitalSystem.patients.stream()
                    .filter(p -> p.getId().equals(patientId))
                    .findFirst()
                    .orElse(null);

            Doctor doctor = HospitalSystem.doctors.stream()
                    .filter(d -> d.getId().equals(doctorId))
                    .findFirst()
                    .orElse(null);

            if (patient == null || doctor == null) {
                JOptionPane.showMessageDialog(this, "Invalid patient or doctor!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Appointment appointment = new Appointment(patient, doctor, dateTime);
            HospitalSystem.appointments.add(appointment);
            doctor.addAppointment(appointment);

            dateTimeField.setText("");
            refreshTable();

            JOptionPane.showMessageDialog(this, "Appointment created successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Appointment appt = HospitalSystem.appointments.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (appt != null) {
            appt.cancel();
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment cancelled successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Appointment appt : HospitalSystem.appointments) {
            tableModel.addRow(new Object[] {
                    appt.getId(),
                    appt.getPatient().getName(),
                    appt.getDoctor().getName(),
                    appt.getDateTime(),
                    appt.getStatus()
            });
        }
    }
}
