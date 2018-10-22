package net.bote.dynamicrocket.bootstrap;

import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.command.*;
import net.bote.dynamicrocket.console.ConsoleUtils;
import net.bote.dynamicrocket.console.LogLevel;
import net.bote.dynamicrocket.console.Logger;
import net.bote.dynamicrocket.messenger.CommunicationServer;
import net.bote.dynamicrocket.messenger.MessageBlock;
import net.bote.dynamicrocket.server.GroupConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Elias Arndt
 * created on 20.10.2018 at 12:03
 *
 * This class is the main class of the java application. The {@link #main(String[])} method launchs DynamicRocket.
 */

public class RocketLauncher {

    /**
     * Starts java application
     * @param args {@link #main(String[])} standard start parameter of java
     */
    public static void main(String[] args) {

        if(Float.parseFloat(System.getProperty("java.class.version")) < 52D) {
            System.out.println("DynamicRocket requiere Java 8 or 10.0.1 ! Check your Java Version with: java -version");
            System.out.println("Stopping application in 5 seconds...");

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
            return;
        }

        DynamicRocketCore.setInstance(new DynamicRocketCore());

        DynamicRocketCore.getInstance().getConsole().sendMessage("Starting DynamicRocket...");

        // delete all server of the session before
        new File("running").delete();

        new ConsoleUtils().printAscii();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(DynamicRocketCore.getInstance().getFileManager().isFirstStart()) {
            DynamicRocketCore.getInstance().getConsole().sendMessage("Launching Rocket first time!");
            DynamicRocketCore.getInstance().getFileManager().createConfigs();
        }

        try {
            DynamicRocketCore.getInstance().setCommunicateServer(new CommunicationServer());
        } catch(InterruptedException ex) {
            ex.printStackTrace();
            new Logger(ex.getMessage(), LogLevel.ERROR).log();
        }

        DynamicRocketCore.getInstance().getConsole().sendMessage("Netty loaded.");

        DynamicRocketCore.getInstance().getCommandManager().registerCommand(new DeveloperCommand());
        DynamicRocketCore.getInstance().getCommandManager().registerCommand(new StopCommand());
        DynamicRocketCore.getInstance().getCommandManager().registerCommand(new CommandReload());
        DynamicRocketCore.getInstance().getCommandManager().registerCommand(new CreateServerGroupCommand());
        DynamicRocketCore.getInstance().getCommandManager().registerCommand(new HelpCommand());

        MessageBlock block = MessageBlock.load(new File("groups/Bungee.json"));

        DynamicRocketCore.getInstance().getServerManager().startProxy(new GroupConfiguration(block.getInteger("maxPlayers"), block.getInteger("maxram"), block.getString("name") + "-1", 1, 1, false));

        // END!
        DynamicRocketCore.getInstance().getConsole().sendMessage(DynamicRocketCore.getInstance().getCommandManager().getCommands().size() + " Commands registered.");
        new File("bungeestart.bat").delete();
        listen();

    }

    /**
     * This method starts at the end of {@link #main(String[])}. It reads the console input and execute commands.
     */
    private static void listen() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.length() == 0) return;

                if(!DynamicRocketCore.getInstance().getCommandManager().commandExists(line.split(" ")[0])) {
                    DynamicRocketCore.getInstance().getConsole().sendMessage("This command doesn't exists! Please type 'help' to get a list with commands.");
                }

                /*
                  Using for loop and not lambda, becaouse line must be final.
                 */
                for(RocketCommand command : DynamicRocketCore.getInstance().getCommandManager().getCommands()) {
                    if(command.commandName().equalsIgnoreCase(line.split(" ")[0])) {
                        String[] arguments = line.split(" ");
                        ArrayList<String> argList = new ArrayList<>();
                        for(int i = 0; i < line.split(" ").length; i++) {
                            argList.add(arguments[i]);
                        }
                        argList.remove(arguments[0]);
                        String str = "";
                        for(int i = 0; i < argList.size(); i++) {
                            if(i == argList.size() - 1) {
                                str = str + argList.get(i);
                            } else {
                                str = str + argList.get(i) + " ";
                            }
                        }
                        command.execute(str.split(" "));
                    }
                }
            }
        } catch (Exception ex) {}
    }
}
