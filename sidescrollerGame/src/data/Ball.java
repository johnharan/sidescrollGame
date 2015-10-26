package data;

import static helpers.Artist.drawCircle;

public class Ball {
	private float x, y, radius;
	private int sides;

	public Ball(float x, float y, float radius, int sides){
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.sides = sides;
	}
	
	public void updateBall(){
		
	}
	
	public void drawBall(){
		drawCircle(x, y, radius, sides);
	}
	
	public void collisionDetection(){
		
		
		
	}
	
}
