public class Ship {
  public float angle;
  public PVector velocity;
  public PVector acceleration;
  public PVector position;
  
  public Ship() {
    angle = 0;
    position = new PVector();
    velocity = new PVector();
    acceleration = new PVector();
  } 
  
  public void draw() {
    stroke(255);
    drawShip(position.x, position.y, angle);
  }
  
  public void update() {
     if(acceleration.mag() > 0.05) {
       acceleration.normalize();
       acceleration.mult(0.05); 
     }
     
     velocity.add(acceleration);
     
     if(velocity.mag() > 2.0) {
       velocity.normalize(); 
       velocity.mult(2.0);
     }
     
     position.add(velocity);
     
     // wrap
     if(position.y < 0) {
       position.y = 600; 
     } else if(position.y > 600) {
       position.y = 0; 
     }
     
     if(position.x < 0) {
       position.x = 800; 
     } else if(position.x > 800) {
       position.x = 0; 
     }
   }
  
}
