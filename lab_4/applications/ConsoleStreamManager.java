package lab_4.applications;

import java.util.*;
import java.util.stream.Collectors;

import lab_4.classes.StreamUtils;
import lab_4.classes.StreamExceptions;

public class ConsoleStreamManager {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        boolean running = true;
        
        while (running) {
            printMenu();
            int choice = getIntInput("Выберите операцию: ");
            
            switch (choice) {
                case 1 -> calculateAverage();
                case 2 -> processStringsWithPrefix();
                case 3 -> calculateSquaresOfUnique();
                case 4 -> getLastElement();
                case 5 -> sumEvenNumbers();
                case 6 -> convertStringsToMap();
                case 0 -> running = false;
                default -> {
                    System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
            
            if (running && choice != 0) {
                System.out.println("\nНажмите Enter для продолжения...");
                scanner.nextLine();
            }
        }
        
        System.out.println("Программа завершена.");
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== МЕНЮ ОПЕРАЦИЙ СО STREAM API ===");
        System.out.println("1. Вычислить среднее значение списка чисел");
        System.out.println("2. Преобразовать строки с префиксом");
        System.out.println("3. Квадраты уникальных элементов");
        System.out.println("4. Получить последний элемент коллекции");
        System.out.println("5. Сумма четных чисел массива");
        System.out.println("6. Преобразовать строки в Map");
        System.out.println("0. Выход");
        System.out.println("====================================");
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int result = Integer.parseInt(scanner.nextLine().trim());
                return result;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число!");
            }
        }
    }

    private static List<Integer> getIntegerList() {
        System.out.println("Введите числа через пробел:");
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            List<Integer> result = Arrays.stream(input.split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            return result;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите только числа!");
            return getIntegerList();
        }
    }

    private static List<String> getStringList() {
        System.out.println("Введите строки (каждую с новой строки, пустая строка для завершения):");
        List<String> strings = new ArrayList<>();
        
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }
            strings.add(line);
        }
        
        return strings;
    }

    private static int[] getIntArray() {
        System.out.println("Введите числа через пробел:");
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) {
            return new int[0];
        }
        
        try {
            int[] result = Arrays.stream(input.split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            return result;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите только числа!");
            return getIntArray();
        }
    }

    // Методы операций с использованием StreamUtils
    private static void calculateAverage() {
        System.out.println("\n=== ВЫЧИСЛЕНИЕ СРЕДНЕГО ЗНАЧЕНИЯ ===");
        List<Integer> numbers = getIntegerList();
        
        try {
            double average = StreamUtils.average(numbers);
            System.out.printf("Среднее значение: %.2f%n", average);
        } catch (StreamExceptions.EmptyCollectionException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (StreamExceptions.InvalidInputException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void processStringsWithPrefix() {
        System.out.println("\n=== ПРЕОБРАЗОВАНИЕ СТРОК С ПРЕФИКСОМ ===");
        List<String> strings = getStringList();
        
        try {
            List<String> result = StreamUtils.toUpperCaseWithPrefix(strings);
            System.out.println("Результат:");
            result.forEach(System.out::println);
        } catch (StreamExceptions.InvalidInputException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void calculateSquaresOfUnique() {
        System.out.println("\n=== КВАДРАТЫ УНИКАЛЬНЫХ ЭЛЕМЕНТОВ ===");
        List<Integer> numbers = getIntegerList();
        
        try {
            List<Integer> result = StreamUtils.squaresOfUniqueElements(numbers);
            System.out.println("Квадраты уникальных элементов: " + result);
        } catch (StreamExceptions.InvalidInputException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (StreamExceptions.StreamProcessingException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void getLastElement() {
        System.out.println("\n=== ПОЛУЧЕНИЕ ПОСЛЕДНЕГО ЭЛЕМЕНТА ===");
        List<String> strings = getStringList();
        
        try {
            String lastElement = StreamUtils.getLastElement(strings);
            System.out.println("Последний элемент: " + lastElement);
        } catch (StreamExceptions.NoSuchElementStreamException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (StreamExceptions.InvalidInputException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void sumEvenNumbers() {
        System.out.println("\n=== СУММА ЧЕТНЫХ ЧИСЕЛ ===");
        int[] numbers = getIntArray();
        
        try {
            int sum = StreamUtils.sumOfEvenNumbers(numbers);
            System.out.println("Сумма четных чисел: " + sum);
        } catch (StreamExceptions.InvalidInputException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (StreamExceptions.StreamProcessingException e) {
            System.out.println("Ошибка при вычислении суммы: " + e.getMessage());
        }
    }

    private static void convertStringsToMap() {
        System.out.println("\n=== ПРЕОБРАЗОВАНИЕ СТРОК В MAP ===");
        List<String> strings = getStringList();
        
        try {
            Map<Character, String> result = StreamUtils.stringsToMap(strings);
            System.out.println("Результат преобразования:");
            result.forEach((key, value) -> System.out.println(key + " -> " + value));
        } catch (StreamExceptions.InvalidInputException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при преобразовании: " + e.getMessage());
        }
    }
}