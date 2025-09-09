package lab_1.classes.Movement;

public class HorseMovement implements BaseMovement {

    @Override
    public void move(String from, String to) {
        System.out.println("Герой движется на лощади из "+from+"в"+to);
    }
    
}
