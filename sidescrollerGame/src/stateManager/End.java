package stateManager;

import data.GameObjects;


public class End implements GameState{

	public void updateState() {

	}

	public void drawState() {
		GameObjects.getBall().drawBall();
		GameObjects.getForeground().draw();
	}

	public void checkGameReset(){

	}
}
