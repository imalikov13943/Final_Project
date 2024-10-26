import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 1. Родительский класс Животное
abstract class Animal {
    protected String name;
    protected String birthDate;
    protected String gender;
    protected List<String> commands = new ArrayList<>();

    public Animal(String name, String birthDate, String gender) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public abstract String getType();

    public void addCommand(String command) {
        commands.add(command);
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate; 
    }

    public abstract String getCategory(); 
}

// 2. Классы домашних животных
class Dog extends Animal {
    public Dog(String name, String birthDate, String gender) {
        super(name, birthDate, gender);
    }

    @Override
    public String getType() {
        return "Собака";
    }

    @Override
    public String getCategory() {
        return "Домашнее"; 
    }
}

class Cat extends Animal {
    public Cat(String name, String birthDate, String gender) {
        super(name, birthDate, gender);
    }

    @Override
    public String getType() {
        return "Кошка";
    }

    @Override
    public String getCategory() {
        return "Домашнее";
    }
}

class Hamster extends Animal {
    public Hamster(String name, String birthDate, String gender) {
        super(name, birthDate, gender);
    }

    @Override
    public String getType() {
        return "Хомяк";
    }

    @Override
    public String getCategory() {
        return "Домашнее"; 
    }
}

// 3. Классы вьючных животных
class Horse extends Animal {
    public Horse(String name, String birthDate, String gender) {
        super(name, birthDate, gender);
    }

    @Override
    public String getType() {
        return "Лошадь";
    }

    @Override
    public String getCategory() {
        return "Вьючее"; 
    }
}

class Camel extends Animal {
    public Camel(String name, String birthDate, String gender) {
        super(name, birthDate, gender);
    }

    @Override
    public String getType() {
        return "Верблюд";
    }

    @Override
    public String getCategory() {
        return "Вьючее"; 
    }
}

class Donkey extends Animal {
    public Donkey(String name, String birthDate, String gender) {
        super(name, birthDate, gender);
    }

    @Override
    public String getType() {
        return "Осел";
    }

    @Override
    public String getCategory() {
        return "Вьючее"; 
    }
}

// 4. Класс Счетчик для контроля количества созданных животных
class AnimalCounter implements AutoCloseable {
    private static int count = 0;
    private boolean closed = false;

    public void add() {
        if (closed) {
            throw new IllegalStateException("Ресурс уже закрыт.");
        }
        count++;
        System.out.println("Новое животное добавлено. Общее количество: " + count);
    }

    public static int getCount() {
        return count;
    }

    @Override
    public void close() {
        closed = true;
        System.out.println("Ресурс счетчика закрыт.");
    }
}

// 5. Программа реестра животных с меню
public class AnimalRegistry {
    private List<Animal> animals = new ArrayList<>();

    public void addAnimal(Animal animal) {
        animals.add(animal);
        System.out.println("Добавлено новое " + animal.getType() + ": " + animal.getName() + " (Пол: " + animal.getGender() + ")");
    }

    public void listCommands(Animal animal) {
        System.out.println("Команды для " + animal.getName() + " (Пол: " + animal.getGender() + ", Год рождения: " + animal.getBirthDate() + "): " + animal.getCommands());
    }

    public void trainAnimal(Animal animal, String command) {
        animal.addCommand(command);
        System.out.println("Обучено " + animal.getName() + " новой команде: " + command);
    }

    public void viewAnimals() {
        if (animals.isEmpty()) {
            System.out.println("Нет добавленных животных.");
            return;
        }

        System.out.println("Список животных по категориям и полу:");
        // Группируем животных по категориям
        animals.stream()
            .sorted((a1, a2) -> a1.getCategory().compareTo(a2.getCategory())) 
            .forEach(animal -> System.out.println("Категория: " + animal.getCategory() +
                    ", Вид: " + animal.getType() +
                    ", Имя: " + animal.getName() +
                    ", Пол: " + animal.getGender() +
                    ", Год рождения: " + animal.getBirthDate())); 
    }

    public void trainAnimalByName(String name, String command) {
        List<Animal> foundAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                foundAnimals.add(animal);
            }
        }

        if (foundAnimals.isEmpty()) {
            System.out.println("Животное не найдено.");
            return;
        }

        if (foundAnimals.size() > 1) {
            System.out.println("Найдено несколько животных с именем '" + name + "':");
            for (int i = 0; i < foundAnimals.size(); i++) {
                Animal animal = foundAnimals.get(i);
                System.out.println((i + 1) + ". " + animal.getType() + " (Пол: " + animal.getGender() + ", Год рождения: " + animal.getBirthDate() + ")");
            }
            System.out.print("Выберите номер животного для тренировки: ");
            Scanner scanner = new Scanner(System.in);
            int index = scanner.nextInt() - 1;
            if (index >= 0 && index < foundAnimals.size()) {
                Animal selectedAnimal = foundAnimals.get(index);
                trainAnimal(selectedAnimal, command);
            } else {
                System.out.println("Неверный номер. Тренировка не выполнена.");
            }
        } else {
            trainAnimal(foundAnimals.get(0), command);
        }
    }

    public void showMenu() {
        System.out.println("\nМеню реестра животных:");
        System.out.println("1. Добавить новое животное");
        System.out.println("2. Показать команды животного");
        System.out.println("3. Тренировать животное с новой командой");
        System.out.println("4. Просмотреть всех животных");
        System.out.println("5. Выйти");
    }

    public static void main(String[] args) {
        AnimalRegistry registry = new AnimalRegistry();

        try (AnimalCounter counter = new AnimalCounter()) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                registry.showMenu();
                System.out.print("Выберите вариант: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        System.out.print("Введите тип животного (Собака, Кошка, Хомяк, Лошадь, Верблюд, Осел): ");
                        String type = scanner.nextLine();
                        System.out.print("Введите имя: ");
                        String name = scanner.nextLine();
                        System.out.print("Введите дату рождения (ГГГГ-ММ-ДД): ");
                        String birthDate = scanner.nextLine();
                        System.out.print("Введите пол (мужской/женский): ");
                        String gender = scanner.nextLine();

                        Animal newAnimal;
                        switch (type.toLowerCase()) {
                            case "собака":
                                newAnimal = new Dog(name, birthDate, gender);
                                break;
                            case "кошка":
                                newAnimal = new Cat(name, birthDate, gender);
                                break;
                            case "хомяк":
                                newAnimal = new Hamster(name, birthDate, gender);
                                break;
                            case "лошадь":
                                newAnimal = new Horse(name, birthDate, gender);
                                break;
                            case "верблюд":
                                newAnimal = new Camel(name, birthDate, gender);
                                break;
                            case "осел":
                                newAnimal = new Donkey(name, birthDate, gender);
                                break;
                            default:
                                System.out.println("Неизвестный тип животного.");
                                continue;
                        }

                        registry.addAnimal(newAnimal);
                        counter.add(); 
                        break;

                    case 2:
                        System.out.print("Введите имя животного для показа команд: ");
                        String showCommandsAnimalName = scanner.nextLine();
                        List<Animal> foundAnimalsForCommands = new ArrayList<>();
                        for (Animal animal : registry.animals) {
                            if (animal.getName().equalsIgnoreCase(showCommandsAnimalName)) {
                                foundAnimalsForCommands.add(animal);
                            }
                        }
                        if (foundAnimalsForCommands.isEmpty()) {
                            System.out.println("Животное не найдено.");
                        } else {
                            if (foundAnimalsForCommands.size() > 1) {
                                System.out.println("Найдено несколько животных с именем '" + showCommandsAnimalName + "':");
                                for (int i = 0; i < foundAnimalsForCommands.size(); i++) {
                                    Animal animal = foundAnimalsForCommands.get(i);
                                    System.out.println((i + 1) + ". " + animal.getType() + " (Пол: " + animal.getGender() + ", Год рождения: " + animal.getBirthDate() + ")");
                                }
                                System.out.print("Выберите номер животного для показа команд: ");
                                int index = scanner.nextInt() - 1;
                                if (index >= 0 && index < foundAnimalsForCommands.size()) {
                                    registry.listCommands(foundAnimalsForCommands.get(index));
                                } else {
                                    System.out.println("Неверный номер.");
                                }
                            } else {
                                registry.listCommands(foundAnimalsForCommands.get(0));
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Введите имя животного для тренировки: ");
                        String trainAnimalName = scanner.nextLine();
                        System.out.print("Введите новую команду: ");
                        String command = scanner.nextLine();
                        registry.trainAnimalByName(trainAnimalName, command);
                        break;

                    case 4:
                        registry.viewAnimals();
                        break;

                    case 5:
                        exit = true;
                        break;

                    default:
                        System.out.println("Неверный вариант. Пожалуйста, выберите снова.");
                }
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
