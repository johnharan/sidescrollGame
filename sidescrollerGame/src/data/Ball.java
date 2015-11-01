package data;

import static helpers.Artist.drawCircle;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;

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
			o.setY(o.getY() + changeY/4);
		}
		
	}
	
	public void updateBall(){
		if(isBallAlive() == true){
			gravity();
			
			adjustCameraY();
			collisionDetection();
		}else{
			States.setState(States.GameStates.END);
		}
	}
	
	public void drawBall(){
		drawCircle(x, y, radius, sides);
	}
	
	public void closestPointOnLine(Vector2f p1, Vector2f p2, Vector2f c){
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
		
		if(length_dist_v < radius){
			y += offset.getY();
			x += offset.getX();
		}else{
			//not intersecting
		}
		
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

		drawQuad(x1,y1,1000,10);
		drawQuad(x2,y2,1000,10);
		drawQuad(x3,y3,1000,10);
		drawQuad(x4,y4,2000,10);
		
		
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
		
		
		if(o.getRotation() != 0){
			
			
			HashMap<String, Float> coordsCopy = calculateRotatedCoords();
			
			float x1 = coordsCopy.get("x1");
			float y1 = coordsCopy.get("y1");
			float x2 = coordsCopy.get("x2");
			float y2 = coordsCopy.get("y2");
			
			drawLine(x1,y1,x2,y2);
			
			Vector2f p1 = new Vector2f(x1,y1);
			Vector2f p2 = new Vector2f(x2,y2);
			Vector2f c = new Vector2f(x,y);
			
			//System.out.println("p1: " + p1 + ",p2: " + p2 + ",p1.p2: " + Vector2f.dot(p1, p2));
			
			closestPointOnLine(p1,p2,c);
			
			/*
			if(isBetween(x1,y1,x2,y2,x,y)){
				y -= 0.5f * Clock.getDelta();
				
				if(o.getRotation() >0 && o.getRotation() < 90){
					x += 1;	
				}else if(o.getRotation() <0 && o.getRotation() > -90){
					x -= 1;	
				}
			}
			*/
			
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
			//drawQuad(cx,cy,1000,10,40.0f);
			y = 100;
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
