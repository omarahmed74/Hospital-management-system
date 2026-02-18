import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillingPanel extends JPanel {
    private JComboBox<String> patientCombo;
    private JTextField billIdField;
    private JSpinner doctorFeeSpinner, visitsSpinner, feePerVisitSpinner, roomRateSpinner, daysSpinner,
            medicineCostSpinner, paymentSpinner;
    private JList<String> servicesList;
    private DefaultTableModel tableModel;
    private JTable billsTable;

    public BillingPanel() {
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
            refreshAll();
        }
    }

    private JPanel createInputPanel() {
        JPanel p = new JPanel(new GridLayout(3, 4, 10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        p.add(new JLabel("Bill ID:"));
        billIdField = new JTextField();
        p.add(billIdField);

        p.add(new JLabel("Patient:"));
        patientCombo = new JComboBox<>();
        p.add(patientCombo);

        p.add(new JLabel("Doctor Fee:"));
        doctorFeeSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100000.0, 10.0));
        p.add(doctorFeeSpinner);

        p.add(new JLabel("Visits:"));
        visitsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        p.add(visitsSpinner);

        p.add(new JLabel("Fee/Visit:"));
        feePerVisitSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 5.0));
        p.add(feePerVisitSpinner);

        p.add(new JLabel("Room Rate:"));
        roomRateSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 10.0));
        p.add(roomRateSpinner);

        p.add(new JLabel("Days Stayed:"));
        daysSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 365, 1));
        p.add(daysSpinner);

        p.add(new JLabel("Medicine Cost:"));
        medicineCostSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100000.0, 1.0));
        p.add(medicineCostSpinner);

        p.add(new JLabel("Payment:"));
        paymentSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100000.0, 1.0));
        p.add(paymentSpinner);

        // services list
        DefaultListModel<String> lm = new DefaultListModel<>();
        for (Service s : HospitalSystem.services)
            lm.addElement(s.getName() + " - $" + s.getPrice());
        servicesList = new JList<>(lm);
        servicesList.setVisibleRowCount(4);
        JScrollPane sp = new JScrollPane(servicesList);
        p.add(new JLabel("Services (select multiple):"));
        p.add(sp);

        return p;
    }

    private JScrollPane createTablePanel() {
        String[] cols = { "Bill ID", "Patient", "Total", "Paid", "Status" };
        tableModel = new DefaultTableModel(cols, 0);
        billsTable = new JTable(tableModel);
        billsTable.setRowHeight(24);
        return new JScrollPane(billsTable);
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p.setBackground(new Color(240, 240, 240));

        JButton create = new JButton("Create Bill");
        create.addActionListener(e -> createBill());

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshAll());

        JButton markPaid = new JButton("Mark as Paid");
        markPaid.setBackground(new Color(76, 175, 80));
        markPaid.setForeground(Color.WHITE);
        markPaid.addActionListener(e -> markAsPaid());

        JButton delete = new JButton("Delete Selected");
        delete.addActionListener(e -> deleteSelected());

        p.add(create);
        p.add(refresh);
        p.add(markPaid);
        p.add(delete);
        return p;
    }

    private void createBill() {
        try {
            String id = billIdField.getText().trim();
            if (!ValidationUtils.isValidId(id)) {
                ValidationUtils.showError(this, "Bill ID must be a positive integer");
                return;
            }
            if (HospitalSystem.bills.stream().anyMatch(b -> b.getBillid().equals(id))) {
                ValidationUtils.showError(this, "Bill ID already exists");
                return;
            }
            String patientSel = (String) patientCombo.getSelectedItem();
            if (patientSel == null) {
                ValidationUtils.showError(this, "Select patient");
                return;
            }
            String patientName = patientSel.split(" - ")[1];

            Billing bill = new Billing(id, patientName);

            // doctor fee info: allow direct set or computed from visits*feePerVisit
            double docFee = ((Number) doctorFeeSpinner.getValue()).doubleValue();
            int visits = (Integer) visitsSpinner.getValue();
            double feePerVisit = ((Number) feePerVisitSpinner.getValue()).doubleValue();
            double roomRate = ((Number) roomRateSpinner.getValue()).doubleValue();
            int days = (Integer) daysSpinner.getValue();
            double medCost = ((Number) medicineCostSpinner.getValue()).doubleValue();

            bill.setVisits(visits);
            bill.setFeePerVisit(feePerVisit);

            // If doctorFee is provided (> 0), use it; otherwise compute from visits *
            // feePerVisit
            if (docFee > 0) {
                bill.setDoctorFee(docFee);
            }

            bill.setRoomRate(roomRate);
            bill.setDaysStayed(days);

            bill.setMedicineCost(medCost);

            // services
            for (String sel : servicesList.getSelectedValuesList()) {
                String sname = sel.split(" - ")[0];
                for (Service s : HospitalSystem.services)
                    if (s.getName().equals(sname))
                        bill.addService(s);
            }

            bill.calculate();
            double payment = ((Number) paymentSpinner.getValue()).doubleValue();
            if (payment < 0) {
                ValidationUtils.showError(this, "Payment cannot be negative");
                return;
            }
            bill.setPayment(payment);
            bill.checkPayment();

            HospitalSystem.bills.add(bill);
            refreshAll();
            JOptionPane.showMessageDialog(this, "Bill created. Total: $" + String.format("%.2f", bill.getTotal()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void refreshAll() {
        // refresh patient combo
        patientCombo.removeAllItems();
        for (Patient p : HospitalSystem.patients)
            patientCombo.addItem(p.getId() + " - " + p.getName());

        // refresh services list
        DefaultListModel<String> lm = new DefaultListModel<>();
        for (Service s : HospitalSystem.services)
            lm.addElement(s.getName() + " - $" + s.getPrice());
        servicesList.setModel(lm);

        // refresh bills table
        tableModel.setRowCount(0);
        for (Billing b : HospitalSystem.bills) {
            tableModel.addRow(new Object[] { b.getBillid(), b.getPatient(), String.format("$%.2f", b.getTotal()),
                    String.format("$%.2f", b.getPayment()), b.isPaid() ? "PAID" : "PENDING" });
        }
    }

    private void deleteSelected() {
        int r = billsTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select a bill");
            return;
        }
        String id = (String) tableModel.getValueAt(r, 0);
        HospitalSystem.bills.removeIf(b -> b.getBillid().equals(id));
        refreshAll();
    }

    private void markAsPaid() {
        int r = billsTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select a bill to mark as paid");
            return;
        }
        String id = (String) tableModel.getValueAt(r, 0);
        Billing bill = null;
        for (Billing b : HospitalSystem.bills) {
            if (b.getBillid().equals(id)) {
                bill = b;
                break;
            }
        }

        if (bill == null) {
            JOptionPane.showMessageDialog(this, "Bill not found");
            return;
        }

        if (bill.isPaid()) {
            JOptionPane.showMessageDialog(this, "This bill is already marked as paid");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Mark bill " + id + " as paid and remove from records?\nTotal Amount: $"
                        + String.format("%.2f", bill.getTotal()),
                "Confirm Payment", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            bill.setPayment(bill.getTotal());
            bill.checkPayment();
            HospitalSystem.bills.remove(bill);
            refreshAll();
            JOptionPane.showMessageDialog(this, "Bill marked as paid and archived");
        }
    }
}
