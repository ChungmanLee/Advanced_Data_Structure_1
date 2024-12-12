package ie.ucd.csnl.comp47500.utility;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {

    public static void main(String[] args) {
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"))) {
            for (int i = 1; i <= 10000; i++) {
                int id = i;
                String name = generateRandomName();
                int tableNumber = random.nextInt(10) + 1;
                String items = generateRandomItems();
                boolean isVIP = false;
                double billAmount = calculateBillAmount(items.length());

                String line = String.format("%d,%s,%d,%s,%b,%.2f", id, name, tableNumber, items, isVIP, billAmount);
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Data has been written to data.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomName() {
        String[] firstNames = {"John", "Jane", "Moon", "Cheery", "Merly", "Jane", "Stacey", "Jon", "Ricky", "Donald"};
        String[] lastNames = {"Doe", "Smith", "Row", "Carrick", "Fletcher", "Mave"};

        Random random = new Random();

        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        return firstName + " " + lastName;
    }

    private static String generateRandomItems() {
        String[] menuItems = {"Burger", "Fries", "Coke", "Pizza", "Salad", "Water", "Rice", "Orange"};

        Random random = new Random();
        int numberOfItems = random.nextInt(5) + 1; // Randomly choose 1 to 5 items

        StringBuilder items = new StringBuilder();

        for (int i = 0; i < numberOfItems; i++) {
            items.append(menuItems[random.nextInt(menuItems.length)]);
            if (i < numberOfItems - 1) {
                items.append(";");
            }
        }

        return items.toString();
    }

    private static double calculateBillAmount(int quantity) {
        return quantity * 100.0;
    }
}
