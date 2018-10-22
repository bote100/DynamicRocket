package net.bote.dynamicrocket.console;

import java.util.Date;

/**
 * @author Elias Arndt
 *
 * With a logger, you can print in the console messages, with diffrent states and prefix.
 */
public class Logger {
	
	private String message;
	private LogLevel level;
	private Date date = new Date(System.currentTimeMillis());

	/**
	 * Constructor
	 * @param log Message
	 * @param loglevel {@link LogLevel} of the log (INFO, WARNING, ERROR)
	 */
	public Logger(String log, LogLevel loglevel) {
		this.level = loglevel;
		this.message = log;
	}

	/**
	 * Print the log message in console. The log message was defined in the contructor of {@link Logger} .
	 */
	public void log() {
		switch (level) {
		case INFO:
			System.out.println("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " | INFO] " + this.message);
			break;
		case WARNING:
			System.out.println("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " | WARNING] " + this.message);
			break;
		case ERROR:
			System.err.println("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " | ERROR] " + this.message);
			break;
		default:
			new NullPointerException("LogLevel can't be null");
			break;
		}
	}

}
