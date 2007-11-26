package ru.styskin.vkontakte;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RobotRunner {
	
	public static void main(String[] args) throws Throwable {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(getContextPath());
		Robot robot = (Robot) applicationContext.getBean("robot");
		while(true) {
			robot.run();
			Thread.sleep(5*60*1000L);
		}
	}

	
	protected static String[] getContextPath() {
		return new String[] {"vkontakte.xml"};
	}
}
