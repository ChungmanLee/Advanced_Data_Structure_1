package ie.ucd.csnl.comp47500;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ie.ucd.csnl.comp47500.constant.Message;
import ie.ucd.csnl.comp47500.implementation.DequeImpl;
import ie.ucd.csnl.comp47500.implementation.QueueImpl;
import ie.ucd.csnl.comp47500.model.Order;

public class OrderManagementSystem {
	private DequeImpl<Order> doubleEndedQueue;
	private QueueImpl<Order> priorityLinkedQueue;
	private QueueImpl<Order> regularLinkedQueue;
	private List<Integer> idList;
	private int numPriorityOrders;

	public int getNumPriorityOrders() {
		return numPriorityOrders;
	}

	public OrderManagementSystem() {
		priorityLinkedQueue = new QueueImpl<>();
		regularLinkedQueue = new QueueImpl<>();
		idList = new ArrayList<>();
		doubleEndedQueue = new DequeImpl<Order>();
	}

	public void placeOrder(Order order, boolean isPriority) {
		if (isPriority) {
			doubleEndedQueue.addFirst(order);
			numPriorityOrders++;
		} else {
			doubleEndedQueue.add(order);
		}
	}

	public void placeOrderLinkedQueue(Order order, boolean isPriority) {
		// if it's priority, add in the priorityLinkedQueue. Else, add in the
		// regularLinkedQueue.
		if (isPriority) {
			priorityLinkedQueue.add(order);
			numPriorityOrders++;
		} else {
			regularLinkedQueue.add(order);
		}
	}

	public boolean containsOrder(Order order) {
		return doubleEndedQueue.contains(order);
	}

	public boolean containsOrderLinkeQueue(Order order) {
		return priorityLinkedQueue.contains(order) || regularLinkedQueue.contains(order);
	}

	public void displayOrders() {
		Iterator<Order> iterator = doubleEndedQueue.iterator();
		while (iterator.hasNext()) {
			Order order = iterator.next();
			// System.out.println(order.toString());
		}
	}

	public void displayOrdersLinkedQueue() {
		// System.out.println("Priority Orders:");
		for (Order order : priorityLinkedQueue) {
			// System.out.println(order.toString());
		}

		// System.out.println("Regular Orders:");
		for (Order order : regularLinkedQueue) {
			// System.out.println(order.toString());
		}
	}

	public List<Integer> returnIdList() {
		return idList;
	}

	public Order getNextOrder() {
		if (!(doubleEndedQueue.size() == 0)) {
			return (Order) doubleEndedQueue.poll();
		} else {
			return null; // No orders in either queue
		}
	}

	public Order getNextOrderLinkedQueue() {
		if (!priorityLinkedQueue.isEmpty()) {
			return (Order) priorityLinkedQueue.remove();
		} else {
			return null; // No orders in either queue
		}
	}

	public List<Order> readOrdersFromFile(String filePath) {
		List<Order> orders = new ArrayList<>();
		Integer orderId, quantity;
		List<String> items = new ArrayList<>();
		String[] orderData;
		Boolean isPriority;
		Double billAmount;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.contains("search")) {
					String nextLine = reader.readLine();
					orderId = Integer.parseInt(nextLine);
					idList.add(orderId);
				} else {
					orderData = line.split(",");
					orderId = Integer.parseInt(orderData[0]);
					String customerName = orderData[1];
					quantity = Integer.parseInt(orderData[2]);
					items = Arrays.asList(orderData[3].split(";"));
					isPriority = Boolean.parseBoolean(orderData[4]);
					billAmount = Double.parseDouble(orderData[5]);

					Order order = new Order(orderId, customerName, quantity, items, isPriority, billAmount);
					// order.setBillAmount(billAmount);
					orders.add(order);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return orders;
	}

	public static void main(String[] args) {
		String[] files = { "order_input_1.txt", "order_input_2.txt", "order_input_3.txt", "order_input_4.txt",
				"order_input_5.txt", "order_input_6.txt" };

		System.out.println("Process for Dequeue(double ended Queue)");
		for (int i = 0; i < files.length; i++) {
			OrderManagementSystem orderManagementSystem = new OrderManagementSystem();
			boolean found;
			long orderLength = 0l;
			long startTimeForIteration = System.currentTimeMillis();

			// Read orders from file
			List<Order> orders = orderManagementSystem.readOrdersFromFile(Message.INPUT_DATA_FILE_PATH + files[i]);
			orderLength = orders.size();

			// Place the read orders
			for (Order order : orders) {
				orderManagementSystem.placeOrder(order, order.isPriority());
			}

			// orderManagementSystem.displayOrders();

			List<Integer> orderIdsToSearch = orderManagementSystem.returnIdList();

			for (int orderId : orderIdsToSearch) {

				System.out.println(Message.SEARCHING_FOR_ORDER + orderId);
				found = false;
				for (Order order : orders) {

					if (orderId == order.getOrderNumber()) {
						found = true;
						if (orderManagementSystem.containsOrder(order)) {
							System.out.println(Message.ORDER_PRESENT_IN_QUEUE);
						} else {
							System.out.println(Message.ORDER_NOT_PRESENT_IN_QUEUE);
						}
					}
				}
				if (!found) {
					System.out.println(Message.ORDER_NOT_FOUND);
				}
			}

			// for testing create a new order without placing it and then search for it
			// Order testOrder = new Order(100, "John Doe", 5, Arrays.asList("item1",
			// "item2"), false);

			// if (orderManagementSystem.containsOrder(testOrder)) {
			// System.out.println(Message.ORDER_PRESENT_IN_QUEUE);
			// } else {
			// // we expect this behaviour
			// System.out.println("Order number " + testOrder.getOrderNumber() +
			// Message.ORDER_NOT_PRESENT_IN_QUEUE);
			// }

			ProcessOrder processor = new ProcessOrder(orderManagementSystem.getNumPriorityOrders());

			while (true) {
				Order nextOrder = orderManagementSystem.getNextOrder();
				// no more orders to process
				if (nextOrder == null) {
					break;
				}

				// Process the order based on different logics
				if (nextOrder.isPriority()) {
					processor.processPriorityOrder(nextOrder);
				} else {
					processor.processOrder(nextOrder);
				}
			}
			long endTimeForIteration = System.currentTimeMillis();

			System.out.println(orderLength + "," + (endTimeForIteration - startTimeForIteration));
		}

		// for Queue test
		// processing for orders
		System.out.println();
		System.out.println("Process for Queue");
		for (int i = 0; i < files.length; i++) {
			OrderManagementSystem orderManagementSystem = new OrderManagementSystem();
			boolean found;
			long orderLength = 0L;
			long startTimeForIteration = System.currentTimeMillis();

			// Read Files
			List<Order> orders = orderManagementSystem.readOrdersFromFile(Message.INPUT_DATA_FILE_PATH + files[i]);
			orderLength = orders.size();

			// Place order
			for (Order order : orders) {
				orderManagementSystem.placeOrderLinkedQueue(order, order.isPriority());
			}

			// Display Order
			orderManagementSystem.displayOrdersLinkedQueue();

			// Searching process
			List<Integer> orderIdsToSearch = orderManagementSystem.returnIdList();
			for (int orderId : orderIdsToSearch) {

				// find the order from the priority queue first.
				System.out.println(Message.SEARCHING_FOR_ORDER + orderId);
				found = false;
				for (Order order : orders) {

					if (orderId == order.getOrderNumber()) {
						found = true;
						if (orderManagementSystem.containsOrderLinkeQueue(order)) {
							System.out.println(Message.ORDER_PRESENT_IN_QUEUE);
						} else {
							System.out.println(Message.ORDER_NOT_PRESENT_IN_QUEUE);
						}
					}
				}
				if (!found) {
					System.out.println(Message.ORDER_NOT_FOUND);
				}
			}
			// for testing create a new order without placing it and then search for it
			// Order testOrder = new Order(100, "John Doe", 5, Arrays.asList("item1",
			// "item2"), false);

			// if (orderManagementSystem.containsOrderLinkeQueue(testOrder)) {
			// System.out.println(Message.ORDER_PRESENT_IN_QUEUE);
			// } else {
			// // we expect this behaviour
			// System.out.println("Order number " + testOrder.getOrderNumber() +
			// Message.ORDER_NOT_PRESENT_IN_QUEUE);
			// }
			// System.out.println("Execution time: " + elapsedTime + " milliseconds");

			// process order

			ProcessOrder processor = new ProcessOrder(orderManagementSystem.getNumPriorityOrders());
			while (true) {
				// System.out.println(orderManagementSystem.priorityLinkedQueue + "\n" +
				// orderManagementSystem.regularLinkedQueue + " orders left to process.");
				Order nextOrder = orderManagementSystem.getNextOrderLinkedQueue();
				if (nextOrder == null) {
					// System.out.println(Message.NO_MORE_ORDER_TO_PROCESS);
					break;
				}

				// Process the order based on different logics
				if (nextOrder.isPriority()) {
					processor.processPriorityOrder(nextOrder);
				} else {
					processor.processOrder(nextOrder);
				}
			}

			long endTimeForIteration = System.currentTimeMillis();

			System.out.println(orderLength + "," + (endTimeForIteration - startTimeForIteration));

		}

		queueOperations();
		deQueOperations();

	}

	/* Working of Queue Functions */
	private static void queueOperations() {
		QueueImpl<Order> orderQueueData = new QueueImpl<Order>();

		System.out.println("\n---------------------------------------------------------------------------------------");
		System.out.println("Queue Operations: ");

		// To Add Data Into Queue
		System.out.println("\nAdding data into OrderQueue");
		orderQueueData.add(new Order(1, "Merly Carrick", 3, Arrays.asList("Rice", "Coke", "Rice"), true, 1400.00));
		orderQueueData.add(new Order(2, "Ricky Row", 8, Arrays.asList("Water", "Coke"), false, 400.00));
		orderQueueData.add(new Order(3, "Jane Row", 9, Arrays.asList("Pizza", "Rice"), false, 2700.00));
		orderQueueData.add(new Order(4, "Cheery Doe", 9, Arrays.asList("Salad", "Orange", "Apple"), true, 1500.00));
		orderQueueData.add(new Order(5, "Donald Mave", 5, Arrays.asList("Rice", "Coke", "Fries"), false, 1100.00));
		orderQueueData.add(new Order(6, "Stacey Smith", 7, Arrays.asList("Apple", "Coke", "Water"), true, 250.00));
		orderQueueData.add(new Order(7, "Ricky Carrick", 7, Arrays.asList("Orange", "Coke", "Sprite"), false, 1400.00));
		orderQueueData.add(new Order(8, "Jane Carrick", 9, Arrays.asList("Pizza", "Burger", "Rice"), true, 750.00));
		orderQueueData.add(new Order(9, "Jane Doe", 7, Arrays.asList("Salad", "Coke", "Rice"), false, 900.00));
		orderQueueData.add(new Order(10, "Jane Mow", 7, Arrays.asList("Water", "Coke", "Rice"), true, 820.00));

		// To Print data of Queue

		System.out.println("\nPrinting OrderQueue");
		for (Order order : orderQueueData) {
			System.out.println(order);
		}

		// To remove head of Queue
		System.out.println("\nRemoving Head element of OrderQueue");
		orderQueueData.remove();

		// To Print data of Queue
		System.out.println("\nPrinting OrderQueue after use of remove()");
		for (Order order : orderQueueData) {
			System.out.println(order);
		}

		System.out.println("\nPoll Method: " + orderQueueData.poll());

		// To Print data of Queue
		System.out.println("\nPrinting OrderQueue after use of poll()");
		for (Order order : orderQueueData) {
			System.out.println(order);
		}

		System.out.println("\nPeek Method: " + orderQueueData.peek());

		System.out.println("\nChecking if Queue is empty or not?: " + orderQueueData.isEmpty());
		System.out.println("\nSize of Queue: " + orderQueueData.size());
	}

	/* Working of DeQue Functions */
	private static void deQueOperations() {
		DequeImpl<Order> orderDeQueData = new DequeImpl<Order>();

		System.out.println("\n---------------------------------------------------------------------------------------");
		System.out.println("DeQue Operations: ");

		// To Add Data Into DeQue
		System.out.println("\nAdding data into OrderDeQue");

		orderDeQueData.add(new Order(2, "Ricky Row", 8, Arrays.asList("Water", "Coke"), false, 400.00));
		orderDeQueData.add(new Order(3, "Jane Row", 9, Arrays.asList("Pizza", "Rice"), false, 2700.00));
		orderDeQueData.add(new Order(4, "Cheery Doe", 9, Arrays.asList("Salad", "Orange", "Apple"), true, 1500.00));
		orderDeQueData.add(new Order(5, "Donald Mave", 5, Arrays.asList("Rice", "Coke", "Fries"), false, 1100.00));
		orderDeQueData.add(new Order(6, "Stacey Smith", 7, Arrays.asList("Apple", "Coke", "Water"), true, 250.00));
		orderDeQueData.add(new Order(7, "Ricky Carrick", 7, Arrays.asList("Orange", "Coke", "Sprite"), false, 1400.00));
		orderDeQueData.add(new Order(8, "Jane Carrick", 9, Arrays.asList("Pizza", "Burger", "Rice"), true, 750.00));
		orderDeQueData.add(new Order(9, "Jane Doe", 7, Arrays.asList("Salad", "Coke", "Rice"), false, 900.00));

		// To Print data of DeQue
		System.out.println("\nPrinting orderDeQueData");
		for (Order order : orderDeQueData) {
			System.out.println(order);
		}

		// To add at the first
		orderDeQueData.addFirst(new Order(1, "Merly Carrick", 3, Arrays.asList("Rice", "Coke", "Rice"), true, 1400.00));

		// To Print data of DeQue
		System.out.println("\nPrinting OrderQueue after use of addFirst()");
		for (Order order : orderDeQueData) {
			System.out.println(order);
		}

		// To add at the last
		orderDeQueData.addLast(new Order(10, "Jane Mow", 7, Arrays.asList("Water", "Coke", "Rice"), true, 820.00));

		// To Print data of DeQue
		System.out.println("\nPrinting OrderQueue after use of addLast()");
		for (Order order : orderDeQueData) {
			System.out.println(order);
		}

		// To remove head of DeQue
		System.out.println("\nRemoving Head element of orderDeQueData");
		orderDeQueData.remove();

		// To Print data of DeQue
		System.out.println("\nPrinting orderDeQueData after use of remove()");
		for (Order order : orderDeQueData) {
			System.out.println(order);
		}

		// To remove first element of DeQue
		System.out.println("\nRemoving first element of orderDeQueData");
		orderDeQueData.removeFirst();

		// To Print data of DeQue
		System.out.println("\nPrinting orderDeQueData after use of removeFirst()");
		for (Order order : orderDeQueData) {
			System.out.println(order);
		}

		// To remove last element of DeQue
		System.out.println("\nRemoving last element of orderDeQueData");
		orderDeQueData.removeLast();

		// To Print data of DeQue
		System.out.println("\nPrinting orderDeQueData after use of removeLast()");
		for (Order order : orderDeQueData) {
			System.out.println(order);
		}

		System.out.println("\nPoll Method: " + orderDeQueData.poll());

		// To Print data of Queue
		System.out.println("\nPrinting orderDeQueData after use of poll()");
		for (Order order : orderDeQueData) {
			System.out.println(order);
		}

		System.out.println("\nPeek Method: " + orderDeQueData.peek());

		System.out.println("\nChecking if DeQue is empty or not?: " + orderDeQueData.isEmpty());
		System.out.println("\nSize of DeQue: " + orderDeQueData.size());
	}

}
