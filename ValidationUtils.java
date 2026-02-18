import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationUtils {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isNumeric(String s) {
        if (s == null) return false;
        return s.matches("\\d+");
    }

    public static boolean isPositiveInteger(String s) {
        try {
            return Integer.parseInt(s) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPositiveDouble(String s) {
        try {
            return Double.parseDouble(s) > 0.0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidName(String s) {
        if (s == null) return false;
        return s.matches("[a-zA-Z\\s]+");
    }

    public static boolean isValidId(String s) {
        return isPositiveInteger(s);
    }

    public static boolean isValidPhone(String s) {
        if (s == null) return false;
        String digits = s.replaceAll("[^0-9]", "");
        return digits.length() == 11;
    }

    public static boolean isValidDate(String s) {
        if (s == null) return false;
        try {
            LocalDate.parse(s, DATE_FMT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidDateTime(String s) {
        if (s == null) return false;
        try {
            LocalDateTime.parse(s, DATETIME_FMT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
}
