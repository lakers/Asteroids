void drawShip(float x, float y, float angle) {
   pushMatrix();
   noFill();
   translate(x, y);
   rotate(angle*PI/180.0);
   line(0, -10, -5, 10);
   line(0, -10, 5, 10);
   smooth();
   curve(-5.5, 12.0, -3.0, 7.5, 3.0, 7.5, 5.5, 12.0);
   noSmooth();
   popMatrix();
}
