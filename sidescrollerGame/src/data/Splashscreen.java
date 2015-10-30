package data;

import static helpers.Artist.drawQuadTexture;
import static helpers.Artist.loadTexture;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import stateManager.States;

public class Splashscreen {

	private static Texture game_over = loadTexture("res/game_over.png","PNG");
	private static Texture play_again = loadTexture("res/play_again.png","PNG");
	private static Texture start_game = loadTexture("res/start_game.png","PNG");
	private static Texture intro = loadTexture("res/intro.png","PNG");
	private static int startX = Display.getWidth()/2 - 150,startY = Display.getHeight()/2 + 150;
	private static int playX = Display.getWidth()/2 - 150,playY = Display.getHeight()/2;
	
	public static void draw(){
		if(States.getState() == States.GameStates.START){
			drawQuadTexture(intro,Display.getWidth()/2 - 500,Display.getHeight()/2 - 400,1024,512,0.0f);
			drawQuadTexture(start_game,startX,startY,256,128,0.0f);
		}else if(States.getState() == States.GameStates.PLAY){
			
			
		}else if(States.getState() == States.GameStates.END){
			drawQuadTexture(game_over,Display.getWidth()/2 - 275,Display.getHeight()/2 - 300,512,256,0.0f);
			drawQuadTexture(play_again,playX,playY,256,128,0.0f);
		}else{
			System.out.println("Error in splashscreen update method");
		}
	}

	public static Texture getPlay_again() {
		return play_again;
	}

	public static int getPlayX() {
		return playX;
	}

	public static int getPlayY() {
		return playY;
	}

	public static Texture getStart_game() {
		return start_game;
	}

	public static Texture getIntro() {
		return intro;
	}

	public static int getStartX() {
		return startX;
	}

	public static int getStartY() {
		return startY;
	}
}
