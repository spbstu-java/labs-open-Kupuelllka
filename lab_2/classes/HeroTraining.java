package lab_2.classes;


public class HeroTraining {
    private String heroName;
    private int strength;
    private int agility;
    private int intelligence;
    
    public HeroTraining(String name) {
        this.heroName = name;
        this.strength = 10;
        this.agility = 10;
        this.intelligence = 10;
    }
    
    @Repeat(3)
    public void doPushUps(int count) {
        int gained = count / 5;
        strength += gained;
        System.out.println(heroName + " делает " + count + " отжиманий! +" + gained + " к силе");
    }
    
    public void rest() {
        System.out.println(heroName + " отдыхает... Zzz");
    }
    
    @Repeat(2)
    protected void practiceArchery(int arrows) {
        int gained = arrows / 3;
        agility += gained;
        System.out.println(heroName + " выпускает " + arrows + " стрел! +" + gained + " к ловкости");
    }
    
    @Repeat(4)
    protected String studySpell(String spellName, int complexity) {
        int gained = complexity * 2;
        intelligence += gained;
        String result = heroName + " изучает заклинание '" + spellName + "'! +" + gained + " к интеллекту";
        System.out.println(result);
        return result;
    }
    
    @Repeat(1)
    private void meditate(boolean deepMeditation) {
        int gained = deepMeditation ? 5 : 2;
        intelligence += gained;
        System.out.println(heroName + (deepMeditation ? " глубоко медитирует" : " медитирует") + 
                          "! +" + gained + " к интеллекту");
    }
    
    @Repeat(5)
    private int runMarathon(int distance, int speed) {
        int staminaGain = distance * speed / 10;
        agility += staminaGain;
        System.out.println(heroName + " пробегает марафон " + distance + " км со скоростью " + speed + 
                          " км/ч! +" + staminaGain + " к ловкости");
        return staminaGain;
    }
    
    @Repeat(2)
    private void liftWeights(double weight, String weaponType) {
        int strengthGain = (int)(weight / 20);
        strength += strengthGain;
        System.out.println(heroName + " поднимает " + weight + " кг " + weaponType + 
                          "! +" + strengthGain + " к силе");
    }
    
    // Методы для получения статистики
    public void showStats() {
        System.out.println("\n=== СТАТИСТИКА ГЕРОЯ ===");
        System.out.println("Имя: " + heroName);
        System.out.println("Сила: " + strength);
        System.out.println("Ловкость: " + agility);
        System.out.println("Интеллект: " + intelligence);
        System.out.println("Общий уровень: " + (strength + agility + intelligence));
        System.out.println("=======================\n");
    }
    
    public boolean isReadyForBattle() {
        return (strength + agility + intelligence) > 50;
    }
    
    // Геттеры
    public String getHeroName() { return heroName; }
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }
}
