package ua.lyubchenko.commands;

import ua.lyubchenko.commands.entityCommands.PetCommand;
import ua.lyubchenko.commands.entityCommands.OrderCommand;
import ua.lyubchenko.commands.entityCommands.UserCommand;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MainMenu implements ICommands {

    private final Map<String, ICommands> iCommandsMap = Map.of(
            "pet", new PetCommand(),
            "store", new OrderCommand(),
            "user", new UserCommand()
    );

    @Override
    public void handle(String param, Consumer<ICommands> consumer) {
        Optional<String> firstWord = getFirstWord(param);
        firstWord.map(iCommandsMap::get).ifPresent(iCommands -> {
            consumer.accept(iCommands);
            iCommands.handle(param.replace(firstWord.get(), "").trim(), consumer);
        });

    }

    @Override
    public void printInstruction() {
        System.out.println("Вы находитесь в Интернет магазине \"Питомец\". Введите из предлагающего списка," +
                "с какой сущностью вы хотите работать - [\"pet\", \"store\", \"user\"]");

    }
}
