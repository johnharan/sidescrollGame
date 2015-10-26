package Foreground;

import java.util.ArrayList;

public class Foreground {
	
	private ArrayList<Foreground> elements = new ArrayList<Foreground>();
	
	public void update(){
		
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

}
