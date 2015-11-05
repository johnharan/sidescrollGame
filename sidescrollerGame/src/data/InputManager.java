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
	
				for(Foreground o: GameObjects.getForeground().getForegroundElements()){
						o.setX(o.getX() + 0.7f * Clock.getDelta());
						GameObjects.getBall().setX(GameObjects.getBall().getX() - 0.35f);
				}	
			
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F)){
			GameObjects.getBall().setX(GameObjects.getBall().getX() + 10f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			GameObjects.getBall().setX(GameObjects.getBall().getX() - 10f);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){
			/*
			if(GameObjects.getBall().getX() >= Display.getWidth() - 800){
				GameObjects.getBall().setX(GameObjects.getBall().getX());
				for (Foreground o : GameObjects.getForeground()
						.getForegroundElements()) {
					o.setX(o.getX() - 0.35f * Clock.getDelta());
					GameObjects.getBall().setX(GameObjects.getBall().getX() + 0.35f);
					}
				
			}else{
			*/
			for (Foreground o : GameObjects.getForeground()
					.getForegroundElements()) {
				o.setX(o.getX() - 0.25f * Clock.getDelta()); // need to mix between this and below depending on slope size
				GameObjects.getBall().setX(GameObjects.getBall().getX() + 0.45f);
			}
			
			/*
			if(GameObjects.getBall().getClosestObject().getRotation() > 0){
				GameObjects.getBall().setX(GameObjects.getBall().getX());
				for (Foreground o : GameObjects.getForeground()
						.getForegroundElements()) {
					o.setX(o.getX() - 0.7f * Clock.getDelta());
					}
			}
			*/
			
			/*
			if(GameObjects.getBall().getX() > Display.getWidth() - 400){
				GameObjects.getBall().setX(Display.getWidth() - 400);
				for (Foreground o : GameObjects.getForeground()
						.getForegroundElements()) {
					o.setX(o.getX() - 0.7f * Clock.getDelta());
					}
			}else{
			for (Foreground o : GameObjects.getForeground()
						.getForegroundElements()) {
					o.setX(o.getX() - 0.7f * Clock.getDelta());
					}
			}
			*/
		}
		
		/*
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){	
			Foreground closest = GameObjects.getBall().getClosestObject();
			if (closest.getRotation() < 0 && GameObjects.getBall().isRotatedObjectColliding() && GameObjects.getBall().getX() >= Display.getWidth() - 400) {
				float normalised_rotation = (closest.getRotation() + 180)/(180 + 180);
				System.out.println("normalised rotation: " + normalised_rotation);
				float offsetX = GameObjects.getBall().getOffset().getX();
				float offsetY = GameObjects.getBall().getOffset().getY();
				float changeY = GameObjects.getBall().getChangeY() / 4;
				for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
					///////////////////////////////////////
					// need to decay the 0.7f depending on rotation,
					// so a normalised range between maybe 0.7 as max and ~0.1 is min
					//o.setX(o.getX() - 0.1f * Clock.getDelta()); 
					//o.setY(o.getY() - 2.1f * Clock.getDelta()); 
					///////////////////////////////////////
					//o.setX(o.getX() + offsetX);
					System.out.println("offsetX: " + offsetX + ",offsetX / 40: " + offsetX/40);
					//o.setY(o.getY() - offsetY);
					//o.setY(o.getY() - changeY);
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
		*/	
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
