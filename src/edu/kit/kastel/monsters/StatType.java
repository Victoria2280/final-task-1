package edu.kit.kastel.monsters;
/**
 * Enum representing the different types of statistics that can be associated with a monster.
 * The available statistics are ATK (Attack), DEF (Defense), SPD (Speed), PRC (Accuracy), and AGL (Agility).
 * @author uljyv
 * @version 1.0
 */
public enum StatType {
    /** The Attack stat. Represents the offensive power of a monster.*/
    ATK,
    /** The Defense stat. Represents the ability of a monster to reduce damage taken from attacks.*/
    DEF,
    /** The Speed stat. Represents how fast a monster can act in combat.*/
    SPD,
    /** The Precision or Accuracy stat. Represents the monster's ability to hit targets accurately.*/
    PRC,
    /** The Agility stat. Represents the monster's ability to evade attacks or react faster in battle.*/
    AGL;
    /**
     * Returns the corresponding StatType for a given string value.
     * The input string should match one of the valid stat type names (e.g., "ATK", "DEF", "SPD", "PRC", or "AGL").
     *
     * @param statType the string representation of a stat type
     * @return the corresponding StatType if the string is valid, or null if the string is invalid
     */
    public static StatType getStatType(String statType) {
        switch (statType) {
            case "ATK": return ATK;
            case "DEF": return DEF;
            case "SPD": return SPD;
            case "PRC": return PRC;
            case "AGL": return AGL;
            default: return null;
        }
    }
    /**
     * Returns the string representation of the StatType.
     * This method is useful for converting a StatType enum value into its corresponding string form.
     *
     * @return the string representation of the StatType (e.g., "ATK", "DEF", "SPD", "PRC", "AGL")
     */
    public String statToString() {
        switch (this) {
            case ATK: return "ATK";
            case DEF: return "DEF";
            case SPD: return "SPD";
            case PRC: return "PRC";
            case AGL: return "AGL";
            default: return "";
        }
    }
}
