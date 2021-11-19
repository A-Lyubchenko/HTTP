package ua.lyubchenko;


import ua.lyubchenko.commands.Command;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Command command = new Command();
        while (scanner.hasNext()) {
            command.getMainCommand(scanner.nextLine());
        }

    }
}
