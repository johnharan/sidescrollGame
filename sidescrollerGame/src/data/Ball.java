package data;

import static helpers.Artist.drawCircle;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import stateManager.States;
import static helpers.Artist.*;
import Foreground.Foreground;

public class Ball {
	private float x, y, radius, halfRadius, changeY, xoffset, velocity, rotation = 0, rotation2 = 90,rotation3 = 45,rotation4 = 135;

	private int sides;
	private float xoffsetSlope;
	private float yoffsetSlope;

	
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
		changeY = startY - y;
		
		for(Foreground o: GameObjects.getForeground().getForegroundElements()){
			//System.out.println("y: " + y + ",changeY: " + changeY);
			o.setY(o.getY() + changeY/4);
		}
	}
	
	public void updateBall(){
		if(isBallAlive() == true){
			gravity();
			adjustCameraY();
			collisionDetection();
			calculateVelocity();
			updateWheelRotation();
			//addMomentum();
			//to do:
			//-----
			// add sound effects
			//-----
			// add a bounce function like jump
			// that will jump the ball with height equivalent to half -velocity if ball on surface
			//-----
			// ball momentum
		}else{
			States.setState(States.GameStates.END);
		}
	}
	

	public void drawBall(){
		drawCircle(x, y, radius, sides);
	}
	
	public void updateWheelRotation(){
		rotation += velocity *15;
		rotation2 += velocity * 15;
		rotation3 += velocity *15;
		rotation4 += velocity * 15;	
	}
	
	public void calculateVelocity(){
		if (Thread.activeCount() <= 4) { // this allows the main thread plus max of one timer thread. need to account for sound effect threads also
 			Thread timedVelocity = new Thread(new Runnable() {
 				float lastx = 0;
				float lastFGx = 0;
 				public void run() {
 					try {
 						if((x > 400 && x < (Display.getWidth() - 500) && !GameObjects.getBall().isRotatedObjectColliding()) 
 							|| (GameObjects.getBall().isRotatedObjectColliding() 
 							&& !(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) 
 							&& !(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)|| Keyboard.isKeyDown(Keyboard.KEY_D)))
 							|| (x < 400 && (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)|| Keyboard.isKeyDown(Keyboard.KEY_D)) && !GameObjects.getBall().isRotatedObjectColliding())
 							|| (x > (Display.getWidth() - 500) && (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) && !GameObjects.getBall().isRotatedObjectColliding())){
 							lastx = x;
 							Thread.sleep(20); // wait 20 milliseconds
 							velocity = ((x - lastx)/20);
 						}else if(((Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) && GameObjects.getBall().getClosestObject().getRotation() <= -66.23f) || (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) && GameObjects.getBall().getClosestObject().getRotation() >= 66.23f){
 							// simulates no friction on a steep slope
 						}else{
 							float fgX = GameObjects.getBall().getClosestObject().getX();
 							lastFGx = fgX;
 							Thread.sleep(20); // wait 20 milliseconds
 							velocity = -(GameObjects.getBall().getClosestObject().getX() - lastFGx)/20;
 						}
 					}catch(InterruptedException e) {
		 				e.printStackTrace();
 					}
 				}
 		});
	    timedVelocity.start();
		}
	}
	
	public void drawSpokes(){	
		drawThickLine(x+1,y+radius,x+1,y-radius,rotation);
		drawThickLine(x,y+radius,x,y-radius,rotation2);
		drawThickLine(x+1,y+radius,x+1,y-radius,rotation3);
		drawThickLine(x,y+radius,x,y-radius,rotation4);
	}
	
	public ArrayList<Object> closestPointOnLine(Vector2f p1, Vector2f p2, Vector2f c){
		// below is from tutorial http://doswa.com/2009/07/13/circle-segment-intersectioncollision.html
		Vector2f line_v = new Vector2f();
		Vector2f unit_line_v = new Vector2f();
		Vector2f pt_v = new Vector2f(); // distCtoP1
		Vector2f closest = new Vector2f();
		Vector2f dist_v = new Vector2f();
		Vector2f unit_dist_v = new Vector2f();
		
		float length_proj_v = 0; // scalar (real number)
		float length_line_v = 0;
		float length_dist_v = 0;

		Vector2f.sub(p2, p1, line_v);
		Vector2f.sub(c, p1, pt_v);
		
		line_v.normalise(unit_line_v); // normalise line_v and store result in unit_line_v
		
		length_proj_v = Vector2f.dot(pt_v, unit_line_v); // ||proj_v|| = pt_v . unit_line_v
		length_line_v = line_v.length(); // |line_v|
		
		if(length_proj_v < 0){
			closest = p1;
		}
		if(length_proj_v > length_line_v){
			closest = p2;
		}
		
		// proj_v = ||line_v|| x |proj_v|  
		float proj_v_x = unit_line_v.getX() * length_proj_v;
		float proj_v_y = unit_line_v.getY() * length_proj_v;
		
		Vector2f proj_v = new Vector2f(proj_v_x,proj_v_y);

		Vector2f.add(p1, proj_v, closest);
		
		Vector2f.sub(c, closest, dist_v);
		length_dist_v = dist_v.length();
		
		dist_v.normalise(unit_dist_v);
		
		float offset_x = unit_dist_v.getX() * (radius - length_dist_v);
		float offset_y = unit_dist_v.getY() * (radius - length_dist_v);
		
		Vector2f offset = new Vector2f(offset_x,offset_y);
		
		/*
		if(length_dist_v < radius){
			y += offset.getY();
			//x += offset.getX();
		}else{
			//not intersecting
		}
		*/
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(length_dist_v);
		result.add(offset);
		
		return result;
	}
	

	public HashMap<String,Float> calculateRotatedCoords(Foreground o){
		HashMap<String,Float> rotatedCoords = new HashMap<String,Float>();
		

		// below formula found on http://gamedev.stackexchange.com/questions/86755/how-to-calculate-corner-marks-of-a-rotated-rectangle
		// cx, cy - center of square coordinates
		// x, y - coordinates of a corner point of the square
		// theta is the angle of rotation

		// calculate cx and cy by adding half width and height from top left
		float cx = o.getX() + (o.getWidth()/2), cy = o.getY() + (o.getHeight()/2);
		float theta = (float) ((o.getRotation() * 3.149) / 180); // need to convert to radians for math functions
		
		float tempX1 = o.getX() - cx;
		float tempY1 = o.getY() - cy;
		float tempX2 = o.getX() + o.getWidth() - cx;
		float tempY2 = o.getY() - cy;
		float tempX3 = o.getX() + o.getWidth() - cx;
		float tempY3 = o.getY() + o.getHeight() - cy;
		float tempX4 = o.getX() - cx;
		float tempY4 = o.getY() + o.getHeight() - cy;
		
		/*  now apply rotation formula for each point
		 * 
		 * 	x[n] = x[n] * cos(theta) - y[n] * sin(theta)
		 * 	y[n] = x[n] * sin(theta) + y[n] * cos(theta)
		 */
		float rotatedX1 = (float) (tempX1*Math.cos(theta) - tempY1*Math.sin(theta));
		float rotatedY1 = (float) (tempX1*Math.sin(theta) + tempY1*Math.cos(theta));

		float rotatedX2 = (float) (tempX2*Math.cos(theta) - tempY2*Math.sin(theta));
		float rotatedY2 = (float) (tempX2*Math.sin(theta) + tempY2*Math.cos(theta));

		float rotatedX3 = (float) (tempX3*Math.cos(theta) - tempY3*Math.sin(theta));
		float rotatedY3 = (float) (tempX3*Math.sin(theta) + tempY3*Math.cos(theta));
		
		float rotatedX4 = (float) (tempX4*Math.cos(theta) - tempY4*Math.sin(theta));
		float rotatedY4 = (float) (tempX4*Math.sin(theta) + tempY4*Math.cos(theta));
		
		
		// translate back
		float x1 = rotatedX1 + cx;
		float y1 = rotatedY1 + cy;
		
		float x2 = rotatedX2 + cx;
		float y2 = rotatedY2 + cy;
		
		float x3 = rotatedX3 + cx;
		float y3 = rotatedY3 + cy;
		
		float x4 = rotatedX4 + cx;
		float y4 = rotatedY4 + cy;
		////


		
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
	
	
	public HashMap<String,Float> calculateRotatedCoords(){
		HashMap<String,Float> rotatedCoords = new HashMap<String,Float>();
		
		Foreground o = getClosestRotatedObject();

		// below formula found on http://gamedev.stackexchange.com/questions/86755/how-to-calculate-corner-marks-of-a-rotated-rectangle
		// cx, cy - center of square coordinates
		// x, y - coordinates of a corner point of the square
		// theta is the angle of rotation

		// calculate cx and cy by adding half width and height from top left
		float cx = o.getX() + (o.getWidth()/2), cy = o.getY() + (o.getHeight()/2);
		float theta = (float) ((o.getRotation() * 3.149) / 180); // need to convert to radians for math functions
		
		float tempX1 = o.getX() - cx;
		float tempY1 = o.getY() - cy;
		float tempX2 = o.getX() + o.getWidth() - cx;
		float tempY2 = o.getY() - cy;
		float tempX3 = o.getX() + o.getWidth() - cx;
		float tempY3 = o.getY() + o.getHeight() - cy;
		float tempX4 = o.getX() - cx;
		float tempY4 = o.getY() + o.getHeight() - cy;
		
		/*  now apply rotation formula for each point
		 * 
		 * 	x[n] = x[n] * cos(theta) - y[n] * sin(theta)
		 * 	y[n] = x[n] * sin(theta) + y[n] * cos(theta)
		 */
		float rotatedX1 = (float) (tempX1*Math.cos(theta) - tempY1*Math.sin(theta));
		float rotatedY1 = (float) (tempX1*Math.sin(theta) + tempY1*Math.cos(theta));

		float rotatedX2 = (float) (tempX2*Math.cos(theta) - tempY2*Math.sin(theta));
		float rotatedY2 = (float) (tempX2*Math.sin(theta) + tempY2*Math.cos(theta));

		float rotatedX3 = (float) (tempX3*Math.cos(theta) - tempY3*Math.sin(theta));
		float rotatedY3 = (float) (tempX3*Math.sin(theta) + tempY3*Math.cos(theta));
		
		float rotatedX4 = (float) (tempX4*Math.cos(theta) - tempY4*Math.sin(theta));
		float rotatedY4 = (float) (tempX4*Math.sin(theta) + tempY4*Math.cos(theta));
		
		
		// translate back
		float x1 = rotatedX1 + cx;
		float y1 = rotatedY1 + cy;
		
		float x2 = rotatedX2 + cx;
		float y2 = rotatedY2 + cy;
		
		float x3 = rotatedX3 + cx;
		float y3 = rotatedY3 + cy;
		
		float x4 = rotatedX4 + cx;
		float y4 = rotatedY4 + cy;
		////


		
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
	
	public boolean isRotatedObjectColliding(){

		HashMap<String, Float> rotatedCoords = calculateRotatedCoords();
		
		float x1 = rotatedCoords.get("x1");
		float y1 = rotatedCoords.get("y1");
		float x2 = rotatedCoords.get("x2");
		float y2 = rotatedCoords.get("y2");
		
		Vector2f p1 = new Vector2f(x1,y1);
		Vector2f p2 = new Vector2f(x2,y2);
		Vector2f c = new Vector2f(x,y);
		

		ArrayList<Object> result = closestPointOnLine(p1,p2,c); // closestPointOnLine(Vector2f p1, Vector2f p2, Vector2f c)
		
		float length_dist_v = (Float) result.get(0);
		
		if(length_dist_v < radius + 1){ // adding + 1 is a tweak to ensure ball can jump
			return true;
		}
		return false;
		
	}
	
	
	public void collisionDetection(){
		
		if(x <= GameObjects.getForeground().getForegroundElements().get(0).getX() + radius){ // if ball is at far left of first ground element
			x = GameObjects.getForeground().getForegroundElements().get(0).getX() + radius;
		}

		ArrayList<Foreground> nClosestObjects = getNClosestObjects(2);
		
		for(Foreground closest: nClosestObjects){

			if(closest.getRotation() == 0){ // for non rotated object collision
				boolean isInsideLeftAndRight = (Boolean) (x >= closest.getX() - halfRadius ? x <= (closest.getX() + closest.getWidth() + halfRadius):false);
				boolean isOnTop = (Boolean) (y >= closest.getY() - radius ? y <= (closest.getY() - radius + 100):false);
				boolean isOnBottom = (Boolean) (y <= (closest.getY() + closest.getHeight() + radius) ? y >= (closest.getY() + closest.getHeight() - 20):false);
				boolean isInsideTopAndBottom = (Boolean) (y >= closest.getY() - halfRadius ? y <= (closest.getY() + closest.getHeight() + halfRadius):false);
				boolean isOnLeft = (Boolean) (x >= closest.getX() - radius ? x <= (closest.getX() + 20):false);
				boolean isOnRight = (Boolean) (x <= (closest.getX() + closest.getWidth() + radius) ? x >= (closest.getX() + closest.getWidth() - 20):false);


				// top of element
				if (isOnTop && isInsideLeftAndRight) { // stops ball at top/surface of an element
					y = closest.getY() - radius;
				}

				if (isOnBottom && isInsideLeftAndRight) { // stops ball at bottom of an element
					y = closest.getY() + closest.getHeight() + radius;
				}

				// sides of element
				if (isOnLeft && isInsideTopAndBottom) { // stops ball at left hand side of an element
					x = closest.getX() - radius;
				}

				if (isOnRight && isInsideTopAndBottom) { // stops ball at right hand side of an element
					x = (closest.getX() + closest.getWidth() + radius);
				}

			}else{
				HashMap<String, Float> rotatedCoords = calculateRotatedCoords(closest);

				float x1 = rotatedCoords.get("x1");
				float y1 = rotatedCoords.get("y1");
				float x2 = rotatedCoords.get("x2");
				float y2 = rotatedCoords.get("y2");
				
				if((x < x1 - halfRadius|| x > x2 + halfRadius) || ((closest.getRotation() < 0) && (y < y2 - radius)) ||  ((closest.getRotation() > 0) && (y < y1 - radius))){ // don't need to check outside line	
				
				}else{

					Vector2f c = new Vector2f(x, y);
					Vector2f p1 = new Vector2f(x1, y1);
					Vector2f p2 = new Vector2f(x2, y2);

					ArrayList<Object> linep1p2 = closestPointOnLine(p1, p2, c); // closestPointOnLine(Vector2f p1, Vector2f p2, Vector2f c)

					float dist_top_to_ball = (Float) linep1p2.get(0);
					Vector2f offset1 = (Vector2f) linep1p2.get(1);

					if (dist_top_to_ball < radius) {
						y += offset1.getY();					
						x += offset1.getX();
						xoffset = offset1.getX();
					}			
				}
			}
		
		}
	}

	/*
	private void addMomentum() {
		if(GameObjects.getBall().isRotatedObjectColliding()){ // can only jump if on a foreground element surface
			if (Thread.activeCount() <= 6) { // this allows the main thread plus max of one timer thread
				Thread timedJump = new Thread(new Runnable() {
					public void run() {
						float momentum = 0;
						while (momentum < 100) {
							try {
								momentum += 0.1f * (GameObjects.getBall().getClosestObject().getRotation()/90);
								if(GameObjects.getBall().getClosestObject().getRotation() < 0){
									x -= momentum;
								}else if(GameObjects.getBall().getClosestObject().getRotation() == 0){
									momentum = 0;
								}else{
									x -= momentum;
								}
								
								Thread.sleep(50);
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
	 */

	public boolean isBallOnSurface(){
		Foreground o = getClosestObject();
		
		if(o.getRotation() != 0){ // if item is rotated, need to run below collision check
			return isRotatedObjectColliding();
		}
		
		boolean isOnTop = (Boolean) (y >= o.getY() - radius ? y <= (o.getY() - radius + 20):false);
		boolean isInsideLeftAndRight = (Boolean) (x >= o.getX() - halfRadius ? x <= (o.getX() + o.getWidth() + halfRadius):false);
		
		if(isOnTop && isInsideLeftAndRight){
			return true;
		}
		return false;
	}
	
	/*
	public void bounce(final float velocity){
		if(isBallOnSurface()){ // can only jump if on a foreground element surface
			if (Thread.activeCount() <= 5) { // this allows the main thread plus max of one timer thread
				Thread timedJump = new Thread(new Runnable() {
					public void run() {
						float gravity = 0;
						while (y >= velocity - 200) {
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
	*/
	
	public void jump(){
		if(isBallOnSurface()){ // can only jump if on a foreground element surface
			final float currentY = y;
			if (Thread.activeCount() <= 5) { // this allows the main thread plus max of one timer thread
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
	
	public ArrayList<Foreground> getNClosestObjects(int n){
		ArrayList<Foreground> closestObjects = new ArrayList<Foreground>();
		ArrayList<Foreground> foregroundObjects = new ArrayList<Foreground>(GameObjects.getForeground().getForegroundElements());
		
		Foreground closestObject = null;
			
		while(n != 0){
			int index = 0;
			float centerX, centerY = 0;
			double minDistance = Double.MAX_VALUE, distance = 0;
			for(Foreground o: foregroundObjects){
				centerX = o.getX() + (o.getWidth() / 2);
				centerY = o.getY() + (o.getHeight() / 2);
				distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2)); // use distance formula for two coordinate points

				if(distance < minDistance){ // compare distances to find minimum distance
					minDistance = distance;
					closestObject = o;
					index = foregroundObjects.indexOf(o);
				}
			}
			closestObjects.add(closestObject);
			foregroundObjects.remove(index);
			n--;
		}
		return closestObjects;
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
	
	public Foreground getClosestRotatedObject(){
		float centerX, centerY = 0;
		double distance = 0;
		double minDistance = Double.MAX_VALUE;
		Foreground closestObject = null;
		for(Foreground o: GameObjects.getForeground().getForegroundElements()){
			if(o.getRotation() != 0){
				centerX = o.getX() + (o.getWidth() / 2);
				centerY = o.getY() + (o.getHeight() / 2);
				distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2)); // use distance formula for two coordinate points

				if(distance < minDistance){ // compare distances to find minimum distance
					minDistance = distance;
					closestObject = o;
				}
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
	
	public float getXoffset() {
		return xoffset;
	}
	
	public float getChangeY() {
		return changeY;
	}

	public float getRadius() {
		return radius;
	}
	
	public float getXoffsetSlope() {
		return xoffsetSlope;
	}

	public float getYoffsetSlope() {
		return yoffsetSlope;
	}

	public float getVelocity() {
		return velocity;
	}
}
