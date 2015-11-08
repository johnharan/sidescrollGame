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
			if (closest.getRotation() != 0  && GameObjects.getBall().isRotatedObjectColliding()){
				float xoffset = GameObjects.getBall().getXoffset();
				float speed = (xoffset / numberObjects);
				System.out.println(xoffset);
				// find what balance makes ball stay in same position (the x position at which it entered a slope)
				float rotation = GameObjects.getBall().getClosestObject().getRotation();
				for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
					o.setX(o.getX() + (0.7f-speed) * Clock.getDelta());
					GameObjects.getBall().setX(GameObjects.getBall().getX() - (0.0f + speed));
				}
				System.out.println("x: " + GameObjects.getBall().getX() + ",delta: " + Clock.getDelta() + ",rotation: " + rotation);
				
			}else{
				float x = GameObjects.getBall().getX();
				float leftEdge = 400;
				if(x <= leftEdge){
					for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
						o.setX(o.getX() + (0.7f) * Clock.getDelta());
					}
				}else{
					float ballSpeed = 0.7f * numberObjects;
					GameObjects.getBall().setX(GameObjects.getBall().getX() - ballSpeed);
				}
			}		
		}
		
		
		// at -63.35 the ball comes to a complete stop, any lower and it will move up, any higher and it will move down
		// so <= -63.35 -> move ball only at 0.7f
		// and > -63.35 -> as slope decreases add more to foreground x, and take from ball x 
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){
			Foreground closest = GameObjects.getBall().getClosestObject();
			Foreground closestRotated = GameObjects.getBall().getClosestRotatedObject();
			System.out.println(closestRotated.getRotation());
			int numberObjects = GameObjects.getForeground().getForegroundElements().size();
			
			if (closest.getRotation() != 0 && GameObjects.getBall().isRotatedObjectColliding()){
				float xoffset = GameObjects.getBall().getXoffset();
				float speed = -(xoffset / numberObjects);
				System.out.println(xoffset);
				// find what balance makes ball stay in same position (the x position at which it entered a slope)
				float rotation = GameObjects.getBall().getClosestObject().getRotation();
				for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
					o.setX(o.getX() - (0.7f-speed) * Clock.getDelta());
					GameObjects.getBall().setX(GameObjects.getBall().getX() + (0.0f + speed));
				}
				System.out.println("x: " + GameObjects.getBall().getX() + ",delta: " + Clock.getDelta() + ",rotation: " + rotation);
				
			// to do: need to stop ball if it reaches a wall
			}else{
				float x = GameObjects.getBall().getX();
				float rightEdge = Display.getWidth() - 500;
				if(x >= rightEdge){
					for (Foreground o : GameObjects.getForeground().getForegroundElements()) {
						o.setX(o.getX() - (0.7f) * Clock.getDelta());
						
					}
				}else{
					float ballSpeed = 0.7f * numberObjects;
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
