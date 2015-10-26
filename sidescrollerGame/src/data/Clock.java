package data;


public class Clock {
	private static long lastTime,currentTime;
	private static float delta;
	private static boolean paused;
	

	public static void update(){
		delta = calculateDelta();
	}

	public static float calculateDelta(){
		currentTime = System.nanoTime();
		delta = (currentTime - lastTime) / 1000000; // delta is the duration of time since last frame/iteration of loop
		lastTime = currentTime;
		if(delta >= 20){ // restricting delta time to 20ms
			delta = 20;
		}
		return delta;
	}
	
	public static float getDelta(){
		if(paused == true){
			return 0f;
		}else{
			return delta;
		}
	}

	public static boolean isPaused() {
		return paused;
	}

	public static void setPaused(boolean paused) {
		Clock.paused = paused;
	}
	
	

}
