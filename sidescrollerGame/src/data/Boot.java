package data;

import static helpers.Artist.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import org.lwjgl.opengl.Display;

import stateManager.States;

public class Boot {
	
	public static boolean shutdown = false;
	private InputManager input;
	
	public Boot(){
		beginSession();

		GameObjects.loadGameObjects();
		
		input = new InputManager();
		
		new States(States.GameStates.START);
					
		while(!shutdown){
			Clock.update();
			input.keyListen();	
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			

			switch(States.getState()){
			case START:
				States.getInstance().drawState();
				input.checkForReset();
				break;
			case PLAY:
				States.getInstance().updateState();
				States.getInstance().drawState();
				break;
			case END:
				States.getInstance().drawState();
				input.checkForReset();
				break;
			}			
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}
	
	public static void main(String[] args){
		new Boot();
		
	}

}
