package data;

import static helpers.Artist.loadTexture;

import org.lwjgl.opengl.Display;

import Foreground.Foreground;
import Foreground.SolidGround;

public class GameObjects {
	private static Ball ball;
	private static Foreground foreground;
	
	public static void loadGameObjects(){
		ball = new Ball(200,178,70,20);
		foreground = new Foreground();
		// texture width/height must be multiple of 16
		//foreground.addElement(new SolidGround(loadTexture("res/ground.png","PNG"), 1500,100,256,256));
		//foreground.addElement(new SolidGround(loadTexture("res/ground2.png","PNG"), 700,20,700,512));
		foreground.addElement(new SolidGround(loadTexture("res/groundLarge.png","PNG"), 0,Display.getHeight()-512,1024,512));
	}
	
	public static Foreground getForeground(){
		return foreground;
	}
	
	public static Ball getBall() {
		// TODO Auto-generated method stub
		return ball;
	}

}
