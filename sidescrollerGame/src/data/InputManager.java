package data;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import Foreground.Foreground;

public class InputManager {

	public void keyListen(){

		while(Keyboard.next()){
			if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
			    if (Keyboard.getEventKeyState()) { // on key press
			    	GameObjects.getBall().jump();
			    }else{ // on key release
			    	
			    }
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){ // shutdown if escape key pressed
			Boot.shutdown = true;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			if(GameObjects.getBall().getX() <= 200 && isBallAtStart()){
				for(Foreground o: GameObjects.getForeground().getForegroundElements()){
					o.setX(o.getX() + 0.7f * Clock.getDelta());
				}
			}else{
				GameObjects.getBall().setX(GameObjects.getBall().getX() - 1 * Clock.getDelta());
			}	
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			if(GameObjects.getBall().getX() >= Display.getWidth() - 800){
				for(Foreground o: GameObjects.getForeground().getForegroundElements()){
					o.setX(o.getX() - 0.7f * Clock.getDelta());
				}
			}else{
				GameObjects.getBall().setX(GameObjects.getBall().getX() + 1 * Clock.getDelta());
			}	
		}	
	}
	
	public boolean isBallAtStart(){
		return (GameObjects.getBall().getX() - GameObjects.getForeground().getForegroundElements().get(0).getX() >= 220);
	}
	
}
