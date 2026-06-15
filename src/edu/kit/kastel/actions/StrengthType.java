package edu.kit.kastel.actions;
/**
 * The StrengthType enum represents different types of strength
 * used in actions, determining how the strength value is applied.
 * @author uljyv
 * @version 1.0
 */
public enum StrengthType {
    /** Strength based on an attack formula, influenced by various factors. */
    BASE,
    /** Strength relative to a percentage of a stat, such as HP. */
    REL,
    /** Absolute strength, applied as a fixed value. */
    ABS;
    /**
     * Converts a string representation into a corresponding StrengthType.
     *
     * @param strength The string representation of the strength type.
     * @return The corresponding StrengthType, or null if invalid.
     */
    public static StrengthType getStrengthType(String strength) {
        switch (strength) {
            case "base": return StrengthType.BASE;
            case "rel": return StrengthType.REL;
            case "abs": return StrengthType.ABS;
            default: return null;
        }
    }
    /**
     * Returns a short string representation of the StrengthType.
     *
     * @return A short identifier for the strength type ("b", "r", or "a").
     */
    public String getShortString() {
        switch (this) {
            case ABS: return "a";
            case REL: return "r";
            case BASE: return "b";
            default: return "";
        }
    }
}