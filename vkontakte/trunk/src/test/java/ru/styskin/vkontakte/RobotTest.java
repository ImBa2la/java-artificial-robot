package ru.styskin.vkontakte;

import org.apache.log4j.BasicConfigurator;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class RobotTest extends AbstractDependencyInjectionSpringContextTests {
	
	private Robot robot;

	public RobotTest() {
		super();		
		BasicConfigurator.configure();
		super.setAutowireMode(AUTOWIRE_BY_NAME);
	}	
	
	public void testRobot() {
		robot.run();
	}	

	protected String[] getConfigLocations() {
		return new String[] { "vkontakte.xml" };
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	
}