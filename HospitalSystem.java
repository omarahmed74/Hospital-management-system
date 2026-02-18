import javax.swing.*;
import java.util.ArrayList;

public class HospitalSystem extends JFrame {

    public static ArrayList<Patient> patients = new ArrayList<>();
    public static ArrayList<Doctor> doctors = new ArrayList<>();
    public static ArrayList<Appointment> appointments = new ArrayList<>();
    public static ArrayList<Medicine> medicines = new ArrayList<>();
    public static ArrayList<Service> services = new ArrayList<>();
    public static ArrayList<Billing> bills = new ArrayList<>();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password123";

    public HospitalSystem() {
        setTitle("Hospital Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        createLoginPanel();
    }

    private void createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        panel.setBackground(new java.awt.Color(240, 240, 240));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username Label and Field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Buttons Panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new java.awt.Color(240, 240, 240));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new java.awt.Color(33, 150, 243));
        loginBtn.setForeground(java.awt.Color.WHITE);
        loginBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (validateLogin(username, password)) {
                loadDataFromFiles();
                SwingUtilities.invokeLater(() -> {
                    HospitalMainFrame mainFrame = new HospitalMainFrame();
                    mainFrame.setVisible(true);
                });
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
                usernameField.requestFocus();
            }
        });

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(new java.awt.Color(244, 67, 54));
        exitBtn.setForeground(java.awt.Color.WHITE);
        exitBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginBtn);
        buttonPanel.add(exitBtn);
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private boolean validateLogin(String username, String password) {
        return username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD);
    }

    private void loadDataFromFiles() {
        FileIOUtils.loadDoctors();
        FileIOUtils.loadMedicines();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HospitalSystem launcher = new HospitalSystem();
            launcher.setVisible(true);
        });
    }
}
