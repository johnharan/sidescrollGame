package data;

import static helpers.Artist.drawCircle;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

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
		collisionDetection();
		
	}
	
	public void drawBall(){
		drawCircle(x, getY(), radius, sides);
	}
	
	public void collisionDetection(){
		if(y <= 0){
			y = 0;
		}
		if(y >= Display.getHeight() + 100){
			y = Display.getHeight() + 100;
		}
		if(x <= GameObjects.getForeground().getForegroundElements().get(0).getX() + radius){
			x = GameObjects.getForeground().getForegroundElements().get(0).getX() + radius;
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
	
	public void jump(){
		final float currentY = y;
		if (Thread.activeCount() <= 2) { // this allows the main thread plus max of one timer thread																											
			Thread timedJump = new Thread(new Runnable() {
				public void run() {
					float gravity = 0;
					while(y >= currentY - 200){
						y -= 1 * Clock.getDelta() - gravity;
						try {
							gravity = gravity + 0.6f;
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					while(y <= currentY){
						y += 1 * Clock.getDelta();
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			timedJump.start();
		}
	}
	
}
