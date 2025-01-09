package task1;

import java.io.*;
import java.util.*;

public class ATM {
    static Scanner scanner = new Scanner(System.in);
    static String loggedInUser = "user"; // User file name

    public static void main(String[] args) {
        // Create user file if it doesn't exist
        createDefaultUserFile();

        while (true) {
            System.out.println("\n--- ATM Menu ---");
            System.out.println("1. Account Balance Inquiry");
            System.out.println("2. Cash Withdrawal");
            System.out.println("3. Cash Deposit");
            System.out.println("4. Change PIN");
            System.out.println("5. Mini Statement");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewBalance();
                    break;
                case 2:
                    withdrawMoney();
                    break;
                case 3:
                    depositMoney();
                    break;
                case 4:
                    changePin();
                    break;
                
                case 5:
                    miniStatement();
                    break;
                case 6:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;  // Exit the application
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Create a default user file if it doesn't exist
    static void createDefaultUserFile() {
        File file = new File(loggedInUser + ".txt");
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("1234");  // Default PIN
                writer.println("9876543210");  // Mobile number
                writer.println("Balance: 10000.0");  // Default balance
            } catch (IOException e) {
                System.out.println("Error creating user file: " + e.getMessage());
            }
        }
    }

    // View Account Balance
    static void viewBalance() {
        try (BufferedReader reader = new BufferedReader(new FileReader(loggedInUser + ".txt"))) {
            reader.readLine(); // Skip PIN
            reader.readLine(); // Skip Mobile
            double balance = Double.parseDouble(reader.readLine().split(": ")[1]);
            System.out.println("Your current balance is: ₹" + balance);
        } catch (IOException e) {
            System.out.println("Error reading balance: " + e.getMessage());
        }
    }

    // Withdraw money from the account
    static void withdrawMoney() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Invalid amount. Try again.");
            return;
        }

        if (!updateBalance(-amount)) {
            System.out.println("Insufficient balance.");
        } else {
            logTransaction("Withdrew ₹" + amount);
            System.out.println("Withdrew ₹" + amount + " successfully.");
        }
    }

    // Deposit money into the account
    static void depositMoney() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Invalid amount. Try again.");
            return;
        }

        updateBalance(amount);
        logTransaction("Deposited ₹" + amount);
        System.out.println("Deposited ₹" + amount + " successfully.");
    }

    // Update balance in the file
    static boolean updateBalance(double amount) {
        File file = new File(loggedInUser + ".txt");
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines.add(reader.readLine()); // PIN
            lines.add(reader.readLine()); // Mobile
            double balance = Double.parseDouble(reader.readLine().split(": ")[1]);
            balance += amount;

            if (balance < 0) {
                return false; // Insufficient balance
            }

            lines.add("Balance: " + balance);
        } catch (IOException e) {
            System.out.println("Error updating balance: " + e.getMessage());
            return false;
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error saving updated balance: " + e.getMessage());
            return false;
        }

        return true;
    }

    // Change PIN method
    static void changePin() {
        System.out.print("Enter your new 4-digit PIN: ");
        String newPin = scanner.next();

        if (newPin.length() != 4 || !newPin.matches("\\d+")) {
            System.out.println("Invalid PIN format! PIN must be 4 digits.");
            return;
        }

        File file = new File(loggedInUser + ".txt");
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines.add(newPin);  // Change PIN here
            reader.readLine(); // Mobile (skip)
            reader.readLine(); // Balance (skip)
        } catch (IOException e) {
            System.out.println("Error reading data: " + e.getMessage());
            return;
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error saving updated PIN: " + e.getMessage());
        }

        System.out.println("PIN updated successfully!");
    }

    // Log a transaction (deposit/withdrawal)
    static void logTransaction(String transactionDetails) {
        File file = new File(loggedInUser + "_transactions.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.println(transactionDetails);
        } catch (IOException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }


    // Mini Statement
    static void miniStatement() {
        File file = new File(loggedInUser + "_transactions.txt");
        if (!file.exists()) {
            System.out.println("No transactions found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String transaction;
            int count = 0;
            System.out.println("\n--- Mini Statement ---");
            while ((transaction = reader.readLine()) != null && count < 5) {
                System.out.println(transaction);
                count++;
            }
            if (count == 0) {
                System.out.println("No transactions available.");
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction history: " + e.getMessage());
        }
    }
}
