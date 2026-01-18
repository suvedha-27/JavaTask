
 import java.util.Scanner;

/* 
 * Simple ATM demo with BankAccount and ATM UI in one file.
 */
public class ATM {

    // Simple BankAccount class
    static class BankAccount {
        private double balance;

        public BankAccount(double initialBalance) {
            this.balance = Math.max(0.0, initialBalance);
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
            } else {
                throw new IllegalArgumentException("Deposit amount must be positive.");
            }
        }

        public boolean withdraw(double amount) {
            if (amount <= 0) return false;
            if (amount > balance) return false;
            balance -= amount;
            return true;
        }
    }

    // Reads a positive double from the user; returns -1 on invalid/abort
    private static double readPositiveAmount(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value > 0) return value;
                System.out.println("Please enter an amount greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private static int readMenuChoice(Scanner sc) {
        while (true) {
            System.out.print("Choose an option (1-4): ");
            String line = sc.nextLine().trim();
            try {
                int c = Integer.parseInt(line);
                if (c >= 1 && c <= 4) return c;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid choice. Enter a number between 1 and 4.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Simple ATM ===");
        System.out.print("Enter initial balance (or press Enter for 0): ");
        double initial = 0.0;
        String initLine = sc.nextLine().trim();
        if (!initLine.isEmpty()) {
            try {
                initial = Double.parseDouble(initLine);
                if (initial < 0) {
                    System.out.println("Negative initial balance not allowed. Starting with 0.");
                    initial = 0.0;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Starting with 0.");
            }
        }

        BankAccount account = new BankAccount(initial);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");

            int choice = readMenuChoice(sc);

            switch (choice) {
                case 1:
                    System.out.printf("Current balance: %.2f%n", account.getBalance());
                    break;
                case 2: {
                    double amt = readPositiveAmount(sc, "Enter deposit amount: ");
                    try {
                        account.deposit(amt);
                        System.out.printf("Deposit successful. New balance: %.2f%n", account.getBalance());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Deposit failed: " + e.getMessage());
                    }
                    break;
                }
                case 3: {
                    double amt = readPositiveAmount(sc, "Enter withdrawal amount: ");
                    if (account.withdraw(amt)) {
                        System.out.printf("Withdrawal successful. Remaining balance: %.2f%n", account.getBalance());
                    } else {
                        System.out.println("Withdrawal failed: Insufficient funds or invalid amount.");
                        System.out.printf("Current balance: %.2f%n", account.getBalance());
                    }
                    break;
                }
                case 4:
                    System.out.println("Thank you. Goodbye!");
                    running = false;
                    break;
            }
        }

        sc.close();
    }
}
