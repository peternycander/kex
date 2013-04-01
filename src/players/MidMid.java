package players;

import utilities.Constants;
import utilities.WorldState;

public class MidMid extends Agent {

	public static void main(String[] args) {
		new MidMid();
	}

	private int notLookedAroundSince;

	public MidMid() {
		super(Constants.Team.MID_MID);
	}

	@Override
	public void run() {
		while (true) {
			if (world.hasNewData()) {
				switch (world.getState()) {
				case WorldState.PLAY_ON:
					playLogic();
					break;
				case WorldState.ENEMY_KICK_OFF:
					break;
				case WorldState.FRIENDLY_KICK_OFF:
					break;
				case WorldState.BEFORE_KICK_OFF:
					moveFriendlyKickoff();
					break;
				case WorldState.FRIENDLY_FREE_KICK:
					break;
				case WorldState.ENEMY_FREE_KICK:
					break;
				case WorldState.FRIENDLY_KICK_IN:
					break;
				case WorldState.ENEMY_KICK_IN:
					break;
				case WorldState.FRIENDLY_CORNER_KICK:
					break;
				case WorldState.ENEMY_CORNER_KICK:
					break;
				default:
					break;
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

	private void playLogic() {
		if (canSeeBall() && world.getDistToBall() < Constants.Params.TAKE_BALL_DISTANCE*1.5) {
			if (world.getDistToBall() < Double.parseDouble(world
					.getServerParam("kickable_margin"))) {
				String passTarget = getPassTarget();
				if (world.getDistToEnemyGoal() < Constants.Params.SCORING_DISTANCE) {
					tryToScore();
				} else if (passTarget != null) {
					passForward(passTarget);
				} else {
					dribble();
				}
			} else {
				if (friendlyPlayerChasingBall()) {
					approachBall();
				} else {
					runToBall();
				}
			}
		} else {
			runToSlot();
		}
	}
	private void tryToScore() {

		if (canSeeEnemyGoal()) {
			kick(100, world.getAngleToEnemyGoal());
		} else {
			turn(Constants.Params.TURNING_LOOKING_ANGLE);
		}
	}


	private void runToSlot() {
		if (notLookedAroundSince > 20) {
			turn(180);
			notLookedAroundSince = 0;
			return;
		}
		notLookedAroundSince++;
		String target = "f c";
		if (world.getAngleToObject(target) != Constants.Params.NOT_DEFINED) {
			if (Math.abs(world.getAngleToObject(target)) > 10
					&& world.getDistanceToObject(target) > 15) {
				turn(world.getAngleToObject(target));
			} else {
				if (world.getDistanceToObject(target) < 20) {
					turn(Constants.Params.TURNING_LOOKING_ANGLE); // In slot position, look for ball
				} else {
					dash(Constants.Params.JOGGING_SPEED,
							world.getAngleToObject(target));
				}
			}
		} else {
			turn(Constants.Params.TURNING_LOOKING_ANGLE);
		}
	}

}