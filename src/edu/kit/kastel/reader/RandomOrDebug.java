package edu.kit.kastel.reader;

import java.util.Random;
/**
 * A utility class that provides random number generation
 * with an optional debug mode allowing manual user input.
 * @author uljyv
 * @version 1.0
 */
public final class RandomOrDebug {

    private boolean isDebug = false;
    private Random random;

    private RandomOrDebug() {

    }
    /**
     * Creates an instance of RandomOrDebug with a standard random generator.
     *
     * @return A new instance of RandomOrDebug with a random seed.
     */
    public static RandomOrDebug create() {
        RandomOrDebug randomOrDebug = new RandomOrDebug();
        randomOrDebug.random = new Random();
        return randomOrDebug;
    }
    /**
     * Creates an instance of RandomOrDebug with a specified seed.
     *
     * @param seed The seed for the random generator.
     * @return A new instance of RandomOrDebug with a fixed seed.
     */
    public static RandomOrDebug createForSeed(long seed) {
        RandomOrDebug randomOrDebug = new RandomOrDebug();
        randomOrDebug.random = new Random(seed);
        return randomOrDebug;
    }
    /**
     * Creates an instance of RandomOrDebug in debug mode.
     * In debug mode, random values are determined by user input.
     *
     * @return A new instance of RandomOrDebug in debug mode.
     */
    public static RandomOrDebug createForDebug() {
        RandomOrDebug randomOrDebug = new RandomOrDebug();
        randomOrDebug.isDebug = true;
        return randomOrDebug;
    }
    /**
     * Determines an outcome based on the given probability.
     * In debug mode, the user manually decides the result.
     *
     * @param probability The probability of returning true (between 0 and 1).
     * @param context     A description of the decision context.
     * @return True if the event occurs, false otherwise.
     * @throws CompetitionInterrupter If the user enters a quit command.
     */
    public boolean decideProbability(double probability, String context) throws CompetitionInterrupter {
        if (!isDebug) {
            return random.nextDouble() <= probability;
        }

        do {
            System.out.println("Decide " + context + ": yes or no? (y/n)");
            String input = Main.getScanner().nextLine();
            if (performDebugCommands(input)) {
                continue;
            }

            if (input.equals("y")) {
                return true;
            }

            if (input.equals("n")) {
                return false;
            }
            System.err.println("Error, enter y or n.");

        } while (true);
    }
    /**
     * Generates a random double within the given range.
     * In debug mode, the user manually selects a value.
     *
     * @param min     The minimum value (inclusive).
     * @param max     The maximum value (exclusive).
     * @param context A description of the decision context.
     * @return A double between min and max.
     * @throws CompetitionInterrupter If the user enters a quit command.
     */
    public double getBetween(double min, double max, String context) throws CompetitionInterrupter {
        if (!isDebug) {
            return random.nextDouble(min, max);
        }

        do {
            System.out.println("Decide " + context + ": a number between " + min + " and " + max + "?");
            String input = Main.getScanner().nextLine();
            if (performDebugCommands(input)) {
                continue;
            }

            try {
                double number = Double.parseDouble(input);

                if (number <= max & number >= min) {
                    return number;
                }
                System.out.println("Error, out of range.");
            } catch (NumberFormatException e) {
                System.err.println("Error, wrong number.");
            }
        } while (true);
    }
    /**
     * Generates a random integer within the given range.
     * In debug mode, the user manually selects a value.
     *
     * @param min     The minimum value (inclusive).
     * @param max     The maximum value (inclusive).
     * @param context A description of the decision context.
     * @return An integer between min and max.
     * @throws CompetitionInterrupter If the user enters a quit command.
     */
    public int getBetweenInteger(int min, int max, String context) throws CompetitionInterrupter {
        if (!isDebug) {
            return random.nextInt(min, max + 1);
        }

        do {
            System.out.println("Decide " + context + ": an integer between " + min + " and " + max + "?");
            String input = Main.getScanner().nextLine();
            if (performDebugCommands(input)) {
                continue;
            }

            try {
                int number = Integer.parseInt(input);
                if (number <= max & number >= min) {
                    return number;
                }
                System.out.println("Error, out of range.");
            } catch (NumberFormatException nfe) {
                System.out.println("Error, wrong number");
            }
        } while (true);
    }

    private boolean performDebugCommands(String input) throws CompetitionInterrupter {
        switch (input) {
            case "quit":
                throw new CompetitionInterrupter(input);
            case "show":
                Game.printShow();
                return true;
            default: return false;
        }
    }
}