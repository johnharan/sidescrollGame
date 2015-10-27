package Foreground;

import org.newdawn.slick.opengl.Texture;
import static helpers.Artist.*;

public class SolidGround extends Foreground{
	
	private float x, y, width, height;
	private Texture text;
	
	public SolidGround(Texture text, float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}
	
	public void update(){
		
	}
	
	public void draw(){
		drawQuadTexture(text, x, y, width, height);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	
	
}
