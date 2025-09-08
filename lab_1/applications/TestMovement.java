package lab_1.applications;

import lab_1.classes.Hero.Hero;
import lab_1.classes.Movement.BaseMovement;
import lab_1.classes.Movement.FlyMovement;
import lab_1.classes.Movement.HorseMovement;
import lab_1.classes.Movement.SwimMovement;
import lab_1.classes.Movement.WalkMovement;

public class TestMovement {
    public static void main(String[] args) {
        // Создаем героя
        Hero hero = new Hero("Артур");
        
        // Создаем различные стратегии перемещения
        BaseMovement walk = new WalkMovement();
        BaseMovement horse = new HorseMovement();
        BaseMovement fly = new FlyMovement();
        BaseMovement swim = new SwimMovement();
        
        System.out.println("=== Демонстрация перемещений героя ===");
        
        // Перемещаемся разными способами
        hero.move("деревни", "замка");
        
        // Меняем стратегию на лошадь
        hero.setMovementStrategy(horse);
        hero.move("замка", "леса");
        
        // Меняем стратегию на полет
        hero.setMovementStrategy(fly);
        hero.move("леса", "гор");
        
        // Меняем стратегию на плавание
        hero.setMovementStrategy(swim);
        hero.move("острова", "материка");
        
        // Возвращаемся к ходьбе
        hero.setMovementStrategy(walk);
        hero.move("материка", "деревни");
        
        System.out.println("\n=== Демонстрация смены стратегий в цикле ===");
        
        // Массив всех доступных стратегий
        BaseMovement[] strategies = {walk, horse, fly, swim};
        String[] locations = {"деревня", "река", "лес", "гора", "пещера"};
        
        // Проходим по всем точкам разными способами
        for (int i = 0; i < locations.length - 1; i++) {
            BaseMovement currentStrategy = strategies[i % strategies.length];
            hero.setMovementStrategy(currentStrategy);
            hero.move(locations[i], locations[i + 1]);
        }
    }
}
