package edu.kit.kastel.monsters;
/**
 * Enum representing the various status conditions that a monster can experience during the game.
 * @author uljyv
 * @version 1.0
 */
public enum StatusCondition {
    /** The BURN status condition. The monster suffers damage over time due to fire or heat.*/
    BURN,
    /** The WET status condition. The monster is affected by water, which may alter its effectiveness in battle.*/
    WET,
    /** The QUICKSAND status condition. The monster is slowed or immobilized by quicksand.*/
    QUICKSAND,
    /** The SLEEP status condition. The monster is asleep and cannot act in battle.*/
    SLEEP;
    /**
     * Returns the corresponding StatusCondition for a given string value.
     * The input string should match one of the valid status condition names (e.g., "BURN", "WET", "QUICKSAND", or "SLEEP").
     *
     * @param statusCondition the string representation of a status condition
     * @return the corresponding StatusCondition if the string is valid, or null if the string is invalid
     */
    public static StatusCondition getStatusCondition(String statusCondition) {
        switch (statusCondition) {
            case "BURN": return BURN;
            case "WET": return WET;
            case "QUICKSAND": return QUICKSAND;
            case "SLEEP": return SLEEP;
            default: return null;
        }
    }
}
