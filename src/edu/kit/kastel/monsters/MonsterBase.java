package edu.kit.kastel.monsters;

import edu.kit.kastel.Element;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a Monster in the game, with various attributes such as health points (HP), attack (ATK), defense (DEF),
 * speed (SPD), and a list of available actions. It also provides methods to interact with these attributes, including printing
 * the monster's details and checking if it has a certain action.
 * @author uljyv
 * @version 1.0
 */
public class MonsterBase {
    private final String name;
    private final Element element;
    private int maxHP;
    private int atk;
    private int def;
    private int spd;
    private List<String> actions;
    /**
     * Constructs a new MonsterBase object with the specified attributes.
     *
     * @param name the name of the monster
     * @param element the element type of the monster
     * @param maxHP the maximum health points of the monster
     * @param atk the attack value of the monster
     * @param def the defense value of the monster
     * @param spd the speed value of the monster
     * @param actions the list of actions that the monster can perform
     */
    public MonsterBase(String name, Element element, int maxHP, int atk, int def, int spd, List<String> actions) {
        this.name = name;
        this.maxHP = maxHP;
        this.element = element;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.actions = actions;
    }
    /**
     * Prints the details of the monster, including its name, element, HP, ATK, DEF, and SPD.
     */
    public void print() {
        System.out.println(getName() + ": ELEMENT " + getElement()
                + ", HP " + getMaxHP() + ", ATK " + getAtk() + ", DEF "
                + getDef() + ", SPD " + getSpd());
    }
    /**
     * Checks if the monster has a specific action.
     *
     * @param actionName the name of the action to check
     * @return true if the monster has the specified action, otherwise false
     */
    public boolean hasAction(String actionName) {
        return actions.contains(actionName);
    }
    /**
     * Gets the maximum health points (HP) of the monster.
     *
     * @return the maximum HP of the monster
     */
    public int getMaxHP() {
        return maxHP;
    }
    /**
     * Gets the list of actions that the monster can perform.
     *
     * @return the list of actions
     */
    public List<String> getActions() {
        return actions;
    }
    /**
     * Gets the defense value of the monster.
     *
     * @return the defense value of the monster
     */
    public int getDef() {
        return def;
    }
    /**
     * Gets the attack value of the monster.
     *
     * @return the attack value of the monster
     */
    public int getAtk() {
        return atk;
    }
    /**
     * Gets the speed value of the monster.
     *
     * @return the speed value of the monster
     */
    public int getSpd() {
        return spd;
    }
    /**
     * Gets the element type of the monster.
     *
     * @return the element type of the monster
     */
    public Element getElement() {
        return element;
    }
    /**
     * Gets the name of the monster.
     *
     * @return the name of the monster
     */
    public String getName() {
        return name;
    }
    /**
     * Reads and parses a line of text representing a monster, creating a MonsterBase object from it.
     * The line should contain the word "monster" followed by the name, element, max HP, ATK, DEF, SPD, and a list of actions.
     *
     * @param line the line of text representing a monster
     * @return a MonsterBase object if the line is valid, or null if the line is not in the expected format
     */
    public static MonsterBase readMonster(String line) {
        String[] parts = line.split(" ");

        if (parts.length < 8 || parts.length > 11) {
            return null;
        }

        if (!parts[0].equals("monster")) {
            return null;
        }

        String name = parts[1];
        Element element = Element.getElement(parts[2]);

        if (element == null) {
            return null;
        }

        int maxHp = 0;
        int atk = 0;
        int def = 0;
        int spd = 0;

        try {
            maxHp = Integer.parseInt(parts[3]);
            atk = Integer.parseInt(parts[4]);
            def = Integer.parseInt(parts[5]);
            spd = Integer.parseInt(parts[6]);
        } catch (NumberFormatException nfe) {
            return null;
        }

        List<String> actions = new ArrayList<>();
        for (int i = 7; i < parts.length; i++) {
            actions.add(parts[i]);
        }

        return new MonsterBase(name, element, maxHp, atk, def, spd, actions);
    }
}