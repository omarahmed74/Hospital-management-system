import java.util.ArrayList;

public class Doctor extends Person {
	private String specialization;
	private ArrayList<Appointment> appointments = new ArrayList<>();

	public Doctor(String id, String name, int age, String gender, String specialization) {
		super(id, name, age, gender);
		this.specialization = specialization;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void addAppointment(Appointment a) {
		appointments.add(a);
	}

	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(ArrayList<Appointment> appointments) {
		this.appointments = appointments;
	}

	@Override
	public String getRole() {
		return "Doctor";
	}
}
