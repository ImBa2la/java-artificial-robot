package ru.styskin.vkontakte;

import org.apache.log4j.BasicConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RobotRunner {
	
	public static void main(String[] args) throws Throwable {
		BasicConfigurator.configure();		
		new ClassPathXmlApplicationContext(getContextPath());
	}

	
	protected static String[] getContextPath() {
		return new String[] {"vkontakte.xml"};
	}
}
