package helpers;


import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;



import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Artist {

	
	public static void beginSession(){
		Display.setTitle("Pong");
		
		try {
			//Display.getDesktopDisplayMode();
			Display.setFullscreen(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,Display.getWidth(),Display.getHeight(),0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D); 
		//glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
	}
	
	public static void drawQuad(float x, float y, float width, float height){
		glBegin(GL_QUADS);
		glVertex2f(x,y);
		glVertex2f(x + width,y);
		glVertex2f(x + width,y + height);
		glVertex2f(x,y + height);
		
		glEnd();
	}
	
	public static void drawCircle(float x, float y, float radius, int sides){
		//glEnable(GL_LINE_SMOOTH);
		//glLineWidth(5);
		
		glBegin(GL_TRIANGLE_FAN);
		//glBegin(GL_LINE_LOOP);
		
		
		for (int i = 0; i < 360; i += 360 / sides)
		{
		    double angle = i * Math.PI / 180; // increment angle by i each iteration
		    glVertex2d(x + Math.cos(angle) * radius, y + Math.sin(angle) * radius);
		}

		glEnd();
		glLoadIdentity();
	}
	
	public static void drawQuadTexture(Texture text, float x, float y, float width, float height){
		text.bind(); // sets current texture
		// x,y are coords of top left vertex, gltranslate allows us to set coords of
		// texture using local coords (0,0)-(1,0)-(1,1)-(0,1)
	
		glTranslatef(x,y,0); 
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(0,0);
		glTexCoord2f(1,0);
		glVertex2f(width,0);
		glTexCoord2f(1,1);
		glVertex2f(width,height);
		glTexCoord2f(0,1);
		glVertex2f(0,height);
		glEnd();
		glLoadIdentity();
		
	}
	
	public static void drawNet(){
	    for(int i=15;i<1100;i+=100){
	    	drawQuad(Display.getWidth()/2-20, i, 20, 50);
	    }
	}
	
	public static void drawNumbers(Texture text, int num, float x, float y){
		if(num < 10){
			int width = 100, height = 113;
			float x1 = num * 0.065f, x2 = (num + 1) * 0.065f, y1 = 0, y2 = 1; // diff between x and y = 0.065

			text.bind();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
			glTexCoord2f(x1, y1);
			glVertex2f(0, 0);
			glTexCoord2f(x2, y1);
			glVertex2f(width, 0);
			glTexCoord2f(x2, y2);
			glVertex2f(width, height);
			glTexCoord2f(x1, y2);
			glVertex2f(0, height);
			glEnd();
			glLoadIdentity();
		}
	}

	public static Texture loadTexture(String path, String fileType){
		Texture tex = null;
		InputStream in = ResourceLoader.getResourceAsStream(path);
		try {
			tex = TextureLoader.getTexture(fileType, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tex;
	}
}
