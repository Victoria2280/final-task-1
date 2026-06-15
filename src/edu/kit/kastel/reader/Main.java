package edu.kit.kastel.reader;

import edu.kit.kastel.monsters.Monster;

import java.util.List;
import java.util.Scanner;
/**
 * The main class of the application.
 * It initializes the random number generator and starts the game.
 * @author uljyv
 * @version 1.0
 */
public final class Main {
    /** The currently active monster controlled by the user. */
    public static Monster currentUser = null;
    /** The list of monsters participating in the competition. */
    public static List<Monster> competitionMonsters = null;
    /** Random generator or debug instance.*/
    public static RandomOrDebug random = null;

    private static boolean isDebug = false;
    private static Scanner scanner = null;

    private Main() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * Returns the global scanner instance used for reading user input.
     *
     * @return The global scanner instance.
     */
    public static Scanner getScanner() {
        return scanner;
    }
    /**
     * Returns whether the application is running in debug mode.
     *
     * @return true if debug mode is enabled, false otherwise.
     */
    public static boolean getIsDebug() {
        return isDebug;
    }
    /**
     * The main method that starts the application.
     *
     * @param args Command-line arguments.
     * @throws CloneNotSupportedException If an object cloning operation is not supported.
     */
    public static void main(String[] args) throws CloneNotSupportedException {

        if (args.length < 1) {
            System.err.println("Error, Configuration file path is required.");
            return;
        }

        String configPath = args[0];
        if (args.length > 1) {
            if (args.length > 2) {
                System.out.println("Error, too many command line arguments.");
                return;
            }
            if (args[1].equalsIgnoreCase("debug")) {
                random = RandomOrDebug.createForDebug();
                isDebug = true;
            } else {

                try {
                    long seed = Long.parseLong(args[1]);
                    random = RandomOrDebug.createForSeed(seed);
                } catch (NumberFormatException nfe) {
                    System.out.println("Error, wrong parameter");
                    return;
                }
            }
        } else {
            random = RandomOrDebug.create();
        }

        scanner = new Scanner(System.in);
        Game.startGame(configPath);
        scanner.close();
    }
}