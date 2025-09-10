package lab_4.classes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamUtils {

    /**
     * Метод возвращает среднее значение списка целых чисел
     * @throws StreamExceptions.EmptyCollectionException если список пуст
     */
    public static double average(List<Integer> numbers) {
        if (numbers == null) {
            throw new StreamExceptions.InvalidInputException("Список не может быть null");
        }
        
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElseThrow(() -> new StreamExceptions.EmptyCollectionException(
                    "Невозможно вычислить среднее значение: список пуст"));
    }

    /**
     * Метод, приводящий все строки в списке в верхний регистр и добавляющий к ним префикс «_new_»
     * @throws StreamExceptions.InvalidInputException если список null или содержит null
     */
    public static List<String> toUpperCaseWithPrefix(List<String> strings) {
        if (strings == null) {
            throw new StreamExceptions.InvalidInputException("Список строк не может быть null");
        }
        
        return strings.stream()
                .map(s -> {
                    if (s == null) {
                        throw new StreamExceptions.InvalidInputException("Список содержит null элементы");
                    }
                    return s.toUpperCase();
                })
                .map(s -> "_new_" + s)
                .collect(Collectors.toList());
    }

    /**
     * Метод, возвращающий список квадратов всех встречающихся только один раз элементов списка
     */
    public static List<Integer> squaresOfUniqueElements(List<Integer> numbers) {
        if (numbers == null) {
            throw new StreamExceptions.InvalidInputException("Список не может быть null");
        }
        
        try {
            return numbers.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == 1)
                    .map(entry -> {
                        if (entry.getKey() == null) {
                            throw new StreamExceptions.InvalidInputException("Список содержит null элементы");
                        }
                        return entry.getKey() * entry.getKey();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new StreamExceptions.StreamProcessingException("Ошибка обработки уникальных элементов", e);
        }
    }

    /**
     * Метод, принимающий на вход коллекцию и возвращающий ее последний элемент
     * @throws StreamExceptions.NoSuchElementStreamException если коллекция пуста
     */
    public static <T> T getLastElement(Collection<T> collection) {
        if (collection == null) {
            throw new StreamExceptions.InvalidInputException("Коллекция не может быть null");
        }
        
        return collection.stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new StreamExceptions.NoSuchElementStreamException(
                    "Коллекция пуста, невозможно получить последний элемент"));
    }

    /**
     * Метод, принимающий на вход массив целых чисел, возвращающий сумму чётных чисел
     * или 0, если чётных чисел нет
     */
    public static int sumOfEvenNumbers(int[] numbers) {
        if (numbers == null) {
            throw new StreamExceptions.InvalidInputException("Массив не может быть null");
        }
        
        try {
            return IntStream.of(numbers)
                    .filter(n -> n % 2 == 0)
                    .sum();
        } catch (Exception e) {
            throw new StreamExceptions.StreamProcessingException("Ошибка вычисления суммы четных чисел", e);
        }
    }

    /**
     * Метод, преобразовывающий все строки в списке в Map, где первый символ – ключ,
     * оставшиеся – значение
     * @throws StreamExceptions.InvalidInputException если список содержит пустые строки или null
     */
    public static Map<Character, String> stringsToMap(List<String> strings) {
        if (strings == null) {
            throw new StreamExceptions.InvalidInputException("Список строк не может быть null");
        }
        
        return strings.stream()
                .filter(s -> {
                    if (s == null) {
                        throw new StreamExceptions.InvalidInputException("Список содержит null строки");
                    }
                    return !s.isEmpty();
                })
                .collect(Collectors.toMap(
                        s -> {
                            if (s.isEmpty()) {
                                throw new StreamExceptions.InvalidInputException("Обнаружена пустая строка");
                            }
                            return s.charAt(0);
                        },
                        s -> s.length() > 1 ? s.substring(1) : "",
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }
}