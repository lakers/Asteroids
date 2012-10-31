Ship ship = new Ship();
ArrayList shots;
ArrayList asteroids;
int lives;
PFont scoreFont;
int score;
boolean[] keys = new boolean[4];

void setup() {
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

void setupAsteroids(int num) {
  for(int i = 0; i < num; i++) {
    int s = (int)random(5)+1;
    float x = random(800);
    float y = random(600);
    Asteroid testAsteroid = new Asteroid(s, x, y);
    testAsteroid.velocity.x = random(-10.0, 10.0)/10.0;
    testAsteroid.velocity.y = random(-10.0, 10.0)/10.0;
    asteroids.add(testAsteroid);
  } 
}


void draw() {
  if(keys[0] == true) {
    ship.angle -= 10;
  }
  if(keys[1] == true) {
    ship.angle += 10; 
  }
  if(keys[2] == true) {  
    ship.acceleration.x += 0.03*cos((ship.angle-90)*PI/180.0);
    ship.acceleration.y += 0.03*sin((ship.angle-90)*PI/180.0);
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

void drawScore() {
  if(lives > 0) {
    fill(255);
    stroke(255);
    textFont(scoreFont);
    String scorestr = "" + score;
    text(scorestr, 2, 12);
  }
}

void drawLives() {
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

void checkCollisions() {
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
           int newsize = int(random(s-1)+1);
           s -= newsize;
           float angle = random(360)*PI/180;
           Asteroid newA = new Asteroid(newsize, asteroid.position.x+newsize*cos(angle),
                                           asteroid.position.y+newsize*sin(angle));
           newA.velocity.x = random(10.0)/10.0 * cos(angle);
           newA.velocity.y = random(10.0)/10.0 * sin(angle);
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

void keyPressed() {
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


void keyReleased() {
  //if(keyCode == UP) {
  //  ship.acceleration.mult(0);
  //} 
  if(keyCode == SHIFT) {
    Shot shot = new Shot();
    shot.position.x = ship.position.x;
    shot.position.y = ship.position.y;
    shot.velocity.x = 3*cos((ship.angle-90)*PI/180.0);
    shot.velocity.y = 3*sin((ship.angle-90)*PI/180.0);
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
