/**
 * <p>Title: SIGHUP signal handler and logging functions</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Yandex</p>
 * @author Oleg Okhotnikov
 * @version 1.0
 */

package ru.yandex.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class LogSignalHandler implements SignalHandler {
	private static final String SIGNAL_NAME = "HUP";
	private static final String MESSAGE_HEAD = "Logfile turned over due to signal ";

	//private SignalHandler oldHandler;
	private Reloadable servant;
	private String logFile;
	private String config;

	/**
	 * Install new SIGHUP signal handler
	 *
	 * @param servant Servant which have to be <i>Reloadable</i>
	 * @param log     Log file name to append
	 * @param config  Config file name to read
	 * @return new handler
	 * @see #handle(sun.misc.Signal)
	 * @see Reloadable#reload()
	 */
	public static LogSignalHandler install(Reloadable servant, String log, String config) {
		LogSignalHandler logHandler = new LogSignalHandler();
		Signal.handle(new Signal(SIGNAL_NAME), logHandler);
		logHandler.servant = servant;
		logHandler.logFile = log;
		logHandler.config = config;
		return logHandler;
	}

	/**
	 * Start logging
	 *
	 * @param log    Log file name to append
	 * @param config Config file name to read
	 * @see #reopenLog(String,String)
	 */
	public static void startLog(String log, String config) {
		reopenLog(log, config);
	}

	/**
	 * Reopen logFile file
	 *
	 * @param logFile Log file name to append
	 * @param config  Config file name to read
	 */
	public static void reopenLog(String logFile, String config) {
		try {
			// закрываем старый вывод
			System.err.close();
			// создаем новый
			System.setOut(new PrintStream(new FileOutputStream(logFile, true)));
			System.setErr(System.out);
			// конфигурируем логгер
			DOMConfigurator.configure(config);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * SIGHUP signal handler
	 *
	 * @param sig Signal
	 * @see #install(Reloadable,String,String)
	 */
	public void handle(Signal sig) {
		Logger log = Logger.getLogger(LogSignalHandler.class);
		try {
			String message = MESSAGE_HEAD + sig;
			if (log.isInfoEnabled()) {
				log.info(message);
			}
			reopenLog(logFile, config);
			if (log.isInfoEnabled()) {
				log.info(message);
			}
			if (servant != null) {
				servant.reload();
			}
		} catch (Exception e) {
			log.error("Error while process Signal", e);
		}
	}
}
