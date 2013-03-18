package utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Parser extends Thread {
	
	WorldState world;
	DatagramSocket socket;
	
	public Parser(WorldState world, DatagramSocket socket) {
		this.world = world;
		this.socket = socket;
	}
	
	public void run() {
		while (true) {
		    byte[] receiveData = new byte[1024];
			DatagramPacket p = new DatagramPacket(receiveData, receiveData.length);
			try {
				socket.receive(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String message = new String(p.getData());
			parse(message);
			
		}
	}

	private void parse(String message) {
		System.out.println(message);
		if (message.contains("hear")) {
			if(message.contains("kick_off")) {
				world.setState(WorldState.KICK_OFF);
			}
			else if(message.contains("play_on")) {
				world.setState(WorldState.PLAY_ON);
			}
		}
		else if (message.contains("see")) {
			if(message.contains("ball")) {
				String[] stringAfterBall = message.split("(ball)")[1].split("[ \\)]");
				world.setDistToBall(Double.parseDouble(stringAfterBall[2]));
				world.setAngleToBall(Double.parseDouble(stringAfterBall[3]));
			} else {
				world.setAngleToBall(Constants.Params.NOT_DEFINED);
			}
			if(message.contains("goal r")) {
				String[] stringAfterGoal = message.split("(goal r)")[1].split("[ \\)]");
				world.setDistToEnemyGoal(Double.parseDouble(stringAfterGoal[2]));
				world.setAngleToEnemyGoal(Double.parseDouble(stringAfterGoal[3]));
			} else {
				world.setAngleToEnemyGoal(Constants.Params.NOT_DEFINED);
			}
		}
	}
}
