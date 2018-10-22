package net.bote.dynamicrocket.console;

/**
 * @author Elias Arndt
 *
 * This class contains method which could be utility for the console.
 */
public class ConsoleUtils {
	
	public static Thread thread;
	public static boolean running = true;

	/**
	 * Constructor for this class
	 */
	public ConsoleUtils() {}
	
	public void startReader() {
		thread.start();
	}
	
	public void stopReader() {
		thread.stop();
	}

	/**
	 * Print a Ascii Logo of DynamicRocket in console on launching.
	 */
	public void printAscii() {
		System.out.println("\r\n" + 
				"\r\n" + 
				"______                             _       ______           _        _   \r\n" + 
				"|  _  \\                           (_)      | ___ \\         | |      | |  \r\n" + 
				"| | | |_   _ _ __   __ _ _ __ ___  _  ___  | |_/ /___   ___| | _____| |_ \r\n" + 
				"| | | | | | | '_ \\ / _` | '_ ` _ \\| |/ __| |    // _ \\ / __| |/ / _ \\ __|\r\n" + 
				"| |/ /| |_| | | | | (_| | | | | | | | (__  | |\\ \\ (_) | (__|   <  __/ |_ \r\n" + 
				"|___/  \\__, |_| |_|\\__,_|_| |_| |_|_|\\___| \\_| \\_\\___/ \\___|_|\\_\\___|\\__|\r\n" + 
				"        __/ |                                                            \r\n" + 
				"       |___/                                                             \r\n" + 
				"\r\n" + 
				"");
	}

	public void printStopMessage() {
		System.out.println("\n" +
				"\n" +
				"  ____             _ \n" +
				" |  _ \\           | |\n" +
				" | |_) |_   _  ___| |\n" +
				" |  _ <| | | |/ _ \\ |\n" +
				" | |_) | |_| |  __/_|\n" +
				" |____/ \\__, |\\___(_)\n" +
				"         __/ |       \n" +
				"        |___/        \n" +
				"\n");
	}
}
