package stateManager;


import static helpers.Artist.drawQuad;
import data.GameObjects;



public class Play implements GameState{

	public void updateState() {
		GameObjects.getBall().updateBall();
	}

	public void drawState() {
		GameObjects.getBall().drawBall();
		GameObjects.getForeground().draw();
	}
	
	
	public void timedMissAdjustment(){
		if (Thread.activeCount() <= 10) { // this allows the main thread plus max of one timer thread. need to account for sound effect threads also
			Thread timedMissAdjustment = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(2000); // wait 2 seconds
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			timedMissAdjustment.start();
		}
	}
	
	public void timedRespawn(){
		if (Thread.activeCount() <= 10) { // this allows the main thread plus max of one timer thread																											
			Thread timedRespawn = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1500); // wait 1 second then respawn ball
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			timedRespawn.start();
		}
	}

}
