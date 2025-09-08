package lab_1.classes.Movement;

public class SwimMovement implements BaseMovement{
    
    @Override
    public void move(String from, String to) {
        System.out.println("Герой плывет из "+from+"в"+to);
    }
}
