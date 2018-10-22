package net.bote.dynamicrocket.command;

import java.util.ArrayList;
import java.util.List;

import net.bote.dynamicrocket.DynamicRocketCore;

/**
 * @author Elias Arndt
 *
 * This class exists to manage commands.
 */
public class CommandManager {
	
	private boolean exists = false;
	
	private List<RocketCommand> commands = new ArrayList<>();
	private static CommandManager instance = new CommandManager();

    /**
     * Register a new command.
     * @param arg0 RocketCommand class to register
     */
	public void registerCommand(RocketCommand arg0) {
		commands.add(arg0);
		DynamicRocketCore.getInstance().getConsole().sendMessage("Command '/" + arg0.commandName() + "' is now registered.");
	}

    /**
     * Checks if a RocketCommand is registered and avaible to execute.
     * @param arg0 RocketCommand class to check
     * @return if RocketCommand is registered
     */
	public boolean isRegistered(RocketCommand arg0) {
		return commands.contains(arg0);
	}

    /**
     * Unregister a RocketCommand
     * @param arg0 RocketCommand class to unregister
     */
	public void unregisterCommand(RocketCommand arg0) {
		commands.remove(arg0);
		DynamicRocketCore.getInstance().getConsole().sendMessage("Command '/" + arg0.commandName() + "' is now unregistered.");
	}

    /**
     * List of all registered commands
     * @return registered commands list
     */
	public List<RocketCommand> getCommands() {
		return commands;
	}

    /**
     * Get instance of the class.
     * @return instance
     */
	public static CommandManager getInstance() {
		return instance;
	}

    /**
     * Checks out of an command line if a command exists
     * @param line command line
     * @return if executed command exists
     */
	public boolean commandExists(String line) {
		boolean bol = false;
		DynamicRocketCore.getInstance().getCommandManager().getCommands().forEach(cmd -> {
			if(cmd.commandName().equalsIgnoreCase(line.split(" ")[0])) {
				if(!exists) {
					exists = true;
				}
			}
		});
		bol = exists;
		exists = false;
		return bol;
	}

}
