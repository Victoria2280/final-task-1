package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.monsters.StatusCondition;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
/**
 * The Action class represents an action in the game. An action has a name, an associated element,
 * and a list of effects that define what happens when the action is applied to a target.
 * The action can be applied to monsters in the game, affecting their states accordingly.
 * The class also provides functionality to read actions from a list of strings and print their details.
 * @author uljyv
 * @version 1.0
 */
public class Action {
    /** variable to prevent several messages about effectiveness.*/
    public static boolean wasEffectivenessMessage = false;

    private String name;
    private Element element;
    private List<Effect> effects;
    /**
     * Constructs an Action object with the specified name, element, and list of effects.
     *
     * @param name    The name of the action.
     * @param element The element associated with the action.
     * @param effects The list of effects that define the action's behavior.
     */
    protected Action(String name, Element element, List<Effect> effects) {
        this.effects = effects;
        this.element = element;
        this.name = name;
    }
    /**
     * Applies the effects of the action to the target monster.
     * The effects are applied sequentially, and if the first effect fails, the action is considered unsuccessful.
     *
     * @param user   The monster using the action.
     * @param target The monster receiving the effects of the action.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    protected void applyEffects(Monster user, Monster target) throws CompetitionInterrupter {
        wasEffectivenessMessage = false;

        for (Effect effect : effects) {
            effect.clearCountValues();
        }

        for (Effect effect : effects) {
            effect.generateCountValue();
        }

        System.out.println(user.getName() + " uses " + getName() + "!");

        if (user.getStatusCondition() == StatusCondition.SLEEP) {
            return;
        }

        boolean isFirstInAction = true;
        for (Effect effect : effects) {

            if (!effect.applyEffect(user, target, element, isFirstInAction) & isFirstInAction) {
                System.out.println("The action failed...");
                break;
            }
            isFirstInAction = false;
        }
    }
    /**
     * Applies the action to a target monster.
     * The effects of the action are applied in sequence. If the user or target has 0 health, the action is not executed.
     *
     * @param user   The monster using the action.
     * @param target The monster receiving the effects of the action.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public void applyAction(Monster user, Monster target) throws CompetitionInterrupter {
        user.startAction();

        applyEffects(user, target);

        user.endAction();
    }
    /**
     * Reads and parses an action from a given iterator of strings.
     *
     * @param lineIterator A ListIterator of strings representing lines of action data.
     * @return An Action object if the input data is valid; otherwise, returns null.
     */
    public static Action readAction(ListIterator<String> lineIterator) {
        
        if (!lineIterator.hasNext()) {
            return null;
        }

        String line = lineIterator.next().trim();
        String[] parts = line.split(" ");

        if (parts.length != 3) {
            return null;
        }

        if (!parts[0].equals("action")) {
            return null;
        }

        String name = parts[1];
        Element element = Element.getElement(parts[2]);

        if (element == null) {
            return null;
        }

        List<Effect> effects = Effect.readEffects(lineIterator);
        if (effects == null) {
            return null;
        }

        if (!lineIterator.hasNext()) {
            return null;
        }
        line = lineIterator.next().trim();

        if (!line.equals("end action")) {
            return null;
        }
        return new Action(name, element, effects);
    }

    /**
     * Prints a summary of the action, including its name, element, damage, and hit rate.
     */
    public void print() {
        Effect effect  = effects.get(0);
        System.out.println(getName() + ": ELEMENT " + element.elementToString() + ", Damage " + effect.getDamageStr() + ", HitRate "
                + effect.getHitRateStr());
    }

    /**
     * Gets the name of the action.
     *
     * @return The name of the action.
     */
    public String getName() {
        return name;
    }
    /**
     * Reads a list of actions from the provided iterator.
     *
     * @param lineIterator A ListIterator of strings representing lines of action data.
     * @return A list of Action objects if valid data is found; otherwise null.
     */
    public static List<Action> readActions(ListIterator<String> lineIterator) {
        List<Action> actions = new ArrayList<>();

        while (lineIterator.hasNext()) {
            String line = lineIterator.next().trim();

            while (line.isEmpty() && lineIterator.hasNext()) {
                line = lineIterator.next().trim();
            }

            String[] parts = line.split(" ");

            lineIterator.previous();
            if (parts.length > 1 & !parts[0].equals("action")) {
                if (!actions.isEmpty()) {
                    return actions;
                }
                return null;
            }

            if (parts.length != 3) {
                return null;
            }

            Action action = Action.readAction(lineIterator);
            if (action == null) {
                return null;
            }
            actions.add(action);
        }
        return null;
    }
    /**
     * Determines if the action requires a target monster.
     *
     * @return true if the action needs a target, false otherwise.
     */
    public boolean needTarget() {
        for (Effect effect : effects) {
            if (effect.needTarget()) {
                return true;
            }
        }
        return false;
    }
}