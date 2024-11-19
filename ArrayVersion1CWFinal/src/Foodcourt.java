//Uow no-W1989400
//IIT no- 20222357
//22.06.23

import java.io.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Foodcourt {
    //No.of available burgers
    private static int stockCount = 50;

    //customers name array
    private static String[][] cashierQueues = new String[3][];

    //length of the queue
    private static int[] queueSizes = {2, 3, 5};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < cashierQueues.length; i++) {
            cashierQueues[i] = new String[queueSizes[i]];
        }

        while (true) {
            displayMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "100":
                case "VFQ":
                    viewAllQueues();
                    break;
                case "101":
                case "VEQ":
                    viewAllEmptyQueues();
                    break;
                case "102":
                case "ACQ":
                    addCustomerToQueue(scanner);
                    break;
                case "103":
                case "RCQ":
                    removeCustomerFromQueue(scanner);
                    break;
                case "104":
                case "PCQ":
                    removeServedCustomer();
                    break;
                case "105":
                case "VCS":
                    viewCustomersSorted();
                    break;
                case "106":
                case "SPD":
                    storeProgramData();
                    break;
                case "107":
                case "LPD":
                    loadProgramData();
                    break;
                case "108":
                case "STK":
                    viewRemainingStock();
                    break;
                case "109":
                case "AFS":
                    addBurgersToStock(scanner);
                    break;
                case "999":
                case "EXT":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void viewAllQueues() {
        System.out.println("*****************");
        System.out.println(" * Cashiers *");
        System.out.println("*****************");

        //printing the cashier
        for (int row = 0; row < 5; row++) {      //reference- stackoverflow
            for (int col = 0; col < 3; col++) {
                if (cashierQueues[col].length > row) {
                    if (cashierQueues[col][row] != null) {
                        System.out.print(" O ");
                    } else {
                        System.out.print(" X ");
                    }
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }

        System.out.println("X – Not Occupied   O – Occupied");
    }
    //to check if the queue is empty or not
    private static void viewAllEmptyQueues() {
        if (cashierQueues[0][0] == null){
            System.out.println("queue 1 is empty");
        }
        if (cashierQueues[1][0] == null){
            System.out.println("queue 2 is empty");
        }
        if (cashierQueues[2][0] == null){
            System.out.println("queue 3 is empty");
        }
    }

    //adding the customer to the queue
    private static void addCustomerToQueue(Scanner scanner) {
        if (stockCount>0){
            try {
                System.out.print("Enter the cashier number (1, 2, or 3): ");
                int cashierNumber = Integer.parseInt(scanner.nextLine());

                if (cashierNumber >= 1 && cashierNumber <= 3) {
                    int queueIndex = cashierNumber - 1;
                    if (cashierQueues[queueIndex].length > 0 && cashierQueues[queueIndex][cashierQueues[queueIndex].length - 1] == null) {
                        System.out.print("Enter the customer name: ");
                        String customerName = scanner.nextLine();

                        for (int i = 0; i < cashierQueues[queueIndex].length; i++) {
                            if (cashierQueues[queueIndex][i] == null) {
                                cashierQueues[queueIndex][i] = customerName;
                                break;
                            }
                        }

                        updateStockCount();
                        System.out.println("Customer " + customerName + " added to the queue.");
                    } else {
                        System.out.println("The queue is full. Customer cannot be added.");
                    }
                } else {
                    System.out.println("Invalid cashier number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        } else {
            System.out.println("Not enough burgers");
        }
    }
    //removing the customer from the queue
    private static void removeCustomerFromQueue(Scanner scanner) {
        try {
            System.out.print("Enter the cashier number (1, 2, or 3): ");
            int cashierNumber = Integer.parseInt(scanner.nextLine());        //reference - w3school.com

            if (cashierNumber >= 1 && cashierNumber <= 3) {
                int queueIndex = cashierNumber - 1;
                if (cashierQueues[queueIndex].length > 0 && cashierQueues[queueIndex][0] != null) {
                    System.out.print("Enter the customer position (1 to " + cashierQueues[queueIndex].length + "): ");
                    int position = Integer.parseInt(scanner.nextLine());

                    if (position >= 1 && position <= cashierQueues[queueIndex].length && cashierQueues[queueIndex][position - 1] != null) {
                        String customerName = cashierQueues[queueIndex][position - 1];
                        cashierQueues[queueIndex][position - 1] = null;
                        shiftCustomersLeft(cashierQueues[queueIndex], position - 1);

                        updateStockCount();
                        System.out.println("Customer '" + customerName + "' removed from the queue.");
                    } else {
                        System.out.println("Invalid customer position.");
                    }
                } else {
                    System.out.println("The queue is empty. No customer to remove.");
                }
            } else {
                System.out.println("Invalid cashier number.");
            }
        } catch (NumberFormatException e){
            System.out.println("Invalid input");
        }
    }
    //removing the served customers
    private static void removeServedCustomer() {
        for (int i = 0; i < cashierQueues.length; i++) {
            String[] queue = cashierQueues[i];
            if (queue.length > 0 && queue[0] != null) {
                String customerName = queue[0];
                queue[0] = null;
                shiftCustomersLeft(queue, 0);

                updateStockCount();
                System.out.println("Customer '" + customerName + "' removed from the queue.");
                return;
            }
        }

        System.out.println("All queues are empty. No customer to remove.");
    }
    //to view the customers in the alphabetical order
    private static void viewCustomersSorted() {
        String[] allCustomers = getAllCustomers();
        if (allCustomers.length > 0) {
            bubbleSort(allCustomers);
            System.out.println("Customers Sorted in alphabetical order:");
            for (String customer : allCustomers) {
                System.out.println(customer);
            }
        } else {
            System.out.println("No customers in the queues.");
        }
    }

    public static void bubbleSort(String[] arr) {
        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    // Swap arr[j] and arr[j+1]
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }

            // If no two elements were swapped in the inner loop,
            // the array is already sorted
            if (!swapped)
                break;
        }
    }
    //to store the programme data
    private static void storeProgramData() {
        try (PrintWriter writer = new PrintWriter("program_data.txt")) {
            writer.println(stockCount);
            for (String[] queue : cashierQueues) {
                for (String customer : queue) {
                    if (customer != null) {
                        writer.println(customer);
                    }
                }
            }
            System.out.println("Program data stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing program data: " + e.getMessage());
        }
    }
    //loading the programme data
    private static void loadProgramData() {
        try (Scanner scanner = new Scanner(new File("program_data.txt"))) {
            stockCount = Integer.parseInt(scanner.nextLine());

            for (String[] queue : cashierQueues) {
                Arrays.fill(queue, null);
            }

            int queueIndex = 0;
            while (scanner.hasNextLine()) {
                String customer = scanner.nextLine();
                if (customer.trim().isEmpty()) {
                    queueIndex++;
                } else {
                    int availableIndex = -1;
                    for (int i = 0; i < cashierQueues[queueIndex].length; i++) {
                        if (cashierQueues[queueIndex][i] == null) {
                            availableIndex = i;
                            break;
                        }
                    }

                    if (availableIndex != -1) {
                        cashierQueues[queueIndex][availableIndex] = customer;
                    } else {
                        System.out.println("Warning: Some customers could not be loaded due to insufficient queue space.");
                    }
                }
            }

            System.out.println("Program data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Program data file not found. Starting with default values.");
        }
    }
    //to view the remaining stock of burgers left
    private static void viewRemainingStock() {
        System.out.println("Remaining burgers in stock: " + stockCount);
    }

    //to add the burgers to the stock
    private static void addBurgersToStock(Scanner scanner) {
        try {
            System.out.print("Enter the number of burgers to add: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            stockCount += quantity;
            System.out.println("Burgers added to stock. Total stock count: " + stockCount);
        }  catch (Exception e){
            System.out.println("Invalid input");
        }
    }

    private static void updateStockCount() {
        stockCount -= 5;
        if (stockCount <= 10) {
            System.out.println("Warning: Low stock count. Remaining burgers in stock: " + stockCount);
        }
    }

    private static void shiftCustomersLeft(String[] queue, int startIndex) {
        for (int i = startIndex; i < queue.length - 1; i++) {
            queue[i] = queue[i + 1];
        }
        queue[queue.length - 1] = null;
    }

    private static String[] getAllCustomers() {
        int totalCustomers = 0;
        for (String[] queue : cashierQueues) {
            totalCustomers += countNonNullElements(queue);
        }

        String[] allCustomers = new String[totalCustomers];
        int index = 0;
        for (String[] queue : cashierQueues) {
            for (String customer : queue) {
                if (customer != null) {
                    allCustomers[index] = customer;
                    index++;
                }
            }
        }

        return allCustomers;
    }


    private static int countNonNullElements(String[] array) {
        int count = 0;
        for (String element : array) {
            if (element != null) {
                count++;
            }
        }
        return count;
    }
    //display the menu
    private static void displayMenu() {
        System.out.println("**********************************");
        System.out.println("*   Foodies Fave Food Center    *");
        System.out.println("**********************************");
        System.out.println("100 or VFQ: View all Queues");
        System.out.println("101 or VEQ: View all Empty Queues");
        System.out.println("102 or ACQ: Add customer to a Queue");
        System.out.println("103 or RCQ: Remove a customer from a Queue ");
        System.out.println("104 or PCQ: Remove a served customer");
        System.out.println("105 or VCS: View Customers Sorted in alphabetical order ");
        System.out.println("106 or SPD: Store Program Data into file");
        System.out.println("107 or LPD: Load Program Data from file");
        System.out.println("108 or STK: View Remaining burgers Stock");
        System.out.println("109 or AFS: Add burgers to Stock");
        System.out.println("999 or EXT: Exit the Program");
        System.out.println("**********************************");
        System.out.print("Enter your choice: ");
    }
}
