package stateManager;


public class States {

	private static GameStates state;
	private static GameStateInterface start, play, end;
	
	public enum GameStates{
		START,PLAY,END;
	}
	
	public States(GameStates state){
		States.state = state;
		start = new Start();
		play = new Play();
		end = new End();
	}
	
	public static GameStates getState() {
		return state;
	}

	public static void setState(GameStates state) {
		States.state = state;
	}
	
	public static GameStateInterface getInstance(){
		if(state == GameStates.START){
			return start;
		}else if(state == GameStates.PLAY){
			return play;
		}else if(state == GameStates.END){
			return end;
		}
		return null;
	}
	
}
