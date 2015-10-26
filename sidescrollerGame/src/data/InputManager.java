package data;

import org.lwjgl.input.Keyboard;

public class InputManager {

	public void keyListen(){

		while(Keyboard.next()){
			if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
			    if (Keyboard.getEventKeyState()) { // on key press
			    	GameObjects.getBall().setY(GameObjects.getBall().getY() - 10 * Clock.getDelta());
			    }else{ // on key release
			    	
			    }
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			Boot.shutdown = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			GameObjects.getBall().setX(GameObjects.getBall().getX() - 1 * Clock.getDelta());
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			GameObjects.getBall().setX(GameObjects.getBall().getX() + 1 * Clock.getDelta());
		}
	}
	
	
}
