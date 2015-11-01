package data;

import static helpers.Artist.loadTexture;

import org.lwjgl.opengl.Display;

import Foreground.Foreground;
import Foreground.SolidGround;

public class GameObjects {
	private static Ball ball;
	private static Foreground foreground;
	private static float startY = 498f;
	
	public static void loadGameObjects(){
		ball = new Ball(400,startY,70,40);
		foreground = new Foreground();
		
		SolidGround large = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 0,Display.getHeight()-512,1024,512,0.0f);
		SolidGround large2 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1200,700,1024,512,10f);
		SolidGround large3 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1100,1700,1024,512,0.0f);
		SolidGround large4 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 0,2000,1024,512,0.0f);
		SolidGround large5 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1300,3000,1024,512,0.0f);
		SolidGround large6 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 0,4000,1024,512,0.0f);
		// texture width/height must be multiple of 16
		foreground.addElement(large);
		foreground.addElement(large2);
		foreground.addElement(large3);
		foreground.addElement(large4);
		foreground.addElement(large5);
		foreground.addElement(large6);
		//System.out.println("x: " + foreground.getForegroundElements().get(1).getX() + ",y: " + foreground.getForegroundElements().get(1).getY());
		
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
