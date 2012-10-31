import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class Asteroids extends PApplet {

Ship ship = new Ship();
ArrayList shots;
ArrayList asteroids;
int lives;
PFont scoreFont;
int score;
boolean[] keys = new boolean[4];

public void setup() {
  size(800, 600);
  ship.position.x = 400;
  ship.position.y = 300;
  ship.velocity.x = 0;
  ship.velocity.y = 0;
  ship.acceleration.x = 0;
  ship.acceleration.y = 0;
  shots = new ArrayList();
  asteroids = new ArrayList();
  setupAsteroids(20);
  lives = 4;
  scoreFont = createFont("Arial", 15);
  score = 0;
}

public void setupAsteroids(int num) {
  for(int i = 0; i < num; i++) {
    int s = (int)random(5)+1;
    float x = random(800);
    float y = random(600);
    Asteroid testAsteroid = new Asteroid(s, x, y);
    testAsteroid.velocity.x = random(-10.0f, 10.0f)/10.0f;
    testAsteroid.velocity.y = random(-10.0f, 10.0f)/10.0f;
    asteroids.add(testAsteroid);
  } 
}


public void draw() {
  if(keys[0] == true) {
    ship.angle -= 10;
  }
  if(keys[1] == true) {
    ship.angle += 10; 
  }
  if(keys[2] == true) {  
    ship.acceleration.x += 0.03f*cos((ship.angle-90)*PI/180.0f);
    ship.acceleration.y += 0.03f*sin((ship.angle-90)*PI/180.0f);
  } 

  
  background(0); 
  ship.update();
  ship.draw();
  

  checkCollisions();
  for(int i = 0; i < shots.size(); i++) {
    ((Shot)shots.get(i)).update(); 
    ((Shot)shots.get(i)).draw(); 
    if(!((Shot)shots.get(i)).active){
      shots.remove(i); 
    }
  }
  for(int i = 0; i < asteroids.size(); i++) {
    ((Asteroid)asteroids.get(i)).update(); 
    ((Asteroid)asteroids.get(i)).draw(); 
    if(!((Asteroid)asteroids.get(i)).active){
      asteroids.remove(i); 
    }
  }
  drawLives();
  drawScore();
}

public void drawScore() {
  if(lives > 0) {
    fill(255);
    stroke(255);
    textFont(scoreFont);
    String scorestr = "" + score;
    text(scorestr, 2, 12);
  }
}

public void drawLives() {
  fill(0);
  stroke(255);
  rect(0, 0, 60, 35); 
  pushMatrix();
  translate(3, 10);
  for(int i = 0; i < lives; i++) {
    drawShip(i*15 + 5, 11, 0);
  }
  popMatrix();
  if(lives < 0) {
    stroke(0);
    rect(0, 0, 800, 600);
    PFont font;
    font = createFont("Arial", 32);
    textFont(font);
    stroke(255);
    fill(255);
    
    text("GAME OVER", 300, 300); 
    text("SCORE: " + score, 300, 350);
  }
}

public void checkCollisions() {
  for(int i = 0; i < asteroids.size(); i++) {
    Asteroid asteroid = (Asteroid)asteroids.get(i);
    for(int j = 0; j < shots.size(); j++) {
      Shot shot = (Shot)shots.get(j);
      
      float distance = (shot.position).dist(asteroid.position);
      if(distance < (10 * asteroid.size)) {
        shots.remove(j);
        score += (5 - asteroid.size) * 10;
        asteroid.size -= 1;
        
        int s = asteroid.size;
        while(s > 0) {
           int newsize = PApplet.parseInt(random(s-1)+1);
           s -= newsize;
           float angle = random(360)*PI/180;
           Asteroid newA = new Asteroid(newsize, asteroid.position.x+newsize*cos(angle),
                                           asteroid.position.y+newsize*sin(angle));
           newA.velocity.x = random(10.0f)/10.0f * cos(angle);
           newA.velocity.y = random(10.0f)/10.0f * sin(angle);
           asteroids.add(newA);
        }
        asteroids.remove(i);
      } 
    }
    if(asteroid.position.dist(ship.position) < (asteroid.size * 10)) {
      asteroids.remove(i);
      ship.position.x = 400; 
      ship.position.y = 300;
      ship.velocity.x = 0;
      ship.velocity.y = 0;
      ship.acceleration.x = 0;
      ship.acceleration.y = 0;
      lives -= 1;
      score -= 50;
    }
  } 
}

public void keyPressed() {
  if(keyCode == LEFT) {
    keys[0] = true;
  } else if(keyCode == RIGHT) {
    keys[1] = true; 
  } else if(keyCode == UP) {
    keys[2] = true; 
  } else if(keyCode == DOWN) {
    keys[3] = true; 
  }
}


public void keyReleased() {
  //if(keyCode == UP) {
  //  ship.acceleration.mult(0);
  //} 
  if(keyCode == SHIFT) {
    Shot shot = new Shot();
    shot.position.x = ship.position.x;
    shot.position.y = ship.position.y;
    shot.velocity.x = 3*cos((ship.angle-90)*PI/180.0f);
    shot.velocity.y = 3*sin((ship.angle-90)*PI/180.0f);
    shots.add(shot);
  }
  if(key == 'r') {
    setup(); 
  }
  if(keyCode == LEFT) {
    keys[0] = false;
  } else if(keyCode == RIGHT) {
    keys[1] = false; 
  } else if(keyCode == UP) {
    ship.acceleration.mult(0);
    keys[2] = false; 
  } else if(keyCode == DOWN) {
    keys[3] = false; 
  }
  
}
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
    sides = PApplet.parseInt(random(5) + 10);
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
public class Player {
  public static final int MAX_LIVES = 4;
  
  public int lives;
  public int score;
  public Ship ship;
  
  public Player() {
    lives = MAX_LIVES;
    score = 0;
    ship = new Ship();
   
  } 
}
public void drawShip(float x, float y, float angle) {
   pushMatrix();
   noFill();
   translate(x, y);
   rotate(angle*PI/180.0f);
   line(0, -10, -5, 10);
   line(0, -10, 5, 10);
   smooth();
   curve(-5.5f, 12.0f, -3.0f, 7.5f, 3.0f, 7.5f, 5.5f, 12.0f);
   noSmooth();
   popMatrix();
}
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
     if(acceleration.mag() > 0.05f) {
       acceleration.normalize();
       acceleration.mult(0.05f); 
     }
     
     velocity.add(acceleration);
     
     if(velocity.mag() > 2.0f) {
       velocity.normalize(); 
       velocity.mult(2.0f);
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
public class Shot {
  public float angle;
  public PVector velocity;
  public PVector position;
  public float distance;
  public boolean active;
  
  public Shot() {
    velocity = new PVector();
    position = new PVector();
    distance = 0.0f;
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

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "Asteroids" });
  }
}
