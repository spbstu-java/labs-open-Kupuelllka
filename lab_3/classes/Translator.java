package lab_3.classes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Translator {
    private static final Logger logger = Logger.getLogger(Translator.class.getName());
    private final String dictionaryPath;
    private Map<String, String> dictionary;
    private List<String> sortedKeys; // Ключи, отсортированные по длине (от большего к меньшему)

    public Translator(String dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
        logger.info("Создан Translator для словаря: " + dictionaryPath);
    }

    /**
     * Загрузка словаря из файла и подготовка данных для перевода
     */
    public void loadDictionary() throws IOException {
        logger.info("Начало загрузки словаря из файла: " + dictionaryPath);
        
        try {
            dictionary = new HashMap<>();
            List<String> lines = Files.readAllLines(Paths.get(dictionaryPath));
            
            for (String line : lines) {
                if (line.trim().isEmpty() || !line.contains("|")) {
                    continue;
                }
                
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim().toLowerCase();
                    String value = parts[1].trim();
                    dictionary.put(key, value);
                }
            }
            
            // Сортируем ключи по длине (от большего к меньшему) для greedy matching
            sortedKeys = new ArrayList<>(dictionary.keySet());
            sortedKeys.sort((a, b) -> Integer.compare(b.length(), a.length()));
            
            logger.info("Словарь успешно загружен. Записей: " + dictionary.size());
            logger.fine("Ключи отсортированы по длине для greedy matching");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при загрузке словаря из файла: " + dictionaryPath, e);
            throw new IOException("Ошибка загрузки словаря: " + e.getMessage(), e);
        }
    }

    /**
     * Перевод текста с использованием greedy matching
     */
    public String translateText(String text) {
        logger.info("Начало перевода текста: '" + text + "'");
        
        if (text == null) {
            logger.warning("Передан null вместо текста для перевода");
            throw new IllegalArgumentException("Текст не может быть null");
        }
        
        if (text.trim().isEmpty()) {
            logger.warning("Передан пустой текст для перевода");
            return "Введен пустой текст";
        }

        if (dictionary == null || dictionary.isEmpty()) {
            logger.severe("Словарь не загружен или пуст");
            throw new IllegalStateException("Словарь не загружен. Вызовите loadDictionary() сначала");
        }

        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < words.length) {
            String longestMatch = findLongestMatch(words, i);
            
            if (longestMatch != null) {
                // Найдено совпадение в словаре
                String translation = dictionary.get(longestMatch.toLowerCase());
                result.append(translation).append(" ");
                i += longestMatch.split("\\s+").length; // Пропускаем все слова совпадения
                logger.fine("Найдено совпадение: '" + longestMatch + "' -> '" + translation + "'");
            } else {
                // Совпадение не найдено, добавляем оригинальное слово
                result.append(words[i]).append(" ");
                i++;
                logger.fine("Совпадение не найдено для слова: '" + words[i-1] + "'");
            }
        }

        logger.info("Перевод завершен: '" + text + "' -> '" + result.toString().trim() + "'");
        return result.toString().trim();
    }

    /**
     * Поиск самого длинного совпадения начиная с позиции startIndex
     * Использует greedy matching для выбора варианта с максимальной длиной
     */
    private String findLongestMatch(String[] words, int startIndex) {
        if (startIndex >= words.length) {
            return null;
        }

        // Собираем возможные фразы начиная с startIndex
        StringBuilder currentPhrase = new StringBuilder();
        String bestMatch = null;

        for (int j = startIndex; j < words.length; j++) {
            if (j > startIndex) {
                currentPhrase.append(" ");
            }
            currentPhrase.append(words[j]);
            
            String currentPhraseStr = currentPhrase.toString().toLowerCase();
            
            // Проверяем, есть ли текущая фраза в словаре
            if (dictionary.containsKey(currentPhraseStr)) {
                // Нашли совпадение, сохраняем как лучшее (самое длинное)
                bestMatch = currentPhrase.toString();
            }
        }

        return bestMatch;
    }

    /**
     * Запуск интерактивного режима перевода
     */
    public void startInteractiveMode() {
        logger.info("Запуск интерактивного режима перевода");
        
        if (dictionary == null) {
            logger.severe("Попытка запуска интерактивного режима без загруженного словаря");
            System.err.println("Ошибка: словарь не загружен");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Переводчик (Greedy Matching) ===");
        System.out.println("Загружено выражений: " + dictionary.size());
        System.out.println("Правила перевода:");
        System.out.println("- Регистр букв игнорируется");
        System.out.println("- Выбирается вариант с максимальной длиной");
        System.out.println("- Неизвестные слова выводятся без изменений");
        System.out.println("Введите текст для перевода (или 'выход' для завершения):");
        System.out.println("======================================================");

        try {
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("выход") || input.equalsIgnoreCase("exit")) {
                    logger.info("Пользователь завершил работу переводчика");
                    System.out.println("Завершение работы переводчика...");
                    break;
                }

                if (input.isEmpty()) {
                    continue;
                }

                try {
                    String translation = translateText(input);
                    System.out.println("Перевод: " + translation);
                    System.out.println("---");
                    
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                    logger.warning("Ошибка ввода пользователя: " + e.getMessage());
                    
                } catch (Exception e) {
                    System.out.println("Неожиданная ошибка при переводе: " + e.getMessage());
                    logger.log(Level.SEVERE, "Неожиданная ошибка при переводе текста: " + input, e);
                }
            }
            
        } finally {
            scanner.close();
            logger.info("Интерактивный режим завершен");
        }
    }
    
    /**
     * Получение статистики словаря
     */
    public int getDictionarySize() {
        if (dictionary == null) {
            logger.warning("Попытка получить размер не загруженного словаря");
            return 0;
        }
        return dictionary.size();
    }

    /**
     * Проверка загрузки словаря
     */
    public boolean isDictionaryLoaded() {
        return dictionary != null && !dictionary.isEmpty();
    }
}