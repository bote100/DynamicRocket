package net.bote.dynamicrocket.command;

/**
 * @author Elias Arndt
 *
 * This interface is used for DynamicRocket commands. For executing commands the method {@link #execute(String[])} will be called.
 */
public interface RocketCommand {

    /**
     * Execute command
     * @param args arguments of the command
     */
	void execute(String[] args);

    /**
     * @return The name of the command
     */
	String commandName();

	String getCommandHelp();

}
