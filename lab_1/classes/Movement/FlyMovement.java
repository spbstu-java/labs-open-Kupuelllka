package lab_1.classes.Movement;

public class FlyMovement implements BaseMovement{
    
    @Override
    public void move(String from, String to) {
        System.out.println("Герой летит из " + from + " в " + to);
    }
}
