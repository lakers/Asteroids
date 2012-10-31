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
