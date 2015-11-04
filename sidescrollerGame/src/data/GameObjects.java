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
		SolidGround large5 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 2580,2120,1024,512,-50.0f);
		SolidGround large6 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 500,2200,1024,512,0.0f);
		SolidGround large7 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 0,3400,1024,512,0.0f);
		SolidGround large8 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1050,3150,1024,512,-27.0f);
		SolidGround large9 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 2048,3700,1024,512,0.0f);
		SolidGround large10 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 3072,3700,1024,512,0.0f);
		SolidGround large11 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 2350,Display.getHeight()-1400,1024,512,-50.0f);
		SolidGround large12 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 2900,Display.getHeight()-2060,1024,512,-50.0f);
		SolidGround large13 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 3450,Display.getHeight()-2720,1024,512,-50.0f);
		SolidGround large14 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 4000,Display.getHeight()-3380,1024,512,-50.0f);
		SolidGround large15 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), -600,Display.getHeight()-900,1024,512,0.0f);
		SolidGround large16 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 450,Display.getHeight()-1640,1024,512,-40.0f);
		SolidGround large17 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1050,Display.getHeight()-2150,1024,512,-40.0f);
		SolidGround large18 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1650,Display.getHeight()-2660,1024,512,-40.0f);
		SolidGround large19 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 2300,Display.getHeight()-3200,1024,512,-40.0f);
		SolidGround large20 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 1400,Display.getHeight()-512,1024,512,0.0f);
		SolidGround large21 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 2450,Display.getHeight()-450,1024,512,0.0f);
		SolidGround large22 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 3474,Display.getHeight()-450,1024,512,0.0f);
		SolidGround large23 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 4498,Display.getHeight()-450,1024,512,0.0f);
		SolidGround large24 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 5522,Display.getHeight()-450,1024,512,0.0f);
		SolidGround large25 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 4498,Display.getHeight()-1100,1024,512,-70.0f);
		SolidGround large26 = new SolidGround(loadTexture("res/groundLarge.png","PNG"), 4000,3650,1024,512,-20.0f);
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
		foreground.addElement(large11);
		foreground.addElement(large12);
		foreground.addElement(large13);
		foreground.addElement(large14);
		foreground.addElement(large15);
		foreground.addElement(large16);
		foreground.addElement(large17);
		foreground.addElement(large18);
		foreground.addElement(large19);
		foreground.addElement(large20);
		foreground.addElement(large21);
		foreground.addElement(large22);
		foreground.addElement(large23);
		foreground.addElement(large24);
		foreground.addElement(large25);
		foreground.addElement(large26);
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
