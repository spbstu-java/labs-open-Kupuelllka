package lab_3.applications;

import java.io.IOException;
import java.util.Map;

import lab_3.classes.DictionaryFileReader;

public class ReaderTest {
  public static void main(String[] args) {
        DictionaryFileReader reader = new DictionaryFileReader("lab_3/data/dict.txt");
        
        try {
            // Чтение всего словаря
            Map<String, String> dictionary = reader.readDictionary();
            System.out.println("Словарь загружен: " + dictionary.size() + " слов");
            
            // Поиск конкретного слова
            String translation = reader.readWordTranslation("hello");
            System.out.println("Перевод: " + translation);
            
            // Проверка существования слова
            boolean exists = reader.wordExists("world");
            System.out.println("Слово существует: " + exists);
            
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
