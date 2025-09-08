package lab_3.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Translator {
    private static final Logger logger = Logger.getLogger(Translator.class.getName());
    private final DictionaryFileReader dictionaryReader;
    private final String dictionaryPath;
    private Map<String, String> dictionary;

    public Translator(String dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
        this.dictionaryReader = new DictionaryFileReader(dictionaryPath);
        logger.info("Создан Translator для словаря: " + dictionaryPath);
    }

    /**
     * Загрузка словаря из файла
     */
    public void loadDictionary() throws IOException {
        logger.info("Начало загрузки словаря из файла: " + dictionaryPath);
        
        try {
            dictionary = dictionaryReader.readDictionary();
            logger.info("Словарь успешно загружен. Записей: " + dictionary.size());
            
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Файл словаря не найден: " + dictionaryPath, e);
            throw new IOException("Файл словаря не найден: " + dictionaryPath, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при загрузке словаря из файла: " + dictionaryPath, e);
            throw new IOException("Ошибка загрузки словаря: " + e.getMessage(), e);
        }
    }

    /**
     * Перевод текста
     */
    public String translateText(String text) throws IOException {
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

        String[] words = text.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        int translatedWords = 0;
        int unknownWords = 0;

        for (String word : words) {
            String cleanedWord = cleanWord(word);
            if (cleanedWord.isEmpty()) {
                continue;
            }
            
            String translation = translateWord(cleanedWord);
            if (translation.equals("[" + cleanedWord + "]")) {
                unknownWords++;
            } else {
                translatedWords++;
            }
            result.append(translation).append(" ");
        }

        logger.info(String.format("Перевод завершен. Переведено слов: %d, неизвестных слов: %d", 
            translatedWords, unknownWords));
        
        return result.toString().trim();
    }

    /**
     * Очистка слова от знаков препинания
     */
    private String cleanWord(String word) {
        String cleaned = word.replaceAll("[^a-zA-Zа-яА-Я0-9]", "").toLowerCase();
        logger.fine("Очистка слова: '" + word + "' -> '" + cleaned + "'");
        return cleaned;
    }

    /**
     * Перевод одного слова
     */
    private String translateWord(String word) {
        if (word.isEmpty()) {
            return "";
        }

        // Ищем точное совпадение
        String translation = dictionary.get(word);
        if (translation != null) {
            logger.fine("Найден точный перевод для '" + word + "': '" + translation + "'");
            return translation;
        }

        // Ищем совпадение без учета регистра
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(word)) {
                logger.fine("Найден перевод без учета регистра для '" + word + "': '" + entry.getValue() + "'");
                return entry.getValue();
            }
        }

        // Если перевод не найден
        logger.warning("Перевод для слова '" + word + "' не найден в словаре");
        return "[" + word + "]";
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

        System.out.println("=== Переводчик ===");
        System.out.println("Загружено слов: " + dictionary.size());
        System.out.println("Введите текст для перевода (или 'выход' для завершения):");
        System.out.println("======================================================");

        int translationCount = 0;
        
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
                    translationCount++;
                    
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                    logger.warning("Ошибка ввода пользователя: " + e.getMessage());
                    
                } catch (IOException e) {
                    System.out.println("Ошибка перевода: " + e.getMessage());
                    logger.log(Level.SEVERE, "Ошибка при переводе текста: " + input, e);
                    
                } catch (Exception e) {
                    System.out.println("Неожиданная ошибка при переводе");
                    logger.log(Level.SEVERE, "Неожиданная ошибка при переводе текста: " + input, e);
                }
            }
            
        } finally {
            scanner.close();
            logger.info("Интерактивный режим завершен. Выполнено переводов: " + translationCount);
        }
    }

    /**
     * Статический метод для быстрого перевода
     */
    public static String quickTranslate(String dictionaryPath, String text) throws IOException {
        logger.info("Быстрый перевод текста: '" + text + "' с использованием словаря: " + dictionaryPath);
        
        if (text == null || text.trim().isEmpty()) {
            logger.warning("Пустой текст для быстрого перевода");
            throw new IllegalArgumentException("Текст не может быть пустым");
        }

        Translator translator = new Translator(dictionaryPath);
        translator.loadDictionary();
        
        String translation = translator.translateText(text);
        logger.info("Быстрый перевод завершен: '" + text + "' -> '" + translation + "'");
        
        return translation;
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