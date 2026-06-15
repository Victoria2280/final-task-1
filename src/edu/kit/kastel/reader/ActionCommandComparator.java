package edu.kit.kastel.reader;

import java.util.Comparator;
/**
 * Comparator for comparing two ActionCommand objects based on their user speed.
 * The comparison is done in descending order, meaning that commands with a higher user speed
 * will be ordered before those with a lower user speed.
 * This comparator is useful when sorting lists of ActionCommand instances where
 * the highest speed should come first.
 * @author uljyv
 */
public class ActionCommandComparator implements Comparator<ActionCommand> {
    @Override
    public int compare(ActionCommand command1, ActionCommand command2) {

        if (command1.getUserSpd() > command2.getUserSpd()) {
            return -1;
        }
        if (command2.getUserSpd() > command1.getUserSpd()) {
            return 1;
        }
        return 0;
    }
}

