package stateManager;

import data.Splashscreen;


public class Start implements GameStateInterface{

	public void updateState() {

	}

	public void drawState() {
		Splashscreen.draw();
	}

}
