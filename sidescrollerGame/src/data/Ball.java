package data;

import static helpers.Artist.drawCircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;

import stateManager.States;
import static helpers.Artist.*;
import Foreground.Foreground;

public class Ball {
	private float x, y, radius, halfRadius, velocity, changeY, changeX, ballSpeed, foregroundSpeed, distance, ballS = 0.0f, fgS = 0.7f, xoffset;
	public float getChangeY() {
		return changeY;
	}

	private int sides;

	public Ball(float x, float y, float radius, int sides){
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.sides = sides;
		halfRadius = radius/2;
	}
	
	public void gravity(){
		if(!isRotatedObjectColliding()){
			//y += 0.5f * Clock.getDelta();
		}
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
	
	public void calculateVelocity(){
		
		if (Thread.activeCount() <= 10) { // this allows the main thread plus max of one timer thread. need to account for sound effect threads also
			Thread timedMissAdjustment = new Thread(new Runnable() {
				float lastx = 0;
				public void run() {
					try {
						lastx = x;
						Thread.sleep(2000); // wait 2 seconds
						velocity = (x - lastx)/2000;
						//System.out.println(velocity);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			timedMissAdjustment.start();
		}
	}
	
	public void updateBall(){
		if(isBallAlive() == true){
			gravity();
			//calculateVelocity(); // move camerax at rate proportional to velocity of ball x
			//System.out.println(velocity);
			adjustSpeedChange();
			calcDistance();
			adjustCameraY();
			
			collisionDetection();
			//System.out.println(y);
			//System.out.println("x: " + GameObjects.getForeground().getForegroundElements().get(1).getX() + ",y: " + GameObjects.getForeground().getForegroundElements().get(1).getY());
			
		}else{
			States.setState(States.GameStates.END);
		}
	}
	

	public void adjustCameraX() {
		float startX = GameObjects.getStartX();
		changeX = startX - x;
		
		for(Foreground o: GameObjects.getForeground().getForegroundElements()){
			//System.out.println("y: " + y + ",changeY: " + changeY);
			o.setX(o.getX() + changeX/1000);
		}
		
	}

	public float getBallSpeed() {
		return ballSpeed;
	}

	public float getForegroundSpeed() {
		return foregroundSpeed;
	}

	public float getRadius() {
		return radius;
	}

	public void drawBall(){
		drawCircle(x, y, radius, sides);
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
		
		//System.out.println("p1: " + p1 + ",normalise: " + p1.normalise() + ",length: " + p1.length());
		
		line_v.normalise(unit_line_v); // normalise line_v and store result in unit_line_v
		
		length_proj_v = Vector2f.dot(pt_v, unit_line_v); // ||proj_v|| = pt_v . unit_line_v
		length_line_v = line_v.length(); // |line_v|
		
		//System.out.println("|proj_v| = " + length_proj_v);
		
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
		
		//System.out.println("unit_line_v: "+ unit_line_v + ",length_proj_v: " + length_proj_v + ",proj_v: " + proj_v);
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
	
	public HashMap<String,Float> calculateRotatedCoords(){
		HashMap<String,Float> rotatedCoords = new HashMap<String,Float>();
		
		Foreground o = getClosestObject();

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
	
	public void calcDistance(){
		distance = ((Display.getWidth() - 600) - x)/100;
		
		
	}
	
	public float getDistance() {
		return distance;
	}


	
	public float getBallS() {
		return ballS;
	}

	public void setBallS(float ballS) {
		if(ballS >= 0.7f){
			ballS = 0.7f;
		}
		if(ballS <= 0.0f){
			ballS = 0.0f;
		}
		
		this.ballS = ballS;
	}

	public float getFgS() {
		return fgS;
	}

	public void setFgS(float fgS) {
		if(fgS <= 0.0f){
			fgS = 0.0f;
		}
		if(fgS >= 0.7f){
			fgS = 0.7f;
		}
		
		this.fgS = fgS;
	}

	public void adjustSpeedChange(){
		
		Foreground closest = GameObjects.getBall().getClosestObject();
		float decay = 0.1f; // .1f after change of 20 in rotation, .2 after change of 40, .3 after 60
		float rotation = closest.getRotation();
		
		if(rotation > -63.35 && rotation < 53.35){
			 decay = .05f;
		}else if(rotation > -53.35 && rotation < -43.35){
			 decay = .1f;
		}else if(rotation > -43.35 && rotation < -33.35){
			 decay = .15f;
		}else if(rotation > -33.35 && rotation < -23.35){
			 decay = .2f;
		}else if(rotation > -23.35 && rotation < -13.35){
			 decay = .25f;
		}else if(rotation > -13.35 && rotation < 0){
			 decay = .3f;
		}
		// 0.7 / 63.35 = 0.011049724
		float speedChange = -(rotation * 0.011049724f) -.01f;
		
				
		ballSpeed = 0.0f + speedChange;
		foregroundSpeed = 0.7f - speedChange;
		//System.out.println(ballSpeed);
		
	}
	
	public Vector2f getOffset(){
		Vector2f offset = new Vector2f(0,0);
		Foreground o = getClosestObject(); // gets nearest foreground element to ball
		
		if(o.getRotation() != 0){
		
			HashMap<String, Float> rotatedCoords = calculateRotatedCoords();

			float x1 = rotatedCoords.get("x1");
			float y1 = rotatedCoords.get("y1");
			float x2 = rotatedCoords.get("x2");
			float y2 = rotatedCoords.get("y2");

			Vector2f c = new Vector2f(x, y);
			Vector2f p1 = new Vector2f(x1, y1);
			Vector2f p2 = new Vector2f(x2, y2);

			offset = (Vector2f) closestPointOnLine(p1, p2, c).get(1);
			
			return offset;
		}
		
		return offset;
	}
	
	public void collisionDetection(){
		
		if(x <= GameObjects.getForeground().getForegroundElements().get(0).getX() + radius){ // if ball is at far left of first ground element
			x = GameObjects.getForeground().getForegroundElements().get(0).getX() + radius;
		}
		
		Foreground o = getClosestObject(); // gets nearest foreground element to ball
		
		if(o.getRotation() != 0){
			
			HashMap<String, Float> rotatedCoords = calculateRotatedCoords();
			
			float x1 = rotatedCoords.get("x1");
			float y1 = rotatedCoords.get("y1");
			float x2 = rotatedCoords.get("x2");
			float y2 = rotatedCoords.get("y2");
			
			if(x < x1 - halfRadius|| x > x2 + halfRadius){ // don't need to check outside line
					
			}else{

				float x3 = rotatedCoords.get("x3");
				float y3 = rotatedCoords.get("y3");
				float x4 = rotatedCoords.get("x4");
				float y4 = rotatedCoords.get("y4");

				Vector2f c = new Vector2f(x, y);
				Vector2f p1 = new Vector2f(x1, y1);
				Vector2f p2 = new Vector2f(x2, y2);

				Vector2f p3 = new Vector2f(x3, y3);
				Vector2f p4 = new Vector2f(x4, y4);

				ArrayList<Object> linep1p2 = closestPointOnLine(p1, p2, c); // closestPointOnLine(Vector2f p1, Vector2f p2, Vector2f c)
				ArrayList<Object> linep1p4 = closestPointOnLine(p1, p4, c);

				float dist_top_to_ball = (Float) linep1p2.get(0);
				Vector2f offset1 = (Vector2f) linep1p2.get(1);

				float dist_left_to_ball = (Float) linep1p4.get(0);
				Vector2f offset2 = (Vector2f) linep1p4.get(1);

				if (dist_top_to_ball < radius) {
					
					// might need to reverse the below x for example, and reverse the foreground x, if on a slope
					//
					y += offset1.getY();
					
					x += offset1.getX();
					
					xoffset = offset1.getX();
					
					//System.out.println("x offset: " + offset1.getX() + ",y offset: " + offset1.getY());
					
					
				
				}
				

			}
		}else{ // for non rotated object collision
			boolean isInsideLeftAndRight = (Boolean) (x >= o.getX() - halfRadius ? x <= (o.getX() + o.getWidth() + halfRadius):false);
			boolean isOnTop = (Boolean) (y >= o.getY() - radius ? y <= (o.getY() - radius + 100):false);
			boolean isOnBottom = (Boolean) (y <= (o.getY() + o.getHeight() + radius) ? y >= (o.getY() + o.getHeight() - 20):false);
			boolean isInsideTopAndBottom = (Boolean) (y >= o.getY() - halfRadius ? y <= (o.getY() + o.getHeight() + halfRadius):false);
			boolean isOnLeft = (Boolean) (x >= o.getX() - radius ? x <= (o.getX() + 20):false);
			boolean isOnRight = (Boolean) (x <= (o.getX() + o.getWidth() + radius) ? x >= (o.getX() + o.getWidth() - 20):false);
			
		
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

	
	public float getXoffset() {
		return xoffset;
	}

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
	
	public float getVelocity() {
		return velocity;
	}
	
}
