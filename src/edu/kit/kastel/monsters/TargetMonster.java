package edu.kit.kastel.monsters;
/**
 * Enum representing the target selection for actions in the game.
 * @author uljyv
 * @version 1.0
 */
public enum TargetMonster {
    /** The target is another monster that is not the user.*/
    TARGET,
    /** The target is the user (the monster performing the action).*/
    USER;
    /**
     * Returns the corresponding TargetMonster for a given string value.
     * The input string should match one of the valid target types (e.g., "target" or "user").
     *
     * @param targetMonster the string representation of a target type
     * @return the corresponding TargetMonster if the string is valid, or null if the string is invalid
     */
    public static TargetMonster getTargetMonster(String targetMonster) {
        switch (targetMonster) {
            case "target":
                return TARGET;
            case "user":
                return USER;
            default: return null;
        }

    }
    /**
     * Returns the appropriate monster (either the user or target) based on the current TargetMonster.
     * This is used to determine which monster is the target of an action.
     *
     * @param user the monster performing the action
     * @param target the monster that is the intended target of the action
     * @return the monster selected as the target (either the user or the target monster)
     */
    public Monster getMonster(Monster user, Monster target) {
        switch (this) {
            case TARGET:
                return target;
            case USER:
                return user;
            default: return null;
        }
    }
}
