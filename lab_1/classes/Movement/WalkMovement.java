package lab_1.classes.Movement;

public class WalkMovement implements BaseMovement{
    
    @Override
    public void move(String from, String to){
        System.out.println("Герой идет пешком из " + from + " в "+to);
    }
}
