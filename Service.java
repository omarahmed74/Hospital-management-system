public class Service {
	
	private String name;
	private double price;
	
	public Service(String name,double price) {
		this.name=name;
		this.price=price;
	}

	public double getPrice() {
		return price;
	}
	
	public String getName() {
		return name;
	}

	public String toString() {
		return name + " - $" + price;
	}
}