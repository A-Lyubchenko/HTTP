package ua.lyubchenko.commands;

import java.util.regex.Matcher;

import static ua.lyubchenko.commands.ICommands.pattern;

public class Command {
    private final ICommands iCommands = new MainMenu();
    private ICommands activeCommand = iCommands;

    public Command() {
        System.out.println("Здравствуйте, выберите одну из команд: \"start\" - войти в магазин \"Питомец\", " +
                "\"main\" - получить главное меню магазина \"Питомца\", \"finish\" - для выхода из программы.");
    }

    public void getMainCommand(String params) {
        Matcher firstWord = pattern.matcher(params);
        if (firstWord.find()) {
            String group = firstWord.group();
            switch (group) {

                case "start":

                case "main":
                    activeCommand = iCommands;
                    activeCommand.printInstruction();
                    return;

                case "finish":
                    System.out.println("До свидание!");
                    System.exit(0);
                    return;

                default:
                    activeCommand.handle(params, iCommands1 -> {
                        activeCommand = iCommands1;
                        activeCommand.printInstruction();
                    });

            }

        }
    }
}
