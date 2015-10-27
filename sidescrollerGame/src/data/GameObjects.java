package data;

import static helpers.Artist.loadTexture;

import org.lwjgl.opengl.Display;

import Foreground.Foreground;
import Foreground.SolidGround;

public class GameObjects {
	private static Ball ball;
	private static Foreground foreground;
	
	public static void loadGameObjects(){
		ball = new Ball(1800,650,70,40);
		foreground = new Foreground();
		
		SolidGround large = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 0,Display.getHeight()-512,1024,512);
		SolidGround large2 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1600,100,1024,512);
		SolidGround large3 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1600,850,1024,512);
		// texture width/height must be multiple of 16
		foreground.addElement(large);
		foreground.addElement(large2);
		foreground.addElement(large3);

		
	}
	
	public static Foreground getForeground(){
		return foreground;
	}
	
	public static Ball getBall() {
		// TODO Auto-generated method stub
		return ball;
	}

}
