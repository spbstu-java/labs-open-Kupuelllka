package lab_3.applications;
    
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lab_3.classes.Translator;

public class TranslatorTest {

        private static final Logger logger = Logger.getLogger(TranslatorTest.class.getName());
    
        public static void main(String[] args) {
            // Путь к файлу словаря
            String dictionaryPath = "lab_3/data/dict.txt";
            
            logger.info("Запуск приложения Переводчик");
            
            try {
                // Создаем и запускаем переводчик
                Translator translator = new Translator(dictionaryPath);
                
                // Загружаем словарь
                translator.loadDictionary();
                
                logger.info("Словарь успешно загружен, запуск интерактивного режима");
                
                // Запускаем интерактивный режим
                translator.startInteractiveMode();
                
                logger.info("Приложение успешно завершено");
                
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Критическая ошибка при загрузке словаря", e);
                System.err.println("Ошибка при загрузке словаря: " + e.getMessage());
                System.err.println("Убедитесь, что файл " + dictionaryPath + " существует и имеет правильный формат");
                System.exit(1);
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Неожиданная ошибка в приложении", e);
                System.err.println("Неожиданная ошибка: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }
}