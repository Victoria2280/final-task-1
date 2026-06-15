package edu.kit.kastel.actions;
/**
 * The ProtectTarget enum represents the different types of protection that can be applied to a monster.
 * A monster can either be protected against health-related effects or stat changes.
 * @author uljyv
 * @version 1.0
 */
public enum ProtectTarget {
    /** Protection against health-related damage or effects. */
    HEALTH,
    /** Protection against stat changes (e.g., attack reduction, defense decrease). */
    STATS;
    /**
     * Returns the corresponding ProtectTarget based on a given string input.
     *
     * @param protection The string representation of the protection type.
     * @return The corresponding ProtectTarget if a valid match is found, otherwise null.
     */
    public static ProtectTarget getProtectTarget(String protection) {
        switch (protection) {
            case "health": return HEALTH;
            case "stats": return STATS;
            default: return null;
        }
    }
}
