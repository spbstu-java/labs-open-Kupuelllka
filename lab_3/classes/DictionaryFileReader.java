package lab_3.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DictionaryFileReader {
    private static final Logger logger = Logger.getLogger(DictionaryFileReader.class.getName());
    private String filePath;

    public DictionaryFileReader(String filePath) {
        this.filePath = filePath;
        logger.info("Создан DictionaryFileReader для файла: " + filePath);
    }

    /**
     * Проверка существования файла
     */
    private void checkFileExists() throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            String errorMsg = "Файл не найден: " + filePath + 
                            ". Абсолютный путь: " + file.getAbsolutePath();
            logger.severe(errorMsg);
            throw new FileNotFoundException(errorMsg);
        }
        if (!file.isFile()) {
            String errorMsg = "Указанный путь не является файлом: " + filePath;
            logger.severe(errorMsg);
            throw new FileNotFoundException(errorMsg);
        }
        if (!file.canRead()) {
            String errorMsg = "Нет прав на чтение файла: " + filePath;
            logger.severe(errorMsg);
            throw new FileNotFoundException(errorMsg);
        }
    }

    /**
     * Чтение всего словаря из файла
     */
    public Map<String, String> readDictionary() throws IOException {
        logger.info("Начало чтения словаря из файла: " + filePath);
        
        // Проверяем существование файла перед чтением
        checkFileExists();
        
        Map<String, String> dictionary = new HashMap<>();
        int lineNumber = 0;
        int successfulEntries = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                processDictionaryLine(line, lineNumber, dictionary);
            }
            
            successfulEntries = dictionary.size();
            logger.info(String.format("Успешно прочитано %d записей из %d строк файла", 
                successfulEntries, lineNumber));
            
        } catch (FileNotFoundException e) {
            // Эта ошибка уже обработана в checkFileExists, но на всякий случай
            logger.log(Level.SEVERE, "Файл не найден: " + filePath, e);
            throw new FileNotFoundException("Файл словаря не найден: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при чтении словаря из файла: " + filePath, e);
            throw new IOException("Ошибка чтения файла словаря: " + e.getMessage(), e);
        }

        if (dictionary.isEmpty()) {
            logger.warning("Словарь пуст или не содержит валидных записей");
            throw new IOException("Файл не содержит валидных записей словаря");
        }

        return dictionary;
    }

    /**
     * Обработка одной строки словаря
     */
    private void processDictionaryLine(String line, int lineNumber, Map<String, String> dictionary) {
        if (line == null || line.trim().isEmpty()) {
            logger.fine("Пропущена пустая строка №" + lineNumber);
            return;
        }

        String trimmedLine = line.trim();
        String[] parts = trimmedLine.split("\\|", 2);
        
        if (parts.length < 2) {
            logger.warning("Неверный формат в строке " + lineNumber + ": '" + trimmedLine + "'");
            return;
        }

        String word = parts[0].trim();
        String translation = parts[1].trim();

        if (word.isEmpty() || translation.isEmpty()) {
            logger.warning("Пустое слово или перевод в строке " + lineNumber + ": '" + trimmedLine + "'");
            return;
        }

        if (dictionary.containsKey(word)) {
            logger.warning("Дублирующееся слово '" + word + "' в строке " + lineNumber);
        }
        
        dictionary.put(word, translation);
        logger.fine("Добавлена запись: '" + word + "' -> '" + translation + "'");
    }

    public String readWordTranslation(String word) throws IOException {
        logger.info("Поиск перевода для слова: '" + word + "'");
        
        if (word == null || word.trim().isEmpty()) {
            logger.warning("Передано пустое слово для поиска");
            throw new IllegalArgumentException("Слово не может быть пустым");
        }

        String searchWord = word.trim();
        Map<String, String> dictionary = readDictionary();
        
        String translation = dictionary.get(searchWord);
        
        if (translation == null) {
            logger.warning("Перевод для слова '" + searchWord + "' не найден в словаре");
            throw new IOException("Слово '" + searchWord + "' не найдено в словаре");
        }
        
        logger.info("Найден перевод для '" + searchWord + "': '" + translation + "'");
        return translation;
    }

    public boolean wordExists(String word) throws IOException {
        logger.info("Проверка существования слова: '" + word + "'");
        
        if (word == null || word.trim().isEmpty()) {
            return false;
        }

        Map<String, String> dictionary = readDictionary();
        boolean exists = dictionary.containsKey(word.trim());
        
        logger.info("Слово '" + word + "' " + (exists ? "найдено" : "не найдено") + " в словаре");
        return exists;
    }
}