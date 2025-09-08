package lab_2.applications;

import java.lang.reflect.Method;
import java.util.Scanner;

import lab_2.classes.HeroTraining;
import lab_2.classes.Repeat;

public class TrainingManager {
    
    public static void startTraining(HeroTraining hero) {
        System.out.println("🐉 Начинаем тренировку героя " + hero.getHeroName() + "! 🐉");
        hero.showStats();
        
        invokeAnnotatedMethods(hero);
        
        System.out.println("🏁 Тренировка завершена!");
        hero.showStats();
        
        if (hero.isReadyForBattle()) {
            System.out.println("🎉 " + hero.getHeroName() + " готов к битве с драконом!");
        } else {
            System.out.println("⚠️  Нужно еще потренироваться!");
        }
    }
    
    private static void invokeAnnotatedMethods(Object obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        
        for (Method method : methods) {
            if (method.isAnnotationPresent(Repeat.class)) {
                Repeat repeat = method.getAnnotation(Repeat.class);
                int times = repeat.value();
                
                method.setAccessible(true);
                
                System.out.println("\n⚡ Упражнение: " + method.getName() + " (повторов: " + times + ")");
                
                for (int i = 0; i < times; i++) {
                    try {
                        Object[] parameters = createTrainingParameters(method.getParameterTypes(), i + 1);
                        method.invoke(obj, parameters);
                        
                        // Небольшая пауза между упражнениями
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.err.println("Ошибка при выполнении упражнения: " + e.getMessage());
                    }
                }
            }
        }
    }
    
    private static Object[] createTrainingParameters(Class<?>[] parameterTypes, int attempt) {
        Object[] parameters = new Object[parameterTypes.length];
        
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = getTrainingValue(parameterTypes[i], attempt);
        }
        
        return parameters;
    }
    
    private static Object getTrainingValue(Class<?> type, int attempt) {
        if (type == int.class || type == Integer.class) {
            return attempt * 5 + 10; // Увеличиваем сложность с каждой попыткой
        } else if (type == double.class || type == Double.class) {
            return attempt * 10.5 + 20.0;
        } else if (type == boolean.class || type == Boolean.class) {
            return attempt % 2 == 0;
        } else if (type == String.class) {
            String[] spells = {"Огненный шар", "Ледяная стрела", "Молния", "Исцеление", "Телепортация"};
            String[] weapons = {"меча", "топора", "лука", "посоха", "кинжала"};
            return type.getSimpleName().contains("Spell") ? spells[attempt % spells.length] : weapons[attempt % weapons.length];
        } else {
            return null;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("🎮 СИМУЛЯТОР ТРЕНИРОВКИ ГЕРОЯ 🎮");
        System.out.print("Введите имя героя: ");
        String heroName = scanner.nextLine();
        
        HeroTraining hero = new HeroTraining(heroName);
        startTraining(hero);
        
        scanner.close();
    }
}