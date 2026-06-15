package edu.kit.kastel.actions;

import edu.kit.kastel.monsters.Monster;
/**
 * The PassAction class represents an action where the user skips their turn.
 * It extends the Action class but does not apply any effects.
 * Instead, it simply prints a message indicating that the user has passed their turn.
 * @author uljyv
 * @version 1.0
 */
public class PassAction extends Action {
    /**
     * Constructs a PassAction with a predefined name and no associated element or effects.
     */
    public PassAction() {
        super("Pass", null, null);
    }

    @Override
    protected void applyEffects(Monster user, Monster target) {
        System.out.println(user.getName() + " passes!");
    }
}
