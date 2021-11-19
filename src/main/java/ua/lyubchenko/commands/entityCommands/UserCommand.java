package ua.lyubchenko.commands.entityCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.lyubchenko.commands.ICommands;
import ua.lyubchenko.controllers.UserController;
import ua.lyubchenko.domains.ApiResponse;
import ua.lyubchenko.domains.User;

import java.util.*;
import java.util.function.Consumer;

public class UserCommand implements ICommands {
    private final User user = new User();
    private final UserController userController = new UserController();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String URL_NAME = "https://petstore.swagger.io/v2/user";
    private final List<String> commands = Arrays.asList("\"getByUserName\"", "\"logUser\"", "\"logOut\"",
            "\"createWithList\"", "\"createUser\"", "\"updateUser\"", "\"deleteUser\"");

    @Override
    public void handle(String param, Consumer<ICommands> consumer) {
        String[] words = param.split(" ");
        String newParam = param.replace(words[0], "").trim();
        switch (words[0].toLowerCase(Locale.ROOT)) {
            case "getbyusername":
                getByUserName(newParam);
                return;
            case "loguser":
                logUser(newParam);
                return;
            case "logout":
                logOut();
                return;
            case "createwithlist":
                createUserWithList(newParam);
                return;
            case "createuser":
                createUser(newParam);
                return;
            case "updateuser":
                updateUser(newParam);
                return;
            case "deleteuser":
                deleteUser(newParam);


        }
    }

    private void deleteUser(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty()) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        ApiResponse apiResponse = userController.deleteUser(URL_NAME, param);
        if (apiResponse == null) {
            System.out.println("User c таким userName не существует. Введите другие данные.");
            return;
        }
        System.out.println(GSON.toJson(apiResponse));
    }

    private void updateUser(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty()) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        user.setUsername(firstWord.get());
        ApiResponse apiResponse = userController.updateUser(URL_NAME, user);
        if (apiResponse.getCode() != 200) {
            System.out.println("User c таким userName не существует. Введите другие данные.");
            return;
        }
        System.out.println(GSON.toJson(apiResponse));
    }

    private void createUser(String param) {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(.+)\\s{1}(.+)\\s{1}(\\d+)\\s{1}(\\d+)$")) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        String[] words = param.split(" ");
        user.setId(Long.valueOf(words[0]));
        user.setUsername(words[1]);
        user.setFirstName(words[2]);
        user.setLastName(words[3]);
        user.setEmail(words[4]);
        user.setPassword(words[5]);
        user.setPhone(words[6]);
        user.setUserStatus(Integer.parseInt(words[7]));
        ApiResponse withList = userController.createUser(URL_NAME, user);
        if (withList.getCode() != 200) {
            System.out.println("User под такими данными уже существует. Введите другие данные.");
            return;
        }
        System.out.println("User создался успешно.");
        System.out.println(GSON.toJson(withList));
    }

    private void createUserWithList(String param) {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(.+)\\s{1}(.+)\\s{1}(\\d+)\\s{1}(\\d+)$")) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        List<User> users = new ArrayList<>();
        String[] words = param.split(" ");
        user.setId(Long.valueOf(words[0]));
        user.setUsername(words[1]);
        user.setFirstName(words[2]);
        user.setLastName(words[3]);
        user.setEmail(words[4]);
        user.setPassword(words[5]);
        user.setPhone(words[6]);
        user.setUserStatus(Integer.parseInt(words[7]));
        users.add(user);
        ApiResponse withList = userController.createUserWithList(URL_NAME, users);
        if (withList.getCode() != 200) {
            System.out.println("User под такими данными уже существует. Введите другие данные.");
            return;
        }
        System.out.println("User создался успешно.");
        System.out.println(GSON.toJson(withList));
    }

    private void logOut() {
        ApiResponse logOut = userController.getLogOut(URL_NAME);
        System.out.println(GSON.toJson(logOut));
    }

    private void logUser(String param) {
        if (!param.matches("^(\\w+)\\s{1}(.+)$")) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        ApiResponse apiResponse = userController.logsUser(URL_NAME, param);
        if (apiResponse.getCode() != 200) {
            System.out.println("User под такими данными уже существует. Введите другие данные.");
            return;
        }
        System.out.println(GSON.toJson(apiResponse));
    }


    private void getByUserName(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty()) {
            System.out.println("Что-то пошло не так. Вы ничего не ввели, введите userName");
            return;
        }
        user.setUsername(firstWord.get());
        User byUserName = userController.getByUserName(URL_NAME, firstWord.get());
        if (byUserName.getId() == null) {
            System.out.printf("User под именем %s не найден. Введите другое имя user.", firstWord.get());
            return;
        }
        System.out.println(GSON.toJson(byUserName));
    }

    @Override
    public void printInstruction() {
        System.out.println("Выберите из списка, что вам нужно - " + commands);
        System.out.println("Если вы выберите \"getByUserName\" введите через пробел: getByUserName userName. Например: getByUserName user1");
        System.out.println("Если вы выберите \"logUser\" введите через пробел: logUser userName password. Например: logUser Ketty 123");
        System.out.println("Если вы выберите \"logOut\" введите: logOut. Например: logOut");
        System.out.println("Если вы выберите \"createWithList\" введите: createWithList id userName firstName lastName email password phone userStatus." +
                " Например: createWithList 1 Ketty Mikky Mouse mouse@gmail.com 123 123456789 1");
        System.out.println("Если вы выберите \"createUser\" введите: createUser id userName firstName lastName email password phone userStatus." +
                " Например: createUser 1 Ketty Mikky Mouse mouse@gmail.com 123 123456789 1");
        System.out.println("Если вы выберите \"updateUser\" введите: updateUser userName." +
                " Например: updateUser Ketty");
        System.out.println("Если вы выберите \"deleteUser\" введите: deleteUser userName." +
                " Например: deleteUser Ketty");
    }
}
