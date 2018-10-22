package net.bote.dynamicrocket.command;

import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.console.Console;
import net.bote.dynamicrocket.console.LogLevel;
import net.bote.dynamicrocket.console.Logger;
import net.bote.dynamicrocket.messenger.MessageBlock;
import net.bote.dynamicrocket.server.GroupConfiguration;
import net.bote.dynamicrocket.server.MinecraftServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author Elias Arndt
 * created on 30.09.2018 at 22:22
 *
 * /creategroup command
 */

public class CreateServerGroupCommand implements RocketCommand {

    @Override
    /**
     * In this command the server create a new server group if it not exists. A new .json file
     * will be saved in the groups directory and a new templates folder will be created. If the procss
     * was successfully a new server of this group will start.
     */
    public void execute(String[] args) {
        Console console = DynamicRocketCore.getInstance().getConsole();
        if(args.length != 6) {
            console.sendMessage("Use /creategroup <Groupname> <M RAM per server> <Slots> <Start when 100 players are in group> <Start when 100 players are on the server> <LOBBY | SERVER>");
            return;
        }

        /*
        New values for .json configuration file
         */
        String name = args[0];
        int ram;
        int slots;
        int groupstart;
        int globalstart;
        boolean isLobby = false;
        try {
            ram = Integer.parseInt(args[1]);
            slots = Integer.parseInt(args[2]);
            groupstart = Integer.parseInt(args[3]);
            globalstart = Integer.parseInt(args[4]);
        } catch(NumberFormatException ex) {
            console.sendMessage("Use /creategroup <Groupname> <M RAM per server> <Slots> <Start when 100 players are in group> <Start when 100 players are on the server> <LOBBY | SERVER>");
            return;
        }

        if(args[5].equalsIgnoreCase("server")) {
            isLobby = false;
        } else if(args[5].equalsIgnoreCase("lobby")) {
            isLobby = true;
        } else {
            console.sendMessage("Use /creategroup <Groupname> <M RAM per server> <Slots> <Start when 100 players are in group> <Start when 100 players are on the server> <LOBBY | SERVER>");
        }

        if(new File("groups/" + name + ".json").exists()) {
            console.sendMessage("This group already exists!");
            return;
        }

        /*
        Creating .json file with arguments of the command
         */
        MessageBlock block = new MessageBlock();

        block.append("name", name);
        block.append("ram", ram);
        block.append("slots", slots);
        block.append("groupstart", groupstart);
        block.append("globalstart", globalstart);
        block.append("isLobby", isLobby);

        DynamicRocketCore.getInstance().getConsole().sendMessage("Creating servergroup " + name);

        try {
            final PrintWriter writer = new PrintWriter("groups/" + name + ".json", "UTF-8");
            writer.println(block.toJSON());
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            new Logger(ex.getMessage(), LogLevel.ERROR).log();
            return;
        }

        /*
        New templates folder
         */
        new File("templates/" + name).mkdir();
        new File("templates/"+name+"/plugins").mkdir();

        DynamicRocketCore.getInstance().getConsole().sendMessage("Successfully created server group " + name);

        MinecraftServer server = DynamicRocketCore.getInstance().getServerManager().startRocketServer(new GroupConfiguration(slots, ram, name, groupstart, globalstart, isLobby));

    }

    @Override
    public String commandName() {
        return "creategroup";
    }

    @Override
    public String getCommandHelp() {
        return "Create a new ServerGroup";
    }
}
