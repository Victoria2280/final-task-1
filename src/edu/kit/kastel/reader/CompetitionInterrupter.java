package edu.kit.kastel.reader;
/**
 * Custom exception class that is thrown to interrupt a competition or game process.
 * This exception includes the input that triggered the interruption.
 * @author uljyv
 * @version 1.0
 */
public class CompetitionInterrupter extends Exception {
    private String input;
    /**
     * Constructs a new CompetitionInterrupter exception with the specified input.
     *
     * @param input the input that triggered the interruption
     */
    public CompetitionInterrupter(String input) {
        this.input = input;
    }
    /**
     * Returns the input that caused the competition interruption.
     *
     * @return the input string that caused the interruption
     */
    public String getInput() {
        return input;
    }
}