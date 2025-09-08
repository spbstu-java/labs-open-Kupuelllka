package lab_1.classes.Hero;

import lab_1.classes.Movement.BaseMovement;
import lab_1.classes.Movement.WalkMovement;

public class Hero {
    private String name;
    private BaseMovement typeMovement;
    
    public Hero(String name) {
        this.name = name;
        this.typeMovement = new WalkMovement(); 
    }
    
    public void setMovementStrategy(BaseMovement typeMovement) {
        this.typeMovement = typeMovement;
    }
    
    public void move(String from, String to) {
        System.out.print(name + ": ");
        typeMovement.move(from, to);
    }
    
    public String getCurrentMovementType() {
        return typeMovement.getClass().getSimpleName().replace("Movement", "");
    }
}
