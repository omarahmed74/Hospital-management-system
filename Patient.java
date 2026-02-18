public class Patient extends Person {
	private String phone;
	private String address;

	public Patient(String id, String name, int age, String gender, String phone, String address) {
		super(id, name, age, gender);
		this.phone = phone;
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String getRole() {
		return "Patient";
	}
}
