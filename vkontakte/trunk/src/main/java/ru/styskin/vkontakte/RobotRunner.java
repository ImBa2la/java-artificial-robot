package ru.styskin.vkontakte;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RobotRunner {
	
	public static void main(String[] args) throws Throwable {
		DOMConfigurator.configure("log4j-config.xml");
		new ClassPathXmlApplicationContext(getContextPath());
	}

	
	protected static String[] getContextPath() {
		return new String[] {"vkontakte.xml"};
	}
}
