package data;

import static helpers.Artist.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Boot {
	
	private boolean shutdown = false;
	
	public Boot(){
		beginSession();
		
		while(!shutdown){
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			isEscapePressed();
			
			drawQuad(0, 750, 600, 400);
			drawCircle(200, 685, 70, 20);
			
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
