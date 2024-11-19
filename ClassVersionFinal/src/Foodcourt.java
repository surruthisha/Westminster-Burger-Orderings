
//Uow no-W1989400
//IIT no- 20222357

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

class Customer {
    private String firstName;
    private String lastName;
    private int burgersRequired;

    public Customer(String firstName, String lastName, int burgersRequired) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.burgersRequired = burgersRequired;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getBurgersRequired() {
        return burgersRequired;
    }
}

class FoodQueue {
    private Customer[] customers;
    private int maxLength;
    private int currentLength;

    public FoodQueue(int maxLength) {
        this.maxLength = maxLength;
        this.currentLength = 0;
        this.customers = new Customer[maxLength];
    }

    public boolean isFull() {
        return currentLength == maxLength;
    }

    public boolean isEmpty() {
        return currentLength == 0;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public void addCustomer(Customer customer) {
        if (!isFull()) {
            customers[currentLength] = customer;
            currentLength++;
        }
    }

    //removing the customer form the specific location
    public Customer removeCustomer(int position) {
        if (position >= 0 && position < currentLength) {
            Customer customer = customers[position];
            shiftCustomersLeft(position);
            currentLength--;
            return customer;
        }
        return null;
    }

    private void shiftCustomersLeft(int startIndex) {
        for (int i = startIndex; i < currentLength - 1; i++) {
            customers[i] = customers[i + 1];
        }
        customers[currentLength - 1] = null;
    }

    //geting the income of the sold burgers in each queue
    public double getIncome() {
        double income = 0;
        for (Customer customer : customers) {
            if (customer != null) {
                income += customer.getBurgersRequired() * 650;
            }
        }
        return income;
    }

    public Customer[] getCustomers() {
        return customers;
    }
}

public class Foodcourt {
    // No. of available burgers
    private static int stockCount = 50;

    // Customers queues
    private static FoodQueue[] cashierQueues = new FoodQueue[3];
    private static FoodQueue waitingList = new FoodQueue(10);

    // Length of the queue
    private static int[] queueSizes = {2, 3, 5};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < cashierQueues.length; i++) {
            cashierQueues[i] = new FoodQueue(queueSizes[i]);
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
                case "110":
                case "IFQ":
                    printIncomeOfEachQueue();
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

    //used to view all queues
    private static void viewAllQueues() {
        System.out.println("*****************");
        System.out.println(" * Cashiers *");
        System.out.println("*****************");

        String[] queueOne = new String[2];
        String[] queueTwo = new String[3];
        String[] queueThree = new String[5];
        String temp1 = Arrays.toString(cashierQueues[0].getCustomers());
        String[] customers1 = temp1.replace("[", "").replace("]", "").split(",");
        int m = 0;
        for (String i : customers1) {
            if (i.trim().equals("null")) {
                queueOne[m] = "  X  ";
            } else {
                queueOne[m] = "  O  ";
            }
            m++;
        }
        String temp2 = Arrays.toString(cashierQueues[1].getCustomers());
        String[] customers2 = temp2.replace("[", "").replace("]", "").split(",");
        int k = 0;
        for (String i : customers2) {
            if (i.trim().equals("null")) {
                queueTwo[k] = "  X  ";
            } else {
                queueTwo[k] = "  O  ";
            }
            k++;
        }
        String temp3 = Arrays.toString(cashierQueues[2].getCustomers());
        String[] customers3 = temp3.replace("[", "").replace("]", "").split(",");
        int l = 0;
        for (String i : customers3) {
            if (i.trim().equals("null")) {
                queueThree[l] = "  X  ";
            } else {
                queueThree[l] = "  O  ";
            }
            l++;
        }

        String[][] queueLen = { queueOne, queueTwo, queueThree };

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                if ((y == 0 && x < 2) || (y == 1 && x < 3) || (y == 2)) {
                    System.out.print(queueLen[y][x]);
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println();
        }

        System.out.print("\nWaiting List - ");
        for (int i=0; i<10;) {
            if (i<waitingList.getCurrentLength()) {
                System.out.print(" O ");
            } else {
                System.out.print(" X ");
            }
            i++;
        }
        System.out.println("\n\nX – Not Occupied   O – Occupied");
    }

    //viewing the empty queues
    private static void viewAllEmptyQueues() {
        for (int i = 0; i < cashierQueues.length; i++) {
            if (cashierQueues[i].isEmpty()) {
                System.out.println("Queue " + (i + 1) + " is empty");
            }
        }
    }

    //Adding customers to the queue
    private static void addCustomerToQueue(Scanner scanner) {
        if(stockCount>0){
            try {
                int queueIndex = getShortestQueueIndex();
                if (queueIndex != -1) {
                    System.out.print("Enter the customer's first name: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Enter the customer's last name: ");
                    String lastName = scanner.nextLine();
                    int burgersRequired;
                    while (true) {
                        System.out.print("Enter the number of burgers required: ");
                        burgersRequired = Integer.parseInt(scanner.nextLine());
                        if (burgersRequired <= stockCount) {
                            break;
                        } else {
                            System.out.println("we only have " + stockCount + " burgers left!");
                        }
                    }

                    Customer customer = new Customer(firstName, lastName, burgersRequired);
                    cashierQueues[queueIndex].addCustomer(customer);

                    stockCount -= burgersRequired;
                    if (stockCount <= 10) {
                        System.out.println("Warning: Low stock count. Remaining burgers in stock: " + stockCount);
                    }
                    System.out.println("Customer added to the queue.");
                } else {
                    System.out.print("Enter the customer's first name: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Enter the customer's last name: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Enter the number of burgers required: ");
                    int burgersRequired = Integer.parseInt(scanner.nextLine());

                    Customer customer = new Customer(firstName, lastName, burgersRequired);
                    waitingList.addCustomer(customer);

                    stockCount -= burgersRequired;
                    if (stockCount <= 10) {
                        System.out.println("Warning: Low stock count. Remaining burgers in stock: " + stockCount);
                    }
                    System.out.println("Customer added to the waiting list.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        } else {
            System.out.println("No burgers left");
        }
    }

    //getting the shortest queue index
    private static int getShortestQueueIndex() {
        int minLength = Integer.MAX_VALUE;
        int shortestIndex = -1;

        for (int i = 0; i < cashierQueues.length; i++) {
            int currentLength = cashierQueues[i].getCurrentLength();
            if (currentLength < minLength) {
                minLength = currentLength;
                shortestIndex = i;
            }
        }
        if (minLength==2 && shortestIndex==0){
            if (cashierQueues[2].getCurrentLength()==5) {
                shortestIndex=-1;
            } else if (cashierQueues[1].getCurrentLength()==3) {
                shortestIndex=shortestIndex + 2;
            }else if(cashierQueues[0].getCurrentLength()==2){
                shortestIndex=shortestIndex + 1;
            }
        }

        return shortestIndex;
    }

    //Removing the customer from the queue
    private static void removeCustomerFromQueue(Scanner scanner) {
        try {
            System.out.print("Enter the cashier number (1, 2, or 3): ");
            int cashierNumber = Integer.parseInt(scanner.nextLine());

            int queueIndex = cashierNumber - 1;
            if (queueIndex >= 0 && queueIndex < cashierQueues.length) {
                FoodQueue queue = cashierQueues[queueIndex];
                if (!queue.isEmpty()) {
                    System.out.print("Enter the customer position (1 to " + queue.getCurrentLength() + "): ");
                    int position = Integer.parseInt(scanner.nextLine());

                    Customer customer = queue.removeCustomer(position - 1);
                    if (customer != null) {
                        stockCount += customer.getBurgersRequired();
                        if (stockCount <= 10) {
                            System.out.println("Warning: Low stock count. Remaining burgers in stock: " + stockCount);
                        }
                        System.out.println("Customer '" + customer.getFirstName() + " " + customer.getLastName() +
                                "' removed from the queue.");

                        if (!waitingList.isEmpty()) {
                            Customer nextCustomer = waitingList.removeCustomer(0);
                            cashierQueues[getShortestQueueIndex()].addCustomer(nextCustomer);
                            stockCount -= nextCustomer.getBurgersRequired();
                            System.out.println("Next customer in the waiting list placed in the food queue.");
                        } else {
                            System.out.println("All queues and the waiting list are empty. No customer to remove.");
                        }
                    } else {
                        System.out.println("Invalid customer position.");
                    }
                } else {
                    System.out.println("The queue is empty. No customer to remove.");
                }
            } else {
                System.out.println("Invalid cashier number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    //removing served customer
    private static void removeServedCustomer() {
        for (FoodQueue queue : cashierQueues) {
            if (!queue.isEmpty()) {
                Customer customer = queue.removeCustomer(0);
                if (customer != null) {
                    updateStockCount();
                    System.out.println("Customer '" + customer.getFirstName() + " " + customer.getLastName() +
                            "' removed from the queue.");
                    if (!waitingList.isEmpty()) {
                        Customer nextCustomer = waitingList.removeCustomer(0);
                        cashierQueues[getShortestQueueIndex()].addCustomer(nextCustomer);
                        stockCount -= nextCustomer.getBurgersRequired();
                        System.out.println("Next customer in the waiting list placed in the food queue.");
                    } else {
                        System.out.println("All queues and the waiting list are empty. No customer to remove.");
                    }
                    return;
                }
            }
        }

    }

    //viewing the full name of customers being placed in alphabetical order.
    private static void viewCustomersSorted() {
        Customer[] allCustomers = getAllCustomers();
        if (allCustomers.length > 0) {
            Arrays.sort(allCustomers, (c1, c2) -> c1.getLastName().compareToIgnoreCase(c2.getLastName()));
            System.out.println("Customers Sorted in alphabetical order:");
            for (Customer customer : allCustomers) {
                System.out.println(customer.getFirstName() + customer.getLastName());
            }
        } else {
            System.out.println("No customers in the queues.");
        }
    }

    //storing the programme data
    private static void storeProgramData() {
        try (PrintWriter writer = new PrintWriter("program_data.txt")) {
            writer.println(stockCount);
            for (FoodQueue queue : cashierQueues) {
                boolean x = false;
                Customer[] customers = queue.getCustomers();
                for (Customer customer : customers) {
                    if (customer != null) {
                        writer.println(customer.getFirstName());
                        writer.println(customer.getLastName());
                        writer.println(customer.getBurgersRequired());
                        x=true;
                    }
                }
                if (x){
                    writer.println(); /*Empty line to separate queues*/
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

            for (int i = 0; i < cashierQueues.length; i++) {
                cashierQueues[i] = new FoodQueue(queueSizes[i]);
            }

            int queueIndex = 0;
            while (scanner.hasNextLine()) {
                String firstName = scanner.nextLine();
                String lastName = scanner.nextLine();
                int burgersRequired = Integer.parseInt(scanner.nextLine());

                if (queueIndex >= 0 && queueIndex < cashierQueues.length) {
                    FoodQueue queue = cashierQueues[queueIndex];
                    Customer customer = new Customer(firstName, lastName, burgersRequired);
                    if (!queue.isFull()) {
                        queue.addCustomer(customer);
                    } else {
                        System.out.println("Warning: Some customers could not be loaded due to insufficient queue space.");
                    }
                }

                if (scanner.hasNextLine() && scanner.nextLine().trim().isEmpty()) {
                    queueIndex++;
                }
            }

            System.out.println("Program data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Program data file not found. Starting with default values.");
        }
    }

    //viewing the remaining stock
    private static void viewRemainingStock() {
        System.out.println("Remaining burgers in stock: " + stockCount);
    }

    //adding the burgers to the stock
    private static void addBurgersToStock(Scanner scanner) {
        try {
            System.out.print("Enter the number of burgers to add: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            stockCount += quantity;
            System.out.println("Burgers added to stock. Total stock count: " + stockCount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    //printing the income of the sold burgers of each queue
    private static void printIncomeOfEachQueue() {
        for (int i = 0; i < cashierQueues.length; i++) {
            FoodQueue queue = cashierQueues[i];
            double income = queue.getIncome();
            System.out.println("Income of Queue " + (i + 1) + ": " + income);
        }
    }

    //displaying a warning message for the low stock count
    private static void updateStockCount() {
        stockCount -= 5;
        if (stockCount <= 10) {
            System.out.println("Warning: Low stock count. Remaining burgers in stock: " + stockCount);
        }
    }

    private static Customer[] getAllCustomers() {
        int totalCustomers = 0;
        for (FoodQueue queue : cashierQueues) {
            totalCustomers += queue.getCurrentLength();
        }
        totalCustomers += waitingList.getCurrentLength();

        Customer[] allCustomers = new Customer[totalCustomers];
        int index = 0;
        for (FoodQueue queue : cashierQueues) {
            Customer[] customers = queue.getCustomers();
            for (Customer customer : customers) {
                if (customer != null) {
                    allCustomers[index] = customer;
                    index++;
                }
            }
        }
        Customer[] waitingCustomers = waitingList.getCustomers();
        for (Customer customer : waitingCustomers) {
            if (customer != null) {
                allCustomers[index] = customer;
                index++;
            }
        }

        return allCustomers;
    }

    //Displaying the menu options
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
        System.out.println("110 or IFQ: Print the income of each queue");
        System.out.println("999 or EXT: Exit the Program");
        System.out.println("**********************************");
        System.out.print("Enter your choice: ");
    }
}
