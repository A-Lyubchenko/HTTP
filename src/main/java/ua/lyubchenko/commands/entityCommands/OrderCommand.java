package ua.lyubchenko.commands.entityCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.lyubchenko.commands.ICommands;
import ua.lyubchenko.controllers.OrderController;

import ua.lyubchenko.domains.ApiResponse;
import ua.lyubchenko.domains.Order;


import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class OrderCommand implements ICommands {
    private final Order order = new Order();
    private final OrderController orderController = new OrderController();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String URL_NAME = "https://petstore.swagger.io/v2/store";
    private final List<String> commands = Arrays.asList("\"getInventory\"", "\"getOrderByPetsId\"", "\"createOrder\"",
            "\"deleteOrderById\"");

    @Override
    public void handle(String param, Consumer<ICommands> consumer) {
        String[] words = param.split(" ");
        String newParam = param.replace(words[0], "").trim();
        switch (words[0].toLowerCase(Locale.ROOT)) {
            case "getinventory":
                getInventory(newParam);
                return;
            case "getorderbypetsid":
                getOrderByPetsId(newParam);
                return;
            case "createorder":
                createOrder(newParam);
                return;
            case "deleteorderbyid":
                deleteOrderById(newParam);
        }
    }

    private void deleteOrderById(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Что-то пошло не так. Либо вы ввели букву, вам нужно вести цифру. Либо вы ничего не ввели, введите цифру");
            return;
        }
        ApiResponse apiResponse = orderController.deleteOrderById(URL_NAME, String.valueOf(Integer.parseInt(param)));
        if (apiResponse.getCode() != 200) {
            System.out.printf("Заказ под id %s не найден. Введите другой id.", param);
            return;
        }

        System.out.printf("Заказ под id %s удален.\n", param);
        System.out.println(GSON.toJson(apiResponse));

    }


    private void createOrder(String param) {
        if (!param.matches("^(\\d+)\\s{1}(\\d+)\\s{1}(\\d+)\\s{1}(\\d{4}-\\d{2}-\\d{2})\\s{1}(\\w+)\\s{1}(\\w+)$")) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        String[] words = param.split(" ");
        order.setId(Long.valueOf(words[0]));
        order.setPetId(Long.parseLong(words[1]));
        order.setQuantity(Long.parseLong(words[2]));
        order.setShipDate(words[3]);
        order.setStatus(words[4]);
        order.setComplete(Boolean.parseBoolean(words[5]));
        Order createdOrder = orderController.placeAnOrderForAPet(URL_NAME, order);
        System.out.println(GSON.toJson(createdOrder));

    }

    private void getOrderByPetsId(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Что-то пошло не так. Либо вы ввели букву, вам нужно вести цифру. Либо вы ничего не ввели, введите цифру");
            return;
        }
        Order orderByPetsId = orderController.getOrderByPetsId(URL_NAME, String.valueOf(Integer.parseInt(firstWord.get())));
        if (orderByPetsId.getId() == null) {
            System.out.printf("Заказ под id %s не найден. Введите другой id заказа.", firstWord.get());
            return;
        }
        System.out.println("Ваш заказ.");
        System.out.println(GSON.toJson(orderByPetsId));
    }


    private void getInventory(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Что-то пошло не так. Либо вы ввели цифру, вам нужно вести inventory. Либо вы ничего не ввели, введите inventory");
            return;
        }
        String storeInventory = orderController.getStoreInventory(URL_NAME, firstWord.get());
        System.out.println(GSON.toJson(storeInventory));

    }


    @Override
    public void printInstruction() {
        System.out.println("Выберите из списка, что вам нужно - " + commands);
        System.out.println("Если вы выберите \"getInventory\" введите через пробел: getInventory inventory. Например: getInventory inventory");
        System.out.println("Если вы выберите \"getOrderByPetsId\" введите через пробел: getOrderByPetsId (id - питомца). Например: getOrderByPetsId 1");
        System.out.println("Если вы выберите \"createOrder\" введите через пробел: createOrder (id - заказа) (id - питомца)" +
                " quantity shipDate status complete.\nНапример: createOrder 1 1 3 2021-11-16 placed true. " +
                "У заказов есть 3 вида статуса: {approved, placed, delivered}.");
        System.out.println("Если вы выберите \"deleteOrderById\" введите через пробел: " +
                "deleteOrderById (id - питомца которого хотите получить). Например: deleteOrderById 1");
    }
}

