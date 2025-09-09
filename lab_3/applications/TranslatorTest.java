package lab_3.applications;

import java.util.logging.Level;

import lab_3.classes.AppLogger;
import lab_3.classes.Exceptions;
import lab_3.classes.Translator;

public class TranslatorTest {
    public static void main(String[] args) {
        
        Translator translator = new Translator("lab_3/data/dict.txt");

        try {
            translator.loadDictionary();
            System.out.println("Словарь загружен успешно. Записей: " + translator.getDictionarySize());
            translator.startInteractiveMode();
            
        } catch (Exceptions.DictionaryLoadException e) {
            System.err.println("Ошибка загрузки словаря: " + e.getMessage());
            AppLogger.log(Level.SEVERE, "Ошибка загрузки словаря", e);
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
            AppLogger.log(Level.SEVERE, "Неожиданная ошибка", e);
        }
    }
}
