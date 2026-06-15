package edu.kit.kastel.actions;
/**
 * The Strength class represents a strength value used in effects,
 * which can have different types based on how the strength is applied.
 * @author uljyv
 * @version 1.0
 */
public class Strength {
    private StrengthType type;
    private int value;
    /**
     * Constructs a Strength object with the specified type and value.
     *
     * @param type  The type of strength (absolute, relative, base, etc.).
     * @param value The numerical value of the strength.
     */
    public Strength(StrengthType type, int value) {
        this.type = type;
        this.value = value;
    }
    /**
     * Gets the numeric value of the strength.
     *
     * @return The strength value.
     */
    public int getValue() {
        return value;
    }
    /**
     * Gets the type of strength.
     *
     * @return The strength type.
     */
    public StrengthType getType() {
        return type;
    }
    /**
     * Returns a short string representation of the strength.
     * This format consists of the short string representation of the type
     * followed by the numerical value.
     *
     * @return A string representation of the strength.
     */
    public String shortStringr() {
        return getType().getShortString() + getValue();
    }
}