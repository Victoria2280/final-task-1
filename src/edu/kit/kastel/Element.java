package edu.kit.kastel;
/**
 * Represents the different elements in the game.
 * @author uljyv
 * @version 1.0
 */
public enum Element {
    /** Represents the water element.*/
    WATER,
    /** Represents the fire element.*/
    FIRE,
    /** Represents the earth element.*/
    EARTH,
    /** Represents a neutral or non-elemental type.*/
    NORMAL;
    /**
     * Returns the corresponding Element for the given string.
     *
     * @param word The string representation of the element.
     * @return The matching Element.
     * @throws IllegalArgumentException if the element is not recognized.
     */
    public static Element getElement(String word) {
        switch (word) {
            case "FIRE":
                return Element.FIRE;
            case "WATER":
                return Element.WATER;
            case "EARTH":
                return Element.EARTH;
            case "NORMAL":
                return Element.NORMAL;
            default:
                return null;
        }
    }
    /**
     * Converts the current element type to its corresponding string representation.
     *
     * @return A string representing the element type. If the element is not recognized, an empty string is returned.
     */
    public String elementToString() {
        switch (this) {
            case NORMAL: return "NORMAL";
            case FIRE: return "FIRE";
            case WATER: return "WATER";
            case EARTH: return "EARTH";
            default: return "";
        }
    }
}