package ru.styskin.vkontakte;

import java.util.TimerTask;

public class Reloader extends TimerTask {
	
	private Robot robot;

	@Override
	public void run() {
		robot.run();
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}
}
