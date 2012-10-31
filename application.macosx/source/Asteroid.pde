public class Asteroid {
  public PVector velocity;
  public PVector position;
  public boolean active;
  public int size;
  public int sides;
  public ArrayList pnts;
  
  public Asteroid(int s, float x, float y) {
    velocity = new PVector();
    position = new PVector(x, y);
    active = true;
    size = s;
    sides = int(random(5) + 10);
    pnts = new ArrayList();
    setupPoints();
  } 
  
  public void setupPoints() {
     pnts.clear();
     float angle = 360/sides;
     for(int i = 0; i < sides; i++) {
       float distance =  7*size + random(5);
       PVector pnt = new PVector();
       float offsetAngle = random(10)-5;
       pnt.x = distance*cos((angle*i + offsetAngle)*PI/180);
       pnt.y = distance*sin((angle*i + offsetAngle)*PI/180);
       pnts.add(pnt);
   }
  }
  
  
  public void update() {
    position.add(velocity); 
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
  
  public void draw() {
    //ellipse(position.x, position.y, 10*size, 10*size);  
    stroke(255);
    fill(0);
    pushMatrix();
    translate(position.x, position.y);
    beginShape();
    for(int i = 0; i < pnts.size(); i++) {
      PVector pnt = (PVector)pnts.get(i);
      vertex(pnt.x, pnt.y); 
    }
    endShape(CLOSE);
    popMatrix();
  }
}
