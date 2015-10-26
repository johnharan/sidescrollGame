package data;

import static helpers.Artist.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static data.GameObjects.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import stateManager.GameState;
import stateManager.Play;

public class Boot {
	
	private boolean shutdown = false;
	private Game_State state;
	private GameState start,play,end;
	
	public enum Game_State{
		START,PLAY,END;
	}
	
	public Boot(){
		beginSession();
		GameObjects loader = new GameObjects();
		loader.loadGameObjects();
		
		state = Game_State.PLAY;
		play = new Play();
				
		while(!shutdown){
			Clock.update();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			isEscapePressed();
						
			switch(state){
			case START:
				System.out.println("Start");
				break;
			case PLAY:
				play.drawState();
				break;
			case END:
				System.out.println("End");
				break;
			}			
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	public void isEscapePressed(){
		while(Keyboard.next()){
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				shutdown = true;
			}
		}	
	}
	
	public static void main(String[] args){
		new Boot();
		
	}


	
}
