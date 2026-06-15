package edu.kit.kastel.reader;
import edu.kit.kastel.actions.Action;
import edu.kit.kastel.monsters.MonsterBase;
import edu.kit.kastel.monsters.Monster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
/**
 * This class represents the game where monsters fight in a competition.
 * It loads monster data and actions from a configuration file, handles the competition flow,
 * and manages user inputs during the competition.
 * @author uljyv
 * @version 1.0
 */
public final class Game {
    static final String PASS_COMMAND = "pass";
    static final String SHOW_COMMAND = "show";
    static final String SHOW_ACTIONS_COMMAND = "show actions";
    static final String SHOW_MONSTERS_COMMAND = "show monsters";
    static final String SHOW_STATS_COMMAND = "show stats";
    static final String COMPETITION_COMMAND = "competition";
    static final String ACTION_COMMAND = "action";
    static final String LOAD_COMMAND = "load";
    static final String QUIT_COMMAND = "quit";
    private List<MonsterBase> baseMonsters;
    private List<Action> actions;
    private String filePath;

    private Game(String filePath) {
        this.filePath = filePath;
    }
    /**
     * Prints the current state of the competition monsters.
     * If a monster is the current user, it is marked with an asterisk (*).
     */
    public static void printShow() {
        if (Main.currentUser == null || Main.competitionMonsters == null) {
            System.out.println("Error, no competition.");
            return;
        }
        for (int i = 0; i < Main.competitionMonsters.size(); ++i) {
            Monster monster = Main.competitionMonsters.get(i);
            String currentMonsterStr = "";
            if (monster.getName().equals((Main.currentUser.getName()))) {
                currentMonsterStr = "*";
            }
            monster.printMonster(currentMonsterStr, i + 1);
        }
    }

    private static List<MonsterBase> readMonsters(ListIterator<String> lineIterator) {
        List<MonsterBase> baseMonsters = new ArrayList<>();
        while (lineIterator.hasNext()) {
            String line = lineIterator.next().trim();

            while (line.isEmpty() && lineIterator.hasNext()) {
                line = lineIterator.next().trim();
            }

            MonsterBase monsterBase = MonsterBase.readMonster(line);
            if (monsterBase == null) {
                return null;
            }
            baseMonsters.add(monsterBase);
        }
        if (baseMonsters.isEmpty()) {
            return null;
        }
        return baseMonsters;
    }

    private void showActions(Monster currentMonster) {
        System.out.println("ACTIONS OF " + currentMonster.getName());
        for (String actionName : currentMonster.getMonsterBase().getActions()) {
            for (Action action : actions) {
                if (action.getName().equals(actionName)) {
                    action.print();
                    break;
                }
            }
        }
    }

    private boolean loadConfig(String configPath) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(configPath));
        } catch (IOException e) {
            return false;
        }
        ListIterator<String> listIterator = lines.listIterator();
        List<Action> actions = Action.readActions(listIterator);
        List<MonsterBase> baseMonsters = readMonsters(listIterator);

        for (String line : lines) {
            System.out.println(line);
        }
        if (actions == null) {
            System.out.println("Error, read actions.");
            return false;
        }
        if (baseMonsters == null) {
            System.out.println("Error, read monsters.");
            return false;
        }
        for (int i = 0; i < baseMonsters.size() - 1; i++) {
            for (int j = i + 1; j < baseMonsters.size(); j++) {
                if (baseMonsters.get(i).getName().equals(baseMonsters.get(j).getName())) {
                    System.out.println("Error, monsters with same name.");
                    return false;
                }
            }
        }
        for (int i = 0; i < actions.size() - 1; i++) {
            for (int j = i + 1; j < actions.size(); j++) {
                if (actions.get(i).getName().equals(actions.get(j).getName())) {
                    System.out.println("Error, actions with same name.");
                    return false;
                }
            }
        }
        for (MonsterBase baseMonster : baseMonsters) {
            for (String actionName : baseMonster.getActions()) {
                boolean actionPresent = false;
                for (Action action: actions) {
                    if (action.getName().equals(actionName)) {
                        actionPresent = true;
                        break;
                    }
                }
                if (!actionPresent) {
                    System.out.println("Error, unknown action.");
                    return false;
                }
            }
        }
        this.actions = actions;
        this.baseMonsters = baseMonsters;

        System.out.println("");
        System.out.println("Loaded " + actions.size() + " actions, " + baseMonsters.size() + " monsters.");
        return true;
    }
    private List<Monster> removeDied(List<Monster> monsters) {
        List<Monster> monstersAlive = new ArrayList<>();
        for (Monster monster : monsters) {
            if (monster.getCurrentHealth() != 0) {
                monstersAlive.add(monster);
            }
        }
        return monstersAlive;
    }

    private boolean isCommandInterrupter(String input) {
        if (input.equals(QUIT_COMMAND)) {
            return true;
        }
        String[] parts = input.split(" ");
        if (parts.length == 0) {
            return false;
        }

        switch (parts[0]) {
            case LOAD_COMMAND:
            case COMPETITION_COMMAND:
                return true;
            default:
                return false;
        }
    }

    private void showMonsters() {
        for (MonsterBase baseMonster : baseMonsters) {
            baseMonster.print();
        }
    }

    private void loadFile(String input) {
        String loadCommand = input.trim();
        String filePath = loadCommand.substring(4).trim().replace("\"", "");
        if (filePath.isEmpty()) {
            System.err.println("Error, configuration file path is required.");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Error, such file does not exist.");
        }
        loadConfig(filePath);
    }

    private List<Monster> parseMonsters(String input) {
        List<Monster> monsters = new ArrayList<>();
        String[] parts = input.split(" ");
        for (int i = 1; i < parts.length; i++) {
            Monster monster = null;
            for (MonsterBase baseMonster : baseMonsters) {
                if (baseMonster.getName().equals(parts[i])) {
                    monster = new Monster(baseMonster);
                    break;
                }
            }
            if (monster == null) {
                System.out.println("Error, such monster does not exist.");
                return null;
            }
            monsters.add(monster);
        }
        if (monsters.size() < 2) {
            System.out.println("Error, not enough monsters.");
            return null;
        }
        for (int i = 0; i < monsters.size() - 1; i++) {
            Monster monsterI = monsters.get(i);
            int nameCount = 1;
            for (int j = i + 1; j < monsters.size(); j++) {
                Monster monsterJ = monsters.get(j);
                if (monsterI.getName().equals(monsterJ.getName())) {
                    monsterJ.setNameNumber(++nameCount);
                }
            }
            if (nameCount > 1) {
                monsterI.setNameNumber(1);
            }
        }
        return monsters;
    }

    private List<Monster> phase0(List<Monster> monsters) {
        List<Monster> resultMonsters = removeDied(monsters);

        if (resultMonsters.size() == 1) {
            System.out.println("");
            System.out.println(resultMonsters.get(0).getName() + " has no opponents left and wins the competition!");
            return null;
        }
        if (resultMonsters.isEmpty()) {
            System.out.println("");
            System.out.println("All monsters have fainted. The competition ends without a winner!");
            return null;
        }
        return  resultMonsters;
    }

    private List<ActionCommand> phase1(List<Monster> monsters) throws CompetitionInterrupter {
        List<ActionCommand> actionCommands = new ArrayList<>();

        for (Monster monster : monsters) {
            ActionCommand actionCommand = null;
            Main.currentUser = monster;
            do {
                System.out.println("");
                System.out.println("What should " + Main.currentUser.getName() + " do?");

                String inputCompetition = Main.getScanner().nextLine().trim();

                if (isCommandInterrupter(inputCompetition)) {
                    throw new CompetitionInterrupter(inputCompetition);
                }
                if (inputCompetition.equals(PASS_COMMAND)) {
                    actionCommand = new PassCommand(Main.currentUser);
                } else {
                    actionCommand = performCommand(inputCompetition, monsters, Main.currentUser);
                }
            }
            while (actionCommand == null);
            actionCommands.add(actionCommand);
        }
        Main.currentUser = null;
        actionCommands.sort(new ActionCommandComparator());
        return actionCommands;
    }

    private void phase2(List<ActionCommand> actionCommands) throws CompetitionInterrupter {
        for (ActionCommand command : actionCommands) {
            Main.currentUser = command.getUser();
            if (!command.isUserAlive()) {
                continue;
            }
            System.out.println("");
            System.out.println("It's " + Main.currentUser.getName() + "'s turn.");
            command.applyAction();
        }
        Main.currentUser = null;
    }

    private void startCompetition(String input) throws CompetitionInterrupter {
        List<Monster> monsters = parseMonsters(input);

        if (monsters == null) {
            return;
        }

        Main.competitionMonsters = new ArrayList<>();
        for (Monster monster : monsters) {
            Main.competitionMonsters.add(monster);
        }
        System.out.println("The " + monsters.size() + " monsters enter the competition!");

        while (true) {
            monsters = phase0(monsters);

            if (monsters == null) {
                break;
            }

            List<ActionCommand> actionCommands = phase1(monsters);

            phase2(actionCommands);

            for (ActionCommand command : actionCommands) {
                command.getUser().endRound();
            }
        }
        Main.competitionMonsters = null;
    }

    private ActionCommand performCommand(String inputCompetition, List<Monster> monsters,
                                         Monster monster) throws CompetitionInterrupter {

        ActionCommand command = ActionCommand.parseAction(inputCompetition, actions, monsters, monster);
        if (command != null) {
            return command;
        }

        String[] parts = inputCompetition.split(" ");
        if (parts.length > 0 & parts[0].equals(ACTION_COMMAND)) {
            return null;
        }

        switch (inputCompetition) {
            case SHOW_COMMAND:
                printShow();
                break;
            case SHOW_MONSTERS_COMMAND:
                showMonsters();
                break;
            case SHOW_ACTIONS_COMMAND:
                showActions(monster);
                break;
            case SHOW_STATS_COMMAND:
                showStats(monster);
                break;
            default:
                System.out.println("Error, such command does not exist.");
                return null;
        }
        return null;
    }
    private void showStats(Monster currentMonster) {
        currentMonster.printStats();
    }
    /**
     * Starts the game by initializing a new `Game` instance and running the game logic.
     *
     * @param filePath the path to the configuration file that contains game setup information,
     *                 including monsters, actions, and other game parameters
     */
    public static void startGame(String filePath) {
        Game game = new Game(filePath);
        game.start();
    }

    private void start() {
        loadConfig(filePath);

        String input = Main.getScanner().nextLine().trim();
        while (!input.equals(QUIT_COMMAND)) {

            String[] parts = input.split(" ");
            String command = parts[0];
            if (command.equals(COMPETITION_COMMAND)) {
                try {
                    startCompetition(input);
                    input = null;
                } catch (CompetitionInterrupter interrupter) {
                    input = interrupter.getInput();
                    Main.competitionMonsters = null;
                    Main.currentUser = null;
                }
            } else {
                if (input.equals(SHOW_MONSTERS_COMMAND)) {
                    showMonsters();
                } else if (command.equals(LOAD_COMMAND)) {
                    loadFile(input);
                } else {
                    System.out.println("Error, such command does not exist.");
                }
                input = null;
            }
            if (input == null || input.isEmpty()) {
                input = Main.getScanner().nextLine().trim();
            }
        }
    }
}