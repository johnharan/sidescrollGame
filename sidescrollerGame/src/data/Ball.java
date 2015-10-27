package data;

import static helpers.Artist.drawCircle;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import Foreground.Foreground;

public class Ball {
	private float x, y, radius, halfRadius;
	private int sides;

	public Ball(float x, float y, float radius, int sides){
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.sides = sides;
		halfRadius = radius/2;
	}
	
	public void gravity(){
		y += 0.1f * Clock.getDelta();
	}
	
	public void updateBall(){
		gravity();
		collisionDetection();
		
	}
	
	public void drawBall(){
		drawCircle(x, y, radius, sides);
	}
	
	public void collisionDetection(){
		
		if(y >= Display.getHeight() + 100){
			y = Display.getHeight() + 100;
		}
		if(x <= GameObjects.getForeground().getForegroundElements().get(0).getX() + radius){ // if ball is at far left of first ground element
			x = GameObjects.getForeground().getForegroundElements().get(0).getX() + radius;
		}
		
		
		for(Foreground o: GameObjects.getForeground().getForegroundElements()){
			// top of element
			if(y >= o.getY() - radius && y <= (o.getY() - radius + 20) && x >= o.getX() - halfRadius && x <= (o.getX() + o.getWidth() + halfRadius) ){ // stops ball at top/surface of an element
				y = o.getY() - radius;
			}
			
			if(y <= (o.getY() + o.getHeight() + radius) && y >= (o.getY() + o.getHeight() - 20) && x >= o.getX() - halfRadius && x <= (o.getX() + o.getWidth() + halfRadius) ){ // stops ball at bottom of an element
				y = o.getY() + o.getHeight() + radius;
			}
			
			// sides of element
			if(y >= o.getY() - halfRadius && y <= (o.getY() + o.getHeight() + halfRadius) && x >= o.getX() - radius && x <= (o.getX() + 20) ){ // stops ball at left hand side of an element
				x = o.getX() - radius;
			}
			
			if(y >= o.getY() - halfRadius && y <= (o.getY() + o.getHeight() + halfRadius) && x <= (o.getX() + o.getWidth() + radius) && x >= (o.getX() + o.getWidth() - 20) ){ // stops ball at right hand side of an element
				x = (o.getX() + o.getWidth() + radius);
			}
	
			
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
						if(gravity > 1 * Clock.getDelta()){
							gravity = 0;
							break;
						}else{
							gravity += 0.4f;
						}
						try {
							Thread.sleep(5);
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
