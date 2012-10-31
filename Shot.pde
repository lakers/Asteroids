public class Shot {
  public float angle;
  public PVector velocity;
  public PVector position;
  public float distance;
  public boolean active;
  
  public Shot() {
    velocity = new PVector();
    position = new PVector();
    distance = 0.0;
    active = true;
  } 
  
  public void draw() {
    pushMatrix();
    translate(position.x, position.y);
    ellipse(0, 0, 2, 2);
    popMatrix(); 
  }
  
  public void update() {
     position.add(velocity);
     distance += velocity.mag();
     if(distance > 250) {
        active = false;
     }
     
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
