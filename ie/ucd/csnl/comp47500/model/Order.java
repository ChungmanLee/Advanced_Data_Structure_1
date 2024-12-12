package ie.ucd.csnl.comp47500.model;
import java.util.List;

public class Order {
    private int orderNumber;
    private String customerName;
    private int tableNumber;
    private List<String> itemsOrdered;
    private boolean isPriority;
    private double billAmount;

    public Order(int orderNumber, String customerName, int tableNumber, List<String> itemsOrdered, boolean isPriority, double billAmount) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.itemsOrdered = itemsOrdered;
        this.isPriority = isPriority;
        this.billAmount = billAmount;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTableNumber() {
        return tableNumber;
    }
    
    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}

	public List<String> getItemsOrdered() {
        return itemsOrdered;
    }

	@Override
	public String toString() {
		return "Order: " + orderNumber + ", Customer: " + customerName + ", Table: " + tableNumber
				+ ", Items: " + itemsOrdered + ", Bill Amount=" + billAmount;
	}
}
