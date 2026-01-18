import java.util.Scanner;

public class GradeCalculator {

    private static String getGrade(double avg) {
        if (avg >= 90) return "A+";
        if (avg >= 80) return "A";
        if (avg >= 70) return "B";
        if (avg >= 60) return "C";
        if (avg >= 50) return "D";
        return "F";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n;

        while (true) {
            System.out.print("How many subjects? ");
            String line = sc.nextLine().trim();
            try {
                n = Integer.parseInt(line);
                if (n > 0) break;
                System.out.println("Please enter a positive integer.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter an integer.");
            }
        }

        double total = 0.0;
        for (int i = 1; i <= n; i++) {
            while (true) {
                System.out.print("Enter marks for subject " + i + " (0-100): ");
                String line = sc.nextLine().trim();
                try {
                    double mark = Double.parseDouble(line);
                    if (mark >= 0 && mark <= 100) {
                        total += mark;
                        break;
                    } else {
                        System.out.println("Please enter a number between 0 and 100.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a numeric value.");
                }
            }
        }

        double average = total / n;
        String grade = getGrade(average);

        System.out.println("\nResults:");
        System.out.printf("Total Marks: %.2f / %d%n", total, n * 100);
        System.out.printf("Average Percentage: %.2f%%%n", average);
        System.out.println("Grade: " + grade);

        sc.close();
    }
}
