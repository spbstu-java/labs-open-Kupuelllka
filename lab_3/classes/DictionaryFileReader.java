package lab_3.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DictionaryFileReader {
    private final String filePath;

    public DictionaryFileReader(String filePath) {
        this.filePath = filePath;
        AppLogger.info("Создан DictionaryFileReader для файла: " + filePath);
    }

    /**
     * Проверка существования файла
     */
    private void checkFileExists() throws Exceptions.DictionaryFileException {
        File file = new File(filePath);
        if (!file.exists()) {
            String errorMsg = "Файл не найден: " + filePath;
            AppLogger.severe(errorMsg);
            throw new Exceptions.DictionaryFileException(errorMsg);
        }
        if (!file.isFile()) {
            String errorMsg = "Указанный путь не является файлом: " + filePath;
            AppLogger.severe(errorMsg);
            throw new Exceptions.DictionaryFileException(errorMsg);
        }
        if (!file.canRead()) {
            String errorMsg = "Нет прав на чтение файла: " + filePath;
            AppLogger.severe(errorMsg);
            throw new Exceptions.DictionaryFileException(errorMsg);
        }
    }

    /**
     * Чтение всего словаря из файла
     */
    public Map<String, String> readDictionary() throws Exceptions.DictionaryLoadException {
        AppLogger.info("Начало чтения словаря из файла: " + filePath);
        
        Map<String, String> dictionary = new HashMap<>();
        int lineNumber = 0;
        int successfulEntries = 0;

        try {
            // Проверяем существование файла перед чтением
            checkFileExists();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    processDictionaryLine(line, lineNumber, dictionary);
                }
                
                successfulEntries = dictionary.size();
                AppLogger.info(String.format("Успешно прочитано %d записей из %d строк файла", 
                    successfulEntries, lineNumber));
                
            } catch (IOException e) {
                throw new Exceptions.DictionaryLoadException("Ошибка чтения файла: " + e.getMessage(), e);
            }

            if (dictionary.isEmpty()) {
                AppLogger.warning("Словарь пуст или не содержит валидных записей");
                throw new Exceptions.DictionaryLoadException("Файл не содержит валидных записей словаря");
            }

            return dictionary;
            
        } catch (Exceptions.DictionaryFileException e) {
            throw new Exceptions.DictionaryLoadException("Ошибка доступа к файлу словаря", e);
        }
    }

    /**
     * Обработка одной строки словаря
     */
    private void processDictionaryLine(String line, int lineNumber, Map<String, String> dictionary) {
        if (line == null || line.trim().isEmpty()) {
            AppLogger.fine("Пропущена пустая строка №" + lineNumber);
            return;
        }

        String trimmedLine = line.trim();
        String[] parts = trimmedLine.split("\\|", 2);
        
        if (parts.length < 2) {
            AppLogger.warning("Неверный формат в строке " + lineNumber + ": '" + trimmedLine + "'");
            return;
        }

        String word = parts[0].trim();
        String translation = parts[1].trim();

        if (word.isEmpty() || translation.isEmpty()) {
            AppLogger.warning("Пустое слово или перевод в строке " + lineNumber + ": '" + trimmedLine + "'");
            return;
        }

        if (dictionary.containsKey(word)) {
            AppLogger.warning("Дублирующееся слово '" + word + "' в строке " + lineNumber);
        }
        
        dictionary.put(word, translation);
        AppLogger.fine("Добавлена запись: '" + word + "' -> '" + translation + "'");
    }

    public String readWordTranslation(String word) throws Exceptions.DictionaryLoadException, Exceptions.TranslationException {
        AppLogger.info("Поиск перевода для слова: '" + word + "'");
        
        if (word == null || word.trim().isEmpty()) {
            AppLogger.warning("Передано пустое слово для поиска");
            throw new Exceptions.InvalidInputException("Слово не может быть пустым");
        }

        String searchWord = word.trim();
        Map<String, String> dictionary = readDictionary();
        
        String translation = dictionary.get(searchWord);
        
        if (translation == null) {
            AppLogger.warning("Перевод для слова '" + searchWord + "' не найден в словаре");
            throw new Exceptions.TranslationException("Слово '" + searchWord + "' не найдено в словаре");
        }
        
        AppLogger.info("Найден перевод для '" + searchWord + "': '" + translation + "'");
        return translation;
    }

    public boolean wordExists(String word) throws Exceptions.DictionaryLoadException {
        AppLogger.info("Проверка существования слова: '" + word + "'");
        
        if (word == null || word.trim().isEmpty()) {
            return false;
        }

        Map<String, String> dictionary = readDictionary();
        boolean exists = dictionary.containsKey(word.trim());
        
        AppLogger.info("Слово '" + word + "' " + (exists ? "найдено" : "не найдено") + " в словаре");
        return exists;
    } 
}