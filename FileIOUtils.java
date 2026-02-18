import java.io.*;

public class FileIOUtils {

    private static final String MEDICINES_FILE = "medicines.txt";
    private static final String DOCTORS_FILE = "doctors.txt";

    public static void saveMedicines() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MEDICINES_FILE, false))) {
            for (Medicine m : HospitalSystem.medicines) {
                writer.println(m.getId() + ";" + m.getName() + ";" + m.getPrice());
            }
        } catch (IOException e) {
            System.err.println("Error saving medicines: " + e.getMessage());
        }
    }

    public static void loadMedicines() {
        HospitalSystem.medicines.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICINES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    Medicine m = new Medicine(id, name, 10, price, "2025-12-31");
                    HospitalSystem.medicines.add(m);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading medicines: " + e.getMessage());
        }
    }

    public static void saveDoctors() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DOCTORS_FILE, false))) {
            for (Doctor d : HospitalSystem.doctors) {
                writer.println(d.getId() + ";" + d.getName() + ";" + d.getAge() + ";" + d.getGender() + ";"
                        + d.getSpecialization());
            }
        } catch (IOException e) {
            System.err.println("Error saving doctors: " + e.getMessage());
        }
    }

    public static void loadDoctors() {
        HospitalSystem.doctors.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTORS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String gender = parts[3].trim();
                    String specialization = parts[4].trim();
                    Doctor d = new Doctor(id, name, age, gender, specialization);
                    HospitalSystem.doctors.add(d);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading doctors: " + e.getMessage());
        }
    }
}