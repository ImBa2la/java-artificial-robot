package ru.yandex.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Application level utilities.
 * 
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 * @author ashevenkov
 */
public final class ApplicationUtils {
	private ApplicationUtils() {
	}

	public static String getHostName() {
		String hostName = System.getProperty("host.name");
		if (hostName == null) {
			try {
				hostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				return "unknown";
			}
		}
		return hostName;
	}
}