package net.bote.dynamicrocket.command;

import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.console.LogLevel;
import net.bote.dynamicrocket.console.Logger;
import net.bote.dynamicrocket.lib.Document;

public class StopCommand implements RocketCommand {

	@Override
	/**
	 * Execute command
	 * This method use multithreading to add a shutdown hook.
	 * After executing this command the {@link net.bote.dynamicrocket.messenger.CommunicationServer} send a event
	 * for stopping all minecraft servers and the proxy.
	 * After this, the System exit with code 0.
	 */
	public void execute(String[] args) {
		if(args.length != 1) {
			new Logger("Please use 'stop'", LogLevel.WARNING).log();
			return;
		}

		Runtime rt = Runtime.getRuntime();
		rt.addShutdownHook(new Thread(() -> {
			DynamicRocketCore.getInstance().getConsole().sendMessage("Stopping servers...");
			DynamicRocketCore.getInstance().getCommunicationServer().getServer().
					getBroadcastOperations().sendEvent("Stop", new Document().convertToJson());
			DynamicRocketCore.getInstance().getConsole().sendMessage("Let disconnect communication server...");
			DynamicRocketCore.getInstance().getConsoleUtils().printStopMessage();
			DynamicRocketCore.getInstance().getCommunicationServer().getServer().stop();
		}));

		System.exit(0);

	}

	@Override
	public String commandName() {
		return "stop";
	}

	@Override
	public String getCommandHelp() {
		return "Stop DynamicRocket";
	}

}
