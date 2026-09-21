# PSPJ-
RESTAURANT SYSTEM

RESTAURANT ORDER, KITCHEN & BILLING SYSTEM
              PSPJ Project

ABSTRACT
The Restaurant Order, Kitchen & Billing System is a beginner-level Java application designed to manage basic restaurant operations through a console interface. The system allows users to view a menu, place food orders, send the order information to the kitchen, and generate a final bill. It uses arrays to store menu items, prices, and quantities, while control-flow statements are used to handle menu selection and input validation. Methods are used to divide the program into smaller and reusable sections. The project demonstrates fundamental Java concepts such as variables, constants, operators, Scanner input, formatted output, conditional statements, loops, methods, method parameters, return values, arrays, searching, summation, and counting.

SEGREGATION 
The project is divided into separate functional modules so that each part performs a specific task. This makes the program easier to understand, test, modify, and maintain.
•	Menu Management: Stores food names and prices in arrays and displays them to the user.
•	Order Management: Accepts item numbers and quantities and stores the customer's order.
•	Kitchen Management: Displays the ordered food items and their quantities for kitchen preparation.
•	Billing Management: Calculates subtotal, discount, tax, and final payable amount.
•	Input Validation: Checks invalid menu choices and incorrect quantities.
•	Method-Based Design: Uses separate methods for menu display, ordering, calculation, and billing.
Advantages:
•	Simple and beginner-friendly console interface.
•	Uses modular methods, making the code easier to understand and maintain.
•	Arrays reduce repetitive code for storing menu and order information.
•	Automatic calculation reduces manual billing errors.
•	Input validation prevents invalid item numbers and quantities.
•	The system can be expanded later with customer details, tables, payment modes, or database support.

IMPLEMENTATION 

    import java.util.Scanner;
    public class RestaurantSystem {

    static final double TAX_RATE = 0.05;
    static String[] foodItems = {
        "Burger", "Pizza", "Pasta",
        "French Fries", "Soft Drink"
    };

    static double[] prices = {
        120.0, 250.0, 180.0, 100.0, 60.0
    };

    static int[] quantities = new int[foodItems.length];

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== RESTAURANT SYSTEM =====");
            System.out.println("1. Display Menu");
            System.out.println("2. Place Order");
            System.out.println("3. Kitchen Order");
            System.out.println("4. Generate Bill");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

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
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        sc.close();
    }

    public static void displayMenu() {

        System.out.println("\n===== RESTAURANT MENU =====");

        for (int i = 0; i < foodItems.length; i++) {
            System.out.println(
                (i + 1) + ". " +
                foodItems[i] + " - Rs." +
                prices[i]
            );
        }

        System.out.println("===========================");
    }

    public static void placeOrder(Scanner sc) {

        int item;
        int quantity;

        displayMenu();

        System.out.println("Enter 0 to finish ordering.");

        while (true) {

            System.out.print("Enter item number: ");
            item = sc.nextInt();

            if (item == 0) {
                break;
            }

            if (item < 1 || item > foodItems.length) {
                System.out.println("Invalid item number.");
                continue;
            }

            System.out.print("Enter quantity: ");
            quantity = sc.nextInt();

            if (quantity <= 0) {
                System.out.println("Invalid quantity.");
                continue;
            }

            quantities[item - 1] += quantity;

            System.out.println(
                foodItems[item - 1] +
                " added to the order."
            );
        }

        System.out.println("Order sent to kitchen.");
    }

    public static void displayKitchenOrder() {

        System.out.println("\n===== KITCHEN ORDER =====");

        boolean found = false;

        for (int i = 0; i < quantities.length; i++) {

            if (quantities[i] > 0) {

                System.out.println(
                    foodItems[i] +
                    " - Quantity: " +
                    quantities[i]
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No orders available.");
        }

        System.out.println("=========================");
    }

    public static double calculateSubtotal() {

        double subtotal = 0;

        for (int i = 0; i < foodItems.length; i++) {
            subtotal += prices[i] * quantities[i];
        }

        return subtotal;
    }

    public static double calculateDiscount(double subtotal) {

        double discount = 0;

        if (subtotal >= 1000) {
            discount = subtotal * 0.10;
        } else if (subtotal >= 500) {
            discount = subtotal * 0.05;
        }

        return discount;
    }

    public static void generateBill() {

        double subtotal = calculateSubtotal();

        if (subtotal == 0) {
            System.out.println("No items ordered.");
            return;
        }

        double discount = calculateDiscount(subtotal);
        double amount = subtotal - discount;
        double tax = amount * TAX_RATE;
        double finalAmount = amount + tax;

        System.out.println("\n===== FINAL BILL =====");

        for (int i = 0; i < foodItems.length; i++) {

            if (quantities[i] > 0) {

                double total =
                    prices[i] * quantities[i];

                System.out.println(
                    foodItems[i] + " x " +
                    quantities[i] + " = Rs." +
                    total
                );
            }
        }

        System.out.println("----------------------");
        System.out.println("Subtotal: Rs." + subtotal);
        System.out.println("Discount: Rs." + discount);
        System.out.println("Tax: Rs." + tax);
        System.out.println("Final Amount: Rs." + finalAmount);
        System.out.println("======================");
    }
