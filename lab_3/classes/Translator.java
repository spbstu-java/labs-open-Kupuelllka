package lab_3.classes;

import java.util.*;
import java.util.logging.Level;

public class Translator {
    private final String dictionaryPath;
    private final DictionaryFileReader dictionaryReader;
    private Map<String, String> dictionary;
    private List<String> sortedKeys;

    public Translator(String dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
        this.dictionaryReader = new DictionaryFileReader(dictionaryPath);
        AppLogger.info("Создан Translator для словаря: " + dictionaryPath);
    }

    /**
     * Загрузка словаря из файла и подготовка данных для перевода
     */
    public void loadDictionary() throws Exceptions.DictionaryLoadException {
        AppLogger.info("Начало загрузки словаря из файла: " + dictionaryPath);
        
        try {
            dictionary = dictionaryReader.readDictionary();
            
            // Сортируем ключи по длине (от большего к меньшему) для greedy matching
            sortedKeys = new ArrayList<>(dictionary.keySet());
            sortedKeys.sort((a, b) -> Integer.compare(b.length(), a.length()));
            
            AppLogger.info("Словарь успешно загружен. Записей: " + dictionary.size());
            AppLogger.fine("Ключи отсортированы по длине для greedy matching");
            
        } catch (Exceptions.DictionaryLoadException e) {
            AppLogger.log(Level.SEVERE, "Ошибка при загрузке словаря из файла: " + dictionaryPath, e);
            throw new Exceptions.DictionaryLoadException("Ошибка загрузки словаря: " + e.getMessage(), e);
        }
    }

    /**
     * Перевод текста с использованием greedy matching
     */
    public String translateText(String text) throws Exceptions.TranslationException {
        AppLogger.info("Начало перевода текста: '" + text + "'");
        
        if (text == null) {
            AppLogger.warning("Передан null вместо текста для перевода");
            throw new Exceptions.InvalidInputException("Текст не может быть null");
        }
        
        if (text.trim().isEmpty()) {
            AppLogger.warning("Передан пустой текст для перевода");
            return "Введен пустой текст";
        }

        if (dictionary == null || dictionary.isEmpty()) {
            AppLogger.severe("Словарь не загружен или пуст");
            throw new Exceptions.TranslationException("Словарь не загружен. Вызовите loadDictionary() сначала");
        }

        try {
            String[] words = text.split("\\s+");
            StringBuilder result = new StringBuilder();
            int i = 0;

            while (i < words.length) {
                String longestMatch = findLongestMatch(words, i);
                
                if (longestMatch != null) {
                    String translation = dictionary.get(longestMatch.toLowerCase());
                    result.append(translation).append(" ");
                    i += longestMatch.split("\\s+").length;
                    AppLogger.fine("Найдено совпадение: '" + longestMatch + "' -> '" + translation + "'");
                } else {
                    result.append(words[i]).append(" ");
                    i++;
                    AppLogger.fine("Совпадение не найдено для слова: '" + words[i-1] + "'");
                }
            }

            AppLogger.info("Перевод завершен: '" + text + "' -> '" + result.toString().trim() + "'");
            return result.toString().trim();
            
        } catch (Exception e) {
            AppLogger.log(Level.SEVERE, "Ошибка при переводе текста: " + text, e);
            throw new Exceptions.TranslationException("Ошибка перевода: " + e.getMessage(), e);
        }
    }

    /**
     * Поиск самого длинного совпадения
     */
    private String findLongestMatch(String[] words, int startIndex) {
        if (startIndex >= words.length) {
            return null;
        }

        StringBuilder currentPhrase = new StringBuilder();
        String bestMatch = null;

        for (int j = startIndex; j < words.length; j++) {
            if (j > startIndex) {
                currentPhrase.append(" ");
            }
            currentPhrase.append(words[j]);
            
            String currentPhraseStr = currentPhrase.toString().toLowerCase();
            
            if (dictionary.containsKey(currentPhraseStr)) {
                bestMatch = currentPhrase.toString();
            }
        }

        return bestMatch;
    }

    /**
     * Запуск интерактивного режима перевода
     */
    public void startInteractiveMode() {
        AppLogger.info("Запуск интерактивного режима перевода");
        
        if (dictionary == null) {
            AppLogger.severe("Попытка запуска интерактивного режима без загруженного словаря");
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
                    AppLogger.info("Пользователь завершил работу переводчика");
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
                    
                } catch (Exceptions.InvalidInputException e) {
                    System.out.println("Ошибка ввода: " + e.getMessage());
                    AppLogger.warning("Ошибка ввода пользователя: " + e.getMessage());
                    
                } catch (Exceptions.TranslationException e) {
                    System.out.println("Ошибка перевода: " + e.getMessage());
                    AppLogger.log(Level.SEVERE, "Ошибка при переводе текста: " + input, e);
                    
                } catch (Exception e) {
                    System.out.println("Неожиданная ошибка при переводе");
                    AppLogger.log(Level.SEVERE, "Неожиданная ошибка при переводе текста: " + input, e);
                }
            }
            
        } finally {
            scanner.close();
            AppLogger.info("Интерактивный режим завершен");
        }
    }


    public int getDictionarySize() {
        return dictionary != null ? dictionary.size() : 0;
    }

    public boolean isDictionaryLoaded() {
        return dictionary != null && !dictionary.isEmpty();
    }
}