package data;

import static helpers.Artist.drawCircle;

import java.util.HashMap;

import stateManager.States;
import static helpers.Artist.*;
import Foreground.Foreground;

public class Ball {
	private float x, y, radius, halfRadius, interX, interY;
	private int sides;

	public Ball(float x, float y, float radius, int sides){
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.sides = sides;
		halfRadius = radius/2;
	}
	
	public void gravity(){
		y += 0.5f * Clock.getDelta();
	}
	
	public boolean isBallAlive(){
		for(Foreground o: GameObjects.getForeground().getForegroundElements()){
			if(y <= o.getY() + o.getHeight() + radius){ // checking if ball is above the bottom of any foreground object
				return true;
			}
		}
		return false;
	}
	
	public void adjustCameraY(){
		float startY = GameObjects.getStartY();
		float changeY = startY - y;
		
		for(Foreground o: GameObjects.getForeground().getForegroundElements()){
			o.setY(o.getY() + changeY/2);
		}
		
	}
	
	public void updateBall(){
		if(isBallAlive() == true){
			if(isBallOnSurface() == false){
				gravity();
			}
			adjustCameraY();
			collisionDetection();
		}else{
			States.setState(States.GameStates.END);
		}
	}
	
	public void drawBall(){
		drawCircle(x, y, radius, sides);
	}
	
	public HashMap<String,Float> calculateRotatedCoords(){
		HashMap<String,Float> rotatedCoords = new HashMap<String,Float>();
		
		Foreground o = getClosestObject();
		float rotation = o.getRotation();
		float rectX1 = o.getX(), rectY1 = o.getY();
		float rectX2 = o.getX() + o.getWidth(), rectY2 = o.getY();
		float rectX3 = o.getX() + o.getWidth(), rectY3 = o.getY() + o.getHeight();
		float rectX4 = o.getX(), rectY4 = o.getY() + o.getHeight();

		// below calculates new x and y values (after rotation)
		float x1 = (float) (rectX1 * Math.cos((rotation * 3.149) / 180) + (rectY1 * Math
				.sin((rotation * 3.149) / 180)));
		float y1 = (float) (-rectX1 * Math.sin((rotation * 3.149) / 180) + (rectY1 * Math
				.cos((rotation * 3.149) / 180)));

		
		float x2 = (float) (rectX2 * Math.cos((rotation * 3.149) / 180) + (rectY2 * Math
				.sin((rotation * 3.149) / 180)));
		float y2 = (float) (-rectX2 * Math.sin((rotation * 3.149) / 180) + (rectY2 * Math
				.cos((rotation * 3.149) / 180)));
		//float x2 = (float) (rectX2 * Math.cos((rotation * 3.149) / 180) - (rectY2 * Math
		//		.sin((rotation * 3.149) / 180)));
		//float y2 = (float) (rectY2 * Math.cos((rotation * 3.149) / 180) + (rectX2 * Math
		//		.sin((rotation * 3.149) / 180)));

		
		
		float x3 = (float) (rectX3 * Math.cos((rotation * 3.149) / 180) - (rectY3 * Math
				.sin((rotation * 3.149) / 180)));
		float y3 = (float) (rectY3 * Math.cos((rotation * 3.149) / 180) + (rectX3 * Math
				.sin((rotation * 3.149) / 180)));
		
		float x4 = (float) (rectX4 * Math.cos((rotation * 3.149) / 180) - (rectY4 * Math
				.sin((rotation * 3.149) / 180)));
		float y4 = (float) (rectY4 * Math.cos((rotation * 3.149) / 180) + (rectX4 * Math
				.sin((rotation * 3.149) / 180)));

		
		drawQuad(x1, y1, 100, 10);
		drawQuad(x2, y2, 100, 10);
		//drawQuad(x3, y3, 100, 10);
		//drawQuad(x4, y4, 100, 10);
		
		rotatedCoords.put("x1", x1);
		rotatedCoords.put("y1", y1);
		rotatedCoords.put("x2", x2);
		rotatedCoords.put("y2", y2);
		rotatedCoords.put("x3", x3);
		rotatedCoords.put("y3", y3);
		rotatedCoords.put("x4", x4);
		rotatedCoords.put("y4", y4);
		
		return rotatedCoords;
	}
	
	public void collisionDetection(){
		
		if(x <= GameObjects.getForeground().getForegroundElements().get(0).getX() + radius){ // if ball is at far left of first ground element
			x = GameObjects.getForeground().getForegroundElements().get(0).getX() + radius;
		}
		
		Foreground o = getClosestObject(); // gets nearest foreground element to ball
		boolean isInsideLeftAndRight = (Boolean) (x >= o.getX() - halfRadius ? x <= (o.getX() + o.getWidth() + halfRadius):false);
		boolean isOnTop = (Boolean) (y >= o.getY() - radius ? y <= (o.getY() - radius + 100):false);
		boolean isOnBottom = (Boolean) (y <= (o.getY() + o.getHeight() + radius) ? y >= (o.getY() + o.getHeight() - 20):false);
		boolean isInsideTopAndBottom = (Boolean) (y >= o.getY() - halfRadius ? y <= (o.getY() + o.getHeight() + halfRadius):false);
		boolean isOnLeft = (Boolean) (x >= o.getX() - radius ? x <= (o.getX() + 20):false);
		boolean isOnRight = (Boolean) (x <= (o.getX() + o.getWidth() + radius) ? x >= (o.getX() + o.getWidth() - 20):false);
		
		
		if(o.getRotation() > 0){
			
			
			HashMap<String, Float> coordsCopy = calculateRotatedCoords();
			
			float x1 = coordsCopy.get("x1");
			float y1 = coordsCopy.get("y1");
			float x2 = coordsCopy.get("x2");
			float y2 = coordsCopy.get("y2");
			
			//drawQuad(o.getX(), o.getY(), 100, 10);
			//drawQuad(o.getX() + o.getWidth(), o.getY(), 100, 10);
			
			System.out.println("x: " + o.getX() + ",y: " + o.getY());
			
			if(isBetween(x1,y1,x2,y2,x,y)){
				y = 200;
				
			}
			//System.out.println(isBetween(x1,y1,x2,y2,x,y));
			
			
		}else{
		
			// top of element
			if (isOnTop && isInsideLeftAndRight) { // stops ball at top/surface of an element
				y = o.getY() - radius;
			}

			if (isOnBottom && isInsideLeftAndRight) { // stops ball at bottom of an element
				y = o.getY() + o.getHeight() + radius;
			}

			// sides of element
			if (isOnLeft && isInsideTopAndBottom) { // stops ball at left hand side of an element
				x = o.getX() - radius;
			}

			if (isOnRight && isInsideTopAndBottom) { // stops ball at right hand side of an element
				x = (o.getX() + o.getWidth() + radius);
			}
	
		}
	}

	public float distance(float x1,float x2,float y1,float y2){ // distance between two points
		return (float) Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
	}
	
	public boolean isBetween(float ax,float ay,float bx,float by,float cx,float cy){
		//System.out.println("distance(ax,cx,ay,cy) " + distance(ax,cx,ay,cy) + ",distance(cx,bx,cy,by) " + distance(cx,bx,cy,by) + " == distance(ax,bx,cy,by)" + distance(ax,bx,cy,by) + ",total = " + (distance(ax,cx,ay,cy) + distance(cx,bx,cy,by)));
		
		if((distance(ax,cx,ay,cy) + distance(cx,bx,cy,by)) - distance(ax,bx,cy,by) <= 20){
			interX = 0;
			interY = 0;
			return true;
		}
		return false;
	}
	
	public boolean isBallOnSurface(){
		Foreground o = getClosestObject();
		boolean isOnTop = (Boolean) (y >= o.getY() - radius ? y <= (o.getY() - radius + 20):false);
		boolean isInsideLeftAndRight = (Boolean) (x >= o.getX() - halfRadius ? x <= (o.getX() + o.getWidth() + halfRadius):false);
		
		if(isOnTop && isInsideLeftAndRight){
			return true;
		}
		return false;
	}
	
	public void jump(){
		if(isBallOnSurface()){ // can only jump if on a foreground element surface
			final float currentY = y;
			if (Thread.activeCount() <= 2) { // this allows the main thread plus max of one timer thread
				Thread timedJump = new Thread(new Runnable() {
					public void run() {
						float gravity = 0;
						while (y >= currentY - 200) {
							y -= 1 * Clock.getDelta() - gravity;
							if (gravity > 1 * Clock.getDelta()) {
								gravity = 0;
								break;
							} else {
								gravity += 0.9f;
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
	
	public Foreground getClosestObject(){
		float centerX, centerY = 0;
		double distance = 0;
		double minDistance = Double.MAX_VALUE;
		Foreground closestObject = null;
		for(Foreground o: GameObjects.getForeground().getForegroundElements()){
			centerX = o.getX() + (o.getWidth() / 2);
			centerY = o.getY() + (o.getHeight() / 2);
			distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2)); // use distance formula for two coordinate points

			if(distance < minDistance){ // compare distances to find minimum distance
				minDistance = distance;
				closestObject = o;
			}
		}
		return closestObject;
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
