package net.bote.dynamicrocket.command;

import java.util.List;

import net.bote.dynamicrocket.DynamicRocketCore;

/**
 * @author Elias Arndt
 *
 * /reload Command
 */
public class CommandReload implements RocketCommand {

	@Override
	/**
	 * Executes command
	 */
	public void execute(String[] args) {
		DynamicRocketCore.getInstance().getConsole().sendMessage("Reloading Rocket...");
		
		DynamicRocketCore.getInstance().getFileManager().createConfigs();
		DynamicRocketCore.getInstance().getConsole().sendMessage("Reloading & build configs...");
		
		// Reload bungee server
		DynamicRocketCore.getInstance().getConsole().sendMessage("Reloading Proxy-Server");
		DynamicRocketCore.getInstance().updateBungeeCord();

		DynamicRocketCore.getInstance().getConsole().sendMessage("Reload completed!");;

	}

	@Override
	public String commandName() {
		return "reload";
	}

	@Override
	public String getCommandHelp() {
		return "Reload DynamicRocket";
	}

}
