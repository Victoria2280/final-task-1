package edu.kit.kastel.reader;

import edu.kit.kastel.actions.PassAction;
import edu.kit.kastel.monsters.Monster;
/**
 * Represents an action where the monster skips its turn.
 * @author uljyv
 * @version 1.0
 */
public class PassCommand extends ActionCommand {

    private static PassAction passAction = new PassAction();
    /**
     * Creates a new PassAction for the given monster.
     *
     * @param user The monster that will perform the pass action.
     */
    public PassCommand(Monster user) {
        super(user, null, null);
    }
    @Override
    public void applyAction() throws CompetitionInterrupter {
        passAction.applyAction(user, user);
    }
}