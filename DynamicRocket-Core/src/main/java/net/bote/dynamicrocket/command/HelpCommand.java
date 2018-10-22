package net.bote.dynamicrocket.command;

import net.bote.dynamicrocket.DynamicRocketCore;

/**
 * @author Elias Arndt
 * created on 22.10.2018 at 18:29
 *
 * This method shows all commands, with it's help message.
 */

public class HelpCommand implements RocketCommand {

    @Override
    public void execute(String[] args) {
        DynamicRocketCore.getInstance().getConsole().sendMessage("Here is a command list:");
        DynamicRocketCore.getInstance().getCommandManager().getCommands().forEach(cmd -> {
            DynamicRocketCore.getInstance().getConsole().sendMessage(cmd.commandName() + " - " + cmd.getCommandHelp());
        });
    }

    @Override
    public String commandName() {
        return "help";
    }

    @Override
    public String getCommandHelp() {
        return "Show all avaible commands and a explanation";
    }
}
