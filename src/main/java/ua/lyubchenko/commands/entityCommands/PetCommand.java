package ua.lyubchenko.commands.entityCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.lyubchenko.commands.ICommands;
import ua.lyubchenko.controllers.PetController;
import ua.lyubchenko.domains.ApiResponse;
import ua.lyubchenko.domains.Category;
import ua.lyubchenko.domains.Pet;
import ua.lyubchenko.domains.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;


public class PetCommand implements ICommands {
    private final Pet pet = new Pet();
    private final PetController petController = new PetController();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String URL_NAME = "https://petstore.swagger.io/v2/pet";
    private final List<String> commands = Arrays.asList("\"getPetById\"", "\"getPetsByStatus\"", "\"deletePetById\"",
            "\"updatePet\"", "\"createPet\"", "\"updatePetsStatus\"", "\"postUploadImage\"");


    @Override
    public void handle(String param, Consumer<ICommands> consumer) {
        String[] words = param.split(" ");
        String newParam = param.replace(words[0], "").trim();
        switch (words[0].toLowerCase(Locale.ROOT)) {
            case "getpetbyid":
                getPetById(newParam);
                return;
            case "getpetsbystatus":
                getPetsByStatus(newParam);
                return;
            case "deletepetbyid":
                deletePetById(newParam);
                return;
            case "updatepet":
                updatePet(newParam);
                return;
            case "createpet":
                createPet(newParam);
                return;
            case "updatepetsstatus":
                updatePetsStatus(newParam);
                return;
            case "postuploadimage":
                postUploadImage(newParam);
        }

    }

    private void postUploadImage(String param) {
        String[] words = param.split(" ");
        if (words.length != 3) {
            System.out.println("Вы ввели не достаточно или больше чем достаточно данных, повторите ваш ввод.");
            return;
        }
        ApiResponse apiResponse = petController.postUploadImage(URL_NAME, param);
        if (apiResponse.getCode() != 200) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        System.out.println(GSON.toJson(apiResponse));
    }


    private void updatePetsStatus(String param) {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)$")) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        String[] words = param.split(" ");
        pet.setId(Long.parseLong(words[0]));
        pet.setName(words[1]);
        pet.setStatus(words[2]);
        ApiResponse apiResponse = petController.updatePetsStatus(URL_NAME, pet);
        System.out.println(GSON.toJson(apiResponse));
    }

    private void createPet(String param) {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(.+)\\s{1}(\\d+)\\s{1}(\\w+)$")) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        String[] words = param.split(" ");
        pet.setId(Long.parseLong(words[0]));
        pet.setName(words[1]);
        pet.setCategory(new Category(Long.parseLong(words[2]), words[3]));
        pet.setStatus(words[4]);
        pet.setPhotoUrls(new String[]{words[5]});
        pet.setTags(new Tag[]{new Tag(Long.parseLong(words[6]), words[7])});
        Pet createdPet = petController.createPet(URL_NAME, pet);
        System.out.println(GSON.toJson(createdPet));
    }

    private void updatePet(String param) {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(.+)\\s{1}(\\d+)\\s{1}(\\w+)$")) {
            System.out.println("Вы ввели не корректные данные. Введите их еще раз.");
            return;
        }
        String[] words = param.split(" ");
        pet.setId(Long.parseLong(words[0]));
        pet.setName(words[1]);
        pet.setCategory(new Category(Long.parseLong(words[2]), words[3]));
        pet.setStatus(words[4]);
        pet.setPhotoUrls(new String[]{words[5]});
        pet.setTags(new Tag[]{new Tag(Long.parseLong(words[6]), words[7])});
        Pet updatedPet = petController.updatePet(URL_NAME, pet);
        System.out.println(GSON.toJson(updatedPet));

    }

    private void deletePetById(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Что-то пошло не так. Либо вы ввели букву, вам нужно вести цифру. Либо вы ничего не ввели, введите цифру");
            return;
        }
        ApiResponse apiResponse = petController.deletePetById(URL_NAME, String.valueOf(Integer.parseInt(firstWord.get())));
        if (apiResponse == null) {
            System.out.printf("Питомец под id %s не найден. Введите другой id.", firstWord.get());
            return;
        }
        System.out.printf("Питомец под id %s удален.\n", firstWord.get());
        System.out.println(GSON.toJson(apiResponse));

    }

    private void getPetsByStatus(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Что-то пошло не так. Либо вы ввели цифру, вам нужно вести статус. Либо вы ничего не ввели, введите статус.");
            return;
        }
        List<Pet> petsByStatus = petController.getPetsByStatus(URL_NAME, firstWord.get());
        petsByStatus.forEach(s -> System.out.println(GSON.toJson(s)));

    }

    private void getPetById(String param) {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Что-то пошло не так. Либо вы ввели букву, вам нужно вести цифру. Либо вы ничего не ввели, введите цифру.");
            return;
        }
        Pet petById = petController.getPetById(URL_NAME, String.valueOf(Integer.parseInt((firstWord.get()))));
        if (petById.getId() == null) {
            System.out.printf("Питомец под id %s не найден. Введите другой id.", firstWord.get());
            return;
        }
        System.out.println(GSON.toJson(petById));
    }


    @Override
    public void printInstruction() {
        System.out.println("Выберите из списка, что вам нужно - " + commands);
        System.out.println("Если вы выберите \"getPetById\" введите через пробел: getPetById (id - питомца которого хотите получить). Например: getPetById 1");
        System.out.println("Если вы выберите \"getPetsByStatus\" введите через пробел: getPetsByStatus (status - питомцев которых хотите получить)." +
                "У питомцев есть 3 статуса: {available, pending, sold}. Например: getPetsByStatus sold");
        System.out.println("Если вы выберите \"deletePetById\" введите через пробел: " +
                "deletePetById (id - питомца которого хотите удалить). Например: deletePetById 1");
        System.out.println("Если вы выберите \"updatePet\" введите через пробел: " +
                "updatePet (id - питомца которого хотите изменить) name categoryId categoryName petStatus photoUrl tagId tagName" +
                "\nНапример: updatePet 1 Max 1 dog available https://mypets24.com/11-malenkie-sobachki-porody-osobennosti-foto-i-ceny.html 1 sweet");
        System.out.println("Если вы выберите \"createPet\" введите через пробел: " +
                "createPet (id - питомца которого хотите создать) name categoryId categoryName petStatus photoUrl tagId tagName" +
                "\nНапример: createPet 1 Max 1 dog available https://mypets24.com/11-malenkie-sobachki-porody-osobennosti-foto-i-ceny.html 1 sweet");
        System.out.println("Если вы выберите \"updatePetsStatus\" введите через пробел: " +
                "updatePetsStatus (id - питомца которого хотите обновить) name petStatus" +
                " Например: updatePetsStatus 1 Max available");
        System.out.println("Если вы выберите \"postUploadImage\" введите через пробел: " +
                "postUploadImage (id - питомца которому хотите загрузить файл)" +
                " Например: postUploadImage 1 MyPet https://www.meme-arsenal.com/create/template/1650250");
    }

}
