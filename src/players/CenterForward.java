package players;

import utilities.Constants;
import utilities.WorldState;

public class CenterForward extends Agent {

	public static void main(String[] args) {
		new CenterForward();
	}

	public CenterForward() {
		super(Constants.Team.CENTER_FORWARD);
	}

	@Override
	public void run() {
		while (true) {
			if (world.hasNewData()) {
				switch (world.getState()) {
				case WorldState.PLAY_ON:
					if (canSeeBall()) {
						if(!tryToKick()) {
							runToBall();
						}
					} else {
						//turn(45);
					}
					break;
				case WorldState.BEFORE_KICK_OFF:
					moveFriendlyKickoff();
				default:
				}
				world.dataProcessed();
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean tryToKick() {
		if (world.getDistToBall() < Double.parseDouble(world.getServerParam("kickable_margin"))) {
			kick(100, world.getAngleToEnemyGoal());
			return true;
		} else {
			return false;
		}
	}
}
