
import java.util.ArrayList;

public class Billing {

	private String billid;
	private String patient;
	private double doctorFee;
	private int visits;
	private double feePerVisit;
	private double roomCharge;
	private double roomRate;
	private int daysStayed;
	private double medicineCost;
	private ArrayList<Service> service;
	private double payment;
	private double total;
	private boolean isPaid;

	public Billing(String billid, String patient) {
		this.billid = billid;
		this.patient = patient;
		this.service = new ArrayList<>();
		this.doctorFee = 0;
		this.visits = 0;
		this.feePerVisit = 0;
		this.roomCharge = 0;
		this.roomRate = 0;
		this.daysStayed = 0;
		this.medicineCost = 0;
		this.total = 0;
		this.payment = 0;
		this.isPaid = false;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public double getDoctorFee() {
		return doctorFee;
	}

	public void setDoctorFee(double doctorFee) {
		this.doctorFee = doctorFee;
	}

	public int getVisits() {
		return visits;
	}

	public void setVisits(int visits) {
		this.visits = visits;
	}

	public double getFeePerVisit() {
		return feePerVisit;
	}

	public void setFeePerVisit(double feePerVisit) {
		this.feePerVisit = feePerVisit;
	}

	public double getRoomRate() {
		return roomRate;
	}

	public void setRoomRate(double roomRate) {
		this.roomRate = roomRate;
	}

	public double getMedicineCost() {
		return medicineCost;
	}

	public void setMedicineCost(double medicineCost) {
		this.medicineCost = medicineCost;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public String getBillid() {
		return billid;
	}

	public double getRoomCharge() {
		return roomCharge;
	}

	public int getDaysStayed() {
		return daysStayed;
	}

	public ArrayList<Service> getService() {
		return service;
	}

	public double getTotal() {
		return total;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public boolean checkPayment() {
		if (payment >= total) {
			isPaid = true;
		} else {
			isPaid = false;
		}
		return isPaid;
	}

	public void roomCharge() {
		roomCharge = roomRate * daysStayed;
	}

	public void doctorfee() {
		doctorFee = visits * feePerVisit;
	}

	public void addService(Service s) {
		service.add(s);
	}

	public void setDaysStayed(int daysStayed) {
		this.daysStayed = daysStayed;
	}

	public void calculate() {
		
		if (visits > 0 && feePerVisit > 0) {
			doctorFee = visits * feePerVisit;
		}
		
		if (roomRate > 0 && daysStayed > 0) {
			roomCharge = roomRate * daysStayed;
		}

		total = roomCharge + doctorFee + medicineCost;
		for (int i = 0; i < service.size(); i++) {
			total += service.get(i).getPrice();
		}
	}
}
