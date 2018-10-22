package net.bote.dynamicrocket.command;

import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.lib.Document;

/**
 * @author Elias Arndt
 * This is a command which is used to test / debug the application. If the version is released, a normal
 * author message will be send.
 */
public class DeveloperCommand implements RocketCommand {

    @Override
    /**
     * Executes command
     */
    public void execute(String[] args) {
        DynamicRocketCore.getInstance().getConsole().sendMessage("DynamicRocket coded by bote100");

        /*
        System.out.println("size: " + DynamicRocketCore.getInstance().getOnlinePlayers().size());

        System.out.println("1");

        DynamicRocketCore.getInstance().getPlayer("bote100").sendMessage("§3Rocket §8| §4TEST TEST TEST");

        System.out.println("3");
        */
    }

    @Override
    public String commandName() {
        return "dev";
    }

    @Override
    public String getCommandHelp() {
        return "Debugging and info command";
    }

}
