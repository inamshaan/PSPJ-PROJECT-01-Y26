import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

interface Printable {
    void printDetails();
}

class MenuItem implements Printable, Comparable<MenuItem> {
    public  String name;
    public double price;

    public MenuItem() {
        this.name = "Unknown Item";
        this.price = 0.0;
    }

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    @Override
    public void printDetails() {
        System.out.println(name + " - Rs." + price);
    }

    @Override
    public int compareTo(MenuItem other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return name + " [Rs." + price + "]";
    }
}

class OrderItem extends MenuItem {
    public int quantity;

    public OrderItem(String name, double price, int quantity) {
        super(name, price);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int qty) {
        this.quantity += qty;
    }

    public double calculateTotal() {
        return getPrice() * quantity;
    }

    @Override
    public void printDetails() {
        System.out.println(getName() + " x " + quantity + " = Rs." + calculateTotal());
    }

    @Override
    public String toString() {
        return getName() + " x " + quantity + " = Rs." + calculateTotal();
    }
}

// Custom exception for invalid user input
class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}

public class restaurant_order {
    public static final double TAX_RATE = 0.05;

    private static final MenuItem[] MENU = {
        new MenuItem("Burger", 120.0),
        new MenuItem("Pizza", 250.0),
        new MenuItem("Pasta", 180.0),
        new MenuItem("French Fries", 100.0),
        new MenuItem("Soft Drink", 60.0)
    };

    private static final OrderItem[] ORDERS = new OrderItem[MENU.length];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        // MODULE 5: Sorting menu array using Arrays.sort() and Comparable
        Arrays.sort(MENU);

        // Initialize order items corresponding to menu items
        for (int i = 0; i < MENU.length; i++) {
            ORDERS[i] = new OrderItem(MENU[i].getName(), MENU[i].getPrice(), 0);
        }

        do {
            System.out.println("\n===== RESTAURANT SYSTEM =====");
            System.out.println("1. Display Menu (Sorted by Price)");
            System.out.println("2. Place Order");
            System.out.println("3. Kitchen Order");
            System.out.println("4. Generate & Save Bill");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            // MODULE 4: Exception Handling for Input Mismatch
            try {
                choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        displayMenu();
                        break;
                    case 2:
                        placeOrder(sc);
                        break;
                    case 3:
                        displayKitchenOrder();
                        break;
                    case 4:
                        generateBill();
                        break;
                    case 5:
                        System.out.println("Thank you! Exiting system.");
                        break;
                    default:
                        System.out.println("Invalid choice! Please select between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Input must be an integer.");
                sc.nextLine();
            }
        } while (choice != 5);

        sc.close();
    }

    public static void displayMenu() {
        System.out.println("\n====== RESTAURANT MENU ======");
        System.out.printf("%7s %15s", "Item", "Price");
        System.out.println();
        System.out.println("-----------------------------");
        for (int i = 0; i < MENU.length; i++) {
            System.out.printf("%-1d  %-13s  Rs.%7.2f%n", (i + 1), MENU[i].getName(), MENU[i].getPrice());
        }
        System.out.println("=============================");
    }

    public static void placeOrder(Scanner sc) {
        displayMenu();
        System.out.println("Enter 0 to finish ordering.");

        while (true) {
            try {
                System.out.print("Enter item number: ");
                int itemNo = sc.nextInt();

                if (itemNo == 0) {
                    break;
                }

                if (itemNo < 1 || itemNo > MENU.length) {
                    throw new InvalidOrderException("Item number does not exist in menu.");
                }

                System.out.print("Enter quantity: ");
                int qty = sc.nextInt();

                if (qty <= 0) {
                    throw new InvalidOrderException("Quantity must be greater than zero.");
                }

                ORDERS[itemNo - 1].addQuantity(qty);
                System.out.println(ORDERS[itemNo - 1].getName() + " added to order.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter numbers only.");
                sc.nextLine();
            } catch (InvalidOrderException e) {
                System.out.println("Order Error: " + e.getMessage());
            }
        }

        System.out.println("Order processed and sent to kitchen.");
    }

    public static void displayKitchenOrder() {
        System.out.println("\n===== KITCHEN ORDER =====");
        boolean found = false;

        for (OrderItem item : ORDERS) {
            if (item.getQuantity() > 0) {
                System.out.printf("\n%-15s quantity: %-5d", item.getName(), item.getQuantity());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No active orders in kitchen.");
        }

        System.out.println("\n\n=========================");
    }

    public static double calculateSubtotal() {
        double subtotal = 0;
        for (OrderItem item : ORDERS) {
            subtotal += item.calculateTotal();
        }
        return subtotal;
    }

    public static double calculateDiscount(double subtotal) {
        if (subtotal >= 1000) {
            return subtotal * 0.10;
        } else if (subtotal >= 500) {
            return subtotal * 0.05;
        }
        return 0.0;
    }

    public static void generateBill() {
        double subtotal = calculateSubtotal();

        if (subtotal == 0) {
            System.out.println("No items ordered yet.");
            return;
        }

        double discount = calculateDiscount(subtotal);
        double amount = subtotal - discount;
        double tax = amount * TAX_RATE;
        double finalAmount = amount + tax;

        StringBuilder billBuilder = new StringBuilder();
        billBuilder.append("\n=============== FINAL BILL ===============\n");
        billBuilder.append(String.format("%-15s %-5s %-8s %2s\n","Item", "Qty", "Price", "Total"));
        billBuilder.append("------------------------------------------\n");

        for (OrderItem item : ORDERS) {
            if (item.getQuantity() > 0) {
                billBuilder.append(String.format("%-15s x%-4d Rs.%-5.0f Rs.%7.2f\n", item.getName(), item.getQuantity(), item.getPrice(), item.calculateTotal()));
            }
        }

        billBuilder.append("------------------------------------------\n");
        billBuilder.append(String.format("%-17s  Rs.%7.2f\n","Subtotal: ", subtotal));
        billBuilder.append(String.format("%-17s  Rs.%7.2f\n","Discount: ", discount));
        billBuilder.append(String.format("%-17s  Rs.%7.2f\n","Tax (5%%): ", tax));
         billBuilder.append("------------------------------------------\n");
        billBuilder.append(String.format("%-17s  Rs.%7.2f\n","Final Amount: ", finalAmount));
        billBuilder.append("==========================================\n");

        String billText = billBuilder.toString();
        System.out.print(billText);

        saveBillToFile(billText);
    }

    public static void saveBillToFile(String billData) {
        Path filePath = Paths.get("latest_receipt.txt");

        try {
            Files.writeString(filePath, billData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Bill successfully saved to file: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("File I/O Error: Unable to save receipt - " + e.getMessage());
        }
    }
}
