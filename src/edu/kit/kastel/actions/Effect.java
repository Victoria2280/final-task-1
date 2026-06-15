package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * The Effect class is an abstract representation of an action effect
 * that can be applied to a monster during battle. Effects may include damage,
 * healing, status conditions, and other modifications to a monster's state.
 * @author uljyv
 * @version 1.0
 */
public abstract  class Effect {

    static final String DAMAGE_EFFECT = "damage";
    static final String HEAL_EFFECT = "heal";
    static final String REPEAT_EFFECT = "repeat";
    static final String CONTINUE_EFFECT = "continue";
    static final String PROTECT_EFFECT = "protectStat";
    static final String INFLICT_STATUS_EFFECT = "inflictStatusCondition";
    static final String INFLICT_CHANGE_EFFECT = "inflictStatChange";
    private List<Integer> countValues = new ArrayList<Integer>();
    private Count count = null;

    /**
     * Applies the effect to the specified target based on the given parameters.
     *
     * @param user            The monster using the effect.
     * @param target          The target monster affected by the effect.
     * @param actionElement   The element associated with the action triggering this effect.
     * @param isFirstInAction A flag indicating if this is the first effect in an action sequence.
     * @return true if the effect was successfully applied, false otherwise.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public abstract  boolean applyEffect(Monster user, Monster target, Element actionElement, boolean isFirstInAction)
            throws CompetitionInterrupter;
    /**
     * Reads and creates an Effect instance based on the effect name from the given line iterator.
     *
     * @param lineIterator The iterator over input lines defining the effect.
     * @param effectName   The name of the effect to read.
     * @return A new Effect instance corresponding to the given name, or null if the name is invalid.
     */
    public static Effect readEffect(ListIterator<String> lineIterator, String effectName) {
        switch (effectName) {
            case DAMAGE_EFFECT:
                return DamageEffect.readEffect(lineIterator);
            case HEAL_EFFECT:
                return HealEffect.readEffect(lineIterator);
            case REPEAT_EFFECT:
                return RepeatEffect.readEffect(lineIterator);
            case CONTINUE_EFFECT:
                return ContinueEffect.readEffect(lineIterator);
            case PROTECT_EFFECT:
                return ProtectStat.readEffect(lineIterator);
            case INFLICT_STATUS_EFFECT:
                return InflictStatusCondition.readEffect(lineIterator);
            case INFLICT_CHANGE_EFFECT:
                return InflictStatChange.readEffect(lineIterator);
            default:
                return null;
        }
    }
    /**
     * Reads and returns a list of Effect objects from the provided line iterator.
     *
     * @param lineIterator The iterator over input lines defining multiple effects.
     * @return A list of parsed Effect instances, or null if the input is invalid.
     */
    public static List<Effect> readEffects(ListIterator<String> lineIterator) {
        List<Effect> effects = new ArrayList<Effect>();

        while (lineIterator.hasNext()) {
            String line = lineIterator.next().trim();

            while (line.isEmpty() && lineIterator.hasNext()) {
                line = lineIterator.next().trim();
            }

            String[] parts = line.split(" ");

            if (parts.length < 2) {
                return null;
            }

            lineIterator.previous();

            Effect effect = readEffect(lineIterator, parts[0]);

            if (effect == null) {
                if (effects.isEmpty()) {
                    return null;
                }
                return effects;
            }

            if (effect.hasNestedRepeat()) {
                return null;
            }

            effects.add(effect);
        }
        return null;
    }
    /**
     * Returns a string representation of the damage associated with this effect.
     * By default, this method returns "--" indicating no damage. Subclasses may override
     * this method to provide actual damage values.
     *
     * @return A string representing the damage value, or "--" if not applicable.
     */
    public String getDamageStr() {
        return "--";
    }
    /**
     * Returns a string representation of the hit rate for this effect.
     * By default, this method returns an empty string. Subclasses that have a defined
     * hit rate should override this method to return a percentage value.
     *
     * @return A string representing the hit rate percentage, or an empty string if not applicable.
     */
    public String getHitRateStr() {
        return "";
    }
    /**
     * Determines whether this effect requires a target.
     *
     * @return true if the effect requires a target, false otherwise.
     */
    public boolean needTarget() {
        return false;
    }
    /**
     * Checks whether the object contains nested repeating structures.
     * By default, this method always returns false,
     * but it can be overridden in subclasses to change its behavior.
     *
     * @return false if there are no nested repetitions.
     */
    public boolean hasNestedRepeat() {
        return false;
    }
    /**
     * Sets the count value associated with this effect.
     * The count may be used to control the number of times an effect is applied.
     *
     * @param count The Count object managing repetitions of this effect.
     */
    public void setCount(Count count) {
        this.count = count;
    }
    /**
     * Clears the stored count values. This method is useful when resetting
     * the effect's repeat-related properties.
     */
    protected void clearCountValues() {
        countValues.clear();
    }
    /**
     * Generates a new count value based on the associated Count object.
     * If a count object is set, its value is retrieved and stored.
     *
     * @return The generated count value, or null if no count is set.
     * @throws CompetitionInterrupter If the competition is interrupted during value generation.
     */
    protected Integer generateCountValue() throws CompetitionInterrupter {
        if (count != null) {
            Integer newValue = Integer.valueOf(count.getValue());
            countValues.add(newValue);
            return newValue;
        }
        return null;
    }
    /**
     * Retrieves the next stored count value. If there are no stored values,
     * null is returned.
     *
     * @return The next available count value, or null if none exist.
     */
    protected Integer getNextCountValue() {
        if (countValues.isEmpty()) {
            return null;
        }

        return countValues.remove(0);
    }
}