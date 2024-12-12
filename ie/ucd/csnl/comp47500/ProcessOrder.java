package ie.ucd.csnl.comp47500;

import java.util.HashMap;
import java.util.List;

import ie.ucd.csnl.comp47500.model.Order;

/**
 * Class for processing orders and tracking relevant statistics.
 */
public class ProcessOrder {

    private int numPriorityOrders;
    private HashMap<String, Integer> menuCounter = new HashMap<>();

    /**
     * Constructs a new ProcessOrder instance with the specified number of priority orders.
     *
     * @param numPrioOrders the initial number of priority orders.
     */
    public ProcessOrder(int numPrioOrders) {
        this.numPriorityOrders = numPrioOrders;
    }

    /**
     * Retrieves the current number of priority orders.
     *
     * @return the current number of priority orders.
     */
    public int getNumPriorityOrders() {
        return numPriorityOrders;
    }

    /**
     * Processes a regular order by updating the menu item counters.
     *
     * @param order the regular order to be processed.
     */
    public void processOrder(Order order) {
        List<String> items = order.getItemsOrdered();
        items.forEach(item ->
                menuCounter.put(item, menuCounter.getOrDefault(item, 0) + 1));
    }

    /**
     * Processes a priority order by updating the menu item counters and applying a 10% surcharge to the bill amount.
     * Decrements the count of remaining priority orders.
     *
     * @param order the priority order to be processed.
     */
    public void processPriorityOrder(Order order) {
        numPriorityOrders--;
        order.setBillAmount(order.getBillAmount() * 1.1);
        processOrder(order);
    }
}
