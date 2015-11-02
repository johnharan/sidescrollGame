package data;

import static helpers.Artist.loadTexture;

import org.lwjgl.opengl.Display;

import Foreground.Foreground;
import Foreground.SolidGround;

public class GameObjects {
	private static Ball ball;
	private static Foreground foreground;
	private static float startY = 498f, startX = 400;
	
	public static void loadGameObjects(){
		ball = new Ball(startX,startY,70,40);
		foreground = new Foreground();
		
		SolidGround large = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 0,Display.getHeight()-512,1024,512,0.0f);
		SolidGround large2 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 700,1000,1024,100,60f);
		SolidGround large3 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1350,1800,1024,100,40.0f);
		SolidGround large4 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1800,2750,1024,512,-30.0f);
		SolidGround large5 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 2600,2120,1024,512,-50.0f);
		SolidGround large6 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 500,2200,1024,512,0.0f);
		SolidGround large7 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 0,3400,1024,512,0.0f);
		SolidGround large8 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1050,3150,1024,512,-27.0f);
		SolidGround large9 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 2048,3700,1024,512,0.0f);
		SolidGround large10 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 3072,3700,1024,512,0.0f);
		
		// texture width/height must be multiple of 16
		foreground.addElement(large);
		foreground.addElement(large2);
		foreground.addElement(large3);
		foreground.addElement(large4);
		foreground.addElement(large5);
		foreground.addElement(large6);
		foreground.addElement(large7);
		foreground.addElement(large8);
		foreground.addElement(large9);
		foreground.addElement(large10);
		//System.out.println("x: " + foreground.getForegroundElements().get(1).getX() + ",y: " + foreground.getForegroundElements().get(1).getY());
		
	}
	
	public static float getStartX() {
		return startX;
	}

	public static float getStartY() {
		return startY;
	}

	public static Foreground getForeground(){
		return foreground;
	}
	
	public static Ball getBall() {
		// TODO Auto-generated method stub
		return ball;
	}

}
