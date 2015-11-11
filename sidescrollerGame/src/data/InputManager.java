package data;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import stateManager.States;

import Foreground.Foreground;

public class InputManager {

	public void keyListen(){

		while(Keyboard.next()){
			if (Keyboard.getEventKey() == Keyboard.KEY_UP || Keyboard.getEventKey() == Keyboard.KEY_W) {
			    if (Keyboard.getEventKeyState()) { // on key press
			    	GameObjects.getBall().jump();
			    }else{ // on key release
			    	
			    }
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){ // shutdown if escape key pressed
			Boot.shutdown = true;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_R)){ // shutdown if escape key pressed
			States.setState(States.GameStates.START);
		}
		/*
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)){
	
				for(Foreground o: GameObjects.getForeground().getForegroundElements()){
						o.setX(o.getX() + 0.7f * Clock.getDelta());
						GameObjects.getBall().setX(GameObjects.getBall().getX() - 0.35f);
				}	
			
		}
		*/

		
		if(Keyboard.isKeyDown(Keyboard.KEY_G)){
			for (Foreground o : GameObjects.getForeground()
					.getForegroundElements()) {
				GameObjects.getBall().setX(GameObjects.getBall().getX() + 0.7f);
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			for (Foreground o : GameObjects.getForeground()
					.getForegroundElements()) {
				o.setX(o.getX() + 0.7f * Clock.getDelta()); // need to mix between this and below depending on slope size
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_T)){
			for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
				o.setX(o.getX() - 0.7f * Clock.getDelta()); // need to mix between this and below depending on slope size
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F)){
			GameObjects.getBall().setX(GameObjects.getBall().getX() + 18f);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			GameObjects.getBall().setX(GameObjects.getBall().getX() - 18f);
		}
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)){
			Foreground closest = GameObjects.getBall().getClosestObject();
			int numberObjects = GameObjects.getForeground().getForegroundElements().size();
			boolean isRotatedObjectColliding = GameObjects.getBall().isRotatedObjectColliding();
			float mainSpeed = 0.7f;
			
			if(closest.getRotation() >= 66.23f && isRotatedObjectColliding){ // if slope steep, move only ball
				float ballSpeed = mainSpeed * numberObjects;
				GameObjects.getBall().setX(GameObjects.getBall().getX() - ballSpeed);
			}else if (closest.getRotation() != 0  && isRotatedObjectColliding){
				float xoffset = GameObjects.getBall().getXoffset();
				float speedAdjust = (xoffset / numberObjects);
				for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
					o.setX(o.getX() + (mainSpeed-speedAdjust) * Clock.getDelta());
					GameObjects.getBall().setX(GameObjects.getBall().getX() - (0.0f + speedAdjust));
				}
			}else{
				float x = GameObjects.getBall().getX();
				float leftEdge = 400;
				if(x <= leftEdge){
					for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
						o.setX(o.getX() + mainSpeed * Clock.getDelta());
					}
				}else{
					float ballSpeed = mainSpeed * numberObjects;
					GameObjects.getBall().setX(GameObjects.getBall().getX() - ballSpeed);
				}
			}		
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){
			Foreground closest = GameObjects.getBall().getClosestObject();
			int numberObjects = GameObjects.getForeground().getForegroundElements().size();
			boolean isRotatedObjectColliding = GameObjects.getBall().isRotatedObjectColliding();
			float mainSpeed = 0.7f;
			
			if(closest.getRotation() <= -66.23f && isRotatedObjectColliding){ // if slope steep, move only ball
				float ballSpeed = mainSpeed * numberObjects;
				GameObjects.getBall().setX(GameObjects.getBall().getX() + ballSpeed);
			}
			else if (closest.getRotation() != 0 && isRotatedObjectColliding){
				float xoffset = GameObjects.getBall().getXoffset();
				float speedAdjust = -(xoffset / numberObjects);
				for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
					o.setX(o.getX() - (mainSpeed-speedAdjust) * Clock.getDelta());
					GameObjects.getBall().setX(GameObjects.getBall().getX() + (0.0f + speedAdjust));
				}
			}else{
				float x = GameObjects.getBall().getX();
				float rightEdge = Display.getWidth() - 500;
				if(x >= rightEdge){
					for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
						o.setX(o.getX() - mainSpeed * Clock.getDelta());
					}
				}else{
					float ballSpeed = mainSpeed * numberObjects;
					GameObjects.getBall().setX(GameObjects.getBall().getX() + ballSpeed);
				}
			}		
		}
	}
	
	public void checkForReset(){
		int mouseY = Display.getHeight() - Mouse.getY(); // need to invert mouse y
		// below checks for mouse click inside playAgain box

		if(States.getState() == States.GameStates.END){
			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) || (Mouse.isButtonDown(0)
					&& Mouse.getX() >= Splashscreen.getPlayX()
					&& Mouse.getX() <= Splashscreen.getPlayX() + Splashscreen.getPlay_again().getImageWidth()
					&& mouseY >= Splashscreen.getPlayY()
					&& mouseY <= Splashscreen.getPlayY() + Splashscreen.getPlay_again().getImageHeight())) {
				// GameObjects.getBall().setX(GameObjects.getForeground().getForegroundElements().get(0).getX()
				// + 400);
				// GameObjects.getBall().setY(GameObjects.getForeground().getForegroundElements().get(0).getY()
				// - 300);
				GameObjects.loadGameObjects(); // lazy way to restart
				States.setState(States.GameStates.PLAY);
			}
		}else if(States.getState() == States.GameStates.START){
			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Mouse.isButtonDown(0)
					&& Mouse.getX() >= Splashscreen.getStartX()
					&& Mouse.getX() <= Splashscreen.getStartX() + Splashscreen.getStart_game().getImageWidth()
					&& mouseY >= Splashscreen.getStartY()
					&& mouseY <= Splashscreen.getStartY() + Splashscreen.getStart_game().getImageHeight()) {
				// GameObjects.getBall().setX(GameObjects.getForeground().getForegroundElements().get(0).getX()
				// + 400);
				// GameObjects.getBall().setY(GameObjects.getForeground().getForegroundElements().get(0).getY()
				// - 300);
				GameObjects.loadGameObjects(); // lazy way to restart
				States.setState(States.GameStates.PLAY);
			}
		}
	}
	
	public boolean isBallAtStart(){
		return (GameObjects.getBall().getX() - GameObjects.getForeground().getForegroundElements().get(0).getX() >= 220);
	}
	
}
