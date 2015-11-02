package data;

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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)){
			if(GameObjects.getBall().getX() <= 200 && isBallAtStart()){
				for(Foreground o: GameObjects.getForeground().getForegroundElements()){
						o.setX(o.getX() + 0.7f * Clock.getDelta());
				}	
			}else{
				GameObjects.getBall().setX(GameObjects.getBall().getX() - 1 * Clock.getDelta());
			}	
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){	
			Foreground closest = GameObjects.getBall().getClosestObject();
			if (closest.getRotation() != 0 && GameObjects.getBall().isRotatedObjectColliding() && GameObjects.getBall().getX() >= Display.getWidth() - 400) {
				if (closest.getRotation() < 0) {
					float offset = GameObjects.getBall().getOffset().getX();
					float offsety = GameObjects.getBall().getOffset().getY();
					for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
						o.setX(o.getX() - 0.7f * Clock.getDelta());
						o.setX(o.getX() + offset);
						o.setY(o.getY() - offsety * 2);
						
					}
					//System.out.println("x: " + GameObjects.getBall().getX());
				}
					
			}else{
				if(GameObjects.getBall().getX() >= Display.getWidth() - 400){
					for (Foreground o : GameObjects.getForeground()
							.getForegroundElements()) {
						o.setX(o.getX() - 0.7f * Clock.getDelta());
					}
				}else{
					GameObjects.getBall().setX(GameObjects.getBall().getX() + 1 * Clock.getDelta());
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
