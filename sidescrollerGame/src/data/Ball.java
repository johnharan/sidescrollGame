package data;

import static helpers.Artist.drawCircle;

import org.lwjgl.input.Keyboard;

public class Ball {
	private float x, y, radius;
	private int sides;

	public Ball(float x, float y, float radius, int sides){
		this.x = x;
		this.setY(y);
		this.radius = radius;
		this.sides = sides;
	}
	
	public void updateBall(){
		collisionDetection();
		
	}
	
	public void drawBall(){
		drawCircle(x, getY(), radius, sides);
	}
	
	public void collisionDetection(){
		if(x <= 0){
			x = 0;
		}
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		
	}
	

	
}
