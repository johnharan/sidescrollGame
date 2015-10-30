package Foreground;

import java.util.ArrayList;

public class Foreground {

	private ArrayList<Foreground> elements = new ArrayList<Foreground>();
	
	public void update(){
		//for(Foreground o: elements){}
			
		
	}
	
	public void draw(){
		for(Foreground o: elements){
			o.draw();
		}
	}
	
	public void addElement(Foreground o){
		elements.add(o);
	}
	
	public void removeElement(Foreground o){
		elements.remove(o);
	}
	
	public ArrayList<Foreground> getForegroundElements(){
		return elements;
	}
	
	public float getX() {
		return 0.0f;
	}

	public void setX(float x) {}

	public float getY() {
		return 0.0f;
	}

	public void setY(float y) {}
	
	public float getWidth() {
		return 0.0f;
	}

	public void setWidth(float width) {}

	public float getHeight() {
		return 0.0f;
	}

	public void setHeight(float height) {}
	
	public float getRotation() {
		return 0.0f;
	}
	
	public void setRotation(float rotation) {}
	
}
