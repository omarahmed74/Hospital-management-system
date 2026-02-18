public class Medicine {

    private String id;
    private String name;
    private int quantity;
    private double price;
    private double totalPrice;
    private String expiryDate;

    public Medicine(String id, String name, int quantity, double price, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        this.totalPrice = this.quantity * this.price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean issue(int amount) {
        if (amount <= 0)
            return false;
        if (amount > quantity)
            return false;
        quantity -= amount;
        updateTotalPrice();
        return true;
    }

    public void restock(int amount) {
        if (amount > 0) {
            quantity += amount;
            updateTotalPrice();
        }
    }

    @Override
    public String toString() {
        return name +
                " | Qty: " + quantity +
                " | Price/unit: " + price +
                " | Total Price: " + totalPrice;
    }
}
