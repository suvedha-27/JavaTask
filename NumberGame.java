import java.util.Random;
import java.util.Scanner;

public class NumberGame {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static class RoundResult {
        boolean won;
        int attempts;
        RoundResult(boolean won, int attempts) {
            this.won = won;
            this.attempts = attempts;
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Number Guessing Game!");

        System.out.println("\nDefaults: range 1 to 100, max attempts = 10.");
        boolean customize = askYesNo("Would you like to customize range or attempts? (y/N): ");

        int low = 1;
        int high = 100;
        Integer maxAttempts = 10; // null means unlimited

        if (customize) {
            Integer lowIn = promptIntAllowEmpty("Enter lower bound (integer, empty for default 1): ");
            Integer highIn = promptIntAllowEmpty("Enter upper bound (integer, empty for default 100): ");
            if (lowIn != null) low = lowIn;
            if (highIn != null) high = highIn;
            if (high < low) {
                int tmp = low;
                low = high;
                high = tmp;
                System.out.printf("Swapped bounds to make valid range: %d..%d%n", low, high);
            }
            Integer attemptsIn = promptIntAllowEmpty("Enter max attempts per round (empty for unlimited): ");
            if (attemptsIn != null) {
                if (attemptsIn < 1) {
                    System.out.println("Max attempts must be at least 1. Using unlimited instead.");
                    maxAttempts = null;
                } else {
                    maxAttempts = attemptsIn;
                }
            } else {
                maxAttempts = null;
            }
        }

        int roundsPlayed = 0;
        int roundsWon = 0;
        int totalPoints = 0;
        int totalAttempts = 0;

        while (true) {
            roundsPlayed++;
            RoundResult result = playRound(low, high, maxAttempts);
            totalAttempts += result.attempts;
            if (result.won) {
                roundsWon++;
                int pts = calculatePoints(result.attempts, maxAttempts);
                totalPoints += pts;
                System.out.printf("You earned %d point%s this round.%n", pts, pts == 1 ? "" : "s");
            } else {
                System.out.println("No points this round.");
            }

            // summary so far
            System.out.printf("%nScore after %d round%s:%n", roundsPlayed, roundsPlayed == 1 ? "" : "s");
            System.out.printf(" - Rounds won: %d%n", roundsWon);
            System.out.printf(" - Total points: %d%n", totalPoints);
            double avgAttempts = (double) totalAttempts / roundsPlayed;
            System.out.printf(" - Average attempts per round: %.2f%n", avgAttempts);

            boolean playAgain = askYesNo("\nPlay another round? (Y/n): ");
            if (!playAgain) break;
        }

        // final summary
        System.out.println("\nFinal summary:");
        System.out.printf(" - Rounds played: %d%n", roundsPlayed);
        System.out.printf(" - Rounds won: %d%n", roundsWon);
        System.out.printf(" - Total points: %d%n", totalPoints);
        if (roundsPlayed > 0) {
            System.out.printf(" - Average attempts per round: %.2f%n", (double) totalAttempts / roundsPlayed);
        }
        System.out.println("Thanks for playing!");
    }

    private static RoundResult playRound(int low, int high, Integer maxAttempts) {
        int secret = random.nextInt(high - low + 1) + low;
        int attempts = 0;

        System.out.printf("%nI'm thinking of a number between %d and %d.%n", low, high);
        if (maxAttempts != null) {
            System.out.printf("You have up to %d attempts to guess it.%n", maxAttempts);
        } else {
            System.out.println("You have unlimited attempts. Try to guess it in as few tries as possible.");
        }

        while (true) {
            if (maxAttempts != null) {
                int remaining = maxAttempts - attempts;
                if (remaining <= 0) {
                    System.out.printf("Out of attempts! The number was %d.%n", secret);
                    return new RoundResult(false, attempts);
                }
                System.out.printf("Enter your guess (%d attempts left): ", remaining);
            } else {
                System.out.print("Enter your guess: ");
            }

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Please enter a number.");
                continue;
            }

            int guess;
            try {
                guess = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("That's not an integer. Please try again (this does NOT count as an attempt).");
                continue;
            }

            attempts++;

            if (guess == secret) {
                System.out.printf("Correct! You guessed it in %d attempt%s.%n", attempts, attempts == 1 ? "" : "s");
                return new RoundResult(true, attempts);
            } else if (guess < secret) {
                System.out.println("Too low.");
            } else {
                System.out.println("Too high.");
            }

            if (maxAttempts != null && attempts >= maxAttempts) {
                System.out.printf("Sorry — you've used all %d attempts. The number was %d.%n", maxAttempts, secret);
                return new RoundResult(false, attempts);
            }
        }
    }

    private static int calculatePoints(int attempts, Integer maxAttempts) {
        if (attempts <= 0) return 0;
        if (maxAttempts != null) {
            return Math.max(0, maxAttempts - attempts + 1);
        } else {
            return Math.max(0, 11 - attempts); // inverse scale with soft cap
        }
    }

    private static boolean askYesNo(String prompt) {
        System.out.print(prompt);
        String resp = scanner.nextLine().trim().toLowerCase();
        if (resp.isEmpty()) return false; // default No
        return resp.charAt(0) == 'y';
    }

    private static Integer promptIntAllowEmpty(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) return null;
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid integer input. Treating as empty (default).");
            return null;
        }
    }
}
