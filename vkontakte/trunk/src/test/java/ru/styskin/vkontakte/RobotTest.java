package ru.styskin.vkontakte;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class RobotTest extends AbstractDependencyInjectionSpringContextTests {
	
	private Robot robot;

	public RobotTest() {
		super();		
		DOMConfigurator.configure("bin/log4j-config.xml");
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
