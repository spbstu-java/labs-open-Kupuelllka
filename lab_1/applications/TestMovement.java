package lab_1.applications;

import lab_1.classes.Hero.Hero;
import lab_1.classes.Movement.BaseMovement;
import lab_1.classes.Movement.FlyMovement;
import lab_1.classes.Movement.HorseMovement;
import lab_1.classes.Movement.SwimMovement;
import lab_1.classes.Movement.WalkMovement;

import java.util.Scanner;

public class TestMovement {
    private static Scanner scanner = new Scanner(System.in);
    private static Hero hero;
    private static BaseMovement[] strategies;
    
    public static void main(String[] args) {
        initialize();
        showMainMenu();
    }
    
    private static void initialize() {
        // Создаем героя
        hero = new Hero("Артур");
        
        // Создаем различные стратегии перемещения
        strategies = new BaseMovement[]{
            new WalkMovement(),
            new HorseMovement(),
            new FlyMovement(),
            new SwimMovement()
        };
    }
    
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
            System.out.println("1. Выбрать стратегию перемещения");
            System.out.println("2. Переместиться между точками");
            System.out.println("3. Автоматическая демонстрация");
            System.out.println("4. Выход");
            System.out.print("Выберите опцию (1-4): ");
            
            int choice = getIntInput(1, 4);
            
            switch (choice) {
                case 1:
                    selectStrategy();
                    break;
                case 2:
                    moveBetweenPoints();
                    break;
                case 3:
                    automaticDemo();
                    break;
                case 4:
                    System.out.println("До свидания!");
                    return;
            }
        }
    }
    
    private static void selectStrategy() {
        System.out.println("\n=== ВЫБОР СТРАТЕГИИ ПЕРЕМЕЩЕНИЯ ===");
        System.out.println("1. Пешком");
        System.out.println("2. На лошади");
        System.out.println("3. Полёт");
        System.out.println("4. Плавание");
        System.out.print("Выберите способ перемещения (1-4): ");
        
        int choice = getIntInput(1, 4);
        hero.setMovementStrategy(strategies[choice - 1]);
        
        String[] strategyNames = {"пешком", "на лошади", "полётом", "плаванием"};
        System.out.println("Стратегия изменена на: " + strategyNames[choice - 1]);
    }
    
    private static void moveBetweenPoints() {
        if (hero.getCurrentMovementType() == null) {
            System.out.println("Сначала выберите стратегию перемещения!");
            return;
        }
        
        System.out.println("\n=== ПЕРЕМЕЩЕНИЕ МЕЖДУ ТОЧКАМИ ===");
        System.out.print("Введите начальную точку: ");
        String from = scanner.nextLine();
        
        System.out.print("Введите конечную точку: ");
        String to = scanner.nextLine();
        
        hero.move(from, to);
    }
    
    private static void automaticDemo() {
        System.out.println("\n=== АВТОМАТИЧЕСКАЯ ДЕМОНСТРАЦИЯ ===");
        String[] locations = {"деревня", "река", "лес", "гора", "пещера"};
        
        for (int i = 0; i < locations.length - 1; i++) {
            BaseMovement currentStrategy = strategies[i % strategies.length];
            hero.setMovementStrategy(currentStrategy);
            
            String[] strategyNames = {"пешком", "на лошади", "полётом", "плаванием"};
            System.out.println("\n--- Используем " + strategyNames[i % strategies.length] + " ---");
            
            hero.move(locations[i], locations[i + 1]);
        }
    }
    
    private static int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.print("Пожалуйста, введите число от " + min + " до " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Неверный ввод. Пожалуйста, введите число: ");
            }
        }
    }
}