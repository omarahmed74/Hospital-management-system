public class Appointment {

    private String id;
    private Patient patient;
    private Doctor doctor;
    private String dateTime;
    private String status;
    private static int counter = 1;

    public Appointment(Patient patient, Doctor doctor, String dateTime) {
        this.id = "APT" + counter++;
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.status = "BOOKED";
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getStatus() {
        return status;
    }

    public boolean reschedule(String newDateTime) {
        if (status.equals("CANCELLED")) {
            return false;
        }
        this.dateTime = newDateTime;
        return true;
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patient=" + //patient.getName() +
                ", doctor=" + doctor.getName() +
                ", dateTime='" + dateTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
