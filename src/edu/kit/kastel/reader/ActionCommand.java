package edu.kit.kastel.reader;

import edu.kit.kastel.actions.Action;
import edu.kit.kastel.monsters.Monster;

import java.util.List;
/**
 * Class representing an action command issued by a monster.
 * An action command contains the user (the monster who performs the action),
 * the target (the monster being targeted), and the action being performed.
 * @author uljyv
 * @version 1.0
 */
public class ActionCommand {

    protected Monster user;
    private Monster target;
    private Action action;
    /**
     * Constructs an ActionCommand with the given user, target, and action.
     *
     * @param user the monster performing the action
     * @param target the monster being targeted by the action
     * @param action the action to be performed
     */
    protected ActionCommand(Monster user, Monster target, Action action) {
        this.action = action;
        this.target = target;
        this.user = user;
    }
    /**
     * Returns the speed of the user (performing monster) to determine action order.
     *
     * @return the current speed of the user
     */
    public double getUserSpd() {
        return user.getCurrentSpd();
    }
    /**
     * Applies the action to the target by invoking the action's effects.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public void applyAction() throws CompetitionInterrupter {
        action.applyAction(user, target);
    }
    /**
     * Checks if the user is alive.
     * The user is considered alive if their current health is greater than zero.
     *
     * @return true if the user's health is greater than zero, false otherwise.
     */
    public boolean isUserAlive() {
        if (user.getCurrentHealth() != 0) {
            return true;
        }
        return false;
    }
    /**
     * Parses an input string to create an ActionCommand object.
     * This method validates the input, finds the correct action and target,
     * and creates an ActionCommand with the user, target, and action.
     *
     * @param input the user input containing the action command
     * @param actions a list of available actions that monsters can perform
     * @param monstersInComp a list of monsters currently in combat
     * @param currentUser the monster who is performing the action
     * @return an ActionCommand object if the input is valid, or null if the input is invalid
     */
    public static ActionCommand parseAction(String input, List<Action> actions, List<Monster> monstersInComp, Monster currentUser) {
        String[] parts = input.split(" ");
        if (parts.length == 0 || !parts[0].equals("action")) {
            return null;
        }

        if (parts.length < 2) {
            System.out.println("Error, wrong input.");
            return null;
        }

        String currentAction = parts[1];
        if (!currentUser.getMonsterBase().hasAction(currentAction)) {
            System.out.println("Error, " + currentUser.getName() + " does not know the action " + currentAction);
            return null;
        }

        Action userAction = null;
        for (Action action : actions) {
            if (action.getName().equals(parts[1])) {
                userAction = action;
                break;
            }
        }
        if (userAction == null) {
            return null;
        }

        Monster targetMonster = null;
        if (userAction.needTarget()) {

            if (monstersInComp.size() > 2 & parts.length != 3) {
                return null;
            }

            if (monstersInComp.size() == 2) {
                if (parts.length != 2) {
                    return null;
                }

                for (Monster monster : monstersInComp) {
                    if (!monster.getName().equals(currentUser.getName())) {
                        targetMonster = monster;
                        break;
                    }
                }
            } else {
                for (Monster monster : monstersInComp) {
                    if (monster.getName().equals(parts[2])) {
                        targetMonster = monster;
                        break;
                    }
                }
            }
            if (targetMonster == null) {
                return null;
            }
        }
        return new ActionCommand(currentUser, targetMonster, userAction);
    }
    /**
     * Returns the user (performing monster) of the action command.
     *
     * @return the user monster performing the action
     */
    public Monster getUser() {
        return user;
    }
}