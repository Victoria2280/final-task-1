package edu.kit.kastel.actions;

import edu.kit.kastel.reader.CompetitionInterrupter;
import edu.kit.kastel.reader.Main;

/**
 * The Count class represents a numerical value that can either be a fixed quantity
 * or a randomly generated value within a specified range.
 * @author uljyv
 * @version 1.0
 */
public final class Count {

    private int quantity;
    private CountType type;
    private int min;
    private int max;
    private String context;

    private Count(int min, int max, String context) {
        this.type = CountType.RANDOM;
        this.min = min;
        this.max = max;
        this.context = context;
    }

    private Count(int quantity) {
        this.type = CountType.VALUE;
        this.quantity = quantity;
    }
    /**
     * Reads and parses a Count object from a given string array.
     * The value can be either a fixed number or a random range.
     *
     * @param parts      The array of strings containing the count data.
     * @param startIndex The index from which to start reading the count data.
     * @param context    The context in which the count is being used.
     * @return A Count object if the input data is valid; otherwise null.
     */
    public static Count readCount(String[] parts, int startIndex, String context) {
        if (parts.length < startIndex + 1) {
            return null;
        }

        if (parts[startIndex].equals("random")) {
            if (parts.length <= startIndex + 2) {
                return null;
            }

            try {
                int min = Integer.parseInt(parts[startIndex + 1]);
                int max = Integer.parseInt(parts[startIndex + 2]);

                return new Count(min, max, context);
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        try {
            int value = Integer.parseInt(parts[startIndex]);
            return new Count(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
    /**
     * Gets the type of count, indicating whether it is a fixed value or a random range.
     *
     * @return The CountType representing the count type.
     */
    public CountType getType() {
        return type;
    }
    /**
     * Gets the numerical value of the count. If the count is of type RANDOM,
     * a random value between min and max (inclusive) is generated.
     *
     * @return The numerical value of the count.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public int getValue() throws CompetitionInterrupter {
        if (type == CountType.RANDOM) {
            return Main.random.getBetweenInteger(min, max, context);
        }
        return quantity;
    }
}